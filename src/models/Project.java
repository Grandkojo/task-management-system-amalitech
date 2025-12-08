package models;

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

    protected Project(String name, String description, long budget, int teamSize, String projectType) {
        this.id = generateProjectId();
        this.name = name;
        this.description = description;
        this.budget = budget;
        this.teamSize = teamSize;
        this.projectType = projectType;
    }

    /**
    *   Allow only one thread to call at a time, preventing 
    *   duplicated project ids
    */
    private static synchronized String generateProjectId() {
        projectCount++;
        return String.format("PRJ%03d", projectCount);
    }
    public String getProjectType() { return this.projectType; }
    
    public abstract Project displayProject(Project p);

    public String getId() { return this.id; }

    public String getName() { return this.name; }

    public long getBudget() { return this.budget; }

    public int getTeamSize() { return this.teamSize; }

    public String getDescription() { return this.description; }


    public int getProjectsCount() { return projectCount; }

}
