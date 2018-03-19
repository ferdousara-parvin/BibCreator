// Assignment 3: Practicing Exception Handling and File I/O Written by: Viveka
// Written by: Viveka Anban(40063308) and Ferdousara Parvin(40062738) 
package bibcreator;

// Imports
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 * PURPOSE NOT DONEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE!!!!!!!
 * <h1>BibCreator class is the driver class</h1>
 * The purpose of this program is to review some concepts that we learnt
 * previously concerning Exception Handling and File I/O. For this assignment,
 * we had to create a BibCreator class which possessed 4 attributes (price,
 * author, ISB, and title). We then implemented this class in the driver class
 * called Assignment1 (which simulated a book store) to keep track of the books.
 * We were able to add new Books to the "store", change information of a book,
 * display all books by a specific author and display all books under a certain
 * price <br>
 * COMP249<br>
 * Assignment 3 <br>
 * Due date: Monday, March 19th, 2018</br>
 *
 * @author Viveka Anban(40063308) and Ferdousara Parvin(40062738) COMP249
 */
public class BibCreator {

    // Declare the scanner and printwriter arrays that will be used to hold scanner and printwriter objects respectively
    static Scanner[] inputs;
    static PrintWriter[][] outputs;

    // Create some constants that will be used throughout the whole program
    static final int ZERO = 0;
    static final int ONE = 1;
    static final int TWO = 2;
    static final int THREE = 3;
    static final int TEN = 10;
    static final String IEEE = "IEEE";
    static final String ACM = "ACM";
    static final String NJ = "NJ";

    /**
     * This is the main method where all the scanners and printwriters are being
     * created, and where all the different methods are invoked.
     *
     * @param args
     */
    public static void main(String[] args) {

        // Print a welcome message
        System.out.println(
                "--------------------------------------\n"
                + " Welcome to Viveka and Ferdousara's program\n"
                + "--------------------------------------------");

        // Create some variables
        final String FILENAME = "Latex"; // We assume that every file that we will open starts with the word "Latex"
        String jsonFileName = ""; // This variable stores the name of the current json file we are working with
        int indexInputs = ZERO; // This index will be used to find which input file we are currently trying to read.
        int indexOutputs = ZERO; // This index will be used to find which output file we are currently trying to open (this variable can only store numbers 0, 1 or 2).
        int counterInvalidFiles = ZERO; // A counter to keep track of the number of invalid files (invalid files are the ones that contain at least one empty field)

        // Create an arrays of scanners to store each input stream in one single array
        inputs = new Scanner[TEN];

        // Create a 2D array, that will contain an array of 3 PrintWriters for each input files read
        // outputs[i][0] represents IEEE, outputs[i][1] represents ACM and outputs[i][2] represents NJ. 
        // i corresponds to the corresponding Bib File
        outputs = new PrintWriter[TEN][THREE];

        // Attempt opening each Bib file to read using a try catch block
        try {

            // Create a new Scanner to read from each Bib file  
            for (indexInputs = ZERO; indexInputs < inputs.length; indexInputs++) {
                inputs[indexInputs] = new Scanner(new FileInputStream("Bib_Files/" + FILENAME + (indexInputs + ONE) + ".bib"));

            }

        } catch (FileNotFoundException e) {

            // Warning message
            System.out.println("Could not open input file " + FILENAME + (indexInputs + ONE) + ".bib" + " for reading. "
                    + "Please check if file exists! Program will terminate after closing any opened files.");

            // Clsoe all scanners that were opened prior to the one that was not able to be created because the requested inptu file was not found
            if (indexInputs != ZERO) { // If sttaement to make sure that at least one scanner was created because or else, there would be nothing to close
                for (int k = indexInputs - ONE; k >= ZERO; k--) {
                    inputs[k].close();
                }
            }

            // Exit the progrsm
            System.exit(ZERO);

        }

        // Attempt creating the 3 json files where we will output bibliographies using a try catch block. 
        // Note that all ten Scanners have been created at this point and are opened
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

            // Invoke method processFilesForValidation
            for (int i = ZERO; i < inputs.length; i++) {
                boolean incrementCounterInvalidFiles = processFilesForValidation(inputs[i], outputs[i], (i + 1));

                if (incrementCounterInvalidFiles) // Bib input file was invalid
                {
                    counterInvalidFiles++; // Update the counter for the number of invalid files
                }
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

            // Delete all ouput files already created in directory Json_Files
            deleteOutputFiles("Json_Files"); // delete all json files created in that directory

            // Close all scanners
            for (int k = ZERO; k < inputs.length; k++) {
                inputs[k].close();
            }

            System.exit(ZERO);

        } finally { // End of reading from Bib input files and outputting bibliographies into JSON output files

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

            // Print a message telling the user how many files were invalid
            System.out.println("\nA total of " + counterInvalidFiles + " files were invalid, and could not be processed. All other " + (TEN - counterInvalidFiles) + " \"Valid\" files have been created.");

            // Print closing message
            System.out.println(
                    "\n--------------------------------------------\n"
                    + " Thank you for using our Bib Creator. Goodbye!\n"
                    + "--------------------------------------------\n");
        }

    }

