# Treatment application

This is a Spring Boot application which every minute tries to get treatment plans from DB 
and create new unique treatment tasks based on recurrence (every hour, 2 times a day, 3 times a week and etc). <br/>

Treatment tasks are not created ahead of time, they are only created for the day when 
executor service was launched (there is a check for today date). 
When the next day comes then new treatment tasks for that day will be created

## Requirements

- Java 21 or higher

## Technologies
- Java 21
- Spring Boot 3.x.x
- Maven
- H2
- Liquibase
- Lombok

## Running the Application

To run the application, just start application. Some initial data will be created at start up.
To check different cases just manipulate with liquibase insert script db/changelog/002-insert-data.sql

## Database

### H2

Use H2 Console web application http://localhost:8080/h2-console to manage data

### Liquibase

DB schema and initial data are created using Liquibase migration scripts

## Recurrence configuration

To set more than 1 times occurrence plz use ' AND ' separator

```
0 13 * * 1 and 0 15 * * 3
```
