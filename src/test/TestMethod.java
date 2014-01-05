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
import Jama.*;
import GA.matrix;
/**
 *
 * @author vietlink
 */
public class TestMethod {

   static public void main (String[] args){
      double[][] mat= new double[1][10];
       for (int i = 0; i < 1; i++) {
           for (int j = 0; j < 10; j++) {
               mat[i][j]= j;
           }
       }
      Matrix A= matrix.creatMatrix(1, 10, mat);
      
      Matrix C=A.times(A.transpose());
      System.out.println("rows: "+ A.getRowDimension());
      System.out.println("cols: "+A.getColumnDimension());      
      System.out.println("rows: "+ C.getRowDimension());
      System.out.println("cols: "+C.getColumnDimension());      
      for (int i = 0; i < 1; i++) {
           for (int j = 0; j < 10; j++) {
               System.out.println(C.get(i, j)+" ");
           }
      }
   }


}
