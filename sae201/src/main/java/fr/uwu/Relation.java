package fr.uwu;

public class Relation {

    // Variables
    Integer id1;
    Integer id2;
    Integer temps;

    // Constructeurs
    public Relation() {
    }

    public Relation(Integer id1, Integer id2, Integer temps) {
        this.id1 = id1;
        this.id2 = id2;
        this.temps = temps;
    }

    public String toString() {
        return "Id1: " + id1 + " Id2: " + id2 + " Tps: " + temps;
    }
}
