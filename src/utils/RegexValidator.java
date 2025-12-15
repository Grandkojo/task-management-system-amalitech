package utils;

import java.util.function.Predicate;
import java.util.regex.Pattern;

public class RegexValidator {
    
    public static final Predicate<String> VALID_TASK_ID = id -> Pattern.matches("TSK\\d{3}", id.trim().toUpperCase());

    public static final Predicate<String> VALID_PROJECT_ID = id -> Pattern.matches("PRJ\\d{3}", id.trim().toUpperCase());


}
