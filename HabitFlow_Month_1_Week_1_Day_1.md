# HabitFlow -- Month 1, Week 1, Day 1

**Topic:** Understanding HabitFlow Requirements\
**Status:** Completed\
**Study time:** 1 hour

## 1. Learning objectives

By the end of this lesson, I should be able to:

-   Explain the Software Development Life Cycle (SDLC).
-   Describe the purpose of HabitFlow.
-   Distinguish functional and non-functional requirements.
-   Explain why requirements must be gathered before development.
-   Understand MVP and future enhancements.

## 2. Software Development Life Cycle (SDLC)

The **SDLC** is a structured process used to plan, build, test, deploy,
and maintain software.

### Seven common stages

1.  **Requirement gathering:** Understand what the customer needs.
2.  **Requirement analysis:** Analyze requirements, constraints, and
    feasibility.
3.  **System design:** Design architecture, database, and application
    components.
4.  **Development:** Implement the application by writing code.
5.  **Testing:** Verify that the application works as expected.
6.  **Deployment:** Release the application for users.
7.  **Maintenance:** Fix defects and improve the application over time.

**Key lesson:** Understand the problem and requirements before starting
implementation.

## 3. HabitFlow project overview

### Problem statement

People often set personal goals but struggle to maintain consistency.
HabitFlow helps users create, manage, and track habits, schedule
reminders, and understand their progress.

### Main purpose

HabitFlow helps users build consistent habits through:

-   Habit creation and management.
-   Daily completion tracking.
-   Scheduled reminders.
-   Progress history and streaks.
-   Progress analytics.

### Potential users

-   Students learning new skills.
-   Employees improving professional skills.
-   People maintaining fitness routines.
-   Readers building a daily reading habit.
-   Individuals working on personal development.

## 4. Functional requirements

Functional requirements describe **what the application should do**.
They define the features and operations the system must provide.

### Initial HabitFlow functional requirements

  -----------------------------------------------------------------------
  ID                                  Requirement
  ----------------------------------- -----------------------------------
  FR01                                Users can register, log in, and log
                                      out.

  FR02                                Users can create, view, update,
                                      archive, and delete habits.

  FR03                                Users can configure multiple
                                      reminder times for a habit.

  FR04                                Users can view daily, weekly,
                                      monthly, and yearly progress.

  FR05                                Users can export habit data and
                                      import a previously exported
                                      backup.

  FR06                                Users can mark a habit as completed
                                      for a scheduled date.

  FR07                                Users can view habit history and
                                      streaks.

  FR08                                Users can receive push and in-app
                                      notifications.
  -----------------------------------------------------------------------

### Examples

-   "The user should be able to delete a habit." --- **Functional
    requirement.**
-   "The user should be able to recover a recently deleted habit." ---
    **Functional requirement.**

Both describe actions the application must provide.

## 5. Non-functional requirements

Non-functional requirements describe the **quality attributes and
constraints** of the application: how well it should operate.

### Initial HabitFlow non-functional requirements

  -----------------------------------------------------------------------
  ID                      Quality attribute       Example expectation
  ----------------------- ----------------------- -----------------------
  NFR01                   Performance             APIs should meet agreed
                                                  response-time targets
                                                  under expected load.

  NFR02                   Usability and           The application should
                          responsiveness          be easy to use on
                                                  desktop, tablet, and
                                                  mobile screens.

  NFR03                   Reliability and         Errors should be
                          recovery                handled gracefully
                                                  without losing
                                                  committed user data.

  NFR04                   Availability            The backend should
                                                  expose health
                                                  information and recover
                                                  pending work after
                                                  restarts.

  NFR05                   Security                Passwords must be
                                                  securely hashed and
                                                  users must only access
                                                  their own data.

  NFR06                   Maintainability         Code should be modular,
                                                  readable, and testable.

  NFR07                   Data integrity          Database constraints
                                                  and transactions should
                                                  protect data
                                                  consistency.
  -----------------------------------------------------------------------

### Examples

-   "The system should support 10,000 concurrent users within agreed
    performance limits." --- **Non-functional requirement**; it
    describes scalability and performance.
-   "The application must protect users' personal data." ---
    **Non-functional requirement**; it describes security.

**Important:** Non-functional requirements should be specific and
testable wherever possible. Avoid vague statements such as "the
application should work perfectly."

