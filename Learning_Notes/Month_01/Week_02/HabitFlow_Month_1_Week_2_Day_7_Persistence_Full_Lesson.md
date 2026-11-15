# HabitFlow – Month 1, Week 2, Day 7
## Save and Retrieve a Habit and Verify It in MySQL Workbench

**Roadmap location:** Month 1 → Week 2 → Day 7  
**Tracker area:** Persistence  
**Exact tracker task:** Save and retrieve a habit and verify the row in MySQL Workbench

---

## 1. Today's learning objectives

By the end of this lesson, you should be able to:

1. Explain what persistence means.
2. Use `HabitRepository.save()` to store a Habit entity.
3. Retrieve a saved entity using `findById()` and `findAll()`.
4. Understand why `save()` returns the saved entity.
5. Verify the saved record directly in MySQL Workbench.
6. Understand the basic flow from a Java object to a database row.

**Today's boundary:** We are practising persistence with the entity and repository. We are not building the Controller-Service-Repository layered CRUD APIs today; those are scheduled for Week 3.

---

## 2. What is persistence?

Persistence means keeping application data beyond the lifetime of the Java object or application process.

For example, suppose a user creates this habit:

- Name: Read a book
- Description: Read for 20 minutes every day

While the application is running, these values can exist in a Java object in memory. If the application stops, an ordinary in-memory object disappears.

When the application saves the entity to MySQL, the data is stored in the database. It remains there after the application stops.

```text
Java Habit object
       |
       | habitRepository.save(habit)
       v
Spring Data JPA
       |
       v
Hibernate (JPA provider)
       |
       v
JDBC
       |
       v
MySQL table: habits
```

When you retrieve the record, the flow goes in the opposite direction: the persistence provider reads the row and maps it into a Java `Habit` object.

---

## 3. Review the Habit entity

Use the `Habit` entity you created on Week 2 Day 4 and updated on Day 5. A simplified example is shown below. Your actual entity may have additional fields or different package names; keep your existing project structure consistent.

```java
package com.habitflow.backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "habits")
public class Habit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "habit_name", nullable = false, length = 150)
    private String name;

    @Column(name = "description", length = 500)
    private String description;

    public Habit() {
    }

    public Habit(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
```

### Understand the important annotations

- `@Entity` tells JPA that this class is a persistent entity.
- `@Table(name = "habits")` maps the entity to the `habits` table.
- `@Id` identifies the primary-key field.
- `@GeneratedValue(strategy = GenerationType.IDENTITY)` asks the database to generate the identifier using the identity/auto-increment mechanism.
- `@Column` configures the mapping of a Java field to a database column.

The no-argument constructor is required by JPA. The second constructor is a convenience for creating a new Habit in our example.

If your entity already exists, do not replace it blindly. Compare the example with your class and add only what is missing.

---

## 4. Review HabitRepository

From Week 2 Day 6, you created:

```java
package com.habitflow.backend.repository;

import com.habitflow.backend.entity.Habit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HabitRepository extends JpaRepository<Habit, Long> {
}
```

The two generic parameters mean:

- `Habit`: the entity type.
- `Long`: the primary-key Java type.

Spring Data JPA supplies the implementation of common repository methods at runtime.

For today's exercise, we will use:

```java
save(habit);
findById(id);
findAll();
```

### What does `save()` do?

`save()` asks Spring Data JPA to persist the entity. It can be used for a new entity and for an existing entity that is being updated. The persistence provider determines whether the entity needs to be persisted or merged.

It returns the saved entity. For a newly created entity with a generated ID, inspect the returned object to obtain the generated identifier.

### What does `findById()` do?

`findById(id)` searches for the entity with that primary key. It returns an `Optional<Habit>` because a row with that ID might not exist.

### What does `findAll()` do?

`findAll()` retrieves all entities in the repository. It is useful for a small learning exercise, but avoid loading an unbounded table in a real application. Pagination is more appropriate for large result sets.

---

## 5. Check the database configuration

Before running the application, confirm that:

