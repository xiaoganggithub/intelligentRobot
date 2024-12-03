package com.robot.domain.service;

import com.robot.domain.model.Command;
import com.robot.domain.repository.CommandRepository;
import org.springframework.stereotype.Service;

@Service
public class CommandProcessService {
    private final CommandRepository commandRepository;
    
    public CommandProcessService(CommandRepository commandRepository) {
        this.commandRepository = commandRepository;
    }
    
    public Command processCommand(Command command) {
        // 检查是否存在重复命令
        Command existingCommand = commandRepository.findByCommandTextAndTimestamp(
            command.getCommandText(), 
            command.getTimestamp()
        );
        
        if (existingCommand != null) {
            return existingCommand;
        }
        
        command.setStatus(Command.CommandStatus.PENDING);
        return commandRepository.save(command);
    }
} 