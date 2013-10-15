/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GA;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author vietlink
 */
public class Datum {
    String word;
    String type;
    String pos;

    public String getWord() {
        return word;
    }

    public String getType() {
        return type;
    }

    public String getPos() {
        return pos;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setPos(String pos) {
        this.pos = pos;
    }
    
   // List<Feature> features=new ArrayList<Feature>();
    public HashMap<String, String> featureMap=new HashMap();

    public Datum( ){
        word="~";
        pos="~";
    };
    public Datum(String word) {
        this.word = word;
        
    }

    public Datum(String word, String type) {
        this.word = word;
        this.type = type;
    }

    public Datum(String word, String type, String pos) {
        if(word==null||word.equals("")) word="~~~";
        this.word = word;
        this.type = type;
        this.pos = pos;
    }
}
