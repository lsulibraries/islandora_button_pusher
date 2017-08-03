/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package buttonpusher;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

/**
 *
 * @author jason
 */
public class ButtonPusher {

    WebDriver driver;
    String host;
    private String root = "/islandora";
    private String repositoryRoot;
    DigitalLibrary dlib;
    private int lastPage;

    public ArrayList<String> populateDigitalLibraryData() {
        int n = 2;
        this.getRandomCollectionURLs(n);
        ArrayList<String> populatedCollections = new ArrayList<>();

        for(String collection : this.dlib.getRandomCollections(n)) {
            ArrayList<String> items = this.getItemUrls(collection);
            this.dlib.collections.get(collection).items.addAll(items);
            populatedCollections.add(collection);
        }
        return populatedCollections;
    }

    private List<String> getRandomCollectionURLs(int n) {
        List<String> collections = new ArrayList<>();
        for(int i = 0; i < n; i++) {
            String rPage = this.getRandomPageURL("islandora:root");
            String rColl = this.getRandomCollectionURL(rPage);
        }
        return collections;
    }
    
    private String getRandomCollectionURL(String pageURL) {
        List<String> collectionsOnPage = this.getCollections(pageURL);
        Random r = new Random();
        return collectionsOnPage.get(r.nextInt(collectionsOnPage.size()));
    }

    public ButtonPusher() {
        System.setProperty("webdriver.gecko.driver", "/usr/local/bin/geckodriver");

        WebDriver driver = new FirefoxDriver();
        this.driver = driver;
        
        String host = "ldl.lib.lsu.edu";
//        String host = "test.louisianadigitallibrary.org";
//        String host = "lib-ti012.lsu.edu:8000";
        this.host = host;
        this.repositoryRoot = "http://" + this.host + this.root;

        this.dlib = new DigitalLibrary();
    }

    private void testSearch(List<String> searchTerms) {
        for (String term : searchTerms) {
            driver.get("http://" + host + "/islandora/search/" + term + "?type=dismax");
        }
    }

    private List<String> getCollections(String collectionListingURL) {
        driver.get(collectionListingURL);
        List<WebElement> collectionURLs = driver.findElements(By.xpath("//div[@class=\"islandora-basic-collection-wrapper\"]//dd/a"));
        List<String> urls = new ArrayList<String>();
        for (WebElement a : collectionURLs) {
            String url = a.getAttribute("href");
            urls.add(url);
            this.dlib.addCollection(url);
        }
        return urls;
    }

    private List<String> getAllCollections(){
        ArrayList<String> allCollections = new ArrayList<>();
        String collectionRoot = this.host + this.root;
        int lastPage = this.lastPage(collectionRoot);
        for (int i = 0; i < lastPage; i++) {
            String pageURL = "http://" + host + root + "/object/islandora%3Aroot?page=" + i;
            List<String> collections = getCollections(pageURL);
            allCollections.addAll(collections);
        }
        return allCollections;
    }

    private int lastPage(String collectionURL) {
        if(this.lastPage != 0) {
            return this.lastPage;
        }
        driver.get(this.repositoryRoot);
        String xpath = "//li[@class=\"pager-last last\"]/a";
        List<WebElement> pagerLast = driver.findElements(By.xpath(xpath));
        String href = pagerLast.get(0).getAttribute("href");
        int len = href.length();
        String lastPage = href.substring(len - 2, len);
        this.lastPage = Integer.parseInt(lastPage);
        return this.lastPage;
    }

    void visitItems(List<String> items) {
        for (String url : items) {
            driver.get(url);
        }
    }

    private ArrayList<String> getItemUrls(String itemListingURL) {
        driver.get(itemListingURL);
        List<WebElement> anchors = driver.findElements(By.xpath("//div[@class=\"islandora-basic-collection-wrapper\"]//dd/a"));
        ArrayList<String> urls = new ArrayList<String>();
        for (WebElement a : anchors) {
            urls.add(a.getAttribute("href"));
        }
        return urls;
    }
    
    private String getRandomPageURL(String collectionAlias) {
        ArrayList<Integer> pages = new ArrayList<>();
        Random r = new Random();
        int n = r.nextInt(this.lastPage(this.host + this.root) + 1);
        return this.repositoryRoot + "/object/" + collectionAlias + "?page=" + Integer.toString(n);
    }

    private List<String> getRandomForList(ArrayList<String> list, int n) {
        ArrayList<String> rnd = new ArrayList<>();
        Random r = new Random();
        for(int i = 0; i < n; i++) {
            rnd.add(list.get(r.nextInt(list.size())));
        }
        return rnd;
    }
}
