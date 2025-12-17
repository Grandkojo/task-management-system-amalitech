package utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import models.Project;
import models.SoftwareProject;
import models.Task;
import models.Task.Status;
import org.junit.jupiter.api.Test;
import services.ProjectService;
import services.TaskService;
import services.GsonProvider;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FileUtilsTests {

    private static final Path DATA_PATH = Path.of("data/projects_data.json");

    private void clearInMemoryStores() throws Exception {
        // Clear ProjectService.allProjects
        Field projectsField = ProjectService.class.getDeclaredField("allProjects");
        projectsField.setAccessible(true);
        ((java.util.Map<?, ?>) projectsField.get(null)).clear();

        // Clear TaskService.tasks
        Field tasksField = TaskService.class.getDeclaredField("tasks");
        tasksField.setAccessible(true);
        ((ArrayList<?>) tasksField.get(null)).clear();
    }

    @Test
    void saveProjects_writesNonEmptyJsonFile() throws Exception {
        clearInMemoryStores();

        Project p = new SoftwareProject("Test Project", "For FileUtils save", 1000, 3);
        ProjectService.addProjectToStorage(p);
        Task t = Task.create("Initial Task", Status.PENDING, p.getId());
        TaskService.addTaskToStorage(t);

        FileUtils.saveProjects();

        assertTrue(Files.exists(DATA_PATH));
        String json = Files.readString(DATA_PATH);
        assertFalse(json.isBlank());
        assertTrue(json.contains("Test Project"));
        assertTrue(json.contains("Initial Task"));
    }

    @Test
    void loadProjects_whenFileMissing_doesNotThrow() throws Exception {
        // Temporarily rename the data file if it exists
        Path backup = null;
        if (Files.exists(DATA_PATH)) {
            backup = Path.of("projects_data_backup.json");
            Files.move(DATA_PATH, backup, StandardCopyOption.REPLACE_EXISTING);
        }

        try {
            clearInMemoryStores();
            assertDoesNotThrow(FileUtils::loadProjects);
        } finally {
            if (backup != null && Files.exists(backup)) {
                Files.move(backup, DATA_PATH, StandardCopyOption.REPLACE_EXISTING);
            }
        }
    }

    @Test
    void loadProjects_populatesServicesFromSavedFile() throws Exception {
        clearInMemoryStores();

        // Seed and save
        Project p = new SoftwareProject("Persisted Project", "To be reloaded", 2000, 4);
        ProjectService.addProjectToStorage(p);
        Task t = Task.create("Persisted Task", Status.COMPLETED, p.getId());
        TaskService.addTaskToStorage(t);
        FileUtils.saveProjects();

        // Clear in-memory and then load from file
        clearInMemoryStores();
        FileUtils.loadProjects();

        // Verify that at least one project and its task came back
        Gson gson = GsonProvider.getGson();
        String json = Files.readString(DATA_PATH);
        Type type = new TypeToken<Collection<Project>>() {}.getType();
        Collection<Project> projects = gson.fromJson(json, type);

        assertFalse(projects.isEmpty());
        assertTrue(projects.stream().anyMatch(pr -> "Persisted Project".equals(pr.getName())));
    }
}
