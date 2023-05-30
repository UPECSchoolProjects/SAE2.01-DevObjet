package fr.uwu;

import java.util.List;

/**
 * Represente une station de métro.
 * C'est le noeud du graphe.
 */
public class Quai {

    private static int idCounter = 0;

    public static Quai getQuaiById(List<Quai> quais, String id) {
        return quais.stream().filter(quai -> quai.getId().equals(id)).findFirst().orElse(null);
    }

    //#region Variables
    // TODO : Getters and Setters si besoin
    private Integer id;
    String ligne;
    boolean terminus;
    String nom;
    boolean virtuel;
    //#endregion

    //#region Constructeurs

    public Quai(Integer id, String ligne, Boolean terminus, String nom, boolean virtuel) {
        this.id = id;
        this.ligne = ligne;
        this.terminus = terminus;
        this.nom = nom;
        this.virtuel = virtuel;
    }


    public Quai(Boolean terminus, String nom) {
        this.id = idCounter++;
        this.ligne = "VIRT";
        this.terminus = terminus;
        this.virtuel = true;
        this.nom = nom;
    }
    //#endregion

    //#region Méthodes


    public List<Relation> getRelations(List<Relation> relations) {
        return relations.stream().filter(relation -> relation.st1.id == this.id || relation.st2.id == this.id).toList();
    }

    public String toString() {
        return "Id: " + this.getId() + ", Ligne: " + ligne + ", Terminus: " + terminus + ", Nom: " + nom;
    }

    public Boolean isTerminus(){
        if (this.terminus == true){
            return true;
        }
        else {
            return false;
        }
    }

    public String getNom() {
        return nom;
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

    public String getLigne() {
        return ligne;
    }

    public String getId() {
        return (this.virtuel ? "V" : "Q") + id.toString();
    }

    //#endregion

}