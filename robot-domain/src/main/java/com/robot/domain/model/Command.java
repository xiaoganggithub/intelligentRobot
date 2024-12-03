package com.robot.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Command {
    private String commandId;
    private String commandText;
    private String response;
    private Long timestamp;
    private CommandStatus status;

    public Command(String commandId, String commandText) {
        this.commandId = commandId;
        this.commandText = commandText;
        this.timestamp = System.currentTimeMillis();
        this.status = CommandStatus.PENDING;
    }

    public enum CommandStatus {
        PENDING,
        PROCESSING,
        COMPLETED,
        FAILED
    }
} 