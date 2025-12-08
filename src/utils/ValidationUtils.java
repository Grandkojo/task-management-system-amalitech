package utils;

import utils.exceptions.InvalidBudgetRangeException;

public class ValidationUtils {

    // Validate allowed project types for user input
    public static boolean isValidProjectType(String projectType) {
        return projectType != null && ("Software".equals(projectType) || "Hardware".equals(projectType));
    }

    // Validate that a budget range is positive and logically correct
    public static boolean isValidBudgetRange(long minAmount, long maxAmount) throws InvalidBudgetRangeException {
        if (minAmount <= 0 || maxAmount <= 0) {
            throw new InvalidBudgetRangeException("Both minimum and maximum amounts must be positive numbers");
        }

        if (minAmount > maxAmount) {
            throw new InvalidBudgetRangeException("Minimum amount cannot be greater than maximum amount");
        }

        return true;
    }
}

