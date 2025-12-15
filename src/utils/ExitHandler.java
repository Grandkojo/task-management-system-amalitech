package utils;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Ensures the exit message is printed at most once across the app.
 */
public final class ExitHandler {

    private static final AtomicBoolean printed = new AtomicBoolean(false);

    private ExitHandler() {}

    public static void printOnce() {
        
        if (printed.compareAndSet(false, true)) {
            FileUtils.saveProjects();
            System.out.println("\nThank you using Project Management today!!");
        }
    }
}


