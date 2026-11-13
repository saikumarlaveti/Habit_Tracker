# HabitFlow -- Month 1, Week 1, Day 3

## Development Environment Setup: JDK, IntelliJ IDEA, Maven and Spring Boot

**Duration:** 60 minutes\
**Goal:** Prepare the HabitFlow backend development environment,
generate a Spring Boot project, understand its files, run it, and test a
simple HTTP endpoint.

## 1. Learning objectives

After this lesson, you should be able to: - Explain JVM, JRE and JDK. -
Install and verify a JDK. - Explain the purpose of IntelliJ IDEA and
Maven. - Generate a Spring Boot project using Spring Initializr. -
Understand the generated project structure and main application class. -
Run the application and test an HTTP GET endpoint.

## 2. JVM, JRE and JDK

### JVM -- Java Virtual Machine

The JVM executes Java bytecode. The Java compiler converts source code
(`.java`) into bytecode (`.class`), and the JVM executes that bytecode.

``` text
Hello.java --javac--> Hello.class --JVM--> Program executes
```

### JRE -- Java Runtime Environment

The JRE provides the runtime components needed to run Java applications,
including the JVM and supporting libraries.

### JDK -- Java Development Kit

The JDK provides tools to develop Java applications, including the Java
compiler (`javac`), Java launcher (`java`), JVM and libraries.

  Term   Main purpose
  ------ -----------------------------------------------------
  JVM    Executes bytecode
  JRE    Provides the environment to run Java applications
  JDK    Provides tools to develop and run Java applications

Modern Java distributions commonly package the runtime within the JDK
rather than requiring a separate JRE installation.

## 3. Java version for HabitFlow

As of November 2026, Java 25 is the latest LTS release and Java 21 is
the previous LTS release. For HabitFlow, we will use **JDK 21 LTS** as a
conservative, broadly compatible baseline for learning Spring Boot and
preparing for Java backend roles.

Spring Boot 4.1.2 requires at least Java 17 and supports Java versions
through 27, so Java 21 is compatible.

Download JDK 21: https://adoptium.net/temurin/releases/?version=21

## 4. Install and verify JDK on Windows

1.  Open the download page above.
2.  Select Windows and the x64 installer for a standard Intel/AMD 64-bit
    PC.
3.  Download and run the `.msi` installer.
4.  Complete installation.
5.  Open a new PowerShell window and run:

``` powershell
java -version
javac -version
where.exe java
```

`java -version` checks the Java runtime; `javac -version` checks the
compiler; `where.exe java` shows which executable Windows finds.

Both version commands should report Java 21. If `java` works but `javac`
does not, check that the JDK `bin` folder is available through the
Windows `PATH`.

## 5. IntelliJ IDEA

IntelliJ IDEA is an Integrated Development Environment (IDE). It
provides tools to write, run, debug and manage software projects.

  Feature             Purpose
  ------------------- ----------------------------------
  Code editor         Write Java code
  Code completion     Suggest code while typing
  Debugger            Investigate errors
  Project explorer    Browse project files
  Terminal            Run commands
  Maven integration   Manage dependencies and builds
  Run console         View application output and logs

Download: https://www.jetbrains.com/idea/download/

The free core Java features are enough for today's basic exercise. Some
advanced Spring tooling requires the paid Ultimate features. We can
generate the project using the Spring Initializr website.

## 6. Maven and dependencies

Maven is a build automation and dependency management tool.

A **dependency** is an external library or module that an application
needs. HabitFlow will eventually use Spring Web, Spring Data JPA, MySQL
connectivity, Spring Security, JWT, JUnit and Mockito libraries.

Maven downloads dependencies and their required transitive dependencies,
compiles code, runs tests and packages applications.

### `pom.xml`

`pom.xml` means Project Object Model. It is Maven's main project
configuration file. It contains project information, Java settings,
dependencies and build plugins.

Simplified dependency example:

``` xml
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
</dependencies>
```

