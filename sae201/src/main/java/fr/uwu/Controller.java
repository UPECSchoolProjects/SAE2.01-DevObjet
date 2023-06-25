package fr.uwu;

import org.springframework.web.bind.annotation.RestController;

import fr.uwu.ReseauMetro.TrajectPreference;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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
     * 
     * @param args Arguments de la ligne de commande
     */
    public static void main(String[] args) {
        SpringApplication.run(Controller.class, args);
    }

    /**
     * Permet d'obtenir un chemin entre deux quais.
     * Ce point d'extrémité permet d'obtenir un chemin optimal entre deux quais
     * spécifiés en utilisant leurs identifiants.
     * Le résultat est renvoyé au format JSON, représentant le chemin entre les deux
     * quais.
     * 
     * @param startID Identifiant du quai de départ
     * @param endID   Identifiant du quai d'arrivée
     * @return JSON représentant le chemin entre les deux quais
     */
    @GetMapping("/path")
    @CrossOrigin(origins = "*")
    public String path(@RequestParam("stations") String stationsStr, @RequestParam("preference") String preference) {
        List<Quai> stationsList = new ArrayList<Quai>(reseauMetro.quais);

        // on ajoute les stations virtuelles
        stationsList.addAll(reseauMetro.stations.keySet());

        System.out.println("Nombre de stations: " + stationsList.size());

        String[] points = stationsStr.split(",");

        if (points.length < 2) {
            return "Nombre de points incorrect";
        }

        if(preference == null || preference.isEmpty()) {
            preference = "temps";
        }

        if(!preference.equals("temps") && !preference.equals("correspondance")) {
            return "Préférence incorrecte";
        }

        TrajectPreference pref = preference.equals("temps") ? TrajectPreference.TEMPS : TrajectPreference.CORRESPONDANCE;

        StringBuilder sb2 = new StringBuilder();
        sb2.append("Stations: ");
        for (String point : points) {
            sb2.append(point + ", ");
        }
        System.out.println(sb2.toString());
    

        ArrayList<Quai> quais = new ArrayList<Quai>();

        for (String point : points) {
            Quai quai = Quai.getQuaiById(stationsList, point);
            if (quai == null) {
                return "Station non trouvée " + point;
            }
            quais.add(quai);
        }

        List<Relation> path = reseauMetro.trajetEntrePlusieursStation(quais, pref);

        if (path == null) {
            return "Aucun chemin trouvé";
        }

        if(path.size() == 0) {
            return "Erreur";
        }

        StringBuilder pathSb = new StringBuilder();
        pathSb.append("Path: ");
        for (Relation r : path) {
            pathSb.append(r.getSt1().getId() + "\n -> " + r.getSt2().getId() + ", ");
        }

        System.out.println(pathSb.toString());


        List<Quai> stations = ReseauMetro.convertRelationPathToStationPath(path, quais.get(quais.size() - 1), quais.get(0));

        // to json
        StringBuilder sb = new StringBuilder();
        sb.append("{\"stationsInPath\": [");
        for (Quai s : stations) {
            sb.append("\"" + s.getId() + "\",");
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append("]");

        sb.append(",\"path\": [");

        String id_last = null;

        for (Relation r : path) {
            // si la relation est "dans le mauvais sens" on la retourne
            if (id_last != null && !r.getSt1().getId().equals(id_last)) {
                r = r.reverse();
            }
            sb.append(r.toJSON());
            sb.append(",");
            id_last = r.getSt2().getId();
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append("]");
        sb.append("}");

        // add cors header

        return sb.toString();
    }

    @GetMapping(value = "/stations", produces = "application/json;charset=UTF-8")
    @CrossOrigin(origins = "*")
    public String stations() {
        StringBuilder sb = new StringBuilder();
        sb.append("{\"stations\": [");
        // id name
        for (Quai s : reseauMetro.quais) {
            sb.append("{");
            sb.append("\"virtual\":");

            if (s.virtuel) {
                continue;
            }

            sb.append("false,");

            sb.append("\"content\":");

            sb.append("{");

            sb.append("\"id\": \"" + s.getId() + "\",");
            sb.append("\"name\": \"" + s.nom + "\",");
            sb.append("\"nameFront\": \"" + s.idName + "\",");
            sb.append("\"displayName\": \"" + s.displayName + "\",");
            sb.append("\"displayType\": \"" + s.displayType + "\",");
            sb.append("\"line\": \"" + s.ligne + "\",");
            sb.append("\"x\": " + s.posX + ",");
            sb.append("\"y\": " + s.posY + "");

            sb.append("}");

            sb.append("},");
        }

        for (Map.Entry<Quai, Set<Quai>> stationVirt : reseauMetro.stations.entrySet()) {
            sb.append("{");
            sb.append("\"virtual\":");

            sb.append("true,");

            sb.append("\"content\":");

            sb.append("{");

            sb.append("\"id\": \"" + stationVirt.getKey().getId() + "\",");
            sb.append("\"name\": \"" + stationVirt.getKey().nom + "\",");
            sb.append("\"nameFront\": \"" + stationVirt.getKey().idName + "\",");
            sb.append("\"displayName\": \"" + (stationVirt.getKey().displayName == null ? stationVirt.getKey().nom
                    : stationVirt.getKey().displayName) + "\",");
            sb.append("\"lignes\": [");
            Set<Quai> lignes = stationVirt.getValue();
            if (lignes != null) {
                for (Quai ligne : lignes) {
                    sb.append("\"" + ligne.ligne + "\",");
                }
                sb.deleteCharAt(sb.length() - 1);
            }
            sb.append("]");
            sb.append("}");
            sb.append("},");
        }

        sb.deleteCharAt(sb.length() - 1);
        sb.append("]}");

        return sb.toString();
    }

    @GetMapping(value = "/acm", produces = "application/json;charset=UTF-8")
    @CrossOrigin(origins = "*")
    public String acm() {
        List<Relation> rel_acm = reseauMetro.ACM();

        List<Quai> stations = rel_acm.stream().map(r -> r.getSt1()).distinct().collect(Collectors.toList());
        stations.addAll(rel_acm.stream().map(r -> r.getSt2()).distinct().collect(Collectors.toList()));

        StringBuilder sb = new StringBuilder();
        sb.append("{\"stations\": [");
        // id name
        for (Quai s : stations) {
            // id
            sb.append("\"" + s.getId() + "\",");
        }

        sb.deleteCharAt(sb.length() - 1);
        sb.append("],");

        sb.append("\"path\": [");

        for (Relation r : rel_acm) {
            // si la relation est "dans le mauvais sens" on la retourne
            sb.append(r.toJSON());
            sb.append(",");
        }

        sb.deleteCharAt(sb.length() - 1);
        sb.append("]");
        sb.append("}");

        return sb.toString();
    }

}
