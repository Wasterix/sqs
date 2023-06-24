package project.UnitTests;

import org.junit.Assert;
import org.junit.Test;
import project.LookupTable;

public class LookupTableTestFinal {

    @Test
    public void testContainsTeam() {
        // Testfall: Team vorhanden
        String teamName = "Boston Celtics";
        boolean result = LookupTable.containsTeam(teamName);
        Assert.assertTrue(result);

        // Testfall: Team nicht vorhanden
        teamName = "Rosenheim";
        result = LookupTable.containsTeam(teamName);
        Assert.assertFalse(result);
    }

    @Test
    public void testGetTeamId() {
        // Testfall: Team vorhanden
        String teamName = "Boston Celtics";
        String expectedId = "2";
        String actualId = LookupTable.getTeamId(teamName);
        Assert.assertEquals(expectedId, actualId);

        // Testfall: Team nicht vorhanden
        teamName = "Rosenheim";
        String expectedNull = "";
        actualId = LookupTable.getTeamId(teamName);
        Assert.assertEquals(expectedNull, actualId);
    }


}
