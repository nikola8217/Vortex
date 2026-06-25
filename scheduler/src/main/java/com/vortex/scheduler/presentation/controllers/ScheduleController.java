package com.vortex.scheduler.presentation.controllers;

import com.vortex.scheduler.business.responses.ScheduleResponse;
import com.vortex.scheduler.business.services.ScheduleService;
import com.vortex.scheduler.presentation.requests.ScheduleRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/schedules")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;

    @PostMapping
    public ResponseEntity<ScheduleResponse> createSchedule(@RequestBody ScheduleRequest request) {
        ScheduleResponse response = ScheduleResponse.from(scheduleService.createSchedule(request.format()));
        return ResponseEntity.status(201).body(response);
    }

    @GetMapping
    public ResponseEntity<List<ScheduleResponse>> getActiveSchedules() {
        List<ScheduleResponse> responses = scheduleService.getActiveSchedules()
                .stream()
                .map(ScheduleResponse::from)
                .toList();
        return ResponseEntity.ok(responses);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSchedule(@PathVariable UUID id) {
        scheduleService.deleteSchedule(id);
        return ResponseEntity.noContent().build();
    }
}