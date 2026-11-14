# HabitFlow – Month 1, Week 1, Day 5
## Spring Boot: Main Application Class, Auto-configuration and Application Startup

**Official tracker task:** Understand main application class, auto-configuration and application startup  
**Suggested duration:** 60 minutes  
**Level:** Beginner  
**Project:** HabitFlow – Habit Tracker

---

## 1. Learning objectives

By the end of this lesson, you should be able to:

1. Explain the purpose of the Spring Boot main application class.
2. Explain the Java `main()` method and `SpringApplication.run()`.
3. Describe the three annotations combined by `@SpringBootApplication`.
4. Explain Spring Boot auto-configuration.
5. Understand component scanning and package placement.
6. Explain the purpose of the Spring Application Context.
7. Describe the basic Spring Boot startup sequence.
8. Run HabitFlow and identify important startup log messages.

## 2. What is the main application class?

When you create a Spring Boot project, Spring Initializr generates a Java class similar to this:

```java
package com.habitflow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class HabitflowBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(HabitflowBackendApplication.class, args);
    }

}
```

This is called the **main application class** or entry-point class.

In a standard Java application, execution starts from the `main()` method. Spring Boot applications also start from a `main()` method, but this method delegates the startup process to Spring Boot.

### 2.1 Package declaration

```java
package com.habitflow;
```

This declares the package containing the class. Packages help organize Java classes. Our main application class is in the root package `com.habitflow`.

### 2.2 Import statements

```java
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
```

- `SpringApplication` provides the mechanism to launch a Spring application.
- `SpringBootApplication` is the annotation that enables important Spring Boot configuration features.

### 2.3 Class declaration

```java
public class HabitflowBackendApplication {
```

This declares the main class of our HabitFlow backend.

### 2.4 Main method

```java
public static void main(String[] args) {
```

This is the standard Java entry point.

- `public`: The method can be accessed from outside the class.
- `static`: Java can invoke it without creating an object of the class.
- `void`: The method does not return a value.
- `String[] args`: Receives command-line arguments.

### 2.5 SpringApplication.run()

```java
SpringApplication.run(HabitflowBackendApplication.class, args);
```

This tells Spring Boot to start the application using the configuration associated with `HabitflowBackendApplication`.

It triggers the preparation of the Spring application context, configuration processing, bean creation and, for a web application, embedded web server startup.

## 3. What is `@SpringBootApplication`?

This is one of the most important annotations in Spring Boot.

```java
@SpringBootApplication
public class HabitflowBackendApplication {
}
```

It combines three important annotations:

```text
                 @SpringBootApplication
                           |
          +----------------+----------------+
          |                |                |
@SpringBootConfiguration  @EnableAutoConfiguration  @ComponentScan
```

### 3.1 `@SpringBootConfiguration`

It marks the class as a source of Spring configuration. It is a specialized form of `@Configuration`.

A Spring configuration class can define beans and other application configuration.

### 3.2 `@EnableAutoConfiguration`

It enables Spring Boot's auto-configuration mechanism.

For example, when Spring Boot detects the required web dependencies, it can configure web application infrastructure and an embedded server.

When database dependencies and suitable configuration are present, it can configure database-related infrastructure too.

**Important:** Auto-configuration does not automatically create your HabitFlow business logic, entities, database tables or APIs. You still write those.

### 3.3 `@ComponentScan`

It tells Spring where to look for classes that should become Spring-managed components.

Common component annotations include:

| Annotation | Typical purpose |
|---|---|
| `@Component` | General Spring-managed component |
| `@Service` | Service/business logic layer |
| `@Repository` | Persistence layer |
| `@Controller` | MVC controller |
| `@RestController` | REST API controller |

These annotations make classes eligible for component scanning.

## 4. What is Spring Boot auto-configuration?

Imagine building a web application without Spring Boot. You may need to configure many things manually, such as web application infrastructure, request handling, JSON conversion, embedded server configuration and database connectivity infrastructure.

Spring Boot provides auto-configuration to reduce this manual setup.

**Definition:** Auto-configuration is a Spring Boot feature that attempts to configure application infrastructure based on dependencies available on the classpath, existing beans and configuration properties.

### Example: Spring Web dependency

Your `pom.xml` may contain:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
```

This starter brings in web-related dependencies.

When Spring Boot starts, it can detect relevant classes and configure a web application, including an embedded server.

That is why we can create a controller and run the application without manually configuring every web component.

### 4.1 Conceptual auto-configuration flow

```text
Dependencies in pom.xml
       |
       v
Spring Boot examines classpath and configuration
       |
       v
Conditional auto-configuration is evaluated
       |
       v