1. MySQL Server is running.
2. The `habit_tracker` database exists.
3. The application has the MySQL JDBC driver and Spring Data JPA dependencies.
4. Your database username and password are correct.
5. Your `application.yml` is valid YAML.

Example configuration:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/habit_tracker
    username: ${DB_USERNAME:root}
    password: ${DB_PASSWORD}
    driver-class-name: com.mysql.cj.jdbc.Driver

  jpa:
    show-sql: true
    hibernate:
      ddl-auto: update
    properties:
      hibernate:
        format_sql: true
```

Notes:

- YAML uses `:` with indentation, not Java-style `=` assignments.
- `${DB_USERNAME:root}` means use the `DB_USERNAME` environment variable, or `root` if it is not set.
- `${DB_PASSWORD}` expects a `DB_PASSWORD` environment variable. Set it in your run configuration or environment before starting the application.
- Do not commit real database passwords to Git.
- `show-sql` and formatted SQL are useful while learning.
- `ddl-auto: update` is convenient for local learning, but it is not a production database migration strategy. Later, the project will use a migration tool.

If you use a different database name or credentials, adjust the configuration to match your local MySQL setup.

---

## 6. Confirm that the database exists

Open MySQL Workbench and connect to your local MySQL Server.

Run:

```sql
CREATE DATABASE IF NOT EXISTS habit_tracker;
```

Then select it:

```sql
USE habit_tracker;
```

To confirm:

```sql
SHOW DATABASES;
```

You should see `habit_tracker` in the output.

If you already created the database on Week 2 Day 2, you do not need to create it again.

---

## 7. Save and retrieve a Habit

For this learning exercise, we will temporarily use `CommandLineRunner`. It runs code when the Spring Boot application starts.

This is a simple way to test persistence before we build the proper service and controller layers in Week 3.

Create a temporary class such as `HabitPersistenceDemo.java`:

```java
package com.habitflow.backend;

import com.habitflow.backend.entity.Habit;
import com.habitflow.backend.repository.HabitRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class HabitPersistenceDemo implements CommandLineRunner {

    private final HabitRepository habitRepository;

    public HabitPersistenceDemo(HabitRepository habitRepository) {
        this.habitRepository = habitRepository;
    }

    @Override
    public void run(String... args) {

        // 1. Create a new Habit object
        Habit habit = new Habit(
                "Read a book",
                "Read for 20 minutes every day"
        );

        // 2. Save the Habit in the database
        Habit savedHabit = habitRepository.save(habit);

        System.out.println("Saved Habit ID: " + savedHabit.getId());

        // 3. Retrieve the same Habit using its generated ID
        Optional<Habit> retrievedHabit =
                habitRepository.findById(savedHabit.getId());

        // 4. Display the retrieved result
        if (retrievedHabit.isPresent()) {
            Habit foundHabit = retrievedHabit.get();

            System.out.println("Habit found!");
            System.out.println("ID: " + foundHabit.getId());
            System.out.println("Name: " + foundHabit.getName());
            System.out.println("Description: " + foundHabit.getDescription());
        } else {
            System.out.println("Habit not found.");
        }
    }
}
```

### Understand the code step by step

**Step 1 – Inject the repository**

```java
private final HabitRepository habitRepository;

public HabitPersistenceDemo(HabitRepository habitRepository) {
    this.habitRepository = habitRepository;
}
```

Spring injects the repository bean through the constructor. This is constructor injection, which you studied in Week 1 Day 6.

**Step 2 – Create an entity object**

```java
Habit habit = new Habit(
        "Read a book",
        "Read for 20 minutes every day"
);
```

At this point, you have created a Java object. You have not explicitly saved it to the database yet.

**Step 3 – Save it**

```java
Habit savedHabit = habitRepository.save(habit);
```

The repository asks Spring Data JPA to persist the entity. Hibernate generates the necessary SQL and communicates with MySQL through JDBC.

Because the ID is generated by the database, the saved entity returned by `save()` can contain the generated ID.

**Step 4 – Retrieve by ID**

```java
Optional<Habit> retrievedHabit =
        habitRepository.findById(savedHabit.getId());
