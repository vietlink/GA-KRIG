/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GA;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import org.jmatrices.dbl.Matrix;
import util.Convert;

import org.jmatrices.dbl.MatrixFactory;
import org.jmatrices.dbl.operator.MatrixOperator;
import org.jmatrices.dbl.transformer.MatrixTransformer;

/**
 *
 * @author vietlink
 */
public class CalFitness implements Callable<List<Chromosome>> {
    public static int count=0;
    public List<Chromosome> population;
    public int start;
    public int end;
    List<Datum> train;
    List<Datum> dev;
    public HashMap<Chromosome, Double> fitnessValue;

    public CalFitness() {
    }

    public CalFitness(List<Chromosome> population, int start, int end, List<Datum> train, List<Datum> dev) {
        this.population = population;
        this.start = start;
        this.end = end;
        this.train = train;
        this.dev = dev;
    }

    public CalFitness(List<Chromosome> population, int start, int end, List<Datum> train, List<Datum> dev, HashMap</*String*/Chromosome, Double> fitnessValue) {
        this.population = population;
        this.start = start;
        this.end = end;
        this.train = train;
        this.dev = dev;
        this.fitnessValue = fitnessValue;
    }

    public CalFitness(List<Chromosome> population, int start, int end) {
        this.population = population;
        this.start = start;
        this.end = end;
    }

    @Override
    public List<Chromosome> call() throws Exception {
        
        FeatureFactory featureFactory = new FeatureFactory();
        System.gc();
        Runtime.getRuntime().gc();
        for (int i = start; i < end; i++) {
            Chromosome ch = population.get(i);
            if (fitnessValue.get(ch) != null && fitnessValue.get(ch)!=0.0) {
                ch.setFitness(fitnessValue.get(ch));
            } else {
                Random t = new Random();
                if (t.nextDouble() >= GA.GA_Krigg.predicRatio || fitnessValue.size()<50) {
                    String listFeature= Convert.convertToString(ch);
                    List<Datum> computedTrain = featureFactory.computeFeature(train, listFeature);
                    List<Datum> computedDev = featureFactory.computeFeature(dev, listFeature);
                    String path = "temp/";
                    Random r = new Random();

                    String trainFile = path + String.valueOf("train_"+r.nextInt(1000000));
                    String devFile = path + String.valueOf("dev"+r.nextInt(10000000));
                    featureFactory.writeToFeatureFile(computedTrain, trainFile, listFeature);
                    featureFactory.writeToFeatureFile(computedDev, devFile, listFeature);
                    String result = runMaxent(trainFile, devFile);                                        
                    int start2 = 0;                    
                    result = result.trim();                  
                    for (int j = 0; j < result.length(); j++) {

                        if (Character.isDigit(result.charAt(j))) {
                            start2 = j;
                            break;
                        }

                    }
                    result = result.substring(start2 - 1, result.indexOf("%"));
                    ch.setFitness(Double.valueOf(result) * 0.99 + 0.01 *78/ ch.getEncodedFeature().cardinality());
                    
                    //delete file
                    File f = new File(trainFile);
                    File f2 = new File(devFile);
                    f.delete();
                    f2.delete();
                    fitnessValue.put(ch, ch.getFitness());

                } else {

                    computeFitnessByKriging(ch, fitnessValue);

                }
            }

        }
       
        return population;
    }
     public  String runMaxent(String train, String dev) throws IOException, InterruptedException {
        String result = "";
        String modelName = String.valueOf((new Random()).nextDouble());
        ProcessBuilder processBuilder = new ProcessBuilder("maxent", train,"-b", "-m", modelName, "-i", "30");

        Process p = processBuilder.start();

        BufferedReader bre;
        String line;
        try (BufferedReader bri = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
            bre = new BufferedReader(new InputStreamReader(p.getErrorStream()));
            while ((line = bri.readLine()) != null) {
            
            }
        }
        while ((line = bre.readLine()) != null) {
            
        }
        bre.close();
        p.waitFor();
        ProcessBuilder processBuilder2 = new ProcessBuilder("maxent", "-p", dev, "-m", modelName);
        processBuilder2.redirectErrorStream(true);
        Process process2 = processBuilder2.start();
        

        try (BufferedReader bri = new BufferedReader(new InputStreamReader(process2.getInputStream()))) {
            bre = new BufferedReader(new InputStreamReader(process2.getErrorStream()));
            while ((line = bri.readLine()) != null) {
                result += line + "\n";            
            }
        }
        while ((line = bre.readLine()) != null) {
            
        }
        bre.close();
        p.waitFor();

        File model = new File(modelName);
        model.delete();

        return result;

    }
     // dung cho the he F1 tro di
     public static Chromosome computeFitnessByKriging2(Chromosome ch, List<Chromosome> temp){
        
        Chromosome featureListStar= new Chromosome();
        featureListStar= ch;
        Chromosome[] x= new Chromosome[temp.size()];
        double[] fitness = new double[temp.size()];
        
        for (int i = 0; i<temp.size(); i++) {
            x[i] = temp.get(i);
            fitness[i] = (double) temp.get(i).getFitness();            
        }
        double[] result = computeFitnessByKrig(featureListStar, x, fitness);

        ch.setFitness(result[0]) ;
        ch.setFitness_err(Math.abs(result[1])); ;

        return ch;
     }
     //dung cho the he P
     public static Chromosome computeFitnessByKriging(Chromosome ch, HashMap hm)
    {

        Chromosome featureListStar= new Chromosome();

        featureListStar= ch;
        Chromosome[] x= new Chromosome[hm.keySet().size()];
        double[] fitness = new double[hm.keySet().size()];
        int i = 0;
        for (Object k : hm.keySet()) {
            x[i] = (Chromosome) k;
            fitness[i] = (double) hm.get(k);
            i++;
        }
        double[] result = computeFitnessByKrig(featureListStar, x, fitness);

        ch.setFitness(result[0]) ;
        ch.setFitness_err(Math.abs(result[1])); ;

        return ch;
    }
    
