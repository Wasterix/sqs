package project.UnitTests;

import org.junit.jupiter.api.Test;
import project.LookupTable;

import static org.junit.jupiter.api.Assertions.*;

class LookupTableTest {

    @Test
    void containsTeam_existingTeam_returnTrue() {
        assertTrue(LookupTable.containsTeam("Golden State Warriors"));
    }

    @Test
    void containsTeam_nonExistingTeam_returnFalse() {
        assertFalse(LookupTable.containsTeam("Raubling"));
    }

    @Test
    void getTeamId_existingTeam_returnIdString() {
        String expectedId = "14";
        String actualId = LookupTable.getTeamId("Los Angeles Lakers");
        assertEquals(expectedId, actualId);
    }

    @Test
    void getTeamId_nonExistingTeam_returnEmptyString() {
        String expectedId = "";
        String actualId = LookupTable.getTeamId("Rosenheim Hochschülers");
        assertEquals(expectedId, actualId);
    }
}
