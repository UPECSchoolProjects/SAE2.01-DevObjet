package fr.uwu;

/**
 * Represente une relation (un troncon de métro) entre deux stations.
 * C'est l'arête du graphe.
 */
public class Relation {

    // #region Variables
    // TODO : Getters and Setters si besoin
    Quai st1;
    Quai st2;
    Integer temps; // Temps en secondes
    // #endregion

    // #region Constructeurs
    public Relation() {
    }

    public Relation(Quai st1, Quai st2, Integer temps) {
        this.st1 = st1;
        this.st2 = st2;
        this.temps = temps;
    }
    // #endregion

    // #region Méthodes

    public String toString() {
        return "\t- St1: " + st1 + "\n\t- St2: " + st2 + "\n\t- Tps: " + temps;
    }

    public boolean compareTo(Relation relation) {
        if (this.st1 != relation.st2) {
            return false;
        }
        if (this.st2 != relation.st2) {
            return false;
        }
        if (this.temps != relation.temps) {
            return false;
        }
        return true;
    }

    // #endregion
}