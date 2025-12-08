package models;

public class SoftwareProject extends Project {

    private static String projectType = "Software";

    public SoftwareProject(String name, String description, long budget, int teamSize)
    {
        super(name, description, budget, teamSize, projectType);
    }

    @Override
    public Project displayProject(Project p){
        return p;
    }
}