Required application infrastructure is configured
```

### 4.2 What does conditional mean?

Auto-configuration is not blindly applied. Spring Boot uses conditions to decide whether a configuration should be applied.

For example, a configuration may apply only when:

- A particular class is available.
- A particular bean is missing.
- A configuration property has a specific value.

This lets Spring Boot provide defaults while still allowing customization.

## 5. What is component scanning?

Component scanning is how Spring discovers eligible classes and registers them as beans.

Consider this project structure:

```text
habitflow-backend/
└── src/
    └── main/
        └── java/
            └── com/
                └── habitflow/
                    ├── HabitflowBackendApplication.java
                    ├── controller/
                    │   └── HelloController.java
                    ├── service/
                    │   └── HabitService.java
                    └── repository/
                        └── HabitRepository.java
```

Because the main application class is in `com.habitflow`, component scanning includes its subpackages by default.

That means Spring can discover components in:

- `com.habitflow.controller`
- `com.habitflow.service`
- `com.habitflow.repository`

### Important package rule

Keep your main application class in the root package:

```text
com.habitflow
```

Avoid placing it inside a subpackage such as:

```text
com.habitflow.controller
```

Otherwise, the default component scan may not discover sibling packages such as `com.habitflow.service`.

## 6. What is a Spring Application Context?

The Spring Application Context is a container that manages Spring beans and provides application infrastructure.

A bean is an object managed by the Spring container.

Example:

```java
@Component
public class GreetingService {

    public String greet() {
        return "Welcome to HabitFlow!";
    }
}
```

Spring can discover this class and create a managed bean.

The application context manages its lifecycle and can provide it to other components through dependency injection.

You will study beans and dependency injection in detail on **Week 1, Day 6**.

For today, remember:

- Spring discovers eligible components.
- Spring creates and manages their beans.
- The application context is the container that manages those beans.

## 7. What happens when a Spring Boot application starts?

When you run:

```java
SpringApplication.run(HabitflowBackendApplication.class, args);
```

Spring Boot performs a sequence of startup operations. The exact internal sequence has additional details, but this is a useful conceptual view.

1. **Java invokes the main method.** Execution begins in `HabitflowBackendApplication`.
2. **`SpringApplication.run()` is invoked.** Spring Boot begins preparing the application.
3. **The application environment is prepared.** Spring Boot reads configuration, profiles and relevant properties.
4. **The application context is created.** The Spring container is prepared.
5. **Configuration and component scanning happen.** Spring processes configuration classes and discovers eligible components.
6. **Beans are created and initialized.** Spring creates and manages required application objects.
7. **The embedded web server starts.** For a servlet-based web application, the embedded server starts and listens on its configured port, commonly `8080`.
8. **The application becomes ready.** It can now receive HTTP requests.

Simplified flow:

```text
main()
  |
  v
SpringApplication.run()
  |
  v
Prepare environment and application context
  |
  v
Process configuration and discover components
  |
  v
Create and initialize beans
  |
  v
Start embedded web server (web application)
  |
  v
