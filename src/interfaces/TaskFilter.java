package interfaces;

import models.Task;

/**
 * Functional interface used for filtering tasks with lambdas or method references.
 */
@FunctionalInterface
public interface TaskFilter {

    /**
     * Evaluate whether the given task matches some condition.
     * @param task task to test
     * @return true if condition matches, false otherwise
     */
    boolean test(Task task);
}


