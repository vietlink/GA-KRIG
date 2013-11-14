/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import GA.*;
import java.io.IOException;
import util.FileOperation;
/**
 *
 * @author vietlink
 */
public class GA_NERSystem {
    
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
    public static List<Chromosome> temp;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException{
        // TODO code application logic here
        List<Datum> train = FileOperation.readDataFile("/home/vietlink/Documents/chuong_trinh/postagged/trn/");
        List<Datum> dev = FileOperation.readDataFile("/home/vietlink/Documents/chuong_trinh/postagged/dev");
        train = train.subList(0, 20000);
        dev = dev.subList(0, 15000);
//        perDict = FileOperation.readDictFromFile("perDict");
//        locDict = FileOperation.readDictFromFile("locDict");
//        orgDict = FileOperation.readDictFromFile("orgDict");
        evolution4(40, train, dev, 1);
    }
    
    public static void evolution4(int startSize, List<Datum> train,
            List<Datum> dev, int numberGeneration) throws InterruptedException, IOException {
        
        key_temp = new String[startSize];
        // tao quan the ngau nhien
        List<Chromosome> population = GA_Krigg.createRandomPopulation(startSize);
//        System.out.println("create completed");
        // tinh fitness cho ca the
        population = GA_Krigg.computeFitness2(population, train, dev);
//        System.out.print("compute completed");
        //sap xep ca the theo fitness
        population = GA_Krigg.sortPopulation2(population);
//        GA_Krigg.printPopulation(population);

        //Tinh lai cho cai tot nhat
//        Chromosome ch = population.get(0);
//        if (population.get(0).getFitness_err()!=0.0){
//            Chromosome ch = population.get(0);
//            population.set(0, GA_Krigg.calFitness(ch, train, dev));
//        }
//        ch_max = population.get(0);
         GA_Krigg.printPopulation(population);
//         population=recomputeFitness(population,train, dev);
        for (int i = 0; i < numberGeneration; i++) {
            System.out.println("Generation "+i);
            temp= GA_Krigg.getRealChromosome();
            // tao the he tiep theo
            population = GA_Krigg.createNewPopulation2(population);
            //tinh fitness
            population = GA_Krigg.computeFitness(population,temp, train, dev);
            //sap xep ca the
            population = GA_Krigg.sortPopulation2(population);
            
            GA_Krigg.printPopulation(population);
            //tinh lai fitness
             population=GA_Krigg.recomputeFitness(population,train,dev);
             //sap xep lai ca the
             population = GA_Krigg.sortPopulation2(population);
            if (ch_max.getFitness() < GA_Krigg.calFitness(population.get(0), train, dev).getFitness()) {
                ch_max = population.get(0);
            }
              System.out.println("Ch max:" + ch_max.getFitness());
        }
        GA_Krigg.printPopulation(population);
        System.out.println("Ch max:" + ch_max.getFitness());
        //in ra ket qua, doi chieu tu encodeFeature sang FeatureName
        System.out.println(util.Convert.convertToString(ch_max));
    }
}
