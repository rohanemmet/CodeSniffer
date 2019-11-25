package Analysis;

import Filetypes.TextReadable;
import java.io.IOException;
import java.util.Vector;

/* This class runs the battery of smell tests which can be performed on the
   raw Java text.
 */

public class JavaTestBattery implements TestRunnable {
    private Vector<TextReadable> javaFiles;
    private Vector<TextSmellable> indiviualSmellTests; // Tests carried out file by file
    private Vector<BatchSmellable<TextReadable>> batchSmellTests; // Tests carried out over all files
    private Vector<SmellData> javaSmellData;

    public JavaTestBattery(Vector<TextReadable> files) {
        this.javaFiles = files;
        this.indiviualSmellTests = new Vector<>();
        this.batchSmellTests = new Vector<>();
        this.javaSmellData = new Vector<>();

        batchSmellTests.add(BloatedCode.instantiate());
        indiviualSmellTests.add(Overchaining.instantiate());
        indiviualSmellTests.add(SwitchStatements.instantiate());
    }

    public void testSmells() throws IOException, ClassNotFoundException {
        for(BatchSmellable<TextReadable> t : batchSmellTests) {
            t.addFiles(javaFiles);
            t.testSmell();
            this.javaSmellData.addAll(t.getData());
            t.refresh();
        }

        for(TextReadable t : javaFiles) {
            for(TextSmellable ts : indiviualSmellTests) {
                ts.setFile(t);
                ts.testSmell();
                this.javaSmellData.addAll(ts.getData());
                ts.refresh();
            }
        }
    }

    public Vector<SmellData> getSmells() {
        return this.javaSmellData;
    }
}
