package fr.uwu;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Comparator;
/**
 * Classe principale du Graphe. ELle gère les stations (noeud) et les relations entre les stations
 * (arête).
 */
import java.util.Set;
import java.util.stream.Collectors;

public class ReseauMetro {


    public static List<Quai> convertRelationPathToStationPath(List<Relation> relations, Quai start,
            Quai end) {
        List<Quai> stations = new ArrayList<Quai>();
        Quai current = end;

        stations.add(end);

        for (Relation r : relations) {
            // graphe non orienté, donc on peut avoir st1 ou st2 en premier
            if (r.getSt1().compareTo(current)) {
                stations.add(r.getSt2());
                current = r.getSt2();
            } else if (r.getSt2().compareTo(current)) {
                stations.add(r.getSt1());
                current = r.getSt1();
            } else {
                System.out.println(
                        "Erreur : Relation " + r + " ne contient pas la station " + current);
            }
        }

        return stations;
    }

    // #region Attributs
    ArrayList<Quai> quais;
    ArrayList<Relation> relations;
    Map<Quai, Set<Quai>> stations; // Quai est une station virtuelle qui regroupe toutes les
                                   // stations
                                   // ayant le même nom (en correspondance)
    Set<Relation> relTraites; // qui déja traité dans la fonction relierStationMemeNom
                              // (optimisation)
    // #endregion

    // #region Constructeurs

    /**
     * Constructeur par défaut. Initialise les listes de stations et de relations (vides).
     */
    public ReseauMetro() {
        this.quais = new ArrayList<Quai>();
        this.relations = new ArrayList<Relation>();
        init();
    }

    /**
     * Constructeur avec paramètres. Initialise les listes de stations et de relations avec les
     * paramètres.
     * 
     * @param stations ArrayList de stations
     * @param relations ArrayList de relations
     */
    public ReseauMetro(ArrayList<Quai> stations, ArrayList<Relation> relations) {
        this.quais = stations;
        this.relations = relations;
        init();
    }

    public void init() {
        this.stations = new HashMap<Quai, Set<Quai>>();
        this.relTraites = new HashSet<Relation>();
        this.relierStationCorresp();
    }

    // #endregion

    // #region Méthodes

    /**
     * Ajoute une station (noeud) au graphe.
     * 
     * @param station Objet Station à ajouter
     */
    public void addStation(Quai station) {
        this.quais.add(station);
        this.relierStationCorresp();
    }

    /**
     * Ajoute une relation (arête) au graphe.
     * 
     * @param relation Objet Relation à ajouter
     */
    public void addRelation(Relation relation) {
        this.relations.add(relation);
    }

    /**
     * Relie les relations avec correspondance = true à un sommet virtuel qui regroupe tout les
     * quais d'une même station
     */
    public void relierStationCorresp() {
        for (Relation rel : this.relations) {
            if (this.relTraites.contains(rel) || !rel.correspondance) {
                continue;
            }

            Quai st1 = rel.getSt1();
            Quai st2 = rel.getSt2();

            Quai stVirt = new Quai(st1.terminus, st1.nom);

            boolean alreadyExists = false;
            for (Quai st : this.stations.keySet()) {
                if (st.nom.equals(stVirt.nom)) {
                    alreadyExists = true;
                    stVirt = st;
                    break;
                }
            }

            if (!alreadyExists) {
                this.stations.put(stVirt, new HashSet<Quai>());
            }

            this.stations.get(stVirt).add(st1);
            this.stations.get(stVirt).add(st2);

            this.relTraites.add(rel);
        }
    }

