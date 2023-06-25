package fr.uwu;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class VerificationConnexe {
    ReseauMetro reseau;

    @BeforeEach
    public void init() {
        DbConnector db = DbConnector.getDbFromResourceFile();
        List<Quai> stations = db.get_stations();
        List<Relation> relations = db.get_relations(stations);

        System.out.println("Nombre de relations: " + relations.size());

        ReseauMetro reseau = new ReseauMetro(new ArrayList<Quai>(stations), new ArrayList<Relation>(relations));

        this.reseau = reseau;
    }

    @Test
    public void testdeConnexite() {
        System.out.println("testdeConnexite");
        assert (this.reseau.verificationConnexe() == true);
    }
}
