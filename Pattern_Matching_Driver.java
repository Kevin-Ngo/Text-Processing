import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;
import java.io.PrintWriter;
import java.io.File;

/**
 *  @author Kevin Ngo
 *  @date 05/06/18
 *  @file_description: This is a pattern matching driver, that simply tests the KMP and BM method for searching that was implemented in "Pattern_Matching.java".
 *                     In addition, this class, "Pattern_Matching.java" is coded specifically for "Project #3" at Mt. SAC, assigned by Prof. Tuan Vo. therefore,
 *                     contains methods used to collect various data pertaining to each pattern match.
 */
public class Pattern_Matching_Driver
{
    public static void main(String[] args)
    {
        System.out.println("Author: Kevin Ngo\nProfessor: Tuan Vo\nProject 3: Text Processing\n\n");

        String firstFileAddress = "usdeclarPC.txt";
        String secondFileAddress = "humanDNA.txt";

        String text1 = readText(firstFileAddress);
        String text2 = readText(secondFileAddress);
        int indexInText;
        String pattern;

        /*
            Testing to see if the text was read in correctly.
            (lowercase, white spaces, and as one single string)
         */
        //System.out.println(text1);
        //System.out.println(text2);

        /*
            Test the class using the first text.
         */
        Pattern_Matching matcher1 = new Pattern_Matching(text1);

        // KMP Testing
        System.out.println("----- KMP Testing with the first text file -----");

        // Case 1: look for the word "adopted"
        System.out.println("Case 1: Looking for the pattern \"adopted\" in the US Declaration");
        pattern = "adopted";
        indexInText = matcher1.findUsingKMP(pattern);
        possiblyPrintWord(text1, indexInText, pattern);

        // Case 2: look for the word "King T'Challa is the new president"
        System.out.println("Case 2: Looking for a pattern that is definitely... not in the US... Declaration... \"King T'Challa is the new president\"");
        pattern = "King T'Challa is the new president";
        indexInText = matcher1.findUsingKMP(pattern);
        possiblyPrintWord(text1, indexInText, pattern);

        // Case 3: look for the last word in the document "British brethren"
        System.out.println("Case 3: Looking for the pattern \"british brethren\"");
        pattern = "british brethren";
        indexInText = matcher1.findUsingKMP(pattern);
        possiblyPrintWord(text1, indexInText, pattern);

        // BMP Testing
        System.out.println("----- BM Testing with the first text file -----");

        // Case 1: look for the pattern "adopted"
        System.out.println("Case 1: Looking for the pattern \"adopted\" in the US Declaration");
        pattern = "adopted";
        indexInText = matcher1.findUsingBM(pattern);
        possiblyPrintWord(text1, indexInText, pattern);

        // Case 2: look for the pattern "King T'Challa is the new president"
        System.out.println("Case 2: Looking for a pattern that is definitely... not in the US... Declaration... \"King T'Challa is the new president\"");
        pattern = "King T'Challa is the new president";
        indexInText = matcher1.findUsingBM(pattern);
        possiblyPrintWord(text1, indexInText, pattern);

        // Case 3: look for a later pattern in the document "British brethren"
        System.out.println("Case 3: Looking for the pattern \"british brethren\"");
        pattern = "british brethren";
        indexInText = matcher1.findUsingBM(pattern);
        possiblyPrintWord(text1, indexInText, pattern);



        /*
            Test the class using the second file
         */
        Pattern_Matching matcher2 = new Pattern_Matching(text2);

        // KMP Testing
        System.out.println("\n\n----- KMP Testing with the second text file -----");

        // Case 1: look for the pattern "ACTGGA"
        System.out.println("Case 1: Looking for the pattern \"actgga\"");
        pattern = "actgga";
        indexInText = matcher2.findUsingKMP(pattern);
        possiblyPrintWord(text2, indexInText, pattern);

        // Case 2: look for the pattern "TAGTAC"
        System.out.println("Case 2: Looking for the pattern \"tagtac\"");
        pattern = "tagtac";
        indexInText = matcher2.findUsingKMP(pattern);
        possiblyPrintWord(text2, indexInText, pattern);

        // Case 3: look for the pattern "Black panther is the coolest superhero."
        System.out.println("Case 3: Looking for the pattern \"Black panther is the coolest superhero.\"");
        pattern = "Black panther is the coolest superhero.";
        indexInText = matcher2.findUsingKMP(pattern);
        possiblyPrintWord(text2, indexInText, pattern);

        // BM Testing
        System.out.println("----- BM Testing with the second text file -----");

        // Case 1: look for the pattern "ACTGGA"
        System.out.println("Case 1: Looking for the pattern \"actgga\"");
        pattern = "actgga";
        indexInText = matcher2.findUsingBM(pattern);
        possiblyPrintWord(text2, indexInText, pattern);

        // Case 2: look for the pattern "TAGTAC"
        System.out.println("Case 2: Looking for the pattern \"tagtac\"");
        pattern = "tagtac";
        indexInText = matcher2.findUsingBM(pattern);
        possiblyPrintWord(text2, indexInText, pattern);

        // Case 3: look for the pattern "Black panther is the coolest superhero."
        System.out.println("Case 3: Looking for the pattern \"Black panther is the coolest superhero.\"");
        pattern = "Black panther is the coolest superhero.";
        indexInText = matcher2.findUsingBM(pattern);
        possiblyPrintWord(text2, indexInText, pattern);


        // Write information for each case to a text file.
        System.out.println("\nSee the \"results.txt\" file for in depth information for each pattern matching case\n\n");
        writeOutput(matcher1, matcher2);
    }

