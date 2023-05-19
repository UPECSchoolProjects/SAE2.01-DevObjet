package fr.uwu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
/**
 * Classe principale du Graphe. ELle gère les stations (noeud) et les relations
 * entre les stations (arête).
 */
import java.util.Set;
public class ReseauMetro {

    // #region Attributs
    ArrayList<Quai> quais;
    ArrayList<Relation> relations;
    Map<String, List<Quai>> stations;
    Set<Integer> quaisTraites; // qui déja traité dans la fonction relierStationMemeNom (optimisation)
    // #endregion

    // #region Constructeurs

    /**
     * Constructeur par défaut. Initialise les listes de stations et de relations
     * (vides).
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
        this.stations = new HashMap<String, List<Quai>>();
        this.quaisTraites = new HashSet<Integer>();
        this.relierStationMemeNom();
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
        this.relierStationMemeNom();
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
     * 
     */
    public void relierStationMemeNom() {
        for (Quai station : this.quais) {
            //System.out.println(station);
            // ne rien faire si la station est déja présente dans la hashmap
            if (this.quaisTraites.contains(station.id)) {
                continue;
            }

            if (this.stations.containsKey(station.nom)) {
                this.stations.get(station.nom).add(station);

                // relier cette station a toutes les autres déja presente
                for (Quai stationsDejaPresente : this.stations.get(station.nom)) {
                    if (stationsDejaPresente != station) {
                        //System.out.println("Relier " + station + " à " + stationsDejaPresente);
                        this.relations.add(new Relation(station, stationsDejaPresente, 180));
                    }
                }
            } else {
                this.stations.put(station.nom, new ArrayList<Quai>(Arrays.asList(station)));
            }

            this.quaisTraites.add(station.id);
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
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Retourne le trajet le plus court entre deux stations.
     * Se base sur l'algorithme de Bellman-Ford (voir
     * https://fr.wikipedia.org/wiki/Algorithme_de_Bellman-Ford).
     * 
     * @param station1 Objet reference de la station de départ
     * @param station2 Objet reference de la station d'arrivée
     * @return renoive une liste de relations (arêtes) qui forment le trajet le plus
     *         court (dans l'ordre)
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
        quais.stream()
                .filter(quai -> quai.getLigne().equals(ligne))
                .map(Quai::getNom)
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
            throw new IllegalArgumentException("Au moins l'une des lignes spécifiées n'existe pas.");
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
            System.out.println("Correspondances possibles entre les lignes " + ligne1 + " et " + ligne2 + ":");
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
     * proposer un algorithme qui permet de trouver l’arbre couvrant minimum (ACM) reliant toutes
     * les stations. En quoi ce réseau serait-il avantageux ou pas pour la RATP ? et pour les
     * utilisateurs ?
     */
    public void ACM() {
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
        //     List<Quai> quais_accassible = stations.get(key);  
        //     for (int i = 0; i < quais_accassible.size(); i++) {
        //         Quai station_accessible = quais_accassible.get(i);
        //         if (accessibilite_S1 < BellmanFord(station1, station_accessible)){
        //             accessibilite_S1 = BellmanFord(station1, station_accessible);
        //         }
        //         if (accessibilite_S2 < BellmanFord(station2, station_accessible)){
        //             accessibilite_S2 = BellmanFord(station2, station_accessible);
        //         }
        //     }
        // }
        // if (accessibilite_S1 < accessibilite_S2){
        //     return station1.getNom() + " plus accessible que "+ station2.getNom();
        // }
        // if (accessibilite_S2 < accessibilite_S1){
        //     return station2.getNom() + " plus accessible que "+ station1.getNom();
        // }
        // if (accessibilite_S2 == accessibilite_S1){
        //     return station2.getNom() + " aussi accessible que "+ station1.getNom();
        // }

        // CENTRALE
// Problème avec l'utilisation de la classe BellmanFord
        // int centrale_S1 = 0;
        // int centrale_S2 = 0;
        // for (String key : stations.keySet()) {
        //     List<Quai> quais_accassible = stations.get(key);  
        //     for (int i = 0; i < quais_accassible.size(); i++) {
        //         Quai station_accessible = quais_accassible.get(i);
        //         if (centrale_S1 < BellmanFord(station1, station_accessible) && BellmanFord(station1, station_accessible) <= pDistance ){
        //             centrale_S1 += 1;
        //         }
        //         if (centrale_S2 < BellmanFord(station2, station_accessible) && BellmanFord(station2, station_accessible) <= pDistance){
        //             centrale_S2 += 1;
        //         }
        //     }
        // }
        // if (accessibilite_S1 < accessibilite_S2){
        //     return station1.getNom() + " plus centrale que "+ station2.getNom() + " : " + station1.getNom() +" a " + accessibilite_S1 + " stations à moins de " + pDistance + " contre " + accessibilite_S2 + " pour la station " + station2.getNom();
        // }
        // if (accessibilite_S2 < accessibilite_S1){
        //     return station2.getNom() + " plus centrale que "+ station1.getNom() + " : " + station2.getNom() +" a " + accessibilite_S2 + " stations à moins de " + pDistance + " contre " + accessibilite_S1 + " pour la station " + station1.getNom();
        // }
        // if (accessibilite_S2 == accessibilite_S1){
        //     return station1.getNom() + " aussi centrale que "+ station2.getNom() + " : " + station1.getNom() +" a " + accessibilite_S1 + " stations à moins de " + pDistance + " et " + station2.getNom() + " a " + accessibilite_S2 + " stations à cette distance" + ;
        // }
        
        // TERMINALE
        // ArrayList<Quai> terminus = new ArrayList<>();
        // for (Quai quai : quais) {
        //     if (quai.isTerminus()) { // Vérifier si le terminus est vrai
        //         terminus.add(quai);
        //     }
        // }
        // for (Quai terminus_quai : terminus){
        //     if (dijkstra_algo(station1, terminus_quai) > dijkstra_algo(station2, terminus_quai) ){
        //         return station1 + " plus proche d'un terminus que "+ station2;
        //     }
        //     if (dijkstra_algo(station2, terminus_quai) > dijkstra_algo(station1, terminus_quai) ){
        //         return station2 + " plus proche d'un terminus que "+ station1;
        //     }
        //     if (dijkstra_algo(station2, terminus_quai) == dijkstra_algo(station1, terminus_quai) ){
        //         return station1 + " aussi plus proche d'un terminus que "+ station2;
        //     }
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
    public void analyse1Distance(Quai station1, Quai station2) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Être capable de fournir le trajet, le plus court en temps, entre deux stations passant par
     * une troisième station étape.
     * 
     * Cette fonction généralise cett etape en un trajet qui passe par plusieurs
     * stations
     * 
     * @param stations
     */
    public void trajetEntrePlusieursStation(ArrayList<Quai> stations) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    // #endregion
}
