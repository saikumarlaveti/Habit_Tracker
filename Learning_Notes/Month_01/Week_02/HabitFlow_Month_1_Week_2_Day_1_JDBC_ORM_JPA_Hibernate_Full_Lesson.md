# HabitFlow – Month 1, Week 2, Day 1

## Topic: JPA Fundamentals – JDBC, ORM, JPA and Hibernate

**Tracker task:** Understand JDBC, ORM, JPA and Hibernate and how they differ.

**Today's boundary:** This lesson is conceptual. Creating the local `habit_tracker` database and configuring the MySQL connection are separate tracker tasks. Entity mapping and repositories are also covered on later days.

## 1. Why do we need database persistence?

HabitFlow has Java objects in application memory, but the application must store habits permanently in MySQL. When the application stops, ordinary in-memory objects disappear. Database persistence allows the application to save information and retrieve it later.

Example habit:

| Field | Value |
|---|---|
| id | 1 |
| name | Read Java |
| description | Read Java for 30 minutes |

The Java application needs a way to communicate with the MySQL table that stores this data.

```text
Java application                 MySQL database
Habit object   <------------->   habits table
```

## 2. JDBC – Java Database Connectivity

JDBC stands for **Java Database Connectivity**. It is a Java API that lets Java applications connect to databases, execute SQL statements and process returned results.

With JDBC, developers generally write SQL themselves and handle the database interaction.

### Example

```java
String sql = "SELECT id, name FROM habits WHERE id = ?";

Connection connection = DriverManager.getConnection(
    "jdbc:mysql://localhost:3306/habit_tracker",
    "root",
    "password"
);

PreparedStatement statement = connection.prepareStatement(sql);
statement.setLong(1, 1L);

ResultSet resultSet = statement.executeQuery();

while (resultSet.next()) {
    System.out.println(resultSet.getLong("id"));
    System.out.println(resultSet.getString("name"));
}

resultSet.close();
statement.close();
connection.close();
```

This is an educational example. In production, do not hardcode credentials; use configuration or a secrets manager, and use try-with-resources to close JDBC resources safely.

### Important JDBC objects

| Object | Responsibility |
|---|---|
| `Connection` | Represents a connection to the database |
| `PreparedStatement` | Executes a parameterized SQL statement |
| `ResultSet` | Holds rows returned by a query |
| `DriverManager` | Helps obtain a database connection |

### JDBC workflow

1. Obtain a database connection.
2. Prepare an SQL statement.
3. Set any query parameters.
4. Execute the statement.
5. Read the returned `ResultSet`, if applicable.
6. Close resources.

**Advantages:** direct SQL control, broad database support, and predictable access to SQL features.

**Trade-offs:** more boilerplate; developers often need to map result rows to Java objects and manage SQL and resources themselves.

## 3. ORM – Object-Relational Mapping

ORM stands for **Object-Relational Mapping**. It is a technique for mapping object-oriented program structures to relational database structures.

| Java concept | Relational database concept |
|---|---|
| Class | Table |
| Object / entity instance | Row |
| Field / property | Column |
| Object property value | Column value |

For example, a Java class might represent a habit:

```java
public class Habit {
    private Long id;
    private String name;
    private String description;
}
```

A relational table might store the same information:

```sql
CREATE TABLE habits (
    id BIGINT PRIMARY KEY,
    name VARCHAR(100),
    description VARCHAR(255)
);
```

An ORM tool handles much of the conversion between Java objects and database rows. ORM is a technique, not one specific Java library.

## 4. JPA – Java Persistence API

JPA is a **Java specification** that defines standard APIs and rules for persistence and object-relational mapping. It describes how Java applications can map entities, persist them, retrieve them and manage their relationships.

JPA defines annotations and APIs such as:

- `@Entity`
- `@Table`
- `@Id`
- `@GeneratedValue`
- `@Column`
- `EntityManager`

Example:

```java
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
}
```

In this example:

- `@Entity` marks the class as a JPA entity.
- `@Table(name = "habits")` specifies the database table name.
- `@Id` marks the entity's primary-key field.

