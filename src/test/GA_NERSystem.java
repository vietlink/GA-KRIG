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
    private static final int NO_OF_GENERATION=5;
    private static final int NO_OF_CHROMOSOME=40;
    private static final double PERCENT_PREDICT=0.7;
    public static List<String> featureNames;
    public static Chromosome ch_max = new Chromosome();
    public static HashMap<Chromosome, Double> fitnessValues = new HashMap<>();
    public static HashMap<Chromosome, Double> fitnessValues1 = new HashMap<>();
    public static HashMap<Chromosome, Double> fitnessValues2 = new HashMap<>();
    public static HashMap<Chromosome, Double> fitnessValues3 = new HashMap<>();
    public static HashMap<Chromosome, Double> fitnessValues4 = new HashMap<>();
    public static List<Double> bestFitness = new ArrayList<>();
    public static List<String> perDict = new ArrayList<>();
    public static List<String> locDict = new ArrayList<>();
    public static List<String> orgDict = new ArrayList<>();
    public static String[] key_temp;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException, ExecutionException, NRException{
        // TODO code application logic here
        List<Datum> train = FileOperation.readDataFile("/home/vietlink/Documents/chuong_trinh/postagged/trn/");
        List<Datum> dev = FileOperation.readDataFile("/home/vietlink/Documents/chuong_trinh/postagged/dev");
        train = train.subList(0, 20000);
        dev = dev.subList(0, 15000);
//        perDict = FileOperation.readDictFromFile("perDict");
//        locDict = FileOperation.readDictFromFile("locDict");
//        orgDict = FileOperation.readDictFromFile("orgDict");
        evolution4(NO_OF_CHROMOSOME, train, dev, NO_OF_GENERATION);
    }
    
    public static void evolution4(int startSize, List<Datum> train,
            List<Datum> dev, int numberGeneration) throws InterruptedException, IOException, ExecutionException, NRException {
        
        key_temp = new String[startSize];
        int seed_size= (int)(startSize*(1-PERCENT_PREDICT));
        // tao quan the ngau nhien
        List<Chromosome> population;
        List<Chromosome> seed_population;
        seed_population = GA_Krigg.createRandomPopulation(seed_size);
        seed_population = GA_Krigg.calFitnessPopulation(seed_population, train, dev);
        GA_Krigg.printPopulation(seed_population);
//        for (int i = 0; i < population.size(); i++) {
//            System.out.print(population.get(i).getEncodedFeature()+"\n");
//        }
//        System.out.println("create completed");                        
//        System.out.print("compute completed");
        population= GA_Krigg.createRandomPopulation(startSize);
        population=GA_Krigg.predictFitness2(population, seed_population);
        //sap xep ca the theo fitness                
        population = GA_Krigg.sortPopulation2(population);
        GA_Krigg.printPopulation(population);
//         population=recomputeFitness(population,train, dev);
        List<Chromosome> sub_population= population.subList(0, (int)((1-PERCENT_PREDICT)*population.size()));
        sub_population=GA_Krigg.parallelComputeFitness(sub_population, train, dev);
        sub_population=GA_Krigg.sortPopulation2(sub_population);
        ch_max= sub_population.get(0);
        System.out.println("------------------------ \n");
        
        for (int i = 1; i < numberGeneration; i++) {
            System.err.println("Generation "+(i+1));                                                                
                population = GA_Krigg.createNewPopulation2(population);            
//            for (int j=0; j<temp.size(); j++){
//                System.out.println("temp "+temp.get(j).getFitness()+" "+temp.get(j).getFitness_err()+"\n");
//            }
            // tao the he tiep theo
            
//            for (int j = 0; j < population.size(); j++) {
//            System.out.print(population.get(j).getEncodedFeature()+"\n");
//        }
//            System.out.println("create completed");
            //tinh fitness
//            temp= GA_Krigg.getRealChromosome();
            population = GA_Krigg.computeFitness(population, train, dev);
            //sap xep ca the
            population = GA_Krigg.sortPopulation2(population);
            ch_max= population.get(0);
            if(i==numberGeneration-1){
                int noOfcal=0;                
                while (ch_max.getFitness_err()>0 && noOfcal<5){
                    ch_max=GA_Krigg.calFitness(ch_max, train, dev);
                    population.set(0, ch_max);
                    population=GA_Krigg.sortPopulation2(population);
                }
            }
            GA_Krigg.printPopulation(population);
            //tinh lai fitness
//             population=GA_Krigg.recomputeFitness(population,train,dev);
             //sap xep lai ca the
//             population = GA_Krigg.sortPopulation2(population);
//            population= GA_Krigg.sortPopulation2(population);
            
            System.err.println("Ch max:" + ch_max.getFitness());
        }
        System.out.println("------------------------ \n");
        GA_Krigg.printPopulation(population);
        System.out.println("------------------------ \n");
        System.out.println("Ch max:" + ch_max.getFitness());
        //in ra ket qua, doi chieu tu encodeFeature sang FeatureName
        System.out.println(util.Convert.convertToString(ch_max));
    }
}
