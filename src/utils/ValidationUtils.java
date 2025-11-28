package utils;

public class ValidationUtils {

    public static boolean isValidProjectType(String projectType) {
        return projectType != null && ("Software".equals(projectType) || "Hardware".equals(projectType));
    }

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

