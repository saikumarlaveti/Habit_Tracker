# HabitFlow – Month 1, Week 3, Day 2
## Use Constructor Injection and Understand Why It Is Preferred

**Roadmap location:** Month 1 → Week 3 → Day 2  
**Tracker area:** Dependency Injection  
**Exact tracker task:** Use constructor injection and understand why it is preferred

---

## 1. Today's learning objectives

By the end of this lesson, you should be able to:

1. Explain Dependency Injection (DI) in simple terms.
2. Explain what a dependency is.
3. Understand how Spring manages objects as beans.
4. Use constructor injection to provide a dependency to a class.
5. Understand why constructor injection is generally preferred over field injection.
6. Recognize the role of `@Service`, `@Repository`, and Spring's component scanning.
7. Identify common Dependency Injection errors.

**Today's boundary:** This lesson focuses on constructor injection. DTOs are the next scheduled topic (Week 3 Day 3). We will not implement Habit CRUD APIs today.

---

## 2. First understand what a dependency is

A dependency is an object or service that another class needs to perform its work.

For example, imagine that `HabitService` needs `HabitRepository` to save or retrieve Habit entities.

```text
HabitService
     |
     | needs
     v
HabitRepository
```

Here:

- `HabitService` is the class that needs another object.
- `HabitRepository` is its dependency.

A class can create the dependency itself using `new`, but in a Spring application we commonly let Spring manage and provide the dependency.

---

## 3. What is Dependency Injection?

Dependency Injection is a design technique in which an object receives the other objects it needs from an external source instead of creating those objects itself.

In a Spring Boot application, the Spring IoC container manages Spring beans and supplies their dependencies.

Without Dependency Injection, a class might create its own dependency:

```java
public class HabitService {

    private HabitRepository habitRepository =
            new HabitRepositoryImplementation();
}
```

This is only a conceptual illustration; a Spring Data JPA repository is normally provided by Spring as a generated implementation. Creating it manually is not the intended approach.

With Dependency Injection, the class declares what it needs and Spring provides it:

```java
public class HabitService {

    private final HabitRepository habitRepository;

    public HabitService(HabitRepository habitRepository) {
        this.habitRepository = habitRepository;
    }
}
```

The class does not create the repository. It receives the repository through its constructor.

### Why use Dependency Injection?

- It reduces tight coupling between classes.
- It makes dependencies explicit.
- It makes unit testing easier because a test can supply a mock or alternative dependency.
- It lets the Spring container manage bean creation and wiring.
- It makes application components easier to maintain.

---

## 4. Understand Spring IoC container and beans

The Spring IoC (Inversion of Control) container is responsible for creating, configuring and managing Spring beans.

A Spring bean is an object managed by the Spring container.

Some common annotations that help Spring detect application components are:

| Annotation | Typical purpose |
|---|---|
| `@Component` | General-purpose Spring-managed component |
| `@Service` | Service-layer component |
| `@Repository` | Repository-layer component; also supports persistence exception translation |
| `@Controller` | Spring MVC controller |
| `@RestController` | REST controller |

`@Service` and `@Repository` are specialized component stereotypes. Spring can detect them through component scanning when they are in the application's scan scope.

You created `HabitRepository` by extending `JpaRepository`. Spring Data JPA detects the repository interface and provides a runtime implementation bean when repository scanning and configuration are correct.

The application can then inject that repository bean into another Spring-managed class.

---

## 5. What is constructor injection?

Constructor injection means providing a class's required dependencies through its constructor.

Example:

```java
@Service
public class HabitService {

    private final HabitRepository habitRepository;

    public HabitService(HabitRepository habitRepository) {
        this.habitRepository = habitRepository;
    }
}
```

Let's understand each part.

### `@Service`

```java
@Service
```

This marks `HabitService` as a Spring-managed service component. Spring can discover it through component scanning.

### `private final HabitRepository habitRepository`

