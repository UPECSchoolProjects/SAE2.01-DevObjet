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

    /**
     * Constructeur
     * 
     * @param st1   Station 1
     * @param st2   Station 2
     * @param temps Temps de trajet entre les deux stations
     */
    public Relation(Quai st1, Quai st2, Integer temps) {
        this.st1 = st1;
        this.st2 = st2;
        this.temps = temps;

        // Si les deux stations ont le même nom, alors c'est une correspondance
        if (st1.nom.equals(st2.nom)) {
            this.correspondance = true;
        } else {
            this.correspondance = false;
        }
    }
    // #endregion

    // #region Méthodes

    /**
     * Retourne une représentation sous forme de chaîne de caractères.
     * Cette méthode retourne les informations.
     * Comme les quais st1 et st2 qui sont reliés,
     * Le temps de trajet entre ces deux quais, formatées dans une chaîne de
     * caractères.
     * 
     * @return chaîne de caractères
     */
    public String toString() {
        return "\t- " + Couleurs.RED + "St1:" + Couleurs.RESET + " " + st1 +
                "\n\t- " + Couleurs.RED + "St2: " + Couleurs.RESET + st2 +
                "\n\t- " + Couleurs.YELLOW + "Tps: " + Couleurs.RESET + temps;
    }

    /**
     * Cette méthode retourne le quai 1.
     */
    public Quai getSt1() {
        return st1;
    }

    /**
     * Cette méthode retourne le quai 2.
     */
    public Quai getSt2() {
        return st2;
    }

    /**
     * Cette méthode retourne le temps de trajet entre les deux quais.
     * 
     * @return temps de trajet entre les deux quais
     */
    public boolean hasStation(Quai station) {
        return (this.st1 == station || this.st2 == station);
    }

    /**
     * Obtient l'autre quai relié à cette relation.
     * Cette méthode permet d'obtenir le quai qui est relié à cette relation et qui
     * est différent du quai spécifié.
     * Si le quai spécifié correspond à st1, la méthode retourne st2.
     * Si le quai spécifié correspond à st2, la méthode retourne st1.
     * Si le quai spécifié ne correspond à aucun des quais de la relation, la
     * méthode retourne null.
     * 
     * @param station Quai spécifié
     * @return L'autre quai relié à cette relation, ou null s'il n'existe pas
     */
    public Quai getOtherStation(Quai station) {
        if (this.st1 == station) {
            return this.st2;
        } else if (this.st2 == station) {
            return this.st1;
        } else {
            return null;
        }
    }

    /**
     * Compare cette relation à une autre relation spécifiée.
     * Cette méthode compare les attributs de cette relation avec ceux de la
     * relation spécifiée.
     * Si tous les attributs sont identiques, la méthode retourne true, sinon elle
     * retourne false.
     * 
     * @param relation Relation à comparer avec cette relation
     * @return True si les relations sont identiques, False sinon
     */
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

    public String toJSON() {
        return "{\"st1\": " + st1.IdLineToJSON() + ", \"st2\": " + st2.IdLineToJSON() + ", \"temps\": " + temps + "}";
    }

    public Relation reverse() {
        return new Relation(this.st2, this.st1, this.temps);
    }

    /**
     * Obtient le temps de trajet entre les deux quais.
     * 
     * @return Le temps de trajet entre les deux quais
     */
    public Integer getTemps() {
        return temps;
    }
    // #endregion
}