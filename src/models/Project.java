package models;

import services.ProjectService;

/**
 * Base class for all projects. Holds common project data and
 * generates sequential IDs (PRJ001, PRJ002, ...) for each run.
 */
public abstract class Project {
    
    private static int projectCount = 0;

    private String id;
    private String name;
    private String description;
    private long budget;
    private int teamSize;
    private String projectType;

    public Project(String name, String description, long budget, int teamSize, String projectType) {
        this.id = generateProjectId();
        this.name = name;
        this.description = description;
        this.budget = budget;
        this.teamSize = teamSize;
        this.projectType = projectType;

        // Add project to storage via service
        ProjectService.addProjectToStorage(this);
    }

    private static String generateProjectId() {
        projectCount++;
        return String.format("PRJ%03d", projectCount);
    }

    public abstract void displayProjectDetailsHeader();

    public void getProjectDetails() {
    }

    public String getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public long getBudget() {
        return this.budget;
    }

    public int getTeamSize() {
        return this.teamSize;
    }

    public String getDescription() {
        return this.description;
    }

    public String getProjectType() {
        return this.projectType;
    }

    public void displayProject() {
        System.out.printf("Project ID: %s%nName %s%nType: %s%nBudget: %d%n%n", this.id, this.name, this.projectType, this.budget);
    }
}