```

We use the ID returned from the saved entity rather than guessing an ID.

**Step 5 – Check the Optional**

```java
if (retrievedHabit.isPresent()) {
    Habit foundHabit = retrievedHabit.get();
}
```

`isPresent()` checks whether an entity was found. `get()` obtains the value when present. In later lessons, you will learn other Optional patterns too.

### Important: this is temporary demonstration code

Every time the application starts, this runner creates another habit row. That is intentional for demonstrating the flow, but it can create duplicate test records.

After you have successfully completed and verified the exercise:

- Remove the temporary `HabitPersistenceDemo` class, or disable it.
- Keep your `Habit` entity and `HabitRepository`.
- Do not leave startup demo inserts active in the finished application.

---

## 8. Optional: retrieve all habits

After the first save-and-retrieve test works, you can temporarily add:

```java
System.out.println("All habits:");

habitRepository.findAll().forEach(saved -> {
    System.out.println(
            saved.getId() + " | " +
            saved.getName() + " | " +
            saved.getDescription()
    );
});
```

This calls `findAll()` and prints each returned entity.

For a real application with many records, do not rely on loading the entire table. You will learn pagination when the API and application layers are developed.

---

## 9. Verify the row in MySQL Workbench

After the application starts successfully, open MySQL Workbench and run:

```sql
USE habit_tracker;

SELECT * FROM habits;
```

You should see a row containing values similar to:

| id | habit_name | description |
|---:|---|---|
| 1 | Read a book | Read for 20 minutes every day |

The ID shown may be different. It is generated by MySQL and can increase each time a new row is inserted.

If you want to inspect only the latest rows:

```sql
SELECT *
FROM habits
ORDER BY id DESC
LIMIT 10;
```

To inspect the table structure:

```sql
DESCRIBE habits;
```

**Important:** The actual column names depend on your entity mappings. If your entity maps `name` to a column named `name` rather than `habit_name`, use that actual column name in your expectations.

---

## 10. Understand what happened behind the scenes

When this line runs:

```java
Habit savedHabit = habitRepository.save(habit);
```

the overall process is:

```text
CommandLineRunner
      |
      v
HabitRepository.save(habit)
      |
      v
Spring Data JPA repository implementation
      |
      v
Hibernate maps entity fields to columns
      |
      v
JDBC sends SQL to MySQL
      |
      v
MySQL inserts the row
      |
      v
Generated ID is available on the saved entity
```

The exact SQL and timing depend on the JPA provider, transaction boundaries and database configuration. With `GenerationType.IDENTITY`, the database generates the ID for the inserted row.

When `findById()` runs, the persistence provider queries the database and maps the matching row back to a Java entity.

---

## 11. Common errors and troubleshooting

### Error 1: Communications link failure or connection refused

Possible causes:

- MySQL Server is not running.
- The hostname or port is incorrect.
- The JDBC URL points to the wrong database server.

Check that MySQL is running and that your URL is correct:

```yaml
url: jdbc:mysql://localhost:3306/habit_tracker
```

### Error 2: Access denied for user

Possible causes:

- Incorrect username.
- Incorrect password.
- The user does not have permission to access the database.

Check the credentials and permissions. Do not paste your real password into screenshots or public Git repositories.

### Error 3: Unknown database `habit_tracker`

Create it in MySQL Workbench:

```sql
CREATE DATABASE IF NOT EXISTS habit_tracker;
```

### Error 4: Table `habits` doesn't exist

Check:

- The class has `@Entity`.
- The entity is within the JPA entity-scan scope.
- The database selected by the JDBC URL is correct.
- The local schema-generation setting is configured as intended.
- Application startup logs do not show schema-generation errors.

For this local learning exercise, `ddl-auto: update` can create or update mapped tables. It should not be treated as a production migration process.

### Error 5: `Table 'habits' doesn't exist` even though the entity is present

The entity may not be discovered, the table name may differ, or schema generation may be disabled or failing. Inspect the startup logs and the actual database/schema selected by the application.

### Error 6: `HabitRepository` bean not found

Check that:

- The Spring Data JPA dependency is included.
- The repository interface extends the correct `JpaRepository`.
- The repository package is under the application's scan scope, or is explicitly configured.
- The application starts from the expected base package.

