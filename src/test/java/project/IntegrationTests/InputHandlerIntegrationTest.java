package project.IntegrationTests;

import org.junit.jupiter.api.Test;
import project.InputHandler;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class InputHandlerIntegrationTest {
    private static final String VALID_TEAM_NAME = "Los Angeles Lakers";
    private static final String VALID_SEASON = "2021";
    private static final String VALID_INPUT = VALID_TEAM_NAME + System.lineSeparator() + VALID_SEASON;

    @Test
    public void testGetInputNameAndSeason_ValidInput_ReturnsInputNameAndSeason() {
        provideInput(VALID_INPUT);

        InputHandler inputHandler = new InputHandler();

        String inputName = inputHandler.getInputName();
        String inputSeason = inputHandler.getInputSeason();

        assertEquals(VALID_TEAM_NAME, inputName);
        assertEquals(VALID_SEASON, inputSeason);
    }

    private void provideInput(String input) {
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
    }
}
