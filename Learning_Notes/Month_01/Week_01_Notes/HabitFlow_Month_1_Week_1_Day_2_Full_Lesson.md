# HabitFlow -- Month 1, Week 1, Day 2

## System Architecture and Client--Server Communication

**Project:** HabitFlow\
**Learning phase:** Month 1 -- Backend Foundation\
**Week:** 1 -- Software and Application Foundations\
**Estimated duration:** 60 minutes\
**Level:** Beginner

------------------------------------------------------------------------

## 1. Learning objectives

By the end of this lesson, you should be able to:

1.  Explain software architecture and client--server architecture.
2.  Distinguish frontend, backend, and database responsibilities.
3.  Explain how React communicates with Spring Boot.
4.  Explain how Spring Boot accesses MySQL.
5.  Describe the Controller, Service, and Repository layers.
6.  Trace a login request and a habit-creation request from the browser
    to the database and back.

No application code is required today. The goal is to understand the
system before building it.

## 2. What is software architecture?

Software architecture is the high-level structure or blueprint of an
application. It describes the major components, each component's
responsibility, and how the components communicate.

Think about building a house. Before construction, an architect prepares
a plan showing rooms, doors, electrical wiring, plumbing, and how
everything fits together. Software architecture serves a similar purpose
for an application.

When designing software architecture, we ask:

-   What major components does the application need?
-   What is each component responsible for?
-   Where will application data be stored?
-   How will components communicate?
-   How will security, errors, and data consistency be handled?
-   How can the application be tested, maintained, and extended?

### 2.1 HabitFlow's high-level architecture

HabitFlow has three main parts:

1.  **Frontend:** The user interface.
2.  **Backend:** The server-side application that processes requests and
    applies business rules.
3.  **Database:** The system that stores application information.

``` text
+--------------------------------+
|         User's Browser         |
|                                |
|       React + TypeScript       |
|            FRONTEND            |
+---------------+----------------+
                |
                | HTTP request / response
                | REST API using JSON
                |
+---------------v----------------+
|           Spring Boot           |
|             BACKEND             |
|                                 |
|  Controller                     |
|      |                          |
|  Service                        |
|      |                          |
|  Repository                     |
+---------------+----------------+
                |
                | JPA / Hibernate / SQL
                |
+---------------v----------------+
|              MySQL              |
|             DATABASE            |
|                                 |
| users, habits, reminders,       |
| completions, notifications      |
+---------------------------------+
```

This is a simplified three-tier architecture. The backend is further
divided into layers, which we will study in this lesson.

### 2.2 Restaurant analogy

  Restaurant     HabitFlow   Responsibility
  -------------- ----------- ---------------------------------------------
  Customer       User        Requests a service
  Waiter         Frontend    Carries the request and presents the result
  Kitchen        Backend     Processes the request
  Storage room   Database    Stores information used by the kitchen

A customer gives an order to the waiter. The waiter takes it to the
kitchen. The kitchen prepares the order using ingredients from storage.
The waiter brings the result to the customer.

In HabitFlow, the frontend sends a request to the backend. The backend
may retrieve or update information in MySQL. It then returns a response
that the frontend displays.

This analogy explains responsibilities; a real application uses network
protocols and software components.

## 3. What is client--server architecture?

Client--server architecture is a model in which a client requests a
service or resource and a server processes the request and sends a
response.

### 3.1 Client

A client is an application or device that initiates a request.

Examples: - A web browser. - A mobile application. - A desktop
application. - A command-line HTTP client.

For HabitFlow, the React application running in the user's browser acts
as the client.

### 3.2 Server

A server is a program or computer that receives requests, processes
them, and returns responses.

For HabitFlow, the Spring Boot application is the backend server. It
exposes API endpoints that the frontend can call.

### 3.3 Request and response cycle

1.  The user performs an action in the client.
2.  The client creates and sends a request.
3.  The server receives the request.
4.  The server validates and processes it.
5.  The server may read or write information in a database.
6.  The server sends a response.
7.  The client handles the response and updates the user interface.

