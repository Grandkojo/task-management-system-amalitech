package models;
import java.util.Scanner;
import java.util.UUID;
import static utils.ConsoleColors.*;
public abstract class Project {
    //define fields

    // private static Scanner scanner = new Scanner(System.in);


    private String id;
    private String name;
    private String description;
    private long budget;
    private int teamSize;
    private String projectType;
    
    //storage can take up to only  20 projects
    private static Project[] allProjects = new Project[20]; 
    private static int projectCount = 0;

    public Project(String name, String description, long budget, int teamSize, String projectType)
    {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.description = description;
        this.budget = budget;
        this.teamSize = teamSize;
        this.projectType = projectType;

        //add project to list
        if (projectCount < allProjects.length)
        {
            allProjects[projectCount] = this;
            projectCount++; 
        } else {
            System.out.println("Warning! Project list is full. Could not add Project" + this.name);
        }
    }

    public static Project findProject(String id){
        for (int i = 0; i < projectCount; i++){
            Project p = allProjects[i];

            if (p != null && p.id.equals(id))
                return p;
        }   
        return null;
    }

    public abstract void displayProjectDetailsHeader();

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
        System.out.printf("Project ID: %s%nName %s%nType: %s%nBudget: %d%n%n", this.id, this.name, this.projectType, this.budget);
    }

    public static void displayProjectDetails(Scanner scanner)
    {
        System.out.print("Enter project ID to view details (or 0 to return): ");
        
        if (scanner.hasNext()){

            String choice = scanner.next();
            scanner.nextLine();

            switch (choice) {
                case "0":
                    Project.displayProjectHeader();
                    Project.displayProjectMenu();
                    break;
                default:
                    Project foundProject = Project.findProject(choice);
                    if (foundProject != null)
                        foundProject.getProjectDetails();
                    break;
            }
        }
        scanner.nextLine();
    }

    public static void displayProjects(Scanner scanner)
    {
        System.out.printf("All projects (%s)%n%n", projectCount);
        System.out.println("-----------------------------------------------------------------------------------------------------------------");
        System.out.println("|ID\t\t\t\t| PROJECT NAME\t| DESCRIPTION\t\t\t| TYPE\t| TEAM SIZE\t| BUDGET|");
        System.out.println("-----------------------------------------------------------------------------------------------------------------");

        // for ( int i = 0; i < projectCount; i++){
        //     Project p = allProjects[i];
        //     p.displayProject();
        // }

        for ( int i = 0; i < projectCount; i++){
            Project p = allProjects[i];
            // p.displayProject();
            System.out.printf("|%s| %s| %s| %s| %d| %d|", p.id, p.name, p.description, p.projectType, p.teamSize, p.budget);
            System.out.println("\n-----------------------------------------------------------------------------------------------------------------\n");
        }

        displayProjectDetails(scanner);
        
    }

    // public abstract String getProjectType();

    public static void filterByType(String projectType, Scanner scanner)
    {
        if (projectType == null || (!"Software".equals(projectType) && !"Hardware".equals(projectType))) {
            System.out.println("Invalid project type: " + projectType + "\n");
            return;
        }
                
        System.out.println("\n\n==================================");
        System.out.printf("|\t%s Projects\t|\n", projectType);
        System.out.println("==================================\n\n");

        for (int i = 0; i < projectCount; i++) {
            Project p = allProjects[i];
            if (p != null && projectType.equals(p.projectType)) {
                p.displayProject();
            }
        }

        displayProjectDetails(scanner);
    }
    public static void filterByBudget(long minAmount, long maxAmount, Scanner scanner)
    {
        if (minAmount <= 0 || maxAmount <= 0) {
            System.out.println("Both minimum and maximum amounts needed");
            return;
        }

        if (minAmount > maxAmount) {
            System.out.println("Minimum amount cannot be greater than maximum amount");
            return;
        }

        int count = 0;
        System.out.println("\n\n==================================");
        System.out.println("|\tProjects within range\t|");
        System.out.println("==================================\n\n");

        for ( int i = 0; i < projectCount; i++){
            Project p = allProjects[i]; 
            if (p != null && minAmount <= p.budget && p.budget <= maxAmount)
            {
                count++;
                p.displayProject();       
            }
        }

        if(count == 0)
            System.out.println("No projects found within budget range\n\n");
        else
            displayProjectDetails(scanner);

    }

    public static void addProject(Scanner scanner)
    {
        System.out.print("Enter project name: ");
        String pName = scanner.nextLine();

        System.out.print("Enter project description: ");
        String pDescription = scanner.nextLine();
        
        System.out.print("Enter budget (numbers): ");
        long pBudget = scanner.nextLong();
        scanner.nextLine();

        System.out.print("Enter team size (numbers): ");
        int pTeamSize = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter project type (Software or Hardware - type in full): ");
        String pProjectType = scanner.next();

        Project newProject = null;

        if (pProjectType.equals("Software")) {
            newProject =  new SoftwareProject(pName, pDescription, pBudget, pTeamSize);
        } else if (pProjectType.equals("Hardware")){
            newProject = new HardwareProject(pName, pDescription, pBudget, pTeamSize);
        } else {
            System.out.println(RED + "Invalid project type: " + pProjectType + ". Project not created.\n" + RESET);
        }

        if (newProject != null) {
            System.out.print(GREEN + "\nProject " + newProject.getName() + " created successfully!\n" + RESET);
        }

        scanner.nextLine();

        Project.displayProjectHeader();
        displayProjectMenu();
    }

    public static void displayProjectHeader()
    {
        System.out.println("\n\n==================================");
        System.out.println("|\t PROJECT CATALOG \t|");
        System.out.println("==================================\n\n");
    }

      public static void displayProjectAddHeader()
    {
        System.out.println("\n\n==================================");
        System.out.println("|\t ADD NEW PROJECT \t|");
        System.out.println("==================================\n\n");
    }
    
    public static void displayProjectMenu()
    {
        System.out.println("Filter Options:");
        System.out.println("-----------");
        System.out.println("1. Add Project");
        System.out.printf("2. View All Projects (%d)%n", projectCount);
        System.out.println("3. Software Projects Only");
        System.out.println("4. Hardware Projects Only");
        System.out.println("5. Search by Budget Range");
        System.out.print("\n\nEnter filter choice: ");
    }

    public static void handleProjectUserInput(Boolean isRunning, Scanner scanner)
    {
        if (scanner.hasNextInt())
        {
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    Project.displayProjectAddHeader();
                    Project.addProject(scanner);
                    Project.handleProjectUserInput(isRunning, scanner);
                    break;
                case 2:
                    Project.displayProjects(scanner);
                    break;
                case 3:
                    Project.filterByType("Software", scanner);
                    break;
                case 4:
                    Project.filterByType("Hardware", scanner);
                    break;
                case 5:
                    System.out.print("Enter mininum amount (numbers): ");
                    long min = scanner.nextInt();
                    System.out.print("Enter maximum amount (numbers): ");
                    long max = scanner.nextInt();

                    Project.filterByBudget(min, max, scanner);
                    break;
                default:
                    System.out.println("\n>> Invalid input. Please a number between 1 - 5");
            }
        } else {
            System.out.println("\n>> Invalid input, Please enter a number");
            scanner.next();
        }
        if(isRunning)
        {
            // System.out.println("\nPress Enter to continue...");
            scanner.nextLine();
        }
    }

}
