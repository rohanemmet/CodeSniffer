package Analysis;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Vector;

import Filetypes.Reflectable;

// Lazy class checks if a class has too few methods
public class LazyClass implements ReflectSmellable {
	
	// Declare variables
	private static final String NAME = "Lazy Class";
	private static LazyClass instanceCache = new LazyClass();
	private Vector<Reflectable> files;
	private Vector<SmellData> smellInstances;
	private boolean detectable;
	
	// Singleton instantiator
	public static LazyClass instantiate() {
		if (instanceCache == null) {
			instanceCache = new LazyClass();
		}
		return instanceCache;
	}
	
	// Constructor sets some necessary variables
	private LazyClass() {
		this.detectable = true;
		this.files = new Vector<>();
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

	// Helper class to set one smell and add it to the vector of smellData
	private void setOneSmell(String name, int severity, int n) {
		// Create a smell
    	SmellData smell = new SmellData(NAME);
    	
    	// Smell has no line vector
    	smell.setHasLineVector(false);
    	
    	// Set smell report based on how many methods (0-2) the class has
    	String report = null;
    	if (n == 0) {
    		report = "The class " + name + " may be a \"Lazy Class,\" as there are no methods declared in it.";
    	}
    	else if (n == 1) {
    		report = "The class " + name + " may be a \"Lazy Class,\" as there is only one method declared in it.";
    	}
    	else if (n == 2) {
    		report = "The class " + name + " may be a \"Lazy Class,\" as there are only two methods declared in it.";
    	}
    	smell.setReport(report);
    	
    	// Set smell severity
    	smell.setSeverity(severity);
    	
    	// Add the smell to the instances
        this.smellInstances.add(smell);
	}
	
	// Main function to actually test for a Lazy Class
	@Override
	public void testSmell() throws IOException, ClassNotFoundException {
		this.smellInstances = new Vector<>();
		
		// Loop through class files
		for (Reflectable r : files) {
			
			// Open class file for reflection
			r.open();
			Class c = r.getJavaClass();
			
			// Get name of class
			String name = c.getName();
			
			// Find out how many methods are in the class
			// If it's 0, 1, or 2, this may be a lazy class, set a smell
			int num_methods = c.getDeclaredMethods().length;
			if (num_methods == 0) {
				setOneSmell(name, 10, num_methods);
			}
			else if (num_methods == 1) {
				setOneSmell(name, 5, num_methods);
			}
			else if (num_methods == 2) {
				setOneSmell(name, 1, num_methods);
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
		this.files.clear();
		this.smellInstances.clear();
		this.detectable = true;
	}

	// Add a file to the file vector
	@Override
	public void setFile(Reflectable t) throws FileNotFoundException {
		this.files.add(t);
	}
}
