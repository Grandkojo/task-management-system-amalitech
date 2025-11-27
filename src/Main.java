import models.HardwareProject;
import models.SoftwareProject;

public class Main {
    public static void main(String[] args) {
        System.out.println("\nWelcome to my task manager\n");
        
        SoftwareProject alphaTracker = new SoftwareProject("Alpha Tracker", "Track my tasks for this project", 15000, 5);
        HardwareProject temperatureRecorder = new HardwareProject("Temperature Recorder", "Record the temperature of the day at 1 pm", 10000, 3);
        
        alphaTracker.displayProject();
        alphaTracker.getProjectDetails();

        temperatureRecorder.displayProject();
        temperatureRecorder.getProjectDetails();

    }
}
