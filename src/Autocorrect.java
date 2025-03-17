import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Autocorrect
 * <p>
 * A command-line tool to suggest similar words when given one not in the dictionary.
 * </p>
 * @author Zach Blick
 * @author Caden Chock
 */
public class Autocorrect {
    String[] dctionary;
    int threshold;
    String[] dictionary;
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
        ArrayList<Integer>[] table = new ArrayList[784];

    }

    public int editDistance(String a, String b){
        int[][] saved = new int[a.length()+1][b.length()+1];
        //Base Case
        for(int i = 0 ; i < a.length(); i++){
            saved[0][i] = i;
        }
        for(int i = 0; i < b.length(); i++){
            saved[i][0] = i;
        }
        for(int i = 1; i < a.length()+1; i++){
            for(int j = 1; j < b.length()+1; j++){
                if(a.charAt(i) == b.charAt(j)){
                    saved[i][j] = saved[i-1][j-1];
                }
                else{
                    int temp = Math.min(saved[i-1][j-1],saved[i-1][j]);
                    saved[i][j] = Math.min(temp, saved[i][j-1]);
                }
            }
        }
        return 0;
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
        for(int i = 0; i < dictionary.length; i++){
            int edit = editDistance(dictionary[i], typed);
            if(edit <= threshold){
                words[edit].add(dictionary[i]);
            }
        }
        return new String[0];
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