
package krig;

import static com.snuggy.nr.util.Static.*;
import static java.lang.Math.*;

import java.math.*;

import com.snuggy.nr.refs.*;
import com.snuggy.nr.util.*;

public class LUdcmp {

    private int n;
    private final double[][] lu; // Stores the decomposition.
    private final int[] indx; // Stores the permutation.
    private double d; // Used by det.
    // private LUdcmp(MatDoub_I &a); // Constructor. Argument is the matrix A.
    // private void solve(VecDoub_I &b, VecDoub_O &x); // Solve for a single
    // right-hand side.
    // private void solve(MatDoub_I &b, MatDoub_O &x); // Solve for multiple
    // right-hand sides.
    // private void inverse(MatDoub_O &ainv); // Calculate matrix inverse A1.
    // private Doub det(); // Return determinant of A.
    // private void mprove(VecDoub_I &b, VecDoub_IO &x); // Discussed in 2.5.
    private final double[][] aref; // Used only by mprove.

    public LUdcmp(final double[][] a) throws NRException {
        // Given a matrix a[0..n-1][0..n-1], this routine replaces it by the
        // LU decomposition of a rowwise permutation of itself. a is input.
        // On output, it is arranged as in equation (2.3.14) above;
        // indx[0..n-1] is an output vector that records the row permutation
        // effected by the partial pivoting; d is output as ?1 depending on
        // whether the number of row interchanges was even or odd, respectively.
        // This routine is used in combination with solve to solve linear
        // equations or invert a matrix.

        n = (nrows(a));
        lu = doub_mat(a);
        aref = doub_mat(a);
        indx = int_vec(n);

        final double TINY = 1.0e-40; // A small number.
        int i, imax = 0, j, k;
        double big, temp;
        final double[] vv = doub_vec(n); // vv stores the implicit scaling of each
                                     // row.
        d = 1.0; // No row interchanges yet.
        for (i = 0; i < n; i++) { // Loop over rows to get the implicit scaling
                                  // infor
            big = 0.0; // mation.
            for (j = 0; j < n; j++)
                if ((temp = abs(lu[i][j])) > big)
                    big = temp;
            if (big == 0.0)
                throw new NRException("Singular matrix in LUdcmp");
            // No nonzero largest element.
            vv[i] = 1.0 / big; // Save the scaling.
        }
        for (k = 0; k < n; k++) { // This is the outermost kij loop.
            big = 0.0; // Initialize for the search for largest pivot element.
            for (i = k; i < n; i++) {
                temp = vv[i] * abs(lu[i][k]);
                if (temp > big) { // Is the figure of merit for the pivot better
                                  // than
                    big = temp; // the best so far?
                    imax = i;
                }
            }
            if (k != imax) { // Do we need to interchange rows?
                for (j = 0; j < n; j++) { // Yes, do so...
                    temp = lu[imax][j];
                    lu[imax][j] = lu[k][j];
                    lu[k][j] = temp;
                }
                d = -d; // ...and change the parity of d.
                vv[imax] = vv[k]; // Also interchange the scale factor.
            }
            indx[k] = imax;
            if (lu[k][k] == 0.0)
                lu[k][k] = TINY;
            // If the pivot element is zero, the matrix is singular (at least
            // to the precision of the algorithm). For some applications on
            // singular matrices, it is desirable to substitute TINY for zero.
            for (i = k + 1; i < n; i++) {
                temp = lu[i][k] /= lu[k][k]; // Divide by the pivot element.
                for (j = k + 1; j < n; j++)
                    // Innermost loop: reduce remaining submatrix.
                    lu[i][j] -= temp * lu[k][j];
            }
        }
    }

    // Once the LUdcmp object is constructed, two functions implementing
    // equations (2.3.6) and (2.3.7) are available for solving linear
    // equations. The first solves a single right-hand side vector b for
    // a solution vector x. The second simultaneously solves multiple
    // right-hand vectors, arranged as the columns of a matrix B. In
    // otherwords, it calculates the matrix A1 B.

