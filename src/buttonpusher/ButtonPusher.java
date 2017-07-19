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

    private WebDriver driver;
    private String host;
    private String root = "/islandora";
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws InterruptedException {
        ButtonPusher pusher = new ButtonPusher();
        
        List<String> searchTerms = new ArrayList<String>(Arrays.asList("goat", "boy", "chicken", "hurricane", "apple", "orange", "ferry", "boat"));
        List<String> firstPageCollections = pusher.getCollections("http://" + pusher.host + pusher.root);
        DigitalLibrary dlib = new DigitalLibrary();

        for(String collection : pusher.getAllCollections()) {
            dlib.addCollection(collection);
        }

        ArrayList<String> collectionsUnderTest = dlib.getRandomCollections(5);

        Thread.sleep(1000);
        for (String collectionURL : collectionsUnderTest) {
            
            List<String> itemURLs = dlib.getRandomItems(collectionURL, 5);
            pusher.visitItems(itemURLs);
        }

        pusher.driver.close();
    }

    public ButtonPusher() {
        System.setProperty("webdriver.gecko.driver", "/usr/local/bin/geckodriver");

        WebDriver driver = new FirefoxDriver();
        this.driver = driver;
        
        String host = "ldl.lib.lsu.edu";
//        String host = "test.louisianadigitallibrary.org";
//        String host = "lib-ti012.lsu.edu:8000";
        this.host = host;
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
            urls.add(a.getAttribute("href"));
        }
        return urls;
    }

    private List<String> getAllCollections(){
        ArrayList<String> allCollections = new ArrayList<>();
        String collectionRoot = this.host + this.root;
        int lastPage = Integer.parseInt(this.lastPage(collectionRoot));
        for (int i = 0; i < lastPage; i++) {
            String pageURL = "http://" + host + root + "/object/islandora%3Aroot?page=" + i;
            List<String> collections = getCollections(pageURL);
            allCollections.addAll(collections);
        }
        return allCollections;
    }

    private String lastPage(String collectionURL) {
        String xpath = "//li[@class=\"pager-last last\"]/a";
        List<WebElement> pagerLast = driver.findElements(By.xpath(xpath));
        String href = pagerLast.get(0).getAttribute("href");
        int len = href.length();
        String lastPage = href.substring(len - 2, len);
        return lastPage;
    }

    private void visitItems(List<String> items) {
        for (String url : items) {
            driver.get(url);
        }
    }

    private List<String> getItemUrls(String itemListingURL) {
        driver.get(itemListingURL);
        List<WebElement> anchors = driver.findElements(By.xpath("//div[@class=\"islandora-basic-collection-wrapper\"]//dd/a"));
        List<String> urls = new ArrayList<String>();
        for (WebElement a : anchors) {
            urls.add(a.getAttribute("href"));
        }
        return urls;
    }
    
    private ArrayList<Integer> getRandomPage(String url, int n) {
        ArrayList<Integer> pages = new ArrayList<>();
        int lastPage = Integer.parseInt(this.lastPage(this.host + this.root));
        Random r = new Random();
        for(int i = 0; i < n; i++) {
            pages.add(r.nextInt(lastPage + 1));
        }
        return pages;
    }
}
