package test;


import org.junit.jupiter.api.Test;
import utils.ValidationUtils;
import utils.exceptions.InvalidBudgetRangeException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class ValidationTests {
    
    @Test
    public void GreaterThanZeroBudgetRangeTest(){
        Exception ex = assertThrows(InvalidBudgetRangeException.class, () -> {
            ValidationUtils.isValidBudgetRange(0, 0);
        });

        assertEquals("Both minimum and maximum amounts must be positive numbers", ex.getMessage());
    }
    
    @Test
    public void NotNegativeaBudgetRangeTest(){
        Exception ex = assertThrows(InvalidBudgetRangeException.class, () -> {
            ValidationUtils.isValidBudgetRange(-1, 0);
        });

        assertEquals("Both minimum and maximum amounts must be positive numbers", ex.getMessage());
    }

    @Test
    public void validBudgetRange() {
        Exception ex = assertThrows(InvalidBudgetRangeException.class, () -> {
            ValidationUtils.isValidBudgetRange(5000, 100);
        });

        assertEquals("Minimum amount cannot be greater than maximum amount", ex.getMessage());

    }
}
