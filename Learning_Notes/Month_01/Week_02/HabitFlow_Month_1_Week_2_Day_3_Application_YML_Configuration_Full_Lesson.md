# HabitFlow – Month 1, Week 2, Day 3
## Configuration: Configure `application.yml` and Externalize Database Credentials

**Official tracker task:** Configure `application.yml` and externalize database credentials  
**Suggested duration:** 60 minutes  
**Project:** HabitFlow – Habit Tracker  
**Prerequisites:** Week 2 Day 1 – JDBC, ORM, JPA and Hibernate; Week 2 Day 2 – Create the local `habit_tracker` database and configure the MySQL connection

---

## 1. Learning objectives

By the end of this lesson, you should be able to:

1. Explain the purpose of `application.yml`.
2. Locate the configuration file in a Spring Boot Maven project.
3. Understand Spring Boot datasource properties.
4. Add the MySQL connection URL, username and password configuration.
5. Externalize database credentials using environment variables.
6. Understand why credentials should not be hardcoded or committed to Git.
7. Run the application with the correct configuration and troubleshoot connection errors.

**Today's boundary:** Configure the Spring Boot connection to the database created on Day 2. Creating the `Habit` entity and repository belongs to later tracker tasks.

---

## 2. Why do we need `application.yml`?

Imagine writing database settings directly inside Java classes:

```java
String url = "jdbc:mysql://localhost:3306/habit_tracker";
String username = "root";
String password = "my-secret-password";
```

This is a poor approach because:

- Configuration becomes mixed with application logic.
- Changing the database environment requires code changes.
- Passwords can accidentally be committed to Git.
- Different environments may need different settings.

Spring Boot supports external configuration so that application settings can be managed separately from Java code.

For HabitFlow, we will keep the database URL in `application.yml` and obtain the username and password from environment variables.

---

## 3. What is `application.yml`?

`application.yml` is a Spring Boot configuration file. It uses YAML syntax to organize settings in a readable hierarchy.

It is normally located here:

```text
habitflow-backend/
└── src/
    └── main/
        └── resources/
            └── application.yml
```

If your project currently contains `application.properties`, you can either continue with that format or create `application.yml`. For this lesson, use `application.yml` as specified in the tracker. Avoid maintaining duplicate definitions for the same properties in both files.

### YAML basics

YAML uses indentation to represent nesting. Use spaces, not tabs.

Example:

```yaml
spring:
  application:
    name: habitflow-backend
```

This represents the nested properties:

- `spring.application.name`
- Value: `habitflow-backend`

The equivalent property in a `.properties` file would be:

```properties
spring.application.name=habitflow-backend
```

---

## 4. Understand Spring Boot datasource properties

A datasource describes how the application connects to a database.

The main settings are:

| Property | Purpose |
|---|---|
| `spring.datasource.url` | JDBC URL of the database |
| `spring.datasource.username` | MySQL account name |
| `spring.datasource.password` | MySQL account password |
| `spring.datasource.driver-class-name` | JDBC driver class (often detected automatically) |

For the local HabitFlow database, the example JDBC URL is:

```text
jdbc:mysql://localhost:3306/habit_tracker
```

Use the actual host and port from your MySQL installation if they differ.

### Example `application.yml`

Create or update:

`src/main/resources/application.yml`

```yaml
spring:
  application:
    name: habitflow-backend

  datasource:
    url: jdbc:mysql://localhost:3306/habit_tracker
    username: ${DB_USERNAME}
    password: ${DB_PASSWORD}
    driver-class-name: com.mysql.cj.jdbc.Driver
```

Important:

- `url` identifies the local MySQL database.
- `${DB_USERNAME}` tells Spring Boot to resolve the username from an external property or environment variable.
- `${DB_PASSWORD}` tells Spring Boot to resolve the password externally.
- `com.mysql.cj.jdbc.Driver` is the MySQL Connector/J driver class.

Do not replace the placeholders with your real password and then commit that file.

---

## 5. Check the Maven dependencies

The project needs the MySQL JDBC driver to communicate with MySQL.

If you generated the project with Spring Data JPA and the MySQL Driver selected in Spring Initializr, these dependencies may already be present.

A typical Maven dependency for Spring Data JPA is:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
```

A typical MySQL Connector/J dependency for a Spring Boot Maven project is:

```xml
<dependency>
    <groupId>com.mysql</groupId>
    <artifactId>mysql-connector-j</artifactId>
    <scope>runtime</scope>
