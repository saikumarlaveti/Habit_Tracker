# HabitFlow – Month 1, Week 2, Day 2
## MySQL Setup: Create the Local `habit_tracker` Database and Configure the MySQL Connection

**Official tracker task:** Create the local `habit_tracker` database and configure the MySQL connection  
**Suggested duration:** 60 minutes  
**Prerequisite:** Week 2, Day 1 – JDBC, ORM, JPA and Hibernate

## 1. Learning objectives

By the end of this lesson, you should be able to:
1. Explain why HabitFlow needs a database.
2. Distinguish a MySQL server, database and table.
3. Connect to your local MySQL server using MySQL Workbench.
4. Create and verify the `habit_tracker` database.
5. Understand the connection details used by a Java application.
6. Explain a MySQL JDBC URL.

**Today's boundary:** Create the database and verify the local MySQL connection using Workbench. Do not configure Spring Boot's `application.yml` or externalize credentials yet; that is Week 2, Day 3 in your tracker.

## 2. Why does HabitFlow need MySQL?

HabitFlow must preserve data after the backend stops or the computer restarts. It will eventually store users, habits, schedules, daily completions, reminders and notifications.

Java objects exist in application memory while the application runs. A relational database stores information persistently.

```text
HabitFlow frontend
        |
        | HTTP request
        v
Spring Boot backend
        |
        v
JPA / Hibernate
        |
        | JDBC communication
        v
MySQL server
        |
        v
habit_tracker database
```

## 3. MySQL server, database and table

- **MySQL server:** The database management system process that accepts connections and manages databases.
- **Database:** A logical container for related database objects.
- **Table:** A structure inside a database that stores data in rows and columns.

HabitFlow's planned structure:

```text
MySQL Server (local)
|
+-- habit_tracker (database)
    |
    +-- users (future table)
    +-- habits (future table)
    +-- habit_schedules (future table)
    +-- habit_completions (future table)
    +-- reminders (future table)
    +-- notifications (future table)
```

We are creating only the database today, not these tables.

## 4. What is MySQL Workbench?

MySQL Workbench is a graphical client for working with MySQL servers. It can connect to a server, execute SQL, create databases and tables, and inspect database objects.

## 5. Connect to your local MySQL server

1. Open the Windows Start menu and launch **MySQL Workbench**.
2. Find your saved local MySQL connection.
3. Double-click it and enter your MySQL password if prompted.
4. Wait for the SQL Editor to open.

Typical local connection settings (your installation may differ):

| Setting | Typical value |
|---|---|
| Connection method | Standard TCP/IP |
| Hostname | `127.0.0.1` or `localhost` |
| Port | `3306` |
| Username | Often `root`, or the account you created |

Run this query:

```sql
SELECT VERSION();
```

A successful result displays the installed MySQL server version.

You can also inspect the connection identity:

```sql
SELECT USER(), CURRENT_USER();
```

## 6. Create the `habit_tracker` database

In the Workbench SQL Editor, run:

```sql
CREATE DATABASE habit_tracker;
```

A safer repeatable version is:

```sql
CREATE DATABASE IF NOT EXISTS habit_tracker;
```

`IF NOT EXISTS` avoids an error if a database with that name already exists. It does not recreate or clear an existing database.

**Safety:** Do not use `DROP DATABASE` for this exercise. Do not delete an existing database to make this lesson work.

### Alternative: use the Workbench interface

1. Find the **Schemas** panel.
2. Right-click inside it and select **Create Schema**.
3. Enter `habit_tracker`.
4. Select **Apply**, review the generated SQL, then select **Apply** again.
5. Select **Finish**.

In MySQL, *schema* and *database* are often used interchangeably in this context. Use either the SQL method or the GUI method; you do not need both.

## 7. Verify and select the database

List databases:

```sql
SHOW DATABASES;
```

You should see `habit_tracker`.

You can also verify it with:

```sql
SELECT SCHEMA_NAME
FROM INFORMATION_SCHEMA.SCHEMATA
WHERE SCHEMA_NAME = 'habit_tracker';
```

Select the database for the current SQL session:

```sql
USE habit_tracker;
```

Check the selected database:

```sql
SELECT DATABASE();
```

Expected result:

```text
habit_tracker
```

The database will not contain HabitFlow tables yet. That is expected; table creation is covered by later tracker tasks.

## 8. Understand the connection details

A Java application needs to know where the MySQL server is and which database to use.

