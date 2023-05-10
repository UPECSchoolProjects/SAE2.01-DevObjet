package fr.uwu;

import java.util.List;

import fr.uwu.utils.CSVUtils;

public class App {
    public static void main(String[] args) throws Exception {

        // ? Test pour voir si les classes marchent
        // Station stationtest = new Station(1, "2", 3, "Test");
        // System.out.println(stationtest);
        // Relation rel = new Relation(1, 2, 23);
        // System.out.println(rel);

        // ? Parcours du CSV Relations

        List<Station> station = CSVUtils.readStationCSV(null);

        for (int i = 10; i < 26; i++) {
            System.out.println(station.get(i));
        }

        
        System.out.println("Nombre de stations: " + station.size());
        System.out.println(station.get(24));
    }
}
