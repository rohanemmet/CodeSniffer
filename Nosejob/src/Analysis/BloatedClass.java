package Analysis;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Vector;
import Filetypes.Reflectable;

// Bloated class checks to see if a class has too many methods
// COMPARATIVE to this entire project's average
public class BloatedClass implements BatchSmellable<Reflectable> {
	
	// Declare variables
	private static final String NAME = "Bloated Class";
	private static BloatedClass instanceCache = new BloatedClass();
	private Vector<Reflectable> files;
	private Vector<SmellData> smellInstances;
	private boolean detectable;
	
	// Singleton instantiator
	public static BloatedClass instantiate() {
		if (instanceCache == null) {
			instanceCache = new BloatedClass();
		}
		return instanceCache;
	}
	
	// Constructor sets some necessary variables
	private BloatedClass() {
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
	
	// Main test function to actually test for a Bloated Class
	@Override
	public void testSmell() throws IOException, ClassNotFoundException {
		this.smellInstances = new Vector<>();
		HashMap<String, Integer> num_methods = new HashMap<String, Integer>();
		
		// Cycle through all class files
		for (Reflectable r : files) {

			// Open class file
			r.open();
			Class c = r.getJavaClass();
			String name = c.getName();

			// Get all the methods, and add to the hash map
			// the name of the class and the number of methods it has

			Method[] methods = c.getDeclaredMethods();
			num_methods.put(name, methods.length);
		}
		
		// Similar code as in BloatedCode to test w/ std dev
        double stddev = 0.0;
        double mean = 0.0;
        // Calculate the mean file length
        for(String key : num_methods.keySet()) {
            mean += num_methods.get(key);
        }
        mean /= num_methods.size();

        // Calculate the standard deviation of the lengths
        for(String k : num_methods.keySet()) {
            stddev += ((num_methods.get(k) - mean) * (num_methods.get(k) - mean));
        }
        stddev = Math.sqrt(stddev / (num_methods.size() - 1));

        // Find those files whose method count is more than 2 sigma greater than the mean
        for(String key : num_methods.keySet()) {
        	
        	// If the number of methods does surpass 2 std devs, then it's a potential smell
            if(num_methods.get(key) > Math.floor(mean + 2 * stddev)) {
            	// Create a smell
            	SmellData smell = new SmellData(NAME);
            	
            	// Smell has no line vector
            	smell.setHasLineVector(false);
            	
            	// Set smell report
            	int value = (int) (num_methods.get(key) - Math.floor(mean + 2*stddev));
            	String report = "The class " + key + " may be a \"bloated class,\" as its method count is " + value + " more methods than two standard deviations above the mean in this project.";
            	smell.setReport(report);
            	
            	// Set smell severity
            	int severity = 2 * value;
            	smell.setSeverity(severity);
            	
            	// Add the smell to the instances
                this.smellInstances.add(smell);
            }
        }
	}
	
	// Method to test the smell and return the vector of smell instances
	@Override
	public Vector<SmellData> getData() {
		try {
			testSmell();
		}
		catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
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
