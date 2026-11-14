# HabitFlow – Month 1, Week 1, Day 6
## Spring Core: IoC, Spring Beans and Dependency Injection

**Official tracker task:** Learn IoC, Spring Beans and Dependency Injection  
**Suggested duration:** 60 minutes  
**Level:** Beginner  
**Project:** HabitFlow – Habit Tracker  
**Prerequisite:** Day 5 – Main application class, auto-configuration and application startup

---

## 1. Learning objectives

By the end of this lesson, you should be able to:

1. Explain why Spring Core is useful.
2. Define Inversion of Control (IoC).
3. Explain what a Spring Bean is.
4. Understand common ways to register beans.
5. Define Dependency Injection (DI).
6. Differentiate constructor, setter and field injection.
7. Explain why constructor injection is generally preferred for required dependencies.
8. Implement a small service-to-controller dependency in HabitFlow.

## 2. Why do we need Spring Core?

Imagine we are building HabitFlow without Spring.

We create a `HabitService` class:

```java
public class HabitService {

    public void createHabit() {
        System.out.println("Habit created");
    }
}
```

To use this class, another class might create its object:

```java
public class HabitController {

    private HabitService habitService = new HabitService();

    public void create() {
        habitService.createHabit();
    }
}
```

This works, but consider what happens when our application grows:

- What if `HabitService` depends on another class?
- What if multiple classes need the same service?
- What if we want to replace one implementation with another?
- Who should create and manage all these objects?

If every class creates its own dependencies, the application becomes tightly coupled and harder to maintain.

**Spring Core helps solve this problem by managing objects and their dependencies.**

## 3. What is IoC (Inversion of Control)?

IoC stands for **Inversion of Control**.

In simple terms:

- Without IoC: Your application code creates and manages its dependencies.
- With IoC: The Spring container takes responsibility for creating and managing eligible objects and providing their dependencies.

### Without IoC

```text
HabitController
      |
      | creates using new
      v
HabitService
```

The controller creates its own service dependency.

### With IoC

```text
             Spring IoC Container
                      |
          +-----------+-----------+
          |                       |
          v                       v
   HabitController            HabitService
          |
          | receives dependency
          v
     Uses HabitService
```

The Spring container creates and manages the objects and supplies dependencies.

Example:

```java
import org.springframework.stereotype.Component;

@Component
public class HabitService {

    public void createHabit() {
        System.out.println("Habit created");
    }
}
```

The `@Component` annotation makes the class eligible for component scanning. Spring can then register it as a bean.

**Remember:** IoC is the principle; the Spring IoC container is the mechanism that manages the objects.

## 4. What is a Spring Bean?

A Spring Bean is an object that is created, configured and managed by the Spring IoC container.

Example:

```java
import org.springframework.stereotype.Component;

@Component
public class HabitService {

    public String getMessage() {
        return "Welcome to HabitFlow!";
    }
}
```

When Spring discovers this class through component scanning, it can create and manage an instance of it as a bean.

### 4.1 Common stereotype annotations

| Annotation | Typical use |
|---|---|
| `@Component` | General-purpose component |
| `@Service` | Business logic class |
| `@Repository` | Data access class |
| `@Controller` | MVC controller |
| `@RestController` | REST controller |

These annotations are commonly called stereotype annotations. They make classes eligible for component scanning when they are in a scanned package.

### 4.2 Defining a bean using `@Bean`

Sometimes you want to register an object from a class that you cannot annotate, such as a third-party library class.

You can define it inside a configuration class:

```java
@Configuration
public class HabitConfig {

    @Bean
    public GreetingService greetingService() {
        return new GreetingService();
    }
}
```

Explanation:

- `@Configuration` identifies a configuration class.
- `@Bean` tells Spring to register the returned object in the application context.
- `greetingService()` creates and returns the object.

### 4.3 `@Component` versus `@Bean`

| `@Component` | `@Bean` |
|---|---|
| Applied to a class | Applied to a method |
| Discovered through component scanning | Registered through configuration |
| Useful for classes you control | Useful for third-party or manually configured objects |

## 5. What is Dependency Injection (DI)?

Dependency Injection is a design technique in which an object receives the other objects it needs instead of creating them itself.

