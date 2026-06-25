package com.vortex.task.presentation.controllers;

import com.vortex.task.business.responses.TaskResponse;
import com.vortex.task.business.services.TaskService;
import com.vortex.task.presentation.requests.TaskRequest;
import com.vortex.shared.enums.TaskPriority;
import com.vortex.shared.enums.TaskStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    public ResponseEntity<TaskResponse> submitTask(@RequestBody TaskRequest request) {
        TaskResponse response = TaskResponse.from(taskService.submitTask(request.format()));
        return ResponseEntity.status(201).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> getTask(@PathVariable UUID id) {
        TaskResponse response = TaskResponse.from(taskService.getTask(id));
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<TaskResponse>> getTasks(
            @RequestParam(required = false) TaskStatus status,
            @RequestParam(required = false) TaskPriority priority) {
        List<TaskResponse> responses = taskService.getTasks(status, priority)
                .stream()
                .map(TaskResponse::from)
                .toList();
        return ResponseEntity.ok(responses);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelTask(@PathVariable UUID id) {
        taskService.cancelTask(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/retry")
    public ResponseEntity<TaskResponse> retryTask(@PathVariable UUID id) {
        TaskResponse response = TaskResponse.from(taskService.retryTask(id));
        return ResponseEntity.ok(response);
    }
}