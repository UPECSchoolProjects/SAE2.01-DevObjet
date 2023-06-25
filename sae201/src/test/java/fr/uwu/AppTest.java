package fr.uwu;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test des classes Relation, Quai et ReseauMetro
 */
public class AppTest {

    Quai quai1;
    Quai quai2;
    Quai quai4;
    Quai quai5;
    Quai quai6;
    Quai quai7;

    Relation rel1;
    Relation rel2;
    Relation rel3;
    Relation rel4;
    Relation rel5;

    ArrayList<Quai> stations;
    ArrayList<Relation> relations;

    ReseauMetro reseau1;
    ReseauMetro reseau2;

    @BeforeEach
    public void init() {

        // ^ Variables

        // Quai
        this.quai1 = new Quai(1, "L1", false, "Quai1", false);
        this.quai2 = new Quai(2, "L2", true, "Quai2", false);
        this.quai4 = new Quai(3, "L3", false, "Quai4", false);
        this.quai5 = new Quai(4, "L2", false, "Quai5", false);
        this.quai6 = new Quai(5, "L3", false, "Quai6", false);
        this.quai7 = new Quai(6, "L4", true, "Quai7", false);

        // Relation
        this.rel1 = new Relation(quai1, quai4, 20);
        this.rel2 = new Relation();
        this.rel3 = new Relation(quai1, quai2, 10);
        this.rel4 = new Relation(quai5, quai6, 15);
        this.rel5 = new Relation();

        // ArrayList de stations
        this.stations = new ArrayList<Quai>();
        this.stations.add(quai1);
        this.stations.add(quai2);
        this.stations.add(quai4);
        this.stations.add(quai5);
        this.stations.add(quai6);
        this.stations.add(quai7);

        // ArrayList de relations
        this.relations = new ArrayList<Relation>();
        this.relations.add(rel1);
        this.relations.add(rel3);
        this.relations.add(rel4);

        // ReseauMetro
        this.reseau1 = new ReseauMetro();
        this.reseau1.addStation(quai1);
        this.reseau1.addStation(quai2);
        this.reseau1.addRelation(rel1);

        this.reseau2 = new ReseauMetro(stations, relations);
    }

    // ? Relation - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    @Test
    public void testRelationGetStation() {
        assert (this.rel3.getSt1().equals(this.quai1));
        assert (this.rel3.getSt2().equals(this.quai2));
    }

    @Test
    public void testRelationGetOtherStation() {
        assert (this.rel3.getOtherStation(this.quai1).equals(this.quai2));
        assert (this.rel3.getOtherStation(this.quai2).equals(this.quai1));
    }

    @Test
    public void testRelationGetTemps() {
        assert (this.rel3.getTemps() == 10);
    }

    @Test
    public void testRelationCompareTo() {
        assert (this.rel1.compareTo(this.rel3) == false);
        assert (this.rel5.compareTo(this.rel2) == true);
    }

    @Test
    public void testRelationHasStation() {
        assert (this.rel3.hasStation(this.quai1) == true);
        assert (this.rel3.hasStation(this.quai2) == true);
    }

    @Test
    public void testRelationToJson() {
        assert (this.rel3.toJSON().length() == 79);
    }

    @Test
    public void testRelationToString() {
        assert (this.rel1.toString().length() == 241);
        assert (this.rel3.toString().length() == 240);
    }

    @Test
    public void testRelationReverse() {
        assert (this.rel3.reverse().getSt1().equals(this.quai2));
    }

    // ? Quai - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    @Test
    public void testQuaiGetRelation() {
        Relation rel = new Relation(this.quai6, this.quai7, null);
        List<Relation> listrelations = new ArrayList<Relation>();
        listrelations.add(rel);

        assert (this.quai7.getRelations(listrelations).equals(listrelations));
    }

    @Test
    public void testQuaiGetNom() {
        assert (this.quai1.getNom().equals("Quai1"));
        assert (this.quai2.getNom().equals("Quai2"));
    }

    @Test
    public void testQuaiGetLigne() {
        assert (this.quai1.getLigne().equals("L1"));
        assert (this.quai2.getLigne().equals("L2"));
    }

    @Test
    public void testQuaiGetId() {
        assert (this.quai1.getId().equals("Q1"));
        assert (this.quai2.getId().equals("Q2"));
    }

    @Test
    public void testQuaiToString() {
        assert (this.quai1.toString().length() > 0);
        assert (this.quai2.toString().length() > 0);
    }

    @Test
    public void testQuaiIdLigneToJSON() {
        assert (this.quai1.IdLineToJSON().length() > 0);
    }

    @Test
    public void testQuaiIsTerminus() {
        assert (this.quai1.isTerminus() == false);
        assert (this.quai2.isTerminus() == true);
    }

    @Test
    public void testQuaiCompareTo() {
        assert (this.quai1.compareTo(this.quai2) == false);
        assert (this.quai1.compareTo(this.quai1) == true);
    }

    @Test
    public void testQuaiEquaks() {
        assert (this.quai1.equals(this.quai2) == false);
    }

    // ? ReseauMetro - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    @Test
    public void testReseauMetroCalculerTemps() {
        assert (ReseauMetro.calculertemps(relations) == 45);
    }

    @Test
    public void testReseauMetrocorrespondanceEntre2Lignes() {
        assert (this.reseau2.correspondanceEntre2Lignes("L2", "L3").equals("VIRT\nVIRT"));
        assert (this.reseau2.correspondanceEntre2Lignes("L1",
                "L4") == "Il n'y a pas de correspondances entre les lignes spécifiées.");
    }

    @Test
    public void testReseauMetrogetStationByName() {
        Set<Quai> SetQ1 = new HashSet<Quai>();
        SetQ1.add(this.quai1);

        assert (this.reseau1.getStationByName("Quai1").equals(SetQ1));
    }

    @Test
    public void testReseauMetroHasStationVIRT() {
        assert (this.reseau2.stations.keySet().size() == 2);
    }

    @Test
    public void testReseauMetrotrajetEntrePlusieursStation() {

        // Ici, juste faire appel à la méthode permet de faire le test
        this.reseau2.dijkstra_algo(this.quai1, this.quai2, null);
    }

}