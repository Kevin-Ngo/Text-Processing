import java.util.ArrayList;
import java.util.HashMap;

/**
 *  @author Kevin Ngo
 *  @date 05/06/18
 *  @file_description: This is a pattern matching class, that simply stores a text file and implements the KMP and BM method for searching.
 *                     In addition, this class, "Pattern_Matching" is coded specifically for "Project #3" at Mt. SAC, assigned by Prof. Tuan Vo. therefore,
 *                     contains methods used to collect various data pertaining to each pattern match.
 */
public class Pattern_Matching
{
    private String text; // whole text file as one string (all lower case and includes whitespaces)
    private ArrayList<MatchingOutput> results; // holds data specific to each time a method is called

    /**
     * Purpose: Constructor, accepts a whole text file as one string
     * @param txt - string containing a whole text file (whitespaces and lower-cased)
     * @return - none
     */
    Pattern_Matching(String txt)
    {
        text = txt;
        results = new ArrayList<>();
    }

    /**
     * Purpose: Scans the text file and tries to match the passed pattern to an identical one in the text. This method uses the KMP way to process the text/pattern
     * @param pattern - pattern to search for in the text
     * @return int - index in the text file where the pattern first starts (if found), if the pattern was not found in the text, the returned int will be -1
     */
    public int findUsingKMP(String pattern)
    {
        if (pattern.length() == 0)
            return 0;

        int textLength = text.length();
        int patternLength = pattern.length();
        int indexInText = 0;
        int indexInPattern = 0;
        int numComparisons = 0;
        long startTime;

        startTime = System.currentTimeMillis();

        int[] failFunction = computeFailKMP(pattern); // Calls helper function to compute the failure array

        while (indexInText < textLength)
        {
            numComparisons++;
            if (text.charAt(indexInText) == pattern.charAt(indexInPattern)) // All characters are now matched
            {
                if (indexInPattern == (patternLength - 1))
                {
                    results.add(new MatchingOutput("Knuth-Morris-Pratt", pattern, textLength, numComparisons, (System.currentTimeMillis() - startTime), true)); // Store info into the ArrayList
                    return indexInText - patternLength + 1;
                }
                indexInText++; // Continue to see if the characters are amtched
                indexInPattern++;
            }
            else if (indexInPattern > 0) // No match, so now the index in the pattern becomes shifted according to the failure array so that characters are not "re-compared"
            {
                indexInPattern = failFunction[indexInPattern-1];
            }
            else // Cannot shift or match any so continue checking
            {
                indexInText++;
            }
        }

        results.add(new MatchingOutput("Knuth-Morris-Pratt", pattern, textLength, numComparisons, (System.currentTimeMillis() - startTime), false)); // Store info into the ArrayList
        return -1;
    }

    /**
     * Purpose: Helper function to compute the failure array so that correct index jumps can be made in the "findUsingKMP" method (basically see if a there is a suffix that is also a prefix)
     * @param pattern - pattern to search for in the text
     * @return int[] - the failure function array, which is used in the "findUsingKMP" method
     */
    protected int[] computeFailKMP(String pattern)
    {
        int patternLength = pattern.length();
        int[] fail = new int[patternLength];
        int i = 1;
        int k = 0;
        while (i<patternLength)
        {
            if (pattern.charAt(i) == pattern.charAt(k))
            {
                fail[i] = k+1;
                k++;
                i++;
            }
            else if (k>0)
            {
                k = fail[k-1];
            }
            else
                i++;
        }
        return fail;
    }

    /**
     * Purpose: Pattern match using the BM method, (last function included)
     * @param pattern - pattern to search for in the text
     * @return int - index in the text file where the pattern first starts (if found), if the pattern was not found in the text, the returned int will be -1
     */
    public int findUsingBM(String pattern)
    {
        if (pattern.length() == 0)
            return 0;

        int patternLength = pattern.length();
        int textLength = text.length();
        int numComparisons = 0;
        long startTime;

        startTime = System.currentTimeMillis();

        HashMap<Character, Integer> lastMap = new HashMap<>(); // Last map to hold all possible alphabetical keys
        for (int i = 0; i<textLength; i++) // Initiates all indexes to -1
            lastMap.put(text.charAt(i), -1);
        for (int j = 0; j<patternLength; j++) // Updates the map by going through the pattern and recording the last time where each characters appear (in the passed pattern)
            lastMap.put(pattern.charAt(j),j);

        int indexInText = patternLength-1; // Looking glass heuristic
        int indexInPattern = patternLength-1;

        while (indexInText < textLength)
        {
            numComparisons++;
            if (text.charAt(indexInText) == pattern.charAt(indexInPattern))
            {
                if (indexInPattern == 0) // All characters were matched
                {
                    results.add(new MatchingOutput("Boyer-Moore", pattern, textLength, numComparisons, (System.currentTimeMillis() - startTime), true));
                    return indexInText;
                }
                indexInPattern--;
                indexInText--;
            }
            else
            {
                indexInText += patternLength - Math.min(indexInPattern, 1 + lastMap.get(text.charAt(indexInText))); // Shift forward depending on where the last time the character was seen in the pattern
                indexInPattern = patternLength - 1;
            }
        }
        results.add(new MatchingOutput("Boyer-Moore", pattern, textLength, numComparisons, (System.currentTimeMillis() - startTime), false));
        return -1;
    }

    /**
     * @return ArrayList - contains all of the data collected during the lifespan of the object
     */
    public ArrayList<MatchingOutput> getResults()
    {
        return results;
    }
}


/**
 * Purpose: Simple class to hold data collected during each pattern match
 */
class MatchingOutput
{
    public long timeTakenToMatchPattern; // Time taken during the search
    public double averageComparisonsPerCharacter;
    public int numberOfComparisons;
    String pattern; // What pattern was searched for
    String whichAlgorithm; // BM or KMP
    boolean wasSuccessful; // Pattern found in text or not

    MatchingOutput(String algorithm, String pattern_, int lengthText, int numbrOfComparisons, long timeTaken, boolean successFul)
    {
        whichAlgorithm = algorithm;
        pattern = pattern_;
        numberOfComparisons = numbrOfComparisons;
        timeTakenToMatchPattern = timeTaken;
        averageComparisonsPerCharacter = ((double) (numberOfComparisons)) / lengthText;
        wasSuccessful = successFul;
    }
}