### Common Maven commands

  -----------------------------------------------------------------------
  Command                             Purpose
  ----------------------------------- -----------------------------------
  `mvn clean`                         Removes previous build output

  `mvn compile`                       Compiles application source code

  `mvn test`                          Runs tests

  `mvn package`                       Builds the application package

  `mvn install`                       Installs the artifact into the
                                      local Maven repository
  -----------------------------------------------------------------------

### Maven Wrapper

Spring Initializr generates wrapper files such as `mvnw`, `mvnw.cmd` and
`.mvn/wrapper`. The wrapper lets the project use its configured Maven
version without requiring a global Maven installation.

On Windows, use:

``` powershell
.\mvnw.cmd clean
```

For HabitFlow, prefer the Maven Wrapper included with the project.

## 7. Spring Boot and Spring Initializr

Spring Boot simplifies creating and running Spring applications. It
helps us build REST APIs, configure application components, integrate
databases, add security and run an embedded web server.

Spring Initializr is a project generator: https://start.spring.io/

It generates a project using the language, build tool, Java version,
Spring Boot version and dependencies that you select.

## 8. Generate the HabitFlow backend project

Open https://start.spring.io/ and select:

  Field          Value
  -------------- -------------------------------
  Project        Maven
  Language       Java
  Spring Boot    4.1.2
  Group          `com.habitflow`
  Artifact       `habitflow-backend`
  Name           `habitflow-backend`
  Description    HabitFlow backend application
  Package name   `com.habitflow`
  Packaging      Jar
  Java           21

Add the **Spring Web** dependency. It provides support for building HTTP
endpoints and REST APIs. For today's exercise, Spring Web is enough; we
will add database, validation, security and test dependencies in later
lessons.

Click **Generate**, download the ZIP, and extract it to a development
folder, for example:

``` text
C:\Development\HabitFlow\habitflow-backend
```

Open IntelliJ IDEA, select **Open**, choose the extracted project
folder, and allow Maven to import the project and download dependencies.
Only trust projects from sources you trust.

## 9. Understand the generated project structure

``` text
habitflow-backend/
├── .mvn/
│   └── wrapper/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/habitflow/
│   │   │       └── HabitflowBackendApplication.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/
│       └── java/
│           └── com/habitflow/
│               └── HabitflowBackendApplicationTests.java
├── .gitignore
├── mvnw
├── mvnw.cmd
└── pom.xml
```

The exact generated class and test names can vary slightly.

  File or folder             Purpose
  -------------------------- -----------------------------------------
  `src/main/java`            Main Java application code
  `src/main/resources`       Application configuration and resources
  `src/test/java`            Test code
  `application.properties`   Application configuration
  `pom.xml`                  Maven configuration and dependencies
  `.mvn/wrapper`             Maven Wrapper configuration
  `mvnw`                     Wrapper launcher for Unix-like systems
  `mvnw.cmd`                 Wrapper launcher for Windows
  `.gitignore`               Files Git should normally ignore

Later, we will organize packages such as `controller`, `service`,
`repository`, `dto`, `entity`, `security` and `config`. Do not create
all of them yet.

## 10. Understand the main Spring Boot class

The generated class will look similar to:

``` java
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

Line-by-line: - `package com.habitflow;` declares the package. - The
`import` statements make Spring Boot classes available. -
`@SpringBootApplication` combines configuration, auto-configuration and
component scanning. - `public class HabitflowBackendApplication`
declares the main class. - `public static void main(String[] args)` is
the standard Java entry point. - `SpringApplication.run(...)` starts
Spring Boot and creates the application context.

## 11. Run the application

Open `HabitflowBackendApplication.java` and click the green Run icon
beside `main()`.

In the IntelliJ console, look for messages similar to:

``` text
Tomcat started on port 8080
Started HabitflowBackendApplication
```

Exact log wording can vary by version. These messages indicate that the
web application started.

### Create a simple test endpoint

Under `src/main/java/com/habitflow`, create a package named
`controller`. Inside it, create `HelloController.java`:

``` java
package com.habitflow.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/")
    public String hello() {
        return "Welcome to HabitFlow!";
    }
}
```

Explanation: - `@RestController` tells Spring that this class handles
web requests and method return values are written to HTTP response
bodies. - `@GetMapping("/")` maps HTTP GET requests for the root path
`/` to the method. - `public String hello()` declares a method returning
a string. - `return "Welcome to HabitFlow!";` supplies the response
text.

Run the application again and visit http://localhost:8080/.

Expected response:

``` text
Welcome to HabitFlow!
```

If you see this message, you have created a Spring Boot application and
tested your first HTTP GET endpoint.

## 12. Application startup flow

``` text
Click Run in IntelliJ
         |
         v