### Error 7: Duplicate rows appear

The `CommandLineRunner` runs every time the application starts. It creates a new row each time. Remove or disable the temporary runner after testing.

### Error 8: Entity has no suitable constructor

JPA requires a no-argument constructor. Make sure your entity has one with at least package or public visibility.

---

## 12. Practical assignment

Complete these tasks in order.

1. Confirm that the `habit_tracker` database exists in MySQL.
2. Confirm that your `Habit` entity is correctly mapped.
3. Confirm that `HabitRepository` extends `JpaRepository<Habit, Long>` (if your entity ID is `Long`).
4. Create the temporary `HabitPersistenceDemo` runner.
5. Start the Spring Boot application.
6. Check the console for the generated Habit ID and retrieved Habit details.
7. Open MySQL Workbench.
8. Run `SELECT * FROM habits;`.
9. Confirm that the saved Habit appears as a database row.
10. Remove or disable the temporary runner after successful verification.

### Expected result

You should be able to demonstrate both:

- The Java application saves a Habit and retrieves it by its generated ID.
- MySQL Workbench shows the matching database row.

Do not mark the tracker task complete until you have verified the saved record in MySQL Workbench.

---

## 13. Knowledge check – 10 MCQs

Try to answer before reading the answer key.

**Q1. What does persistence mean?**

A. Keeping data only in a local Java variable  
B. Storing data so it remains available beyond the current application execution  
C. Closing the application  
D. Creating a Java package

**Q2. Which repository method is used to save a Habit entity?**

A. `insertRowOnly()`  
B. `save()`  
C. `storeInMemory()`  
D. `createTable()`

**Q3. What does `findById()` return?**

A. `Optional<Habit>`  
B. `String`  
C. `void`  
D. `Connection`

**Q4. Why do we use the ID returned by `save()` in this exercise?**

A. It is always zero  
B. It is the generated identifier for the saved entity  
C. It is the database password  
D. It is the table name

**Q5. Which SQL statement retrieves all rows from the habits table?**

A. `SHOW DATABASES;`  
B. `DELETE FROM habits;`  
C. `SELECT * FROM habits;`  
D. `CREATE TABLE habits;`

**Q6. What does `@GeneratedValue(strategy = GenerationType.IDENTITY)` commonly use with MySQL?**

A. A database identity/auto-increment value  
B. The habit name  
C. A manually entered description  
D. A random table name

**Q7. What is the purpose of `CommandLineRunner` in today's exercise?**

A. It creates a browser page  
B. It runs demonstration code when the Spring Boot application starts  
C. It replaces MySQL  
D. It creates a Git branch

**Q8. Why can repeated application starts create duplicate demo rows?**

A. `findById()` always inserts rows  
B. The runner executes on each start and calls `save()` with a new entity  
C. MySQL always duplicates every query  
D. `@Entity` inserts two rows automatically

**Q9. Which tool are we using to inspect the stored database row?**

A. MySQL Workbench  
B. Notepad  
C. GitHub Desktop  
D. Postman only

**Q10. After verifying the temporary demo, what should you do with the runner?**

A. Keep inserting a new demo row on every startup forever  
B. Remove or disable the temporary runner  
C. Delete the repository interface  
D. Remove the entity

### Answer key with explanations

1. **B** – Persistence stores data so it survives beyond the current in-memory object or process.
2. **B** – `save()` is the standard repository method for saving an entity.
3. **A** – `Optional` represents the possibility that no entity exists for that ID.
4. **B** – The generated ID is available on the saved entity returned by `save()`.
5. **C** – `SELECT * FROM habits;` retrieves the rows and columns from the table.
6. **A** – MySQL commonly uses an auto-increment identity column for this strategy.
7. **B** – `CommandLineRunner` runs code after application startup.
8. **B** – The runner executes again on each startup and creates a new entity.
9. **A** – MySQL Workbench lets you run SQL and inspect the database.
10. **B** – The runner is temporary demonstration code, not part of the intended finished application.

---

## 14. Interview questions and sample answers

