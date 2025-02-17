package com.medicus.treatment.service;

import com.medicus.treatment.model.entity.TreatmentPlan;
import com.medicus.treatment.model.entity.TreatmentTask;
import com.medicus.treatment.model.enums.TreatmentAction;
import com.medicus.treatment.model.enums.TreatmentTaskStatus;
import com.medicus.treatment.repository.TreatmentPlanRepository;
import com.medicus.treatment.repository.TreatmentTaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ActiveProfiles("test")
class TreatmentTaskExecutorServiceTest {

    private final TreatmentPlanRepository treatmentPlanRepository;
    private final TreatmentTaskRepository treatmentTaskRepository;
    private final TreatmentTaskExecutorService treatmentTaskExecutorService;

    @Autowired
    public TreatmentTaskExecutorServiceTest(
            TreatmentPlanRepository treatmentPlanRepository,
            TreatmentTaskRepository treatmentTaskRepository,
            TreatmentTaskExecutorService treatmentTaskExecutorService) {
        this.treatmentPlanRepository = treatmentPlanRepository;
        this.treatmentTaskRepository = treatmentTaskRepository;
        this.treatmentTaskExecutorService = treatmentTaskExecutorService;
    }

    @BeforeEach
    void setUp() {
        treatmentPlanRepository.deleteAll();
        treatmentTaskRepository.deleteAll();
    }

    @Test
    void testExecutorRun() {
        // Given
        TreatmentPlan treatmentPlan = new TreatmentPlan();
        treatmentPlan.setAction(TreatmentAction.ACTION_A);
        treatmentPlan.setPatient("Patient123");
        treatmentPlan.setStartTime(LocalDateTime.now().minusDays(1));
        treatmentPlan.setEndTime(LocalDateTime.now().plusDays(5));
        treatmentPlan.setRecurrence("* * * * *"); // every minute
        treatmentPlanRepository.save(treatmentPlan);

        // When
        treatmentTaskExecutorService.run();

        // Then
        List<TreatmentTask> treatmentTasks = treatmentTaskRepository.findAll();
        assertFalse(treatmentTasks.isEmpty());

        for (var treatmentTask : treatmentTasks) {
            assertEquals(treatmentPlan.getAction(), treatmentTask.getAction());
            assertEquals(treatmentPlan.getPatient(), treatmentTask.getPatient());
            assertEquals(TreatmentTaskStatus.ACTIVE, treatmentTask.getStatus());
            assertEquals(treatmentPlan.getId(), treatmentTask.getTreatmentPlan().getId());
            assertNotNull(treatmentTask.getStartTime());
        }
    }
}
