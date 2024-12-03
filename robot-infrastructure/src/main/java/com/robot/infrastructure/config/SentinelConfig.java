package com.robot.infrastructure.config;

import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.alibaba.cloud.sentinel.annotation.SentinelRestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Configuration
public class SentinelConfig {
    private final RobotProperties properties;

    public SentinelConfig(RobotProperties properties) {
        this.properties = properties;
    }

    @Bean
    @SentinelRestTemplate
    public RestTemplate sentinelRestTemplate() {
        return new RestTemplate();
    }

    @PostConstruct
    public void init() {
        log.info("Initializing Sentinel flow rules with QPS: {}", properties.getRateLimit().getQps());
        List<FlowRule> rules = new ArrayList<>();
        
        FlowRule rule = new FlowRule();
        rule.setResource(properties.getRateLimit().getResourceName());
        rule.setGrade(RuleConstant.FLOW_GRADE_QPS);
        rule.setCount(properties.getRateLimit().getQps());
        rule.setControlBehavior(RuleConstant.CONTROL_BEHAVIOR_WARM_UP_RATE_LIMITER);
        rule.setWarmUpPeriodSec(properties.getRateLimit().getWarmUpPeriodSec());
        rule.setMaxQueueingTimeMs(properties.getRateLimit().getMaxQueueingTimeMs());
        
        rules.add(rule);
        FlowRuleManager.loadRules(rules);
    }
} 