Imagine HabitFlow:

- `HabitController` needs `HabitService`.
- `HabitService` contains the business logic for creating or retrieving habits.

The controller depends on the service. Therefore, `HabitService` is a dependency of `HabitController`.

### Without Dependency Injection

```java
public class HabitController {

    private HabitService habitService = new HabitService();

}
```

The controller creates the dependency itself.

### With Dependency Injection

```java
@RestController
public class HabitController {

    private final HabitService habitService;

    public HabitController(HabitService habitService) {
        this.habitService = habitService;
    }
}
```

Spring provides the `HabitService` object when it creates the controller bean.

**Key idea:** The controller uses the service, but it does not create the service itself.

## 6. Types of Dependency Injection

There are three commonly discussed types.

### 6.1 Constructor Injection

Dependencies are provided through the class constructor.

```java
@Service
public class HabitService {

    private final HabitRepository habitRepository;

    public HabitService(HabitRepository habitRepository) {
        this.habitRepository = habitRepository;
    }
}
```

Advantages:

- Dependencies are explicit.
- Fields can be `final`.
- Easier to unit test.
- Required dependencies can be enforced at object creation.

**Recommendation:** Prefer constructor injection for required dependencies.

### 6.2 Setter Injection

Dependencies are provided through setter methods.

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

Setter injection can be useful when a dependency is optional or can be changed after object creation. Optional dependencies should still be designed carefully.

### 6.3 Field Injection

Dependencies are injected directly into fields.

```java
@Service
public class HabitService {

    @Autowired
    private HabitRepository habitRepository;
}
```

This is concise, but generally not recommended for application code because dependencies are less explicit and direct unit testing is harder.

### 6.4 Comparison

| Type | Where dependency is provided | General recommendation |
|---|---|---|
| Constructor | Constructor | Preferred for required dependencies |
| Setter | Setter method | Useful for optional dependencies |
| Field | Directly into field | Generally avoid |

## 7. How IoC and DI work together

These concepts are related but not identical:

- **IoC:** Control of object creation and management is handed to the Spring container.
- **DI:** The container supplies the dependencies an object needs.

Example flow:

```text
Spring application starts
        |
        v
Spring scans eligible components
        |
        v
Spring creates the HabitService bean
        |
        v
Spring creates HabitController
        |
        v
Spring injects HabitService into HabitController
        |
        v
HabitController can use HabitService
```

IoC is the broader principle. Dependency Injection is one way the container implements that principle.

## 8. Practical implementation – HabitFlow

Create a small example to see Dependency Injection in action. Use your existing package structure under `com.habitflow`.

### Step 1: Create `HabitService`

Create a class inside the `service` package:

```java
package com.habitflow.service;

import org.springframework.stereotype.Service;

@Service
public class HabitService {

    public String getWelcomeMessage() {
        return "Welcome to HabitFlow! Let's build good habits.";
    }
}
```

What does `@Service` do?

- It marks the class as a service component.
- Spring can discover it through component scanning.
- Spring creates and manages its bean.

### Step 2: Create `HabitController`

Create a class inside the `controller` package:

```java
package com.habitflow.controller;

import com.habitflow.service.HabitService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HabitController {

    private final HabitService habitService;

    public HabitController(HabitService habitService) {
        this.habitService = habitService;
    }

    @GetMapping("/api/habits/welcome")
    public String welcome() {
        return habitService.getWelcomeMessage();
    }
}
```

Understand the important lines:

```java
private final HabitService habitService;
```

This declares a dependency on `HabitService`.

```java
public HabitController(HabitService habitService) {
    this.habitService = habitService;
}
```

This is constructor injection. Spring provides the `HabitService` bean when it creates the controller.

```java
return habitService.getWelcomeMessage();
```

The controller calls the service method.

### Why didn't we use `@Autowired` on the constructor?

In modern Spring, if a class has only one constructor, Spring automatically uses that constructor for dependency injection. You do not need to add `@Autowired`.

### Step 3: Run the application

Start your Spring Boot application.

If everything is configured correctly, Spring should discover both classes and inject the service into the controller.

### Step 4: Test the endpoint

Open this URL in your browser or Postman:

