package utils;

public class ValidationUtils {

    // Validate allowed project types for user input
    public static boolean isValidProjectType(String projectType) {
        return projectType != null && ("Software".equals(projectType) || "Hardware".equals(projectType));
    }

    // Validate that a budget range is positive and logically correct
    public static boolean isValidBudgetRange(long minAmount, long maxAmount) {
        if (minAmount <= 0 || maxAmount <= 0) {
            System.out.println("Both minimum and maximum amounts needed");
            return false;
        }

        if (minAmount > maxAmount) {
            System.out.println("Minimum amount cannot be greater than maximum amount");
            return false;
        }

        return true;
    }
}

