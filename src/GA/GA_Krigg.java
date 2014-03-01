/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GA;

import com.snuggy.nr.util.NRException;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.Random_util;
/**
 *
 * @author vietlink
 */
public class GA_Krigg {
    private double crossoverThreshold;
    private double mutationThreshold ;
//    private static int ratioSelection = 4;
    private int startSize;
    private int numberGeneration;
//    public static double predicRatio =0.7;
//    public static double percentTop = 0.2;
    private double percentPredict = 0.7;
    private HashMap<Chromosome, Double> fitnessValues = new HashMap<>();
    private HashMap<Chromosome, Double> fitnessValues1 = new HashMap<>();
    private HashMap<Chromosome, Double> fitnessValues2 = new HashMap<>();
//    public static String[] key_temp;
    private List<Datum> train;
    private List<Datum> dev;
//    public static List<Chromosome> predictChromosome;
//    public static List<Chromosome> new_population;

    public GA_Krigg(int numberGeneration, int startSize, double crossoverThreshold, double mutationThreshold, List<Datum> train, List<Datum> test) {
        this.numberGeneration=numberGeneration;
        this.startSize= startSize;
        this.crossoverThreshold=crossoverThreshold;
        this.mutationThreshold=mutationThreshold;
        this.train=train;
        this.dev=test;
    }
    
