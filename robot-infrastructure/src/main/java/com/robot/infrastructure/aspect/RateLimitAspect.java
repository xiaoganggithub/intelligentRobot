package com.robot.infrastructure.aspect;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.flow.FlowException;
import com.alibaba.csp.sentinel.slots.system.SystemBlockException;
import com.robot.infrastructure.config.RobotProperties;
import com.robot.infrastructure.exception.RateLimitException;
import com.robot.infrastructure.model.RateLimitResponse;
import com.robot.infrastructure.monitor.RateLimitMetrics;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class RateLimitAspect {

    private final RobotProperties properties;
    private final RateLimitMetrics metrics;

    public RateLimitAspect(RobotProperties properties, RateLimitMetrics metrics) {
        this.properties = properties;
        this.metrics = metrics;
    }

    @Around("@annotation(com.robot.infrastructure.annotation.RateLimit)")
    public Object rateLimit(ProceedingJoinPoint point) throws Throwable {
        String resourceName = properties.getRateLimit().getResourceName();
        Entry entry = null;
        try {
            entry = SphU.entry(resourceName);
            Object result = point.proceed();
            metrics.recordSuccess();
            return result;
        } catch (BlockException e) {
            metrics.recordBlocked();
            log.warn("Rate limit exceeded for resource: {}", resourceName);
            throw handleBlockException(e);
        } finally {
            if (entry != null) {
                entry.exit();
            }
        }
    }

    private RateLimitException handleBlockException(BlockException e) {
        RateLimitResponse response = RateLimitResponse.builder()
            .code(429)
            .message("Rate limit exceeded")
            .resourceName(properties.getRateLimit().getResourceName())
            .limitQps(properties.getRateLimit().getQps())
            .waitingTime(properties.getRateLimit().getMaxWaitingTime())
            .build();

        if (e instanceof FlowException) {
            response = response.toBuilder()
                .message("Flow control limit exceeded")
                .waitingTime(properties.getRateLimit().getMaxQueueingTimeMs())
                .build();
        } else if (e instanceof SystemBlockException) {
            response = response.toBuilder()
                .message("System protection limit exceeded")
                .waitingTime(properties.getRateLimit().getMaxWaitingTime())
                .build();
        }

        return new RateLimitException(response);
    }
} 