This is only an introductory mapping example. Constructors, accessors, identifier generation and other mapping details will be covered in the later entity-mapping tasks.

**Important:** JPA is a specification. It defines contracts; a persistence provider supplies the implementation.

## 5. Hibernate

Hibernate is an ORM framework and a widely used **implementation of the JPA specification**. It provides the persistence-provider functionality that performs object mapping and database operations.

For example, when application code asks a JPA provider to persist a `Habit`, Hibernate can generate SQL and execute it against the relational database.

Hibernate also has features beyond the JPA standard. When writing portable application code, prefer standard JPA APIs unless you specifically need a Hibernate-specific feature.

## 6. How the pieces work together

A simplified conceptual flow in a typical Spring Boot application is:

```text
Your application code (Habit object)
              |
              v
JPA APIs / persistence contract
              |
              v
Hibernate (JPA provider and ORM)
              |
              v
JDBC (database connectivity)
              |
              v
MySQL database
```

This is a simplified view. Hibernate commonly uses JDBC underneath to communicate with relational databases. Spring Data JPA, which we will use later, builds on JPA to provide repository abstractions and reduce repetitive persistence code.

## 7. Quick comparison

| Term | What it is | Main purpose |
|---|---|---|
| JDBC | Java API | Connect to a database and execute SQL |
| ORM | Technique | Map objects to relational tables |
| JPA | Specification | Define standard persistence APIs and rules |
| Hibernate | ORM framework / JPA provider | Implement persistence and object mapping |

Memory aid:

- **JDBC:** connectivity and SQL execution.
- **ORM:** object-to-table mapping technique.
- **JPA:** standard persistence contract.
- **Hibernate:** a provider that implements JPA and performs ORM work.

## 8. Why use Spring Data JPA in HabitFlow?

Without an ORM, application code may repeatedly execute SQL, read result sets and manually convert rows into Java objects. With Spring Data JPA, you can work with entity objects and repository interfaces for common persistence operations.

For example, a later HabitFlow task will introduce:

```java
public interface HabitRepository
        extends JpaRepository<Habit, Long> {
}
```

A repository can then save an entity:

```java
habitRepository.save(habit);
```

Spring Data JPA provides the repository abstraction. The JPA provider (for example, Hibernate) performs the persistence work, commonly using JDBC underneath. You will create this repository on the scheduled Repository task, not today.

## 9. Practical learning activities

1. Explain in your own words why a Java application needs database persistence.
2. Write the full forms of JDBC, ORM and JPA.
3. Identify the Java-to-database mapping in the example: `Habit` class, `name` field, and one `Habit` object.
4. Explain why JPA is called a specification and Hibernate an implementation/provider.
5. Draw the simplified flow from Java application code to MySQL, including JPA, Hibernate and JDBC.
6. Explain what `Connection`, `PreparedStatement` and `ResultSet` do.

**Do not configure MySQL or create the entity/repository as part of today's assignment.** Those are separate rows in your tracker.

## 10. Knowledge check

Try answering before looking at the answer key.

1. What does JDBC stand for?
   - A. Java Database Connectivity
   - B. Java Data Class
   - C. Java Database Compiler
   - D. Java Development Connection

2. Which term describes mapping Java objects to relational tables?
   - A. JDBC
   - B. ORM
   - C. JVM
   - D. HTTP

3. What is JPA?
   - A. A database server
   - B. A Java persistence specification
   - C. A programming language
   - D. A JDBC driver

4. Which is a popular implementation of JPA?
   - A. Maven
   - B. Tomcat
   - C. Hibernate
   - D. Postman

5. Which JDBC object represents a database connection?
   - A. ResultSet
   - B. Connection
   - C. Entity
   - D. JpaRepository

6. Which JDBC object holds rows returned by a query?
   - A. ResultSet
   - B. DriverManager
   - C. PreparedStatement
   - D. EntityManagerFactory

7. Does JPA itself mandate one specific implementation?
   - A. Yes
   - B. No

