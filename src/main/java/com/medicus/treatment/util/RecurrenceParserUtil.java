package com.medicus.treatment.util;

import com.cronutils.model.Cron;
import com.cronutils.model.CronType;
import com.cronutils.model.definition.CronDefinitionBuilder;
import com.cronutils.model.time.ExecutionTime;
import com.cronutils.parser.CronParser;
import lombok.experimental.UtilityClass;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class RecurrenceParserUtil {
    private final CronParser cronParser = new CronParser(CronDefinitionBuilder.instanceDefinitionFor(CronType.UNIX));

    public List<LocalDateTime> parseRecurrence(String recurrence) {
        List<LocalDateTime> executionTimes = new ArrayList<>();

        ZonedDateTime today = LocalDateTime.now().atZone(ZoneId.systemDefault());
        LocalDateTime startOfDay = today.toLocalDate().atStartOfDay();
        LocalDateTime endOfDay = today.toLocalDate().atTime(23, 59, 59);

        String[] cronExpressions = recurrence.split(" and ");
        for (var cronExpression : cronExpressions) {
            Cron cron = cronParser.parse(cronExpression);
            ExecutionTime executionTime = ExecutionTime.forCron(cron);

            ZonedDateTime nextExecution = executionTime.nextExecution(startOfDay.atZone(ZoneId.systemDefault())).orElse(null);
            while (nextExecution != null
                    && nextExecution.isBefore(endOfDay.atZone(ZoneId.systemDefault()))) {
                if (nextExecution.isAfter(today)) {
                    executionTimes.add(nextExecution.toLocalDateTime());
                }
                nextExecution = executionTime.nextExecution(ZonedDateTime.from(nextExecution)).orElse(null);
            }
        }

        return executionTimes.stream().distinct().toList();
    }
}
