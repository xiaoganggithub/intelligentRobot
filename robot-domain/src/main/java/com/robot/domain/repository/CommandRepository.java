package com.robot.domain.repository;

import com.robot.domain.model.Command;

public interface CommandRepository {
    Command findByCommandTextAndTimestamp(String commandText, Long timestamp);
    Command save(Command command);
} 