/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import GA.*;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author vietlink
 */
public class FileOperation {
    public static List<String> readDictFromFile(String fileName) throws FileNotFoundException, IOException {
        List<String> result = new ArrayList<>();
        BufferedReader bfr = new BufferedReader(new FileReader(new File(fileName)));
        String line = "";
        while ((line = bfr.readLine()) != null) {
            result.add(line.trim());
        }
        return result;
    }

    public static void writeToDictFile(List<String> d, String fileName) throws IOException {
        FileWriter fw = new FileWriter(new File(fileName));
        for (String s : d) {
            if (!"'".equals(s) && !"\"".equals(s) && !",".equals(s) && !".".equals(s) && !"'m".equals(s)) {
                fw.write(s + "\n");
            }
        }
        fw.close();
    }

    public static List<String> addToDict(List<Datum> datums, String type, List<String> dict) {

        for (Datum d : datums) {
            if (d.getType().equals(type)) {
                dict.add(d.getWord());
            }
        }
        return dict;
    }
    public static List<Datum> readDataFile(String path) throws IOException, ClassNotFoundException {
        List<Datum> datums = new ArrayList<Datum>();
        File directory = new File(path);
        File[] files = directory.listFiles();
        for (File f : files) {
            if (!f.getName().contains("~")) {
                try {

                    BufferedReader bfr = new BufferedReader(new FileReader(f));
                    String line = "";
                    while ((line = bfr.readLine()) != null) {
                        String[] strs = line.split("\\s+");
                        //  System.out.println(strs[0]+":"+strs[1]);
                        Datum d = new Datum(strs[0], strs[2], strs[1]);
                        datums.add(d);

                    }
                    bfr.close();

                } catch (FileNotFoundException ex) {
                    Logger.getLogger(FileOperation.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(FileOperation.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        }

        System.out.println("Size:" + datums.size());
        return datums;
    }
}
