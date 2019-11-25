package Analysis;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Vector;
import Filetypes.Reflectable;

// Bloated class checks to see if a class has too many methods
// COMPARATIVE to this entire project's average
public class longIdentifiers implements BatchSmellable<Reflectable> {

    // Declare variables
    private static final String NAME = "Long Identifiers";
    private static longIdentifiers instanceCache = new longIdentifiers();
    private Vector<Reflectable> files;
    private Vector<SmellData> smellInstances;
    private boolean detectable;

    // Singleton instantiator
    public static longIdentifiers instantiate() {
        if (instanceCache == null) {
            instanceCache = new longIdentifiers();
        }
        return instanceCache;
    }

    // Constructor sets some necessary variables
    private longIdentifiers() {
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


        // Cycle through all class files
        for (Reflectable r : files) {


            // Open class file
            r.open();
            Class c = r.getJavaClass();
            String name = c.getName();

            // Get all the fields, class and methods and add the identifier to the string vector

            Field[] fields = c.getDeclaredFields();
            Class[] classes = c.getDeclaredClasses();
            Method[] methods = c.getDeclaredMethods();

            Vector<String> identifiers = new Vector<>();

            for (Field f : fields) {
                identifiers.add(f.getName());
            }

            for (Class cl : classes) {
                identifiers.add(c.getName());
            }

            for (Method m : methods) {
                identifiers.add(m.getName());
            }

            for(String id : identifiers) {
                if(id.length() > 25) {
                    // Create a smell
                    SmellData smell = new SmellData(NAME);

                    // Smell has no line vector
                    smell.setHasLineVector(false);

                    // Set smell report
                    String report = "The identifier [" + id + "] in class " + name + " is excessively long; consider using a shorter identifier";
                    smell.setReport(report);

                    // Set smell severity
                    int severity = (id.length() - 25)/5;
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
