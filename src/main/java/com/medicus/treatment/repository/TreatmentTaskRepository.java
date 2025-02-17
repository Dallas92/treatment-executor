package com.medicus.treatment.repository;

import com.medicus.treatment.model.entity.TreatmentTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface TreatmentTaskRepository extends JpaRepository<TreatmentTask, Long> {
    String EXISTS_BY_PARAMS = """
            SELECT EXISTS (
                SELECT 1
                FROM treatment_tasks tt
                where tt.treatment_plan_id = :treatmentPlanId
                AND tt.start_time = :startTime
            )
            """;

    @Query(nativeQuery = true, value = EXISTS_BY_PARAMS)
    Boolean existsByParams(@Param(value = "treatmentPlanId") Long treatmentPlanId,
                           @Param(value = "startTime") LocalDateTime startTime);
}
