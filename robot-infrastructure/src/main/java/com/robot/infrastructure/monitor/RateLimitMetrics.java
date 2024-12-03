package com.robot.infrastructure.monitor;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Component
public class RateLimitMetrics {
    
    private final Counter successCounter;
    private final Counter blockedCounter;
    private final AtomicInteger currentQps = new AtomicInteger(0);
    
    public RateLimitMetrics(MeterRegistry registry) {
        this.successCounter = Counter.builder("robot_command_requests")
            .tag("status", "success")
            .description("Successful command requests")
            .register(registry);
            
        this.blockedCounter = Counter.builder("robot_command_requests")
            .tag("status", "blocked")
            .description("Blocked command requests")
            .register(registry);
            
        // 每秒重置QPS计数
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(1000);
                    currentQps.set(0);
                } catch (InterruptedException e) {
                    log.error("QPS reset thread interrupted", e);
                }
            }
        }).start();
    }
    
    public void recordSuccess() {
        successCounter.increment();
        currentQps.incrementAndGet();
    }
    
    public void recordBlocked() {
        blockedCounter.increment();
    }
    
    public int getCurrentQps() {
        return currentQps.get();
    }
} 