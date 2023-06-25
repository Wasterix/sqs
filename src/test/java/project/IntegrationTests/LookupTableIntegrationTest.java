package project.IntegrationTests;

import org.junit.jupiter.api.Test;
import project.LookupTable;

import static org.junit.jupiter.api.Assertions.*;

public class LookupTableIntegrationTest {
    @Test
    public void testContainsTeam_ExistingTeam_ReturnsTrue() {
        String teamName = "Los Angeles Lakers";

        boolean result = LookupTable.containsTeam(teamName);

        assertTrue(result);
    }

    @Test
    public void testContainsTeam_NonExistingTeam_ReturnsFalse() {
        String teamName = "Rosenheim";

        boolean result = LookupTable.containsTeam(teamName);

        assertFalse(result);
    }

    @Test
    public void testGetTeamId_ExistingTeam_ReturnsTeamId() {
        String teamName = "Brooklyn Nets";
        String expectedTeamId = "3";

        String teamId = LookupTable.getTeamId(teamName);

        assertEquals(expectedTeamId, teamId);
    }

    @Test
    public void testGetTeamId_NonExistingTeam_ReturnsEmptyString() {
        String teamName = "Rosenheim";
        String expectedTeamId = "";

        String teamId = LookupTable.getTeamId(teamName);

        assertEquals(expectedTeamId, teamId);
    }
}
