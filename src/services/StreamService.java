package services;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import interfaces.TaskFilter;
import models.Project;
import models.Task;
import models.Task.Status;

/**
 * Central place for stream and lambda based operations on tasks and projects.
 * Keeps functional-style queries reusable and easy to test.
 */
public final class StreamService {

    private StreamService() {
        // utility class
    }

    /**
     * Filter tasks using the provided TaskFilter.
     * @param tasks source tasks
     * @param filter functional filter
     * @return list of tasks that satisfy the filter
     */
    public static List<Task> filterTasks(Collection<Task> tasks, TaskFilter filter) {
        return tasks.stream()
                .filter(t -> t != null)
                .filter(filter::test)
                .collect(Collectors.toList());
    }

    /**
     * Calculate completion rate for tasks under a given project.
     * @param tasks source tasks
     * @param projectId project identifier
     * @return completion percentage in range 0-100
     */
    public static double completionRate(Collection<Task> tasks, String projectId) {
        TaskFilter byProject = t -> t.getProjectId().equals(projectId);

        long total = tasks.stream()
                .filter(byProject::test)
                .count();

        long completed = tasks.stream()
                .filter(byProject::test)
                .filter(t -> t.getStatus() == Status.COMPLETED)
                .count();

        return total == 0 ? 0.0 : (completed * 100.0) / total;
    }

    /**
     * Filter projects by type.
     * @param projects source projects
     * @param type project type (e.g. Software, Hardware)
     * @return list of projects matching the type
     */
    public static List<Project> filterProjectsByType(Collection<Project> projects, String type) {
        return projects.stream()
                .filter(p -> p != null)
                .filter(p -> type.equals(p.getProjectType()))
                .collect(Collectors.toList());
    }

    /**
     * Filter projects within a budget range.
     * @param projects source projects
     * @param min minimum budget
     * @param max maximum budget
     * @return list of projects whose budget is within [min, max]
     */
    public static List<Project> filterProjectsByBudget(Collection<Project> projects, long min, long max) {
        return projects.stream()
                .filter(p -> p.getBudget() >= min && p.getBudget() <= max)
                .collect(Collectors.toList());
    }
}


