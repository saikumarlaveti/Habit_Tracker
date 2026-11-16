# HabitFlow – Month 1, Week 3, Day 1
## Understand Controller, Service and Repository Responsibilities

**Roadmap location:** Month 1 → Week 3 → Day 1  
**Tracker area:** Layered architecture  
**Exact tracker task:** Understand Controller, Service and Repository responsibilities

---

## 1. Today's learning objectives

By the end of this lesson, you should be able to:

1. Explain what layered architecture means.
2. Explain the responsibility of the Controller layer.
3. Explain the responsibility of the Service layer.
4. Explain the responsibility of the Repository layer.
5. Trace an HTTP request through all three layers.
6. Explain why business logic should not be placed directly in a Controller.
7. Identify where a particular piece of code belongs.

**Today's boundary:** This lesson focuses on understanding the responsibilities and request flow of the three layers. Constructor injection is the next tracker task (Week 3 Day 2). DTOs are Week 3 Day 3. We will not implement the Habit CRUD APIs today.

---

## 2. What is layered architecture?

Layered architecture is a way of organizing an application into separate parts, where each part has a clear responsibility.

Instead of writing HTTP handling, business rules and database operations in one large class, we separate them into layers.

For HabitFlow, the three backend layers we are studying are:

1. Controller
2. Service
3. Repository

Conceptual structure:

```text
Client (Browser / Frontend / Postman)
                 |
                 | HTTP request
                 v
        +------------------+
        | Controller Layer |
        | Handles HTTP     |
        +------------------+
                 |
                 | Calls application operation
                 v
        +------------------+
        |  Service Layer   |
        | Business logic   |
        +------------------+
                 |
                 | Calls data-access operation
                 v
        +------------------+
        | Repository Layer |
        | Database access  |
        +------------------+
                 |
                 v
              MySQL
```

Each layer has a focused job. This makes the code easier to understand, test, maintain and extend.

---

## 3. Why do we need separate layers?

Imagine that HabitFlow has a feature to create a habit.

A user sends a request containing:

- Habit name: Read a book
- Description: Read for 20 minutes every day

If all the code is written inside one Controller, that Controller might need to:

- Receive the HTTP request.
- Read and interpret the input.
- Check whether the habit name is acceptable.
- Apply business rules.
- Communicate with the database.
- Handle database-related outcomes.
- Construct the HTTP response.

As the application grows, this single class becomes difficult to maintain.

With layered architecture, responsibilities are separated:

| Layer | Main responsibility |
|---|---|
| Controller | Handles HTTP requests and responses |
| Service | Contains application and business logic |
| Repository | Performs data-access operations |

A useful rule:

- HTTP-related work belongs in the Controller.
- Business decisions belong in the Service.
- Database access belongs in the Repository.

---

## 4. Controller layer

### What is a Controller?

A Controller is the entry point for HTTP requests in a Spring MVC application. It receives requests, invokes the appropriate application logic and returns an HTTP response.

In Spring Boot, a REST controller is commonly declared using:

```java
@RestController
```

A REST controller typically uses mappings such as:

```java
@GetMapping
@PostMapping
@PutMapping
@DeleteMapping
```

You studied `@RestController` and `@GetMapping` in Week 1. Today we are focusing on the Controller's responsibility in the overall architecture.

### Controller responsibilities

1. Receive HTTP requests.
2. Map a URL and HTTP method to an application operation.
3. Read request data or path/query parameters.
4. Call the appropriate Service method.
5. Return the result or an appropriate HTTP response.

### What should a Controller avoid?

A Controller should not become the place where all business rules and database operations are implemented.

Avoid placing complex business logic and direct persistence operations inside Controller methods. Delegate those responsibilities to the Service and Repository layers.

### Conceptual example

This is a small illustration of the Controller's role, not today's implementation assignment:

```java
@RestController
@RequestMapping("/api/v1/habits")
public class HabitController {

    private final HabitService habitService;

    public HabitController(HabitService habitService) {
        this.habitService = habitService;
    }

    @PostMapping
    public ResponseEntity<String> createHabit() {

        habitService.createHabit();

        return ResponseEntity.ok("Habit request processed");
    }
}
```

Notice what the Controller does:

- It receives an HTTP POST request.
- It calls the Service.
- It returns an HTTP response.

It does not directly save an entity using `HabitRepository`.

The constructor shown here previews constructor injection, which is the next scheduled topic. You do not need to implement this example today.

---

## 5. Service layer