```java
private final HabitRepository habitRepository;
```

This declares the dependency.

- `private` means the field is accessible within the class.
- `final` means the field must be assigned once, typically in the constructor, and cannot be reassigned afterward.
- `HabitRepository` is the dependency type.
- `habitRepository` is the field name.

### Constructor

```java
public HabitService(HabitRepository habitRepository) {
    this.habitRepository = habitRepository;
}
```

Spring supplies the repository bean as the constructor argument.

The assignment:

```java
this.habitRepository = habitRepository;
```

stores the supplied dependency in the object's field.

- `this.habitRepository` refers to the class field.
- `habitRepository` refers to the constructor parameter.

### Why is there no `@Autowired` annotation?

If a Spring bean has exactly one constructor, Spring can use that constructor for dependency injection without an explicit `@Autowired` annotation.

You may still see `@Autowired` on constructors in older or different code styles. For a class with one constructor, it is not required.

---

## 6. HabitFlow example: inject HabitRepository into HabitService

You already created `HabitRepository` in Week 2 Day 6:

```java
package com.habitflow.backend.repository;

import com.habitflow.backend.entity.Habit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HabitRepository extends JpaRepository<Habit, Long> {
}
```

Now imagine the service layer needs to use this repository.

Create a service package:

```text
com.habitflow.backend
├── entity
│   └── Habit.java
├── repository
│   └── HabitRepository.java
└── service
    └── HabitService.java
```

Create `HabitService.java`:

```java
package com.habitflow.backend.service;

import com.habitflow.backend.repository.HabitRepository;
import org.springframework.stereotype.Service;

@Service
public class HabitService {

    private final HabitRepository habitRepository;

    public HabitService(HabitRepository habitRepository) {
        this.habitRepository = habitRepository;
    }
}
```

This is the main code example for today's topic.

Notice:

1. `HabitService` is marked with `@Service`.
2. It declares `HabitRepository` as a dependency.
3. It receives the dependency through its constructor.
4. The dependency field is `final`.
5. There is no manual `new HabitRepository...` call.
6. There is no `@Autowired` annotation because this class has one constructor.

We are not adding business methods or CRUD endpoints today. The purpose is to understand how the dependency is provided.

---

## 7. What happens when the application starts?

At a high level, the process is:

```text
Spring Boot application starts
             |
             v
Spring creates the application context
             |
             v
Spring discovers HabitService
             |
             v
Spring Data JPA registers HabitRepository
             |
             v
Spring sees HabitService's constructor dependency
             |
             v
Spring provides the HabitRepository bean
             |
             v
HabitService bean is created
```

This is a conceptual overview. Actual startup involves configuration, component scanning, repository registration and bean creation steps managed by Spring.

If Spring cannot find a suitable bean for a required constructor parameter, application startup normally fails with a dependency-resolution error.

---

## 8. Why is constructor injection preferred?

Constructor injection is generally the recommended default for required dependencies in Spring applications.

### Reason 1 – Dependencies are visible

```java
public HabitService(HabitRepository habitRepository) {
    this.habitRepository = habitRepository;
}
```

Anyone reading the constructor can immediately see that `HabitService` requires `HabitRepository`.

### Reason 2 – Required dependencies cannot be silently omitted

The constructor requires the dependency to create the object. The object cannot be constructed through that constructor without supplying it.

### Reason 3 – Supports `final` fields

A required dependency can be stored in a `final` field and cannot be reassigned after construction.

### Reason 4 – Easier unit testing

A unit test can provide a mock repository directly:

```java
HabitRepository repository = Mockito.mock(HabitRepository.class);

HabitService service = new HabitService(repository);
```

This example illustrates manual construction in a unit test. It does not start the Spring container.

### Reason 5 – Helps avoid partially initialized objects

With constructor injection, the required dependencies are supplied during object creation rather than being assigned later through field injection.

---

