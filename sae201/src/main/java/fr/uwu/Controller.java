package fr.uwu;

import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@SpringBootApplication
@RestController
public class Controller {

    private final ReseauMetro reseauMetro;

    /**
     * Constructeur
    */
    public Controller(ReseauMetro reseauMetro) {
        this.reseauMetro = reseauMetro;
    }

    /**
     * Point d'entrée de l'application
     * @param args Arguments de la ligne de commande
    */
    public static void main(String[] args) {
        SpringApplication.run(Controller.class, args);
    }

    /**
     * Permet d'obtenir un chemin entre deux quais.
     * Ce point d'extrémité permet d'obtenir un chemin optimal entre deux quais spécifiés en utilisant leurs identifiants.
     * Le résultat est renvoyé au format JSON, représentant le chemin entre les deux quais.
     * @param startID Identifiant du quai de départ
     * @param endID Identifiant du quai d'arrivée
     * @return JSON représentant le chemin entre les deux quais
    */
    @GetMapping("/path")
    @CrossOrigin(origins = "*")
    public String hello(@RequestParam("start") String startID, @RequestParam("end") String endID) {
        
        Quai start = Quai.getQuaiById(reseauMetro.quais, startID);
        Quai end = Quai.getQuaiById(reseauMetro.quais, endID);

        if (start == null || end == null) {
            return "Station non trouvée";
        }

        List<Relation> path = reseauMetro.dijkstra_algo(start, end);
        
        if (path == null) {
            return "Aucun chemin trouvé";
        }

        List<Quai> stations = ReseauMetro.convertRelationPathToStationPath(path, start, end);

        // reverse
        for (int i = 0; i < stations.size() / 2; i++) {
            Quai temp = stations.get(i);
            stations.set(i, stations.get(stations.size() - i - 1));
            stations.set(stations.size() - i - 1, temp);
        }

        // to json
        StringBuilder sb = new StringBuilder();
        sb.append("{\"path\": [");
        for (Quai s : stations) {
            sb.append("\"" + s.getId() + "\",");
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append("]}");

        // add cors header

        return sb.toString();
    }

}