### What is a Service?

The Service layer contains application operations and business logic. It coordinates work required to fulfil a use case.

For HabitFlow, examples of possible business rules include:

- A habit must have a meaningful name.
- A habit should belong to the correct user when user accounts are introduced.
- A habit should follow the application's rules when it is created, updated or deleted.

The exact rules will be implemented as the roadmap reaches those topics. For now, understand that business decisions belong in the Service layer rather than being scattered through Controllers.

### Service responsibilities

1. Implement application use cases.
2. Apply business rules.
3. Coordinate calls to repositories or other services.
4. Decide what should happen when application conditions are met or not met.
5. Keep the Controller focused on HTTP handling.

### Conceptual example

```java
@Service
public class HabitService {

    private final HabitRepository habitRepository;

    public HabitService(HabitRepository habitRepository) {
        this.habitRepository = habitRepository;
    }

    public void createHabit() {
        // Business and application logic belongs here.
        // Repository operations can be called when required.
    }
}
```

This is only a structural illustration. The actual Habit creation logic will be developed in later tracker tasks.

### Why not put all business logic in the Controller?

Because business logic should be reusable independently of a particular HTTP endpoint.

For example, a future use case might be triggered by an HTTP request, a scheduled task or another application component. Keeping the use case in the Service layer avoids tying the business operation to one Controller method.

---

## 6. Repository layer

### What is a Repository?

The Repository layer handles data-access operations. It provides an abstraction between application code and the persistence technology.

You created `HabitRepository` in Week 2 Day 6:

```java
public interface HabitRepository extends JpaRepository<Habit, Long> {
}
```

Spring Data JPA supplies the runtime implementation for the inherited repository methods.

### Repository responsibilities

1. Save entities.
2. Retrieve entities.
3. Delete entities.
4. Check whether records exist.
5. Provide custom data-access queries when required.

### Examples of repository methods

```java
habitRepository.save(habit);

habitRepository.findById(id);

habitRepository.findAll();

habitRepository.existsById(id);

habitRepository.deleteById(id);
```

These methods are examples of data-access operations. They do not belong directly in the Controller.

### What should a Repository avoid?

The Repository should not decide application business policies.

For example, whether a user is allowed to create a particular habit is an application/business rule, not a database retrieval responsibility.

---

## 7. Understand the complete request flow

Consider a future HabitFlow endpoint:

```text
POST /api/v1/habits
```

The user wants to create a habit.

### Step-by-step request flow

**Step 1 – Client sends a request**

The frontend or Postman sends an HTTP request to the backend.

**Step 2 – Controller receives it**

Spring MVC routes the request to the matching Controller method.

The Controller reads the input and calls the appropriate Service operation.

**Step 3 – Service processes the use case**

The Service applies the relevant business rules and coordinates the work required to create the habit.

**Step 4 – Repository performs data access**

The Service calls the Repository when it needs to save or retrieve an entity.

**Step 5 – JPA provider communicates with the database**

Spring Data JPA and the JPA provider, commonly Hibernate, perform the persistence work through JDBC.

**Step 6 – Result returns through the layers**

The result travels back from Repository to Service, then to Controller.

**Step 7 – Controller returns an HTTP response**

The Controller sends the HTTP response to the client.

### Request and response diagram

```text
         CLIENT
            |
            | HTTP request
            v
       CONTROLLER
            |
            | Method call
            v
         SERVICE
            |
            | Repository call
            v
       REPOSITORY
            |
            v
          MYSQL

          Response
             ^
             |
       REPOSITORY
             ^
             |
          SERVICE
             ^
             |
        CONTROLLER
             ^
             |
          CLIENT
```

Remember: the request travels down through the layers, and the result travels back up.

---

## 8. Example: where should each responsibility go?

Imagine the user wants to create a habit called "Read a book".

| Activity | Correct layer | Reason |
|---|---|---|
| Receive `POST /api/v1/habits` | Controller | It is HTTP request handling |
| Read request body | Controller | It receives client input |
| Apply a rule about an acceptable habit name | Service | It is a business/application rule |
| Decide which application operation to invoke | Controller | It routes the request to the use case |
| Save the Habit entity | Repository | It is a persistence operation |
| Retrieve a Habit by ID | Repository | It is a data-access operation |
| Coordinate validation/business decisions and persistence | Service | It coordinates the use case |
| Return an HTTP status and response | Controller | It creates the HTTP response |