## 9. Compare the three common injection styles

Spring supports different ways of injecting dependencies. The most common styles discussed in beginner projects are constructor, setter and field injection.

### A. Constructor injection

```java
@Service
public class HabitService {

    private final HabitRepository habitRepository;

    public HabitService(HabitRepository habitRepository) {
        this.habitRepository = habitRepository;
    }
}
```

- Dependency is supplied through the constructor.
- Required dependencies are explicit.
- Works naturally with `final` fields.
- Usually easiest to unit test without Spring.

### B. Setter injection

```java
@Service
public class HabitService {

    private HabitRepository habitRepository;

    @Autowired
    public void setHabitRepository(HabitRepository habitRepository) {
        this.habitRepository = habitRepository;
    }
}
```

- Dependency is supplied through a setter method.
- Can be useful for optional or reconfigurable dependencies.
- The object can exist before the dependency is set.
- The field cannot be `final` in this pattern.

### C. Field injection

```java
@Service
public class HabitService {

    @Autowired
    private HabitRepository habitRepository;
}
```

- Spring injects directly into the field.
- It is concise, but the dependency is less visible in the constructor.
- It makes plain unit testing harder because the dependency is not supplied through a public constructor.
- The field cannot be `final` in this form.

### Comparison

| Feature | Constructor | Setter | Field |
|---|---|---|---|
| Dependency visible in constructor | Yes | No | No |
| Supports `final` required field | Yes | No | No |
| Easy manual unit testing | Yes | Yes, after setter | Less convenient |
| Can represent optional dependency | Possible with deliberate design | Often suitable | Possible, but less explicit |
| Recommended default for required dependencies | Yes | Not usually | Not usually |

**Interview point:** Say that constructor injection is generally preferred for required dependencies. Setter injection can be appropriate for optional dependencies. Field injection is often discouraged because it hides dependencies and complicates isolated testing.

---

## 10. Important: avoid manually creating Spring-managed dependencies

Consider:

```java
HabitRepository repository = new HabitRepository();
```

This is incorrect for our Spring Data JPA repository because:

1. `HabitRepository` is an interface.
2. Spring Data JPA provides its runtime implementation.
3. The repository needs Spring-managed persistence infrastructure.

In a Spring application, declare the dependency and allow Spring to inject the registered bean.

Also avoid manually creating a Spring service with `new HabitService(...)` in normal application code. Spring should manage application components so that their dependencies and other framework features are applied.

Manual construction is fine in focused unit tests when you deliberately supply test dependencies.

---

## 11. Common errors and troubleshooting

### Error 1: No qualifying bean of type `HabitRepository`

Possible causes:

- Spring Data JPA dependency is missing.
- Repository interface is not discovered.
- Repository package is outside the repository scan scope.
- JPA configuration or application startup failed.

Check the dependency, package structure, repository declaration and startup logs.

### Error 2: No qualifying bean of type `HabitService`

Possible causes:

- `@Service` is missing.
- Service package is outside component-scan scope.
- The application is not starting from the expected base package.

Check that `HabitService` has `@Service` and is under the application's scan scope, or configure scanning explicitly.

### Error 3: Constructor parameter cannot be resolved

Spring cannot find a bean matching the required constructor parameter. Check that the dependency is registered and that its type is correct.

### Error 4: `final` field is not initialized

A final field must be assigned by every constructor path. Assign it inside the constructor:

```java
this.habitRepository = habitRepository;
```

### Error 5: Multiple constructors cause confusion

If a class has multiple constructors, Spring's constructor selection rules matter. For this lesson, use one clear constructor for required dependencies. When multiple constructors are genuinely needed, explicitly configure which constructor Spring should use.

### Error 6: Wrong import for `@Service`

Use:

```java
import org.springframework.stereotype.Service;
```

For `@Autowired`, the Spring Framework 6+ import is:

