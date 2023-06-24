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

import javax.swing.RowFilter.Entry;

public class ReseauMetro {

    /**
     * Convertit une liste de relations en une liste de stations.
     * 
     * @param relations Liste de relations
     * @param start     Station de départ
     * @param end       Station d'arrivée
     * @return Liste de stations
     */
    public static List<Quai> convertRelationPathToStationPath(List<Relation> relations, Quai start,
            Quai end) {
        List<Quai> stations = new ArrayList<Quai>();
        Quai current = end;

        stations.add(end);

        // on parcourt les relations dans l'ordre inverse
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

    public static int calculertemps(List<Relation> relations) {
        return relations.stream().mapToInt(r -> r.temps).sum();
    }

    // #region Attributs
    List<Quai> quais;
    ArrayList<Relation> relations;
    Map<Quai, Set<Quai>> stations; // Quai est une station virtuelle qui regroupe toutes les
                                   // stations
                                   // ayant le même nom (en correspondance)
    Set<Relation> relTraites; // qui déja traité dans la fonction relierStationMemeNom
                              // (optimisation)
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
     * Constructeur avec paramètres. Initialise les listes de stations et de
     * relations avec les
     * paramètres.
     * 
     * @param stations  ArrayList de stations
     * @param relations ArrayList de relations
     */
    public ReseauMetro(ArrayList<Quai> stations, ArrayList<Relation> relations) {
        this.quais = stations;
        this.relations = relations;
        init();
    }

    /**
     * Initialise les attributs de la classe.
     * Cette méthode initialise les attributs de la classe comme la map des
     * stations.
     */
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
     * Relie les relations avec correspondance = true à un sommet virtuel qui
     * regroupe tout les
     * quais d'une même station
     */
    public void relierStationCorresp() {
        // on va parcourir toutes les relations
        for (Relation rel : this.relations) {
            // si la relation est déjà traitée ou si elle n'a pas de correspondance, on
            // continue
            if (this.relTraites.contains(rel) || !rel.correspondance) {
                continue;
            }

            Quai st1 = rel.getSt1();
            Quai st2 = rel.getSt2();

            Quai stVirt = new Quai(st1.terminus, st1.nom, st1.displayName);

            boolean alreadyExists = false;
            // on va vérifier si le quai virtuel existe déjà
            for (Quai st : this.stations.keySet()) {
                // si le quai virtuel existe déjà, on le récupère
                if (st.nom.equals(stVirt.nom)) {
                    alreadyExists = true;
                    stVirt = st;
                    break;
                }
            }

            // si le quai virtuel n'existe pas, on l'ajoute
            if (!alreadyExists) {
                this.stations.put(stVirt, new HashSet<Quai>());
            }

            this.stations.get(stVirt).add(st1);
            this.stations.get(stVirt).add(st2);

            this.relTraites.add(rel);
        }
    }

    enum TrajectPreference {
        TEMPS, CORRESPONDANCE
    }

    /*
     * Surcharge de la fonction dijkstra_algo, avec une préférence de trajet par défaut sur le temps
     */
    public List<Relation> dijkstra_algo(Quai station1, Quai station2) {
        return dijkstra_algo(station1, station2, TrajectPreference.TEMPS);
    }

    /**
     * Retourne le trajet le plus court entre deux stations. Se base sur
     * l'algorithme de Dijkstra
     * (voir https://fr.wikipedia.org/wiki/Algorithme_de_Dijkstra).
     * 
     * @param station1 Objet reference de la station de départ
     * @param station2 Objet reference de la station d'arrivée
     * @return renoive une liste de relations (arêtes) qui forment le trajet le plus
     *         court (dans
     *         l'ordre)
     */
    public List<Relation> dijkstra_algo(Quai station1, Quai station2, TrajectPreference pref) {
        Map<Quai, Integer> distances = new HashMap<Quai, Integer>();
        Map<Quai, Relation> anteriorite = new HashMap<Quai, Relation>();
        Set<Quai> quaiTraitees = new HashSet<Quai>();

        ArrayList<Relation> relations = new ArrayList<Relation>(this.relations);
        ArrayList<Quai> quais = new ArrayList<Quai>(this.quais);

        // il faut ajouter aux relations déjà présente la relation entre les quai de la
        // station
        if (station1.virtuel) {
            if (!this.stations.keySet().contains(station1)) {
                System.out.println(
                        "Erreur : la station " + station1 + " n'existe pas dans le réseau");
                return null;
            }

            quais.add(station1);

            // on ajoute les relations entre la station et les quais de la station
            System.out.println(
                    Couleurs.UNDERLINE + "\nAjout des relations de " + station1.getNom() + " :" + Couleurs.RESET);
            for (Quai st : this.stations.get(station1)) {
                relations.add(new Relation(station1, st, 0));
                System.out.println(station1 + "\n|-> " + st);
            }
        }

        // il faut ajouter aux relations déjà présente la relation entre les quai de la
        // station
        if (station2.virtuel) {
            if (!this.stations.keySet().contains(station2)) {
                System.out.println(
                        "Erreur : la station " + station2 + " n'existe pas dans le réseau");
                return null;
            }

            quais.add(station2);

            // on ajoute les relations entre la station et les quais de la station
            System.out.println(
                    Couleurs.UNDERLINE + "\nAjout des relations de " + station2.getNom() + " :" + Couleurs.RESET);
            for (Quai st : this.stations.get(station2)) {
                relations.add(new Relation(station2, st, 0));
                System.out.println(station2 + "\n|-> " + st);
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
                int distance = distances.get(station);

                if (distance < distanceMin && !quaiTraitees.contains(station)) {
                    stationPlusProche = station;
                    distanceMin = distances.get(station);
                }
            }

            // si on a pas trouvé de station, c'est que le réseau n'est pas connexe
            if (stationPlusProche == null) {
                System.out.println("Erreur : le réseau n'est pas connexe");
                System.out.println("Dernière station traitée : " + lastQuai + "(quai traitees : "
                        + quaiTraitees.size() + ")");
                return null;
            }

            // on ajoute la station à la liste des stations traitées
            quaiTraitees.add(stationPlusProche);

            lastQuai = stationPlusProche;

            final Quai stationProche = stationPlusProche;

            // Relations reliées à la station
            // graphe non orienté
            // donc stationPlusProche peut être st1 ou st2
            // on utilise donc la méthode hasStation pour savoir si la station est dans la
            // relation
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

                if(pref == TrajectPreference.CORRESPONDANCE && rel.correspondance) {
                    // on ajoute 8000s de correspondance si on veut privilégier le moins de correspondance possible
                    // cela permet de privilégier les trajets avec le moins de correspondance
                    
                    distance += 8000;
                    System.out.println("oui" + distance);
                }

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

        System.out.println(Couleurs.ITALIC + "\n                (Debug: 1ère boucle finie)" + Couleurs.RESET);

        // je reconstruit le chemin dans l'ordre inverse
        while (!station.equals(station1)) {
            Relation relPrec = anteriorite.get(station);

            // si la relation n'existe pas, c'est que le réseau n'est pas connexe
            if (relPrec == null) {
                System.out.println("Erreur : le réseau n'est pas connexe");
                return null;
            }

            chemin.add(relPrec);
            station = relPrec.getOtherStation(station);
        }

        // on a fini de reconstruire le chemin
        System.out.println("Chemin :n" + chemin.get(0));
        System.out.println("Nation rela :\n" + relations.stream()
                .filter(rel -> rel.hasStation(station2)).collect(Collectors.toList()));

        return chemin;
    }

    /**
     * Retourne le trajet le plus court entre deux stations. Se base sur
     * l'algorithme de
     * Bellman-Ford (voir https://fr.wikipedia.org/wiki/Algorithme_de_Bellman-Ford).
     * 
     * @param station1 Objet reference de la station de départ
     * @param station2 Objet reference de la station d'arrivée
     * @return renoive une liste de relations (arêtes) qui forment le trajet le plus
     *         court (dans
     *         l'ordre)
     */
    public List<Relation> Bellman_Ford_algo(Quai station1, Quai station2) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Afficher les éléments du réseau métropolitain à la demande de l’utilisateur :
     * - La liste des
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
     * Afficher les éléments du réseau métropolitain à la demande de l’utilisateur :
     * - La ou les
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

        // Parcourir tous les quais
        for (java.util.Map.Entry<Quai, Set<Quai>> entry : this.stations.entrySet()) {
            Quai quai = entry.getKey();
            Set<Quai> correspondancesQuai = entry.getValue();

            boolean contientLigne1 = false;
            boolean contientLigne2 = false;

            for (Quai correspondance : correspondancesQuai) {
                if (correspondance.getLigne().equals(ligne1)) {
                    contientLigne1 = true;
                }
                if (correspondance.getLigne().equals(ligne2)) {
                    contientLigne2 = true;
                }
            }

            if (contientLigne1 && contientLigne2) {
                correspondances.add(quai);
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
     * Création de la méthode RelationComparator permettant de comparer les temps
     * des éléments de la
     * liste relations (utilisé pour la méthode ACM)
     */
    public class RelationComparator implements Comparator<Relation> {
        public int compare(Relation r1, Relation r2) {
            return r1.getTemps().compareTo(r2.getTemps());
        }
    }

    /**
     * proposer un algorithme qui permet de trouver l’arbre couvrant minimum (ACM)
     * reliant toutes
     * les stations. En quoi ce réseau serait-il avantageux ou pas pour la RATP ? et
     * pour les
     * utilisateurs ?
     */
    public List<Relation> ACM() {
        // Copier les relations existantes
        List<Relation> allRelations = new ArrayList<>(relations);

        // Trier les relations par ordre croissant de temps
        allRelations.sort(new RelationComparator());

        // Créer une liste pour stocker les relations de l'ACM
        List<Relation> acm = new ArrayList<>();

        // Créer un tableau pour stocker les parents de chaque quai
        int[] parent = new int[quais.size()];
        for (int i = 0; i < quais.size(); i++) {
            parent[i] = i;
        }

        // Parcourir toutes les relations triées
        for (Relation relation : allRelations) {
            Quai st1 = relation.getSt1();
            Quai st2 = relation.getSt2();

            // Vérifier si l'ajout de cette relation crée un cycle
            if (!connected(parent, st1, st2)) {
                // Ajouter la relation à l'ACM
                acm.add(relation);
                // Fusionner les ensembles de st1 et st2
                union(parent, st1, st2);
            }
        }
        return acm;
    }

    /**
     * Vérifie si deux quais sont connectés dans l'ensemble disjoint représenté par
     * le tableau
     * parent.
     *
     * @param parent Tableau représentant les ensembles disjoint
     * @param quai1  Premier quai
     * @param quai2  Deuxième quai
     * @return true si les quais sont connectés, false sinon
     */
    private boolean connected(int[] parent, Quai quai1, Quai quai2) {
        int quai1Index = quais.indexOf(quai1);
        int quai2Index = quais.indexOf(quai2);
        return find(parent, quai1Index) == find(parent, quai2Index);
    }

    /**
     * Effectue l'opération d'union entre deux ensembles représentés par le tableau
     * parent.
     *
     * @param parent Tableau représentant les ensembles disjoint
     * @param quai1  Premier quai
     * @param quai2  Deuxième quai
     */
    private void union(int[] parent, Quai quai1, Quai quai2) {
        int quai1Index = quais.indexOf(quai1);
        int quai2Index = quais.indexOf(quai2);
        int root1 = find(parent, quai1Index);
        int root2 = find(parent, quai2Index);
        parent[root1] = root2;
    }

    /**
     * Retourne la racine de l'ensemble auquel appartient l'élément représenté par
     * l'index.
     *
     * @param parent Tableau représentant les ensembles disjoint
     * @param index  Index de l'élément
     * @return Racine de l'ensemble
     */
    private int find(int[] parent, int index) {
        while (parent[index] != index) {
            index = parent[index];
        }
        return index;
    }

    enum TypeAnalyse {
        ACCESSIBLE, CENTRALE, TERMINALE
    }

    /**
     * Analyse plus poussée du graphe des lignes de métro :
     * 
     * Comparer 2 stations A et B : pour chacune, dire laquelle est la plus
     * ACCESSIBLE que l’autre
     * (la plus proche d’une correspondance), laquelle est la plus CENTRALE (possède
     * le plus de
     * correspondances à 𝑝-distance, la valeur de 𝑝 étant donnée par
     * l’utilisateur) et laquelle
     * est la plus TERMINALE (plus proche en temps d’un terminus).
     * 
     * @param station1  Reference de la station 1
     * @param station2  Reference de la station 2
     * @param pDistance Distance p
     */
    public void comparerStation_A_B(Quai station1, Quai station2, int pDistance,
            TypeAnalyse typeAnalyse) {
        List<Quai> corresp = this.stations.keySet().stream().filter(s -> s.virtuel).collect(Collectors.toList());

        switch (typeAnalyse) {
            case ACCESSIBLE:
                int distanceMinStation1 = Integer.MAX_VALUE;
                Quai stationMinStation1 = null;

                int distanceMinStation2 = Integer.MAX_VALUE;
                Quai stationMinStation2 = null;

                // on va parcourir toutes les correspondances
                for (Quai qVirt : corresp) {
                    // on ne prend pas en compte la station elle-même
                    if (!qVirt.equals(station1)) {
                        System.out.println(
                                "Trajet entre " + station1.getNom() + " et " + qVirt.getNom());
                        // on calcule le trajet entre la station et la correspondance
                        // on récupère toutes les relations
                        List<Relation> relations = dijkstra_algo(station1, qVirt);
                        // on calcule la distance totale
                        int distance = relations.stream().reduce(0, (acc, r) -> acc + r.getTemps(),
                                Integer::sum);

                        // on compare la distance avec la distance minimale
                        if (distance < distanceMinStation1) {
                            distanceMinStation1 = distance;
                            stationMinStation1 = qVirt;
                        }
                    }

                    // on ne prend pas en compte la station elle-même
                    if (!qVirt.equals(station2)) {
                        // on calcule le trajet entre la station et la correspondance
                        List<Relation> relations2 = dijkstra_algo(station2, qVirt);
                        int distance2 = relations2.stream().reduce(0,
                                (acc, r) -> acc + r.getTemps(), Integer::sum);

                        // on compare la distance avec la distance minimale
                        if (distance2 < distanceMinStation2) {
                            distanceMinStation2 = distance2;
                            stationMinStation2 = qVirt;
                        }
                    }
                }

                // on compare les distances minimales
                if (distanceMinStation1 < distanceMinStation2) {
                    System.out.println("\nLa station " + station1.getNom()
                            + " est plus accessible que la station " + station2.getNom());
                    System.out.println(
                            "La station " + station1.getNom() + " est à " + distanceMinStation1
                                    + " secondes de la station " + stationMinStation1.getNom());
                    // on compare les distances minimales
                } else if (distanceMinStation1 > distanceMinStation2) {
                    System.out.println("\nLa station " + station2.getNom()
                            + " est plus accessible que la station " + station1.getNom());
                    System.out.println(
                            "La station " + station2.getNom() + " est à " + distanceMinStation2
                                    + " secondes de la station " + stationMinStation2.getNom());
                } else {
                    System.out.println("\nLes deux stations sont aussi accessibles");
                }
                break;
            case CENTRALE:
                int nbCorrespStation1 = 0;
                int nbCorrespStation2 = 0;

                // on va parcourir toutes les correspondances
                for (Quai qVirt : corresp) {
                    // on ne prend pas en compte la station elle-même
                    if (!qVirt.equals(station1)) {
                        List<Relation> relations = dijkstra_algo(station1, qVirt);
                        int distance = relations.stream().reduce(0, (acc, r) -> acc + r.getTemps(),
                                Integer::sum);

                        // on compare la distance avec la distance minimale
                        if (distance <= pDistance) {
                            nbCorrespStation1++;
                        }
                    }

                    // on ne prend pas en compte la station elle-même
                    if (!qVirt.equals(station2)) {
                        List<Relation> relations2 = dijkstra_algo(station2, qVirt);
                        int distance2 = relations2.stream().reduce(0,
                                (acc, r) -> acc + r.getTemps(), Integer::sum);
                        // on compare la distance avec la distance minimale
                        if (distance2 <= pDistance) {
                            nbCorrespStation2++;
                        }
                    }
                }

                if (nbCorrespStation1 > nbCorrespStation2) {
                    System.out.println("\nLa station " + station1.getNom()
                            + " est plus centrale que la station " + station2.getNom());
                    System.out.println(
                            "La station " + station1.getNom() + " possède " + nbCorrespStation1
                                    + " correspondances à " + pDistance + " s de trajet");
                } else if (nbCorrespStation1 < nbCorrespStation2) {
                    System.out.println("\nLa station " + station2.getNom()
                            + " est plus centrale que la station " + station1.getNom());
                    System.out.println(
                            "La station " + station2.getNom() + " possède " + nbCorrespStation2
                                    + " correspondances à " + pDistance + " s de trajet");
                } else {
                    System.out.println("\nLes deux stations sont aussi centrales");
                    System.out.println("Les deux stations possèdent " + nbCorrespStation1
                            + " correspondances à " + pDistance + " s de trajer");
                }
                break;
            case TERMINALE:
                // plus proche en temps d’un terminus
                List<Quai> terminus = this.stations.keySet().stream().filter(s -> s.terminus)
                        .collect(Collectors.toList());

                int distanceMinStation1Terminus = Integer.MAX_VALUE;
                Quai stationMinStation1Terminus = null;

                int distanceMinStation2Terminus = Integer.MAX_VALUE;
                Quai stationMinStation2Terminus = null;

                // on va parcourir toutes les correspondances
                for (Quai qTerminus : terminus) {
                    // on ne prend pas en compte la station elle-même
                    if (!qTerminus.equals(station1)) {
                        // on calcule le trajet entre la station et la correspondance
                        List<Relation> relations = dijkstra_algo(station1, qTerminus);
                        int distance = relations.stream().reduce(0, (acc, r) -> acc + r.getTemps(),
                                Integer::sum);

                        // on compare la distance avec la distance minimale
                        if (distance < distanceMinStation1Terminus) {
                            distanceMinStation1Terminus = distance;
                            stationMinStation1Terminus = qTerminus;
                        }
                    }

                    // on ne prend pas en compte la station elle-même
                    if (!qTerminus.equals(station2)) {
                        List<Relation> relations2 = dijkstra_algo(station2, qTerminus);
                        int distance2 = relations2.stream().reduce(0,
                                (acc, r) -> acc + r.getTemps(), Integer::sum);

                        // on compare la distance avec la distance minimale
                        if (distance2 < distanceMinStation2Terminus) {
                            distanceMinStation2Terminus = distance2;
                            stationMinStation2Terminus = qTerminus;
                        }
                    }
                }

                if (distanceMinStation1Terminus < distanceMinStation2Terminus) {
                    System.out.println("\nLa station " + station1.getNom()
                            + " est plus proche d'un terminus que la station " + station2.getNom());
                    System.out.println("La station " + station1.getNom() + " est à "
                            + distanceMinStation1Terminus + " secondes du terminus "
                            + stationMinStation1Terminus.getNom());
                } else if (distanceMinStation1Terminus > distanceMinStation2Terminus) {
                    System.out.println("\nLa station " + station2.getNom()
                            + " est plus proche d'un terminus que la station " + station1.getNom());
                    System.out.println("La station " + station2.getNom() + " est à "
                            + distanceMinStation2Terminus + " secondes du terminus "
                            + stationMinStation2Terminus.getNom());
                } else {
                    System.out.println("\nLes deux stations sont aussi proches d'un terminus");
                }
                break;
        }

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
        int distance = relations.stream().reduce(0, (acc, r) -> acc + r.getTemps(), Integer::sum);

        // on compare la distance avec la distance minimale
        if (distance == pDistance) {
            return pDistance;
        } else {
            return distance;
        }
    }

    /**
     * Analyse plus poussée du graphe des lignes de métro :
     * 
     * - Les stations proches d’une station donnée ou reliées par une arête donnée
     * (analyse
     * 1-distance) ;
     * 
     * @param station1
     * @param station2
     */
    public String analyse1Distance(Quai station1, Quai station2) {
        List<Relation> rel = this.relations.stream()
                .filter(r -> r.hasStation(station1) && r.hasStation(station2))
                .collect(Collectors.toList());

        // on compare la distance avec la distance minimale
        if (rel.size() > 0) {
            return "Les stations " + station1.getNom() + " et " + station2.getNom()
                    + " sont reliées par une arête";
        } else {
            return "Les stations " + station1.getNom() + " et " + station2.getNom()
                    + " ne sont pas reliées par une arête";
        }
    }

    /**
     * Être capable de fournir le trajet, le plus court en temps, entre deux
     * stations passant par
     * une troisième station étape.
     * 
     * Cette fonction généralise cett etape en un trajet qui passe par plusieurs
     * stations
     * 
     * @param stations
     */
    public List<Relation> trajetEntrePlusieursStation(ArrayList<Quai> stations, TrajectPreference pref) {
        ArrayList<Relation> relations = new ArrayList<>();
        for (int i = 0; i < stations.size() - 1; i++) {
            Quai station1 = stations.get(i);
            Quai station2 = stations.get(i + 1);

            System.out.println("Calcul du trajet entre " + station1.getNom() + " et "
                    + station2.getNom());

            List<Relation> relations2 = dijkstra_algo(station1, station2, pref);

            // reverse
            Collections.reverse(relations2);

            relations.addAll(relations2);
        }

        return relations;
    }

    /**
     * Obtient les quais correspondant à un nom spécifié.
     * Cette méthode retourne un ensemble de quais correspondant au nom spécifié.
     * Elle va rechercher les quais virtuels dans la map des stations en utilisant
     * le nom spécifié.
     * Si des quais virtuels sont trouvés, tous les quais correspondants sont
     * ajoutés à l'ensemble de quais retourné.
     * Si aucun quai virtuel n'est trouvé
     * la méthode recherche les quais physiques dans la liste des quais, et ajoute
     * le premier quai correspondant trouvé à l'ensemble de quais retourné.
     * 
     * @param pNom Nom pour la recherche des quais
     * @return Ensemble de quais correspondant au nom spécifié
     */
    public Set<Quai> getStationByName(String pNom) {
        Set<Quai> stations = new HashSet<>();
        Quai virtStation = this.stations.keySet().stream()
                .filter(station -> station.getNom().equals(pNom)).findFirst().orElse(null);
        // si la station est virtuelle, on ajoute toutes les stations correspondantes
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

    /**
     * Obtient le quai virtuel correspondant à un quai spécifié.
     * Cette méthode recherche le quai virtuel correspondant au quai spécifié en
     * utilisant le nom du quai.
     * Elle parcourt la map des stations et filtre les quais virtuels dont le nom
     * correspond au nom du quai spécifié.
     * Si un quai virtuel est trouvé, il est retourné.
     * Sinon, le quai spécifié est retourné.
     * 
     * @param pStation Quai spécifié
     * @return Quai virtuel correspondant au quai spécifié, ou le quai spécifié
     *         lui-même s'il n'y a pas de correspondance virtuelle
     */
    public Quai getStationVirtByStation(Quai pStation) {
        // pStation est un quai

        // on va chercher le quai virtuel correspondant
        Quai virtStation = this.stations.keySet().stream()
                .filter(station -> station.getNom().equals(pStation.getNom())).findFirst()
                .orElse(null);

        // si la station est virtuelle, on ajoute toutes les stations correspondantes
        if (virtStation != null) {
            return virtStation;
        } else {
            return pStation;
        }
    }
}
