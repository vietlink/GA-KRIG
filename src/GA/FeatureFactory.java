/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GA;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Arrays;

import util.FileOperation;
/**
 *
 * @author vietlink
 */
public class FeatureFactory {
    public static List<String> perDict = new ArrayList<>();
    public static List<String> locDict = new ArrayList<>();
    public static List<String> orgDict = new ArrayList<>();
    public static String title="Mr.,Mrs.,Ms.,Dr.,Prof.";
    public static String stopWord="a~" +
"about~" +
"above~" +
"after~" +
"again~" +
"against~" +
"all~" +
"am~" +
"an~" +
"and~" +
"any~" +
"are~" +
"aren't~" +
"as~" +
"at~" +
"be~" +
"because~" +
"been~" +
"before~" +
"being~" +
"below~" +
"between~" +
"both~" +
"but~" +
"by~" +
"can't~" +
"cannot~" +
"could~" +
"couldn't~" +
"did~" +
"didn't~" +
"do~" +
"does~" +
"doesn't~" +
"doing~" +
"don't~" +
"down~" +
"during~" +
"each~" +
"few~" +
"for~" +
"from~" +
"further~" +
"had~" +
"hadn't~" +
"has~" +
"hasn't~" +
"have~" +
"haven't~" +
"having~" +
"he~" +
"he'd~" +
"he'll~" +
"he's~" +
"her~" +
"here~" +
"here's~" +
"hers~" +
"herself~" +"him~" +"himself~" +"his~" +"how~" +"how's~" +"i~" +"i'd~" +"i'll~" +"i'm~" +"i've~" +"if~" +"in~" +"into~" +"is~" +"isn't~" +"it~" +"it's~" +"its~" +
"itself~" +"let's~" +"me~" +"more~" +"most~" +"mustn't~" +"my~" +"myself~" +"no~" +"nor~" +"not~" +"of~" +
"off~" +"on~" +"once~" +"only~" +"or~" +"other~" +"ought~" +"our~" +"ours ";
    
    public FeatureFactory()
    {
        
    }
    
