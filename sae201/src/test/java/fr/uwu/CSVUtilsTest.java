package fr.uwu;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.List;

import org.junit.jupiter.api.Test;

import fr.uwu.utils.CSVUtils;
import fr.uwu.utils.ReadfileIterator;

public class CSVUtilsTest {
    
    @Test
    public void testReadFileIterator() throws IOException {
        String strToTest = "123\n456\n789";
        Reader br = new StringReader(strToTest);

        ReadfileIterator rfi = new ReadfileIterator(br);

        StringBuilder sb = new StringBuilder();
        int nbLignes = 0;

        while (rfi.hasNext()) {
            String ligne = rfi.next();
            sb.append(ligne);
            if (rfi.hasNext()) {
                sb.append("\n");
            }
            nbLignes++;
        }

        rfi.close();

        assert(nbLignes == 3);
        assert(sb.toString().equals(strToTest));
    }

    @Test
    public void testReadFileWithClassLoader(){
        ClassLoader classLoader = this.getClass().getClassLoader();
        File file = new File(classLoader.getResource("lorem_ipsum.txt").getFile().replace("%20", " "));
        assert(file.exists());
    }

     @Test
    public void testReadStation() {
        List<Quai> stations = CSVUtils.readStationCSV(getClass().getClassLoader().getResource("./stations.csv").getPath().replace("%20", " "));

        Quai station1 = stations.get(0);

        System.out.println(station1);

        assert(stations.size() == 376);
        assert(station1.id == 0);
        assert(station1.nom.equals("Abbesses"));
        assert(station1.ligne.equals("12"));
        assert(station1.terminus == false);
    }

    @Test
    public void testReadRelation() {
        List<Quai> stations = CSVUtils.readStationCSV(getClass().getClassLoader().getResource("./stations.csv").getPath().replace("%20", " "));
        List<Relation> relations = CSVUtils.readRelationCSV(getClass().getClassLoader().getResource("./relations.csv").getPath().replace("%20", " "), stations);

        Relation relation1 = relations.get(0);

        // Ligne 1 du CSV : 
        // id1;id2;temps
        // 0;238;41

        assert(relation1.st1.id == 0);
        assert(relation1.st2.id == 238);
        assert(relation1.temps == 41);
    }
}