    /**
     * Retourne le trajet le plus court entre deux stations. Se base sur l'algorithme de Dijkstra
     * (voir https://fr.wikipedia.org/wiki/Algorithme_de_Dijkstra).
     * 
     * @param station1 Objet reference de la station de départ
     * @param station2 Objet reference de la station d'arrivée
     * @return renoive une liste de relations (arêtes) qui forment le trajet le plus court (dans
     *         l'ordre)
     */
    public List<Relation> dijkstra_algo(Quai station1, Quai station2) {
        Map<Quai, Integer> distances = new HashMap<Quai, Integer>();
        Map<Quai, Relation> anteriorite = new HashMap<Quai, Relation>();
        Set<Quai> quaiTraitees = new HashSet<Quai>();

        ArrayList<Relation> relations = new ArrayList<Relation>(this.relations);
        ArrayList<Quai> quais = new ArrayList<Quai>(this.quais);

        // il faut ajouter aux relations déjà présente la relation entre les quai de la station
        if (station1.virtuel) {
            if (!this.stations.keySet().contains(station1)) {
                System.out.println(
                        "Erreur : la station " + station1 + " n'existe pas dans le réseau");
                return null;
            }

            quais.add(station1);

            for (Quai st : this.stations.get(station1)) {
                relations.add(new Relation(station1, st, 0));
                System.out.println("Ajout de la relation " + station1 + " -> " + st);
            }
        }

        if (station2.virtuel) {
            if (!this.stations.keySet().contains(station2)) {
                System.out.println(
                        "Erreur : la station " + station2 + " n'existe pas dans le réseau");
                return null;
            }

            quais.add(station2);

            for (Quai st : this.stations.get(station2)) {
                relations.add(new Relation(station2, st, 0));
                System.out.println("Ajout de la relation " + station2 + " -> " + st);
            }
        }

        // initialisation
        for (Quai station : quais) {
            distances.put(station, Integer.MAX_VALUE);
        }

        distances.put(station1, 0);

        Quai lastQuai = station1; // pour debug

        // on va parcourir toutes les stations
        while (quaiTraitees.size() != quais.size()) {
            // on prend la station la plus proche
            Quai stationPlusProche = null;
            int distanceMin = Integer.MAX_VALUE;
            for (Quai station : quais) {
                if (distances.get(station) < distanceMin && !quaiTraitees.contains(station)) {
                    stationPlusProche = station;
                    distanceMin = distances.get(station);
                }
            }

            if (stationPlusProche == null) {
                System.out.println("Erreur : le réseau n'est pas connexe");
                System.out.println("Dernière station traitée : " + lastQuai + "(quai traitees : "
                        + quaiTraitees.size() + ")");
                return null;
            }

            // on a trouvé la station la plus proche
            quaiTraitees.add(stationPlusProche);

            lastQuai = stationPlusProche;

            final Quai stationProche = stationPlusProche;

            // Relations reliées à la station
            // graphe non orienté
            // donc stationPlusProche peut être st1 ou st2
            // on utilise donc la méthode hasStation pour savoir si la station est dans la relation
            // peu importe
            // si elle est st1 ou st2
            List<Relation> relationsStation = relations.stream()
                    .filter(rel -> rel.hasStation(stationProche)).collect(Collectors.toList());

            if (relationsStation.size() == 0) {
                System.out.println("Erreur : la station " + stationPlusProche
                        + " n'est reliée à aucune autre station");
                continue;
            }

            // on va mettre à jour les distances des stations voisines
            for (Relation rel : relationsStation) {
                // on cherche la station à l'autre bout de la relation
                Quai stationVoisine = rel.getOtherStation(stationPlusProche);
                int distance = distances.get(stationPlusProche) + rel.temps;
                if (distance < distances.get(stationVoisine)) {
                    distances.put(stationVoisine, distance);
                    anteriorite.put(stationVoisine, rel);
                }

            }
        }

        // on a fini de parcourir toutes les stations
        // on a donc toutes les distances
        // on va maintenant reconstruire le chemin le plus court
        List<Relation> chemin = new ArrayList<Relation>();
        Quai station = station2;

        System.out.println("fin première boucle finis");

        // je reconstruit le chemin dans l'ordre inverse
        while (!station.equals(station1)) {
            Relation relPrec = anteriorite.get(station);

            if (relPrec == null) {
                System.out.println("Erreur : le réseau n'est pas connexe");
                return null;
            }
            
            chemin.add(relPrec);
            station = relPrec.getOtherStation(station);
        }

        // on a fini de reconstruire le chemin
        System.out.println("Chemin : " + chemin.get(0));
        System.out.println("Nation rela : " + relations.stream()
                .filter(rel -> rel.hasStation(station2)).collect(Collectors.toList()));
        
        return chemin;
    }

