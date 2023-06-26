package project;

import java.util.Scanner;

public class InputHandler {
    private Scanner scanner;

    public InputHandler() {
        scanner = new Scanner(System.in);
    }

    public String getInputName() {
        String inputName;

        do {
            System.out.print("Gib mir einen vollständigen Teamnamen: ");
            inputName = scanner.nextLine();

            if (!LookupTable.containsTeam(inputName)) {
                System.out.println("Ungültiger Teamname. Bitte gib den vollständigen Namen eines aktuellen Teams der NBA ein.");
            }
        } while (!LookupTable.containsTeam(inputName));

        return inputName;
    }

    public String getInputSeason() {
        int minYear = 1949;
        int maxYear = 2022;

        boolean isValidInput = false;
        String inputSeason = null;

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

        return inputSeason;
    }
}