Some cross-cutting concerns, such as validation and exception handling, will be studied in their scheduled lessons. Do not add those implementations today.

---

## 9. Suggested HabitFlow package structure

As the project grows, a common package structure is:

```text
com.habitflow.backend
│
├── controller
│   └── HabitController.java
│
├── service
│   └── HabitService.java
│
├── repository
│   └── HabitRepository.java
│
├── entity
│   └── Habit.java
│
└── HabitFlowApplication.java
```

The purpose of this structure is to keep related classes organized.

You already have the `entity` and `repository` packages from earlier lessons. The `controller` and `service` packages will be used as the roadmap progresses.

Do not create empty classes merely to match this diagram unless you want to practise package creation. Today's task is to understand the responsibilities, not implement the complete architecture.

---

## 10. Common mistakes

### Mistake 1: Putting database operations directly in the Controller

Why it is a problem: the Controller becomes responsible for both HTTP handling and persistence.

Better approach: delegate application work to the Service, and let the Service use the Repository.

### Mistake 2: Putting business rules in the Repository

Why it is a problem: the Repository's main job is data access, not deciding application policies.

Better approach: put business rules in the Service.

### Mistake 3: Making the Service handle HTTP details

Why it is a problem: the Service becomes coupled to HTTP request and response handling.

Better approach: keep HTTP mapping and response construction in the Controller.

### Mistake 4: Thinking the Repository is the database

The Repository is a Java abstraction. The actual data is stored in the database, such as MySQL.

### Mistake 5: Thinking that three layers mean three databases

No. The three layers are code organization boundaries. HabitFlow can use one MySQL database while having Controller, Service and Repository layers.

### Mistake 6: Calling the Repository directly from every Controller

For this project's intended architecture, Controllers should delegate use cases to Services. Services coordinate business logic and repository access.

---

## 11. Practical assignment

Today's assignment is a design and understanding exercise. Do not implement CRUD endpoints yet.

### Task A – Draw the architecture

Draw this flow in your notebook or a Markdown file:

```text
Client → Controller → Service → Repository → MySQL
```

Add one sentence describing each layer.

### Task B – Create a responsibility table

Write your own examples for these questions:

1. Which layer receives an HTTP request?
2. Which layer checks an application business rule?
3. Which layer saves an entity?
4. Which layer returns an HTTP response?

### Task C – Inspect your existing project

Open your HabitFlow project and identify:

- Your existing `Habit` entity.
- Your existing `HabitRepository`.
- The package where each class is located.

Think about where the future `HabitController` and `HabitService` classes should belong. You do not need to implement them today.

### Task D – Explain the request flow

In your own words, explain what happens when a user sends a request to create a Habit.

Try to explain it without looking at the lesson.

---

## 12. Knowledge check – 10 MCQs

Try to answer before checking the answer key.

**Q1. What is the main purpose of layered architecture?**

A. To put all code in one class  
B. To separate application responsibilities  
C. To remove the database  
D. To replace Java

**Q2. Which layer receives HTTP requests?**

A. Repository  
B. Database  
C. Controller  
D. Entity

**Q3. Where should application business rules generally be implemented?**

A. Service  
B. Repository  
C. MySQL Workbench  
D. `pom.xml`

**Q4. Which layer is responsible for data-access operations?**

A. Controller  
B. Repository  
C. Browser  
D. DTO

**Q5. Which is the expected request flow?**

A. Repository → Controller → Service  
B. Controller → Service → Repository  
C. Service → Repository → Controller for the incoming request  
D. MySQL → Controller → Repository

**Q6. Which layer should normally construct the HTTP response?**

A. Controller  
B. Repository  
C. MySQL  
D. JDBC driver

**Q7. Should a Controller contain all the application's business logic?**

A. Yes, always  
B. No, business logic belongs in the Service layer  
C. Yes, if MySQL is used  
D. Only for POST requests

**Q8. What is the main responsibility of `HabitRepository`?**

A. Displaying the Habit page  
B. Handling HTTP requests  
C. Providing data-access operations for Habit entities  
D. Managing browser navigation

**Q9. Why is it useful to keep business logic in the Service layer?**

A. It makes business operations easier to reuse and test independently of HTTP handling  
B. It removes the need for a database  
C. It automatically creates frontend pages  
D. It prevents all application errors

**Q10. Do Controller, Service and Repository represent three different databases?**

A. Yes  
B. No, they are application code layers  
C. Only when using Spring Boot  
D. Only when using MySQL

### Answer key with explanations

