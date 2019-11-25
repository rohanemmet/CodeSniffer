package Analysis;

// Interface for smell tests that will be carried out on Class files.
// Since Java and Class files must be processed in slightly different ways.
// For example, having setFile with 2 signatures for textReadable and Reflectable
// will enforce the creation of unusable methods in different smell test classes.

import Filetypes.Reflectable;

import java.io.FileNotFoundException;

public interface ReflectSmellable extends Smellable{
    public void setFile(Reflectable t) throws FileNotFoundException;
}