```text
http://localhost:8080/api/habits/welcome
```

Expected response:

```text
Welcome to HabitFlow! Let's build good habits.
```

### Step 5: Understand what happened

1. Spring discovered `HabitService` because of `@Service`.
2. Spring created a `HabitService` bean.
3. Spring discovered `HabitController` because of `@RestController`.
4. Spring saw that the controller constructor requires `HabitService`.
5. Spring supplied the service bean through constructor injection.
6. When the endpoint was called, the controller used the service.

This is Dependency Injection working through the Spring IoC container.

## 9. What if the dependency is missing?

Imagine you remove `@Service` from `HabitService` and there is no other bean definition for it.

Spring may fail to create `HabitController` because it cannot find a bean of the required type.

You may see an error similar to:

```text
Parameter 0 of constructor in HabitController
required a bean of type 'HabitService'
that could not be found.
```

To troubleshoot:

- Check that `HabitService` has `@Service` or another valid bean definition.
- Check that it is under a package scanned by Spring.
- Check that the class compiles successfully.
- Check the application startup logs for the underlying exception.

## 10. Knowledge check – 8 MCQs

Try answering these before checking the answers.

**Q1. What does IoC stand for?**

A. Injection of Code  
B. Inversion of Control  
C. Internal Object Creation  
D. Instance of Class

**Q2. What is a Spring Bean?**

A. A MySQL table  
B. An object managed by the Spring container  
C. A Java package  
D. A Maven dependency

**Q3. Which annotation marks a service class as a Spring component?**

A. `@Entity`  
B. `@Service`  
C. `@Column`  
D. `@PathVariable`

**Q4. What does Dependency Injection mean?**

A. A class creates all its dependencies itself  
B. An object receives the dependencies it needs  
C. A database creates Java objects  
D. A method returns an integer

**Q5. Which injection type is generally preferred for required dependencies?**

A. Field injection  
B. Constructor injection  
C. Static injection  
D. Manual injection

**Q6. Which annotation is commonly used on a configuration method that registers a bean?**

A. `@Bean`  
B. `@Autowired`  
C. `@GetMapping`  
D. `@Override`

**Q7. What is the role of the Spring IoC container?**

A. Only execute SQL queries  
B. Create and manage beans and their dependencies  
C. Only render HTML  
D. Only compile Java code

**Q8. In constructor injection, who supplies `HabitService` to `HabitController`?**

A. The browser  
B. The MySQL database  
C. The Spring container  
D. The Java compiler

### MCQ answer key

| Question | Correct answer | Explanation |
|---|---|---|
| Q1 | B | IoC means Inversion of Control. Object creation and management are handled by the container. |
| Q2 | B | A Spring Bean is an object managed by the Spring IoC container. |
| Q3 | B | `@Service` is a stereotype annotation for service-layer components. |
| Q4 | B | DI means dependencies are provided to an object rather than created by that object. |
| Q5 | B | Constructor injection makes required dependencies explicit and supports final fields and straightforward unit testing. |
| Q6 | A | A `@Bean` method in a configuration class registers its returned object in the Spring container. |
| Q7 | B | The container manages bean creation, configuration, lifecycle and dependency wiring. |
| Q8 | C | Spring resolves the dependency and supplies the bean when it creates the controller. |

## 11. Practical assignment

Complete these tasks in your HabitFlow backend:

1. Create `HabitService` in the service package.
2. Annotate it with `@Service`.
3. Create `HabitController` in the controller package.
4. Inject `HabitService` through its constructor.
5. Add the `/api/habits/welcome` GET endpoint.
6. Run the application and test the endpoint.
7. Temporarily remove `@Service`, observe the startup error, and then restore it.

Do not leave the application in a broken state after the experiment.

## 12. Day 6 completion checklist

- [ ] I can explain IoC in my own words.
- [ ] I understand what a Spring Bean is.
- [ ] I understand `@Component`, `@Service` and `@Bean`.
- [ ] I can explain Dependency Injection.
- [ ] I understand constructor, setter and field injection.
- [ ] I understand why constructor injection is preferred for required dependencies.
- [ ] I created `HabitService`.
- [ ] I created `HabitController` and used constructor injection.
- [ ] I successfully tested the welcome endpoint.
- [ ] I attempted the quiz and interview questions.

