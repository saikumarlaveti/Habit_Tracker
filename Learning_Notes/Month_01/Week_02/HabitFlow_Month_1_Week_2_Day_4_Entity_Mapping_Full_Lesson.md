# HabitFlow – Month 1, Week 2, Day 4
## Entity Mapping: Create the Habit Entity and Understand `@Entity`, `@Table` and `@Id`

**Official tracker task:** Create the Habit entity and understand `@Entity`, `@Table` and `@Id`  
**Suggested duration:** 60 minutes  
**Project:** HabitFlow – Habit Tracker  
**Prerequisites:** Week 2 Day 1 – JDBC, ORM, JPA and Hibernate; Day 2 – MySQL database setup; Day 3 – `application.yml` and externalized credentials

---

## 1. Learning objectives

By the end of this lesson, you should be able to:

1. Explain what a JPA entity is.
2. Explain how a Java class maps to a database table.
3. Use `@Entity` to mark a class as a JPA entity.
4. Use `@Table` to specify the database table name.
5. Use `@Id` to identify an entity's primary key.
6. Create a basic `Habit` entity class for HabitFlow.
7. Understand why an entity needs a primary key.

**Today's boundary:** Today's tracker task covers `@Entity`, `@Table` and `@Id`. `@GeneratedValue`, `@Column` and Java-to-SQL type mapping are the next task (Week 2 Day 5). Do not skip ahead.

---

## 2. What is an Entity?

An entity is a Java class that represents data that can be stored in a relational database.

Imagine a HabitFlow user creates this habit:

| id | name | description |
|---:|---|---|
| 1 | Read Java | Read Java for 30 minutes |

In Java, we want to represent that information as an object. In MySQL, we store it as a row in a table.

### Java class and database table

```text
Java application                  MySQL database

Habit                             habits
+------------------+              +------------------+
| id               |   mapping    | id               |
| name             | <----------> | name             |
| description      |              | description      |
+------------------+              +------------------+
```

JPA defines the persistence mapping APIs and annotations. Hibernate is a common JPA provider that implements the persistence behavior.

### Object-to-relational mapping terms

| Java concept | Relational database concept |
|---|---|
| Class | Table |
| Object | Row |
| Field / property | Column |
| Object's field value | Value in a column |

This is the basic idea behind Object-Relational Mapping (ORM).

---

## 3. What is JPA?

JPA stands for Java Persistence API (now commonly referred to as Jakarta Persistence in modern Jakarta-based applications).

It defines a standard way for Java applications to map objects to relational data and manage their persistence.

JPA includes annotations such as:

- `@Entity`
- `@Table`
- `@Id`
- `@GeneratedValue`
- `@Column`

Today's lesson focuses on the first three.

In a modern Spring Boot project using Jakarta Persistence, import annotations from:

```java
jakarta.persistence
```

Do not mix `javax.persistence` imports from older examples with `jakarta.persistence` imports in a Jakarta-based project.

---

## 4. Understand `@Entity`

`@Entity` marks a Java class as a JPA entity. Its instances can be managed and persisted by a JPA provider.

### Basic example

```java
import jakarta.persistence.Entity;

@Entity
public class Habit {

}
```

Explanation:

- `import jakarta.persistence.Entity;` imports the JPA annotation.
- `@Entity` marks the class as an entity.
- `public class Habit` declares the Java class.

