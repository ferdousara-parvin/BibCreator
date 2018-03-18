/**
 * Assignment 3: Practicing Exception Handling and File I/O
 * Written by: Viveka Anban(40063308) and Ferdousara Parvin(40062738)
 * Purpose:
 *
 */
//DEBUGGER:
////                System.out.println("Opening: " + "Bib_Files/" + FILENAME + (index + 1) + ".bib");
//            
//DEBUGGER:
//                System.out.println("\nOpening: " + "Json_Files/" + IEEE + (index + 1) + ".json");
//                System.out.println("Opening: " + "Json_Files/" + ACM + (index + 1) + ".json");
//                System.out.println("Opening: " + "Json_Files/" + NJ + (index + 1) + ".json");
package bibcreator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.regex.Pattern;

public class BibCreator {

    static Scanner[] inputs;
    static PrintWriter[][] outputs;

    //create number constants (it is created outside of main because it will be used in other methods)
    static final int ZERO = 0;
    static final int ONE = 1;
    static final int TWO = 2;
    static final int THREE = 3;
    static final int TEN = 10;
    static final String IEEE = "IEEE";
    static final String ACM = "ACM";
    static final String NJ = "NJ";

    public static void main(String[] args) {
        // Print a welcome message
        System.out.println(
                "--------------------------------------\n"
                + " Welcome to Viveka and Ferdousara's program\n"
                + "--------------------------------------------");

        final String FILENAME = "Latex"; // every file that we will open starts with "Latex"
        String jsonFileName = "";
        int indexInputs = ZERO; // This index will be used to find which file we are currently opening.
        int indexOutputs = ZERO; // This index will be used to find which file we are currently opening.
        int counterInvalidFiles = ZERO;

        // Create an arrays of scanners to create input streams
        inputs = new Scanner[TEN];

        // Create a 2D array, that will contain an array of 3 PrintWriters for each files read
        /*
         outputs[i][0] represents IEEE, outputs[i][1] represents ACM and outputs[i][2] represents NJ. 
         i represents the corresponding Bib_File
         */
        outputs = new PrintWriter[TEN][THREE];

        int index = ZERO; //This index will be used to find which file we are currently opening.

        // Attempt opening each Bib file to read
        try {

            //Create a new Scanner to read from each Bib file  
            for (indexInputs = ZERO; indexInputs < inputs.length; indexInputs++) {
                inputs[indexInputs] = new Scanner(new FileInputStream("Bib_Files/" + FILENAME + (indexInputs + ONE) + ".bib"));

            }

//TODO FERDOU: Call every file with their corresponding output file (will need to change method parameters first)
        } catch (FileNotFoundException e) {

            // Warning message
            System.out.println("Could not open input file " + FILENAME + (indexInputs + ONE) + ".bib" + " for reading. "
                    + "Please check if file exists! Program will terminate after closing any opened files.");

            /*
             Close all scanners that got to be opened: 
             if Latex1.bib was not found, no scanners would have been created. 
             if any other Latexi was not found, close all scanners starting 
             from the scanner created for the previous file.
             */
            if (indexInputs != ZERO) {
                for (int k = indexInputs - ONE; k >= ZERO; k--) {
                    inputs[k].close();
                }
            }

            System.exit(ZERO);

        }

        // Attempt creating the 3 json files where we will output bibliographies. Note that all ten Scanners have been created at this point and are opened
        try {

            // Create 3 new printWriters for each Bib file
            for (indexInputs = ZERO; indexInputs < inputs.length; indexInputs++) {

                jsonFileName = "Json_Files/" + IEEE + (indexInputs + ONE) + ".json";
                outputs[indexInputs][indexOutputs] = new PrintWriter(new FileOutputStream(jsonFileName));
                indexOutputs++;

                jsonFileName = "Json_Files/" + ACM + (indexInputs + ONE) + ".json";
                outputs[indexInputs][indexOutputs] = new PrintWriter(new FileOutputStream(jsonFileName));
                indexOutputs++;

                jsonFileName = "Json_Files/" + NJ + (indexInputs + ONE) + ".json";
                outputs[indexInputs][indexOutputs] = new PrintWriter(new FileOutputStream(jsonFileName));

                indexOutputs = ZERO;
            }

            for (int i = ZERO; i < inputs.length; i++) {
                counterInvalidFiles += processFilesForValidation(inputs[i], outputs[i], (i + 1));
            }

        } catch (FileNotFoundException e) {
            System.out.println("Could not create the following JSON file :" + jsonFileName + " Please check for problems such as directory permission or no available memory.");
            System.out.print("Program will terminate after deleting already created output files and all opened files.");

            // Close all print writers
            for (int i = indexInputs; i >= ZERO; i--) {
                while (indexOutputs >= 0) {
                    outputs[indexInputs][indexOutputs].close();
                    indexOutputs--;
                }

            }

            // Delete all ouput files already created i ndirectory Json_Files
            deleteOutputFiles("Json_Files"); // delete all json files created in that directory

            // Close all scanners
            for (int k = ZERO; k < inputs.length; k++) {
                inputs[k].close();
            }

            System.exit(ZERO);

        } finally {

            // Close all scanners
            for (int k = ZERO; k < inputs.length; k++) {
                inputs[k].close();
            }

            // Close all print writers
            for (int file = ZERO; file < outputs.length; file++) {
                for (int type = ZERO; type < outputs[ZERO].length; type++) {
                    outputs[file][type].close();
                }
            }

            System.out.println("\nA total of " + counterInvalidFiles + " files were invalid, and could not be processed. All other " + (TEN - counterInvalidFiles) + " \"Valid\" files have been created.");

            // Print closing message
            System.out.println(
                    "\n--------------------------------------------\n"
                    + " Thank you for using our Bib Creator. Goodbye!\n"
                    + "--------------------------------------------\n");
        }

    }

