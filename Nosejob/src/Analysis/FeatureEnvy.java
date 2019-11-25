package Analysis;

import Filetypes.TextReadable;

//import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Vector;

/* This class tests for occurences of a class calling the methods of another class.
   This smell is tested on individual files
 */

public class FeatureEnvy implements TextSmellable {
    private static final String NAME = "FeatureEnvy";
    private static FeatureEnvy instanceCache;
    private TextReadable file;
    private Vector<SmellData> smellInstances;
    private boolean detectable;

    public static FeatureEnvy instantiate() {
        if(instanceCache == null) {
            instanceCache = new FeatureEnvy();
        }
        return instanceCache;
    }

    private FeatureEnvy() {
        this.smellInstances = new Vector<>();
        this.detectable = true;
    }

    @Override
    public void setFile(TextReadable t) throws IOException {
        t.open();
        File f = t.openFile();
        if(!f.exists()) {
            throw new FileNotFoundException("File " + t.getName() + " not found");
        }
        this.file = t;
    }

    @Override
    public boolean isDetectable() {
        return this.detectable;
    }

    @Override
    public void toggleDetectable() {
        this.detectable = !this.detectable;
    }

    @Override
    public void testSmell() throws IOException {
        //capture a class object calling a different class object's method
    }

    @Override
    public Vector<SmellData> getData() {
        return this.smellInstances;
    }

    @Override
    public void refresh() {
        this.smellInstances.clear();
    }

    @Override
    public void reset() {
        this.file = null;
        this.smellInstances = null;
        this.detectable = true;
    }

    private void instantiateSmell(int line) {
        SmellData data = new SmellData(NAME);
        data.setLines(new Vector<>(line, line));
        data.setReport("In file " + this.file.getName()
                + " \n\tLine " + line + "The class " + "<envious_class_name>" + " calls " + "other_class_name.method.");
        this.smellInstances.add(data);
    }
}