An entity needs a primary key. We will add that with `@Id shortly.

### Important points

1. An entity is a regular Java class with persistence mapping metadata.
2. The JPA provider manages entity instances when they participate in persistence operations.
3. A JPA entity must have a primary key.
4. An entity class should have a public or protected no-argument constructor. Java supplies a default no-argument constructor if you do not declare any constructor; if you declare another constructor, provide a no-argument constructor as well.

---

## 5. Understand `@Table`

`@Table` specifies the database table associated with an entity.

### Example

```java
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "habits")
public class Habit {

}
```

Explanation:

- `@Entity` marks `Habit` as a JPA entity.
- `@Table(name = "habits")` explicitly maps the entity to the table named `habits`.

### Why specify a table name?

The Java class is named `Habit`, while the database table is named `habits`.

Explicitly specifying the table name makes the intended mapping clear and avoids depending on a provider's default naming behavior.

If `@Table` is omitted, JPA uses default table-name mapping rules based on the entity name and provider configuration.

---

## 6. Understand `@Id`

`@Id` identifies the primary key field or property of an entity.

A primary key uniquely identifies each row in a database table.

### Example data

| id | name |
|---:|---|
| 1 | Read Java |
| 2 | Exercise |
| 3 | Learn Spring Boot |

Each row has a unique `id`.

### Java example

```java
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "habits")
public class Habit {

    @Id
    private Long id;

}
```

Explanation:

- `@Id` marks `id` as the entity's primary key.
- `Long` is a Java wrapper type suitable for representing a numeric identifier.
- `private` restricts direct access to the field from other classes.

### Why is a primary key important?

Suppose HabitFlow has these records:

| id | name |
|---:|---|
| 1 | Read Java |
| 2 | Exercise |

If the user wants to update the Exercise habit, the application can identify that record by its unique ID (`2`).

Without a reliable primary key, it would be difficult to identify a particular entity record unambiguously.

---

## 7. Create the HabitFlow `Habit` entity

Now combine the three annotations.

### Suggested package structure

Use the base package that matches your actual Spring Boot application. For example:

```text
src/main/java/com/habitflow/
├── HabitflowBackendApplication.java
└── entity/
    └── Habit.java
```

Create a package named `entity`, then create `Habit.java`.

### Complete code for today's task

```java
package com.habitflow.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "habits")
public class Habit {

    @Id
    private Long id;

    private String name;

    private String description;

