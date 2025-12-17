package services;
import interfaces.TaskFilter;
import models.Project;
import models.SoftwareProject;
import models.Task;
import models.Task.Status;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class StreamServiceTests {

    @Test
    void filterTasks_filtersByProjectAndPredicate() {
        Task t1 = Task.create("Task 1", Status.PENDING, "PRJ001");
        Task t2 = Task.create("Task 2", Status.COMPLETED, "PRJ001");
        Task t3 = Task.create("Task 3", Status.IN_PROGRESS, "PRJ002");

        List<Task> all = Arrays.asList(t1, t2, t3);
        TaskFilter onlyCompletedInPrj1 =
                task -> "PRJ001".equals(task.getProjectId()) && task.getStatus() == Status.COMPLETED;

        List<Task> result = StreamService.filterTasks(all, onlyCompletedInPrj1);

        assertEquals(1, result.size());
        assertEquals("Task 2", result.get(0).getName());
    }

    @Test
    void completionRate_calculatesPercentageForProject() {
        Task t1 = Task.create("Task 1", Status.COMPLETED, "PRJ001");
        Task t2 = Task.create("Task 2", Status.IN_PROGRESS, "PRJ001");
        Task t3 = Task.create("Task 3", Status.PENDING, "PRJ001");

        double rate = StreamService.completionRate(Arrays.asList(t1, t2, t3), "PRJ001");

        assertEquals(33.33, rate, 0.5); // roughly one out of three
    }

    @Test
    void filterProjectsByType_returnsOnlyMatchingType() {
        Project p1 = new SoftwareProject("P1", "Desc", 1000, 3);
        Project p2 = new SoftwareProject("P2", "Desc", 2000, 4);

        // use anonymous Project subclass to represent Hardware project
        Project p3 = new Project("HW1", "Desc", 1500, 2, "Hardware") {
            @Override
            public Project displayProject(Project p) {
                return p;
            }
        };

        List<Project> result = StreamService.filterProjectsByType(Arrays.asList(p1, p2, p3), "Software");

        assertEquals(2, result.size());
        assertTrue(result.contains(p1));
        assertTrue(result.contains(p2));
    }
}
