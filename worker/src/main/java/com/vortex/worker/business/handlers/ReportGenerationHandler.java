package com.vortex.worker.business.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vortex.shared.entities.Task;
import com.vortex.shared.enums.TaskType;
import com.vortex.worker.business.ports.IDatabaseInstanceRepository;
import com.vortex.worker.business.ports.IProcessedMetricRepository;
import com.vortex.worker.business.ports.IReportRepository;
import com.vortex.worker.core.entities.DatabaseInstance;
import com.vortex.worker.core.entities.ProcessedMetric;
import com.vortex.worker.core.entities.Report;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReportGenerationHandler implements TaskHandler {

    private final IDatabaseInstanceRepository instanceRepository;
    private final IProcessedMetricRepository processedMetricRepository;
    private final IReportRepository reportRepository;
    private final ObjectMapper objectMapper;

    @Override
    public boolean supports(TaskType type) {
        return type == TaskType.REPORT_GENERATION;
    }

    @Override
    public void handle(Task task) {
        List<DatabaseInstance> instances = instanceRepository.findAll();
        Map<String, Object> reportContent = new LinkedHashMap<>();

        List<Map<String, Object>> instanceStats = new ArrayList<>();
        Map<String, Double> totalSlowQueries = new HashMap<>();

        for (DatabaseInstance instance : instances) {
            List<ProcessedMetric> metrics = processedMetricRepository.findByInstanceId(instance.getId());

            if (metrics.isEmpty()) continue;

            double avgQueryCount = metrics.stream().mapToDouble(ProcessedMetric::getAvgQueryCount).average().orElse(0);
            double avgSlowQueries = metrics.stream().mapToDouble(ProcessedMetric::getAvgSlowQueries).average().orElse(0);
            double avgConnections = metrics.stream().mapToDouble(ProcessedMetric::getAvgConnections).average().orElse(0);

            Map<String, Object> stat = new LinkedHashMap<>();
            stat.put("instanceName", instance.getName());
            stat.put("avgQueryCount", avgQueryCount);
            stat.put("avgSlowQueries", avgSlowQueries);
            stat.put("avgConnections", avgConnections);
            instanceStats.add(stat);

            totalSlowQueries.put(instance.getName(), avgSlowQueries);
        }

        List<String> top3 = totalSlowQueries.entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .limit(3)
                .map(Map.Entry::getKey)
                .toList();

        reportContent.put("instanceStats", instanceStats);
        reportContent.put("top3MostLoaded", top3);
        reportContent.put("generatedAt", java.time.LocalDateTime.now().toString());

        try {
            String content = objectMapper.writeValueAsString(reportContent);
            Report report = new Report(UUID.randomUUID(), "DAILY_SUMMARY", content);
            reportRepository.save(report);
            log.info("Daily report generated successfully");
        } catch (Exception e) {
            log.error("Failed to generate report", e);
        }
    }
}