package Analysis;

import Filetypes.TextReadable;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Vector;

/* This class tests for occurences of highly chained method calls, e.g
   obj.f1().f2().f3()...
   which is indicative of excessive coupling as well as obscuring the dataflow
   of the application.

   This smell is tested on individual files. Chained calls are assumed to be on
   the same line in order to allow analysis by regex
 */

public class SwitchStatements implements TextSmellable {
    private static final String NAME = "SwitchStatements";
    private static SwitchStatements instanceCache;
    private TextReadable file;
    private Vector<SmellData> smellInstances;
    private boolean detectable;

    public static SwitchStatements instantiate() {
        if(instanceCache == null) {
            instanceCache = new SwitchStatements();
        }
        return instanceCache;
    }

    private SwitchStatements() {
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
        BufferedReader br = this.file.readFile();
        String line = br.readLine();
        int lineno = 1;

        if(this.matchLine(line)) {
            instantiateSmell(lineno);
        }

        while(line != null) {
            if(matchLine(line)) {
                instantiateSmell(lineno);
            }
            ++lineno;
            line = br.readLine();
        }
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
        this.smellInstances.clear();
        this.detectable = true;
    }

    /* This method tests each line against a regular expression to match any instantances of switch stament usage

       The regex is as follows:
       1.  Any amount of leading whitespace - \\s*
       1.  An identifier - [a-zA-z]\\w*
       2.  Followed by a dot - [a-zA-z]\\w*[.]
       3.  Followed by a function identifier - [a-zA-Z]\\w*[(][)]
       3.1 of which there are at least 3 - ([.][a-zA-Z]\\w*[(][)]){3,})
       4.  Followed by a semicolon - ;
     */
    private boolean matchLine(String line) {
        return line.matches("\\s*switch\\s*[(]\\w+[)]\\s*[{]");
    }

    private void instantiateSmell(int line) {
        Vector<Integer> lines = new Vector<>();
        lines.add(line);
        lines.add(line);

        SmellData data = new SmellData(NAME);
        data.setFileName(this.file.getName());
        data.setHasLineVector(true);
        data.setLines(lines);
        data.setReport(this.report(line));
        this.smellInstances.add(data);
    }

    private String report(int line) {
        return "In file: " + this.file.getName() +
                " Line: " + line + " has a switch statement consider using an alternative to this";
    }
}

