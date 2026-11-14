# HabitFlow – Month 1, Week 1, Day 7
## REST API: Create a Welcome Endpoint and Test It with Postman

**Official tracker tasks:**
1. Create a welcome endpoint using `@RestController` and `@GetMapping`.
2. Run the application and test the endpoint with Postman.

**Suggested duration:** 60 minutes  
**Level:** Beginner  
**Project:** HabitFlow – Habit Tracker

---

## 1. Learning objectives

By the end of this lesson, you should be able to:

1. Explain what a REST controller does.
2. Understand `@RestController` and `@GetMapping`.
3. Create a welcome endpoint in the HabitFlow backend.
4. Run the Spring Boot application.
5. Test the endpoint using Postman.
6. Inspect the HTTP response and status code.
7. Troubleshoot common endpoint testing errors.

## 2. What is a REST Controller?

A REST Controller is a Spring component that handles HTTP requests and returns responses, commonly in JSON or text format.

Spring Boot provides the `@RestController` annotation.

```java
@RestController
public class WelcomeController {
}
```

`@RestController` combines two important behaviors:

- `@Controller`: Marks the class as a Spring MVC controller.
- `@ResponseBody`: Indicates that method return values should be written directly to the HTTP response body.

This means we do not need to add `@ResponseBody` separately to every method.

## 3. What is `@GetMapping`?

`@GetMapping` maps an HTTP GET request to a Java method.

Example:

```java
@GetMapping("/welcome")
public String welcome() {
    return "Welcome to HabitFlow!";
}
```

When a client sends a GET request to `/welcome`, Spring invokes the `welcome()` method. The returned string becomes the HTTP response body.

```java
@GetMapping("/welcome")
```

- `@GetMapping`: Handles HTTP GET requests.
- `"/welcome"`: The URL path associated with the method.

### Request and response flow

```text
Postman
  |
  | GET http://localhost:8080/api/welcome
  v
Spring Boot request mapping
  |
  v
WelcomeController
  |
  v
welcome() method executes
  |
  v
HTTP response: 200 OK
  |
  v
Postman displays "Welcome to HabitFlow!"
```

## 4. Create your first Welcome REST API

Let's implement the exact task in your HabitFlow backend.

### Step 1 – Check your package structure

Your project should have a structure similar to this:

```text
src/main/java/com/habitflow/
│
├── HabitflowBackendApplication.java
│
├── controller/
│   └── WelcomeController.java
│
└── service/
    └── HabitService.java
```

If the `controller` package does not exist, create it under `com.habitflow`.

### Step 2 – Create `WelcomeController.java`

Create a Java class inside:

```text
com.habitflow.controller
```

Add the following code:

```java
package com.habitflow.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WelcomeController {

    @GetMapping("/api/welcome")
    public String welcome() {
        return "Welcome to HabitFlow!";
    }
}
```

### Step 3 – Understand every line

**Package declaration**

```java
package com.habitflow.controller;
```

Places the class in the controller package.

**Import statements**

```java
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
```

Imports the annotations required to create the REST endpoint.

**`@RestController`**

```java
@RestController
```

Marks this class as a REST controller. Spring discovers it through component scanning.

**Class declaration**

```java
public class WelcomeController {
```

Defines the controller class.

**`@GetMapping`**

```java
@GetMapping("/api/welcome")
```

Maps GET requests to the `/api/welcome` path.

**Method**

```java
public String welcome() {
    return "Welcome to HabitFlow!";
}
```

Returns the welcome message to the client.

### Step 4 – Understand the complete URL

```text
http://localhost:8080/api/welcome
```

| Part | Value | Meaning |
|---|---|---|
| Protocol | `http` | HTTP communication |
| Host | `localhost` | Your computer |
| Port | `8080` | Spring Boot application port |
| Path | `/api/welcome` | Controller endpoint |

## 5. Run the Spring Boot application

You can start the application in either of these ways.

### Option A – IntelliJ IDEA

1. Open `HabitflowBackendApplication.java`.
2. Click the green Run icon beside the `main()` method.
3. Wait for the application to start.
4. Check the Run console for successful startup.

### Option B – Maven Wrapper

Open the terminal in your backend project directory and run this command in Windows PowerShell:

```powershell
.\mvnw.cmd spring-boot:run
```

Wait until you see a startup message similar to:

```text
Tomcat started on port 8080 (http)
Started HabitflowBackendApplication
```

The exact message can vary with your Spring Boot version.

Your backend should now be listening on port `8080`.

## 6. Test your API using Postman

Postman is an API testing tool. It allows us to send HTTP requests and inspect server responses without building a frontend.

