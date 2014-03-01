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


import com.snuggy.nr.util.NRException;
import krig.Krig;
import krig.Powvargram;
/**
 *
 * @author vietlink
 */
public class TestMethod {

   static public void main (String[] args) throws NRException{
    double[] result=new double[2];
//        System.out.println("COmpute by Krig, matrix");
//        int N = fitness.length;
         BitSet[][] xx= new BitSet[19][1];
         System.out.println(xx.length+ " "+xx[0].length);
         double[] y= new double[19];
         BitSet[] xstar= new BitSet[20];
         
         Random r= new Random();
         for (int i = 0; i < 19; i++) {
             xx[i][0]=new BitSet(78);
             for (int j = 0; j < 78; j++) {
                 xx[i][0].set(j, r.nextBoolean());
             }
             y[i]=r.nextDouble();
         }
         Powvargram vgram= new Powvargram(xx, y);
         Krig<Powvargram> krig= new Krig<Powvargram>(xx, y, vgram);
         for (int j = 0; j < 20; j++) {
           xstar[j]=new BitSet(78);
            for (int i = 0; i < 78; i++) {
                xstar[j].set(i, r.nextBoolean());
            }         
         result=krig.interp(xstar);
         System.out.println(result[0]+" "+result[1]);
       }
         
         
   }


}