    public Habit() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
```

### Understand the code

| Code | Meaning |
|---|---|
| `package com.habitflow.entity;` | Places the class in the entity package |
| `import jakarta.persistence.Entity;` | Imports the entity annotation |
| `import jakarta.persistence.Id;` | Imports the primary-key annotation |
| `import jakarta.persistence.Table;` | Imports the table annotation |
| `@Entity` | Marks the class as a JPA entity |
| `@Table(name = "habits")` | Maps the entity to the `habits` table |
| `@Id` | Identifies the primary key |
| `private Long id;` | Declares the identifier field |
| `private String name;` | Declares the habit name |
| `private String description;` | Declares the description |
| `public Habit() {}` | Provides a no-argument constructor |
| Getters and setters | Provide access to the private fields |

### Important note about today's `id`

For this lesson, we declare the ID and mark it with `@Id`. We have not yet configured automatic ID generation. That is the next tracker task, where we will learn `@GeneratedValue`.

Do not assume Hibernate will automatically generate the ID just because `@Id` is present.

---

## 8. How the mapping looks conceptually

The entity metadata tells the persistence provider how the Java class relates to the database table.

```text
Habit.java
   |
   | @Entity
   v
JPA recognizes Habit as an entity
   |
   | @Table(name = "habits")
   v
Maps it to the habits table
   |
   | @Id on id
   v
Identifies the primary key
```

The fields `name` and `description` are persistent fields by default in this field-access example. Their detailed column configuration and Java-to-SQL type mapping are covered on Day 5.

---

## 9. Common mistakes and troubleshooting

| Mistake or error | Why it happens | What to check |
|---|---|---|
| `package jakarta.persistence does not exist` | JPA dependency is missing or Maven has not resolved it | Check `spring-boot-starter-data-jpa` in `pom.xml`, then reload Maven |
| Import uses `javax.persistence` | Code copied from an older Java/Jakarta tutorial | Use `jakarta.persistence` for a Jakarta-based project |
| Entity has no identifier | `@Id` is missing | Add `@Id` to the primary-key field or property |
| Entity class is not detected | Package is outside the application component/entity scan scope | Put it under the application's base package or configure scanning deliberately |
| Table name differs from expectation | Table naming was left to defaults or mapping is incorrect | Check `@Table(name = "habits")` |
| No table appears in MySQL | Schema generation may not be enabled, or no schema migration has run | Do not assume the entity annotation alone creates a table; inspect JPA schema settings and logs |

**Important:** Do not change `ddl-auto` settings just to force table creation today. Today's learning objective is entity mapping annotations. Schema generation and migrations should be configured deliberately.

---

## 10. Practical assignment

Complete the following yourself.

### Task 1 – Create the package

Under your application's base package, create:

```text
com.habitflow.entity
```

Adjust the package prefix if your actual project uses a different base package.

### Task 2 – Create `Habit.java`

Add:

- `@Entity`
- `@Table(name = "habits")`
- `@Id`
- `Long id`
- `String name`
- `String description`
- A no-argument constructor
- Getters and setters

### Task 3 – Explain the annotations

Write your own explanation of:

1. `@Entity`
2. `@Table`
3. `@Id`

### Task 4 – Understand the mapping

Explain how the following concepts relate:

- `Habit` class
- `habits` table
- `id` field
- Primary key

### Task 5 – Review your imports

Confirm that the annotations use `jakarta.persistence` in your Jakarta-based Spring Boot project.

Do not create the repository or save a habit record yet. Those are later tracker tasks.

---

## 11. Knowledge check – 10 MCQs

Try to answer these before checking the answer key.

**Q1. What does `@Entity` do?**

A. Marks a class as a JPA entity  
B. Starts the MySQL server  
C. Creates a REST endpoint  
D. Defines a Maven dependency

**Q2. Which annotation identifies the primary key of an entity?**

A. `@Table`  
B. `@Id`  
C. `@Column`  
D. `@Service`

**Q3. What is the purpose of `@Table(name = "habits")`?**

A. Names the Java package  
B. Maps the entity to the `habits` database table  
C. Creates a MySQL user  
D. Declares a REST route

**Q4. In ORM, a Java class generally maps to which database concept?**

A. Row  
B. Column  
C. Table  
D. Database server

**Q5. In ORM, a Java object generally maps to which database concept?**

A. Row  
B. Database server  
C. Index  
D. Schema name

**Q6. Which import is used in a modern Jakarta Persistence project?**

A. `java.sql.Entity`  
B. `jakarta.persistence.Entity`  
C. `org.mysql.Entity`  
D. `java.persistence.Entity`

**Q7. Why does a JPA entity need a primary key?**

A. To identify each entity record  
B. To start the application  
C. To configure the server port  
D. To connect to Postman

**Q8. What is the role of `@Id` in the following code?**

```java
@Id
private Long id;
```

A. It marks `id` as the primary key  
B. It creates a controller  
C. It marks `id` as a database password  
D. It starts Hibernate

**Q9. If the Java class is named `Habit` and the desired table name is `habits`, which annotation makes the mapping explicit?**

A. `@Entity(name = "habits")`  
B. `@Table(name = "habits")`  
C. `@Id(name = "habits")`  
D. `@Repository(name = "habits")`

**Q10. Does `@Id` by itself configure automatic ID generation?**

A. Yes, always  
B. No, automatic generation is configured separately  
C. Only when using Postman  
D. Only when the table is empty

### MCQ answer key

| Question | Correct answer | Explanation |
|---|---|---|
| Q1 | A | `@Entity` marks a class as a JPA entity. |
| Q2 | B | `@Id` identifies the primary key. |
| Q3 | B | `@Table` specifies the table mapping. |
| Q4 | C | A class generally maps to a table. |
| Q5 | A | An object generally maps to a row. |
| Q6 | B | Jakarta-based projects use `jakarta.persistence`. |
| Q7 | A | The primary key uniquely identifies an entity record. |
| Q8 | A | `@Id` marks the field as the identifier. |
| Q9 | B | `@Table(name = "habits")` explicitly specifies the table name. |
| Q10 | B | ID generation is a separate mapping configuration, covered next. |

---

## 12. Completion checklist

Mark each item only after you have completed it.

- [ ] I understand what a JPA entity is.
- [ ] I understand class-to-table mapping.
- [ ] I understand object-to-row mapping.
- [ ] I understand `@Entity`.
- [ ] I understand `@Table`.
- [ ] I understand `@Id`.
- [ ] I created the `Habit` entity class.
- [ ] I used `jakarta.persistence` imports.
- [ ] I added a no-argument constructor.
- [ ] I can explain why an entity needs a primary key.
- [ ] I completed the practical assignment and MCQs.
- [ ] I saved my notes in `Main_Notes_Branch`.

---

## 13. Save your notes in GitHub

Keep your learning documentation in the existing `Main_Notes_Branch`.

Suggested path:

```text
learning-notes/
└── Month-1/
    └── Week-2/
        └── Day-4.md
```

Example commands:

```bash
git switch Main_Notes_Branch
git status
git add learning-notes/Month-1/Week-2/Day-4.md
git commit -m "Add HabitFlow Month 1 Week 2 Day 4 entity mapping notes"
git push origin Main_Notes_Branch
```

Keep application code in `Main_Code_Branch` and learning notes in `Main_Notes_Branch`. Check for uncommitted work before switching branches.

---

## 14. Key takeaways

- An entity is a Java class mapped to relational data.
- `@Entity` marks a class as a JPA entity.
- `@Table` specifies the database table name.
- `@Id` identifies the entity's primary key.
- A class generally maps to a table; an object generally maps to a row.
- Modern Jakarta-based Spring Boot projects use `jakarta.persistence` imports.
- `@Id` does not by itself configure automatic ID generation.
- `@GeneratedValue` and `@Column` are the next tracker task.

---

## 15. Next scheduled lesson

**Month 1 – Week 2 – Day 5: Entity Mapping**

Understand `@GeneratedValue`, `@Column` and Java-to-SQL type mapping.

---

## 16. Interview questions and sample answers

**Q1. What is a JPA entity?**

A JPA entity is a Java class mapped to relational database data. Its instances can be managed by a JPA provider and persisted to a database.

**Q2. What does `@Entity` do?**

`@Entity` marks a Java class as a persistent entity that JPA can manage.

**Q3. What is the purpose of `@Table`?**

`@Table` specifies the database table associated with an entity. For example, `@Table(name = "habits")` maps the entity to the `habits` table.

**Q4. What is the purpose of `@Id`?**

`@Id` marks the entity's primary key field or property. The identifier distinguishes one entity record from another.

**Q5. What is the difference between `@Entity` and `@Table`?**

`@Entity` marks a class as a JPA entity. `@Table` specifies which relational database table that entity maps to.

**Q6. What is a primary key?**

A primary key is a column or set of columns that uniquely identifies each row in a database table. In JPA, `@Id` identifies the entity's primary key mapping.

**Q7. What is the relationship between a Java class and a database table in ORM?**

A Java class generally maps to a database table, and an instance of that class generally maps to a row in that table.

**Q8. Which package should be imported for persistence annotations in a modern Jakarta-based Spring Boot application?**

Use `jakarta.persistence`, for example `jakarta.persistence.Entity` and `jakarta.persistence.Id`.

**Q9. Does `@Id` automatically generate primary-key values?**

No. `@Id` identifies the primary key. Automatic value generation is configured separately, commonly with `@GeneratedValue` and an appropriate generation strategy.

**Q10. Why should an entity have a no-argument constructor?**

JPA providers need to instantiate entity objects. A public or protected no-argument constructor supports that requirement.

**Q11. Does adding `@Entity` always create a table immediately?**

No. The annotation supplies mapping metadata. Whether a table is created depends on the application's schema-generation configuration or a database migration process.

**Q12. What is the difference between an entity and a DTO?**

An entity represents persistent domain data and is managed by the persistence layer. A DTO (Data Transfer Object) is used to transfer data between application boundaries, such as between a REST API and a client. They serve different purposes.

---

**End of Month 1 – Week 2 – Day 4.**
