package logic.utility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RabinKarpStringPatternMatching {
    public static RabinKarpStringPatternMatching rabinKarpStringPatternMatching = null;

    public static RabinKarpStringPatternMatching getRabinKarpStringPatternMatching(){
        if(rabinKarpStringPatternMatching == null)
            rabinKarpStringPatternMatching = new RabinKarpStringPatternMatching();

        return rabinKarpStringPatternMatching;
    }

    private final static String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
            "abcdefghijklmnopqrstuvwxyz·Èˆ˚ı¸Û˙¡…⁄÷€’”‹" +
            "0123456789" +
            " ?!_-+=.,:;\"'/\\$%^&*()[]{}<>¨`??@~#\n\t";
    private static Map<Character, Integer> alphabetCode;

    //this function initializes the encoding. every character in the alphabet paired with an int from the
    // [0, alphabetString.length - 1] interval
    private void initialize(){
        alphabetCode = new HashMap<>();
        for (int i = 0; i < alphabet.length(); i++){
            alphabetCode.put(alphabet.charAt(i), i);
        }
        //for testing purposes
//        for (Map.Entry<Character, Integer> ac: alphabetCode.entrySet()){
//            System.out.println(ac.getKey() + " - " + ac.getValue());
//        }
    }
    // d is the base of the used number system, q is the chosen prime number
    public List<Integer> stringPatternMatching(String text, String pattern, int d, int q){
        initialize();//we need to initialize the alphabetCodes first
        List<Integer> matchingIndices = new ArrayList<>();//that will store the matching indices for later work
        int n = text.length();
        int m = pattern.length();
        int h = (int) (Math.pow(d, m-1) % q); //stores an m digit number's biggest local value's division remainder with q
        int p = 0; //remainder
        int t = 0; //stores the division remainder with q of the original text's m length substring's code from the
        //s+1th position
        if(n >= m){
            for (int i = 0; i < m; i++){
                //it calculates the pattern's code's modulo q
                p = (d * p + alphabetCode.get(pattern.charAt(i))) % q; //it does the encoding on the fly
                //it calculate the encoded text's first m length substring's modulo q
                t = (d * t + alphabetCode.get(text.charAt(i))) % q;
            }
            //the matching part
            for (int s = 0; s < n - m + 1; s++){
                if(p == t){//if their division remainder is the same, we maybe found a match
                    if(pattern.compareTo(text.substring(s, s + m)) == 0){ //test if the two strings are the same
                        for (int i = s; i < s + m; i++){
                            matchingIndices.add(i); //if we found a matching sequence, we want to store it
                        }                           //because that later we will know which characters to color
                    }
                }
                if(s < n - m){ //if we don't reached the end of the text yet
                    t = (d * (t - alphabetCode.get(text.charAt(s)) * h) + alphabetCode.get(text.charAt(s + m))) % q;
                    if (t < 0) //we have to make sure that t is always stays non negative
                        t = t + q;
                }
            }

        }
        return matchingIndices; //give the matching indices to the controller, it will color these indices
    }

}