    public static String readText(String fileAddress) // Reads the text file and inputs it all as one string, all lowercase and with white spaces included
    {
        Scanner input;
        String wholeText = "";
        try
        {
            input = new Scanner(new File(fileAddress));
            StringBuilder words = new StringBuilder(); // Mutable
            while (input.hasNext())
            {
                words.append(input.next().toLowerCase());
                if (input.hasNext()) // Accounts for the last input (do not want a white space after the last word)
                    words.append(" ");
            }
            wholeText = words.toString();
        }
        catch(FileNotFoundException e)
        {
            System.out.println("\"" + fileAddress + "\"" + " could not be found");
        }
        return wholeText;
    }

    public static void possiblyPrintWord(String text, int startIndex, String pattern)
    {
        if (startIndex == -1)
            System.out.println("The pattern was not found in the text.\n");
        else
        {
            int index = startIndex;
            int patternLengthInsurance = 0; // Make sure if pattern is multiple words to keep building the string
            StringBuilder word = new StringBuilder(); // Mutable
            while ( text.charAt(index) != ' '  || (patternLengthInsurance < pattern.length())) // Continutes until white space or until at least the pattern length
            {
                if (patternLengthInsurance > pattern.length()-1)
                    break;
                word.append(Character.toString(text.charAt(index)));
                index++;
                patternLengthInsurance++;
            }
            System.out.println("The pattern \"" + word.toString() + "\" was found in the text, starting at index " + startIndex + ".\n");
        }
    }

    public static void writeOutput(Pattern_Matching firstTextData, Pattern_Matching secondTextData)
    {
        try
        {
            PrintWriter write = new PrintWriter(new File("result.txt"));

            ArrayList<MatchingOutput> outputListFromText1, outputListFromText2;
            outputListFromText1 = firstTextData.getResults();
            outputListFromText2 = secondTextData.getResults();
            MatchingOutput temp;

            write.println("------------ Data from Text #1 ------------");
            for (int i = 0; i<outputListFromText1.size(); i++)
            {
                temp = outputListFromText1.get(i);
                write.println("Case: " + (i+1));
                write.println("Algorithm used: " + temp.whichAlgorithm);
                write.println("Pattern searched for: \"" + temp.pattern + "\"");
                write.println("Successful?: " + temp.wasSuccessful);
                write.println("Time spent searching: " + temp.timeTakenToMatchPattern + "ms");
                write.println("Total number of comparisons: " + temp.numberOfComparisons);
                write.println("Average number of comparisons per character: " + temp.averageComparisonsPerCharacter);
                write.println("\n");
            }

            write.println("\n------------ Data from Text #2 ------------");
            for (int j = 0; j<outputListFromText2.size(); j++)
            {
                temp = outputListFromText2.get(j);
                write.println("Case: " + (j+1));
                write.println("Algorithm used: " + temp.whichAlgorithm);
                write.println("Pattern searched for: \"" + temp.pattern + "\"");
                write.println("Successful?: " + temp.wasSuccessful);
                write.println("Time spent searching: " + temp.timeTakenToMatchPattern + "ms");
                write.println("Total number of comparisons: " + temp.numberOfComparisons);
                write.println("Average number of comparisons per character: " + temp.averageComparisonsPerCharacter);
                write.println("\n");
            }
            write.close();
        }
        catch(FileNotFoundException e)
        {
            System.out.println("Result file could not be created.");
        }
    }
}
