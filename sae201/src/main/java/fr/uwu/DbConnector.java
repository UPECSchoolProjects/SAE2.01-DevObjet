package fr.uwu;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mariadb.jdbc.Connection;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StreamUtils;

public class DbConnector {
    String url;
    String username;
    String password;

    Connection connection;

    public static void main(String[] args) {
        DbConnector db = getDbFromResourceFile();
        List<Quai> stations = db.get_stations();
        for (int i = 0; i < 10; i++) {
            System.out.println(stations.get(i).getId() + " " + stations.get(i).getNom());
        }

        List<Relation> relations = db.get_relations(stations);
        for (int i = 0; i < 10; i++) {
            System.out.println(relations.get(i));
        }
    }

    public static DbConnector getDbFromResourceFile() {
        try {
            ClassPathResource resource = new ClassPathResource("db.yaml");
            InputStream inputStream = resource.getInputStream();
            String yamlContent = StreamUtils.copyToString(inputStream, Charset.defaultCharset());

            String[] lines = yamlContent.split("\n");
            Map<String, String> map = new HashMap<>();
            for (String line : lines) {
                String[] keyValue = line.split(":");
                map.put(keyValue[0].trim(), keyValue[1].trim().replace("\"", ""));
            }

            DbConnector dbMigration = new DbConnector(map.get("db_username"), map.get("db_password"),
                    map.get("db_dialect"), map.get("db_host"), map.get("db_port"), map.get("db_name"));
            return dbMigration;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public DbConnector(String username, String password, String dialect, String host, String port, String dbName) {
        this.url = "jdbc:" + dialect + "://" + host + ":" + port + "/" + dbName;
        this.username = username;
        this.password = password;

        try {
            this.connection = (Connection) DriverManager.getConnection(this.url, this.username, this.password);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Quai> get_stations() {
        ArrayList<Quai> stations = new ArrayList<>();
        try {
            String query = "SELECT * FROM stations";
            Statement statement = this.connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                if (resultSet.getBoolean("virtual")) {
                    continue;
                } else {
                    Quai newStation = new Quai(resultSet.getInt("id"), resultSet.getString("line"),
                            resultSet.getBoolean("terminus"), resultSet.getString("nom"), false);

                    newStation.setFrontProps(resultSet.getString("idName"), resultSet.getString("idfmId"),
                            resultSet.getString("displayName"), resultSet.getString("displayType"),
                            resultSet.getInt("position_x"), resultSet.getInt("position_y"));

                    stations.add(newStation);
                }
            }
            resultSet.close();
            statement.close();
            return stations;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Relation> get_relations(List<Quai> stations) {
        ArrayList<Relation> relations = new ArrayList<>();
        try {
            String query = "SELECT * FROM relations";
            Statement statement = this.connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                relations.add(new Relation(
                        Quai.getQuaiById(stations, "Q" + resultSet.getInt("id1")),
                        Quai.getQuaiById(stations, "Q" + resultSet.getInt("id2")),
                        resultSet.getInt("temps")));
            }
            resultSet.close();
            statement.close();
            return relations;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
