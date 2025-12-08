package services;

import models.Project;
import utils.ConsoleMenu;
import utils.exceptions.EmptyProjectListException;

/**
 * Builds a simple aggregate status report over all projects and their tasks.
 */
public class ReportService {


    public static void generateStatusReport() throws EmptyProjectListException {
    
        if (ProjectService.getProjectCount() == 0) {
            throw new EmptyProjectListException("No projects found to generate status report");
        }

        ConsoleMenu.displayStatusReportHeader();
        float averageCompletion = 0.0f;
        float totalProgress = 0.0f;
        float progressCount = 0.0f;
        System.out.println("-----------------------------------------------------------------------------------------------------------------");
        System.out.println("|PROJECT ID\t\t\t\t| PROJECT NAME\t| \t TASKS \t| COMPLETED\t| PROGRESS\t|");
        System.out.println("-----------------------------------------------------------------------------------------------------------------");
       
        for (Project p : ProjectService.getProjects())
        {
            float[] taskReport = TaskService.getTasksReport(p.getId());
            System.out.printf("|%s\t| %s\t| %d| %d| %.1f%%|\n", p.getId(), p.getName(), (int) taskReport[0], (int) taskReport[1], taskReport[2]);
            System.out.println("-----------------------------------------------------------------------------------------------------------------");

            progressCount++;
            totalProgress += taskReport[2];
        }

        averageCompletion = totalProgress / progressCount;

        System.out.printf("AVERAGE COMPLETION: %.1f%", averageCompletion);
        System.out.println("\n-----------------------------------------------------------------------------------------------------------------");


    }
}
