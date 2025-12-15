package services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import main.java.com.google.gson.typeadapters.RuntimeTypeAdapterFactory;
import models.HardwareProject;
import models.Project;
import models.SoftwareProject;

public class GsonProvider {

    private static final Gson gson;

    static {
        RuntimeTypeAdapterFactory<Project> projectAdapter =
            RuntimeTypeAdapterFactory
                .of(Project.class, "_type")
                .registerSubtype(SoftwareProject.class, "Software")
                .registerSubtype(HardwareProject.class, "Hardware");

        gson = new GsonBuilder()
                .registerTypeAdapterFactory(projectAdapter)
                .setPrettyPrinting()
                .create();
    }

    public static Gson getGson() {
        return gson;
    }
}