    public  List<Datum> computeFeature(List<Datum> datums, String featureNames) throws IOException
    {
        perDict = FileOperation.readDictFromFile("perDict");
        locDict = FileOperation.readDictFromFile("locDict");
        orgDict = FileOperation.readDictFromFile("orgDict");
        String[] temp = featureNames.split("~");
        List<String> featureList= Arrays.asList(temp);
        HashMap<String, Integer>  hm=new HashMap<String, Integer>();
        HashMap<String, Integer>  bigram=new HashMap<String, Integer>();
        for(int i=0;i<datums.size();i++)
        {
            Datum d=datums.get(i);
            String w=d.word;
            
            if(hm.get(w)==null) hm.put(w, 1);
            else hm.put(w, hm.get(w)+1);
            String p="~";
            if(i>=1) p=datums.get(i-1).word;
            String bi=p+"~"+w;
            if(bigram.get(bi)==null) bigram.put(bi,1);
            else bigram.put(bi, bigram.get(bi)+1);
        }
        hm.put("~",1);
        for(int i=0;i<datums.size();i++)
        {
            Datum d=datums.get(i);
            Datum prev=new Datum();
            Datum prev2=new Datum();
           if(i>=1)  prev=datums.get(i-1);
           if(i>=2)  prev2=datums.get(i-2);
           Datum next=new Datum();
           Datum next2=new Datum();
           if(i<=datums.size()-2)  next=datums.get(i+1);
           if(i<=datums.size()-3)  next2=datums.get(i+2);
            String tag=d.pos;
            String w=d.word;
            String p="~";
            if(i>=1) p=datums.get(i-1).word;
            String n="~";
            if(i<datums.size()-1) n=datums.get(i+1).word;
            String p2=prev2.word;
            String n2=next2.word;
            for(String fName: featureList)
            {
                switch (fName) {
                    case "PrevWord":
                        d.featureMap.put("PrevWord", p);
                        break;
                    case "PrevWord2":
                        d.featureMap.put("PrevWord2", p2);
                        break;
                    case "CurrentWord":
                        d.featureMap.put("CurrentWord", w);
                        break;
                    case "NextWord":
                        d.featureMap.put("NextWord", n);
                        break;
                    case "NextWord2":
                        d.featureMap.put("NextWord2", n2);
                        break;
                    case "PosTag":
                        d.featureMap.put("PosTag", tag);
                        break;
                    case "PosTagPrev":
                        d.featureMap.put("PosTagPrev", prev.pos);
                        break;
                    case "PosTagPrev2":
                        d.featureMap.put("PosTagPrev2", prev2.pos);
                        break;
                    case "PosTagNext":
                        d.featureMap.put("PosTagNext", next.pos);
                        break;
                    case "PosTagNext2":
                        d.featureMap.put("PosTagNext2", next2.pos);
                        break;
                        
                    case "Prefix":
                        if(w.length()>=3){
                            d.featureMap.put("Pre1", w.substring(0,1));
                            d.featureMap.put("Pre2", w.substring(0,2));
                            d.featureMap.put("Pre3", w.substring(0,3));
                        }
                        else d.featureMap.put("Prefix", "Prev_ND");
                        break;
                    case "PrefixPrev":
                        if(p.length()>=3){
                            d.featureMap.put("PreP-1", p.substring(0,1));
                            d.featureMap.put("PreP-2", p.substring(0,2));
                            d.featureMap.put("PreP-3", p.substring(0,3));
                        }
                        else d.featureMap.put("PrefixPrev", "Prev_ND");
                        break;
                    case "PrefixPrev2":
                        if(p2.length()>=3){
                            d.featureMap.put("PreP2-1", p2.substring(0,1));
                            d.featureMap.put("PreP2-2", p2.substring(0,2));
                            d.featureMap.put("PreP2-3", p2.substring(0,3));
                        }
                        else d.featureMap.put("PrefixPrev2", "Prev_ND");
                        break;
                    case "PrefixNext":
                        if(n.length()>=3){
                            d.featureMap.put("PreN1", n.substring(0,1));
                            d.featureMap.put("PreN2", n.substring(0,2));
                            d.featureMap.put("PreN3", n.substring(0,3));
                        }
                        else d.featureMap.put("PrefixNext", "Prev_ND");
                        break;
                    case "PrefixNext2":
                        if(n2.length()>=3){
                            d.featureMap.put("PreN2-1", n2.substring(0,1));
                            d.featureMap.put("PreN2-2", n2.substring(0,2));
                            d.featureMap.put("PreN2-3", n2.substring(0,3));
                        }
                        else d.featureMap.put("PrefixNext2", "Prev_ND");
                        break;
                    case "Suffix":
                        if(w.length()>=3){
                            d.featureMap.put("Suf1", w.substring(w.length()-1,w.length()));
                            d.featureMap.put("Suf2", w.substring(w.length()-2,w.length()));
                            d.featureMap.put("Suf3", w.substring(w.length()-3,w.length()));
                        }
                        else d.featureMap.put("Suffix", "Suf_ND");
                        break;
                    case "SuffixPrev":
                        if(p.length()>=3){
                            d.featureMap.put("SufP1", p.substring(p.length()-1,p.length()));
                            d.featureMap.put("SufP2", p.substring(p.length()-2,p.length()));
                            d.featureMap.put("SufP3", p.substring(p.length()-3,p.length()));
                        }
                        else d.featureMap.put("SuffixPrev", "Suf_ND");
                        break;
                    case "SuffixPrev2":
                        if(p2.length()>=3){
                            d.featureMap.put("SufP2-1", p2.substring(p2.length()-1,p2.length()));
                            d.featureMap.put("SufP2-2", p2.substring(p2.length()-2,p2.length()));
                            d.featureMap.put("SufP2-3", p2.substring(p2.length()-3,p2.length()));
                        }
                        else d.featureMap.put("SuffixPrev2", "Suf_ND");
                        break;
                    case "SuffixNext":
                        if(n.length()>=3){
                            d.featureMap.put("SufN1", n.substring(n.length()-1,n.length()));
                            d.featureMap.put("SufN2", n.substring(n.length()-2,n.length()));
                            d.featureMap.put("SufN3", n.substring(n.length()-3,n.length()));
                        }
                        else d.featureMap.put("SuffixNext", "Suf_ND");
                        break;
                    case "SuffixNext2":
                        if(n2.length()>=3){
                            d.featureMap.put("SufN2-1", n2.substring(n2.length()-1,n2.length()));
                            d.featureMap.put("Suf2-2", n2.substring(n2.length()-2,n2.length()));
                            d.featureMap.put("Suf2-3", n2.substring(n2.length()-3,n2.length()));
                        }
                        else d.featureMap.put("SuffixNext2", "Suf_ND");
                        break;
                    case "FirstWord":
                        if(p.charAt(p.length()-1)=='.' && !title.contains(p) && 
                                Character.isUpperCase(w.charAt(0)))
                            d.featureMap.put("FirstWord","1");
                        else d.featureMap.put("FirstWord","0");
                        break;
                    case "FirstWordPrev":
                        if(p2.charAt(p2.length()-1)=='.' && !title.contains(p2) && 
                                Character.isUpperCase(p.charAt(0)))
                            d.featureMap.put("FirstWordPrev","1");
                        else d.featureMap.put("FirstWordPrev","0");
                        break;
                    case "FirstWordNext":
                        if(n2.charAt(n2.length()-1)=='.' && !title.contains(n2) && 
                                Character.isUpperCase(n.charAt(0)))
                            d.featureMap.put("FirstWordNext","1");
                        else d.featureMap.put("FirstWordNext","0");
                        break;
                    case "LengthOfWord":
                        if(w.length()>5) d.featureMap.put("LengthofWord","1");
                        else d.featureMap.put("LengthofWord","0");
                        break;
                    case "LengthOfWordPrev":
                        if(p.length()>5) d.featureMap.put("LengthofWordPrev","1");
                        else d.featureMap.put("LengthofWordPrev","0");
                        break;
                    case "LengthOfWordPrev2":
                        if(p2.length()>5) d.featureMap.put("LengthofWordPrev2","1");
                        else d.featureMap.put("LengthofWordPrev2","0");
                        break;
                    case "LengthOfWordNext":
                        if(n.length()>5) d.featureMap.put("LengthofWordNext","1");
                        else d.featureMap.put("LengthofWordNext","0");
                        break;
                    case "LengthOfWordNext2":
                        if(n2.length()>5) d.featureMap.put("LengthofWordNext2","1");
                        else d.featureMap.put("LengthofWordNext2","0");
                        break;
                    case "InfrequentWord":
                        if(hm.get(w)>9) d.featureMap.put("InfrequentWord","0");
                        else d.featureMap.put("InfrequentWord","1");
                        break;
                    case "InfrequentWordPrev":
                        if(hm.get(p)>9) d.featureMap.put("InfrequentWordPrev","0");
                        else d.featureMap.put("InfrequentWordPrev","1");
                        break;
                    case "InfrequentWordPrev2":
                        if(hm.get(p2)>9) d.featureMap.put("InfrequentWordPrev2","0");
                        else d.featureMap.put("InfrequentWordPrev2","1");
                        break;
                    case "InfrequentWordNext":
                        if(hm.get(n)>9) d.featureMap.put("InfrequentWordNext","0");
                        else d.featureMap.put("InfrequentWordNext","1");
                        break;
                    case "InfrequentWordNext2":
                        if(hm.get(n2)>9) d.featureMap.put("InfrequentWordNext2","0");
                        else d.featureMap.put("InfrequentWordNext2","1");
                        break;
                    case "Capitalization":
                        if(Character.isUpperCase(w.charAt(0)))
                            d.featureMap.put("Capitalization", "1");
                        else d.featureMap.put("Capitalization", "0");
                        break;
                    case "CapitalizationPrev":
                        if(Character.isUpperCase(p.charAt(0)))
                            d.featureMap.put("CapitalizationPrev", "1");
                        else d.featureMap.put("CapitalizationPrev", "0");
                        break;
                    case "CapitalizationPrev2":
                        if(Character.isUpperCase(p2.charAt(0)))
                            d.featureMap.put("CapitalizationPrev2", "1");
                        else d.featureMap.put("CapitalizationPrev2", "0");
                        break;
                    case "CapitalizationNext":
                        if(Character.isUpperCase(n.charAt(0)))
                            d.featureMap.put("CapitalizationNext", "1");
                        else d.featureMap.put("CapitalizationNext", "0");
                        break;
                    case "CapitalizationNext2":
                        if(Character.isUpperCase(n2.charAt(0)))
                            d.featureMap.put("CapitalizationNext2", "1");
                        else d.featureMap.put("CapitalizationNext2", "0");
                        break;
                    case "DigitAndSymbol":
                        d.featureMap.put("DigitAndComma", String.valueOf(containDigitAndComma(w)));
                        d.featureMap.put("DigitAndHyphen", String.valueOf(containDigitAndHyphen(w)));
                        d.featureMap.put("DigitAndPercentage", String.valueOf(containDigitAndPercentage(w)));
                        d.featureMap.put("DigitAndPeriod", String.valueOf(containDigitAndPeriod(w)));
                        d.featureMap.put("DigitAndSlash", String.valueOf(containDigitAndSlash(w)));
                        d.featureMap.put("ContainFourDigit", String.valueOf(containFourDigit(w)));
                        break;
                    case "DigitAndSymbolPrev2":
                        d.featureMap.put("DigitAndCommaPrev2", String.valueOf(containDigitAndComma(p2)));
                        d.featureMap.put("DigitAndHyphenPrev2", String.valueOf(containDigitAndHyphen(p2)));
                        d.featureMap.put("DigitAndPercentagePrev2", String.valueOf(containDigitAndPercentage(p2)));
                        d.featureMap.put("DigitAndPeriodPrev2", String.valueOf(containDigitAndPeriod(p2)));
                        d.featureMap.put("DigitAndSlashPrev2", String.valueOf(containDigitAndSlash(p2)));
                        d.featureMap.put("ContainFourDigitPrev2", String.valueOf(containFourDigit(p2)));
                        break;
                    case "DigitAndSymbolPrev":
                        d.featureMap.put("DigitAndCommaPrev", String.valueOf(containDigitAndComma(p)));
                        d.featureMap.put("DigitAndHyphenPrev", String.valueOf(containDigitAndHyphen(p)));
                        d.featureMap.put("DigitAndPercentagePrev", String.valueOf(containDigitAndPercentage(p)));
                        d.featureMap.put("DigitAndPeriodPrev", String.valueOf(containDigitAndPeriod(p)));
                        d.featureMap.put("DigitAndSlashPrev", String.valueOf(containDigitAndSlash(p)));
                        d.featureMap.put("ContainFourDigitPrev", String.valueOf(containFourDigit(p)));
                        break;
                    case "DigitAndSymbolNext":
                        d.featureMap.put("DigitAndCommaNext", String.valueOf(containDigitAndComma(n)));
                        d.featureMap.put("DigitAndHyphenNext", String.valueOf(containDigitAndHyphen(n)));
                        d.featureMap.put("DigitAndPercentageNext", String.valueOf(containDigitAndPercentage(n)));
                        d.featureMap.put("DigitAndPeriodNext", String.valueOf(containDigitAndPeriod(n)));
                        d.featureMap.put("DigitAndSlashNext", String.valueOf(containDigitAndSlash(n)));
                        d.featureMap.put("ContainFourDigitNext", String.valueOf(containFourDigit(n)));
                        break;
                    case "DigitAndSymbolNext2":
                        d.featureMap.put("DigitAndCommaNext2", String.valueOf(containDigitAndComma(n2)));
                        d.featureMap.put("DigitAndHyphenNext2", String.valueOf(containDigitAndHyphen(n2)));
                        d.featureMap.put("DigitAndPercentageNext2", String.valueOf(containDigitAndPercentage(n2)));
                        d.featureMap.put("DigitAndPeriodNext2", String.valueOf(containDigitAndPeriod(n2)));
                        d.featureMap.put("DigitAndSlashNext2", String.valueOf(containDigitAndSlash(n2)));
                        d.featureMap.put("ContainFourDigitNext2", String.valueOf(containFourDigit(n2)));
                        break;
                        
                    case "PostagPair":
                        if(i>=1) d.featureMap.put("PWPostag",datums.get(i-1).pos+"~"+d.pos);
                        else d.featureMap.put("PWPostag", "ND");
                        if(i<datums.size()-1) d.featureMap.put("WNPostag",datums.get(i+1).pos+"~"+d.pos);
                        else d.featureMap.put("WNPostag","ND");
                        if(i>=1 && i< datums.size()-1)
                            d.featureMap.put("PNPostag", prev.pos+"~"+next.pos);
                        else d.featureMap.put("PNPostag", "ND");
                        break;
                    case "WordPair":
                        d.featureMap.put("WordPairPW",w+p+"PW");
                        d.featureMap.put("WordPairWN",w+n+"WN");
                        d.featureMap.put("WordPairPN",p+n+"PN");
                        break;
                    case "AllCap":
                        if(w.toUpperCase().equals(w)) d.featureMap.put("AllCap","1-AllCap");
                        else d.featureMap.put("AllCap","0-AllCap");
                        break;
                    case "AllCapPrev":
                        if(p.toUpperCase().equals(p)) d.featureMap.put("AllCapPrev","1-AllCap");
                        else d.featureMap.put("AllCapPrev","0-AllCap");
                        break;
                    case "AllCapPrev2":
                        if(p2.toUpperCase().equals(p2)) d.featureMap.put("AllCapPrev2","1-AllCap");
                        else d.featureMap.put("AllCapPrev2","0-AllCap");
                        break;
                    case "AllCapNext":
                        if(n.toUpperCase().equals(n)) d.featureMap.put("AllCapNext","1-AllCap");
                        else d.featureMap.put("AllCapNext","0-AllCap");
                        break;
                    case "AllCapNext2":
                        if(n2.toUpperCase().equals(n2)) d.featureMap.put("AllCapNext2","1-AllCap");
                        else d.featureMap.put("AllCapNext2","0-AllCap");
                        break;
                    case "AllLowerCase":
                        if(w.toLowerCase().equals(w)) d.featureMap.put("AllLowerCase", "1-AllLower");
                        else  d.featureMap.put("AllLowerCase", "0-Lower");
                        break;
                    case "AllLowerCasePrev":
                        if(p.toLowerCase().equals(p)) d.featureMap.put("AllLowerCasePrev", "1-AllLower");
                        else  d.featureMap.put("AllLowerCasePrev", "0-Lower");
                        break;
                    case "AllLowerCasePrev2":
                        if(p2.toLowerCase().equals(p2)) d.featureMap.put("AllLowerCasePrev2", "1-AllLower");
                        else  d.featureMap.put("AllLowerCasePrev2", "0-Lower");
                        break;
                    case "AllLowerCaseNext":
                        if(n.toLowerCase().equals(n)) d.featureMap.put("AllLowerCaseNext", "1-AllLower");
                        else  d.featureMap.put("AllLowerCaseNext", "0-Lower");
                        break;
                    case "AllLowerCaseNext2":
                        if(n2.toLowerCase().equals(n2)) d.featureMap.put("AllLowerCaseNext2", "1-AllLower");
                        else  d.featureMap.put("AllLowerCaseNext2", "0-Lower");
                        break;
                    case "IsUrl":
                        if(w.length()>5) 
                            if(w.substring(0,5).equals("http:")) d.featureMap.put("IsUrl","1-IsUrl");
                            else d.featureMap.put("IsUrl","0-IsUrl");
                        else d.featureMap.put("IsUrl","0-IsUrl");
                        break;
                    case "IsUrlPrev":
                        if(p.length()>5) 
                            if(p.substring(0,5).equals("http:")) d.featureMap.put("IsUrlPrev","1-IsUrl");
                            else d.featureMap.put("IsUrlPrev","0-IsUrl");
                        else d.featureMap.put("IsUrlPrev","0-IsUrl");
                        break;
                    case "IsUrlPrev2":
                        if(p2.length()>5) 
                            if(p2.substring(0,5).equals("http:")) d.featureMap.put("IsUrlPrev2","1-IsUrl");
                            else d.featureMap.put("IsUrlPrev2","0-IsUrl");
                        else d.featureMap.put("IsUrlPrev2","0-IsUrl");
                        break;
                    case "IsUrlNext":
                        if(n.length()>5) 
                            if(n.substring(0,5).equals("http:")) d.featureMap.put("IsUrlNext","1-IsUrl");
                            else d.featureMap.put("IsUrlNext","0-IsUrl");
                        else d.featureMap.put("IsUrlNext","0-IsUrl");
                        break;
                    case "IsUrlNext2":
                        if(n2.length()>5) 
                            if(n2.substring(0,5).equals("http:")) d.featureMap.put("IsUrlNext2","1-IsUrl");
                            else d.featureMap.put("IsUrlNext2","0-IsUrl");
                        else d.featureMap.put("IsUrlNext2","0-IsUrl");
                        break;
                    case "Bigram":
                        int t1=(Integer)bigram.get(p+"~"+w);
                        int t2;
                        if(p.equals("~")) t2=1;
                        else t2=(Integer)hm.get(p);
                        d.featureMap.put("Bigram", String.valueOf(t1*100/t2));
                        break;
                    case "StopWord":
                        if(stopWord.contains(w)) d.featureMap.put("StopWord", "1");
                        else  d.featureMap.put("StopWord", "0");
                        if(stopWord.contains(p)) d.featureMap.put("StopWordPrev", "1");
                        else  d.featureMap.put("StopWordPrev", "0");
                        if(stopWord.contains(n)) d.featureMap.put("StopWordNext", "1");
                        else d.featureMap.put("StopWordNext", "0");
                        break;
                    case "PrevTagAndCWord":
                        d.featureMap.put("PrevTagAndCWord",prev.pos+"~"+w);
                        break;
                    case "InPerDict":
                        if(perDict.contains(w)) d.featureMap.put("InPerDict","1");
                        else d.featureMap.put("InPerDict","0");
                        break;
                    case "NextInPerDict":
                        if(perDict.contains(n)) d.featureMap.put("NextInPerDict","1");
                        else d.featureMap.put("InPerDict","0");
                        break;
                    case "Next2InPerDict":
                        if(perDict.contains(n2)) d.featureMap.put("Next2InPerDict","1");
                        else d.featureMap.put("InPerDict","0");
                        break;
                    case "PrevInPerDict":
                        if(perDict.contains(p)) d.featureMap.put("PrevInPerDict","1");
                        else d.featureMap.put("PrevInPerDict","0");
                        break;
                    case "Prev2InPerDict":
                        if(perDict.contains(p2)) d.featureMap.put("Prev2InPerDict","1");
                        else d.featureMap.put("Prev2InPerDict","0");
                        break;
                        
                        
                    case "InLocDict":
                        if(locDict.contains(w)) d.featureMap.put("InLocDict","1");
                        else d.featureMap.put("InLocDict","0");
                        break;
                    case "NextInLocDict":
                        if(locDict.contains(n)) d.featureMap.put("NextInLocDict","1");
                        else d.featureMap.put("InLocDict","0");
                        break;
                    case "Next2InLocDict":
                        if(locDict.contains(n2)) d.featureMap.put("Next2InLocDict","1");
                        else d.featureMap.put("InLocDict","0");
                        break;
                    case "PrevInLocDict":
                        if(locDict.contains(p)) d.featureMap.put("PrevInLocDict","1");
                        else d.featureMap.put("PrevInLocDict","0");
                        break;
                    case "Prev2InLocDict":
                        if(locDict.contains(p2)) d.featureMap.put("Prev2InLocDict","1");
                        else d.featureMap.put("Prev2InLocDict","0");
                        break;
                        
                        
                    case "InOrgDict":
                        if(orgDict.contains(w)) d.featureMap.put("InOrgDict","1");
                        else d.featureMap.put("InOrgDict","0");
                        break;
                    case "NextInOrgDict":
                        if(orgDict.contains(n)) d.featureMap.put("NextInOrgDict","1");
                        else d.featureMap.put("InOrgDict","0");
                        break;
                    case "Next2InOrgDict":
                        if(orgDict.contains(n2)) d.featureMap.put("Next2InOrgDict","1");
                        else d.featureMap.put("InOrgDict","0");
                        break;
                    case "PrevInOrgDict":
                        if(orgDict.contains(p)) d.featureMap.put("PrevInOrgDict","1");
                        else d.featureMap.put("PrevInOrgDict","0");
                        break;
                    case "Prev2InOrgDict":
                        if(orgDict.contains(p2)) d.featureMap.put("Prev2InOrgDict","1");
                        else d.featureMap.put("Prev2InOrgDict","0");
                        break;
                        
                        
                        
                }
                
            }
        }
//        System.out.println("Compute feature OK");
        return datums;
    }
    public  boolean containDigitAndComma(String w)
    {
        
        boolean containDigit=false;
        boolean containComma=false;
        for(int i=0;i<w.length();i++)
        {
             if(w.charAt(i)==','||w.charAt(i)=='`'||w.charAt(i)=='\'') 
            {
                containComma=true;
               
            }
            if(Character.isDigit(w.charAt(i))) 
            {
                containDigit=true;
                if(containDigit && containComma) break;
            }
            
        } 
      
        return containComma&&containDigit;
    }
    
