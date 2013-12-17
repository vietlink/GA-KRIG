/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GA;
import java.util.BitSet;
import java.util.Random;
/**
 *
 * @author vietlink
 */
public class Chromosome {
    private BitSet encodedFeature;
    private double fitness;
    private double fitness_err;

    public BitSet getEncodedFeature() {
        return encodedFeature;
    }

    public void setEncodedFeature(BitSet encodedFeature) {
        this.encodedFeature = encodedFeature;
    }

    public double getFitness() {
        return fitness;
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    public double getFitness_err() {
        return fitness_err;
    }

    public void setFitness_err(double fitness_err) {
        this.fitness_err = fitness_err;
    }
    
    public Chromosome(double fitness){
        this.fitness= fitness;
        encodedFeature= new BitSet(78);
    }
    public Chromosome(){
        fitness=0.0;
        fitness_err=0.0;
        encodedFeature= new BitSet(78);
        Random r= new Random();
        for (int i = 0; i < 78; i++) {
            if (r.nextDouble()>=0.5)
            {
                encodedFeature.set(i, true);
            } 
            else
            {
                encodedFeature.set(i, false);
            }
        }
    }
}
