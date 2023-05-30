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

    boolean correspondance;

    Integer temps; // Temps en secondes
    // #endregion

    // #region Constructeurs
    public Relation() {
    }

    public Relation(Quai st1, Quai st2, Integer temps) {
        this.st1 = st1;
        this.st2 = st2;
        this.temps = temps;

        if (st1.nom.equals(st2.nom)) {
            this.correspondance = true;
        } else {
            this.correspondance = false;
        }
    }
    // #endregion

    // #region Méthodes

    public String toString() {
        return "\t- St1: " + st1 + "\n\t- St2: " + st2 + "\n\t- Tps: " + temps;
    }

    public Quai getSt1() {
        return st1;
    }

    public Quai getSt2() {
        return st2;
    }

    public boolean hasStation(Quai station) {
        return (this.st1 == station || this.st2 == station);
    }

    public Quai getOtherStation(Quai station) {
        if (this.st1 == station) {
            return this.st2;
        } else if (this.st2 == station) {
            return this.st1;
        } else {
            return null;
        }
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