/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.util.Random;

/**
 *
 * @author vietlink
 */
public class Random_util {
    private double idum;
    private double IA;
    private double IM;
    private double AM;
    private double IQ;
    private double IR;
    private int NTAB;
    private double NDIV;
    private double EPS;
    private double RNMX;
    private double iy;
    
//    private double[] iv;
    
    public void Random_util(){
        
        idum=(double) -12345;
        IA=(double) 16807;
        IM=(double) 214748364;
        AM=(1.0/IM);
        IQ=127773;
        IR=(double)2836;
        NTAB=32;
        NDIV=(1.0+(IM-1.0)/(double)NTAB);
        EPS=1.2e-7;
        RNMX=(1.0-EPS); //largest double less than 1
        iy=0;
//        iv=new double[NTAB]; 
//        assert(iv!=null);
    }
    public void Set_Seed(int x)
    {
        idum=(double) -x;
    }
    public void Set_Seed(double x)
    {
        idum=-x;
    }
    public double Next_Gaussian(double mean,double stdev)
    {
        double x=(double) Next_Gaussian();
        return (double)(x*stdev+mean);
    }
//    public double Next_Double()
//    {
//        int j;
//        double k;
//        double temp;
//        if (idum<=0 || iy!=0)
//        {
//            if (-idum<1)
//                idum=1;
//            else
//                idum=-idum;
//            for(j=NTAB+7;j>=0;j--)
//            {
//                k=(double)(idum/(double)IQ);
//                idum=IA*(idum-k*IQ)-IR*k;
//                if (idum<0)
//                idum+=IM;
//                if (j<NTAB)
//                    iv[j]=idum;
//            }
//            for (int i=0; i<iv.length; i++){
//                System.out.print(iv[i]);
//            }
//            iy=iv[0];
//        }
//        k=(long)(idum/(double)IQ);
//        idum=IA*(idum-k*IQ)-IR*k;
//        if (idum<0)
//            idum+=IM;
//        j=(int)(iy/NDIV);
//        iy=iv[j];
//        iv[j]=idum;
//        if ((temp=AM*iy)>RNMX)
//            return RNMX;
//        else
//            return temp;
//     }
     public double Next_Gaussian()
     {
        int iset=0;
        double gset=0;
        double fac,rsq,v1,v2;
        Random r= new Random();
        if (iset==0)
        {
            do
            {
                v1=2.0*r.nextDouble()-1.0;
                v2=2.0*r.nextDouble()-1.0;
                rsq=v1*v1+v2*v2;
            }
            while (rsq>=1.0||rsq==0.0);
            fac=Math.sqrt(-2.0*Math.log(rsq)/(double)rsq);
            gset=v1*fac;
            iset=1;
            return v2*fac;
        }
        else
        {
            iset=0;
            return gset;
        }
     }
//return TRUE with a probability
    public boolean Flip(double prob)
    {
        Random r= new Random();
        double temp=r.nextDouble();
        if (temp<=prob)
            return true;
        else 
            return false;
}
//return a random integer between lower and upper
public int IRandom(int lower,int upper)
{
 int temp;
 Random r= new Random();
 temp=lower+ (int)(r.nextDouble()*(upper-lower+1));
  return temp;
}
}