1. **B** – Layered architecture separates responsibilities into focused parts.
2. **C** – The Controller handles HTTP requests.
3. **A** – Business and application logic belongs in the Service.
4. **B** – The Repository handles data access.
5. **B** – The incoming request is handled by Controller, then Service, then Repository.
6. **A** – The Controller is responsible for HTTP response handling.
7. **B** – The Controller should delegate business operations to the Service.
8. **C** – The Repository provides persistence operations for Habit entities.
9. **A** – A Service can be called from different entry points and tested without tying the business operation to an HTTP Controller.
10. **B** – The three layers organize code; they do not represent separate databases.

---

## 13. Interview questions and sample answers

### 1. What is layered architecture in Spring Boot?

Layered architecture separates an application into layers with specific responsibilities. A common Spring Boot structure uses Controller, Service and Repository layers to separate HTTP handling, business logic and data access.

### 2. What is the responsibility of the Controller layer?

The Controller receives HTTP requests, maps them to application operations, reads request parameters or bodies, calls the Service and returns HTTP responses.

### 3. What is the responsibility of the Service layer?

The Service contains business and application logic. It coordinates the work needed to fulfil a use case and calls repositories or other services when necessary.

### 4. What is the responsibility of the Repository layer?

The Repository handles data-access operations such as saving, retrieving and deleting entities. Spring Data JPA can provide repository implementations at runtime.

### 5. Why should we not put business logic in the Controller?

Putting business logic in Controllers mixes HTTP handling with application rules. This makes the Controller harder to maintain and the logic harder to reuse or test independently.

### 6. Why should the Controller call the Service instead of directly calling the Repository?

The Service provides a place to coordinate application operations and apply business rules. This keeps Controllers focused on HTTP responsibilities and Repositories focused on data access.

### 7. Can a Service call more than one Repository?

Yes. A Service can coordinate multiple repositories when a business operation requires data from multiple entities or tables.

### 8. What happens when a request reaches a Spring REST Controller?

Spring MVC maps the request to the appropriate Controller method. The Controller invokes application logic, commonly through a Service, and returns a response that Spring serializes into the HTTP response.

### 9. Is the Repository the same as the database?

No. The Repository is a Java abstraction that provides data-access operations. The database is the actual system that stores persistent data.

### 10. What are the benefits of layered architecture?

It improves separation of concerns, readability, maintainability, testability and the ability to evolve individual parts of the application.

### 11. Where would you put a rule that prevents a user from creating a habit with an invalid name?

The application/business rule belongs in the Service layer. Request-format validation can also be applied at the API boundary in the appropriate validation layer, which will be covered later in the roadmap.

### 12. Does layered architecture require a separate database for each layer?

No. Layers are logical code boundaries. All three layers can work with the same MySQL database.

---

## 14. Completion checklist

- [ ] I understand what layered architecture means.
- [ ] I can explain the Controller's responsibilities.
- [ ] I can explain the Service's responsibilities.
- [ ] I can explain the Repository's responsibilities.
- [ ] I can draw the request flow.
- [ ] I can identify where HTTP handling belongs.
- [ ] I can identify where business logic belongs.
- [ ] I can identify where database access belongs.
- [ ] I inspected the existing Habit entity and HabitRepository.
- [ ] I attempted the MCQs and reviewed the explanations.
- [ ] I answered the interview questions in my own words.

Do not mark this tracker row as Done until you have completed the learning and practical assignment.

---

## 15. Git notes

This project uses one Git repository with two branches:

- `Main_Code_Branch` – application code.
- `Main_Notes_Branch` – learning notes and documentation.

Suggested notes location:

```text
learning-notes/
└── Month-1/
    └── Week-3/
        └── Day-1.md
```

Today's task is mainly conceptual, so save your architecture diagram and notes in `Main_Notes_Branch`. No new application code is required for today's assignment.

---

## 16. Key takeaways

- Layered architecture separates responsibilities.
- Controller handles HTTP requests and responses.
- Service contains application and business logic.
- Repository handles data access.
- The typical request flow is Controller → Service → Repository.
- The result returns through the layers to the Controller.
- The three layers can work with one MySQL database.

## 17. Next scheduled lesson

**Month 1 – Week 3 – Day 2: Dependency Injection**

Exact tracker task: **Use constructor injection and understand why it is preferred.**

Next lesson, we will focus on how Spring supplies dependencies to classes and why constructor injection is a good default for the HabitFlow application.
