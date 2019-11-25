package Filetypes;

import java.io.File;
import java.io.IOException;

public interface Reflectable extends Openable {
    public File openFile();
    public Class getJavaClass() throws IOException, ClassNotFoundException;
}
