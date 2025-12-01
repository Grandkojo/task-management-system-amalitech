## Design Decisions

This document explains the main design choices for the Java Project Management / Task Management System.
It focuses on how OOP concepts, storage strategy, ID generation, and roles are implemented.

### Overall Architecture

- **Layered design**
  - `models` package: domain entities like `Project`, `Task`, `User` and their subclasses.
  - `services` package: coordination logic for projects, tasks, status reports, and authentication.
  - `utils` package: console menus, colors, and validation helpers.
  - `Main` class: entry point, sets up sample data and handles the top-level loop.

- **In-memory storage**
  - All data is stored in arrays during a single run.
  - The goal is to practice arrays and basic algorithms rather than persistence.

### OOP: Abstraction, Inheritance, Polymorphism

#### Projects

- `Project` is an **abstract base class** providing shared fields and behavior:
  - `id`, `name`, `description`, `budget`, `teamSize`, `projectType`.
  - Common methods like `getId()`, `getName()`, `getBudget()`, `displayProject()`.
- Two concrete subclasses:
  - `SoftwareProject`
  - `HardwareProject`
- Each subclass customises how project details are displayed, demonstrating **inheritance** and **polymorphism**.

#### Users and Roles

- `User` is an **abstract base class** with:
  - Auto-generated ID (`USR-1`, `USR-2`, …).
  - `name`, `email`, `role` and `getDisplayLabel()` used in the UI.
  - An abstract method `canDeleteTasks()` that expresses **capability**, not type.
- Two concrete subclasses:
  - `AdminUser` - overrides `canDeleteTasks()` to return `true`.
  - `RegularUser` - overrides `canDeleteTasks()` to return `false`.
- `AuthService` holds the **current logged-in user**, and services like `TaskService` query it to enforce permissions.
  - Example: `TaskService.removeTask(Scanner, ...)` checks `currentUser.canDeleteTasks()` before allowing deletion.
- This shows **polymorphism**: code works with `User`, but behaviour changes depending on whether the instance is `AdminUser` or `RegularUser`.

#### Tasks and Interfaces

- `Task` represents a piece of work under a project.
- It implements the `Completable` interface, which represents entities that can be completed.
- `Task` holds:
  - `id`, `projectId`, `name`, `status` (enum: `PENDING`, `IN_PROGRESS`, `COMPLETED`).
  - Methods to update status and query whether it is completed.
- `StatusReport` extends `Task` and can be used as a specialised task-like report object.

### Storage Strategy: Arrays

- Requirements specified **use of arrays for storage**, so:
  - `ProjectService` manages a fixed-size `Project[] allProjects` with a manual `projectCount` pointer.
  - `TaskService` manages a fixed-size `Task[] tasks` with a manual `taskCount` pointer.
- New entities are appended while there is capacity.
- Deletion is handled by shifting remaining elements down one position, then nulling the last entry.
- `getProjectCount()`, `projectExists()`, and `taskExists()` provide safe lookup helpers for other parts of the code.

### ID Auto-generation

- **Projects**
  - `Project` maintains a static `projectCount` and a private `generateProjectId()` method.
  - Each new project gets an ID like `PRJ001`, `PRJ002`, etc.
- **Tasks**
  - `Task` maintains a static `taskCount` and a private `generateTaskId()` method.
  - Each new task gets an ID like `TSK001`, `TSK002`, etc.
- **Users**
  - `User` maintains a static `userCount`.
  - Each new user gets an ID like `USR-1`, `USR-2`, etc.
- IDs are **unique within a single application run**. Because storage is in memory only, counters reset when the program restarts, which is acceptable for this learning-focused project.

### Validation and Input Handling

- `ValidationUtils` centralises project-related validation:
  - `isValidProjectType(String)` ensures only `Software` and `Hardware` are accepted.
  - `isValidBudgetRange(long, long)` enforces positive values and `min <= max`.
- `TaskService` validates:
  - That a project exists before creating a task.
  - That there is no duplicate task with the same name under the same project.
  - That status values are only `Pending`, `In Progress`, or `Completed`.
- Menu handlers in `ProjectService` and `TaskService` use `Scanner` checks (`hasNextInt`) to avoid crashing on invalid numeric input and show friendly error messages instead.

### Role-based Access Control

- The system implements a simple **role-based rule**: only admins can delete tasks.
- Flow:
  1. `Main` creates an `AdminUser` and a `RegularUser` and chooses one as the current user.
  2. `AuthService.login(currentUser)` stores the selected user.
  3. When the user selects "Delete Task", `TaskService.removeTask(Scanner, ...)`:
     - Calls `AuthService.getCurrentUser()`.
     - Calls `canDeleteTasks()` on the returned `User` instance.
     - If `false`, prints an error and returns to the menu without deleting.
- This showcases how **polymorphism and encapsulation** can be used to enforce permissions cleanly.

### Status Reports and Progress

- `TaskService.completionRate(projectId)` calculates completion percentage per project:
  - Counts tasks for a given `projectId`.
  - Counts how many have status `COMPLETED`.
  - Returns a percentage as a `double`.
- Reports use this data to summarise project health in the console.

### Limitations and Future Improvements

- Data is stored **in memory only**; exiting the program loses all changes.
- ID counters reset on each run, so IDs are unique per session, not globally.
- Arrays have fixed capacity (20 projects, 50 tasks), so the app warns when storage is full.
- Future improvements could include:
  - Replacing arrays with collections (`ArrayList`).
  - Persisting data to a file or database.
  - Adding proper authentication instead of hard-coded users.
  - More detailed status reports and filtering.


