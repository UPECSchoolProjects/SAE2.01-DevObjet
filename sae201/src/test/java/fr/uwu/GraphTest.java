package fr.uwu;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class GraphTest {
    ReseauMetro reseau;

    
    @BeforeEach
    public void init() {
        System.out.println("init");
        this.reseau = new ReseauMetro();

        Quai station1 = new Quai(0, "12", false, "Abbesses", false);
        Quai station1Quai2 = new Quai(1, "11", false, "Abbesses", false);
        Quai station1Quai3 = new Quai(1, "10", false, "Abbesses", false);
        Quai station2 = new Quai(2, "12", false, "Abel", false);

        this.reseau.addStation(station1);
        this.reseau.addStation(station1Quai2);
        this.reseau.addStation(station1Quai3);
        this.reseau.addStation(station2);

        //this.reseau.addRelation(new Relation(station1, station2, 5));

        for(Relation r : this.reseau.relations) {
            System.out.println(r);
        }
    }

    @Test
    public void testAddStation() {
        System.out.println("testAddStation");
        assert(this.reseau.quais.size() == 4);
    }

/*     @Test
    public void testRegroupementStation() {
        System.out.println("testRegroupementStation");
        System.out.println(this.reseau.stations.keySet());
        assert(this.reseau.stations.keySet().size() == 2);
    } */
    
}