    public  boolean containFourDigit(String w)
    {
        int count=0;
        for(int i=0;i<w.length();i++)
            if(Character.isDigit(w.charAt(i))) count++;
        return count==4;
    }
    
     public  boolean containDigitAndSlash(String w)
    {
        
        boolean containDigit=false;
        boolean containSlash=false;
        for(int i=0;i<w.length();i++)
        {
             if(w.charAt(i)=='/') 
            {
                containSlash=true;
               
            }
            if(Character.isDigit(w.charAt(i))) 
            {
                containDigit=true;
                if(containDigit && containSlash) break;
            }
            
        } 
      
        return containSlash&&containDigit;
    }
      public  boolean containDigitAndHyphen(String w)
    {
        
        boolean containDigit=false;
        boolean containHyphen=false;
        for(int i=0;i<w.length();i++)
        {
             if(w.charAt(i)=='-') 
            {
                containHyphen=true;
               
            }
            if(Character.isDigit(w.charAt(i))) 
            {
                containDigit=true;
                if(containDigit && containHyphen) break;
            }
            
        } 
      
        return containHyphen&&containDigit;
    }
    
    
    
     public  boolean containDigitAndPeriod(String w)
    {
        
        boolean containDigit=false;
        boolean containPeriod=false;
        for(int i=0;i<w.length();i++)
        {
             if(w.charAt(i)=='.') 
            {
                containPeriod=true;
               
            }
            if(Character.isDigit(w.charAt(i))) 
            {
                containDigit=true;
                if(containDigit && containPeriod) break;
            }
            
        } 
      
        return containPeriod&&containDigit;
    }
    
    
     public  boolean containDigitAndPercentage(String w)
    {
        
        boolean containDigit=false;
        boolean containPercentage=false;
        for(int i=0;i<w.length();i++)
        {
             if(w.charAt(i)=='%') 
            {
                containPercentage=true;
               
            }
            if(Character.isDigit(w.charAt(i))) 
            {
                containDigit=true;
                if(containDigit && containPercentage) break;
            }
            
        } 
      
        return containPercentage&&containDigit;
    }
    
    
    public void writeToFeatureFile(List<Datum> datums,String fileName, String featureNames) throws IOException
    {
       // datums=computeFeature(datums, featureNames);
        File f=new File(fileName);
        String[] temp = featureNames.split("~");
        List<String> featureList= Arrays.asList(temp);
        try (FileWriter fw = new FileWriter(f)) {
            for(Datum d: datums)
            {
                fw.write(d.type+" ");
                for(int i=0;i<featureList.size();i++)
                {
                    fw.write(d.featureMap.get(featureList.get(i))+" ");
                }
                fw.write("\n");
                
            }
            fw.close();
        }
        
        
     //   System.out.println("Write OK");
    }
}
