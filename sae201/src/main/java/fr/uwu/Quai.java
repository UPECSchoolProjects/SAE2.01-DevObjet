package fr.uwu;

import java.util.List;

/**
 * Represente une station de métro.
 * C'est le noeud du graphe.
 */
public class Quai {

    public static Quai getQuaiById(List<Quai> quais, int id) {
        return quais.stream().filter(quai -> quai.id == id).findFirst().orElse(null);
    }

    //#region Variables
    // TODO : Getters and Setters si besoin
    Integer id;
    String ligne;
    boolean terminus;
    String nom;
    //#endregion

    //#region Constructeurs
    public Quai() {
    }

    public Quai(Integer id, String ligne, Boolean terminus, String nom) {
        this.id = id;
        this.ligne = ligne;
        this.terminus = terminus;
        this.nom = nom;
    }
    //#endregion

    //#region Méthodes


    public List<Relation> getRelations(List<Relation> relations) {
        return relations.stream().filter(relation -> relation.st1.id == this.id || relation.st2.id == this.id).toList();
    }

    public String toString() {
        return "Id: " + id + ", Ligne: " + ligne + ", Terminus: " + terminus + ", Nom: " + nom;
    }

    public boolean compareTo(Quai station) {
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

    public String getNom() {
        return nom;
    }

    public String getLigne() {
        return ligne;
    }

    //#endregion

}