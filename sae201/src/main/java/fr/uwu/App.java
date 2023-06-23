package fr.uwu;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import fr.uwu.ReseauMetro.TypeAnalyse;
import fr.uwu.utils.CSVUtils;

public class App {
    public static void main(String[] args) throws Exception {

        // ? Test pour voir si les classes marchent
        // Station stationtest = new Station(1, "2", 3, "Test");
        // System.out.println(stationtest);
        // Relation rel = new Relation(1, 2, 23);
        // System.out.println(rel);

        // ? Parcours du CSV Relations

        List<Quai> station = CSVUtils.readStationCSV(null);
        List<Relation> relations = CSVUtils.readRelationCSV(null, station);

        System.out.println("Nombre de relations: " + relations.size());

        ReseauMetro reseau = new ReseauMetro(new ArrayList<Quai>(station), new ArrayList<Relation>(relations));

        // Boucle pour afficher les relations de chaque station 
        for (Quai s : reseau.quais.subList(0, 10)) {
            List<Relation> rel = s.getRelations(relations);
            StringBuilder sb = new StringBuilder("----- (" + s + ") -> -----  \n");

            int i = 1;

            for (Relation r : rel) {
                sb.append("Relation " + i++ + ": \n");
                sb.append(r + " \n");
            }

            System.out.println(sb.toString() + "\n");
        }

        System.out.println("Nombre de stations: " + station.size());
        System.out.println(station.get(24));

        StringBuffer sb = new StringBuffer();

        // Boucle pour afficher les stations de chaque station
        for (Quai s : reseau.stations.keySet()) {
            sb.append(s + "\n");
        }

        Quai abes = Quai.getQuaiById(new ArrayList<Quai>(reseau.stations.keySet()), "V43");

        sb.append("-- " + abes + " --\n");

        // Boucle pour afficher les stations de chaque station de la station abes
        for (Quai s : reseau.stations.get(abes)) {
            sb.append(s + "\n");
        }

        System.out.println(sb.toString());

        // ? Test de l'algo de Dijkstra
        Quai CDGEtoile = Quai.getQuaiById(new ArrayList<Quai>(reseau.stations.keySet()), "V9");

        Quai Nation = Quai.getQuaiById(new ArrayList<Quai>(reseau.stations.keySet()), "V61");
        //Quai Opera = Quai.getQuaiById(new ArrayList<Quai>(reseau.stations.keySet()), "V69");

        List<Relation> chemin = reseau.dijkstra_algo(CDGEtoile, Nation);

        List<Quai> cheminQuai = ReseauMetro.convertRelationPathToStationPath(chemin, CDGEtoile, Nation);

        System.out.println("Chemin de " + CDGEtoile + " à " + Nation + " : \n");

        for (Quai s : cheminQuai) {
            System.out.println(s);
        }

        System.out.println("Nombre de stations: " + cheminQuai.size());

        // print sous forme de lsite d'id sans le Q
        for (Quai s : cheminQuai) {
            if (s.getId().charAt(0) == 'Q') {
                System.out.print(s.getId().substring(1));
                System.out.print(",");
            }
        }

        System.out.println("\n");
        System.out.println(reseau.relations.size());

        System.out.println("\n");
        System.out.println(reseau.ACM().size());

        List<Quai> stations_virt = reseau.stations.keySet().stream().filter(s -> s.virtuel).collect(Collectors.toList());

        System.out.println(stations_virt.size());

        reseau.comparerStation_A_B(Nation, CDGEtoile, 200, TypeAnalyse.TERMINALE);
    }
}
