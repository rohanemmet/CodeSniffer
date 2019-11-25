package Filetypes;

import java.io.File;
import java.io.IOException;

// This is the interface to which all file and directory objects conform

public interface Openable {
    public void open() throws IOException; // Instantiate the file with the Openable's path
    public String getName();
}