    /**
     * This methods deletes output files that are linked to invalid input files
     *
     * @param pathDirectory Directory where there are files to be deleted
     */
    public static void deleteOutputFiles(String pathDirectory) {
        File file = new File(pathDirectory);

        if (file.isDirectory()) // if the path directory exists, proceed to delete
        {
            String[] files = file.list(); // Retrieve all the file names in the directory

            if (files.length > ZERO) { // if statement to verify that there are files in directory Json_Files
                for (String fileName : files) { // For loop runs through files array 
                    File deleteFile = new File(pathDirectory + "/" + fileName); // Create a file object for each output file to be deleted
                    deleteFile.delete(); // delete file
                }
            }
        }
    }

    // Process one Bib input file at a time
    /**
     * This method processes one Bib input file at a time
     *
     * @param input Scanner object for Bib input file
     * @param output An array of three printwriters for each Json output file
     * @param fileNumber An int that represents the Bib input file's
     * corresponding number
     * @return boolean - Returns true if the Bib input file is invalid (contains
     * an empty field) or false if it is valid
     */
    public static boolean processFilesForValidation(Scanner input, PrintWriter[] output, int fileNumber) {

        boolean isNotValid = false; // Declare and initialize a boolean variable to check if the Bib input file is valid or not

        // Set }, as the delimiter so that every next() method reads until the following },
        input.useDelimiter("\\}\\,");

        try {

            // This variable is used to number the articles in ACM bibliographies
            int counterNbFiles = ZERO;

            while (input.hasNext()) { // while there is another token

                String s1 = input.next().trim(); // s1 contains information between two }, delimiters. Trim is used to eliminate leading and trailing spaces

                // If s1 is noly ocmposed of "}", then it means that we are at the end of the file, as the last element ends with },.
                if (s1.trim().equals("}")) {
                    break; // Get otu of the whiel loop
                }

                // Create a 2d array called bibliography to store element keys and element values. There are 11 of them.
                String[][] bibliography = new String[11][TWO];

                // For loop to run through bibliography array
                for (int i = ZERO; i < bibliography.length; i++) {

                    // 1. Determine the element key 
                    String elementKey;

                    if (s1.contains("@ARTICLE{")) { // Special case: beginning of an article, retrieve first element key
                        counterNbFiles++;
//DEBUGGER:                        
                        System.out.println("\n" + fileNumber + " ARTICLE #" + counterNbFiles);

                        elementKey = s1.substring(s1.lastIndexOf(",") + ONE, s1.indexOf("=")).trim(); // at beginning of an article, first element key is enclosed between , and =

                    } else { // for every following field/element key
                        elementKey = s1.substring(ZERO, s1.indexOf("=")).trim(); // every element key runs from index 0 to the index before the = character
                    }

//DEBUGGER:                   
                    System.out.println("S1: " + s1);
                    bibliography[i][ZERO] = elementKey.trim();
//DEBUGGER:                    
                    System.out.println("KEY:" + elementKey);

                    // 2. Determine the element value.
                    String elementValue = s1.substring(s1.lastIndexOf("{") + 1, s1.length()).trim();
                    bibliography[i][ONE] = elementValue.trim();

                    // Invalid input file (empty field)
                    if (elementValue.trim().isEmpty()) {
                        isNotValid = true;
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
            System.out.println(e); // Print error/warning message

            File invalidFile1 = new File("Json_Files/" + IEEE + fileNumber + ".json");
            File invalidFile2 = new File("Json_Files/" + ACM + fileNumber + ".json");
            File invalidFile3 = new File("Json_Files/" + NJ + fileNumber + ".json");

            // Delete Json output files that correspond to the invalid Bib input file
            invalidFile1.delete();
            invalidFile2.delete();
            invalidFile3.delete();

        }

        // Return statement
        return isNotValid;
    }

    /**
     *
     * @param bibliography
     * @param counterNbFiles
     * @return
     */
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

    /**
     *
     * @param bibliography
     * @param counterNbFiles
     * @return
     */
    public static String[] makeBibliography2(String[][] bibliography, int counterNbFiles) {

        //This will contain all the elements that IEEE, ACM and NJ bibliographies would need in their order.
        String[][] sortedBibliography = new String[9][3];

        for (int i = ZERO; i < sortedBibliography.length; i++) {
            for (int j = ZERO; j < sortedBibliography[i].length; j++) {
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