### Step 1 – Open Postman

Open the Postman desktop application.

### Step 2 – Create a new HTTP request

Create a new request or open a new tab.

### Step 3 – Select GET

Choose the HTTP method:

```text
GET
```

### Step 4 – Enter the URL

Enter:

```text
http://localhost:8080/api/welcome
```

### Step 5 – Click Send

Click the **Send** button.

### Step 6 – Check the response

Expected result:

- Status: `200 OK`
- Response body:

```text
Welcome to HabitFlow!
```

Congratulations! You have created and tested your first standalone HabitFlow welcome endpoint.

## 7. What happens internally?

Let's connect today's work with the concepts from Days 5 and 6.

1. You start the application using `SpringApplication.run()`.
2. Spring scans the `com.habitflow` package.
3. Spring discovers `WelcomeController` because of `@RestController`.
4. Spring registers the controller as a bean.
5. Postman sends a GET request to `/api/welcome`.
6. Spring finds the matching `@GetMapping`.
7. Spring invokes the `welcome()` method.
8. The returned string is written to the HTTP response body.
9. Postman displays the response.

Our welcome endpoint does not need a service or repository because it only returns a fixed message. Later, when we implement actual habit operations, we will use the controller, service and repository layers.

## 8. Common errors and troubleshooting

| Problem | Possible cause | What to check |
|---|---|---|
| Connection refused | Application is not running | Check IntelliJ console |
| 404 Not Found | Wrong URL or controller not discovered | Check `/api/welcome` and package structure |
| 405 Method Not Allowed | Wrong HTTP method | Select GET in Postman |
| 500 Internal Server Error | Unexpected application error | Read Spring Boot logs |
| Port already in use | Another application uses 8080 | Stop it or configure another port |

**Important:** A `404` and a connection-refused error are different. A 404 means a server responded but could not find a matching resource or handler. Connection refused usually means no server accepted the connection at that address and port.

## 9. Practical exercises

Complete these independently.

### Exercise 1 – Change the response message

Change:

```java
return "Welcome to HabitFlow!";
```

To:

```java
return "HabitFlow - Build Better Habits Every Day!";
```

Run the application again and test it in Postman.

### Exercise 2 – Create another endpoint

Add a second method inside `WelcomeController`:

```java
@GetMapping("/api/status")
public String status() {
    return "HabitFlow backend is running!";
}
```

Test:

```text
GET http://localhost:8080/api/status
```

Expected response:

```text
HabitFlow backend is running!
```

### Exercise 3 – Understand the difference

Explain:

1. What is the responsibility of `@RestController`?
2. What is the responsibility of `@GetMapping`?
3. What is the responsibility of the Java method?

## 10. Knowledge check – 8 MCQs

Try answering these before checking the answers.

**Q1. Which annotation marks a class as a REST controller?**

A. `@Service`  
B. `@Repository`  
C. `@RestController`  
D. `@Entity`

**Q2. Which annotation maps an HTTP GET request to a method?**

A. `@PostMapping`  
B. `@GetMapping`  
C. `@PutMapping`  
D. `@DeleteMapping`

**Q3. What is the URL of today's Welcome API?**

A. `http://localhost:3306/api/welcome`  
B. `http://localhost:8080/api/welcome`  
C. `http://localhost:5173/api/welcome`  
D. `http://localhost:8080/welcome/api`

**Q4. What is the expected status code for a successful GET request?**

A. 200  
B. 201  
C. 404  
D. 500

**Q5. Which tool are we using to test the endpoint?**

A. MySQL Workbench  
B. Postman  
C. Git Bash only  
D. Maven

**Q6. What happens when Postman sends GET `/api/welcome`?**

A. Spring invokes the matching welcome method  
B. MySQL creates a table  
C. Maven compiles the project  
D. The controller is deleted

**Q7. What does `@RestController` combine?**

A. `@Service` and `@Repository`  
B. `@Controller` and `@ResponseBody`  
C. `@Entity` and `@Table`  
D. `@Bean` and `@Autowired`

**Q8. What does a 404 response usually mean?**

A. The server is always stopped  
B. No matching resource or handler was found  
C. The request succeeded  
D. The database was created

### MCQ answer key

| Question | Correct answer | Explanation |
|---|---|---|
| Q1 | C | `@RestController` marks a class as a REST controller and writes method return values to the response body by default. |
| Q2 | B | `@GetMapping` maps HTTP GET requests to a handler method. |
| Q3 | B | The Spring Boot backend uses port 8080 in our local setup, and the endpoint path is `/api/welcome`. |
| Q4 | A | 200 OK is the expected status for this successful GET endpoint. |
| Q5 | B | Postman sends HTTP requests and displays responses. |
| Q6 | A | Spring matches the request path and method to the handler method. |
| Q7 | B | `@RestController` is a convenience annotation combining `@Controller` and `@ResponseBody`. |
| Q8 | B | 404 means the server could not find the requested resource or matching handler. |

