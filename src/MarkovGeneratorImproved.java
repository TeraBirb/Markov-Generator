/***
 * Author: Isaiah C, May 2022
 * Project for CIS 36A Java Programming course
 "Improved" version takes into account two words that precede a following two words to output a more natural and "human"
 paragraph structure. Outputs to the console and to a txt file in the root directory. Outputs to the console  the entire
 hashmap used to store two-word pairs.
*/

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class MarkovGeneratorImproved {

    // User Settings
    public static final String INPUT_FILE_NAME = "emma.txt";    // has to be an existing txt file in root directory
    public static final String OUTPUT_FILE_NAME = "output.txt"; // does not have to exist. clears and updates every run
    public static final int NUMBER_OF_OUTPUT_WORDS = 50;
    // End User Settings


    // Keys are Lists, not ArrayLists to fix bug that replaces all existing keys to the current keys
    private HashMap<List<String>, ArrayList<String>> markovEntry = new HashMap<>();
    // ArrayList of LISTS (not ArrayLists) for the same reason
    private ArrayList<List<String>> keys = new ArrayList<>();

    public void readFile(String filename) {

        File inputFile = new File(filename);
        Scanner fileReader = null;

        try {
            fileReader = new Scanner(inputFile);
        } catch (FileNotFoundException e) {
            System.err.println("Error reading file! :(");
            e.printStackTrace();
        }

        if (fileReader != null) {

            String previous1 = ""; // first word of key
            String previous2 = ""; // second word of key
            ArrayList<String> previous = new ArrayList<>();

            // Initialize ArrayList
            previous.add(previous1);
            previous.add(previous2);

            String current = "";

            while(fileReader.hasNext()) {

                // needs to be at least three words
                // keep track of three strings, previous1, previous2, and current
                // key.add(previous1) key.add(previous2), value.add(current)

                // System.out.println("\nFilereader Running!");


                current = fileReader.next();

                // "Skips" first iteration 'current' gets updated,
                // hashMap remains empty after second iteration but 'previous' gets updated
                if (!previous1.equals("")) {

                    previous.set(0, previous1);
                    previous.set(1, previous2);

                    if (!markovEntry.containsKey(previous)) {       // If key DOES NOT exist yet
                        // List instead of array list to fix bug that replaces all existing keys to the current keys
                        markovEntry.put(List.of(previous1,previous2), new ArrayList<>());
                        markovEntry.get(previous).add(current);   // add to arrayList

                        // System.out.println("KEY DOESNT EXIST: " + previous);
                        keys.add(List.of(previous1,previous2));

                    } else {       // If key EXISTS
                        markovEntry.get(previous).add(current);   // add to arrayList
                        // System.out.println("KEY EXISTS: " + previous);
                    }
                }
                previous1 = previous2;
                previous2 = current;

                // System.out.println("markovEntry: " + markovEntry);
                // System.out.println("keys: " + keys);
            }
        }
        else {
            System.err.println("Trying to read from null! :(");
        }

        fileReader.close();
    }

    public String generateText(int numberOfWords) throws IOException {
        // create returnString
        // for loop i < numberOfWords
        // Math.random() * keys.length() to select predecessor word
        // inner rng Math.random() * hashmap.get(predecessor).size() to select successor word from hashmap.get(predecessor)
        // add space after each word
        // add period at the very last word, i == numberofwords - 1;

        PrintWriter outputFile = null;

        try {
            outputFile = new PrintWriter(new FileOutputStream(OUTPUT_FILE_NAME));
        }
        catch (FileNotFoundException e) {
            System.err.println("Error opening output file! :(");
            e.printStackTrace();
        }

        String returnString = "";
        String predecessor1 = "";
        String predecessor2 = "";
        String successor = "";

        int predecessorRng;
        final int LINE_SIZE = 4;

        for (int i = 0; i < numberOfWords; i++) {

            predecessorRng = (int)(Math.random() * keys.size());
            // Getting both words of the same key
            predecessor1 = keys.get(predecessorRng).get(0);
            predecessor2 = keys.get(predecessorRng).get(1);

            int successorSize = markovEntry.get((List.of(predecessor1, predecessor2))).size();

            // use List as key
            successor = markovEntry.get((List.of(predecessor1, predecessor2))).get((int)(Math.random() * successorSize));

            // Capitalize first letter of first word
            if (i == 0) {
                predecessor1 = predecessor1.substring(0,1).toUpperCase() + predecessor1.substring(1);
            }

            returnString = returnString + predecessor1 + " " + predecessor2 + " " + successor;

            // Handle punctuations and line breaks, janky but it works :)
            if (i < numberOfWords - 1) {
                returnString += " ";
            }
            else {
                returnString += ".";
            }
            if (i % LINE_SIZE == LINE_SIZE - 1) {
                returnString += "\n";
            }
        }

        Files.write(Paths.get("output.txt"), returnString.getBytes(), StandardOpenOption.APPEND);
        outputFile.close();

        return returnString;
    }

    public void printMap() {
        System.out.println(markovEntry);
        System.out.println(keys);
        System.out.println("\n");
    }

    public static void main(String[] args) throws IOException {

        MarkovGeneratorImproved test = new MarkovGeneratorImproved();
        test.readFile(INPUT_FILE_NAME);
        test.printMap();

        System.out.println(test.generateText(NUMBER_OF_OUTPUT_WORDS));

    }
}

//      STORING/INPUT ✓
// 1✓ Hashmap of string key to arrayList of strings value
// 2✓ read a txt file, store each unique word in hashMap as well as separate arrayList to generate random from index
// 3✓ during reading a word, if hashmap containsKey == true, add() the successor word to the arrayList value
// 4✓ else put(), create arrayList value

//      PRINTING/OUTPUT ✓
// 5✓ random select from unique word array list from step 2
// 6✓ hashmap.get(keyFromPreviousStep), print random word from arraylist value
// 7✓ repeat for as many words as specified in the parameter
// 8✓ sout and also output.txt

//      IMPROVED VERSION
// 9✓ Hashmap of arrayList of TWO STRINGS, to arrayList of strings value
// 10✓ Repeat storing/input and printing/output
// 11✓ Refactor, refactor

// EASY PEASY LEMON SQUEEZY GO YOU SEGGSY GALAXY BRAIN PROGRAMMER