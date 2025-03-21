import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import static java.util.Arrays.sort;

/**
 * Autocorrect
 * <p>
 * A command-line tool to suggest similar words when given one not in the dictionary.
 * </p>
 * @author Zach Blick
 * @author Caden Chock
 */
public class Autocorrect {
    // Half Extended ACII * ASCII z + ASCII z;
    public static final int MAX_HASH = 15739;
    public static final int HALF_ASCII = 128;
    String[] dctionary;
    int threshold;
    String[] dictionary;
    ArrayList<String>[] table;
    /**
     * Constucts an instance of the Autocorrect class.
     * @param words The dictionary of acceptable words.
     * @param threshold The maximum number of edits a suggestion can have.
     */
    public Autocorrect(String[] words, int threshold) {
        dictionary = words;
       this.threshold = threshold;
        // Make Trie of Dictionary
        // Hash Key Pair

        //Hash Table 28^2. 26 Letters + dash and apostrophe
        table = new ArrayList[MAX_HASH];
        for(int i = 0; i < MAX_HASH; i++){
            table[i] = new ArrayList<String>();
        }

    }

    public String[] Candidates(String[] dictionary, int threshold, String typo){
        HashSet<String> can = new HashSet<String>();

        // Fill up Table with Words at the right spots
        for(int i = 0; i < dictionary.length; i++){
            for(int j = 0; j < dictionary[i].length() -1; j++){
                table[Hash(dictionary[i].substring(j,j+2))].add(dictionary[i]);
            }
        }
        // Get Hash of the mistyped word
        for(int i = 0; i < typo.length()-1; i++){
            can.addAll(table[Hash(typo.substring(i,i+2))]);
        }
        String[] candidates = new String[can.size()];
        candidates = can.toArray(candidates);
        return candidates;
    }

    public int Hash(String segment){
        int hash = 0;
        // 256 is Length of Extended ASCII
        for(int i = 0; i < segment.length(); i++){
            hash = (HALF_ASCII * hash + segment.charAt(i));
        }
        return hash;
    }

    public int editDistance(String a, String b){
        int[][] saved = new int[a.length()+1][b.length()+1];
        //Base Case
        for(int i = 0 ; i < a.length()+1; i++){
            saved[i][0] = i;
        }
        for(int i = 0; i < b.length()+1; i++){
            saved[0][i] = i;
        }
        for(int i = 1; i < a.length()+1; i++){
            for(int j = 1; j < b.length()+1; j++){
                if(a.charAt(i-1) == b.charAt(j-1)){
                    saved[i][j] = saved[i-1][j-1];
                }
                else{
                    saved[i][j] = Math.min((saved[i][j-1]),(Math.min(saved[i-1][j-1],saved[i-1][j]))) +1;
                }
            }
        }
        return saved[a.length()][b.length()];
    }


    /**
     * Runs a test from the tester file, AutocorrectTester.
     * @param typed The (potentially) misspelled word, provided by the user.
     * @return An array of all dictionary words with an edit distance less than or equal
     * to threshold, sorted by edit distnace, then sorted alphabetically.
     */
    public String[] runTest(String typed) {
        // Make Array Arraylist to hold Possible Suggestions
        ArrayList<String>[] words = new ArrayList[threshold];
        int newLength = 0;
        for(int i = 0; i < threshold; i++){
            words[i] = new ArrayList<String>();
        }
        String[] candidates = Candidates(dictionary, threshold, typed);
        for(int i = 0; i < candidates.length; i++){
            int edit = editDistance(candidates[i], typed);
            if(edit <= threshold){
                words[edit - 1].add(candidates[i]);
                newLength++;
            }
        }
        String[] list = new String[newLength];
        int i = 0;
        for(ArrayList<String> l: words){
            Collections.sort(l);
            while(!l.isEmpty()){
                list[i] = l.remove(0);
                i++;
            }
        }
        return list;
    }


    /**
     * Loads a dictionary of words from the provided textfiles in the dictionaries directory.
     * @param dictionary The name of the textfile, [dictionary].txt, in the dictionaries directory.
     * @return An array of Strings containing all words in alphabetical order.
     */
    private static String[] loadDictionary(String dictionary)  {
        try {
            String line;
            BufferedReader dictReader = new BufferedReader(new FileReader("dictionaries/" + dictionary + ".txt"));
            line = dictReader.readLine();

            // Update instance variables with test data
            int n = Integer.parseInt(line);
            String[] words = new String[n];

            for (int i = 0; i < n; i++) {
                line = dictReader.readLine();
                words[i] = line;
            }
            return words;
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}