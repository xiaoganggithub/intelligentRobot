package com.robot.infrastructure.redis;

import com.robot.domain.model.Command;
import com.robot.infrastructure.config.RobotProperties;
import com.robot.infrastructure.exception.ApiCallException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class CommandApiMapper {
    private final RedisTemplate<String, Object> redisTemplate;
    private final RobotProperties properties;

    public CommandApiMapper(RedisTemplate<String, Object> redisTemplate, RobotProperties properties) {
        this.redisTemplate = redisTemplate;
        this.properties = properties;
    }

    public CompletableFuture<Command> getCommand(String commandId) {
        try {
            String key = "command:" + commandId;
            Command command = (Command) redisTemplate.opsForValue().get(key);
            if (command == null) {
                log.warn("Command not found in cache: {}", commandId);
                CompletableFuture<Command> future = new CompletableFuture<>();
                future.completeExceptionally(new ApiCallException("Command not found"));
                return future;
            }
            return CompletableFuture.completedFuture(command);
        } catch (Exception e) {
            log.error("Error getting command from cache: {}", e.getMessage());
            CompletableFuture<Command> future = new CompletableFuture<>();
            future.completeExceptionally(e);
            return future;
        }
    }

    public CompletableFuture<Void> saveCommand(Command command) {
        try {
            String key = "command:" + command.getCommandId();
            redisTemplate.opsForValue().set(key, command, 5, TimeUnit.MINUTES);
            return CompletableFuture.completedFuture(null);
        } catch (Exception e) {
            log.error("Error saving command to cache: {}", e.getMessage());
            CompletableFuture<Void> future = new CompletableFuture<>();
            future.completeExceptionally(e);
            return future;
        }
    }

    public CompletableFuture<Void> deleteCommand(String commandId) {
        try {
            String key = "command:" + commandId;
            redisTemplate.delete(key);
            return CompletableFuture.completedFuture(null);
        } catch (Exception e) {
            log.error("Error deleting command from cache: {}", e.getMessage());
            CompletableFuture<Void> future = new CompletableFuture<>();
            future.completeExceptionally(e);
            return future;
        }
    }
} 