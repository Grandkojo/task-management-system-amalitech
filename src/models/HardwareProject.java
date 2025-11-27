package models;

public class HardwareProject extends Project {

     private static String projectType = "Hardware";

    public HardwareProject(String name, String description, long budget, int teamSize)
    {
        super(name, description, budget, teamSize);
    }

    public void getProjectDetails(){

        System.out.printf("%nPROJECT DETAILS : %s%n%nProject Name: %s%nDescription: %s%nType: %s%nTeam Size: %d%nBudget: $ %d%n%n",
            this.getId(), this.getName(), this.getDescription(), projectType, this.getTeamSize(), this.getBudget());   
    }
}