    /**
     * Retourne le trajet le plus court entre deux stations. Se base sur l'algorithme de
     * Bellman-Ford (voir https://fr.wikipedia.org/wiki/Algorithme_de_Bellman-Ford).
     * 
     * @param station1 Objet reference de la station de départ
     * @param station2 Objet reference de la station d'arrivée
     * @return renoive une liste de relations (arêtes) qui forment le trajet le plus court (dans
     *         l'ordre)
     */
    public List<Relation> Bellman_Ford_algo(Quai station1, Quai station2) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Afficher les éléments du réseau métropolitain à la demande de l’utilisateur : - La liste des
     * stations d’une ligne donnée.
     * 
     * ! doit renvoyer une exception si la ligne n'existe pas
     * 
     * @param ligne le nom de la ligne dont on veut afficher les stations
     * @return ne renvoie rien (affiche les stations)
     */
    public void printStationLigne(String ligne) {
        boolean ligneExiste = quais.stream().anyMatch(quai -> quai.getLigne().equals(ligne));
        if (!ligneExiste) {
            throw new IllegalArgumentException("La ligne spécifiée n'existe pas.");
        }

        // Afficher les stations de la ligne
        quais.stream().filter(quai -> quai.getLigne().equals(ligne)).map(Quai::getNom)
                .forEach(System.out::println);
    }

    /**
     * Afficher les éléments du réseau métropolitain à la demande de l’utilisateur : - La ou les
     * correspondances possibles entre deux lignes.
     * 
     * @param ligne1
     * @param ligne2
     */
    public void correspondanceEntre2Lignes(String ligne1, String ligne2) {
        // Vérifier si les lignes existent dans la liste des quais
        boolean ligne1Existe = quais.stream().anyMatch(quai -> quai.getLigne().equals(ligne1));
        boolean ligne2Existe = quais.stream().anyMatch(quai -> quai.getLigne().equals(ligne2));

        if (!ligne1Existe || !ligne2Existe) {
            throw new IllegalArgumentException(
                    "Au moins l'une des lignes spécifiées n'existe pas.");
        }

        // Rechercher les correspondances possibles entre les deux lignes
        List<Quai> correspondances = new ArrayList<>();

        for (Quai quai1 : quais) {
            if (quai1.getLigne().equals(ligne1)) {
                for (Quai quai2 : quais) {
                    if (quai2.getLigne().equals(ligne2) && quai1.compareTo(quai2)) {
                        correspondances.add(quai1);
                        break;
                    }
                }
            }
        }

        // Afficher les correspondances trouvées
        if (correspondances.isEmpty()) {
            System.out.println("Il n'y a pas de correspondances entre les lignes spécifiées.");
        } else {
            System.out.println(
                    "Correspondances possibles entre les lignes " + ligne1 + " et " + ligne2 + ":");
            for (Quai correspondance : correspondances) {
                System.out.println(correspondance.getNom());
            }
        }
    }

    /**
     * Afficher les éléments du réseau métropolitain à la demande de l’utilisateur : - Les trajets
     * possibles entre deux stations, en indiquant le nombre de stations, le nombre de
     * correspondances, le temps estimé du trajet (on comptera 3 min entre deux stations et 6 min
     * par correspondance).
     */
    public void trajetEntre2Station(Quai station1, Quai station2) {

    }


    /**
     * Création de la méthode RelationComparator permettant de comparer les temps des éléments de la
     * liste relations (utilisé pour la méthode ACM)
     */
    public class RelationComparator implements Comparator<Relation> {
        public int compare(Relation r1, Relation r2) {
            return r1.getTemps().compareTo(r2.getTemps());
        }
    }

