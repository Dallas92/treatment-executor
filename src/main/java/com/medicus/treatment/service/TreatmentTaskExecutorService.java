package com.medicus.treatment.service;

import com.medicus.treatment.model.entity.TreatmentPlan;
import com.medicus.treatment.model.entity.TreatmentTask;
import com.medicus.treatment.model.enums.TreatmentTaskStatus;
import com.medicus.treatment.repository.TreatmentPlanRepository;
import com.medicus.treatment.repository.TreatmentTaskRepository;
import com.medicus.treatment.util.RecurrenceParserUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class TreatmentTaskExecutorService {

    private final TreatmentPlanRepository treatmentPlanRepository;
    private final TreatmentTaskRepository treatmentTaskRepository;
    private final ScheduledExecutorService executorService;

    public TreatmentTaskExecutorService(
            TreatmentPlanRepository treatmentPlanRepository,
            TreatmentTaskRepository treatmentTaskRepository
    ) {
        this.treatmentPlanRepository = treatmentPlanRepository;
        this.treatmentTaskRepository = treatmentTaskRepository;
        this.executorService = Executors.newSingleThreadScheduledExecutor();
    }

    @EventListener(ApplicationReadyEvent.class)
    private void startScheduler() {
        log.info("Starting scheduler with 1 minute periodicity");
        executorService.scheduleAtFixedRate(this::run, 0, 1, TimeUnit.MINUTES);
    }

    public void run() {
        LocalDateTime now = LocalDateTime.now();

        log.info("Get treatment plans");
        List<TreatmentPlan> treatmentPlans = treatmentPlanRepository.findAllTreatmentPlansBetweenTime(now);
        log.info("Found %s treatment plan(s) to process".formatted(treatmentPlans.size()));

        for (var treatmentPlan : treatmentPlans) {
            processTreatmentPlan(treatmentPlan);
        }
    }

    private void processTreatmentPlan(TreatmentPlan treatmentPlan) {
        try {
            List<LocalDateTime> executionDateTimes = RecurrenceParserUtil.parseRecurrence(treatmentPlan.getRecurrence());
            for (var executionDateTime : executionDateTimes) {
                createTask(treatmentPlan, executionDateTime);
            }
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }
    }

    private void createTask(TreatmentPlan treatmentPlan, LocalDateTime executionDateTime) {
        var treatmentTaskExists = treatmentTaskRepository.existsByParams(treatmentPlan.getId(), executionDateTime);
        if (treatmentTaskExists) {
            return;
        }

        TreatmentTask treatmentTask = new TreatmentTask();
        treatmentTask.setAction(treatmentPlan.getAction());
        treatmentTask.setPatient(treatmentPlan.getPatient());
        treatmentTask.setStartTime(executionDateTime);
        treatmentTask.setStatus(TreatmentTaskStatus.ACTIVE);
        treatmentTask.setTreatmentPlan(treatmentPlan);
        treatmentTaskRepository.save(treatmentTask);
        log.info("Created scheduled task " + treatmentTask);
    }
}