Application ready
```

## 8. Practical exercise – inspect your HabitFlow application

### Step 1: Open the main application class

In IntelliJ IDEA, navigate to:

```text
src/main/java/com/habitflow/
```

Open:

```text
HabitflowBackendApplication.java
```

Your class should look similar to:

```java
package com.habitflow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class HabitflowBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(HabitflowBackendApplication.class, args);
    }

}
```

### Step 2: Identify the three important parts

Write down:

1. Which annotation enables auto-configuration?
2. Which annotation enables component scanning?
3. Which statement starts the Spring Boot application?

### Step 3: Run the application

Use IntelliJ's Run button or the Maven Wrapper.

Windows PowerShell:

```powershell
.\mvnw.cmd spring-boot:run
```

Wait for the application startup logs.

Look for messages similar to:

```text
Tomcat started on port 8080 (http)
Started HabitflowBackendApplication
```

Exact wording can differ by Spring Boot version and logging configuration.

### Step 4: Understand the port

Open:

```text
http://localhost:8080/
```

If your welcome controller exists, you should see its welcome response.

### Step 5: Inspect startup logs

Find and understand:

- The Spring Boot startup banner.
- The Spring Boot version.
- The embedded server port.
- The application started message.

Do not worry if your logs look slightly different. Focus on the meaning of each message.

## 9. Common startup problems

| Error or symptom | Possible reason | What to check |
|---|---|---|
| Port 8080 already in use | Another application uses that port | Stop the other process or configure another port |
| Application fails during bean creation | A bean or its dependency cannot be created | Read the first meaningful `Caused by` section |
| Controller returns 404 | Wrong URL or controller not discovered | Check package placement and mapping |
| Database connection failure | Database configuration or MySQL service problem | Check connection settings and MySQL availability |
| Application starts but endpoint is unavailable | Wrong port, path or server startup issue | Inspect startup logs and test the correct URL |

**Debugging tip:** Do not focus only on the last line of a long stack trace. Find the first relevant `Caused by` message and follow the exception chain.

## 10. Knowledge check – 8 questions

Try answering these before checking the explanations.

**Q1. Which method is the entry point of a standard Java application?**

A. `start()`  
B. `main()`  
C. `run()`  
D. `execute()`

**Q2. What is the purpose of `SpringApplication.run()`?**

A. Create a MySQL database  
B. Start the Spring Boot application  
C. Compile Java source code  
D. Generate a Maven project

**Q3. How many major annotations are combined in `@SpringBootApplication`?**

A. Two  
B. Three  
C. Four  
D. Five

**Q4. Which annotation enables Spring Boot auto-configuration?**

A. `@ComponentScan`  
B. `@Service`  
C. `@EnableAutoConfiguration`  
D. `@RestController`

**Q5. What does `@ComponentScan` do?**

A. Creates SQL tables  
B. Discovers eligible Spring components  
C. Starts MySQL  
D. Compiles Java classes

**Q6. Where should the main application class normally be located?**

A. Inside the controller package  
B. Inside the repository package  
C. In the root package  
D. Outside the project

**Q7. What is the Spring Application Context?**

A. A database table  
B. A container that manages Spring beans  
C. A Maven plugin  
D. An HTTP method

**Q8. What usually starts on port 8080 in a Spring Boot web application?**

A. MySQL server  
B. React development server  
C. Embedded web server  
D. Maven repository

### MCQ answer key

| Question | Correct answer | Explanation |
|---|---|---|
| Q1 | B | The JVM starts a standard Java application by invoking its main method. |
| Q2 | B | It launches Spring Boot and prepares the application context and infrastructure. |
| Q3 | B | It combines `@SpringBootConfiguration`, `@EnableAutoConfiguration` and `@ComponentScan`. |
| Q4 | C | `@EnableAutoConfiguration` enables conditional auto-configuration. |
| Q5 | B | It scans configured packages for eligible components. |
| Q6 | C | The root package allows component scanning to include its subpackages by default. |
| Q7 | B | The context manages beans and provides Spring application infrastructure. |
| Q8 | C | A servlet-based Spring Boot web application commonly starts an embedded server such as Tomcat. |

## 11. Day 5 completion checklist

Mark each item only after completing it.

- [ ] I understand the main application class.
- [ ] I can explain `main()` and `SpringApplication.run()`.
- [ ] I understand the three annotations combined in `@SpringBootApplication`.
- [ ] I can explain auto-configuration in my own words.
- [ ] I understand component scanning and why the main class belongs in the root package.
- [ ] I understand what the Spring Application Context does.
- [ ] I can describe the basic Spring Boot startup sequence.
- [ ] I ran my HabitFlow backend and inspected the startup logs.
- [ ] I completed the quiz and practical exercise.

## 12. Save your notes in GitHub

Save this complete lesson in your notes branch:

```text
learning-notes/
└── Month-1/
    └── Week-1/
        └── Day-5.md
