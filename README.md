# CampusFlow

A starter Spring Boot web application for managing college assignments, internal exams, events, and group projects.

## Team
1. Ajeem Asan A
2. Syed Jafar A
3. Kaviyarasan
4. Vignesh
5. Sakthivel

## Technology
- Java 17
- Spring Boot 3.5
- Spring MVC + Thymeleaf
- Spring Data JPA
- My SQL data base
- HTML and CSS

## Features included
- Student dashboard with total, pending, and completed counts
- Add an assignment, exam, event, group project, or other item
- Set a due date and assignee
- Filter by category
- Mark items completed or pending
- Delete items
- Demo data on first launch
- Persistent local H2 database

## Run in IntelliJ IDEA or Eclipse
1. Install JDK 17 or newer and Maven.
2. Extract the ZIP.
3. Open the `CampusFlow` folder as an existing Maven project.
4. Wait for Maven dependencies to download.
5. Run `src/main/java/com/campusflow/CampusFlowApplication.java`.
6. Open http://localhost:8080 in your browser.

If using a terminal from the project folder, run:
- Windows: `mvnw.cmd spring-boot:run` (if a Maven wrapper is available), or `mvn spring-boot:run`
- macOS/Linux: `mvn spring-boot:run`

The app uses an H2 database file in `./data/`, so added items remain after restarting the app.

## H2 database console
Open http://localhost:8080/h2-console
- JDBC URL: `jdbc:h2:file:./data/campusflowdb`
- Username: `sa`
- Password: leave blank

## Suggested future improvements
- User registration/login and separate student accounts
- Email or in-app reminders
- Search and edit existing items
- Team member assignment and task progress
- MySQL configuration for deployment
- Role-based admin features

## Notes
This is a runnable academic starter project, not a production-secured multi-user deployment. It currently has no login or authorization. Do not expose it publicly with real student data before adding security.
