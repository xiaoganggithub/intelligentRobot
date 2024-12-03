package com.robot.api.controller;

import com.robot.application.handler.CommandHandler;
import com.robot.api.service.HealthCheckService;
import com.robot.common.model.HealthCheckResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/robot")
public class RobotController {
    private final CommandHandler commandHandler;
    private final HealthCheckService healthCheckService;
    
    public RobotController(CommandHandler commandHandler, HealthCheckService healthCheckService) {
        this.commandHandler = commandHandler;
        this.healthCheckService = healthCheckService;
    }
    
    @PostMapping("/command")
    public Object processCommand(@RequestBody String command) {
        return commandHandler.handleCommand(command);
    }

    @GetMapping("/health")
    public HealthCheckResponse healthCheck() {
        return healthCheckService.checkHealth();
    }
} 