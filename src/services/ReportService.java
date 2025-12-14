package services;

import utils.ConsoleMenu;
import utils.exceptions.EmptyProjectListException;

/**
 * Builds a simple aggregate status report over all projects and their tasks.
 */
public class ReportService {


    /**
     * Print a status report showing per-project task counts and progress, plus average completion.
     * @throws EmptyProjectListException when no projects exist.
     */
    public static void generateStatusReport() throws EmptyProjectListException {
    
        if (ProjectService.getProjectCount() == 0) {
            throw new EmptyProjectListException("No projects found to generate status report");
        }

        ConsoleMenu.displayStatusReportHeader();
        float averageCompletion = 0.0f;

        final int[] progressCount = {0};
        final double[] totalProgress = {0.0};
        System.out.println("-----------------------------------------------------------------------------------------------------------------");
        System.out.println("|PROJECT ID\t\t\t\t| PROJECT NAME\t| \t TASKS \t| COMPLETED\t| PROGRESS\t|");
        System.out.println("-----------------------------------------------------------------------------------------------------------------");
        ProjectService.getProjects().stream()
            .forEach(p -> {
                float[] taskReport = TaskService.getTasksReport(p.getId());
                System.out.printf("|%s\t| %s\t| %d| %d| %.1f%%|\n", p.getId(), p.getName(), (int) taskReport[0], (int) taskReport[1], taskReport[2]);
                System.out.println("-----------------------------------------------------------------------------------------------------------------");
                progressCount[0]++;
                totalProgress[0] += taskReport[2];
            });

        averageCompletion = progressCount[0] == 0 ? 0.0f : (float) (totalProgress[0] / progressCount[0]);
        System.out.printf("AVERAGE COMPLETION: %.1f%%", averageCompletion);
        System.out.println("\n-----------------------------------------------------------------------------------------------------------------");


    }
}
