package Filetypes;

/* Exception thrown by FileList object if a path to file that is neither
   a directory, a java file, or a class file is input */

public class IncorrectFileTypeException extends Exception {
    public IncorrectFileTypeException(String s) {
        super(s);
    }
}
