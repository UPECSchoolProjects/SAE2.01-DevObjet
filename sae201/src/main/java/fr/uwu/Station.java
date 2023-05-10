package fr.uwu;

public class Station {

    // Variables
    Integer id;
    String ligne;
    boolean terminus;
    String nom;

    // Constructeurs
    public Station() {
    }

    public Station(Integer id, String ligne, Boolean terminus, String nom) {
        this.id = id;
        this.ligne = ligne;
        this.terminus = terminus;
        this.nom = nom;
    }

    public String toString() {
        return "Id: " + id + " Ligne: " + ligne + " Terminus: " + terminus + " Nom: " + nom;
    }

    public boolean compareTo(Station station) {
        if (this.id != station.id) {
            return false;
        }
        if (this.ligne != station.ligne) {
            return false;
        }
        if (this.terminus != station.terminus) {
            return false;
        }
        if (this.nom != station.nom) {
            return false;
        }
        return true;
    }
}