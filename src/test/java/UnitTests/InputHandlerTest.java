/*package UnitTests;

import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import project.InputHandler;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class InputHandlerTest {
    private InputStream standardIn;
    private Scanner scanner;

    @Before
    public void setUp() {
        standardIn = System.in;
        scanner = new Scanner(System.in);
    }

    @After
    public void tearDown() {
        System.setIn(standardIn);
        scanner.close();
    }

    @Test
    public void testGetInputName_ValidInput() {
        // Vorbereitung: Simuliere Benutzereingabe "Los Angeles Lakers"
        String input = "Los Angeles Lakers\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        // Ausführung: Rufe getInputName auf
        InputHandler inputHandler = new InputHandler();
        String result = inputHandler.getInputName();

        // Überprüfung: Das Ergebnis sollte "Los Angeles Lakers" sein
        assertEquals("Los Angeles Lakers", result);
    }

    @Test
    public void testGetInputSeason_ValidInput() {
        // Vorbereitung: Simuliere Benutzereingabe "2010"
        String input = "2010\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        // Ausführung: Rufe getInputSeason auf
        InputHandler inputHandler = new InputHandler();
        String result = inputHandler.getInputSeason();

        // Überprüfung: Das Ergebnis sollte "2010" sein
        assertEquals("2010", result);
    }
}
*/