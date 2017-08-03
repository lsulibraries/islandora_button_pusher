/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package buttonpusher;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author jpeak5
 */
public class ButtonPusherRunner {

    /**
     * @param args the command line arguments
     * @throws java.lang.InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
        ButtonPusher pusher;

        if (args[0].equals("random")) {
            pusher = new ButtonPusher();
            random(pusher);
        } else {
            pusher = new ScriptedButtonPusher();
            visit(pusher, args[1]);
        }

        pusher.driver.close();
    }

    public static void random(ButtonPusher pusher) {
        ArrayList<String> toVisit = pusher.populateDigitalLibraryData();
        for (String collection : toVisit) {
            ArrayList<String> items = pusher.dlib.getRandomItems(collection, 5);
            pusher.visitItems(items);
        }
    }

    public static void visit(ButtonPusher pusher, String pidsFilePath) {
        List<String> paths = fileToList(pidsFilePath);
        while (true) {
            pusher.visitItems(paths);
        }
    }

    public static List<String> fileToList(String fileName) {
        // https://www.mkyong.com/java8/java-8-stream-read-a-file-line-by-line/
        List<String> list = new ArrayList<>();

        try (BufferedReader br = Files.newBufferedReader(Paths.get(fileName))) {

            //br returns as stream and convert it into a List
            list = br.lines().collect(Collectors.toList());

        } catch (IOException e) {
        }

        return list;
    }
}
