package models;

import java.util.UUID;
import services.ProjectService;

public abstract class Project {
    
    private String id;
    private String name;
    private String description;
    private long budget;
    private int teamSize;
    private String projectType;

    public Project(String name, String description, long budget, int teamSize, String projectType) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.description = description;
        this.budget = budget;
        this.teamSize = teamSize;
        this.projectType = projectType;

        // Add project to storage via service
        ProjectService.addProjectToStorage(this);
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
