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

    /**
     * Constructeur
     * @param id Identifiant de la station
     * @param ligne Ligne de la station
     * @param terminus Si la station est un terminus
     * @param nom Nom de la station
    */
    public Quai(Integer id, String ligne, Boolean terminus, String nom, boolean virtuel) {
        this.id = id;
        this.ligne = ligne;
        this.terminus = terminus;
        this.nom = nom;
        this.virtuel = virtuel;
    }

    /**
     * Constructeur
     * @param terminus Si la station est un terminus
     * @param nom Nom de la station
    */
    public Quai(Boolean terminus, String nom) {
        this.id = idCounter++;
        this.ligne = "VIRT";
        this.terminus = terminus;
        this.virtuel = true;
        this.nom = nom;
    }


    /**
     * Obtient les relations associées à ce quai.
     * Cette méthode filtre la liste des relations en ne conservant que celles qui sont connectées à ce quai.
     * @param relations Liste des relations à filtrer
     * @return Liste des relations associées à ce quai
    */
    public List<Relation> getRelations(List<Relation> relations) {
        return relations.stream().filter(relation -> relation.st1.id == this.id || relation.st2.id == this.id).toList();
    }

    /**
     * Retourne une représentation sous forme de chaîne de caractères de l'objet Quai.
     * Cette méthode retourne les informations de l'objet Quai, telles que son identifiant, sa ligne, son terminus et son nom, 
     * sous forme de chaîne de caractères.
     * @return Représentation sous forme de chaîne de caractères de l'objet Quai
    */
    public String toString() {
        return "Id: " + this.getId() + ", Ligne: " + ligne + ", Terminus: " + terminus + ", Nom: " + nom;
    }

    /**
     * Vérifie si ce quai est un terminus.
     * Cette méthode vérifie si le quai est marqué comme terminus.
     * @return True si le quai est un terminus, False sinon
    */
    public Boolean isTerminus(){
        if (this.terminus == true){
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Obtient le nom du quai.
     * @return Le nom du quai
    */
    public String getNom() {
        return nom;
    }

    
    /**
     * Compare ce quai à un autre quai spécifié.
     * Cette méthode compare les attributs de ce quai avec ceux du quai spécifié. Si tous les attributs sont identiques,
     * la méthode retourne true, sinon elle retourne false.
     * @param station Quai à comparer avec ce quai
     * @return True si les quais sont identiques, False sinon
    */
    public boolean compareTo(Quai station) {
        // Comparaison de l'identifiant de la station
        if (this.id != station.id) {
            return false;
        }
        // Comparaison de la ligne de la station
        if (this.ligne != station.ligne) {
            return false;
        }
        // Comparaison du terminus de la station
        if (this.terminus != station.terminus) {
            return false;
        }
        // Comparaison du nom de la station
        if (this.nom != station.nom) {
            return false;
        }
        return true;
    }

    /**
     * Obtient l'identifiant du quai.
     * @return L'identifiant du quai
    */
    public String getLigne() {
        return ligne;
    }

    /**
     * Obtient l'identifiant du quai.
     * Cette méthode retourne l'identifiant du quai, représenté sous forme d'une chaîne de caractères.
     * L'identifiant est précédé par "V" si le quai est virtuel, ou par "Q" s'il est physique.
     * @return L'identifiant du quai
     */
    public String getId() {
        return (this.virtuel ? "V" : "Q") + id.toString();
    }

    /**
     * Obtient l'identifiant du quai.
     * Cette méthode retourne l'identifiant du quai, représenté sous forme d'un entier.
     * @return L'identifiant du quai
    */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Quai) {
            Quai station = (Quai) obj;
            return this.id == station.id;
        }
        return false;
    }

    //#endregion

}