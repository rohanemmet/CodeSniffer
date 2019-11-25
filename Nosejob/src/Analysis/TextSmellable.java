package Analysis;

import Filetypes.TextReadable;

import java.io.FileNotFoundException;
import java.io.IOException;

// Interface for smell tests that will be carried out on Java files.
// Since Java and Class files must be processed in slightly different ways.
// For example, having setFile with 2 signatures for textReadable and Reflectable
// will enforce the creation of unusable methods in different smell test classes.

public interface TextSmellable extends Smellable {
    public void setFile(TextReadable t) throws IOException;
}