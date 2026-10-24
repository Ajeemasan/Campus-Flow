# CampusFlow

CampusFlow is a Spring Boot web application designed to help college students manage assignments, internal exams, events, and group projects efficiently.

## Team Members

1. Ajeem Asan A
2. Syed Jafar A
3. Kaviyarasan
4. Vignesh
5. Sakthivel

## Technologies Used

- Java 17
- Spring Boot 3.5
- Spring MVC and Thymeleaf
- Spring Data JPA
- MySQL
- HTML and CSS
- Maven
- JavaMailSender for email notifications

## Features

- Student dashboard displaying total, pending, and completed tasks
- Create campus tasks for assignments, exams, events, group projects, and other activities
- Set task titles, descriptions, categories, due dates, and assignees
- Filter tasks by category
- Mark tasks as completed or pending
- Delete tasks
- User registration with email OTP verification
- Secure password-based login
- Automatic email reminders for incomplete tasks due the following day
- Persistent data storage using a relational database

## How to Run the Project

### Prerequisites

1. Install JDK 17 or newer.
2. Install Maven.
3. Install and configure MySQL.
4. Clone or download the CampusFlow repository.

### Run Using IntelliJ IDEA or Eclipse

1. Open the `CampusFlow` folder as an existing Maven project.
2. Wait for Maven dependencies to download.
3. Configure the required database and email environment variables.
4. Run `src/main/java/com/campusflow/CampusFlowApplication.java`.
5. Open [http://localhost:8080](http://localhost:8080) in your browser.

### Run Using the Terminal

From the project directory, run:

**Windows:**
```bash
mvn spring-boot:run
```

**macOS/Linux:**
```bash
mvn spring-boot:run
```

If Maven is not installed globally but the Maven wrapper is available, use `mvnw.cmd spring-boot:run` on Windows or `./mvnw spring-boot:run` on macOS/Linux.

## Email Configuration

CampusFlow uses SMTP to send email verification OTPs and campus task reminders.

Configure the following environment variables before running the application:

- `MAIL_USERNAME` — the email account used to send notifications
- `MAIL_PASSWORD` — the email account's SMTP app password

Never commit actual email passwords or database credentials to the repository.

## Automatic Task Reminders

CampusFlow checks for incomplete tasks due the following day and sends email reminders to the task owner's registered email address.

The current scheduler is configured to run daily at 9:00 AM server time. Reminders are based on the task's due date.

## Future Improvements

- In-app notifications and unread notification tracking
- Search and edit existing tasks
- Enhanced task progress tracking
- Team collaboration and task assignment
- Role-based access control
- Improved deployment and database management

## Project Purpose

CampusFlow is an academic project developed to simplify campus task management and help students keep track of important deadlines and activities.