| Detail | Example | Meaning |
|---|---|---|
| Host | `localhost` | MySQL server is on this computer |
| Port | `3306` | TCP port used by MySQL in this example |
| Database | `habit_tracker` | Database the application will access |
| Username | `root` | MySQL account used for the connection |
| Password | Your own password | Credential for that account |

Use your actual installation settings; these are examples.

### What is a JDBC URL?

A typical MySQL JDBC URL is:

```text
jdbc:mysql://localhost:3306/habit_tracker
```

- `jdbc` – Java Database Connectivity.
- `mysql` – identifies the MySQL driver protocol.
- `localhost` – database server host.
- `3306` – server port in this example.
- `habit_tracker` – database name.

This is an example for understanding. We will put Spring Boot datasource settings into `application.yml` in Week 2, Day 3.

**Security:** Never publish your real database password in GitHub, screenshots, shared notes or source code.

## 9. Common errors and troubleshooting

| Error or symptom | Possible cause | What to check |
|---|---|---|
| Cannot connect to server | MySQL service is stopped | Check the MySQL Windows service |
| Connection refused | Wrong host/port or server not listening | Verify hostname and port |
| Access denied | Incorrect credentials or insufficient privileges | Check the MySQL account and password |
| Unknown database | Database has not been created on this server | Run `SHOW DATABASES;` |
| Schema not visible | Workbench list needs refreshing | Refresh the Schemas panel |
| Port 3306 fails | Server uses another port | Check your server configuration |

If Workbench cannot connect, you can inspect the Windows service:

1. Press `Windows + R`.
2. Enter `services.msc`.
3. Find the MySQL service (its name may include a version or instance suffix).
4. Check whether its status is **Running**.
5. If it is stopped, start it if you have permission.

Do not reset passwords or change server configuration without understanding your existing installation.

## 10. Practical assignment

Complete these tasks on your computer:

1. Open MySQL Workbench and connect to your local server.
2. Run `SELECT VERSION();` and record the version.
3. Create or verify the database:

```sql
CREATE DATABASE IF NOT EXISTS habit_tracker;
```

4. Run `SHOW DATABASES;` and confirm it appears.
5. Run:

```sql
USE habit_tracker;
SELECT DATABASE();
```

6. Record your local connection's hostname, port, username and database name. Do not write your password in your notes or commit it to Git.
7. Explain in your own words:
   - What is the difference between a MySQL server and a database?
   - What is a table?
   - Why does HabitFlow need persistent storage?
   - What does `USE habit_tracker;` do?
   - What does `jdbc:mysql://localhost:3306/habit_tracker` identify?

## 11. Knowledge check – 10 MCQs

**Q1. What is MySQL?**  
A. Java framework  
B. Relational database management system  
C. Java IDE  
D. HTTP client

**Q2. What is MySQL Workbench used for?**  
A. Compile Java code  
B. Manage and interact with MySQL servers  
C. Create React components  
D. Run Maven tests

**Q3. Which statement creates the database?**  
A. `CREATE TABLE habit_tracker;`  
B. `CREATE DATABASE habit_tracker;`  
C. `INSERT DATABASE habit_tracker;`  
D. `USE DATABASE habit_tracker;`

**Q4. Which command lists databases?**  
A. `SHOW TABLES;`  
B. `SHOW DATABASES;`  
C. `SELECT DATABASES;`  
D. `LIST DATABASES;`

**Q5. What does `USE habit_tracker;` do?**  
A. Deletes the database  
B. Creates a table  
C. Selects the database for the current SQL session  
D. Starts MySQL

**Q6. Which port is commonly used by a default MySQL installation?**  
A. 8080  
B. 5173  
C. 3306  
D. 5432

**Q7. In the JDBC URL, what does `localhost` represent?**  
A. A table  
B. The local server host  
C. A Java package  
D. The SQL username

**Q8. What does `IF NOT EXISTS` do in `CREATE DATABASE IF NOT EXISTS habit_tracker;`?**  
A. Deletes an existing database  
B. Avoids an error if the database already exists  
C. Creates all HabitFlow tables  
D. Changes the database password

**Q9. Which command displays the selected database?**  
A. `SELECT DATABASE();`  
B. `SHOW USER();`  
C. `SELECT TABLE();`  
D. `CURRENT DATABASE;`

**Q10. Where should you avoid publishing your real database password?**  
A. Public GitHub repositories  
B. Protected local configuration  
C. A secure secret manager  
D. A protected environment variable

### Answer key

