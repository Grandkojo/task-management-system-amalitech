package utils;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import models.Project;
import models.Task;
import services.GsonProvider;
import services.ProjectService;
import services.TaskService;

public class FileUtils {

    final static String fileName = "data/projects_data.json";

    public static void saveProjects() {
        Collection<Project> projects = ProjectService.getProjects();
        ArrayList<Task> tasks = TaskService.getAllTasks();
        Gson gson = GsonProvider.getGson();

        Map<String, List<Task>> tasksByProjectId = tasks.stream()
                .collect(Collectors.groupingBy(Task::getProjectId));

        System.out.println("Saving project data...");

        projects.forEach(project -> {
            String projectId = project.getId();
            List<Task> associatedTasks =
                    tasksByProjectId.getOrDefault(projectId, new ArrayList<>());
            project.setTasks(associatedTasks);
        });

        Type type = new TypeToken<Collection<Project>>() {}.getType();
        String jsonProjects = gson.toJson(projects, type);

        try {
            Files.write(
                Paths.get(fileName),
                jsonProjects.getBytes(StandardCharsets.UTF_8),
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING
            );
            System.out.println("Data written to " + fileName + " successfully!");
        } catch (IOException e) {
            throw new RuntimeException("Error writing JSON file", e);
        }
    }


    public static void loadProjects() {
        Gson gson = GsonProvider.getGson();
        try {
            Collection<String> lines = Files.readAllLines(Paths.get(fileName));
            String jsonProjects = lines.stream().collect(Collectors.joining("\n"));
            Type collectionType = new TypeToken<Collection<Project>>(){}.getType();
            Collection<Project> projects = gson.fromJson(jsonProjects, collectionType);

            long projectsCount = projects.size();
            System.out.println("Loading projects from file...");
            projects.forEach(project -> {
                ProjectService.addProjectToStorage(project);
                project.getTasks().forEach(TaskService::addTaskToStorage);
            });
            System.out.printf("%d projects loaded successfully from %s", projectsCount, fileName);
        } catch (IOException e) {
            System.out.println("Unable to load projects_data.json (File not found)");
            System.out.println("-> Starting with empty catalog");
        }
    }
}