```

Suggested Git commands:

```bash
git switch Main_Notes_Branch
git status
git add learning-notes/Month-1/Week-1/Day-5.md
git commit -m "Add HabitFlow Week 1 Day 5 Spring Boot startup lesson"
git push origin Main_Notes_Branch
```

Keep application code in `Main_Code_Branch` and learning notes in `Main_Notes_Branch`.

## 13. Key takeaways

- The main application class is the entry point of a Spring Boot application.
- The Java `main()` method invokes `SpringApplication.run()`.
- `@SpringBootApplication` combines `@SpringBootConfiguration`, `@EnableAutoConfiguration` and `@ComponentScan`.
- Auto-configuration provides suitable application infrastructure based on dependencies and configuration.
- Component scanning discovers eligible Spring-managed components.
- The Application Context manages Spring beans.
- A web application commonly starts an embedded server and then becomes ready to receive requests.

## 14. Next lesson – Week 1, Day 6

**Topic: Spring Core – IoC, Spring Beans and Dependency Injection**

You will learn how Spring creates objects, manages their lifecycle and supplies dependencies to other classes. This prepares you for the Controller–Service–Repository structure later in Month 1.

---

**Learning rule:** Do not just memorize annotations. Practise explaining what happens when you click Run in IntelliJ and how Spring Boot prepares HabitFlow to receive requests.


## 15. Interview questions – Day 5

Use these questions to prepare for Java/Spring Boot interviews. First answer each question aloud without looking at the sample answer, then compare.

### Q1. What is the purpose of the main application class in Spring Boot?

**Sample answer:**  
The main application class is the entry point of a Spring Boot application. It contains the Java `main()` method, which calls `SpringApplication.run()` to bootstrap the application, prepare the Spring Application Context, process configuration and start the embedded web server for a web application.

### Q2. What does `SpringApplication.run()` do?

**Sample answer:**  
`SpringApplication.run()` bootstraps and runs a Spring application. It prepares the environment, creates and refreshes the Application Context, processes configuration, initializes beans and starts the embedded web server when the application is a web application.

### Q3. What is `@SpringBootApplication`?

**Sample answer:**  
`@SpringBootApplication` is a convenience annotation commonly placed on the main application class. It combines `@SpringBootConfiguration`, `@EnableAutoConfiguration` and `@ComponentScan`.

### Q4. Which three annotations are combined inside `@SpringBootApplication`?

**Sample answer:**

1. `@SpringBootConfiguration` – identifies the class as a source of Spring configuration.
2. `@EnableAutoConfiguration` – enables Spring Boot's conditional auto-configuration.
3. `@ComponentScan` – scans packages for eligible Spring components.

### Q5. What is Spring Boot auto-configuration?

**Sample answer:**  
Auto-configuration is a Spring Boot feature that attempts to configure application infrastructure based on dependencies available on the classpath, existing beans and configuration properties. It reduces the amount of manual configuration developers need to write.

### Q6. How does Spring Boot decide which auto-configurations to apply?

**Sample answer:**  
Auto-configuration uses conditional checks. A configuration may be applied when required classes are present, a particular bean is missing, or a configuration property has a suitable value. Developers can customize or override defaults when necessary.

### Q7. What is component scanning?

**Sample answer:**  
Component scanning is the process by which Spring searches configured packages for eligible classes, such as classes annotated with `@Component`, `@Service`, `@Repository`, `@Controller` or `@RestController`, and registers them as Spring-managed beans.

### Q8. Why should the main application class usually be placed in the root package?

**Sample answer:**  
By default, component scanning starts from the package containing the main application class and includes its subpackages. Placing the main class in the root package, such as `com.habitflow`, allows Spring to discover components in packages such as `com.habitflow.controller`, `com.habitflow.service` and `com.habitflow.repository`.

### Q9. What is the Spring Application Context?

**Sample answer:**  
The Spring Application Context is a container that manages Spring beans and provides application infrastructure. It creates and manages bean instances, handles their lifecycle and makes them available for dependency injection.

### Q10. What happens at a high level when a Spring Boot web application starts?

**Sample answer:**  
The JVM invokes the `main()` method. The method calls `SpringApplication.run()`. Spring Boot prepares the environment and Application Context, processes configuration and component scanning, creates and initializes beans, starts the embedded web server and makes the application ready to receive requests. The exact internal startup sequence contains additional steps.

### Q11. What is the difference between Spring Framework configuration and Spring Boot auto-configuration?

**Sample answer:**  
Spring Framework provides the core container and configuration capabilities. Spring Boot builds on Spring Framework and provides opinionated defaults and conditional auto-configuration to reduce manual setup. Developers can still customize the configuration.

### Q12. Does auto-configuration mean that Spring Boot writes all application code automatically?

**Sample answer:**  
No. Auto-configuration sets up suitable application infrastructure based on dependencies and configuration. Developers still implement business logic, controllers, services, entities, repositories, API behavior and application-specific database design.

### Q13. What is an embedded server in Spring Boot?

**Sample answer:**  
An embedded server is a web server included in the application dependencies and launched as part of the application. For example, a servlet-based Spring Boot application using the web starter commonly runs with embedded Tomcat. This allows the application to start without manually deploying it to a separately installed application server.

### Q14. What would you check if a Spring component is not being discovered?

**Sample answer:**  
I would check whether the class has an eligible stereotype annotation, whether it is in a package included by component scanning, and whether the application configuration or component scan has been customized. I would also inspect startup logs for related errors.

### Interview practice task

Answer these three questions aloud in your own words:

1. Explain `@SpringBootApplication` and its three component annotations.
2. Explain what happens after you call `SpringApplication.run()`.
3. Explain how auto-configuration helps a Spring Boot developer.

**Interview preparation tip:** Do not memorize the sample answers word for word. Understand the idea and explain it clearly, using the HabitFlow project as your example.