8. In the simplified common stack, which technology is commonly used underneath Hibernate to communicate with a relational database?
   - A. HTML
   - B. JDBC
   - C. CSS
   - D. React

### Answer key with explanations

1. **A** – JDBC means Java Database Connectivity.
2. **B** – ORM is the object-relational mapping technique.
3. **B** – JPA defines standard persistence APIs and rules.
4. **C** – Hibernate is a widely used JPA provider and ORM framework.
5. **B** – `Connection` represents a database connection.
6. **A** – `ResultSet` contains the rows returned by a query.
7. **B** – JPA is a specification; providers such as Hibernate implement it.
8. **B** – Hibernate commonly uses JDBC for relational database communication.

## 11. Completion checklist

Do not mark this tracker task as Done until you can explain these concepts yourself.

- [ ] I can explain why HabitFlow needs database persistence.
- [ ] I can explain JDBC and the roles of `Connection`, `PreparedStatement` and `ResultSet`.
- [ ] I can explain ORM using the Java class/table example.
- [ ] I can explain why JPA is a specification.
- [ ] I can explain Hibernate's role as a JPA provider.
- [ ] I can draw and explain the simplified application-to-MySQL flow.
- [ ] I completed the knowledge check and reviewed any mistakes.

## 12. Key takeaways

1. JDBC provides Java database connectivity and SQL execution APIs.
2. ORM is a technique for mapping objects to relational tables.
3. JPA is a standard Java persistence specification.
4. Hibernate is a popular ORM framework and JPA provider.
5. Spring Data JPA offers repository abstractions; it does not replace the database or JDBC driver.

## 13. Next scheduled tracker task

**Week 2 – Day 2: MySQL setup**

Create the local `habit_tracker` database and configure the MySQL connection. Keep this as the next task; do not combine it with today's JPA fundamentals task.

## 14. Interview questions and sample answers

### 1. What is JDBC?
JDBC stands for Java Database Connectivity. It is a Java API that allows an application to connect to a database, execute SQL statements and process query results.

### 2. What is ORM?
ORM stands for Object-Relational Mapping. It is a technique that maps object-oriented structures, such as Java classes and objects, to relational database tables and rows.

### 3. What is JPA?
JPA is a Java persistence specification. It defines standard APIs, annotations and rules for mapping and managing relational data in Java applications.

### 4. Is JPA an implementation?
No. JPA is a specification. A persistence provider such as Hibernate implements the specification.

### 5. What is Hibernate?
Hibernate is an ORM framework and a popular JPA provider. It maps Java objects to database records and performs persistence operations. It commonly uses JDBC to communicate with relational databases.

### 6. What is the difference between JPA and Hibernate?
JPA defines the standard persistence contract, while Hibernate is a framework that implements that contract and provides ORM functionality. Hibernate also offers additional provider-specific features.

### 7. What is the difference between JDBC and Hibernate?
With JDBC, developers generally write SQL and handle result mapping and resource management directly. Hibernate provides ORM functionality so developers can work with entity objects and let the provider handle much of the SQL generation and mapping.

### 8. What is the relationship between ORM and JPA?
ORM is the general object-relational mapping technique. JPA is a Java specification that standardizes APIs and annotations for ORM and persistence in Java applications.

### 9. What are `Connection`, `PreparedStatement` and `ResultSet`?
`Connection` represents a database connection. `PreparedStatement` represents a parameterized SQL statement. `ResultSet` holds rows returned by a query.

### 10. Does Hibernate replace JDBC completely?
No. Hibernate commonly uses JDBC internally to communicate with relational databases. It provides a higher-level ORM and persistence abstraction.

### 11. What is Spring Data JPA?
Spring Data JPA is a Spring project that provides repository abstractions on top of JPA. It reduces repetitive data-access code by providing methods and query support through repository interfaces.

### 12. Why might a project use Spring Data JPA with Hibernate?
Spring Data JPA provides convenient repository interfaces, JPA provides standard persistence APIs, Hibernate can implement those APIs and perform ORM, and JDBC provides the underlying database connectivity.
