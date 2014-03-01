/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import GA.*;
import com.snuggy.nr.util.NRException;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import util.FileOperation;
/**
 *
 * @author vietlink
 */
public class GA_NERSystem {
    private static final int NO_OF_GENERATION=40;
    private static final int NO_OF_CHROMOSOME=40;
    private static final double CROSSOVER=0.9;
    private static final double MUTATION=0.1;
    
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException, ExecutionException, NRException{
        // TODO code application logic here
        List<Datum> train = FileOperation.readDataFile("/home/vietlink/Documents/dataset/data2/train");
        List<Datum> dev = FileOperation.readDataFile("/home/vietlink/Documents/dataset/data2/dev");
//        train = train.subList(0, 20000);
//        dev = dev.subList(0, 15000);
//        perDict = FileOperation.readDictFromFile("perDict");
//        locDict = FileOperation.readDictFromFile("locDict");
//        orgDict = FileOperation.readDictFromFile("orgDict");
        GA_Krigg ga= new GA_Krigg(NO_OF_GENERATION, NO_OF_CHROMOSOME, CROSSOVER, MUTATION, train, dev);
        ga.evolutionWithPredict();
    }
    
   
}
