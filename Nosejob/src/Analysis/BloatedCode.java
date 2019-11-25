package Analysis;

/* This class runs the test for bloated code. This a comparative measure
   between files in the test set. A comparatively large file in terms of lines
   will flag a bloated code warning.
 */

import Filetypes.Openable;
import Filetypes.TextReadable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Vector;

public class BloatedCode implements BatchSmellable<TextReadable>{
    private static final String NAME = "Bloated Code";
    private static BloatedCode instanceCache = new BloatedCode();
    private HashMap<String, Long> lengthCounts;
    private Vector<TextReadable> files;
    private Vector<SmellData> smellInstances;
    private boolean detectable;

    public static BloatedCode instantiate() {
        if(instanceCache == null) {
            instanceCache = new BloatedCode();
        }
        return instanceCache;
    }

    private BloatedCode() {
        this.smellInstances = new Vector<>();
        this.lengthCounts = new HashMap<>();
        this.detectable = true;
    }

    @Override
    public void addFile(TextReadable t) {
        this.files.add(t);
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
    // Uses line counting method in java.nio package.
    // This omits the counting of trailing newlines in a file
    public void testSmell() throws IOException {
        double stddev = 0;
        double mean = 0.0;
        for(Openable o : files) {
            Path path = Paths.get(o.getName());
            lengthCounts.put(o.getName(), Files.lines(path).count());
        }

        // Calculate the mean file length
        for(String key : lengthCounts.keySet()) {
            mean += lengthCounts.get(key);
        }
        mean /= lengthCounts.size();

        // Claculate the standard deviation of the lengths
        for(String key : lengthCounts.keySet()) {
            stddev += ((lengthCounts.get(key) - mean) * (lengthCounts.get(key) - mean));
        }
        stddev = Math.sqrt(stddev / (lengthCounts.size() - 1));

        // Find those files whose length is more than 2 sigma greater than the mean
        for(String key : lengthCounts.keySet()) {
            if(lengthCounts.get(key) > Math.floor(mean + 2 * stddev)) {
                //TODO: Calculate severity better
                int severity = (int) Math.floor(lengthCounts.get(key) / stddev);
                this.instantiateSmell(key, mean, stddev);
            }
        }
    }

    @Override
    public Vector<SmellData> getData() {
    	/*try {
			testSmell();
		}
		catch (IOException e) {
			e.printStackTrace();
		}*/
        return this.smellInstances;
    }

    @Override
    public void refresh() {
        this.smellInstances.clear();
    }

    @Override
    public void reset() {
        this.lengthCounts.clear();
        this.files.clear();
        this.smellInstances.clear();
        this.detectable = true;
    }

    @Override
    public void addFiles(Vector<TextReadable> files) {
        this.files = files;
    }

    private void instantiateSmell(String s, double mean, double stddev) {
        SmellData data = new SmellData(NAME);
        data.setFileName(s);
        data.setHasLineVector(false);
        data.setLines(null);
        data.setReport(this.report(s, lengthCounts.get(s), mean, stddev));
        this.smellInstances.add(data);
    }

    private String report(String name, long len, double mean, double stddev) {
        return "File: " + name + " is relatively large in comparisson to other files (>= 2 sigma)" +
                "\n\tLength of " + name + " = " + len +
                "\n\tMean file length = " + mean +
                "\n\tStandard devation = " + stddev;
    }
}