``` text
CLIENT                                      SERVER
  |                                           |
  |---------- 1. Send request -------------->|
  |                                           |
  |                             2. Process request
  |                             3. Access data if needed
  |                                           |
  |<--------- 4. Return response -------------|
  |                                           |
  | 5. Display result                         |
```

### 3.4 Example: retrieving habits

When a user opens the HabitFlow dashboard, the browser might send:

``` http
GET /api/habits
```

The backend processes the request, retrieves the authenticated user's
habits, and returns a response. The browser displays those habits.

The client and server do not have to run on the same computer. During
local development they may both run on your computer. In production they
may run on separate machines or cloud services.

## 4. The three main parts of HabitFlow

### 4.1 Frontend -- React and TypeScript

The frontend is the part of the application that users see and interact
with.

HabitFlow frontend responsibilities include:

-   Registration and login screens.
-   Dashboard and navigation.
-   Creating, viewing, editing, and archiving habits.
-   Marking a habit as completed.
-   Displaying daily, weekly, monthly, and yearly progress.
-   Showing habit history and streaks.
-   Displaying the notification bell and in-app notifications.
-   Showing validation messages and errors.

**React** is a JavaScript library for building user interfaces.
**TypeScript** adds static types to JavaScript.

The frontend collects user input and presents information. It must not
be trusted to enforce important security rules by itself. The backend
must validate requests and check permissions.

### 4.2 Backend -- Java and Spring Boot

The backend receives requests from the frontend and performs application
operations.

HabitFlow backend responsibilities include:

-   Registering and authenticating users.
-   Checking whether a user is allowed to access a resource.
-   Creating, updating, and retrieving habits.
-   Validating incoming data.
-   Calculating habit streaks and progress.
-   Managing reminder schedules.
-   Creating and managing notifications.
-   Communicating with the database.
-   Returning suitable success or error responses.

Technologies selected:

-   **Java:** The main backend programming language.
-   **Spring Boot:** A framework for building and running Java
    applications, including REST APIs.
-   **Spring Security:** Used for authentication and authorization.
-   **Spring Data JPA:** Provides repository support for database
    access.
-   **Hibernate:** A common implementation of the JPA specification.

The backend is the authority for business rules and access control. For
example, it must check that a user can only view or update their own
habits.

### 4.3 Database -- MySQL

A database stores information so it can be retrieved and updated later.
HabitFlow will use MySQL, a relational database management system.

Examples of planned tables:

  Table                 Example information
  --------------------- -------------------------------------------------
  `users`               Account details, email, password hash, timezone
  `habits`              Habit name, description, category, owner
  `habit_categories`    Category names
  `habit_completions`   Dates on which habits were completed
  `reminders`           Reminder time, enabled state, schedule settings
  `notifications`       In-app notification content and read state

A relational database organizes information into tables. Tables contain
rows and columns. Relationships connect records in different tables.

For example, a habit belongs to a user. A habit record can contain a
`user_id` that refers to the user who owns it.

**Security:** Passwords must never be stored as plain text. The backend
should store a secure password hash using an appropriate password
encoder.

## 5. How React, Spring Boot, and MySQL communicate

Each part has a different responsibility and uses different
communication mechanisms.

### 5.1 React to Spring Boot: HTTP and REST APIs

React communicates with Spring Boot through HTTP requests to API
endpoints. An endpoint is an address exposed by the backend for an
operation.

Example:

``` http
GET http://localhost:8080/api/habits
```

This asks the backend to retrieve habits. `localhost` refers to the
current computer, and `8080` is the example port on which Spring Boot is
running.

