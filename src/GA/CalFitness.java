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
import java.util.BitSet;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;

import util.Convert;
import krig.*;


/**
 *
 * @author vietlink
 */
public class CalFitness implements Callable<List<Chromosome>> {
    private static final double epsilon=0.03;
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
        this(population, start, end, train, dev, null);
    }

    public CalFitness(List<Chromosome> population, int start, int end, List<Datum> train, List<Datum> dev, HashMap</*String*/Chromosome, Double> fitnessValue) {
        this.population = population;        
        this.start = start;
        this.end = end;
        this.train = train;
        this.dev = dev;
        this.fitnessValue = fitnessValue;
    }

   
    @Override
    public List<Chromosome> call() throws Exception {
        
        FeatureFactory featureFactory = new FeatureFactory();
        System.gc();
        Runtime.getRuntime().gc();
        for (int i = start; i < end; i++) {
            Chromosome ch = population.get(i);
//            if (fitnessValue.get(ch) != null && fitnessValue.get(ch)!=0.0) {
//                ch.setFitness(fitnessValue.get(ch));
//            } else {
//                Random t = new Random();
//                if (t.nextDouble() >= GA.GA_Krigg.predicRatio || fitnessValue.size()<50) {
                    String listFeature= Convert.convertToString(ch);
                    List<Datum> computedTrain = featureFactory.computeFeature(train, listFeature);
                    List<Datum> computedDev = featureFactory.computeFeature(dev, listFeature);
                    String path = "temp/";
                    Random r = new Random();
                    String trainFile = path + String.valueOf("train_"+r.nextInt(1000000));
                    String devFile = path + String.valueOf("dev"+r.nextInt(10000000));
                    featureFactory.writeToFeatureFile(computedTrain, trainFile, listFeature);
                    featureFactory.writeToFeatureFile(computedDev, devFile, listFeature);
//                    System.out.print("Chromosome "+i+" :To maxent \n");
                    String result = runMaxent(trainFile, devFile);
//                    System.out.println("Chromosome "+i+" :Maxent completed \n");
                    int start2 = 0;                    
                    result = result.trim();                  
                    for (int j = 0; j < result.length(); j++) {

                        if (Character.isDigit(result.charAt(j))) {
                            start2 = j;
                            break;
                        }

                    }
                    result = result.substring(start2 - 1, result.indexOf("%"));
                    ch.setFitness(Double.valueOf(result));
                    ch.setFitness_err(0.0);
                    System.out.print("Chromosome "+i+" Done \n");
//                    System.out.println("Chromosome "+i+" : fitness "+ch.getFitness()+" err: "+ch.getFitness_err());
                    //delete file
                    File f = new File(trainFile);
                    File f2 = new File(devFile);
                    f.delete();
                    f2.delete();
                    fitnessValue.put(ch, ch.getFitness());
//                }
//                } else {
//
//                    computeFitnessByKriging(ch, fitnessValue);
//
//                }
//            }

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
     public static List<Chromosome> computeFitnessByKriging2(List<Chromosome> pop, List<Chromosome> temp) throws NRException{
        
//        Chromosome featureListStar= new Chromosome();
//        featureListStar= ch;
        Chromosome[] x= new Chromosome[temp.size()];
        double[] fitness = new double[temp.size()];
        
        for (int i = 0; i<temp.size(); i++) {
            x[i] = temp.get(i);
            fitness[i] = temp.get(i).getFitness();            
        }
        double[][] result = computeFitnessByKrig(pop, x, fitness);
         for (int i = 0; i < pop.size(); i++) {
            pop.get(i).setFitness(result[i][0]) ;
            pop.get(i).setFitness_err(Math.abs(result[i][1])); ;
         }        
        return pop;
     }
     //dung cho the he P
//     public static Chromosome computeFitnessByKriging(Chromosome ch, List<Chromosome> temp) throws NRException
//    {
//
//        Chromosome featureListStar= new Chromosome();
//
//        featureListStar= ch;
//        Chromosome[] x= new Chromosome[temp.size()];
//        double[] fitness = new double[temp.size()];
//        for (int i = 0; i<temp.size(); i++) {
//            x[i] = temp.get(i);
//            
//            fitness[i] = (double) temp.get(i).getFitness();            
//        }
//        double[] result = computeFitnessByKrig(featureListStar, x, fitness);
//
//        ch.setFitness(result[0]) ;
//        ch.setFitness_err(Math.abs(result[1])); ;
//
//        return ch;
//    }
    
     public static double[][] computeFitnessByKrig(/*String*/List<Chromosome> pop, Chromosome[] x, double[] fitness) throws NRException {
        
//        System.out.println("COmpute by Krig, matrix");
         int N = fitness.length;
         double[][] result=new double[pop.size()][2];
         BitSet[][] xx= new BitSet[N][1];
         double[] y= new double[N];
         BitSet[] xstar= new BitSet[1];                  
         for (int i = 0; i < N; i++) {
             xx[i][0]=x[i].getEncodedFeature();
             y[i]=x[i].getFitness();
         }
         Powvargram vgram= new Powvargram(xx, y);
         Krig<Powvargram> krig= new Krig<Powvargram>(xx, y, vgram);
         for (int i = 0; i < pop.size(); i++) {
             xstar[0]=pop.get(i).getEncodedFeature();                  
             result[i]=krig.interp(xstar);
         }
         
         
//        System.out.println("N=" + N);
//        System.out.println("Fitness vector is:");
//        for(int i=0;i<fitness.length;i++)
//            System.out.print(fitness[i]+" ");
//        System.out.println("");
//        double[][] Y = new double[1][N + 1];
//        double[][] Vstar = new double[1][N + 1];
//        double[][] V = new double[N + 1][N + 1];
//        System.arraycopy(fitness, 0, Y[0], 0, N-1);
//         
//        Y[0][N] = 0;
////         System.out.println("Matrix Y");
////        for(int i=0;i<Y[0].length;i++) System.out.print(Y[0][i]+" ");
////        System.out.println("");
//
//        for (int i = 0; i < N; i++) {
//            Vstar[0][i] = variogram(featureListStar, x[i]);
//        }
//        Vstar[0][N] = 1;
//
//        for (int i = 0; i < N; i++) {
//            for (int j = 0; j < N; j++) {
//                V[i][j] = variogram(x[i], x[j]);
//            }
//        }
//        for (int j = 0; j < N; j++) {
//            V[N][j] = 1;
//            V[j][N] = 1;
//        }
//        V[N][N] = 0;
//         for (int i = 0; i < N+1; i++) {
//             for (int j = 0; j < N+1; j++) {
//                 System.out.println(V[i][j]+"  ");
//             }
//             System.out.println("\n"); 
//        }
//        org.jmatrices.dbl.Matrix Y_matrix = MatrixFactory.getMatrix(1, N + 1, null, Y);
//        Jama.Matrix Y_matrix= matrix.creatMatrix(1, N+1, Y);
//        Jama.Matrix V_star_matrix= matrix.creatMatrix(1, N+1, Vstar);
//        Jama.Matrix V_matrix= matrix.creatMatrix(N+1, N+1, V);
//        while (V_matrix.det()==0.0){
//            for (int i = 0; i < N+1; i++) { 
//                    double t= V_matrix.get(i, i)+epsilon;
//                    V_matrix.set(i, i, t);                
//            }
//        }
//        Jama.Matrix V_matrix_inverse= V_matrix.inverse();
////        org.jmatrices.dbl.Matrix Vstar_matrix = MatrixFactory.getMatrix(1, N + 1, null, Vstar);
////        org.jmatrices.dbl.Matrix V_matrix = MatrixFactory.getMatrix(N + 1, N + 1, null, V);
////        System.out.println(V_matrix +"\n");
////        try {
////            org.jmatrices.dbl.Matrix V_matrix_Inv = MatrixTransformer.inverse(V_matrix);
////            System.out.println(V_matrix_Inv+"\n");
////            org.jmatrices.dbl.Matrix ystar_matrix =
////                    MatrixOperator.multiply(MatrixOperator.multiply(Vstar_matrix, V_matrix_Inv), MatrixTransformer.transpose(Y_matrix));
//////            System.out.println(ystar_matrix+"\n");
////            org.jmatrices.dbl.Matrix variance =
////                    MatrixOperator.multiply(MatrixOperator.multiply(Vstar_matrix, V_matrix_Inv), MatrixTransformer.transpose(Vstar_matrix));
//////            System.out.println(variance);
////
////            //return null;
////            result = new double[2];
////            result[0] = ystar_matrix.get(1, 1);
//            if (result[0] == Double.POSITIVE_INFINITY) {
//                System.out.println("Infinity here !!!!!!!!");
//                return new double[][]{0.0, 0.0};
//            }
////
//////            System.out.println("Variance matrix");
//////            System.out.println(variance);
////            result[1] = variance.get(1, 1);
////            return result;
////        } catch (Exception e) {
////            System.out.println(V_matrix +"\n");
////            return new double[]{0.0, 0.0};
////
////        }
//        Jama.Matrix value= (V_star_matrix.times(V_matrix_inverse)).times(Y_matrix.transpose());
//        Jama.Matrix variance=(V_star_matrix.times(V_matrix_inverse)).times(V_star_matrix.transpose());
//        result=new double[2];        
//        result[0]= value.get(0,0);
//        result[1]= variance.get(0,0);
//        if (result[0] == Double.POSITIVE_INFINITY) {
//                System.out.println("Infinity here !!!!!!!!");
//                return new double[]{0.0, 0.0};
//        }
        return result;
    }
    public static double variogram(Chromosome x1, Chromosome x2)
    {
        
        double alph=0.2;
        double beta=1.2;
        
        return alph*Math.pow(computeDistance(x1, x2),beta);
    }
    
    
    public static int computeDistance(Chromosome a, Chromosome b){
        int result;
        BitSet t= (BitSet) a.getEncodedFeature().clone();
        t.xor(b.getEncodedFeature());
        result= t.cardinality();
//        System.out.print(t+"\n");
        return result;
    }
    
     
}
