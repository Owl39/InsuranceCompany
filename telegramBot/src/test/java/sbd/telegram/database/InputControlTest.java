package sbd.telegram.database;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sbd.telegram.database.InputControl;
import sbd.telegram.database.InputState;

import java.io.Serializable;
import java.util.ArrayList;

public class InputControlTest {
    private InputControl inputControl;

    @BeforeEach
    public void setUp() {
        inputControl = new InputControl();
    }

    @Test
    public void testValidInputStringParser() {
        Serializable result = inputControl.inputStringParser("Сві Сві Сві Mail@ +3");
        Assertions.assertTrue(result instanceof ArrayList);
        ArrayList<?> inputs = (ArrayList<?>) result;
        Assertions.assertFalse(inputs.isEmpty());
    }

    @Test
    public void testInvalidInputStringParser() {
        Serializable result = inputControl.inputStringParser("Invalid input");
        Assertions.assertTrue(result instanceof ArrayList);
        ArrayList<InputState> inputs = (ArrayList<InputState>) result;
        Assertions.assertFalse(inputs.isEmpty());
    }
}
