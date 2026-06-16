package com.vortex.worker.business.handlers;

import com.vortex.shared.entities.Task;
import com.vortex.shared.enums.TaskType;

public interface TaskHandler {
    void handle(Task task);
    boolean supports(TaskType type);
}