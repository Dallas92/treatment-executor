-- Create Treatment Plans Table
CREATE TABLE treatment_plans (
    id                 BIGINT AUTO_INCREMENT PRIMARY KEY,
    action             VARCHAR(50)  NOT NULL,
    patient            VARCHAR(255) NOT NULL,
    start_time         TIMESTAMP    NOT NULL,
    end_time           TIMESTAMP NULL,
    recurrence VARCHAR(255) NOT NULL
);

-- Create Treatment Tasks Table
CREATE TABLE treatment_tasks (
    id                BIGINT AUTO_INCREMENT PRIMARY KEY,
    action            VARCHAR(50)  NOT NULL,
    patient           VARCHAR(255) NOT NULL,
    start_time        TIMESTAMP    NOT NULL,
    status            VARCHAR(20)  NOT NULL,
    treatment_plan_id BIGINT       NOT NULL,
    FOREIGN KEY (treatment_plan_id) REFERENCES treatment_plans(id)
);
