package com.medicus.treatment.repository;

import com.medicus.treatment.model.entity.TreatmentPlan;
import com.medicus.treatment.model.entity.TreatmentTask;
import com.medicus.treatment.model.enums.TreatmentAction;
import com.medicus.treatment.model.enums.TreatmentTaskStatus;
import com.medicus.treatment.util.RecurrenceParserUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@Transactional
class TreatmentTaskRepositoryTest {

    @Autowired
    private TreatmentPlanRepository treatmentPlanRepository;

    @Autowired
    private TreatmentTaskRepository treatmentTaskRepository;

    @BeforeEach
    void setUp() {
        treatmentPlanRepository.deleteAll();
        treatmentTaskRepository.deleteAll();
    }

    @Test
    void testExistsByParams() {
        // Given
        TreatmentPlan plan = new TreatmentPlan();
        plan.setAction(TreatmentAction.ACTION_A);
        plan.setPatient("Patient123");
        plan.setStartTime(LocalDateTime.now().minusDays(1));
        plan.setEndTime(LocalDateTime.now().plusDays(5));
        plan.setRecurrence("0 8 * * *");
        treatmentPlanRepository.save(plan);

        var dateTimes = RecurrenceParserUtil.parseRecurrence(plan.getRecurrence());
        for (var dateTime : dateTimes) {
            TreatmentTask task = new TreatmentTask();
            task.setAction(plan.getAction());
            task.setPatient(plan.getPatient());
            task.setStartTime(dateTime);
            task.setStatus(TreatmentTaskStatus.ACTIVE);
            task.setTreatmentPlan(plan);
            treatmentTaskRepository.save(task);
        }

        // When
        for (var dateTime : dateTimes) {
            Boolean exists = treatmentTaskRepository.existsByParams(plan.getId(), dateTime);

            // Then
            assertThat(exists).isTrue();
        }
    }
}
