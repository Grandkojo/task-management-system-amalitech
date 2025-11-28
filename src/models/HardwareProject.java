package models;

public class HardwareProject extends Project {

     private static String projectType = "Hardware";

    public HardwareProject(String name, String description, long budget, int teamSize)
    {
        super(name, description, budget, teamSize, projectType);
    }

    public void displayProjectDetailsHeader()
    {
        System.out.println("\n\n=============================================================================");
        System.out.printf("|\t PROJECT DETAILS: %s \t\t|%n", this.getId());
        System.out.println("\n==============================================================================\n\n");   
    }

    public void getProjectDetails(){

        System.out.printf("Project Name: %s%nDescription: %s%nType: %s%nTeam Size: %d%nBudget: $%d%n%n",
            this.getName(), this.getDescription(), projectType, this.getTeamSize(), this.getBudget());   
    }

    // @Override
    // public String getProjectType()
    // {
    //     return this.projectType;
    // }

}
