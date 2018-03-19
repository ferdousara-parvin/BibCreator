package bibcreator;

/**
 * This class is an exception class that is invoked when there are invalid files
 * @author Viveks Anban and Ferdousara Parvin
 */
public class FileInvalidException extends Exception {


    /**
     * Constructs a new FileInvalidException object with a default.
     * @param none
     */
    public FileInvalidException() {
        super("Error: Input file cannot be parsed due to missing information\n");
    }

    /**
     * Constructs a new FileInvalidException object and takes a customized message.
     * @param message Error message
     */
    public FileInvalidException(String message) {
        super(message);
    }

     /**
     * Returns a String representing the warning message
     * @Override toString in class Object
     * @return String - returns warning message
     */
    public String toString() {
        return getMessage();
    }

     /**
     * Gets the current value of the message attribute from parent class
     * <p>
     * The message is the warning/error message that tells the user why FileInvalidException has been thrown
     *
     * @return String - Warning message
     */
    public String getMessage() {
        return super.getMessage();
    }

}
