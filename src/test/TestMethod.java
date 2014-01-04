/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import GA.CalFitness;
import GA.Chromosome;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.Random;
import util.Random_util;
/**
 *
 * @author vietlink
 */
public class TestMethod {

   static public void main (String[] args){
      Chromosome x_star= new Chromosome();
      Chromosome[] x=new Chromosome[13];
      Random r= new Random();
      double[] fitness= new double[13];
      double [] result= new double[2];
       for (int i = 0; i < 13; i++) {
           x[i]= new Chromosome();
           fitness[i]= r.nextDouble();
       }
       result=CalFitness.computeFitnessByKrig(x_star, x, fitness);
       
   }


}