Common HTTP methods:

  -----------------------------------------------------------------------
  Method                  Typical purpose         HabitFlow example
  ----------------------- ----------------------- -----------------------
  `GET`                   Retrieve information    Get the user's habits

  `POST`                  Create a resource or    Create a habit
                          submit an operation     

  `PUT`                   Replace or fully update Update a habit
                          a resource              

  `PATCH`                 Partially update a      Mark a notification as
                          resource                read

  `DELETE`                Delete a resource       Delete a habit, if
                                                  product rules allow it
  -----------------------------------------------------------------------

An HTTP request can contain a method, URL/path, headers, and sometimes a
body. Headers can describe the content type or carry authorization
information.

### 5.2 JSON: the data exchange format

The frontend and backend commonly exchange structured data using JSON.

Example response:

``` json
[
  {
    "id": 1,
    "name": "Reading",
    "completed": true
  },
  {
    "id": 2,
    "name": "Exercise",
    "completed": false
  }
]
```

This response represents two habits.

JSON uses: - Objects enclosed in `{ }`. - Arrays enclosed in `[ ]`. -
Key-value pairs such as `"name": "Reading"`. - Values such as strings,
numbers, booleans, arrays, objects, and `null`.

React can read the response and display the habit names and completion
states.

### 5.3 Spring Boot to MySQL: JPA, Hibernate, and SQL

React should not connect directly to the application's MySQL database.
Instead, the backend accesses the database.

A simplified flow:

``` text
React
  |
  | HTTP request
  v
Spring Boot Controller
  |
  v
Spring Boot Service
  |
  v
Spring Data JPA Repository
  |
  v
Hibernate / JDBC
  |
  | SQL sent to database
  v
MySQL
```

The database returns the result through the database-access layers to
the backend. Spring Boot then creates an HTTP response, often containing
JSON.

Important distinctions:

-   **HTTP** is used for communication between the frontend and backend.
-   **SQL** is used to query and modify relational database data.
-   **JPA** is a Java persistence specification.
-   **Hibernate** is an implementation of JPA.
-   **Spring Data JPA** provides repository support that reduces
    repetitive database-access code.

## 6. Understanding the Spring Boot backend layers

A common backend structure is:

``` text
Controller
    |
    v
Service
    |
    v
Repository
    |
    v
Database
```

### 6.1 Controller layer

The Controller receives HTTP requests and returns HTTP responses.

For example, a `HabitController` might expose endpoints to get, create,
update, or delete/archive habits.

The controller should coordinate the request and response. It should not
contain all the application's business logic.

### 6.2 Service layer

The Service contains business logic.

For example, a `HabitService` might:

-   Validate habit information.
-   Apply rules for creating a habit.
-   Check that the requested habit belongs to the current user.
-   Decide how a habit completion should be recorded.
-   Coordinate repository operations.

### 6.3 Repository layer

The Repository handles data access.

For example, a `HabitRepository` might retrieve habits, save a new
habit, or find a habit by its identifier.

Spring Data JPA can generate common repository implementations from
repository interfaces and method definitions.

### 6.4 Why separate these layers?

Separating responsibilities makes an application easier to understand,
test, debug, maintain, and extend. It also prevents controllers from
becoming large and difficult to maintain.

## 7. Complete example: What happens when a user clicks Login?

Assume a user enters an email address and password on the HabitFlow
login page.

### Step 1: User enters credentials

The user enters their email and password and clicks Login. React
collects the form values. The frontend may check whether required fields
are empty.

Frontend validation improves usability, but it does not replace backend
validation.

### Step 2: React sends an HTTP request

Illustrative request:

``` http
POST /api/auth/login
Content-Type: application/json
```

Illustrative JSON body:

``` json
{
  "email": "user@example.com",
  "password": "user-entered-password"
}
```

This is placeholder data. Real credentials must be transmitted over
HTTPS in production and must never be written to application logs.

### Step 3: Spring Boot receives the request

The backend receives the request through its authentication endpoint and
passes the authentication attempt to the configured Spring Security
authentication mechanism.

### Step 4: Backend checks the account and password

