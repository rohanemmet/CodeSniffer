package Analysis;

import java.io.IOException;
import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Vector;
import Filetypes.Reflectable;

// Bloated class checks to see if a class has too many methods
// COMPARATIVE to this entire project's average
public class PrimitiveObsession implements BatchSmellable<Reflectable> {

    // Declare variables
    private static final String NAME = "Primitive Obsession";
    private static PrimitiveObsession instanceCache = new PrimitiveObsession();
    private Vector<Reflectable> files;
    private Vector<SmellData> smellInstances;
    private boolean detectable;

    // Singleton instantiator
    public static PrimitiveObsession instantiate() {
        if (instanceCache == null) {
            instanceCache = new PrimitiveObsession();
        }
        return instanceCache;
    }

    // Constructor sets some necessary variables
    private PrimitiveObsession() {
        this.detectable = true;
        this.files = new Vector<>();
    }

    // Add one file
    @Override
    public void addFile(Reflectable r) {
        this.files.add(r);
    }

    // Return toggle status
    @Override
    public boolean isDetectable() {
        return this.detectable;
    }

    // Toggle whether this smell should be detected
    @Override
    public void toggleDetectable() {
        this.detectable = !this.detectable;
    }

    // Test method for
    @Override
    public void testSmell() throws IOException, ClassNotFoundException {
        this.smellInstances = new Vector<>();
        int primitve_counter;
        int non_primitve_counter;
        HashMap<String, Integer> num_of_primitives = new HashMap<String, Integer>();
        HashMap<String, Integer> num_of_non_primitives = new HashMap<String, Integer>();


        // Cycle through all class files
        for (Reflectable r : files) {
            //Reset Primitive and non-primitive counter
            primitve_counter = 0;
            non_primitve_counter = 0;

            // Open class file
            r.open();
            Class c = r.getJavaClass();
            String name = c.getName();

            // Get all the fields, and add to the hash map
            // the name of the class and the number of primitive and non primitive fields it has

            Field[] fields = c.getDeclaredFields();

            for (Field f : fields) {
                if (f.getType().isPrimitive()){
                    primitve_counter++;
                } else {
                    non_primitve_counter++;
                }
            }

            num_of_primitives.put(name, primitve_counter);
            num_of_non_primitives.put(name, non_primitve_counter);
        }

        for(String key : num_of_primitives.keySet()) {
            if(num_of_primitives.get(key) > 0) {
                float ratio = (float) (num_of_non_primitives.get(key) / num_of_primitives.get(key));
                if (ratio < 0.5) {
                    // Create a smell
                    SmellData smell = new SmellData(NAME);
                    smell.setFileName(key);
                    smell.setHasLineVector(false);
                    smell.setLines(null);

                    int totalVariables = (num_of_non_primitives.get(key) + num_of_primitives.get(key));
                    float percentage = (float) num_of_primitives.get(key) / totalVariables;
                    percentage = percentage * 100;

                    DecimalFormat form = new DecimalFormat("0.00");

                    smell.setReport("The class " + key + " may present a case of primitive obsession as of the " + totalVariables + " variables " + num_of_primitives.get(key) + " (" + form.format(percentage) + "%)" + " of them are primitive");

                    int severity = (int) percentage/10;
                    smell.setSeverity(severity);
                    // Add the smell to the instances
                    this.smellInstances.add(smell);
                }
            }
        }
    }

    // Method to test the smell and return the vector of smell instances
    @Override
    public Vector<SmellData> getData() {
        try {
            testSmell();
        }
        catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return this.smellInstances;
    }

    // Clear all smell instances
    @Override
    public void refresh() {
        this.smellInstances.clear();
    }

    // Totally reset class
    @Override
    public void reset() {
        this.files = null;
        this.smellInstances = null;
        this.detectable = true;
    }

    // Create full list of files to add
    public void addFiles(Vector<Reflectable> files) {
        this.files = files;
    }
}
