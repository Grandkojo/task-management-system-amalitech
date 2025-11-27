package models;
import java.util.UUID;

public abstract class Project {
    //define fields


    private String id;
    private String name;
    private String description;
    private long budget;
    private int teamSize;
    
    //storage can take up to only  20 projects
    private static Project[] allProjects = new Project[20]; 
    private static int projectCount = 0;

    public Project(String name, String description, long budget, int teamSize)
    {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.description = description;
        this.budget = budget;
        this.teamSize = teamSize;

        //add project to list
        if (projectCount < allProjects.length)
        {
            allProjects[projectCount] = this;
            projectCount++; 
        } else {
            System.out.println("Warning! Project list is full. Could not add Project" + this.name);
        }
    }

    public void getProjectDetails(){
    }

    public String getId()
    {
        return this.id;
    }

    public String getName()
    {
        return this.name;
    }

    public long getBudget()
    {
        return this.budget;
    }

    public int getTeamSize()
    {
        return this.teamSize;
    }

    public String getDescription()
    {
        return this.description;
    }

    public void displayProject()
    {
        System.out.printf("Project ID: %s  - Name %s%n%n", this.id, this.name);
    }

    // public filterByBudget(){}

}