The authentication mechanism obtains account information through the
backend's user-loading and data-access components.

The submitted password is checked against the stored password hash using
the configured password encoder. The application must not store or
compare plain-text passwords.

### Step 5: Backend decides whether authentication succeeds

If the credentials are valid, authentication succeeds. If they are
invalid, the backend rejects the attempt and returns an appropriate
error response. The response should not reveal whether a particular
email address exists.

### Step 6: Backend creates an authentication result

For our planned design, successful login can result in the backend
issuing a JWT access token. The token design, expiration, refresh-token
strategy, storage, and logout behavior will be studied in Month 2.

Illustrative success response:

``` json
{
  "message": "Login successful",
  "token": "example-token-placeholder"
}
```

This is a simplified illustration, not the final API contract.

### Step 7: React handles the response

If login succeeds, React updates the application's authentication state
and navigates the user to the dashboard. If login fails, React displays
a clear and safe error message and allows the user to try again.

### Login flow diagram

``` text
USER
 |
 | Enters email and password; clicks Login
 v
REACT FRONTEND
 |
 | POST /api/auth/login
 | JSON request
 v
SPRING BOOT AUTHENTICATION ENDPOINT
 |
 v
SPRING SECURITY AUTHENTICATION
 |
 | Load account information
 v
USER DATA ACCESS / REPOSITORY
 |
 v
MYSQL
 |
 | Account information and password hash
 v
SPRING SECURITY CHECKS PASSWORD
 |
 +------------------------------+
 |                              |
 | Authentication succeeds     | Authentication fails
 v                              v
Issue authentication result    Reject authentication
 |                              |
 v                              v
Return success response        Return error response
 |                              |
 v                              v
React opens dashboard          React displays error
```

### Security reminders

-   Use HTTPS in production.
-   Store password hashes, never plain-text passwords.
-   Validate requests on the backend.
-   Protect endpoints with authentication and authorization.
-   Check resource ownership on the backend.
-   Avoid logging passwords, access tokens, or other secrets.
-   Use safe token storage and token lifecycle practices; these will be
    studied in Month 2.

## 8. Second example: What happens when a user creates a habit?

Assume the user creates a habit called **Daily Reading**.

### Step 1: User fills out the form

The user enters:

-   Habit name: Daily Reading
-   Description: Read for 20 minutes
-   Category: Learning

The user clicks Save.

### Step 2: React sends a request

Illustrative request:

``` http
POST /api/habits
Content-Type: application/json
```

Illustrative request body:

``` json
{
  "name": "Daily Reading",
  "description": "Read for 20 minutes",
  "categoryId": 1
}
```

### Step 3: Controller receives the request

The `HabitController` receives the HTTP request and passes the submitted
data to the service layer.

### Step 4: Service applies business rules

The `HabitService` can validate the habit information, obtain the
authenticated user's identity from the security context, check that the
category is valid, and apply the application's habit-creation rules.

The backend must determine the owner from the authenticated user
context. It must not blindly trust a user ID supplied by the browser.

### Step 5: Repository saves the habit

The service asks the repository to save the new habit. MySQL stores the
record in the `habits` table, associated with the correct user.

### Step 6: Backend returns a response

The backend returns a success response containing the created habit or
an appropriate result.

### Step 7: React updates the dashboard

React handles the response and updates the user interface so the new
habit appears on the dashboard.

``` text
User clicks Save
       |
       v
React sends POST request
       |
       v
HabitController
       |
       v
HabitService
       |
       v
HabitRepository
       |
       v
MySQL saves habit
       |
       v
Backend returns response
       |
       v
React displays Daily Reading
```

## 9. Local development architecture

During local development, the frontend, backend, and database can run on
the same computer as separate processes.

Planned example addresses:

  Component             Example local address     Purpose
  --------------------- ------------------------- ----------------
  React frontend        `http://localhost:5173`   User interface
  Spring Boot backend   `http://localhost:8080`   REST APIs
  MySQL database        `localhost:3306`          Data storage

