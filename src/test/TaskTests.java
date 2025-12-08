package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import models.HardwareProject;
import models.SoftwareProject;
import models.Task;
import services.ProjectService;
import services.TaskService;
import utils.exceptions.ProjectFullException;
import utils.exceptions.ProjectNotFoundException;
import utils.exceptions.TaskFullException;
import utils.exceptions.TaskNotFoundException;

public class TaskTests {

    private HardwareProject hardwareProject;
    private SoftwareProject softwareProject;
    private Task hardwareTask1, hardwareTask2, hardwareTask3, softwareTask1, softwareTask2, softwareTask3;

    @BeforeEach
    public void setUp() throws ProjectFullException, TaskFullException{

        this.hardwareProject = new HardwareProject("Hardware Project", "This is a hardware project ", 1500, 5);
        this.softwareProject = new SoftwareProject("Software Project", "This is a software project ", 1500, 5);
        ProjectService.addProjectToStorage(hardwareProject);
        ProjectService.addProjectToStorage(softwareProject);

        this.hardwareTask1 = Task.create("Hardware Task 1", Task.Status.IN_PROGRESS, hardwareProject.getId());
        this.hardwareTask2 = Task.create("Hardware Task 2", Task.Status.PENDING, hardwareProject.getId());
        this.hardwareTask3 = Task.create("Hardware Task 3", Task.Status.IN_PROGRESS, hardwareProject.getId());

        this.softwareTask1 = Task.create("Software Task 1", Task.Status.IN_PROGRESS, softwareProject.getId());
        this.softwareTask2 = Task.create("Software Task 2", Task.Status.PENDING, softwareProject.getId());
        this.softwareTask3 = Task.create("Software Task 3", Task.Status.IN_PROGRESS, softwareProject.getId());

        TaskService.addTaskToStorage(hardwareTask1);
        TaskService.addTaskToStorage(hardwareTask2);
        TaskService.addTaskToStorage(hardwareTask3);
        TaskService.addTaskToStorage(softwareTask1);
        TaskService.addTaskToStorage(softwareTask1);
        TaskService.addTaskToStorage(softwareTask1);

    }

    @Test
    public void createTaskTest(){
        assertInstanceOf(Task.class, hardwareTask1);
        assertInstanceOf(Task.class, hardwareTask2);
        assertInstanceOf(Task.class, hardwareTask3);

        assertInstanceOf(Task.class, softwareTask1);
        assertInstanceOf(Task.class, softwareTask2);
        assertInstanceOf(Task.class, softwareTask3);
    }

    @Test
    public void taskCreatedToRightProjectTest(){

        this.hardwareTask1 = Task.create("Hardware Task 1", Task.Status.IN_PROGRESS, "INVID");

        assertThrows(ProjectNotFoundException.class, () -> {
            ProjectService.projectExists(this.hardwareTask1.getProjectId());
        });

    }

    @Test
    public void updateTaskWithRightStatusTest() throws TaskNotFoundException{

        assertTrue(TaskService.updateTask(this.softwareTask1.getId(), Task.Status.COMPLETED));
        assertEquals(Task.Status.COMPLETED, this.softwareTask1.getStatus());
    }
    
}
