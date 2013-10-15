/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GA;

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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author vietlink
 */
public class GA_Krigg {
    public static  double crossoverThreshold = 0.9;
    public static  double mutationThreshold = 0.1;
    public static int ratioSelection = 4;
    public static double predicRatio =0.7;
    public static double percentTop = 0.2;
    public static double percentPredict = 0.7;
    public static HashMap<Chromosome, Double> fitnessValues = new HashMap<>();
    public static HashMap<Chromosome, Double> fitnessValues1 = new HashMap<>();
    public static HashMap<Chromosome, Double> fitnessValues2 = new HashMap<>();
    public static String[] key_temp;
    
    public static List<Chromosome> createRandomPopulation(int size) {
        List<Chromosome> result = new ArrayList<Chromosome>();
        for (int i = 0; i < size; i++) {
            Chromosome ch = new Chromosome();
            result.add(ch);

        }
        return result;
    }
     public static List<Chromosome> createNewPopulation2(List<Chromosome> population/*, List<String> featureNames*/) {
        // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        List<Chromosome> result = new ArrayList<Chromosome>();
        result.add(population.get(0));
        while (result.size() < population.size()) {
            Chromosome[] parents = selection(population);
            Chromosome[] springs = crossover(parents, crossoverThreshold);
            springs[0] = mutation(springs[0], mutationThreshold);
            springs[1] = mutation(springs[1], mutationThreshold);
            result.add(springs[0]);
            result.add(springs[1]);
        }
        return result;
     }
     public static  Chromosome[] selection(List<Chromosome> population) {
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
    public static Chromosome[] crossover(Chromosome[] parents, double threshold) {
        Random r = new Random();
        double t = r.nextDouble();
        if (t > threshold) {
            return parents;
        }
        Chromosome[] springs = new Chromosome[2];
        springs[0] = new Chromosome();
        springs[1] = new Chromosome();
        BitSet first_half1= parents[0].getEncodedFeature().get(0, 38);
        BitSet second_half1= parents[0].getEncodedFeature().get(39, 77);
        BitSet first_half2= parents[1].getEncodedFeature().get(0, 38);
        BitSet second_half2= parents[1].getEncodedFeature().get(39, 77);
        for (int i=0; i<39; i++){
            springs[0].getEncodedFeature().set(i, first_half1.get(i));
            springs[1].getEncodedFeature().set(i, first_half2.get(i));
        }
        for (int i= 39; i<78; i++){
            springs[0].getEncodedFeature().set(i, second_half2.get(i));
            springs[1].getEncodedFeature().set(i, second_half1.get(i));
        }

        return springs;
    }
    public static Chromosome mutation(Chromosome ch, double threshold/*, List<String> featureNames*/) {
        Random r = new Random();
        double t = r.nextDouble();
        if (t > threshold) {
            return ch;
        }
        int a = r.nextInt(78);
//        String f = featureNames.get(a);
        if (ch.getEncodedFeature().get(a)) {
            ch.getEncodedFeature().set(a, false);
        } else {
            ch.getEncodedFeature().set(a, true);
        }
        return ch;
    }
     public static Chromosome[] select2BestCandidate(Chromosome[] candidates) {
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
    public static boolean isBetter(Chromosome ch1, Chromosome ch2) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        Random r = new Random();
        double rf1 = ch1.getFitness() + r.nextGaussian() * ch1.getFitness_err();
        double rf2 = ch2.getFitness() + r.nextGaussian() * ch2.getFitness_err();
        if (rf1 >= rf2) {
            return true;
        } else {
            return false;
        }
    }

     public static  List<Chromosome> computeFitness2(List<Chromosome> population, List<Datum> train, List<Datum> dev) throws InterruptedException, IOException {
      
        List<Chromosome> subList1;
        List<Chromosome> subList2;
        double temp= (1 - percentPredict) * population.size();
        subList1 = calFitnessPopulation(population.subList(0, (int)(temp)), train, dev);      
        subList2 = predictFitness(population.subList((int)(temp), population.size()), train, dev);

        List<Chromosome> new_population = new ArrayList<Chromosome>();
        new_population.addAll(subList1);
        new_population.addAll(subList2);        
        for (int i = new_population.size()-1; i > 0; i--) {
            Chromosome ch = new_population.get(i);
            int k=0;
            while (!(ch.getFitness() > 0.0 && ch.getFitness() < 100.0)) {
              ch=new Chromosome();              
              ch= calFitness(ch, train, dev);
              k++;
              if(k>5) break;
              
            }
            if(ch.getFitness()>0 && ch.getFitness()<100) new_population.set(i, ch);
            else new_population.remove(i);
        }
        return new_population;         
    }
     
     public static  List<Chromosome> sortPopulation2(List<Chromosome> population) {
        //  throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        //chua co sap xep theo xac suat
        Collections.sort(population, new Comparator<Chromosome>() {
            @Override
            public int compare(Chromosome o1, Chromosome o2) {
                Random r = new Random();
                double rFitness1 = o1.getFitness()+ r.nextGaussian()*2*o1.getFitness_err() ;
                double rFitness2 = o2.getFitness()+ r.nextGaussian()*2*o2.getFitness_err() ;
                if (rFitness1 > rFitness2) {
                    return -1;
                } else if (rFitness1 < rFitness2) {
                    return 1;
                } else {
                    return 0;
                }
            }
        });
        return population;
    }
     public static  void printPopulation(List<Chromosome> population) {
        // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.

        for (int i = 0; i < population.size(); i++) {
            System.out.println("Fitness " + i + ":" + population.get(i).getFitness() + " error:"
                    + population.get(i).getFitness_err());
        }
    }
    
        public static List<Chromosome> calFitnessPopulation(List<Chromosome> population,
            List<Datum> train, List<Datum> dev) throws IOException, InterruptedException {
        return parallelComputeFitness(population, train, dev);
    }
        
         public static List<Chromosome> parallelComputeFitness(List<Chromosome> population,
            List<Datum> train, List<Datum> dev) throws InterruptedException {
             
        System.out.println("Parallel compute\n");
        ExecutorService executor = Executors.newFixedThreadPool(4);

        List<Future<Integer>> results;
        List a = new ArrayList();
        a.add(new CalFitness(population.subList(0, population.size() / 2), 0, population.size() / 2,
                train, dev, fitnessValues1));
        a.add(new CalFitness(population.subList(population.size() / 2, population.size() ), 0, population.size() / 2,
                train, dev, fitnessValues2));

        results = executor.invokeAll(a);
        executor.shutdown();

        return population;
    }
          private static List<Chromosome> predictFitness(List<Chromosome> subList, List<Datum> train, List<Datum> dev) throws IOException, InterruptedException {
        // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.

        for (int i = 0; i < subList.size(); i++) {
            Chromosome ch = subList.get(i);
            ch = CalFitness.computeFitnessByKriging(ch, fitnessValues);
            if (ch.getFitness() <= 0 || ch.getFitness() >= 100 || ch.getFitness() == Double.POSITIVE_INFINITY
                    || ch.getFitness() == Double.NEGATIVE_INFINITY) {
               
                    ch.setFitness(0);
                
                Object[] keySet = fitnessValues.keySet().toArray();
               for(int j=0;j<test.GA_NERSystem.key_temp.length;j++)
               {
                   fitnessValues.remove(test.GA_NERSystem.key_temp[j]);
               }
                subList.set(i, ch);
            }
        }
        return subList;
    }
    public static Chromosome calFitness(Chromosome ch, List<Datum> train, List<Datum> dev) throws IOException {

        if (fitnessValues.get(ch) != null && fitnessValues.get(ch) > 0) {
            ch.setFitness(fitnessValues.get(ch));
            return ch;
        }

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
        ch.setFitness(Double.valueOf(result) * 0.99 + 0.01 * 78 / ch.getEncodedFeature().cardinality());

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
    public static List<Chromosome> recomputeFitness(List<Chromosome> population,
            List<Datum> train, List<Datum> dev) throws IOException, InterruptedException {
        
        double temp= percentTop*population.size();
        for (int i = 0; i < temp; i++) {
            Chromosome ch = population.get(i);
            if (ch.getFitness_err() > 0) {
                calFitness(ch, train, dev);
            }
        }
        for(int i=0; i< temp;i++)
        {
            Chromosome ch= population.get(i);
            if(ch.getFitness_err() == 0)
            {
                for(int j=(int)(temp);j<population.size();j++)
                {
                    Chromosome ch2= population.get(j);
                    if(ch2.getFitness_err()==0 && ch2.getFitness() > ch.getFitness())
                    {
                        population.set(i,ch2);
                        population.set(j,ch);
                    }
                }
            }
            
        }

        return population;

    }
           public static String runMaxent(String train, String dev) throws IOException, InterruptedException {
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
