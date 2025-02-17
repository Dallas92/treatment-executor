package com.medicus.treatment.model.entity;

import com.medicus.treatment.model.enums.TreatmentAction;
import com.medicus.treatment.model.enums.TreatmentTaskStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "treatment_tasks")
@Getter
@Setter
public class TreatmentTask {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TreatmentAction action;

    @Column(nullable = false)
    private String patient;

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TreatmentTaskStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "treatment_plan_id", nullable = false)
    private TreatmentPlan treatmentPlan;

    @Override
    public String toString() {
        return "TreatmentTask{" +
                "id=" + id +
                ", action=" + action +
                ", patient=" + patient +
                ", startTime=" + startTime +
                ", status=" + status +
                ", treatmentPlanId=" + treatmentPlan.getId() +
                '}';
    }
}
