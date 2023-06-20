package fr.uwu.utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import fr.uwu.Relation;
import fr.uwu.Quai;

public class CSVUtils {

    /**
     * Lis un fichier CSV et retourne une liste d'objets de type T (T dépénd de ce qui est renvoyé par la fonction convert) en utilisant une fonction de conversion fournie.
     * 
     * En gros, elle reprends la strucutre d'un code qui va parcourir le CSV mais en laissant le choix de ce qu'on fait avec chaque ligne.
     * et du type d'objet qu'on veut renvoyer. (voir https://www.baeldung.com/java-generics et https://www.geeksforgeeks.org/function-interface-in-java-with-examples/)
     * 
     * La synraxe Function<String[], T> signifie que la fonction prends un tableau de String en argument et renvoie un objet de type T. (Function<argument, résultat>)
     * 
     * @param <T> type de l'objet à renvoyer (définis automatiquement par java en fonction de ce que renvoie la fonction convert)
     * @param filename nom du fichier qui contient le CSV
     * @param convert fonction qui convertit une ligne du CSV en objet de type T
     * @return liste d'objets de type T
     */
    private static <T> List<T> readCSV(String filename, Function<String[], T> convert) {
        List<T> catalogue = new ArrayList<>(); // liste d'objets de type T à renvoyer

        ReadfileIterator it = null; // itérateur de String qui va parcourir le fichier ligne par ligne

        try {
            // ReadfileIterator est une classe qui simplifie la lecture d'un fichier ligne par ligne.
            it = new ReadfileIterator(filename); // ouverture du fichier (instanciation de l'itérateur)
            if (!it.hasNext()) { // si le fichier est vide
                System.out.println("Erreur: fichier vide");
                return catalogue;
            }
    
            // skip nom des colonnes
            it.next();
    
            while (it.hasNext()) { // tant qu'il reste des lignes à lire

                /*
                 * Ici on applique la fonction passée en argument à chaque ligne du CSV et 
                 * on ajoute le résultat à la liste d'objets à renvoyer
                 */
                String[] lignesplit = it.next().split(";");
                catalogue.add(convert.apply(lignesplit));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // fermeture du fichier
            if (it != null) { // si le fichier a été ouvert
                it.close(); // on le ferme
            }
        }
        return catalogue;
    }

    /**
     * Lis le fichier CSV stations.csv et retourne une liste d'objets de type Station
     * 
     * Utilise la fonction readCSV avec une fonction de conversion qui convertit une ligne du CSV en objet de type Station (ici le fameux <T> est égal à Station)
     * 
     * Donc dans la fonction readCSV, la fonction convert est de type Function<String[], Station>
     * et le type de retour de readCSV est List<Station>
     * 
     * @see Quai
     * @param filename Chemin du fichier CSV
     * @return liste d'objets de type Station à partir du fichier CSV stations.csv
     */
    public static List<Quai> readStationCSV(String filename) {
        filename = filename != null ? filename : "CSV/stations.csv";

        // (linesplit) -> {} est la fonction de conversion (type Function<String[], Station>) qui convertit une ligne du CSV en objet de type Station. (Elle prends en argument un tableau de String et renvoie un objet de type Station)
        return readCSV(filename, (lignesplit) -> {
            return new Quai(
                Integer.parseInt(lignesplit[0]),
                lignesplit[1],
                lignesplit[2].equals("1"),
                lignesplit[3],
                false);
        });
    }

    /**
     *  Lis le fichier CSV relations.csv et retourne une liste d'objets de type Relation
     * 
     *  Utilise la fonction readCSV avec une fonction de conversion qui convertit une ligne du CSV en objet de type Relation (ici le fameux <T> est égal à Relation)
     *  
     * Donc dans la fonction readCSV, la fonction convert est de type Function<String[], Relation>
     * et le type de retour de readCSV est List<Relation>
     * 
     * @see Relation
     * @param filename Chemin du fichier CSV
     * @return liste d'objets de type Relation à partir du fichier CSV relations.csv
     */
    public static List<Relation> readRelationCSV(String filename, List<Quai> stations) {
        filename = filename != null ? filename : "CSV/relations.csv";

        return readCSV(filename, (lignesplit) -> {
            return new Relation(
                Quai.getQuaiById(stations, "Q" + Integer.parseInt(lignesplit[0])),
                Quai.getQuaiById(stations, "Q" + Integer.parseInt(lignesplit[1])),
                Integer.parseInt(lignesplit[2]));
        });
    }

}
