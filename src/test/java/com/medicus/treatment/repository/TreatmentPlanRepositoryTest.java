package com.medicus.treatment.repository;

import com.medicus.treatment.model.entity.TreatmentPlan;
import com.medicus.treatment.model.enums.TreatmentAction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@Transactional
class TreatmentPlanRepositoryTest {

    @Autowired
    private TreatmentPlanRepository treatmentPlanRepository;


    @BeforeEach
    void setUp() {
        treatmentPlanRepository.deleteAll();
    }

    @Test
    void testPast() {
        // Given
        TreatmentPlan treatmentPlan = new TreatmentPlan();
        treatmentPlan.setAction(TreatmentAction.ACTION_A);
        treatmentPlan.setPatient("Patient123");
        treatmentPlan.setStartTime(LocalDateTime.now().minusDays(10));
        treatmentPlan.setEndTime(LocalDateTime.now().minusDays(1));
        treatmentPlan.setRecurrence("0 8 * * *");
        treatmentPlanRepository.save(treatmentPlan);

        // When
        List<TreatmentPlan> foundPlans = treatmentPlanRepository.findAllTreatmentPlansBetweenTime(LocalDateTime.now());

        // Then
        assertThat(foundPlans).isEmpty();
    }

    @Test
    void testCurrent() {
        // Given
        TreatmentPlan planCurrent = new TreatmentPlan();
        planCurrent.setAction(TreatmentAction.ACTION_A);
        planCurrent.setPatient("Patient123");
        planCurrent.setStartTime(LocalDateTime.now().minusDays(1));
        planCurrent.setEndTime(LocalDateTime.now().plusDays(5));
        planCurrent.setRecurrence("0 8 * * *");
        treatmentPlanRepository.save(planCurrent);

        // When
        List<TreatmentPlan> foundPlans = treatmentPlanRepository.findAllTreatmentPlansBetweenTime(LocalDateTime.now());

        // Then
        assertThat(foundPlans).hasSize(1);
        assertThat(foundPlans.getFirst().getId()).isEqualTo(planCurrent.getId());
    }

    @Test
    void testCurrentWithInfiniteFuture() {
        // Given
        TreatmentPlan planCurrent = new TreatmentPlan();
        planCurrent.setAction(TreatmentAction.ACTION_A);
        planCurrent.setPatient("Patient123");
        planCurrent.setStartTime(LocalDateTime.now().minusDays(1));
        planCurrent.setEndTime(null);
        planCurrent.setRecurrence("0 8 * * *");
        treatmentPlanRepository.save(planCurrent);

        // When
        List<TreatmentPlan> foundPlans = treatmentPlanRepository.findAllTreatmentPlansBetweenTime(LocalDateTime.now());

        // Then
        assertThat(foundPlans).hasSize(1);
        assertThat(foundPlans.getFirst().getId()).isEqualTo(planCurrent.getId());
    }

    @Test
    void testFuture() {
        // Given
        TreatmentPlan treatmentPlan = new TreatmentPlan();
        treatmentPlan.setAction(TreatmentAction.ACTION_A);
        treatmentPlan.setPatient("Patient123");
        treatmentPlan.setStartTime(LocalDateTime.now().plusDays(1));
        treatmentPlan.setEndTime(LocalDateTime.now().plusDays(10));
        treatmentPlan.setRecurrence("0 8 * * *");
        treatmentPlanRepository.save(treatmentPlan);

        // When
        List<TreatmentPlan> foundPlans = treatmentPlanRepository.findAllTreatmentPlansBetweenTime(LocalDateTime.now());

        // Then
        assertThat(foundPlans).isEmpty();
    }
}
