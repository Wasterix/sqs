package project;

import com.sun.net.httpserver.HttpServer;
import project.GamesHandler;
import project.InputHandler;
import project.LookupTable;

import java.io.IOException;
import java.net.InetSocketAddress;

public class NBAViewerMain {
    public static void main(String[] args) {
        InputHandler inputHandler = new InputHandler();

        String inputName = inputHandler.getInputName();
        String inputSeason = inputHandler.getInputSeason();

        String teamId = LookupTable.getTeamId(inputName);
        String year = calculateYear(inputSeason);

        processInput(inputName, teamId, inputSeason, year);
        startHttpServer(teamId, inputSeason, year, inputName);

    }

    private static String calculateYear(String inputSeason) {
        int number = Integer.parseInt(inputSeason);
        return String.valueOf(number + 1);
    }

    private static void processInput(String inputName, String teamId, String inputSeason, String year) {
        System.out.println("Die Id der " + inputName + " lautet: " + teamId);
        System.out.println("Ausgewählte Saison: " + inputSeason + "/" + year);
    }

    private static void startHttpServer(String teamId, String inputSeason, String year, String inputName) {
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
            server.createContext("/id=" + teamId + "&season=" + inputSeason + "/" + year, new GamesHandler(teamId, inputSeason, inputName));
            server.start();
            System.out.println("HTTP-Server läuft auf http://localhost:8000/id=" + teamId + "&season=" + inputSeason + "/" + year);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
