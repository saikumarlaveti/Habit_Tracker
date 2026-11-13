# HabitFlow – Month 1, Week 1, Day 4
## Git, GitHub, Branches, Commits and Project Version Control

**Duration:** 60 minutes  
**Goal:** Understand how to manage HabitFlow's source code and learning documentation using Git and GitHub.

## 1. Learning objectives

By the end of this lesson, you should be able to:

1. Explain Git and GitHub.
2. Understand local and remote repositories.
3. Explain branches and commits.
4. Understand the working directory, staging area, and local repository.
5. Use basic Git commands.
6. Commit application code and learning notes to the correct HabitFlow branches.
7. Explain the purpose of `.gitignore`.

## 2. What is Git?

Git is a **distributed version control system**. It tracks changes made to files in a project over time.

Imagine you are developing HabitFlow. Today, your login functionality works. Tomorrow, you modify the authentication code, but the application stops working. Without version control, it may be difficult to identify what changed or restore an earlier working version.

Git helps you:

- Track changes to files.
- Save project history.
- Compare different versions.
- Restore earlier versions.
- Work with separate branches.
- Collaborate with other developers.

For example, Git allows you to inspect the history of HabitFlow code and identify which commit introduced a change.

## 3. What is GitHub?

GitHub is an online platform for hosting Git repositories.

| Git | GitHub |
|---|---|
| Version control system | Online platform for hosting Git repositories |
| Runs locally on your computer | Stores and shares repositories remotely |
| Tracks file changes | Provides collaboration and repository management |
| Local operations can work without internet | Internet is needed to exchange changes with the remote |

For HabitFlow:
- Git tracks your changes on your computer.
- GitHub stores a remote copy of your repository.
- You use Git commands to send and receive changes.

## 4. What is a repository?

A repository, often called a repo, is a project location managed by Git. It contains project files and Git history.

Example project structure:

```text
HabitFlow/
├── backend/
├── frontend/
├── database/
├── docs/
└── README.md
```

Two common repository locations are:

1. **Local repository:** The Git repository on your computer.
2. **Remote repository:** A hosted repository, such as the one on GitHub.

## 5. What is a branch?

A branch is an independent line of development in Git. Branches allow you to work on different parts of a project without mixing all changes into one development line.

Your HabitFlow project has two branches:

```text
                 HabitFlow Git repository
                         |
             +-----------+-----------+
             |                       |
             v                       v
     Main_Code_Branch        Main_Notes_Branch
             |                       |
       Application code         Learning notes
       Java / Spring Boot       Daily Markdown files
       React / TypeScript       Diagrams and exercises
       SQL / tests              Documentation
```

### 5.1 Why use two branches?

Your code and learning notes have different purposes.

| Branch | Example files |
|---|---|
| `Main_Code_Branch` | `HabitController.java`, `HabitService.java`, `pom.xml` |
| `Main_Notes_Branch` | `Day-1.md`, `Day-2.md`, `Day-3.md`, `Day-4.md` |

This keeps application code and learning documentation separate.

**Important:** Branches are not separate Git repositories. Both branches belong to the same repository and share Git history.

## 6. What is a commit?

A commit is a saved snapshot of tracked project changes in Git history. Think of it as a checkpoint.

Example project history:

```text
Commit 1: Created Spring Boot project
Commit 2: Added HelloController
Commit 3: Added HabitController
Commit 4: Added HabitService
```

Each commit should have a meaningful message explaining the change.

Good commit messages:

```text
Initial HabitFlow backend setup
Add HelloController endpoint
Add habit creation service
Add Day 4 Git learning notes
```

Avoid vague messages such as `update`, `changes`, `test`, or `final`.

## 7. Understand the Git workflow

Git has three important local areas:

1. **Working directory:** Where you edit files.
2. **Staging area:** Where you select changes for the next commit.
3. **Local repository:** Where commits are stored.

After committing, you can push commits to GitHub.

```text
Edit files
    |
    v
Working directory
    |
    | git add
    v
Staging area
    |
    | git commit
    v
Local repository
    |
    | git push
    v
GitHub remote repository
```

### 7.1 Working directory

This is where you create, edit, or delete project files. For example, you create `HelloController.java` in IntelliJ IDEA.

### 7.2 Staging area

The staging area lets you select which changes will be included in the next commit.

```bash
git add HelloController.java
```

### 7.3 Local repository