    /**
     * proposer un algorithme qui permet de trouver l’arbre couvrant minimum (ACM) reliant toutes
     * les stations. En quoi ce réseau serait-il avantageux ou pas pour la RATP ? et pour les
     * utilisateurs ?
     */
    public void ACM() {
        ArrayList<Relation> relation_ordonne = new ArrayList<>(relations);
        Collections.sort(relation_ordonne, new RelationComparator());

        HashSet<Quai> stationsSet = new HashSet<>();

        for (Relation relation : relations) {
            stationsSet.add(relation.getSt1());
            stationsSet.add(relation.getSt2());
        }

        int nbStations = stationsSet.size();

        for (int i = 0; i < nbStations; i++) {

        }
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Analyse plus poussée du graphe des lignes de métro :
     * 
     * Comparer 2 stations A et B : pour chacune, dire laquelle est la plus ACCESSIBLE que l’autre
     * (la plus proche d’une correspondance), laquelle est la plus CENTRALE (possède le plus de
     * correspondances à 𝑝-distance, la valeur de 𝑝 étant donnée par l’utilisateur) et laquelle
     * est la plus TERMINALE (plus proche en temps d’un terminus).
     * 
     * @param station1 Reference de la station 1
     * @param station2 Reference de la station 2
     */
    public void comparerStation_A_B(Quai station1, Quai station2, int pDistance) {
        throw new UnsupportedOperationException("Not supported yet.");

        // int accessibilite_S1 = 0;
        // int accessibilite_S2 = 0;
        // ACCESSIBLE
        // Problème avec l'utilisation de la classe BellmanFord
        // for (String key : stations.keySet()) {
        // List<Quai> quais_accassible = stations.get(key);
        // for (int i = 0; i < quais_accassible.size(); i++) {
        // Quai station_accessible = quais_accassible.get(i);
        // if (accessibilite_S1 < BellmanFord(station1, station_accessible)){
        // accessibilite_S1 = BellmanFord(station1, station_accessible);
        // }
        // if (accessibilite_S2 < BellmanFord(station2, station_accessible)){
        // accessibilite_S2 = BellmanFord(station2, station_accessible);
        // }
        // }
        // }
        // if (accessibilite_S1 < accessibilite_S2){
        // return station1.getNom() + " plus accessible que "+ station2.getNom();
        // }
        // if (accessibilite_S2 < accessibilite_S1){
        // return station2.getNom() + " plus accessible que "+ station1.getNom();
        // }
        // if (accessibilite_S2 == accessibilite_S1){
        // return station2.getNom() + " aussi accessible que "+ station1.getNom();
        // }

        // CENTRALE
        // Problème avec l'utilisation de la classe BellmanFord
        // int centrale_S1 = 0;
        // int centrale_S2 = 0;
        // for (String key : stations.keySet()) {
        // List<Quai> quais_accassible = stations.get(key);
        // for (int i = 0; i < quais_accassible.size(); i++) {
        // Quai station_accessible = quais_accassible.get(i);
        // if (centrale_S1 < BellmanFord(station1, station_accessible) && BellmanFord(station1,
        // station_accessible) <= pDistance ){
        // centrale_S1 += 1;
        // }
        // if (centrale_S2 < BellmanFord(station2, station_accessible) && BellmanFord(station2,
        // station_accessible) <= pDistance){
        // centrale_S2 += 1;
        // }
        // }
        // }
        // if (centrale_S1 < centrale_S2){
        // return station1.getNom() + " plus centrale que "+ station2.getNom() + " : " +
        // station1.getNom() +" a " + centrale_S1 + " stations à moins de " + pDistance + " contre "
        // + centraleS2 + " pour la station " + station2.getNom();
        // }
        // if (centrale_S1 < accessibilite_S1){
        // return station2.getNom() + " plus centrale que "+ station1.getNom() + " : " +
        // station2.getNom() +" a " + centrale_S2 + " stations à moins de " + pDistance + " contre "
        // + centrale_S1 + " pour la station " + station1.getNom();
        // }
        // if (centrale_S2 == centrale_S1){
        // return station1.getNom() + " aussi centrale que "+ station2.getNom() + " : " +
        // station1.getNom() +" a " + centrale_S1 + " stations à moins de " + pDistance + " et " +
        // station2.getNom() + " a " + centrale_S2 + " stations à cette distance" + ;
        // }

        // TERMINALE
        // ArrayList<Quai> terminus = new ArrayList<>();
        // for (Quai quai : quais) {
        // if (quai.isTerminus()) { // Vérifier si le terminus est vrai
        // terminus.add(quai);
        // }
        // }
        // for (Quai terminus_quai : terminus){
        // if (dijkstra_algo(station1, terminus_quai) > dijkstra_algo(station2, terminus_quai) ){
        // return station1 + " plus proche d'un terminus que "+ station2;
        // }
        // if (dijkstra_algo(station2, terminus_quai) > dijkstra_algo(station1, terminus_quai) ){
        // return station2 + " plus proche d'un terminus que "+ station1;
        // }
        // if (dijkstra_algo(station2, terminus_quai) == dijkstra_algo(station1, terminus_quai) ){
        // return station1 + " aussi plus proche d'un terminus que "+ station2;
        // }
        // }
    }


    /**
     * Analyse plus poussée du graphe des lignes de métro :
     * 
     * Dire si deux stations sont reliées à 𝑝-distance ;
     * 
     * @param station1 Reference de la station 1
     * @param station2 Reference de la station 2
     */
    public int reliePDistance(Quai station1, Quai station2, int pDistance) {
        List<Relation> relations = dijkstra_algo(station1, station2);
        int distance = relations.size();

        if (distance == pDistance) {
            return pDistance;
        } else {
            return distance;
        }
    }


    /**
     * Analyse plus poussée du graphe des lignes de métro :
     * 
     * - Les stations proches d’une station donnée ou reliées par une arête donnée (analyse
     * 1-distance) ;
     * 
     * @param station1
     * @param station2
     */
    public String analyse1Distance(Quai station1, Quai station2) {
        throw new UnsupportedOperationException("Not supported yet.");
        // Besoin de Bellman ou de Djikstra
        // int centrale_S1 = 0;
        // int centrale_S2 = 0;
        // for (String key : stations.keySet()) {
        // List<Quai> quais_accassible = stations.get(key);
        // for (int i = 0; i < quais_accassible.size(); i++) {
        // Quai station_accessible = quais_accassible.get(i);
        // if (centrale_S1 < BellmanFord(station1, station_accessible) && BellmanFord(station1,
        // station_accessible) <= 1 ){
        // centrale_S1 += 1;
        // }
        // if (centrale_S2 < BellmanFord(station2, station_accessible) && BellmanFord(station2,
        // station_accessible) <= 1){
        // centrale_S2 += 1;
        // }
        // }
        // }
        // if (centrale_S1 < centrale_S2){
        // return station1.getNom() + " plus proche d'une correspondance que "+ station2.getNom() +
        // " : " + station1.getNom() +" a " + centrale_S1 + " stations à moins de 1 contre " +
        // centrale_S2 + " pour la station " + station2.getNom();
        // }
        // if (centrale_S2 < centrale_S1){
        // return station2.getNom() + " plus proche d'une correspondance que "+ station1.getNom() +
        // " : " + station2.getNom() +" a " + centrale_S2 + " stations à moins de 1 contre " +
        // centrale_S1 + " pour la station " + station1.getNom();
        // }
        // if (centrale_S2 == centrale_S1){
        // return station1.getNom() + " aussi proche d'une correspondance que "+ station2.getNom() +
        // " : " + station1.getNom() +" a " + centrale_S1 + " stations à moins de 1 et " +
        // station2.getNom() + " a " + centrale_S2 + " stations à cette distance" + ;
        // }
    }

    /**
     * Être capable de fournir le trajet, le plus court en temps, entre deux stations passant par
     * une troisième station étape.
     * 
     * Cette fonction généralise cett etape en un trajet qui passe par plusieurs stations
     * 
     * @param stations
     */
    public void trajetEntrePlusieursStation(ArrayList<Quai> stations) {
        throw new UnsupportedOperationException("Not supported yet.");
    }


    public Set<Quai> getStationByName(String pNom) {
        Set<Quai> stations = new HashSet<>();
        Quai virtStation = this.stations.keySet().stream()
                .filter(station -> station.getNom().equals(pNom)).findFirst().orElse(null);

        if (virtStation != null) {
            stations.addAll(this.stations.get(virtStation));
        } else {
            Quai station = this.quais.stream().filter(quai -> quai.getNom().equals(pNom))
                    .findFirst().orElse(null);
            if (station != null) {
                stations.add(station);
            }
        }

        return stations;
    }
    // #endregion
}
