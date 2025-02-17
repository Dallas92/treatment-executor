package com.medicus.treatment.repository;

import com.medicus.treatment.model.entity.TreatmentPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TreatmentPlanRepository extends JpaRepository<TreatmentPlan, Long> {

    String FIND_ALL_TREATMENT_PLANS_BETWEEN_TIME = """
            SELECT *
            FROM treatment_plans pl
            WHERE (:dateTime BETWEEN pl.start_time AND pl.end_time) OR (:dateTime >= pl.start_time AND pl.end_time IS NULL)
            ORDER BY pl.start_time
            """;

    @Query(nativeQuery = true, value = FIND_ALL_TREATMENT_PLANS_BETWEEN_TIME)
    List<TreatmentPlan> findAllTreatmentPlansBetweenTime(@Param("dateTime") LocalDateTime dateTime);
}