    public void evolutionWithPredict() throws InterruptedException, IOException, ExecutionException, NRException {
        Chromosome ch_max= new Chromosome();       
        int seed_size= (int)(startSize*(1-percentPredict));       
        List<Chromosome> population;
        List<Chromosome> seed_population;
        List<Chromosome> sub_population;
        
        seed_population = createRandomPopulation(seed_size);
        seed_population = parallelComputeFitness(seed_population, train, dev);
        printPopulation(seed_population);
        
        population= createRandomPopulation(startSize);       
        population=predictFitness2(population, seed_population);
        //sap xep ca the theo fitness                
        population = sortPopulation2(population);
        printPopulation(population);        
        sub_population= population.subList(0, (int)((1-percentPredict)*population.size()));
        sub_population=parallelComputeFitness(sub_population, train, dev);
        sub_population=sortPopulation(sub_population);
        for (int i = 0; i < sub_population.size(); i++) {
            population.set(i, sub_population.get(i));
        }
        ch_max= sub_population.get(0);
        System.out.println("------------------------ \n");
        
        for (int i = 1; i < numberGeneration; i++) {
            System.err.println("Generation "+(i+1));                                                                
            population = createNewPopulation2(population);            
            population = predictFitness2(population, sub_population);
            //sap xep ca the
            population = sortPopulation2(population);
            printPopulation(population);
            if (i!=numberGeneration-1){
            sub_population= population.subList(0, (int)((1-percentPredict)*population.size()));
            sub_population=parallelComputeFitness(sub_population, train, dev);
            sub_population=sortPopulation(sub_population);
            for (int j = 0; j < sub_population.size(); j++) {
                population.set(j, sub_population.get(j));
            }
            ch_max= sub_population.get(0);
            }
            else{
                ch_max=population.get(0);
                ch_max= calFitness(ch_max, train, dev);                
            }
        System.err.println("------------------------ \n");
        printPopulation(population);
        System.err.println("------------------------ \n");
        System.err.println("Ch max:" + ch_max.getFitness());
                
    }
    //in ra ket qua, doi chieu tu encodeFeature sang FeatureName    
    System.err.println(util.Convert.convertToString(ch_max));
    }
    public void evolutionWithoutPredict() throws InterruptedException, IOException, ExecutionException, NRException {
        Chromosome ch_max;        
        List<Chromosome> population;        
        population= createRandomPopulation(startSize);

        population=parallelComputeFitness(population, train, dev);
        //sap xep ca the theo fitness                
        population = sortPopulation(population);
        printPopulation(population);
        ch_max= population.get(0);
        System.err.println("------------------------ \n");
        System.err.println("Ch max: "+ch_max.getFitness());
        for (int i = 1; i < numberGeneration; i++) {
            System.err.println("Generation "+(i+1));                                                                
            population = createNewPopulation2(population);                        
            population=parallelComputeFitness(population, train, dev);
            population = sortPopulation(population);
            ch_max=population.get(0);
        System.out.println("------------------------ \n");
        printPopulation(population);
        System.out.println("------------------------ \n");
        System.out.println("Ch max:" + ch_max.getFitness());
        //in ra ket qua, doi chieu tu encodeFeature sang FeatureName
        System.out.println(util.Convert.convertToString(ch_max));
    }
    }
    public List<Chromosome> createRandomPopulation(int size) {
        List<Chromosome> result = new ArrayList<Chromosome>();
        for (int i = 0; i < size; i++) {
            Chromosome ch = new Chromosome();
            result.add(ch);
            
        }
        System.out.println("Create P completed, number of chromosome is "+result.size()+ "\n");
        return result;
    }
     public List<Chromosome> createNewPopulation2(List<Chromosome> population/*, List<String> featureNames*/) {
        // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        List<Chromosome> result = new ArrayList<Chromosome>();
        
        result.add(population.get(0));
         for (int i = 1; i < population.size(); i++) {
             Chromosome indiv1= tournementSelection(population);
             Chromosome indiv2= tournementSelection(population);
             Chromosome child= crossover(indiv1, indiv2, crossoverThreshold);
             result.add(child);
         }
         for (int i = 1;i < population.size(); i++) {
             mutation(result.get(i), mutationThreshold);
         }
        return result;
     }
     public  Chromosome[] selection(List<Chromosome> population) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        Chromosome[] result = new Chromosome[2];
        int k = 3;
        Chromosome[] candidates = new Chromosome[k];
        Random r = new Random();
        for (int i = 0; i < k; i++) {
            int index = r.nextInt(population.size());
            candidates[i] = population.get(index);
        }
        result = select2BestCandidate(candidates);
        return result;

    }
    public Chromosome crossover(Chromosome father, Chromosome mother, double threshold) {
        Random r = new Random();
        Chromosome springs= new Chromosome();
        for (int i = 0; i < springs.getEncodedFeature().length(); i++) {
            if(r.nextDouble()<=threshold)
                springs.getEncodedFeature().set(i, father.getEncodedFeature().get(i));
            else 
                springs.getEncodedFeature().set(i, mother.getEncodedFeature().get(i));

       
    }
         return springs;
    }
    public void mutation(Chromosome ch, double threshold/*, List<String> featureNames*/) {
        Random r = new Random();
        for (int i = 0; i < ch.getEncodedFeature().length(); i++) {
            if(r.nextDouble()<=threshold)
                ch.getEncodedFeature().set(i, !ch.getEncodedFeature().get(i));
        }
        
    }
    public Chromosome tournementSelection(List<Chromosome> population){
        Random r= new Random();
        int i, j, k, l;
        Chromosome result, t1, t2;
        Chromosome indiv1, indiv2, indiv3, indiv4;
        i=j=k=l= r.nextInt(population.size());
        while (i==j)
            j=r.nextInt(population.size());
        while((i==k||k==l))
            k=r.nextInt(population.size());
        while((i==l)||(k==l)||(j==l))
            l=r.nextInt(population.size());
        indiv1=population.get(i);
        indiv2=population.get(j);
        indiv3=population.get(k);
        indiv4=population.get(l);
        if (isBetter(indiv1, indiv2))
            t1=indiv1;
        else t1=indiv2;
        if(isBetter(indiv3, indiv4))
            t2=indiv3;
        else t2=indiv4;
        if(isBetter(t1, t2))
            result=t1;
        else result=t2;
        return result;
    }
     public Chromosome[] select2BestCandidate(Chromosome[] candidates) {
        // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        Chromosome[] result = new Chromosome[2];
        result[0] = new Chromosome();
        result[1] = new Chromosome();
        for (int i = 0; i < candidates.length; i++) {
            if (isBetter(candidates[i], result[0])) {
                result[0] = candidates[i];
            } else if (isBetter(candidates[i], result[1])) {
                result[1] = candidates[i];
            }
        }
        return result;
    }
    public boolean isBetter(Chromosome o1, Chromosome o2) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        Random_util r= new Random_util();
                double mean;
                double stdev;
                double gaussian_val;
                mean= o1.getFitness()-o2.getFitness();
                stdev=Math.sqrt(o1.getFitness_err()*o1.getFitness_err()+o2.getFitness_err()*o2.getFitness_err());
                gaussian_val=r.Next_Gaussian(mean, stdev);
                if (gaussian_val<0) {
                    return false;
                } else 
                    return true;
    }
    
    public List<Chromosome> predictFitness2(List<Chromosome> pop, List<Chromosome> temp) throws NRException{                  
            pop= CalFitness.computeFitnessByKriging2(pop, temp);
        return pop;
            
        }
    
     public List<Chromosome> sortPopulation(List<Chromosome> population){
         Collections.sort(population, new Comparator<Chromosome>() {

             @Override
             public int compare(Chromosome o1, Chromosome o2) {
//                 throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                 if(o1.getFitness()<o2.getFitness()){
                     return 1;
                 }
                 else if(o1.getFitness()>o2.getFitness()){
                     return -1;
                 }
                 else {
                     return 0;
                 }
             }
         });
         return population;
     }
     
     public List<Chromosome> sortPopulation2(List<Chromosome> population) {
        //  throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        //chua co sap xep theo xac suat
         Random_util r= new Random_util();
         double mean;
         double stdev;
         double gaussian_val;
         Chromosome t;
         for (int i = 0; i < population.size()-1; i++) {
             for (int j = i+1; j < population.size(); j++) {
                 Chromosome o1= population.get(i);
                 Chromosome o2= population.get(j);
                 mean= o1.getFitness()-o2.getFitness();
                stdev=Math.sqrt(o1.getFitness_err()*o1.getFitness_err()+o2.getFitness_err()*o2.getFitness_err());
                gaussian_val=r.Next_Gaussian(mean, stdev);
                if(gaussian_val<0){
                    t=o2;
                    o2=o1;
                    o1=t;
                }
             }
         }
//        Collections.sort(population, new Comparator<Chromosome>() {
//            @Override
//            public int compare(Chromosome o1, Chromosome o2) {
////                Random r = new Random();
////                double rFitness1 = o1.getFitness()+ r.nextGaussian()*2*o1.getFitness_err() ;
////                double rFitness2 = o2.getFitness()+ r.nextGaussian()*2*o2.getFitness_err() ;
//                Random_util r= new Random_util();
//                double mean;
//                double stdev;
//                double gaussian_val;
//                mean= o1.getFitness()-o2.getFitness();
//                stdev=Math.sqrt(o1.getFitness_err()*o1.getFitness_err()+o2.getFitness_err()*o2.getFitness_err());
//                gaussian_val=r.Next_Gaussian(mean, stdev);
//                if (gaussian_val<0) {
//                    return -1;
//                } else if (gaussian_val>0) {
//                    return 1;
//                } else {
//                    return 0;
//                }
//            }
//        });
        return population;
    }
     public void printPopulation(List<Chromosome> population) {
        // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.

        for (int i = 0; i < population.size(); i++) {
            System.out.println("Fitness " + i + ":" + population.get(i).getFitness() + " error:"
                    + population.get(i).getFitness_err()+" "+population.get(i).getEncodedFeature().toString());
        }
    }
    
        public List<Chromosome> calFitnessPopulation(List<Chromosome> population,
            List<Datum> train, List<Datum> dev) throws IOException, InterruptedException, ExecutionException {
        return parallelComputeFitness(population, train, dev);
    }
        
        
         public List<Chromosome> parallelComputeFitness(List<Chromosome> population,
            List<Datum> train, List<Datum> dev) throws InterruptedException {
             
        System.out.println("Parallel compute\n");
        ExecutorService executor = Executors.newFixedThreadPool(2);

        List<Future<Integer>> results;
        List a = new ArrayList();
        a.add(new CalFitness(population.subList(0, population.size()/2), 0, population.size()/2,
                train, dev, fitnessValues1));
        a.add(new CalFitness(population.subList(population.size()/2, population.size()), 0, population.size()/2,
                train, dev, fitnessValues2));

        results = executor.invokeAll(a);
        
        executor.shutdown();
        
//             for (int i = 0; i < population.size(); i++) {
//                 System.out.print(i+": "+population.get(i).getFitness()+", "+population.get(i).getFitness_err()+"\n");
//             }
        return population;
    }
       
    public Chromosome calFitness(Chromosome ch, List<Datum> train, List<Datum> dev) throws IOException {
        Random r = new Random();

        FeatureFactory featureFactory = new FeatureFactory();
        String listFeature= util.Convert.convertToString(ch);
        List<Datum> computedTrain = featureFactory.computeFeature(train, listFeature);
        List<Datum> computedDev = featureFactory.computeFeature(dev, listFeature);
        String path = "temp/";


        String trainFile = path + String.valueOf(r.nextInt(1000000));
        String devFile = path + String.valueOf(r.nextDouble());
        try {
            featureFactory.writeToFeatureFile(computedTrain, trainFile, listFeature);
        } catch (IOException ex) {
            Logger.getLogger(GA_Krigg.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            featureFactory.writeToFeatureFile(computedDev, devFile, listFeature);
        } catch (IOException ex) {
            Logger.getLogger(GA_Krigg.class.getName()).log(Level.SEVERE, null, ex);
        }
        String result = null;
        try {
            result = runMaxent(trainFile, devFile);
        } catch (InterruptedException ex) {
            Logger.getLogger(GA_Krigg.class.getName()).log(Level.SEVERE, null, ex);
        }

        int start = 0;
   
        result = result.trim();
   
        for (int i = 0; i < result.length(); i++) {

            if (Character.isDigit(result.charAt(i))) {
                start = i;
                break;
            }

        }
        result = result.substring(start - 1, result.indexOf("%"));
        if (result == null) {
            System.out.println("NULL");
        }
        if (ch == null) {
            System.out.println("NULL fitnees");
        }
        if (ch.getEncodedFeature().cardinality() == 0) {
            System.out.println("Features null");
        }
//        if (featureNames == null) {
//            System.out.println("featureNames null");
//        }
        ch.setFitness(Double.valueOf(result));

        ch.setFitness_err(0);

        //add to fitnessValue
        fitnessValues.put(ch, ch.getFitness());

        //delete file
        File f = new File(trainFile);
        File f2 = new File(devFile);
        f.delete();
        f2.delete();
        if (ch.getFitness() >= 100) {
            ch.setFitness(0);
        }
        return ch;

    }

           public String runMaxent(String train, String dev) throws IOException, InterruptedException {
        String result = "";
        String modelName = String.valueOf((new Random()).nextDouble());
        ProcessBuilder processBuilder = new ProcessBuilder("maxent", train, "-b", "-m", modelName, "-i", "30");
        Process p = processBuilder.start();

        BufferedReader bre;
        String line;
        try (BufferedReader bri = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
            bre = new BufferedReader(new InputStreamReader(p.getErrorStream()));
            while ((line = bri.readLine()) != null) {
                //  System.out.println(line);
            }
        }
        while ((line = bre.readLine()) != null) {
            //   System.out.println(line);
        }
        bre.close();
        p.waitFor();
        // System.out.println("Done.");


        ProcessBuilder processBuilder2 = new ProcessBuilder("maxent", "-p", dev, "-m", modelName);
        //  ProcessBuilder processBuilder2 = new ProcessBuilder("maxent", "-n","10", train);
        processBuilder2.redirectErrorStream(true);
        Process process2 = processBuilder2.start();


        try (BufferedReader bri = new BufferedReader(new InputStreamReader(process2.getInputStream()))) {
            bre = new BufferedReader(new InputStreamReader(process2.getErrorStream()));
            while ((line = bri.readLine()) != null) {
                result += line + "\n";
                //   System.out.println(line);
            }
        }
        while ((line = bre.readLine()) != null) {
            // System.out.println(line);
        }
        bre.close();
        p.waitFor();
        //  System.out.println("Done.");

        // System.out.println("Result is : "+result.trim().substring(10,result.indexOf("%")));


        File model = new File(modelName);
        model.delete();

        return result;

    }

}
