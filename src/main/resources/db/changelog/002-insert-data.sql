INSERT INTO treatment_plans (patient, action, start_time, end_time, recurrence)
VALUES
    -- Every hour (old)
    ('patient_123', 'ACTION_A', DATEADD('DAY', -10, NOW()), DATEADD('DAY', -1, NOW()), '0 * * * *'),
    -- Every 30 mins
    ('patient_123', 'ACTION_A', DATEADD('DAY', -1, NOW()), DATEADD('DAY', +10, NOW()), '*/30 * * * * '),
    -- Every hour
    ('patient_123', 'ACTION_A', DATEADD('DAY', -1, NOW()), DATEADD('DAY', +10, NOW()), '0 * * * *'),
    -- At 10:00 AM
    ('patient_123', 'ACTION_A', DATEADD('DAY', -1, NOW()), DATEADD('DAY', +10, NOW()), '0 10 * * *'),
    -- At 10:00 AM and 10:00 PM
    ('patient_123', 'ACTION_A', DATEADD('DAY', -1, NOW()), DATEADD('DAY', +10, NOW()), '0 10,22 * * *'),
    -- At 1:00 PM every Monday, 3:00 PM every Wednesday
    ('patient_123', 'ACTION_A', DATEADD('DAY', -1, NOW()), DATEADD('DAY', +10, NOW()), '0 13 * * 1 and 0 15 * * 3');