    public static void deleteOutputFiles(String pathDirectory) {
        File file = new File(pathDirectory);

        if (file.isDirectory()) // if the path directory exists, proceed 
        {
            String[] files = file.list(); // Retrieve all the file names in the directory

            if (files.length > ZERO) {
                for (String fileName : files) {
                    File deleteFile = new File(pathDirectory + "/" + fileName);
                    boolean deleted = deleteFile.delete();

                    if (deleted) {
                        System.out.println(deleteFile.getName() + " is deleted."); // To debug
                    }
                }
            }
        }

    }

    // Process one file at a time
    public static int processFilesForValidation(Scanner input, PrintWriter[] output, int fileNumber) {

        // Set a }, like the delimiter so that every next() method reads until the following
        input.useDelimiter("\\}\\,");

        int counterInvalidFiles = ZERO;

        try {

            //This variable is used to number the articles in ACM bibliographies
            int counterNbFiles = ZERO;

            while (input.hasNext()) {

                String s1 = input.next().trim(); // trim is used to eliminate leading and trailing spaces

                //If we encounter only a }, then it means that we are at the end of the file. as the last element ends with },.
                /*
                    month={February  }, // when the input.next() is called, only } will be read at the end of the file
                }      
                
                At the end of the file #1, the program shouldn't even try reading "values" that could follow it.
                 */
                if (s1.trim().equals("}")) {
                    break;
                }

                String[][] bibliography = new String[11][2];

                for (int i = ZERO; i < bibliography.length; i++) {

                    String elementKey;

                    //Determine th element key
                    if (s1.contains("@ARTICLE{")) {
                        counterNbFiles++;
//DEBUGGER:                        
                        System.out.println("\n" + fileNumber + " ARTICLE #" + counterNbFiles);
                        elementKey = s1.substring(s1.lastIndexOf(",") + ONE, s1.indexOf("=")).trim();

                        /*
                         At the beginning of an article, the first element is enclosed between , and =.                         
                         */
                    } else {
                        elementKey = s1.substring(ZERO, s1.indexOf("=")).trim();
                        /*
                        Every following elements are enclosed before the = sign.
                         */
                    }

//DEBUGGER:                   
                    System.out.println("S1: " + s1);
                    bibliography[i][ZERO] = elementKey.trim();
//DEBUGGER:                    
                    System.out.println("KEY:" + elementKey);

                    //Determine the element value.
                    String elementValue = s1.substring(s1.lastIndexOf("{") + 1, s1.length()).trim();
                    bibliography[i][ONE] = elementValue.trim();

                    // Invalid input file (empty field)
                    if (elementValue.trim().isEmpty()) {
                        counterInvalidFiles++;
                        throw new FileInvalidException(
                                "Error: Detected Empty Field!"
                                + "\n========================="
                                + "\nProblem detected with input file: Latex" + fileNumber + ".bib\n"
                                + "File is invalid: Field \"" + elementKey + "\" is empty. Processing stopped at this point. Other empty fields may be present as well!\n");

                    }
//DEBUGGER:
                    System.out.println("VALUE:" + elementValue);

                    if (i < bibliography.length - ONE) {
                        s1 = input.next().trim();
                    }

                }

                for (int i = ZERO; i < output.length; i++) {
                    output[i].append(makeBibliography2(bibliography, counterNbFiles)[i]);
                }

            }

        } catch (FileInvalidException e) {
            System.out.println(e);

            // Delete the three output files that correspond to the invalid input file
            File invalidFile1 = new File("Json_Files/" + IEEE + fileNumber + ".json");
            File invalidFile2 = new File("Json_Files/" + ACM + fileNumber + ".json");
            File invalidFile3 = new File("Json_Files/" + NJ + fileNumber + ".json");

            invalidFile1.delete();
            invalidFile2.delete();
            invalidFile3.delete();

        }

        return counterInvalidFiles;
    }