</dependency>
```

Use the Spring Boot parent or dependency-management setup already generated for your project to manage compatible versions. Do not add arbitrary versions without checking your project's Spring Boot version.

### What do these dependencies do?

- `spring-boot-starter-data-jpa` provides Spring Data JPA integration and brings in the JPA/Hibernate persistence infrastructure.
- `mysql-connector-j` provides the JDBC driver used to communicate with MySQL.

After editing `pom.xml`, reload Maven in IntelliJ so it downloads or resolves the dependencies.

**Do not add duplicate dependencies** if the correct ones are already present.

---

## 6. What does externalizing credentials mean?

Externalizing credentials means keeping sensitive values outside the source code and supplying them when the application runs.

Instead of this:

```yaml
spring:
  datasource:
    username: root
    password: my-real-password
```

Use placeholders:

```yaml
spring:
  datasource:
    username: ${DB_USERNAME}
    password: ${DB_PASSWORD}
```

Then provide the real values through your local environment.

Benefits:

1. Passwords are not written directly into the tracked configuration.
2. Different environments can use different credentials.
3. Credentials can be changed without editing Java code.
4. The same application configuration structure can be used in development and deployment with environment-specific values.

**Important:** Environment variables are one way to supply configuration. They are not automatically a secret manager; protect the machine, account and deployment environment where they are stored.

---

## 7. Set environment variables on Windows

Choose the method that matches how you run your application.

### Method A – Run from PowerShell

Open PowerShell in the backend project directory.

Set the variables in the current PowerShell session:

```powershell
$env:DB_USERNAME = "root"
$env:DB_PASSWORD = "YOUR_LOCAL_MYSQL_PASSWORD"
```

Replace the example password with your own local MySQL password. Do not paste your real password into shared notes, screenshots or chat.

In the same PowerShell window, run:

```powershell
.\mvnw.cmd spring-boot:run
```

The environment variables set with `$env:` apply to that PowerShell session and processes launched from it. If you open another terminal, set them there too.

### Method B – Run from IntelliJ IDEA

If you normally start the application using IntelliJ:

1. Open **Run** → **Edit Configurations**.
2. Select your Spring Boot run configuration.
3. Find **Environment variables**.
4. Add:
   - `DB_USERNAME` = your local MySQL username
   - `DB_PASSWORD` = your local MySQL password
5. Apply the changes.
6. Run the application again.

The exact appearance of the Run Configuration dialog can vary by IntelliJ version.

Do not take screenshots that expose the password.

---

## 8. Understand the complete configuration

The configuration file contains placeholders:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/habit_tracker
    username: ${DB_USERNAME}
    password: ${DB_PASSWORD}
    driver-class-name: com.mysql.cj.jdbc.Driver
```

The environment supplies the actual values:

```text
DB_USERNAME = your local MySQL username
DB_PASSWORD = your local MySQL password
```

At startup, Spring Boot resolves the placeholders and configures the datasource.

Conceptual flow:

```text
Start HabitFlow
      |
      v
Spring Boot reads application.yml
      |
      v
Resolves DB_USERNAME and DB_PASSWORD
      |
      v
Configures the datasource
      |
      v
MySQL JDBC driver attempts connection
      |
      v
MySQL server / habit_tracker
```

This is the configuration flow. The actual database connection is established when the datasource obtains a connection.

---

## 9. Optional JPA setting – understand before changing

You may see JPA configuration in examples such as:

```yaml
spring:
  jpa:
    hibernate:
      ddl-auto: none
```

`ddl-auto` controls whether Hibernate performs schema-management actions.

Common values include:

| Value | General meaning |
|---|---|
| `none` | Do not manage the database schema |
| `validate` | Check whether the mapped schema is compatible |
| `update` | Attempt to update the schema to match entities |
| `create` | Create the schema on startup, replacing existing schema objects as applicable |
| `create-drop` | Create schema at startup and drop it when the session factory closes |

For today's task, do not add a schema-generation setting just because it appears in an example. We have not created the Habit entity yet. The key goal today is to configure the datasource securely.

When entities and schema management are introduced, we will make a deliberate choice. For production systems, schema migrations are generally preferable to relying on automatic schema updates.

---

## 10. Run and verify the application

Before running:

1. Confirm the MySQL server is running.
2. Confirm the `habit_tracker` database exists.
3. Confirm the MySQL JDBC dependency is present in `pom.xml`.
4. Confirm `application.yml` is under `src/main/resources`.
5. Confirm the environment variables are available to the process that starts Spring Boot.

Start the application from IntelliJ or PowerShell.

### What should you look for?

- The application starts without datasource configuration errors.
- There is no unresolved placeholder error for `DB_USERNAME` or `DB_PASSWORD`.
- There is no MySQL connection error such as `Communications link failure` or `Access denied`.

At this stage, there may not be a Habit entity or repository yet. That is expected. The task is to prepare and verify the database connection configuration, not to save a habit record today.

If the startup logs do not make it clear whether a connection was successfully obtained, do not assume success solely because the web server started. Review the datasource and Hibernate logs, or ask for help interpreting your console output.

---

