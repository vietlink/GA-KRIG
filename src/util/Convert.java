/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util;
import GA.FeatureName;
import GA.Chromosome;
/**
 *
 * @author vietlink
 */
public class Convert {
     public static String convertToString (Chromosome c){
        String result= "";
        
        for (int i=0; i<78; i++){
            if (c.getEncodedFeature().get(i)){
                result+=FeatureName.FEATURES_NAME[i].toString()+"~";
            }
        }
        return result;
    }
}