    public static String[] makeBibliography(String[][] bibliography, int counterNbFiles) {


        /*
         THE IEEE_OUPUT WOULD GO IN THE OUTPUTS[i][0] FILE
         THE ACM WOULD GO IN THE OUTPUTS[i][1] FILE
         THE NJ WOULD GO IN THE OUTPUTS[i][2] FILE
         */
        String ieee_output = "";
        String acm_output = "[" + counterNbFiles + "]\t";
        String nj_output = "";

        //Add authors in bibliography
        for (int i = ZERO; i < bibliography.length; i++) {
            if (bibliography[i][ZERO].equals("author")) {

                //Create an array containg only the author names (names used to be seperated with "and")
                String[] authors = bibliography[i][ONE].split("and");

                for (int a = ZERO; a < authors.length; a++) {

                    authors[a] = authors[a].trim();

                    //IEEE and NJ OUTPUT
                    if (a < authors.length - ONE) {
                        ieee_output += authors[a] + ", ";
                        nj_output += authors[a] + " & ";
                    } else {
                        ieee_output += authors[a] + ". ";
                        nj_output += authors[a] + ". ";
                    }

                    //ACM OUTPUT
                    if (authors.length == ONE) {
                        acm_output += authors[a] + ". ";
                    } else if (a == ZERO) {
                        acm_output += authors[a] + " et al. ";
                    }

                }

                break;
            }
        }
        
        //Add the year only to ACM to the bibliography
        for (int i = 0; i < bibliography.length; i++) {
            if (bibliography[i][ZERO].equals("year")) {
                acm_output += " " + bibliography[i][ONE] + ". ";
                break;
            }
        }

        //Add title to the bibliography
        for (int i = 0; i < bibliography.length; i++) {
            if (bibliography[i][ZERO].equals("title")) {
                ieee_output += "\"" + bibliography[i][ONE] + "\", ";
                acm_output += bibliography[i][ONE] + ". ";
                nj_output += bibliography[i][ONE] + ". ";
                break;
            }
        }

        //Add the journal to the bibliography
        for (int i = 0; i < bibliography.length; i++) {
            if (bibliography[i][ZERO].equals("journal")) {
                ieee_output += bibliography[i][ONE] + ", ";
                acm_output += bibliography[i][ONE] + ". ";
                nj_output += bibliography[i][ONE] + ". ";
                break;
            }
        }

        //Add volume to the bibliography
        for (int i = 0; i < bibliography.length; i++) {
            if (bibliography[i][ZERO].equals("volume")) {
                ieee_output += "vol. " + bibliography[i][ONE] + ", ";
                acm_output += bibliography[i][ONE] + ", ";
                nj_output += bibliography[i][ONE] + ", ";
                break;
            }
        }

        //Add number to the bibliography
        for (int i = 0; i < bibliography.length; i++) {
            if (bibliography[i][ZERO].equals("number")) {
                ieee_output += "no. " + bibliography[i][ONE] + ", ";
                acm_output += bibliography[i][ONE] + " ";
                break;
            }
        }

        //Add the year only to ACM to the bibliography
        for (int i = 0; i < bibliography.length; i++) {
            if (bibliography[i][ZERO].equals("year")) {
                acm_output += "(" + bibliography[i][ONE] + "), ";
                break;
            }
        }

        //Add the number of pages to the bibliography
        for (int i = 0; i < bibliography.length; i++) {
            if (bibliography[i][ZERO].equals("pages")) {
                ieee_output += "p. " + bibliography[i][ONE] + ", ";
                acm_output += bibliography[i][ONE] + ". ";
                nj_output += bibliography[i][ONE];
            }
        }

        //Add the year to NJ to the bibliography
        for (int i = 0; i < bibliography.length; i++) {
            if (bibliography[i][ZERO].equals("year")) {
                nj_output += "(" + bibliography[i][ONE] + ").\n\n";
                break;
            }
        }

        //Add the month and year only to the ieee bibliography
        for (int i = 0; i < bibliography.length; i++) {
            if (bibliography[i][ZERO].equals("month")) {
                ieee_output += bibliography[i][ONE] + " ";
                break;
            }
        }
        for (int i = 0; i < bibliography.length; i++) {
            if (bibliography[i][ZERO].equals("year")) {
                ieee_output += bibliography[i][ONE] + ".\n\n";
                break;
            }
        }

        for (int i = 0; i < bibliography.length; i++) {
            if (bibliography[i][ZERO].equals("doi")) {
                acm_output += "DOI:https://doi.org/" + bibliography[i][ONE] + ".\n\n";
                break;
            }
        }

        String final_output[] = {ieee_output, acm_output, nj_output};
        return final_output;
    }

