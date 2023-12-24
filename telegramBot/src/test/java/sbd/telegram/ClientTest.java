package sbd.telegram;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sbd.telegram.database.Client;
import sbd.telegram.database.InputState;

import java.io.Serializable;
import java.util.ArrayList;

public class ClientTest {
    private Client client;

    @BeforeEach
    public void setUp() {
        client = new Client();
    }

    @Test
    public void testValidInputStringParser() {
        Serializable result = client.inputStringParser("Сві Сві Сві Mail@ +3");
        Assertions.assertTrue(result instanceof ArrayList);
        ArrayList<?> inputs = (ArrayList<?>) result;
        Assertions.assertFalse(inputs.isEmpty());
    }

    @Test
    public void testInvalidInputStringParser() {
        Serializable result = client.inputStringParser("Invalid input");
        Assertions.assertTrue(result instanceof ArrayList);
        ArrayList<InputState> inputs = (ArrayList<InputState>) result;
        Assertions.assertFalse(inputs.isEmpty());
    }

    // Add more tests for different scenarios, edge cases, and invalid inputs
}
