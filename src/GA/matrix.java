/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GA;
import Jama.*;
/**
 *
 * @author vietlink
 */
public class matrix {
    public static Matrix creatMatrix (int nrows, int ncols, double[][] mat){
        double M[][]= new double[nrows][ncols];
        M=mat;
        return new Matrix(M);
    }
}