```java
import org.springframework.beans.factory.annotation.Autowired;
```

You do not need `@Autowired` for a class with exactly one constructor.

---

## 12. Practical assignment

Complete these tasks in your HabitFlow project.

1. Open your existing project.
2. Locate `HabitRepository`.
3. Create the `service` package if it does not exist.
4. Create `HabitService.java`.
5. Add `@Service`.
6. Declare `private final HabitRepository habitRepository;`.
7. Create a constructor that accepts `HabitRepository`.
8. Assign the constructor parameter to the field.
9. Confirm that there is no unnecessary `new HabitRepository()` call.
10. Confirm imports resolve and the project compiles.

Expected class:

```java
package com.habitflow.backend.service;

import com.habitflow.backend.repository.HabitRepository;
import org.springframework.stereotype.Service;

@Service
public class HabitService {

    private final HabitRepository habitRepository;

    public HabitService(HabitRepository habitRepository) {
        this.habitRepository = habitRepository;
    }
}
```

Adapt the package names if your project uses a different base package.

**Do not add DTOs, Controller methods, or CRUD business methods today.** Those are later tracker tasks.

---

## 13. Knowledge check – 10 MCQs

Try to answer before reading the answer key.

**Q1. What is Dependency Injection?**

A. A way to delete Java objects  
B. A technique where a class receives the dependencies it needs  
C. A way to create database tables  
D. A Java loop

**Q2. In HabitFlow, what is the dependency of `HabitService` in today's example?**

A. `HabitRepository`  
B. `HabitController`  
C. `HabitFlowApplication`  
D. `MySQLWorkbench`

**Q3. Which annotation marks the service class as a Spring service component?**

A. `@Entity`  
B. `@Service`  
C. `@Table`  
D. `@Column`

**Q4. Which injection style provides dependencies through a constructor?**

A. Field injection  
B. Setter injection  
C. Constructor injection  
D. Database injection

**Q5. Why is `final` useful for a required constructor-injected dependency?**

A. It allows the field to be reassigned anywhere  
B. It prevents reassignment after initialization  
C. It creates a database table  
D. It starts the Spring application

**Q6. Is `@Autowired` required on a class's only constructor?**

A. Yes, always  
B. No, Spring can use the single constructor without it  
C. Only when using MySQL  
D. Only when using `@Entity`

**Q7. Which injection style is generally preferred for required dependencies?**

A. Constructor injection  
B. Field injection  
C. Random injection  
D. SQL injection

**Q8. What is a Spring bean?**

A. A Java object managed by the Spring container  
B. A MySQL table  
C. A Maven plugin  
D. A browser cookie

**Q9. Which example is appropriate for a simple unit test that manually supplies a mocked repository?**

A. `new HabitService(repositoryMock)`  
B. `new JpaRepository()`  
C. `new @Service()`  
D. `new MySQLWorkbench()`

**Q10. What can happen if Spring cannot find a required repository bean for a service constructor?**

A. The dependency is always silently ignored  
B. Application context creation normally fails  
C. The database automatically creates the service  
D. Spring converts it into a String

### Answer key with explanations

1. **B** – Dependency Injection means a class receives the objects it depends on.
2. **A** – `HabitService` needs `HabitRepository` for data-access operations.
3. **B** – `@Service` marks the class as a service component.
4. **C** – Constructor injection supplies dependencies through the constructor.
5. **B** – A final field can be assigned once and cannot be reassigned afterward.
6. **B** – A single constructor does not need an explicit `@Autowired`.
7. **A** – Constructor injection is the recommended default for required dependencies.
8. **A** – A Spring bean is an object managed by the Spring container.
9. **A** – A unit test can manually create the service with a mock dependency.
10. **B** – A missing required dependency normally causes application startup/context creation to fail.

---

## 14. Interview questions and sample answers

### 1. What is Dependency Injection?