The actual ports can be changed in configuration.

### What does localhost mean?

`localhost` refers to the computer on which the application is running.

-   `localhost:5173` means the frontend development server on port 5173.
-   `localhost:8080` means the backend server on port 8080.
-   `localhost:3306` means the MySQL service on port 3306.

A port identifies a particular network service on a computer.

### What is CORS?

Because the frontend and backend use different ports, the browser treats
them as different origins.

For example:

-   Frontend origin: `http://localhost:5173`
-   Backend origin: `http://localhost:8080`

The browser's same-origin policy applies. The backend may need a
correctly configured Cross-Origin Resource Sharing (CORS) policy to
allow requests from the frontend origin.

CORS is a browser security mechanism. It is not a replacement for
authentication or authorization. We will configure it when we integrate
React with Spring Boot.

## 10. Important terminology

  Term                    Meaning
  ----------------------- ---------------------------------------------------------
  Software architecture   High-level structure of an application
  Client                  Application that sends a request
  Server                  Application that receives and processes requests
  Frontend                User interface
  Backend                 Server-side application logic
  Database                Stores application data
  HTTP                    Protocol used for web communication
  API                     Interface through which software components communicate
  REST API                API style commonly used with HTTP resources and methods
  JSON                    Data format commonly used in API requests and responses
  Endpoint                API address/path that supports an operation
  Controller              Spring component that handles web requests
  Service                 Component that implements business logic
  Repository              Component that provides data-access operations
  SQL                     Language used to work with relational databases
  JPA                     Java persistence specification
  Hibernate               Common JPA implementation
  CORS                    Browser mechanism controlling cross-origin requests
  Localhost               Name that refers to the current computer

## 11. Common misunderstandings

### Misunderstanding 1: React directly connects to MySQL

**Incorrect:** The browser should connect directly to the application's
MySQL database.

**Correct:** React calls the backend API. The backend accesses MySQL.
Database credentials and database access must remain on the server side.

### Misunderstanding 2: Frontend validation is enough

**Incorrect:** If React validates a form, the backend does not need to
validate it.

**Correct:** The backend must validate incoming data because clients can
be modified or bypassed.

### Misunderstanding 3: The Controller should contain all business logic

**Incorrect:** Put every operation inside the controller.

**Correct:** Keep request handling in the controller, business rules in
the service/domain layer, and database access in the repository layer.

### Misunderstanding 4: JSON and SQL are the same

**Incorrect:** JSON is the language used to query MySQL.

**Correct:** JSON is commonly used to exchange data between frontend and
backend. SQL is used to query and modify relational database data.

### Misunderstanding 5: Authentication and authorization are the same

**Authentication** verifies who the user is.

**Authorization** determines what the authenticated user is allowed to
do.

For example, logging in verifies the user's identity. Checking whether
that user is allowed to edit a particular habit is an authorization
decision.

## 12. Practical assignments

Complete these exercises in your notebook. No application code is
required today.

### Exercise 1: Draw the HabitFlow architecture

Draw a diagram containing:

-   React + TypeScript.
-   Spring Boot.
-   Controller.
-   Service.
-   Repository.
-   MySQL.

Use arrows to show the direction of requests and data. Your diagram
should show that React communicates with Spring Boot over HTTP and that
the backend accesses MySQL through its data-access layer.

### Exercise 2: Explain the habit-creation flow

Scenario: A user creates a habit called **Daily Reading** and clicks
Save.

Write the steps in your own words. Include:

1.  What happens in React?
2.  Which HTTP method could be used?
3.  Which Spring Boot component receives the request?
4.  Which component applies business rules?
5.  Which component accesses MySQL?
6.  What happens after the backend returns a response?

### Exercise 3: Classify the responsibilities