After staging, create a commit:

```bash
git commit -m "Add HelloController endpoint"
```

### 7.4 Remote repository

Push your commit to GitHub:

```bash
git push origin Main_Code_Branch
```

Here:
- `origin` is the commonly used name for the remote repository.
- `Main_Code_Branch` is the branch you are pushing.

## 8. Essential Git commands

Run these commands from your project directory in IntelliJ's terminal or PowerShell.

| Command | Purpose |
|---|---|
| `git --version` | Check Git installation |
| `git status` | Show working tree and staging status |
| `git branch` | List local branches |
| `git branch -a` | List local and remote-tracking branches |
| `git switch branch-name` | Switch branches |
| `git switch -c branch-name` | Create and switch to a new branch |
| `git add filename` | Stage a specific file |
| `git add .` | Stage changes under the current directory |
| `git commit -m "message"` | Create a commit |
| `git log --oneline` | Display compact commit history |
| `git push` | Push commits to a remote |
| `git pull` | Fetch and integrate remote changes |

### Check Git installation

```bash
git --version
```

Expected output resembles `git version 2.x.x`.

### Check the current branch

```bash
git branch
```

The current branch has an asterisk (`*`) beside its name.

### Check project status

```bash
git status
```

This tells you whether files are modified, staged, untracked, or whether the working tree is clean.

### Switch branches

To switch to the code branch:

```bash
git switch Main_Code_Branch
```

To switch to the notes branch:

```bash
git switch Main_Notes_Branch
```

Before switching, check whether you have uncommitted changes. Git may prevent a switch if it would overwrite your work.

### View commit history

```bash
git log --oneline
```

This shows a compact list of commits.

## 9. Practical example: Commit HabitFlow code

Suppose you created `HelloController.java` and tested it successfully.

First, switch to your code branch:

```bash
git switch Main_Code_Branch
```

Check the branch and project status:

```bash
git status
```

Stage the new file:

```bash
git add backend/habitflow-backend/src/main/java/com/habitflow/controller/HelloController.java
```

The exact path depends on where you stored your backend project.

Create a commit:

```bash
git commit -m "Add HelloController endpoint"
```

Push the commit:

```bash
git push origin Main_Code_Branch
```

## 10. Practical example: Commit learning notes

Suppose you completed the Day 4 lesson and downloaded its Markdown file.

Switch to your notes branch:

```bash
git switch Main_Notes_Branch
```

Place the file in:

```text
learning-notes/Month-1/Week-1/Day-4.md
```

Stage it:

```bash
git add learning-notes/Month-1/Week-1/Day-4.md
```

Commit it:

```bash
git commit -m "Add Month 1 Week 1 Day 4 notes"
```

Push it:

```bash
git push origin Main_Notes_Branch
```

Now your learning documentation is saved in the notes branch on GitHub.

## 11. What is `.gitignore`?

`.gitignore` is a file that tells Git which untracked files and directories it should ignore.

For HabitFlow, we generally should not commit generated build output, local IDE metadata, or secret configuration files.

Example:

```gitignore
# Maven build output
target/

# IntelliJ project metadata
.idea/

# Java compiled files
*.class

# Local environment secrets
.env
```

Important:
- `.gitignore` does not automatically stop tracking files already committed.
- Do not ignore files required to build the project, such as `pom.xml` or Maven Wrapper files.
- Never commit passwords, API keys, database credentials, or private tokens.

We will refine the project's `.gitignore` as the application grows.

## 12. Important Git safety rules

1. Always check your current branch before making a commit.
2. Run `git status` before staging files.
3. Read staged changes before committing.
4. Use meaningful commit messages.
5. Do not commit passwords, tokens, or other secrets.
6. Do not commit generated build output such as `target/`.
7. Push code changes to `Main_Code_Branch`.
8. Push learning notes to `Main_Notes_Branch`.
9. Do not force-push or rewrite shared history unless you understand the consequences.
10. Do not switch branches blindly when you have uncommitted changes.

To inspect staged changes:

```bash
git diff --cached
```

To inspect unstaged changes in tracked files:

```bash
git diff
```

## 13. Practical assignments

### Exercise 1: Explore your repository

Run:

```bash
git --version
git status
git branch
git branch -a
git log --oneline
```

Write down:

1. Which branch is currently active?
2. What are your two branch names?
3. How many commits are visible?
4. Is your working tree clean?

