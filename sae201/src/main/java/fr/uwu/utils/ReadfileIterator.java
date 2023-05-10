package fr.uwu.utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;

/**
 * ReadfileIterator est une classe qui simplifie la lecture d'un fichier ligne par ligne.
 * Elle s'occupe de l'ouverture et de la fermeture du fichier. et renvoie juste un itérateur de String.
 * 
 * Elle peut être instanciée avec un flux entrant ou juste un nom de fichier.
 * 
 * Un itérateur est un objet qui permet de parcourir une collection d'objets. il possède deux méthodes:
 * - hasNext() qui renvoie true s'il reste des éléments à parcourir
 * - next() qui renvoie l'élément suivant
 * 
 * Exemple d'utilisation:
 *  ReadfileIterator it = new ReadfileIterator("fichier.txt");
 *  while (it.hasNext()) {
 *     String ligne = it.next();
 *     ...traitement de la ligne
 *  }
 *  it.close();
 * 
 *  ! Attention: il faut fermer l'itérateur à la fin avec la méthode close()
 */
public class ReadfileIterator implements Iterator<String>, AutoCloseable {

    // Attributs
    Reader reader; // flux entrant
    BufferedReader br; // buffer pour lire le fichier ligne par ligne
    String nextline; // prochaine ligne à envoyer. permet de savoir si on est à la fin du fichier pour la méthode hasNext()
    // Nextline à toujours une ligne d'avance sur ce qui est renvoyé par next().

    // Constructeurs

    /**
     * Constructeur avec un flux entrant
     * @param reader flux entrant
     * @throws IOException si erreur d'entrée/sortie
     */
    public ReadfileIterator(Reader reader) throws IOException {
        this.reader = reader;
        open();
    }

    /**
     * Constructeur avec un nom de fichier. Il ouvre le fichier et crée un flux entrant.
     * @param filename nom du fichier
     * @throws FileNotFoundException si le fichier n'existe pas
     * @throws IOException si erreur d'entrée/sortie
     */
    public ReadfileIterator(String filename) throws FileNotFoundException, IOException {
        this.reader = new FileReader(filename);
        open();
    }

    /**
     * Méthode privée qui initialise le buffer et récupère la première ligne du fichier
     * @throws IOException si erreur d'entrée/sortie
     */
    private void open() throws IOException{
        this.br = new BufferedReader(reader);
        nextline = br.readLine();
    }

    // Méthodes

    /**
     * Méthode implémentée de l'interface Iterator
     * @return true s'il reste des lignes à lire
     */
    @Override
    public boolean hasNext() {
        return nextline != null;
    }

    /**
     * Méthode implémentée de l'interface Iterator
     * @return la prochaine ligne du fichier ou null si on est à la fin du fichier (ou en cas d'erreur)
     */
    @Override
    public String next() {
        String tmp = this.nextline;
 
        try {
            this.nextline = br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return tmp;
    }

    /**
     * Méthode qui permet de fermer le fichier.
     * 
     * Elle est appelée automatiquement si on utilise l'instruction try-with-resources (implémentation de l'interface AutoCloseable)
     * 
     * ! N'est pas appelée automatiquement, il faut le faire manuellement !
     */
    @Override
    public void close() {
        try {
            this.br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