### 1. What is persistence in a Spring Boot application?

Persistence is the process of storing application data in a durable data store such as MySQL so that it remains available after the application stops or restarts.

### 2. How do you save an entity using Spring Data JPA?

Inject the repository and call its `save()` method with the entity. For example:

```java
Habit savedHabit = habitRepository.save(habit);
```

### 3. What does the `save()` method return?

It returns the saved entity. For a new entity using a database-generated identifier, the returned entity can contain the generated ID.

### 4. What is the difference between `save()` and `findById()`?

`save()` persists a new or changed entity. `findById()` searches for an entity using its primary key and returns an `Optional`.

### 5. Why does `findById()` return an Optional?

The requested ID might not exist. `Optional` represents either a found entity or an empty result without returning `null` as the normal result.

### 6. What is `GenerationType.IDENTITY`?

It is a JPA identifier-generation strategy that relies on a database identity column. In MySQL, this is commonly implemented with `AUTO_INCREMENT`.

### 7. How can you verify that an entity was saved successfully?

I can inspect the saved entity and its generated ID in the application, then query the mapped table in MySQL Workbench using a `SELECT` statement.

### 8. What is the role of Hibernate when saving an entity?

Hibernate is commonly the JPA provider. It maps the entity to database columns, generates the required SQL and coordinates persistence through JDBC.

### 9. What is the role of JDBC in this flow?

JDBC is the Java API used by database drivers to communicate with relational databases. Hibernate uses JDBC to execute database operations.

### 10. What is `CommandLineRunner`?

`CommandLineRunner` is a Spring Boot interface whose `run()` method is invoked after the application context starts. It is useful for startup tasks and, as in this lesson, temporary demonstrations.

### 11. Why should we not keep a demo `CommandLineRunner` that inserts a row in the finished application?

It runs on each application startup. If it inserts a new entity every time, it can create duplicate records. Startup demo code should be removed or made deliberately idempotent.

### 12. Does `ddl-auto: update` replace a production database migration strategy?

No. It can be convenient for local development, but production schema changes should be controlled through explicit database migration scripts or a migration tool.

---

## 15. Completion checklist

- [ ] I understand what persistence means.
- [ ] I confirmed the `habit_tracker` database exists.
- [ ] I reviewed my `Habit` entity.
- [ ] I reviewed my `HabitRepository`.
- [ ] I used `save()` to save a Habit.
- [ ] I retrieved the saved Habit using `findById()`.
- [ ] I understood the `Optional` result.
- [ ] I verified the matching row in MySQL Workbench.
- [ ] I removed or disabled the temporary runner.
- [ ] I attempted the MCQs and reviewed the explanations.
- [ ] I can explain the entity → repository → Hibernate → JDBC → MySQL flow.

Only mark Week 2 Day 7 as Done in the tracker after completing the practical verification.

---

## 16. Git instructions

This project uses one Git repository with two branches:

- `Main_Code_Branch` – application code.
- `Main_Notes_Branch` – learning notes and documentation.

Suggested notes location:

```text
learning-notes/
└── Month-1/
    └── Week-2/
        └── Day-7.md
```

Commit the temporary demo only if you intentionally want to preserve it for learning; otherwise remove it before committing the finished code. Keep the entity and repository in `Main_Code_Branch`. Save this Markdown lesson in `Main_Notes_Branch`.

---

## 17. Key takeaways

- Persistence stores data beyond the lifetime of an in-memory Java object.
- `HabitRepository.save()` persists the entity.
- `save()` returns the saved entity, allowing you to inspect its generated ID.
- `findById()` returns an `Optional` because the requested record might not exist.
- MySQL Workbench can independently verify the row with a `SELECT` query.
- A temporary `CommandLineRunner` is useful for this exercise, but remove or disable it after verification.

## 18. Next scheduled topic

**Month 1 – Week 3 – Day 1: Layered architecture**

Exact tracker task: **Understand Controller, Service and Repository responsibilities.**

Next week, we will start organizing the application into layers. We will not jump ahead and build the complete CRUD APIs until the tracker reaches those tasks.