## 6. Functional vs non-functional requirements

  -----------------------------------------------------------------------
  Functional requirements             Non-functional requirements
  ----------------------------------- -----------------------------------
  Describe what the system does.      Describe quality attributes and
                                      constraints.

  Define features and operations.     Define expectations such as
                                      performance, security, and
                                      reliability.

  Example: Delete a habit.            Example: Habit APIs meet an agreed
                                      response-time target.

  Example: Recover a deleted habit.   Example: User data is protected
                                      from unauthorized access.
  -----------------------------------------------------------------------

**Remember:** - Functional = **What** the system does. - Non-functional
= **How well** the system operates and the constraints it must satisfy.

## 7. What is an MVP?

**MVP** stands for **Minimum Viable Product**. It is an initial usable
version containing the essential features needed to solve the main user
problem and gather feedback.

### HabitFlow MVP -- proposed scope

-   User registration and login.
-   Create and manage habits.
-   Mark habits as completed.
-   View a basic progress dashboard.

### Later enhancements

-   Multiple scheduled reminders.
-   Push and in-app notifications.
-   Advanced monthly and yearly analytics.
-   Habit backup, import, and export.
-   Gamification and AI-powered insights.

### Why separate MVP from future enhancements?

1.  Focus on the most important user needs first.
2.  Reduce initial development complexity.
3.  Deliver a usable version earlier.
4.  Gather feedback from users.
5.  Add advanced features in stages.

The project is not divided only because it is large. The purpose is to
prioritize value, manage risk, and learn from feedback.

## 8. Why gather requirements before coding?

Gathering requirements helps the team:

-   Understand customer expectations.
-   Identify necessary features and constraints.
-   Define project scope.
-   Design a suitable database and architecture.
-   Avoid unnecessary rework.

### HabitFlow example

If we initially assume that each habit can have only one reminder, but
later discover that users need multiple reminders, we may need to change
the database design, APIs, backend logic, and frontend. Clarifying this
requirement early reduces rework.

## 9. Knowledge check -- reviewed answers

  -----------------------------------------------------------------------
  Question                            Correct answer
  ----------------------------------- -----------------------------------
  Main purpose of HabitFlow?          Help users create, manage, and
                                      track habits so they can build
                                      consistency.

  Functional vs non-functional?       Functional describes what the
                                      system does; non-functional
                                      describes quality attributes and
                                      constraints.

  Why gather requirements first?      To understand expectations, define
                                      scope, and avoid rework.

  Who are the users?                  Students, employees, and other
                                      individuals who want to build
                                      consistent habits.

  Why separate MVP and enhancements?  To prioritize essential
                                      functionality, deliver value
                                      earlier, and add advanced features
                                      incrementally.
  -----------------------------------------------------------------------

### Final classification exercise

1.  A user can delete a habit. --- **Functional**
2.  The application supports 10,000 concurrent users within defined
    performance limits. --- **Non-functional**
3.  A user can recover a recently deleted habit. --- **Functional**

## 10. Key terms for revision

  -----------------------------------------------------------------------
  Term                                Meaning
  ----------------------------------- -----------------------------------
  SDLC                                Structured process for developing
                                      and maintaining software.

  Requirement gathering               Understanding customer needs before
                                      development.

  Functional requirement              A feature or operation the system
                                      must provide.

  Non-functional requirement          A quality attribute or constraint
                                      of the system.

  MVP                                 First usable version containing
                                      essential features.

  Scalability                         Ability to handle growth in users,
                                      workload, or data.

  Reliability                         Ability to operate correctly and
                                      handle failures appropriately.

  Maintainability                     How easily software can be
                                      understood, changed, and tested.
  -----------------------------------------------------------------------

## 11. Day 1 completion checklist

-   [x] Understand SDLC and its seven stages.
-   [x] Understand the HabitFlow problem statement.
-   [x] Identify potential users.
-   [x] Understand functional requirements.
-   [x] Understand non-functional requirements.
-   [x] Understand MVP and future enhancements.
-   [x] Complete and review the knowledge checks.

**Day 1 status: COMPLETED**

## 12. Preview of Day 2

**Topic: System Architecture and Client--Server Communication**

Planned topics:

1.  What is software architecture?
2.  What is client--server architecture?
3.  Frontend vs backend.
4.  How React communicates with Spring Boot.
5.  How Spring Boot communicates with MySQL.
6.  What happens when a user clicks the Login button?

------------------------------------------------------------------------

## Personal revision notes

Use this section to add your own examples, questions, and concepts that
need another review.

-   **What I understood well:**
-   **What I need to revise:**
-   **Questions to ask:**
-   **Real-world examples:**
