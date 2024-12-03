package com.robot.application.handler;

import com.robot.domain.model.Command;
import com.robot.domain.service.CommandProcessService;
import com.robot.infrastructure.http.ApiClient;
import com.robot.infrastructure.redis.CommandApiMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
public class CommandHandler {
    private final CommandProcessService commandProcessService;
    private final CommandApiMapper commandApiMapper;
    private final ApiClient apiClient;
    
    public CommandHandler(
        CommandProcessService commandProcessService,
        CommandApiMapper commandApiMapper,
        ApiClient apiClient
    ) {
        this.commandProcessService = commandProcessService;
        this.commandApiMapper = commandApiMapper;
        this.apiClient = apiClient;
    }
    
    public CompletableFuture<String> handleCommand(String commandText) {
        String commandId = UUID.randomUUID().toString();
        Command command = new Command(commandId, commandText);
        
        return commandApiMapper.saveCommand(command)
            .thenCompose(v -> {
                String response = apiClient.sendCommand(commandText);
                command.setResponse(response);
                return commandApiMapper.saveCommand(command)
                    .thenApply(v2 -> response);
            })
            .exceptionally(e -> {
                log.error("Error handling command: {}", e.getMessage());
                return "Error processing command: " + e.getMessage();
            });
    }
    
    public CompletableFuture<String> getCommandStatus(String commandId) {
        return commandApiMapper.getCommand(commandId)
            .thenApply(command -> {
                if (command.getResponse() != null) {
                    return command.getResponse();
                }
                return "Command is still processing";
            })
            .exceptionally(e -> {
                log.error("Error getting command status: {}", e.getMessage());
                return "Error getting command status: " + e.getMessage();
            });
    }
} 