Dependency Injection is a design technique where an object receives the dependencies it needs from an external source instead of creating them itself. In Spring, the IoC container manages beans and injects their dependencies.

### 2. What is a dependency?

A dependency is another object or component that a class requires to perform its work. For example, `HabitService` depends on `HabitRepository` to perform persistence operations.

### 3. What is constructor injection?

Constructor injection is a technique where required dependencies are passed to a class through its constructor. The class stores those dependencies for use by its methods.

### 4. Why is constructor injection preferred in Spring?

It makes required dependencies explicit, supports immutable `final` fields, helps prevent partially initialized objects and makes unit testing easier because dependencies can be supplied directly.

### 5. Is `@Autowired` mandatory on a single constructor?

No. If a Spring-managed class has exactly one constructor, Spring can use it for dependency injection without `@Autowired`.

### 6. What is a Spring bean?

A Spring bean is an object created, configured and managed by the Spring IoC container.

### 7. What is the difference between `@Component` and `@Service`?

Both are Spring component stereotypes and can be detected by component scanning. `@Service` is a specialized stereotype used to communicate that the class belongs to the service layer.

### 8. What is the difference between constructor injection and field injection?

Constructor injection supplies dependencies through a constructor, making required dependencies explicit and allowing `final` fields. Field injection places dependencies directly into fields, hiding them from the constructor and making isolated unit testing less convenient.

### 9. Can we use constructor injection without Spring?

Yes. A class constructor is a normal Java feature. You can manually create an object and pass its dependencies. Spring automates this wiring when it manages the class as a bean.

### 10. What happens if a required dependency is missing?

Spring normally cannot create the bean and application context startup fails with a dependency-resolution error.

### 11. Can one Service depend on multiple repositories?

Yes. A Service can receive multiple required repositories through its constructor and coordinate them for a business use case.

### 12. How would you unit test a Service that uses a Repository?

I can create a mock of the repository using Mockito and pass it to the Service constructor. Then I can test the Service logic without starting the Spring application context or connecting to the real database.

---

## 15. Completion checklist

- [ ] I understand what a dependency is.
- [ ] I can explain Dependency Injection.
- [ ] I understand the role of the Spring IoC container.
- [ ] I know what a Spring bean is.
- [ ] I created the `service` package.
- [ ] I created `HabitService`.
- [ ] I added `@Service`.
- [ ] I injected `HabitRepository` through the constructor.
- [ ] I used a `final` dependency field.
- [ ] I understand why `@Autowired` is not required on a single constructor.
- [ ] I can explain why constructor injection is preferred.
- [ ] The project compiles successfully.
- [ ] I attempted the MCQs and reviewed the interview answers.

Do not mark this tracker row as Done until you have completed the practical work and checked that the project compiles.

---

## 16. Git instructions

This project uses one Git repository with two branches:

- `Main_Code_Branch` – application code.
- `Main_Notes_Branch` – learning notes and documentation.

Suggested notes location:

```text
learning-notes/
└── Month-1/
    └── Week-3/
        ├── Day-1.md
        └── Day-2.md
```

Commit `HabitService.java` in `Main_Code_Branch`. Save this Markdown lesson in `Main_Notes_Branch`.

---

## 17. Key takeaways

- Dependency Injection means receiving dependencies instead of creating them inside the class.
- Spring's IoC container manages Spring beans and their dependencies.
- `@Service` marks the service-layer component.
- Constructor injection supplies dependencies through the constructor.
- A single constructor does not require `@Autowired`.
- `final` is useful for required constructor-injected dependencies.
- Constructor injection is generally preferred for required dependencies because it makes them explicit and supports easier testing.

## 18. Next scheduled lesson

**Month 1 – Week 3 – Day 3: DTOs**

Exact tracker task: **Understand Entity vs Request DTO vs Response DTO.**

Next lesson, we will understand why an API should not expose its persistence entity directly and how request and response DTOs represent data exchanged with the client.