Java application starts
         |
         v
main() method executes
         |
         v
SpringApplication.run()
         |
         v
Spring Boot initializes
         |
         v
Spring creates application context
         |
         v
Embedded web server starts
         |
         v
Application listens for HTTP requests
         |
         v
Browser requests localhost:8080
         |
         v
HelloController handles GET /
         |
         v
Browser displays Welcome to HabitFlow!
```

## 13. Git branch organization

Keep application code and learning documentation separate.

### `Main_Code_Branch`

``` text
HabitFlow/
└── backend/
    └── habitflow-backend/
        ├── src/
        ├── pom.xml
        ├── mvnw
        └── mvnw.cmd
```

Do not commit generated build output such as the `target/` directory.

### `Main_Notes_Branch`

``` text
learning-notes/
└── Month-1/
    └── Week-1/
        └── Day-3.md
```

## 14. Common errors and troubleshooting

  -----------------------------------------------------------------------------
  Error                         Possible reason         What to check
  ----------------------------- ----------------------- -----------------------
  `'java' is not recognized`    JDK missing or PATH not Confirm JDK
                                configured              installation, reopen
                                                        terminal, check PATH

  `'javac' is not recognized`   Compiler not available  Run `where.exe javac`
                                through PATH            and `javac -version`

  Maven download fails          Network, proxy,         Check internet and
                                repository or           retry
                                configuration issue     

  Port 8080 already in use      Another process uses    Change `server.port`
                                port 8080               

  Browser shows 404             Wrong path, missing     Check package, mapping
                                controller or           and URL
                                application not         
                                restarted               

  Application stops during      Java, dependency,       Read the first
  startup                       configuration or port   meaningful console
                                issue                   error
  -----------------------------------------------------------------------------

To change the port, add this to `application.properties`:

``` properties
server.port=8081
```

Then visit `http://localhost:8081/`.

To retry a Maven build after a dependency-resolution issue:

``` powershell
.\mvnw.cmd -U clean test
```

The `-U` option asks Maven to check for updated snapshots and missing
releases.

## 15. Practical assignments

### Exercise 1: Verify Java

Run these commands in PowerShell and record the output:

``` powershell
java -version
javac -version
where.exe java
```

Answer: 1. Which Java version is installed? 2. What does `java` do? 3.
What does `javac` do?

### Exercise 2: Explore Maven

Find these in IntelliJ: - `pom.xml` - `mvnw.cmd` - `.mvn/wrapper` -
`src/main/java` - `src/main/resources` - `src/test/java`

Explain the purpose of each in your own words. Run:

``` powershell
.\mvnw.cmd clean
.\mvnw.cmd test
```

Observe the console output.

### Exercise 3: Run your first endpoint

1.  Create `HelloController`.
2.  Add the GET endpoint.
3.  Run the Spring Boot application.
4.  Open `http://localhost:8080/`.
5.  Confirm the browser displays `Welcome to HabitFlow!`.

### Exercise 4: Git

Commit the backend project to `Main_Code_Branch`. Commit this lesson to
`Main_Notes_Branch`.

## 16. Knowledge-check quiz

Try to answer without referring to the lesson.

**Q1. Which Java component executes bytecode?**

A. JDK\
B. JVM\
C. Maven

**Q2. Which Java kit do we need to develop Java applications?**

