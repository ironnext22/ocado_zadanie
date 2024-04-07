import com.ocado.basket.BasketSplitter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonReader;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BucketTest {
    BasketSplitter basketSplitter;
    @BeforeEach
    public void setUp() {
        String pathToConfig = "src/main/resources/config.json";
        basketSplitter = new BasketSplitter(pathToConfig);
    }
    @Test
    public void testSplit() {

        JsonReader reader = null;
        try {
            reader = Json.createReader(new FileReader("src/main/resources/basket-1.json"));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        JsonArray jsonArray = reader.readArray();
        List<String> stringList = new ArrayList<>();

        for (int i = 0; i < jsonArray.size(); i++) {
            stringList.add(jsonArray.getString(i));
        }

        Map<String,List<String>> map = basketSplitter.split(stringList);
        List<String> pom = map.keySet().stream().toList();

        assertEquals(map.get(pom.getFirst()).size(), 5);
        assertEquals(map.get(pom.get(1)).size(), 1);
    }
    @Test
    public void testSplit2() {

        JsonReader reader = null;
        try {
            reader = Json.createReader(new FileReader("src/main/resources/basket-2.json"));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        JsonArray jsonArray = reader.readArray();
        List<String> stringList = new ArrayList<>();

        for (int i = 0; i < jsonArray.size(); i++) {
            stringList.add(jsonArray.getString(i));
        }

        Map<String,List<String>> map = basketSplitter.split(stringList);
        List<String> pom = map.keySet().stream().toList();

        assertEquals(map.get(pom.getFirst()).size(), 3);
        assertEquals(map.get(pom.get(1)).size(), 1);
        assertEquals(map.get(pom.get(2)).size(), 13);
    }
}
