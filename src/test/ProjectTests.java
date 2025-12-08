package test;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;


import models.HardwareProject;
import models.SoftwareProject;
import services.ProjectService;
import utils.exceptions.ProjectFullException;
import utils.exceptions.ProjectNotFoundException;
import models.Project;

public class ProjectTests {

    private Project project;
    private HardwareProject hardwareProject;
    private SoftwareProject softwareProject;

    @BeforeEach
    public void setUp() throws ProjectFullException{
        this.hardwareProject = new HardwareProject("Hardware Project", "This is a hardware project ", 1500, 5);
        this.softwareProject = new SoftwareProject("Software Project", "This is a software project ", 1500, 5);
        this.project = hardwareProject;
        ProjectService.addProjectToStorage(hardwareProject);
        ProjectService.addProjectToStorage(softwareProject);

    }

    @Test
    public void createHardwareProjectTest(){
        assertInstanceOf(HardwareProject.class, hardwareProject);
        assertInstanceOf(Project.class, project);
        assertNotNull(hardwareProject);
    }

    @Test
    public void createSoftwareProjectTest(){
        assertInstanceOf(SoftwareProject.class, softwareProject);
        assertInstanceOf(Project.class, project);
        assertNotNull(softwareProject);
    }    

    @Test
    public void projectNoFoundTest(){
        String id = "INVID";

        assertThrows(ProjectNotFoundException.class, () -> {
            ProjectService.findProject(id);
        }); 

    }


}