## 13. Interview questions – Day 6

Try answering each question aloud before reading the sample answer. Explain the concepts in your own words and use HabitFlow examples when possible.

### Q1. What is Inversion of Control (IoC)?

**Sample answer:**  
Inversion of Control is a principle in which control over object creation and management is transferred from application code to a container or framework. In Spring, the IoC container creates and manages beans and their dependencies.

### Q2. What is the Spring IoC container?

**Sample answer:**  
The Spring IoC container is responsible for instantiating, configuring and managing Spring beans. It also resolves and injects dependencies between beans. The `ApplicationContext` is a commonly used Spring container interface.

### Q3. What is a Spring Bean?

**Sample answer:**  
A Spring Bean is an object instantiated, assembled and managed by the Spring IoC container. Beans can be registered through component scanning using stereotype annotations or explicitly through `@Bean` methods in configuration classes.

### Q4. What is Dependency Injection?

**Sample answer:**  
Dependency Injection is a technique in which an object receives the dependencies it needs from an external container rather than creating those dependencies itself. In Spring, the IoC container resolves and supplies the required beans.

### Q5. What is the difference between IoC and DI?

**Sample answer:**  
IoC is the broader principle of transferring control of object creation and management to a container. DI is a technique used to implement IoC by supplying an object's dependencies from outside that object.

### Q6. What are the three common types of Dependency Injection?

**Sample answer:**

1. Constructor injection – dependencies are passed through a constructor.
2. Setter injection – dependencies are supplied through setter methods.
3. Field injection – dependencies are injected directly into fields.

Constructor injection is generally preferred for required dependencies.

### Q7. Why is constructor injection preferred in Spring?

**Sample answer:**  
Constructor injection makes required dependencies explicit, allows fields to be declared `final`, ensures dependencies are supplied when an object is created and makes unit testing easier. It also avoids requiring reflection to set private fields during ordinary unit tests.

### Q8. What is the difference between `@Component` and `@Bean`?

**Sample answer:**  
`@Component` is placed on a class so that component scanning can discover and register it. `@Bean` is placed on a method in a configuration class, and the object returned by that method is registered in the Spring container. `@Bean` is useful when configuring objects from third-party classes.

### Q9. What is the difference between `@Component` and `@Service`?

**Sample answer:**  
Both are stereotype annotations and make a class eligible for component scanning. `@Service` is a specialized form of `@Component` that communicates that the class belongs to the service or business-logic layer. It also provides a clearer architectural role.

### Q10. Does Spring create a new object every time a bean is injected?

**Sample answer:**  
Not necessarily. Spring's default bean scope is singleton, meaning one bean instance is typically created per bean definition per application context and reused for injection. Other scopes are available for different lifecycle requirements.

### Q11. What happens if Spring cannot find a required dependency?

**Sample answer:**  
If Spring cannot resolve a required dependency while creating a bean, application context startup may fail. I would inspect the exception, verify that the dependency is registered as a bean, check component scanning and review configuration.

### Q12. How does Dependency Injection work in the HabitFlow example?

**Sample answer:**  
`HabitService` is registered as a Spring bean using `@Service`. `HabitController` declares a constructor that accepts `HabitService`. When Spring creates the controller, it finds the service bean and passes it to the constructor. The controller can then call the service without creating it manually.

### Interview practice task

Answer these three questions aloud:

1. Explain IoC and DI, and how they are related.
2. Why do you prefer constructor injection over field injection?
3. Explain how Spring injects `HabitService` into `HabitController` in HabitFlow.

**Interview preparation tip:** Do not memorize the sample answers word for word. Understand the concept, explain the reason behind it and connect it to code you have written.

## 14. Next lesson – Week 1, Day 7

According to the Excel tracker, Day 7 covers two tasks:

1. Create a welcome endpoint using `@RestController` and `@GetMapping`.
2. Run the application and test the endpoint with Postman.

We will use that day to consolidate the endpoint and testing work from this week.

---

**Learning rule:** Do not just memorize annotations. Practise explaining why the controller should receive its service dependency instead of creating that service with `new`.
