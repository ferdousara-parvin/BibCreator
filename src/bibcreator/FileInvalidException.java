/**
 * Assignment 3: Practicing Exception Handling and File I/O
 * Written by: Viveka Anban(40063308) and Ferdousara Parvin(40062738)
 */
package bibcreator;

public class FileInvalidException extends Exception {

    public FileInvalidException() {
        super("Error: Input file cannot be parsed due to missing information\n");
    }

    public FileInvalidException(String message) {
        super(message);
    }

    public String toString() {
        return getMessage();
    }

    public String getMessage() {
        return super.getMessage(); // RIGHT???
    }

}
