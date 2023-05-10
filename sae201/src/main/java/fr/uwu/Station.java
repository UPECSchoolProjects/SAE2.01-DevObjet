package fr.uwu;

public class Station {

    // Variables
    Integer id;
    String ligne;
    Integer terminus;
    String nom;

    // Constructeurs
    public Station() {
    }

    public Station(Integer id, String ligne, Integer terminus, String nom) {
        this.id = id;
        this.ligne = ligne;
        this.terminus = terminus;
        this.nom = nom;
    }

    public String toString() {
        return "Id: " + id + " Ligne: " + ligne + " Terminus: " + terminus + " Nom: " + nom;
    }
}