package fr.uwu;

import fr.uwu.Couleurs;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import fr.uwu.ReseauMetro.TypeAnalyse;

public class App {
    public static void main(String[] args) throws Exception {

        // ? Test pour voir si les classes marchent
        // Station stationtest = new Station(1, "2", 3, "Test");
        // System.out.println(stationtest);
        // Relation rel = new Relation(1, 2, 23);
        // System.out.println(rel);

        // ? Parcours du CSV Relations

        // List<Quai> station = CSVUtils.readStationCSV(null);
        // List<Relation> relations = CSVUtils.readRelationCSV(null, station);

        // Clear la console
        System.out.print("\033[H\033[2J");
        System.out.flush();

        DbConnector db = DbConnector.getDbFromResourceFile();
        List<Quai> station = db.get_stations();
        List<Relation> relations = db.get_relations(station);

        // ? Affichage des informations
        System.out.println("");
        System.out.println(Couleurs.BG_RED + "- - - Informations - - -" + Couleurs.RESET);
        System.out.println("");

        System.out.println(Couleurs.UNDERLINE + "Nombre de relations:" + Couleurs.RESET + " " + relations.size());
        System.out.println(Couleurs.UNDERLINE + "Nombre total de stations:" + Couleurs.RESET + " " + station.size());

        // ? Affichage des relations des deux premiers quais
        System.out.println("");
        System.out
                .println(Couleurs.BG_RED + "- - - Affichage des relations des deux premiers quais - - -"
                        + Couleurs.RESET);
        System.out.println("");

        ReseauMetro reseau = new ReseauMetro(new ArrayList<Quai>(station), new ArrayList<Relation>(relations));

        // Boucle pour afficher les relations de chaque station
        for (Quai s : reseau.quais.subList(0, 2)) {
            List<Relation> rel = s.getRelations(relations);
            StringBuilder sb = new StringBuilder("----- (" + s + ") -> -----  \n");

            int i = 1;

            for (Relation r : rel) {
                sb.append(Couleurs.UNDERLINE + "Relation " + i++ + ":" + Couleurs.RESET +
                        "\n");
                sb.append(r + " \n");
            }

            System.out.println(sb.toString() + "\n");
        }

        System.out.println(Couleurs.UNDERLINE + "Info sur le quai 24:" + Couleurs.RESET);
        System.out.println(station.get(24));

        StringBuffer sb = new StringBuffer();

        // Boucle pour afficher les stations de chaque station
        for (Quai s : reseau.stations.keySet()) {
            sb.append(s + "\n");
        }

        // ? Afficher les stations virtuelles (correspondances)
        System.out.println("");
        System.out.println(Couleurs.BG_RED + "- - - Station virtuelles (correspondances) - - -" + Couleurs.RESET);
        System.out.println("");

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
        // Quai Opera = Quai.getQuaiById(new ArrayList<Quai>(reseau.stations.keySet()),
        // "V69");

        List<Relation> chemin = reseau.dijkstra_algo(CDGEtoile, Nation);

        List<Quai> cheminQuai = ReseauMetro.convertRelationPathToStationPath(chemin, CDGEtoile, Nation);

        System.out.println("");
        System.out.println(Couleurs.BG_RED + "- - - Chemin de CDGEtoile à Nation - - -" + Couleurs.RESET);
        System.out.println("");

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

        // ? Afficher les éléments du réseau métropolitain à la demande de l’utilisateur
        System.out.println("");
        System.out.println(Couleurs.BG_RED
                + "- - - Afficher les éléments du réseau métropolitain à la demande de l'utilisateur : - - -"
                + Couleurs.RESET);
        System.out.println("");

        // ^ 1. La liste des stations d’une ligne donnée.

        System.out.println(Couleurs.UNDERLINE + "Liste des stations de la ligne M3:" + Couleurs.RESET + "\n");
        reseau.printStationLigne("M3");

        // ^ 2. La ou les correspondances possibles entre deux lignes.

        System.out.println("\n" + Couleurs.UNDERLINE + "Correspondances entre M7 et M4:" + Couleurs.RESET + "\n");
        reseau.correspondanceEntre2Lignes("M7", "M4");

        System.out.println("\n" + Couleurs.UNDERLINE + "Correspondances entre M6 et M7:" + Couleurs.RESET + "\n");
        reseau.correspondanceEntre2Lignes("M6", "M7");

        // ^ 3. Temps estimé trajet entre deux stations.

        System.out.println(
                "\n" + Couleurs.UNDERLINE + "Temps estimé du trajet entre CDGEtoile et Nation (trajet précédent):"
                        + Couleurs.RESET + " " + ReseauMetro.calculertemps(chemin) + " secondes");

        // ? ACM
        System.out.println("\n\n" + Couleurs.BG_RED + "- - - ACM - - -" + Couleurs.RESET);

        System.out
                .println("\n" + Couleurs.UNDERLINE + "Nombre de relations totale du réseau:" + Couleurs.RESET
                        + " " + reseau.relations.size());

        System.out.println("" + Couleurs.UNDERLINE + "Nombre de relations trouvées par l'ACM:" + Couleurs.RESET
                + " " + reseau.ACM().size());

        List<Quai> stations_virt = reseau.stations.keySet().stream().filter(s -> s.virtuel)
                .collect(Collectors.toList());

        System.out.println(Couleurs.UNDERLINE + "Nombre de stations virtuelles trouvées:" + Couleurs.RESET
                + " " + stations_virt.size());

        // ? Comparaison de deux stations (Méthode ACCESSIBLE)
        // System.out.println("");
        // System.out.println(
        // Couleurs.BG_RED + "- - - Comparaison Nation et CDGEtoile (Méthode ACCESSIBLE)
        // - - -" + Couleurs.RESET);

        // reseau.comparerStation_A_B(Nation, CDGEtoile, 200, TypeAnalyse.ACCESSIBLE);

        // ? Comparaison de deux stations (Méthode CENTRALE)
        // System.out.println("");
        // System.out.println(
        // Couleurs.BG_RED + "- - - Comparaison Nation et CDGEtoile (Méthode CENTRALE) -
        // - -" + Couleurs.RESET);

        // reseau.comparerStation_A_B(Nation, CDGEtoile, 200, TypeAnalyse.CENTRALE);

        // ? Comparaison de deux stations (Méthode TERMINALE)
        System.out.println("");
        System.out.println(Couleurs.BG_RED + "- - - Comparaison Nation et CDGEtoile (Méthode TERMINALE) - - -"
                + Couleurs.RESET);

        reseau.comparerStation_A_B(Nation, CDGEtoile, 200, TypeAnalyse.TERMINALE);
    }
}
