## Java Project Management / Task Management System

This is a console-based Java application that lets you create projects, manage tasks, calculate progress, and experiment with core OOP concepts (abstraction, inheritance, polymorphism, and encapsulation).
It was built as a gradual improvement project to deepen my understanding of Java and object-oriented design.

### Setup

- **Requirements**
  - JDK 21 (or compatible JDK 17+)
  - IntelliJ IDEA (recommended) or any Java-capable IDE

- **Clone and open**
  - Clone this repository.
  - Open the project folder in IntelliJ as a **Java project**.

- **Run the app**
  - Locate the `Main` class in `src/Main.java`.
  - Run the `main` method from your IDE, or from terminal:
    - `cd` into the project root.
    - Compile sources into `bin` (if not already compiled by IDE).
    - Run: `java -cp bin Main`

### How to Use

When you start the application, you will see:

- A header showing the **current user** (Admin or Regular).
- The **Main Menu** with options to:
  - Manage Projects
  - Manage Tasks
  - View Status Reports
  - Exit Application

#### User Roles

User roles are created programmatically in `Main`:

- `AdminUser` – can view and manage everything **including deleting tasks**.
- `RegularUser` – can create and update tasks, but **cannot delete tasks**.

In `Main.main`, you can switch the logged-in user by choosing which instance is assigned to `currentUser` before starting the loop. The active user is stored via `AuthService` and used for permission checks.

#### Projects

- Projects are created from the **Projects menu**.
- Two concrete project types:
  - `SoftwareProject`
  - `HardwareProject`
- Projects are stored in a fixed-size array (`Project[]`) inside `ProjectService`.
- Each project gets an **auto-generated sequential ID** like `PRJ001`, `PRJ002`, etc.

You can:

- Add a new project (name, description, budget, team size, type).
- View all projects and select one to see detailed information.
- Filter by type (Software/Hardware) or by budget range.

#### Tasks

- Tasks are always associated with a specific project (via project ID).
- Tasks are stored in a fixed-size array (`Task[]`) inside `TaskService`.
- Each task gets an **auto-generated sequential ID** like `TSK001`, `TSK002`, etc.

From the **Tasks menu** you can:

- Add a task to a project.
- View all tasks for a chosen project.
- Update a task’s status (Pending / In Progress / Completed).
- Delete a task **(Admin only)**.

Completion percentage per project is calculated by `TaskService.completionRate` and displayed when viewing tasks for that project.

#### Status Reports

- The **Status Reports** option generates a simple report that summarises project progress using task completion data.
- Reports are currently generated in memory and shown in the console.

### Object-Oriented Design

The project demonstrates several OOP principles:

- **Encapsulation**
  - Model classes (`Project`, `Task`, `User`, and subclasses) use private/protected fields with public getters.
  - Services (`ProjectService`, `TaskService`, `ReportService`) expose clear methods instead of exposing arrays directly.

- **Abstraction & Inheritance**
  - `Project` is an abstract base class extended by `SoftwareProject` and `HardwareProject`.
  - `User` is an abstract base class extended by `AdminUser` and `RegularUser`.

- **Polymorphism**
  - `User.canDeleteTasks()` is overridden so `AdminUser` can delete tasks and `RegularUser` cannot.
  - `Project` subclasses override methods that display project details.

- **Interfaces**
  - `Completable` interface is implemented by `Task` to represent “completable” entities.

### ID Auto-generation

- Projects use a static counter in `Project` to generate IDs in the form `PRJ001`, `PRJ002`, … within a single run.
- Tasks use a static counter in `Task` to generate IDs in the form `TSK001`, `TSK002`, … within a single run.
- Users use a static counter in `User` to generate IDs like `USR-1`, `USR-2`, …

IDs are guaranteed unique **within a single session** of the application. Because everything is in memory, restarting the app resets the counters.

### Validation and Error Handling

- Input validation uses `ValidationUtils` to check:
  - Valid project types (`Software`, `Hardware`).
  - Valid budget ranges (min and max constraints).
- Task creation validates project existence and avoids duplicate task names per project.
- Menu handlers check for numeric input and show user-friendly error messages.

### UML and Design Documentation

- A **class diagram UML** image is available at `docs/task_manager_uml.png`.
- Detailed design rationale (abstraction, inheritance, polymorphism, storage choices, and ID strategy) is described in `docs/design-decisions.md`.

### Minimum Requirements Coverage (Summary)

- Two project types implemented (`SoftwareProject`, `HardwareProject`).
- Two user types implemented (`AdminUser`, `RegularUser`).
- Arrays used for project and task storage.
- Encapsulation applied to models.
- Abstract classes and interfaces implemented.
- Polymorphism demonstrated via users and projects.
- Input validation implemented for project type, budget range, task status, and existence checks.
- Completion percentage calculation working via `TaskService.completionRate`.
- Console navigation wired through `ProjectService` and `TaskService` menus.

Note: the number of sample projects created on startup is configured in `Main.createBaseProjects` and can be adjusted as needed for testing.