    public static String[] makeBibliography2(String[][] bibliography, int counterNbFiles) {
        
        //This will contain all the elements that IEEE, ACM and NJ bibliographies would need in their order.
        String[][] sortedBibliography = new String[9][3];

        for (int i = ZERO; i < sortedBibliography.length; i++) {
            for(int j = ZERO; j < sortedBibliography[i].length; j++){
                sortedBibliography[i][j] = "";
            }
        }

        for (int i = ZERO; i < bibliography.length; i++) {
            String element = bibliography[i][ZERO].toLowerCase();

            switch (element) {
                case "author":
                    String[] authors = bibliography[i][ONE].split("and");
                    sortedBibliography[ZERO][ONE] = "[" + counterNbFiles + "]\t";

                    for (int a = ZERO; a < authors.length; a++) {

                        authors[a] = authors[a].trim();

                        //IEEE and NJ OUTPUT
                        if (a < authors.length - ONE) {
                            sortedBibliography[ZERO][ZERO] += authors[a] + ", ";
                            sortedBibliography[ZERO][TWO] += authors[a] + " & ";
                        } else {
                            sortedBibliography[ZERO][ZERO] += authors[a] + ". ";
                            sortedBibliography[ZERO][TWO] += authors[a] + ". ";
                        }

                        //ACM OUTPUT
                        if (authors.length == ZERO) {
                            sortedBibliography[ZERO][ONE] += authors[a] + ". ";
                        } else if (a == ZERO) {
                            sortedBibliography[ZERO][ONE] += authors[a] + " et al. ";
                        }
                    }

                    break;

                case "title":
                    sortedBibliography[ONE][ZERO] = "\"" + bibliography[i][ONE] + "\", ";
                    sortedBibliography[TWO][ONE] = bibliography[i][ONE] + ". ";
                    sortedBibliography[ONE][TWO] = bibliography[i][ONE] + ". ";
                    break;
                case "journal":
                    sortedBibliography[TWO][ZERO] = bibliography[i][ONE] + ", ";
                    sortedBibliography[THREE][ONE] = bibliography[i][ONE] + ". ";
                    sortedBibliography[TWO][TWO] = bibliography[i][ONE] + ". ";
                    break;
                case "volume":
                    sortedBibliography[THREE][ZERO] = "vol. " + bibliography[i][ONE] + ", ";
                    sortedBibliography[4][ONE] = bibliography[i][ONE] + ", ";
                    sortedBibliography[THREE][TWO] = bibliography[i][ONE] + ", ";
                    break;
                case "number":
                    sortedBibliography[4][ZERO] = "no. " + bibliography[i][ONE] + ", ";
                    sortedBibliography[5][ONE] = bibliography[i][ONE] + " ";
                    sortedBibliography[4][TWO] = "";
                    break;
                case "year":
                    sortedBibliography[7][ZERO] = bibliography[i][ONE] + ".\n\n";
                    sortedBibliography[ONE][ONE] = bibliography[i][ONE] + ". ";
                    sortedBibliography[6][ONE] = "(" + bibliography[i][ONE] + "), ";
                    sortedBibliography[6][TWO] = "(" + bibliography[i][ONE] + ").\n\n";
                    break;
                case "pages":
                    sortedBibliography[5][ZERO] = "p. " + bibliography[i][ONE] + ", ";
                    sortedBibliography[7][ONE] = bibliography[i][ONE] + ". ";
                    sortedBibliography[5][TWO] = bibliography[i][ONE];
                    break;
                case "doi":
                    sortedBibliography[8][ONE] = "DOI:https://doi.org/" + bibliography[i][ONE] + ".\n\n";
                    break;
                case "month":
                    sortedBibliography[6][ZERO] = bibliography[i][ONE] + " ";
                    break;
                default:
                    break;

            }

        }

        String ieee_output = "", acm_output = "", nj_output = "";
        for (int index = ZERO; index < sortedBibliography.length; index++) {
            ieee_output += sortedBibliography[index][ZERO];
            acm_output += sortedBibliography[index][ONE];
            nj_output += sortedBibliography[index][TWO];
        }

        String final_output[] = {ieee_output, acm_output, nj_output};
        return final_output;
    }
}


/*
 README

 array bibliography will contain all the values needed to construct the bibliography
 KEY         VALUE
 author 
 journal
 title
 year
 volume
 number
 pages
 keywords
 doi
 issn
        
 bibliography[i][0] -> for all keys
 bibliography[i][1] -> for all values
         


        
 ex:  s1 = volume={PP},

 0   1   2   3   4   5   6   7   8   9   10
 v   o   l   u   m   e   =   {   P   P   }   


        startIndex -> 6
        elementKey ->  s1.substring(0, startIndex) = s1.substring(0, 6) -> volume
        elementvalue -> s1.substring(startIndex + 2, s1.length() - 1) 
        -> s1.substring(8, 11 - 1) 
        -> s1.substring(8, 10) = PP
         


//BRAINSTORM

 startIndex -> 6
 elementKey ->  s1.substring(0, startIndex) = s1.substring(0, 6) -> volume
 elementvalue -> s1.substring(startIndex + 2, s1.length() - 1) 
 -> s1.substring(8, 11 - 1) 
 -> s1.substring(8, 10) = PP
            


 */