Write whether each responsibility belongs primarily to the frontend,
backend, or database. Some responsibilities involve more than one layer;
choose the primary responsibility and explain your reasoning.

  Responsibility                                         Your answer
  ------------------------------------------------------ -------------
  Display the login page                                 
  Validate that a user owns a habit before updating it   
  Store a habit record                                   
  Display a monthly progress chart                       
  Calculate whether a request is authorized              
  Retrieve stored habit records                          

## 13. Knowledge-check quiz

Try to answer without looking at the lesson first.

**Q1. What is software architecture?**

A. A programming language used to build software\
B. The overall structure of an application and how its components
interact\
C. A database table

**Q2. Which component is responsible for displaying the HabitFlow login
page?**

A. MySQL\
B. Spring Boot\
C. React

**Q3. Which component handles HabitFlow backend business logic?**

A. Spring Boot\
B. React\
C. MySQL

**Q4. Which component stores users and habits persistently?**

A. React\
B. MySQL\
C. HTTP

**Q5. What is JSON commonly used for?**

A. Styling web pages\
B. Exchanging structured data between frontend and backend\
C. Compiling Java code

### Answer sheet

``` text
Q1:
Q2:
Q3:
Q4:
Q5:
```

### Answer key -- check only after attempting the quiz

  -----------------------------------------------------------------------
  Question                Correct answer          Explanation
  ----------------------- ----------------------- -----------------------
  Q1                      B                       Architecture describes
                                                  components,
                                                  responsibilities, and
                                                  interactions.

  Q2                      C                       React builds the
                                                  frontend user
                                                  interface.

  Q3                      A                       Spring Boot runs the
                                                  backend application
                                                  logic.

  Q4                      B                       MySQL stores
                                                  application records.

  Q5                      B                       JSON is commonly used
                                                  to exchange structured
                                                  data through APIs.
  -----------------------------------------------------------------------

## 14. Day 2 completion checklist

-   [ ] I can explain software architecture.
-   [ ] I can explain client--server architecture.
-   [ ] I understand frontend, backend, and database responsibilities.
-   [ ] I understand how React communicates with Spring Boot.
-   [ ] I understand how Spring Boot accesses MySQL.
-   [ ] I understand Controller, Service, and Repository
    responsibilities.
-   [ ] I can explain the login request flow.
-   [ ] I can explain the habit-creation request flow.
-   [ ] I completed the architecture drawing.
-   [ ] I completed the practical assignments.
-   [ ] I attempted the five-question quiz.

**Day 2 status:** Not yet completed. Update this after reviewing your
practical work and quiz answers.

## 15. Key takeaways

1.  Software architecture is the blueprint of an application.
2.  HabitFlow uses a frontend, backend, and database.
3.  React + TypeScript is responsible for the user interface.
4.  Java + Spring Boot is responsible for backend processing and
    business rules.
5.  MySQL stores application data.
6.  React communicates with Spring Boot through HTTP APIs.
7.  JSON is commonly used to exchange data between frontend and backend.
8.  Spring Boot accesses MySQL through the data-access layer, using
    technologies such as Spring Data JPA and Hibernate.
9.  Controller, Service, and Repository have separate responsibilities.
10. Security and validation must be enforced by the backend, not only by
    the frontend.

## 16. What comes next?

**Month 1 -- Week 1 -- Day 3: Development Environment Setup**

Planned topics:

-   Understanding the Java Development Kit (JDK).
-   Installing and configuring IntelliJ IDEA.
-   Understanding Maven.
-   Creating a Spring Boot project.
-   Understanding the project folder structure.
-   Running the first Spring Boot application.
-   Understanding application startup logs.

We will use: - `Main_Code_Branch` for application code. -
`Main_Notes_Branch` for learning documentation.

## 17. Personal learning notes

**What I understood well:**

*Write your notes here.*

**What I need to revise:**

*Write your notes here.*

**Questions to ask:**

*Write your questions here.*

**My practical exercise result:**

*Write your architecture and request-flow explanation here.*

**My quiz score:**

*Write your score here.*