    public void solve(final double[] b, final double[] x) throws NRException {
        // Solves the set of n linear equations A  x D b using the stored
        // LU decomposition of A. b[0..n-1] is input as the right-hand side
        // vector b, while x returns the solution vector x; b and x may
        // reference the same vector, in which case the solution overwrites
        // the input. This routine takes into account the possibility that
        // b will begin with many zero elements, so it is efficient for use
        // in matrix inversion.
        int i, ii = 0, ip, j;
        double sum;
        if (b.length != n || x.length != n)
            throw new NRException("LUdcmp::solve bad sizes");
        for (i = 0; i < n; i++)
            x[i] = b[i];
        for (i = 0; i < n; i++) { // When ii is set to a positive value, it will
                                  // become the
            // index of the first nonvanishing element of b. We now
            // do the forward substitution, equation (2.3.6). The
            // only new wrinkle is to unscramble the permutation
            // as we go.
            ip = indx[i];
            sum = x[ip];
            x[ip] = x[i];
            if (ii != 0)
                for (j = ii - 1; j < i; j++)
                    sum -= lu[i][j] * x[j];
            else if (sum != 0.0) // A nonzero element was encountered, so from
                                 // now on we
                ii = i + 1; // will have to do the sums in the loop above.
            x[i] = sum;
        }
        for (i = n - 1; i >= 0; i--) { // Now we do the backsubstitution,
                                       // equation (2.3.7).
            sum = x[i];
            for (j = i + 1; j < n; j++)
                sum -= lu[i][j] * x[j];
            x[i] = sum / lu[i][i]; // Store a component of the solution vector
                                   // X.
        } // All done!
    }

    public void solve(final double[][] b, final double[][] x) throws NRException {
        // Solves m sets of n linear equations A  X D B using the stored
        // LU decomposition of A. The matrix b[0..n-1][0..m-1] inputs the
        // right-hand sides, while x[0..n-1][0..m-1] returns the solution
        // A1 B. b and x may reference the same matrix, in which case the
        // solution overwrites the input.
        int i, j, m = ncols(b);
        if (nrows(b) != n || nrows(x) != n || 
                ncols(b) != ncols(x))
            throw new NRException("LUdcmp::solve bad sizes");
        final double[] xx = doub_vec(n);
        for (j = 0; j < m; j++) { // Copy and solve each column in turn.
            for (i = 0; i < n; i++)
                xx[i] = b[i][j];
            solve(xx, xx);
            for (i = 0; i < n; i++)
                x[i][j] = xx[i];
        }
    }

    public void inverse(final $double2d ainv) throws NRException {
        // Using the stored LU decomposition, return in ainv the matrix inverse
        // A1.
        int i, j;
        ainv.$(doub_mat(n, n));
        for (i = 0; i < n; i++) {
            for (j = 0; j < n; j++)
                ainv.$()[i][j] = 0.;
            ainv.$()[i][i] = 1.;
        }
        solve(ainv.$(), ainv.$());
    }

    public double det() {
        // Using the stored LU decomposition, return the determinant of the
        // matrix A.
        double dd = d;
        for (int i = 0; i < n; i++)
            dd *= lu[i][i];
        return dd;
    }

    public void mprove(final double[] b, final double[] x) throws NRException {
        // Improves a solution vector x[0..n-1] of the linear set of equations
        // A  x D b. The vectors b[0..n-1] and x[0..n-1] are input. On output,
        // x[0..n-1] is modified, to an improved set of values.
        int i, j;
        final double[] r = doub_vec(n);
        for (i = 0; i < n; i++) { // Calculate the right-hand side, accumulating
            BigDecimal sdp = new BigDecimal(-b[i]); // the residual in higher
                                                    // precision.
            for (j = 0; j < n; j++)
                // sdp += (Ldoub)aref[i][j] * (Ldoub)x[j];
                sdp = sdp.add(new BigDecimal(aref[i][j]).multiply(new BigDecimal(x[j])));
            r[i] = sdp.doubleValue();
        }
        solve(r, r); // Solve for the error term,
        for (i = 0; i < n; i++)
            x[i] -= r[i]; // and subtract it from the old solution.
    }

}
