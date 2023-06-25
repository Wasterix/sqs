package outdated;

import com.sun.net.httpserver.HttpServer;
import project.GamesHandler;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class NBASeasonViewer {
    private static Map<String, Integer> lookupTable;
    private String teamId;
    private String inputSeason;
    private String year;
    private String inputName;

    public NBASeasonViewer() {
        initializeLookupTable();
    }

    public void run() {
        getInput();
        processInput();
        startHttpServer();
    }

    public void getInput() {
        Scanner scanner = new Scanner(System.in);

        do {
            System.out.print("Gib mir einen vollständigen Teamnamen: ");
            inputName = scanner.nextLine();

            if (!lookupTable.containsKey(inputName)) {
                System.out.println("Ungültiger Teamname. Bitte gib den vollständigen Namen eines aktuellen Teams der NBA ein.");
            }
        } while (!lookupTable.containsKey(inputName));

        teamId = lookupTable.get(inputName).toString();

        int minYear = 1945;
        int maxYear = 2022;

        boolean isValidInput = false;

        do {
            System.out.print("Welche Saison soll angezeigt werden: ");
            inputSeason = scanner.nextLine();

            try {
                int inputYear = Integer.parseInt(inputSeason);
                if (inputYear >= minYear && inputYear <= maxYear) {
                    isValidInput = true;
                } else {
                    System.out.println("Bitte gib ein Jahr zwischen " + minYear + " und " + maxYear + " ein.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Ungültige Eingabe. Bitte geben Sie eine Zahl zwischen " + minYear + " und " + maxYear + " ein.");
            }
        } while (!isValidInput);

        int number = Integer.parseInt(inputSeason);
        year = String.valueOf(number + 1);

        scanner.close();
    }

    private void processInput() {
        System.out.println("Die Id der " + inputName + " lautet: " + teamId);
        System.out.println("Ausgewählte Saison: " + inputSeason + "/" + year);
    }

    private void startHttpServer() {
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
            server.createContext("/id=" + teamId + "&season=" + inputSeason + "/" + year, new GamesHandler(teamId, inputSeason, inputName));
            server.start();
            System.out.println("HTTP-Server läuft auf http://localhost:8000/id=" + teamId + "&season=" + inputSeason + "/" + year);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initializeLookupTable() {
        lookupTable = new HashMap<>();
        lookupTable.put("Atlanta Hawks", 1);
        lookupTable.put("Boston Celtics", 2);
        lookupTable.put("Brooklyn Nets", 3);
        lookupTable.put("Charlotte Hornets", 4);
        lookupTable.put("Chicago Bulls", 5);
        lookupTable.put("Cleveland Cavaliers", 6);
        lookupTable.put("Dallas Mavericks", 7);
        lookupTable.put("Denver Nuggets", 8);
        lookupTable.put("Detroit Pistons", 9);
        lookupTable.put("Golden State Warriors", 10);
        lookupTable.put("Houston Rockets", 11);
        lookupTable.put("Indiana Pacers", 12);
        lookupTable.put("LA Clippers", 13);
        lookupTable.put("Los Angeles Lakers", 14);
        lookupTable.put("Memphis Grizzlies", 15);
        lookupTable.put("Miami Heat", 16);
        lookupTable.put("Milwaukee Bucks", 17);
        lookupTable.put("Minnesota Timberwolves", 18);
        lookupTable.put("New Orleans Pelicans", 19);
        lookupTable.put("New York Knicks", 20);
        lookupTable.put("Oklahoma City Thunder", 21);
        lookupTable.put("Orlando Magic", 22);
        lookupTable.put("Philadelphia 76ers", 23);
        lookupTable.put("Phoenix Suns", 24);
        lookupTable.put("Portland Trail Blazers", 25);
        lookupTable.put("Sacramento Kings", 26);
        lookupTable.put("San Antonio Spurs", 27);
        lookupTable.put("Toronto Raptors", 28);
        lookupTable.put("Utah Jazz", 29);
        lookupTable.put("Washington Wizards", 30);
    }
}
