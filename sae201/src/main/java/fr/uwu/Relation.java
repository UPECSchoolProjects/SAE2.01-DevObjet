package fr.uwu;

/**
 * Represente une relation (un troncon de métro) entre deux stations.
 * C'est l'arête du graphe.
 */
public class Relation {

    //#region Variables
    // TODO : Remplacer Integer par Station
    // TODO : Getters and Setters si besoin
    Integer id1;
    Integer id2;
    Integer temps;
    //#endregion

    //#region Constructeurs
    public Relation() {
    }

    public Relation(Integer id1, Integer id2, Integer temps) {
        this.id1 = id1;
        this.id2 = id2;
        this.temps = temps;
    }
    //#endregion

    //#region Méthodes

    public String toString() {
        return "Id1: " + id1 + " Id2: " + id2 + " Tps: " + temps;
    }

    public boolean compareTo(Relation relation) {
        if (this.id1 != relation.id1) {
            return false;
        }
        if (this.id2 != relation.id2) {
            return false;
        }
        if (this.temps != relation.temps) {
            return false;
        }
        return true;
    }

    //#endregion
}
