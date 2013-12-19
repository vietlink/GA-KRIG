/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import GA.CalFitness;
import GA.Chromosome;
import java.util.ArrayList;
import java.util.List;
import util.Random_util;
/**
 *
 * @author vietlink
 */
public class TestMethod {
//   static {
//       System.load("/home/vietlink/NetBeansProjects/JNIDemoCd1/dist/libJNIDemoCdl.so");
//   }
   static public void main (String[] args){
     Random_util r= new Random_util();
     r.Set_Seed(1543);
     int t= r.Flip(0.3133);
     System.out.print(t);
   }

//    private native void InitRanGen(); 
//    private native void Set_Seed(int a);
//    private native void Set_Seed(long a);
//    private native double Next_Gaussian(double a, double b);
//    private native double Next_Double();
//    private native double Next_Gaussian();
//    private native boolean Flip(double a);
//    private native int IRandom(int a, int b);
}
