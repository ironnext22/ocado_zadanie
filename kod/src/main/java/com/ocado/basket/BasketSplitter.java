package com.ocado.basket;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import java.io.FileReader;
import java.util.*;
import java.util.stream.Collectors;

public class BasketSplitter {
    private Map<String,List<String>> config;
    //loading the configuration file
    public BasketSplitter(String absolutPathToConfigFile) {
        try (Jsonb jsonb = JsonbBuilder.create()) {
            config = jsonb.fromJson(new FileReader(absolutPathToConfigFile), new HashMap<String, List<String>>() {
            }.getClass().getGenericSuperclass());
        }
        catch (Exception e) {e.printStackTrace();}

    }

    public Map<String, List<String>> split(List<String> items){
        Map<String, List<String>> result = new HashMap<>();
        List<String> pom = new ArrayList<>(items);
        List<String> items_to_delete = new ArrayList<>();

        //Adding products with one delivery method
        for (String item : pom) {
            List<String> p = config.get(item);
            if(p.size() == 1){
                if(result.containsKey(p.getFirst())){
                        result.get(p.getFirst()).add(item);
                }
                else {
                    List<String> newList = new ArrayList<>();
                    newList.add(item);
                    result.put(p.getFirst(), newList);
                }
                items_to_delete.add(item);
            }
        }
        //removing added products from list of items to add
        for(String i : items_to_delete){
            pom.remove(i);
        }
        //adding the rest of the items
        for (String item : pom) {
            List<String> p = config.get(item);
            boolean found = false;
            // adding items to existing method, if possible
                for(String p1 : p){
                    if(result.containsKey(p1)){
                            result.get(p1).add(item);
                            found = true;
                            break;
                    }
                }
                // adding new method, if needed
                if(!found){
                    int maxMatches = 0;
                    String bestDeliveryMethod = null;
                    Map<String, Integer> pom_map = new HashMap<>();
                    for(String i2 : pom){
                        List<String> pom_list = config.get(i2);
                        for(String p1 : pom_list){
                            if(pom_map.containsKey(p1)){
                                pom_map.put(p1,pom_map.get(p1)+1);
                            }
                            else{
                                pom_map.put(p1,1);
                            }
                        }
                    }
                    for(String i : result.keySet()){
                        pom_map.remove(i);
                    }
                    for(Map.Entry<String, Integer> px : pom_map.entrySet()){
                        if(px.getValue() > maxMatches){
                            maxMatches = px.getValue();
                            bestDeliveryMethod = px.getKey();
                        }
                    }
                    List<String> newList = new ArrayList<>();
                    newList.add(item);
                    if(bestDeliveryMethod != null){
                        result.put(bestDeliveryMethod,newList);
                    }
                    else{
                        result.put(pom.getFirst(),newList);
                    }
                }
        }

        // optimizing deliveries
        Map<String,Integer> map = new HashMap<>();
        for(Map.Entry<String, List<String>> entry : result.entrySet()){
            map.put(entry.getKey(),entry.getValue().size());
        }
        map = map.entrySet()
                .stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));

        Map<String,List<String>> pom_map = new HashMap<>(result);
        for(Map.Entry<String,Integer> entry : map.entrySet()){
            pom_map.remove(entry.getKey());
            for(Map.Entry<String,List<String>> entry1 : pom_map.entrySet()){
                Iterator<String> iterator = entry1.getValue().iterator();
                while(iterator.hasNext()){
                    String i2 = iterator.next();
                    if(config.get(i2).contains(entry.getKey())){
                        result.get(entry.getKey()).add(i2);
                        iterator.remove();
                    }
                }
            }
        }

        return result;
    }
}
