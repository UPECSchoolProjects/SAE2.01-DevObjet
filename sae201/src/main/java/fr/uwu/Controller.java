package fr.uwu;

import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@SpringBootApplication
@RestController
public class Controller {

    private final ReseauMetro reseauMetro;

    public Controller(ReseauMetro reseauMetro) {
        this.reseauMetro = reseauMetro;
    }

    public static void main(String[] args) {
        SpringApplication.run(Controller.class, args);
    }

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
            sb.append("\"name\": \"" + s.idName + "\",");
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
            sb.append("\"displayName\": \"" + (stationVirt.getKey().displayName == null ? stationVirt.getKey().nom
                    : stationVirt.getKey()) + "\",");
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

}
