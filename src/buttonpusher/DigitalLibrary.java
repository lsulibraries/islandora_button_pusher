/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package buttonpusher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 *
 * @author jason
 */
public class DigitalLibrary {
    public HashMap<String, ArrayList<String>> collections;
    public DigitalLibrary() {
        this.collections = new HashMap<>();
    }
    
    public void addCollection(String collectionURL) {
        this.collections.put(collectionURL, new ArrayList<>());
    }
    
    public void addItem(String collectionURL, String itemURL) {
        this.collections.get(collectionURL).add(itemURL);
    }

    public int collectionSize(String collectionURL){
        return this.collections.get(collectionURL).size();
    }

    public ArrayList<String> getRandomCollections(int n) {
        ArrayList <String> keys = new ArrayList<>(this.collections.keySet());
        ArrayList <String> cols = new ArrayList<>();

        Random r = new Random();
        for(int i = 0; i < n; i++) {
            cols.add(keys.get(r.nextInt(keys.size())));
        }
        return cols;
    }

    public ArrayList<String> getRandomItems(String collection, int n) {
        ArrayList<String> urls = this.collections.get(collection);
        ArrayList<String> items = new ArrayList<>();

        Random r = new Random();
        for(int i = 0; i < n; i++) {
            items.add(urls.get(r.nextInt(urls.size())));
        }
        return items;
    }
}