### Exercise 2: Practise switching branches

Switch to:

```bash
git switch Main_Code_Branch
```

Then switch to:

```bash
git switch Main_Notes_Branch
```

Verify the active branch using:

```bash
git branch
```

### Exercise 3: Understand the commit workflow

On the notes branch, create a small practice file named `git-practice.md`.

Add:

```markdown
# Git Practice

Today I learned:
- Git
- GitHub
- Branches
- Commits
- Push and pull
```

Then run:

```bash
git status
git add git-practice.md
git status
git commit -m "Add Git practice file"
git log --oneline
```

Push only if you have confirmed that `origin` is configured and the branch is the intended one.

### Exercise 4: Explain the workflow

In your own words, explain the difference between:

- `git add`
- `git commit`
- `git push`

## 14. Knowledge-check quiz

Try to answer without looking at the lesson.

**Q1. What is Git?**

A. A database  
B. A distributed version control system  
C. A Java framework

**Q2. What is GitHub?**

A. A platform for hosting Git repositories  
B. A Java compiler  
C. A database language

**Q3. Which command shows the current working tree status?**

A. `git push`  
B. `git status`  
C. `git commit`

**Q4. What does `git add` do?**

A. Deletes a branch  
B. Stages changes for a commit  
C. Pushes code to GitHub

**Q5. What does `git commit` do?**

A. Saves a snapshot in local Git history  
B. Installs Java  
C. Downloads Maven dependencies

**Q6. Which branch should contain HabitFlow Java application code?**

A. `Main_Notes_Branch`  
B. `Main_Code_Branch`  
C. Both branches must always contain identical files

**Q7. Which branch should contain the Day 4 Markdown lesson?**

A. `Main_Code_Branch`  
B. `Main_Notes_Branch`  
C. Neither branch

**Q8. What does `git push` do?**

A. Sends local commits to a remote repository  
B. Creates a Java class  
C. Stages a file

### Answer sheet

```text
Q1:
Q2:
Q3:
Q4:
Q5:
Q6:
Q7:
Q8:
```

### Answer key – check after attempting

| Question | Correct answer | Explanation |
|---|---|---|
| Q1 | B | Git is a distributed version control system. |
| Q2 | A | GitHub hosts Git repositories and supports collaboration. |
| Q3 | B | `git status` displays repository status. |
| Q4 | B | `git add` stages changes. |
| Q5 | A | `git commit` records a snapshot in local Git history. |
| Q6 | B | Application code belongs in `Main_Code_Branch`. |
| Q7 | B | Learning notes belong in `Main_Notes_Branch`. |
| Q8 | A | `git push` sends local commits to the remote repository. |

## 15. Day 4 completion checklist

- [ ] I understand Git.
- [ ] I understand GitHub.
- [ ] I understand local and remote repositories.
- [ ] I understand branches.
- [ ] I understand commits.
- [ ] I understand the working directory and staging area.
- [ ] I can check the current branch.
- [ ] I can switch branches.
- [ ] I can stage and commit a file.
- [ ] I understand how to push changes.
- [ ] I understand `.gitignore`.
- [ ] I completed the practical exercises.
- [ ] I attempted the quiz.

**Day 4 status:** Not yet completed. Update this after reviewing your practical work and quiz answers.

## 16. Key takeaways

1. Git tracks changes to project files.
2. GitHub hosts remote Git repositories.
3. A repository contains project files and version history.
4. A branch is an independent line of development.
5. A commit records a snapshot in local Git history.
6. `git add` stages changes.
7. `git commit` saves staged changes to local history.
8. `git push` sends local commits to a remote repository.
9. `git status` helps you understand your working tree.
10. HabitFlow code and learning notes belong in their respective branches.

## 17. What comes next?

**Month 1 – Week 1 – Day 5: HTTP, REST APIs and Request/Response Fundamentals**

Planned topics:
- What is HTTP?
- HTTP request and response structure.
- HTTP methods and status codes.
- What is a REST API?
- How a browser calls a Spring Boot endpoint.
- How to test an API using Postman.

## 18. Personal learning notes

**Current branch:**

_Write your answer here._

**What I understood well:**

_Write your notes here._

**What I need to revise:**

_Write your notes here._

**Git commands I practised:**

_Write your commands here._

**Errors I faced and how I fixed them:**

_Write your notes here._

**My quiz score:**

_Write your score here._