## 11. Interview questions – Day 7

Try answering these aloud before reading the sample answers.

### Q1. What is `@RestController` in Spring Boot?

**Sample answer:**  
`@RestController` is a Spring annotation used to create REST controllers. It combines `@Controller` and `@ResponseBody`, so method return values are written directly to the HTTP response body.

### Q2. What is the difference between `@Controller` and `@RestController`?

**Sample answer:**  
`@Controller` is commonly used in Spring MVC applications where methods may return view names. `@RestController` is used for REST APIs and writes method return values to the response body by default.

### Q3. What is `@GetMapping`?

**Sample answer:**  
`@GetMapping` is a composed Spring MVC annotation that maps HTTP GET requests to a controller method for a specified path.

### Q4. What happens when a client sends a GET request to `/api/welcome`?

**Sample answer:**  
Spring's request mapping infrastructure finds a controller method whose HTTP method and path match the request. Spring invokes that method and writes its return value to the HTTP response body.

### Q5. What is the role of Postman in backend development?

**Sample answer:**  
Postman is an API client used to send HTTP requests and inspect responses. Developers can test endpoints, HTTP methods, headers, request bodies, status codes and response data without first building a frontend.

### Q6. What is the difference between a 404 error and connection refused?

**Sample answer:**  
A 404 response means a server accepted the request but did not find a matching resource or handler. Connection refused usually means no server accepted the connection at the requested address and port, for example because the application is not running.

### Q7. Does every REST controller method need a service class?

**Sample answer:**  
No. A simple endpoint that returns a fixed message may not need a service. For business operations such as creating or retrieving habits, a controller should generally delegate business logic to a service.

### Q8. What is the expected response for a successful GET welcome endpoint?

**Sample answer:**  
For our example, the expected response is `200 OK`, with the response body `Welcome to HabitFlow!`.

### Interview practice task

Explain the complete flow in your own words:

1. You start the Spring Boot application.
2. Postman sends `GET /api/welcome`.
3. Spring finds `WelcomeController`.
4. Spring invokes the welcome method.
5. The response is returned to Postman.

## 12. Day 7 completion checklist

- [ ] I understand `@RestController`.
- [ ] I understand `@GetMapping`.
- [ ] I created `WelcomeController`.
- [ ] I implemented `/api/welcome`.
- [ ] I started the Spring Boot application successfully.
- [ ] I tested the endpoint in Postman.
- [ ] I verified the response body and HTTP status code.
- [ ] I attempted the practical exercises.
- [ ] I completed the quiz and interview questions.
- [ ] I saved the complete lesson in `Main_Notes_Branch`.

## 13. Save your notes in GitHub

Save this lesson at:

```text
learning-notes/
└── Month-1/
    └── Week-1/
        └── Day-7.md
```

Suggested commands:

```bash
git switch Main_Notes_Branch
git status
git add learning-notes/Month-1/Week-1/Day-7.md
git commit -m "Add HabitFlow Week 1 Day 7 REST API and Postman lesson"
git push origin Main_Notes_Branch
```

Remember:

- `Main_Code_Branch` – application code.
- `Main_Notes_Branch` – complete daily learning lessons and documentation.

## 14. Week 1 recap

This week, the tracker introduced the foundations needed to start the HabitFlow backend.

| Day | Topic |
|---|---|
| Day 1 | Requirements and MVP scope |
| Day 2 | Client-server architecture and REST API request flow |
| Day 3 | Generate Spring Boot Maven project |
| Day 4 | `pom.xml`, dependencies, plugins and Maven lifecycle |
| Day 5 | Main application class, auto-configuration and startup |
| Day 6 | IoC, Spring Beans and Dependency Injection |
| Day 7 | Welcome REST endpoint and Postman testing |

Before moving to Week 2, make sure you can run the application and successfully test your welcome endpoint. Update the Excel tracker statuses only after completing the corresponding tasks.

## 15. Next lesson – Month 1, Week 2, Day 1

**Topic: JPA Fundamentals – JDBC, ORM, JPA and Hibernate**

You will learn what each technology does, how they differ and how Spring Boot uses them to communicate with MySQL.

---

**Learning rule:** Do not just memorize annotations. Be able to explain what happens from the moment Postman sends a request until the response is returned.