## 11. Common errors and troubleshooting

| Error | Possible reason | What to check |
|---|---|---|
| Could not resolve placeholder `DB_USERNAME` | Environment variable is missing | Set it in the same terminal or IntelliJ run configuration |
| Could not resolve placeholder `DB_PASSWORD` | Environment variable is missing | Check the environment variable name and run configuration |
| `Access denied for user` | Wrong username/password or insufficient privileges | Verify the credentials in MySQL Workbench |
| `Communications link failure` | MySQL server stopped or wrong host/port | Check MySQL service, host and port |
| `Unknown database 'habit_tracker'` | Database does not exist on the connected server | Run `SHOW DATABASES;` in Workbench |
| No suitable driver / driver class not found | MySQL Connector/J missing or Maven not reloaded | Check `pom.xml` and reload Maven |
| YAML parsing error | Incorrect indentation or tabs | Use spaces and check the nesting |
| Application still uses old values | Wrong run configuration or stale process | Stop the application, check the active configuration and restart |

### YAML indentation example

Incorrect:

```yaml
spring:
application:
name: habitflow-backend
```

Correct:

```yaml
spring:
  application:
    name: habitflow-backend
```

YAML hierarchy depends on indentation.

---

## 12. Practical assignment

Complete these tasks yourself.

### Task 1 – Locate the configuration file

Find:

```text
src/main/resources/application.yml
```

If it does not exist, create it.

### Task 2 – Check dependencies

Open `pom.xml` and verify that the project has:

- Spring Data JPA starter.
- MySQL Connector/J driver.

Do not add duplicate dependencies.

### Task 3 – Configure the datasource

Add the following, adjusting the URL if your local MySQL host or port differs:

```yaml
spring:
  application:
    name: habitflow-backend

  datasource:
    url: jdbc:mysql://localhost:3306/habit_tracker
    username: ${DB_USERNAME}
    password: ${DB_PASSWORD}
    driver-class-name: com.mysql.cj.jdbc.Driver
```

### Task 4 – Set environment variables

Set `DB_USERNAME` and `DB_PASSWORD` in the PowerShell session or IntelliJ run configuration you use to start the application.

### Task 5 – Run the application

Start HabitFlow and inspect the logs for configuration, driver or connection errors.

### Task 6 – Explain in your own words

1. Why do we use `application.yml`?
2. What does `spring.datasource.url` represent?
3. What does `${DB_USERNAME}` mean?
4. Why should we not hardcode the database password?
5. Why must the MySQL Connector/J dependency be present?

---

## 13. Knowledge check – 10 MCQs

**Q1. Where is `application.yml` normally stored in a Spring Boot Maven project?**

A. `src/main/java`  
B. `src/main/resources`  
C. `src/test/java`  
D. `target/classes/java`

**Q2. Which property specifies the database JDBC URL?**

A. `spring.datasource.url`  
B. `spring.datasource.name`  
C. `spring.mysql.path`  
D. `server.database.url`

**Q3. What does `${DB_USERNAME}` represent?**

A. A Java class  
B. A placeholder resolved from configuration, commonly an environment variable  
C. A MySQL table  
D. A Maven plugin

**Q4. Which dependency provides the MySQL JDBC driver?**

A. `spring-boot-starter-web`  
B. `mysql-connector-j`  
C. `spring-boot-starter-test`  
D. `spring-boot-starter-validation`

**Q5. Why should database passwords not be hardcoded in a committed configuration file?**

A. YAML cannot store strings  
B. It can expose sensitive credentials  
C. MySQL does not support passwords  
D. Spring Boot requires an empty password

**Q6. What does `localhost` mean in this JDBC URL?**

```text
jdbc:mysql://localhost:3306/habit_tracker
```

A. A remote cloud server  
B. The local machine  
C. The database username  
D. A table name

**Q7. Which Windows PowerShell syntax sets an environment variable for the current session?**

A. `$env:DB_USERNAME = "root"`  
B. `CREATE ENV DB_USERNAME`  
C. `SET DATABASE DB_USERNAME`  
D. `@Value("root")`

**Q8. What is important about YAML indentation?**

A. It is only decorative  
B. It defines the nested structure  
C. It must use tabs  
D. It replaces property names

**Q9. Where can environment variables be configured when launching through IntelliJ?**

A. In the Java class name  
B. In the Run Configuration  
C. In MySQL table properties  
D. In the Maven repository

**Q10. Does creating `application.yml` automatically create the `habits` table?**

A. Yes, always  
B. No, datasource configuration alone does not create the Habit entity table  
C. Yes, if the server port is 8080  
D. No, because Spring Boot cannot connect to MySQL

### MCQ answer key