| Question | Answer | Explanation |
|---|---|---|
| Q1 | B | MySQL is an RDBMS. |
| Q2 | B | Workbench is a graphical client for MySQL. |
| Q3 | B | `CREATE DATABASE` creates a database. |
| Q4 | B | `SHOW DATABASES;` lists databases visible to the account. |
| Q5 | C | `USE` selects the default database for the current session. |
| Q6 | C | 3306 is the commonly used default MySQL port. |
| Q7 | B | `localhost` refers to the local machine. |
| Q8 | B | It avoids an error if the database already exists; it does not clear it. |
| Q9 | A | `SELECT DATABASE();` returns the current default database. |
| Q10 | A | Never expose credentials in public repositories. |

## 12. Completion checklist

- [ ] I understand the difference between a MySQL server, database and table.
- [ ] I opened MySQL Workbench.
- [ ] I connected to my local MySQL server.
- [ ] I ran `SELECT VERSION();`.
- [ ] I created or verified `habit_tracker`.
- [ ] I ran `SHOW DATABASES;`.
- [ ] I ran `USE habit_tracker;`.
- [ ] I verified the selected database using `SELECT DATABASE();`.
- [ ] I understand the parts of a MySQL JDBC URL.
- [ ] I completed the practical assignment and quiz.
- [ ] I saved my notes in `Main_Notes_Branch`.

## 13. Save your notes in GitHub

Keep notes in your existing `Main_Notes_Branch`, not a separate repository.

Suggested path:

```text
learning-notes/
└── Month-1/
    └── Week-2/
        └── Day-2.md
```

Example commands:

```bash
git switch Main_Notes_Branch
git status
git add learning-notes/Month-1/Week-2/Day-2.md
git commit -m "Add HabitFlow Month 1 Week 2 Day 2 MySQL setup notes"
git push origin Main_Notes_Branch
```

Check for uncommitted work before switching branches.

## 14. Key takeaways

- MySQL Server manages databases and handles database connections.
- `habit_tracker` is the database being created for HabitFlow.
- Tables will be created inside this database in upcoming lessons.
- Workbench helps connect to MySQL and execute SQL.
- `CREATE DATABASE` creates a database.
- `SHOW DATABASES` lists databases.
- `USE habit_tracker` selects the database for the current SQL session.
- A JDBC URL identifies the database endpoint and database name.
- Keep database credentials private.
- Spring Boot `application.yml` configuration is the next tracker task, not today's task.

## 15. Next scheduled lesson

**Month 1 – Week 2 – Day 3: Configuration**

Configure `application.yml` and externalize database credentials so the Spring Boot application can connect to the local `habit_tracker` database.

## 16. Interview questions and sample answers

**Q1. What is MySQL?**  
MySQL is a relational database management system (RDBMS) that stores and manages structured data in tables containing rows and columns.

**Q2. What is the difference between a database and a table?**  
A database is a logical container for database objects. A table is an object inside a database that stores data in rows and columns.

**Q3. What is MySQL Workbench?**  
MySQL Workbench is a graphical tool used to connect to MySQL servers, execute SQL queries, create database objects and inspect them.

**Q4. What is the default port of MySQL?**  
The commonly used default MySQL TCP port is `3306`. An installation can use a different port if configured.

**Q5. What does `CREATE DATABASE IF NOT EXISTS` mean?**  
It creates the database only if one with that name does not already exist. It avoids an error if the database is present; it does not clear or recreate it.

**Q6. What is the purpose of the SQL `USE` statement?**  
`USE database_name;` selects a database as the default database for subsequent statements in the current SQL session.

**Q7. What is a JDBC URL?**  
A JDBC URL identifies the database type, server host, port and database name a Java application should connect to. For example: `jdbc:mysql://localhost:3306/habit_tracker`.

**Q8. What does `localhost` mean in a database connection?**  
`localhost` refers to the same machine on which the application or database client is running. It is commonly used during local development.

**Q9. Does creating a database automatically create tables?**  
No. Creating a database creates the database container. Tables must be created separately using SQL or an ORM/schema-management process.

**Q10. Why should database passwords not be committed to GitHub?**  
A repository may be exposed or accessed by people who should not have the credentials. Secrets should be supplied through protected configuration or environment variables instead of committed to source control.

**Q11. How can you verify that `habit_tracker` exists?**  
Run `SHOW DATABASES;` and check the output, or query `INFORMATION_SCHEMA.SCHEMATA` for the database name.

**Q12. What is the difference between MySQL Server and MySQL Workbench?**  
MySQL Server is the database management system that stores and manages data. Workbench is a client application that helps users connect to and work with the server.

---

**End of Month 1 – Week 2 – Day 2.**
