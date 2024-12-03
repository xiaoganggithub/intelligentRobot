package com.robot.infrastructure.repository;

import com.robot.domain.model.Command;
import com.robot.domain.repository.CommandRepository;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

@Repository
public class CommandRepositoryImpl implements CommandRepository {
    
    private final RedisTemplate<String, Object> redisTemplate;
    private static final String COMMAND_KEY_PREFIX = "robot:command:";
    private static final long COMMAND_EXPIRE_TIME = 5; // 5分钟过期
    
    public CommandRepositoryImpl(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
    
    @Override
    public Command findByCommandTextAndTimestamp(String commandText, Long timestamp) {
        String key = generateKey(commandText, timestamp);
        return (Command) redisTemplate.opsForValue().get(key);
    }
    
    @Override
    public Command save(Command command) {
        String key = generateKey(command.getCommandText(), command.getTimestamp());
        redisTemplate.opsForValue().set(key, command, COMMAND_EXPIRE_TIME, TimeUnit.MINUTES);
        return command;
    }
    
    private String generateKey(String commandText, Long timestamp) {
        // 生成5分钟时间窗口的key
        long timeWindow = timestamp / (1000 * 60 * 5);
        return COMMAND_KEY_PREFIX + commandText + ":" + timeWindow;
    }
} 