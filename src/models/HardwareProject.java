package models;

public class HardwareProject extends Project {

    private static String projectType = "Hardware";

    public HardwareProject(String name, String description, long budget, int teamSize)
    {
        super(name, description, budget, teamSize, projectType);
    }    

    @Override
    public Project displayProject(Project p){
        return p;
    }

}
