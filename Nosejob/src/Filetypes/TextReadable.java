package Filetypes;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;

public interface TextReadable extends Openable {
    public BufferedReader readFile() throws FileNotFoundException;
    public File openFile();
}

