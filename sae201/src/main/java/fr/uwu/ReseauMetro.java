package fr.uwu;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe principale du Grpahe. ELle gère les stations (noed) et les relations entre les stations (arête).
 */
public class ReseauMetro {

    //#region Attributs
    ArrayList<Station> stations;
    ArrayList<Relation> relations;
    //#endregion

    //#region Constructeurs

    /**
     * Constructeur par défaut. Initialise les listes de stations et de relations (vides).
     */
    public ReseauMetro() {
        this.stations = new ArrayList<Station>();
        this.relations = new ArrayList<Relation>();
    }

    /**
     * Constructeur avec paramètres. Initialise les listes de stations et de relations avec les paramètres.
     * @param stations ArrayList de stations
     * @param relations ArrayList de relations
     */
    public ReseauMetro(ArrayList<Station> stations, ArrayList<Relation> relations) {
        this.stations = stations;
        this.relations = relations;
    }

    //#endregion
    
    //#region Méthodes

    /**
     * Ajoute une station (noed) au graphe.
     * @param station Objet Station à ajouter
     */
    public void addStation(Station station) {
        this.stations.add(station);
    }

    /**
     * Ajoute une relation (arête) au graphe.
     * @param relation Objet Relation à ajouter
     */
    public void addRelation(Relation relation) {
        this.relations.add(relation);
    }

    /**
     * Retourne le trajet le plus court entre deux stations.
     * Se base sur l'algorithme de Dijkstra (voir https://fr.wikipedia.org/wiki/Algorithme_de_Dijkstra). 
     * @param station1 Objet reference de la station de départ
     * @param station2 Objet reference de la station d'arrivée
     * @return renoive une liste de relations (arêtes) qui forment le trajet le plus court (dans l'ordre)
     */
    public List<Relation> dijkstra_algo(Station station1, Station station2) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Retourne le trajet le plus court entre deux stations.
     * Se base sur l'algorithme de Bellman-Ford (voir https://fr.wikipedia.org/wiki/Algorithme_de_Bellman-Ford). 
     * @param station1 Objet reference de la station de départ
     * @param station2 Objet reference de la station d'arrivée
     * @return renoive une liste de relations (arêtes) qui forment le trajet le plus court (dans l'ordre)
     */
    public List<Relation> Bellman_Ford_algo(Station station1, Station station2) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Afficher les éléments du réseau métropolitain à la demande de
     * l’utilisateur :
     * - La liste des stations d’une ligne donnée.
     * 
     * ! doit renvoyer une exception si la ligne n'existe pas
     * 
     * @param ligne le nom de la ligne dont on veut afficher les stations
     * @return ne renvoie rien (affiche les stations)
     */
    public void printStatioLigne(String ligne) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Afficher les éléments du réseau métropolitain à la demande de
     * l’utilisateur :
     * - La ou les correspondances possibles entre deux lignes.
     * 
     * @param ligne1
     * @param ligne2
     */
    public void printCorrespondanceEntre2Ligne(String ligne1, String ligne2) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Afficher les éléments du réseau métropolitain à la demande de
     * l’utilisateur :
     * - Les trajets possibles entre deux stations, en indiquant le
     * nombre de stations, le nombre de correspondances, le temps
     * estimé du trajet (on comptera 3 min entre deux stations et 6 min par
     * correspondance).
     */
    public void trajetEntre2Station(Station station1, Station station2) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * proposer un algorithme qui permet de trouver l’arbre
     * couvrant minimum (ACM) reliant toutes les stations. En quoi ce
     * réseau serait-il avantageux ou pas pour la RATP ? et pour les
     * utilisateurs ?
     */
    public void ACM() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Analyse plus poussée du graphe des lignes de métro :
     * 
     * Comparer 2 stations A et B : pour chacune, dire laquelle est
     * la plus ACCESSIBLE que l’autre (la plus proche d’une
     * correspondance), laquelle est la plus CENTRALE (possède le
     * plus de correspondances à 𝑝-distance, la valeur de 𝑝 étant
     * donnée par l’utilisateur) et laquelle est la plus TERMINALE
     * (plus proche en temps d’un terminus).
     * 
     * @param station1 Reference de la station 1
     * @param station2 Reference de la station 2
     */
    public void comparerStation_A_B(Station station1, Station station2) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Analyse plus poussée du graphe des lignes de métro :
     * 
     * Dire si deux stations sont reliées à 𝑝-distance ;
     * 
     * @param station1 Reference de la station 1
     * @param station2 Reference de la station 2
     */
    public void reliePDistance(Station station1, Station station2) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Analyse plus poussée du graphe des lignes de métro :
     * 
     * - Les stations proches d’une station donnée ou reliées par une
     * arête donnée (analyse 1-distance) ;
     * 
     * @param station1
     * @param station2
     */
    public void analyse1Distance(Station station1, Station station2) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Être capable de fournir le trajet, le plus court en temps, entre deux
     * stations passant par une troisième station étape.
     * 
     * Cette fonction généralise cett etape en un trajet qui passe par plusieurs stations
     * 
     * @param stations
     */
    public void trajetEntrePlusieursStation(ArrayList<Station> stations) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    //#endregion
}
