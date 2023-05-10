package fr.uwu;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class App {
    public static void main(String[] args) throws Exception {

        // ? Test pour voir si les classes marchent
        // Station stationtest = new Station(1, "2", 3, "Test");
        // System.out.println(stationtest);
        // Relation rel = new Relation(1, 2, 23);
        // System.out.println(rel);

        // ? Parcours du CSV Relations

        // Catalogue Station
        String ligne = " ";
        List<Station> catalogueStation = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader("CSV/stations.csv"))) {
            br.readLine(); // Skip noms de colonnes

            while ((ligne = br.readLine()) != null) {
                String[] lignesplit = ligne.split(";");
                catalogueStation.add(
                        new Station(
                                Integer.parseInt(lignesplit[0]),
                                lignesplit[1],
                                Integer.parseInt(lignesplit[2]),
                                lignesplit[3]));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        // Catalogue Relation
        ligne = " ";
        List<Relation> catalogueRelation = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader("CSV/relations.csv"))) {
            br.readLine(); // Skip noms de colonnes

            while ((ligne = br.readLine()) != null) {
                String[] lignesplit = ligne.split(";");
                catalogueRelation.add(
                        new Relation(
                                Integer.parseInt(lignesplit[0]),
                                Integer.parseInt(lignesplit[1]),
                                Integer.parseInt(lignesplit[2])));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
