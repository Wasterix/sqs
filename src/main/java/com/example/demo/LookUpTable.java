package com.example.demo;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class LookUpTable {
    public static void main(String[] args) {
        // Erstelle eine Map, um die ID den Städten zuzuordnen
        Map<Integer, String> lookupTable = new HashMap<>();

        // Füge die Zuordnungen hinzu
        lookupTable.put(1, "Atlanta Hawks");
        lookupTable.put(2, "Boston Celtics");
        lookupTable.put(3, "Brooklyn Nets");
        lookupTable.put(4, "Charlotte Hornets");
        lookupTable.put(5, "Chicago Bulls");
        lookupTable.put(6, "Cleveland Cavaliers");
        lookupTable.put(7, "Dallas Mavericks");
        lookupTable.put(8, "Denver Nuggets");
        lookupTable.put(9, "Detroit Pistons");
        lookupTable.put(10, "Golden State Warriors");
        lookupTable.put(11, "Houston Rockets");
        lookupTable.put(12, "Indiana Pacers");
        lookupTable.put(13, "LA Clippers");
        lookupTable.put(14, "Los Angeles Lakers");
        lookupTable.put(15, "Memphis Grizzlies");
        lookupTable.put(16, "Miami Heat");
        lookupTable.put(17, "Milwaukee Bucks");
        lookupTable.put(18, "Minnesota Timberwolves");
        lookupTable.put(19, "New Orleans Pelicans");
        lookupTable.put(20, "New York Knicks");
        lookupTable.put(21, "Oklahoma City Thunder");
        lookupTable.put(22, "Orlando Magic");
        lookupTable.put(23, "Philadelphia 76ers");
        lookupTable.put(24, "Phoenix Suns");
        lookupTable.put(25, "Portland Trail Blazers");
        lookupTable.put(26, "Sacramento Kings");
        lookupTable.put(27, "San Antonio Spurs");
        lookupTable.put(28, "Toronto Raptors");
        lookupTable.put(29, "Utah Jazz");
        lookupTable.put(30, "Washington Wizards");

        // Erstelle die umgekehrte Lookup-Tabelle
        Map<String, Integer> reverseLookupTable = new HashMap<>();
        for (Map.Entry<Integer, String> entry : lookupTable.entrySet()) {
            Integer key = entry.getKey();
            String value = entry.getValue();
            reverseLookupTable.put(value, key);
        }

        // Erstelle einen Scanner, um die Konsoleneingabe zu lesen
        Scanner scanner = new Scanner(System.in);
        // Fordere den Benutzer auf, eine Eingabe einzugeben
        System.out.print("Gib einen vollständigen Teamnamen an: ");
        // Lese die Eingabe des Benutzers
        String input_name = scanner.nextLine();
        // Schließe den Scanner, um Ressourcen freizugeben
        scanner.close();


        // Beispielabfrage: Schlüssel für den Wert "Los Angeles Lakers" abrufen
        //String gesuchterWert = "Los Angeles Lakers";
        Integer id = reverseLookupTable.get(input_name);

        System.out.println("Die Id der " + input_name + " lautet: "+ id);
        }
    }

