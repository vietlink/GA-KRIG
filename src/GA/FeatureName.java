/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GA;

import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author vietlink
 */
public class FeatureName {
    public static String[] FEATURES_NAME={
    "CurrentWord", "PrevWord", "PrevWord2", "NextWord", "NextWord2",
    "PosTag", "PosTagPrev", "PosTagPrev2", "PosTagNext", "PosTagNext2",
    "Prefix", "PrefixPrev", "PrefixPrev2", "PrefixNext", "PrefixNext2",
    "Suffix", "SuffixPrev", "SuffixPrev2", "SuffixNext", "SuffixNext2",
    "FirstWord", "FirstWordPrev", "FirstWordNext", "LengthOfWord", "LengthOfWordPrev",
    "LengthOfWordPrev2", "LengthOfWordNext", "LengthOfWordNext2", "InfrequentWord",
    "InfrequentWordPrev", "InfrequentWordPrev2", "InfrequentWordNext", "InfrequentWordNext2",
    "Capitalization", "CapitalizationNext", "CapitalizationNext2", "CapitalizationPrev", 
    "CapitalizationPrev2", "DigitAndSymbol", "DigitAndSymbolPrev", "DigitAndSymbolPrev2",
    "DigitAndSymbolNext", "DigitAndSymbolNext2", "PostagPair", "WordPair", "IsUrl", "IsUrlPrev",
    "IsUrlPrev2", "IsUrlNext", "IsUrlNext2", "AllLowerCase", "AllLowerCaseNext", "AllLowerCaseNext2",
    "AllLowerCasePrev", "AllLowerCasePrev2", "AllCap", "AllCapNext", "AllCapNext2", "AllCapPrev","AllCapPrev2",
    "Bigram", "StopWord", "PrevTagAndCWord", "InPerDict", "PrevInPerDict", "NextInPerDict", "Prev2InPerDict",
    "Next2InPerDict", "InLocDict", "PrevInLocDict", "NextInLocDict", "Prev2InLocDict", "Next2InLocDict", "InOrgDict",
    "PrevInOrgDict", "NextInOrgDict", "Prev2InOrgDict", "Next2InOrgDict"
    };

    public static ArrayList getFEATURES_NAME() {
        return new ArrayList<String>(Arrays.asList(FEATURES_NAME));
    }
}