     public static double[] computeFitnessByKrig(/*String*/Chromosome featureListStar, Chromosome[] x, double[] fitness) {
        double[] result;
//        System.out.println("COmpute by Krig, matrix");
        int N = fitness.length;
//        System.out.println("N=" + N);
//        System.out.println("Fitness vector is:");
//        for(int i=0;i<fitness.length;i++)
//            System.out.print(fitness[i]+" ");
//        System.out.println("");
        double[][] Y = new double[1][N + 1];
        double[][] Vstar = new double[1][N + 1];
        double[][] V = new double[N + 1][N + 1];
        System.arraycopy(fitness, 0, Y[0], 0, N);
        Y[0][Y[0].length - 1] = 0;
//         System.out.println("Matrix Y");
//        for(int i=0;i<Y[0].length;i++) System.out.print(Y[0][i]+" ");
//        System.out.println("");

        for (int i = 0; i < N; i++) {
            Vstar[0][i] = variogram(featureListStar, x[i]);
        }
        Vstar[0][Vstar[0].length - 1] = 1;

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                V[i][j] = variogram(x[i], x[j]);
            }
        }
        for (int j = 0; j < N; j++) {
            V[N][j] = 1;
            V[j][N] = 1;
        }
        V[N][N] = 0;

        org.jmatrices.dbl.Matrix Y_matrix = MatrixFactory.getMatrix(1, N + 1, null, Y);
        org.jmatrices.dbl.Matrix Vstar_matrix = MatrixFactory.getMatrix(1, N + 1, null, Vstar);
        org.jmatrices.dbl.Matrix V_matrix = MatrixFactory.getMatrix(N + 1, N + 1, null, V);
//        System.out.println(V_matrix);
        try {
            org.jmatrices.dbl.Matrix V_matrix_Inv = MatrixTransformer.inverse(V_matrix);
            org.jmatrices.dbl.Matrix ystar_matrix =
                    MatrixOperator.multiply(MatrixOperator.multiply(Vstar_matrix, V_matrix_Inv), MatrixTransformer.transpose(Y_matrix));
            org.jmatrices.dbl.Matrix variance =
                    MatrixOperator.multiply(MatrixOperator.multiply(Vstar_matrix, V_matrix_Inv), MatrixTransformer.transpose(Vstar_matrix));


            //return null;
            result = new double[2];
            result[0] = ystar_matrix.get(1, 1);
            if (result[0] == Double.POSITIVE_INFINITY) {
//                System.out.println("Infinity here !!!!!!!!");
                return new double[]{0.0, 0.0};
            }

//            System.out.println("Variance matrix");
//            System.out.println(variance);
            result[1] = variance.get(1, 1);
            return result;
        } catch (Exception e) {
            return new double[]{0.0, 0.0};

        }



    }
    public static double variogram(Chromosome x1, Chromosome x2)
    {
        
        double alph=0.2;
        double beta=1.2;
        
        return alph*Math.pow( computeDistance(x1, x2),beta);
    }
    
    
    public static int computeDistance(Chromosome a, Chromosome b){
        int result=0;
        a.getEncodedFeature().xor(b.getEncodedFeature());
        result= a.getEncodedFeature().cardinality();
        return result;
    }
    
     
}