| Question | Correct answer | Explanation |
|---|---|---|
| Q1 | B | Spring Boot normally loads application configuration from `src/main/resources`. |
| Q2 | A | `spring.datasource.url` specifies the JDBC connection URL. |
| Q3 | B | Spring resolves the placeholder from available configuration sources. |
| Q4 | B | MySQL Connector/J provides the JDBC driver. |
| Q5 | B | Hardcoded credentials can leak through source control or shared files. |
| Q6 | B | `localhost` refers to the local machine. |
| Q7 | A | `$env:` sets a process environment variable in PowerShell. |
| Q8 | B | YAML indentation represents hierarchy. |
| Q9 | B | IntelliJ run configurations can supply environment variables. |
| Q10 | B | A datasource configuration does not itself define a Habit entity or its table. |

---

## 14. Completion checklist

Mark each item only after you have completed it.

- [ ] I understand the purpose of `application.yml`.
- [ ] I located or created `src/main/resources/application.yml`.
- [ ] I checked the Spring Data JPA dependency.
- [ ] I checked the MySQL Connector/J dependency.
- [ ] I configured the JDBC URL for `habit_tracker`.
- [ ] I used placeholders for the username and password.
- [ ] I supplied the environment variables to the application process.
- [ ] I ran the Spring Boot application.
- [ ] I checked the logs for configuration and connection errors.
- [ ] I completed the practical assignment and MCQs.
- [ ] I saved my notes in `Main_Notes_Branch`.

---

## 15. Save your notes in GitHub

Keep notes in your existing `Main_Notes_Branch`.

Suggested path:

```text
learning-notes/
└── Month-1/
    └── Week-2/
        └── Day-3.md
```

Example commands:

```bash
git switch Main_Notes_Branch
git status
git add learning-notes/Month-1/Week-2/Day-3.md
git commit -m "Add HabitFlow Month 1 Week 2 Day 3 configuration notes"
git push origin Main_Notes_Branch
```

Check for uncommitted work before switching branches.

Never commit actual database passwords or other secrets.

---

## 16. Key takeaways

- `application.yml` stores Spring Boot configuration.
- `spring.datasource.url` identifies the MySQL database endpoint.
- The MySQL Connector/J dependency supplies the JDBC driver.
- `${DB_USERNAME}` and `${DB_PASSWORD}` are placeholders for external values.
- Environment variables can supply credentials at runtime.
- YAML indentation is significant.
- A datasource configuration does not itself create the Habit entity table.
- Do not commit passwords to GitHub.

---

## 17. Next scheduled lesson

**Month 1 – Week 2 – Day 4: Entity Mapping**

Create the `Habit` entity and understand `@Entity`, `@Table` and `@Id`.

---

## 18. Interview questions and sample answers

**Q1. What is `application.yml` in Spring Boot?**

`application.yml` is a configuration file used to define application settings such as the application name, server port, datasource properties and other Spring configuration.

**Q2. Where is `application.yml` normally placed?**

It is normally placed in `src/main/resources`, where Spring Boot can load it from the application classpath.

**Q3. What is a datasource in Spring Boot?**

A datasource is the configured source of database connections. It contains information such as the JDBC URL and credentials and is used by the application to obtain database connections.

**Q4. What does `spring.datasource.url` do?**

It specifies the JDBC URL that identifies the database server and database the application should connect to.

**Q5. What is MySQL Connector/J?**

MySQL Connector/J is the official JDBC driver that enables Java applications to communicate with MySQL databases.

**Q6. What does `${DB_PASSWORD}` mean in `application.yml`?**

It is a property placeholder. Spring resolves it from available configuration sources, commonly an environment variable named `DB_PASSWORD`.

**Q7. What does externalizing credentials mean?**

It means keeping sensitive credentials outside application source code and supplying them through an external configuration source, such as environment variables or a secrets-management system.

**Q8. Why should we avoid hardcoding database passwords?**

Hardcoded passwords can accidentally be committed to source control, shared with others or exposed in code reviews and logs. External configuration reduces that risk.

**Q9. How can you set an environment variable in PowerShell?**

For the current PowerShell session, use syntax such as `$env:DB_USERNAME = "root"`. A process started from that session can inherit the variable.

**Q10. Why is YAML indentation important?**

YAML uses indentation to represent nested data. Incorrect indentation can change the configuration structure or cause parsing errors.

**Q11. Does configuring a datasource create database tables automatically?**

No. Datasource configuration describes how the application connects to the database. Table creation depends on entity mappings and schema-management or migration configuration.

**Q12. What should you check if Spring Boot reports `Could not resolve placeholder 'DB_PASSWORD'`?**

Check that the environment variable is defined with the exact name and is available to the process running Spring Boot. If using IntelliJ, inspect the active Run Configuration. If using PowerShell, set the variable in the same terminal session before starting the application.

---

**End of Month 1 – Week 2 – Day 3.**