A. JDK\
B. JSON\
C. MySQL

**Q3. What is the purpose of Maven?**

A. Design web pages\
B. Manage dependencies and automate builds\
C. Store database records

**Q4. Which file contains Maven project configuration and
dependencies?**

A. `application.properties`\
B. `pom.xml`\
C. `HelloController.java`

**Q5. Which annotation identifies a Spring Boot main application
class?**

A. `@RestController`\
B. `@GetMapping`\
C. `@SpringBootApplication`

**Q6. What does `@GetMapping("/")` do?**

A. Maps HTTP GET requests for `/` to a method\
B. Connects to MySQL\
C. Starts Maven

**Q7. Which port does our example Spring Boot application use by
default?**

A. 3306\
B. 8080\
C. 5173

### Answer sheet

``` text
Q1:
Q2:
Q3:
Q4:
Q5:
Q6:
Q7:
```

### Answer key -- check after attempting

  --------------------------------------------------------------------------
  Question                Correct answer          Explanation
  ----------------------- ----------------------- --------------------------
  Q1                      B                       JVM executes Java
                                                  bytecode.

  Q2                      A                       JDK provides Java
                                                  development tools.

  Q3                      B                       Maven manages dependencies
                                                  and build tasks.

  Q4                      B                       `pom.xml` is Maven's main
                                                  project configuration
                                                  file.

  Q5                      C                       `@SpringBootApplication`
                                                  marks the main Spring Boot
                                                  application configuration.

  Q6                      A                       It maps GET requests to
                                                  the root path.

  Q7                      B                       The default Spring Boot
                                                  web server port is 8080
                                                  unless changed.
  --------------------------------------------------------------------------

## 17. Day 3 completion checklist

-   [ ] I understand JVM, JRE and JDK.
-   [ ] I installed JDK 21.
-   [ ] `java -version` works.
-   [ ] `javac -version` works.
-   [ ] I installed IntelliJ IDEA.
-   [ ] I understand the purpose of Maven.
-   [ ] I understand `pom.xml` and Maven Wrapper.
-   [ ] I generated the HabitFlow backend project.
-   [ ] I understand the main project folders.
-   [ ] I understand the main Spring Boot application class.
-   [ ] I successfully started the Spring Boot application.
-   [ ] I created and tested `HelloController`.
-   [ ] I committed code to `Main_Code_Branch`.
-   [ ] I committed notes to `Main_Notes_Branch`.
-   [ ] I attempted the knowledge-check quiz.

**Day 3 status:** Not yet completed. Mark it complete after reviewing
your setup, practical work and quiz answers.

## 18. Key takeaways

1.  JDK provides tools to develop Java applications.
2.  JVM executes Java bytecode.
3.  We selected JDK 21 for HabitFlow.
4.  IntelliJ IDEA is our Java development environment.
5.  Maven manages dependencies and builds the project.
6.  `pom.xml` contains Maven project configuration.
7.  Spring Initializr generates a starting Spring Boot project.
8.  `@SpringBootApplication` marks the main application class.
9.  `@RestController` defines a REST controller.
10. `@GetMapping` maps an HTTP GET request to a method.
11. The default Spring Boot web server port is 8080.
12. We keep application code and learning notes in separate Git
    branches.

## 19. What comes next?

**Month 1 -- Week 2 -- Day 1: Java and Spring Boot Project Structure --
Deep Dive**

Planned topics: - Java packages and classes in a Spring Boot project. -
Main application class and component scanning. - `src/main/java`,
`src/main/resources`, and `src/test/java`. - Maven dependencies and
Spring Boot starters. - How Spring Boot discovers and manages
components.

## 20. Personal learning notes

**JDK version installed:**

*Write your answer here.*

**What I understood well:**

*Write your notes here.*

**What I need to revise:**

*Write your notes here.*

**Errors I faced and how I fixed them:**

*Write your notes here.*

**My practical exercise result:**

*Write your result here.*

**My quiz score:**

*Write your score here.*
