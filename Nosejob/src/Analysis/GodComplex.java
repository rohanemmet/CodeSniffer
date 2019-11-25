package Analysis;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Vector;

import Filetypes.Reflectable;

// God complex checks to see if a class has an absurd number of methods
public class GodComplex implements ReflectSmellable {
	
	// Declare variables
	private static final String NAME = "God Complex";
	private static GodComplex instanceCache = new GodComplex();
	private Vector<Reflectable> files;
	private Vector<SmellData> smellInstances;
	private boolean detectable;
	
	// Singleton instantiator
	public static GodComplex instantiate() {
		if (instanceCache == null) {
			instanceCache = new GodComplex();
		}
		return instanceCache;
	}
	
	// Constructor sets some necessary variables
	private GodComplex() {
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
    	
    	// Set smell report - based on number of methods in class
    	String report = "The class " + name + " may be a \"God Complex,\" as there are " + n + " methods declared in it.";
    	smell.setReport(report);
    	
    	// Set smell severity - the more methods, the worse
    	smell.setSeverity(severity);
    	
    	// Add the smell to the instances
        this.smellInstances.add(smell);
	}
	
	// Main test function to actually test for a God Complex
	@Override
	public void testSmell() throws IOException, ClassNotFoundException {
		this.smellInstances = new Vector<>();
		
		// Cycle through all class files
		for (Reflectable r : files) {
			
			// Open a class file for reflection
			r.open();
			Class c = r.getJavaClass();
			
			// Get name of class
			String name = c.getName();
			
			// Find out how many methods are in the class
			// If there are >= 10, this class might be a god complex
			int num_methods = c.getDeclaredMethods().length;
			if (num_methods >= 10 && num_methods <= 12) {
				setOneSmell(name, num_methods - 7, num_methods);
			}
			else if (num_methods >= 13 && num_methods <= 18) {
				setOneSmell(name, (num_methods-1)/2, num_methods);
			}
			else if (num_methods >= 19 && num_methods <= 24) {
				setOneSmell(name, 9, num_methods);
			}
			else if (num_methods >= 25) {
				setOneSmell(name, 10, num_methods);
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
