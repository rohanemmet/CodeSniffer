package Analysis;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Vector;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Field;
import Filetypes.Reflectable;

/* This class tests for occurrences of public fields in a class.
*/

public class InappropriateIntimacy implements ReflectSmellable{
	// variable declarations
	private static final String NAME = "Inappropriate Intimacy";
	private static InappropriateIntimacy instanceCache = new InappropriateIntimacy();
	private Vector<Reflectable> files;
	private Vector<SmellData> smellInstances;
	private boolean detectable;
	private Vector<String> publicFieldNames = new Vector<String>();
	private int publicFieldsAmount;
	
	// Singleton instantiation
	public static InappropriateIntimacy instantiate() {
		if (instanceCache == null) {
			instanceCache = new InappropriateIntimacy();
		}
		return instanceCache;
	}
	
	// Constructor sets some necessary variables
	private InappropriateIntimacy() {
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
    	
    	// Set smell report - based on number of public fields in class
    	String report = "The class " + name + " is vunerable to \"Innappropriate Intimacy,\" as there are " + n + " field(s) declared as public.";
    	
    	smell.setReport(report);
    	
    	// Set smell severity - the more methods, the worse
    	smell.setSeverity(severity);
    	
    	// Add the smell to the instances
        this.smellInstances.add(smell);			
	}
	
	
	// Main test function testing for an Inappropriate Intimacy
	@Override
	public void testSmell() throws IOException, ClassNotFoundException {
		this.smellInstances = new Vector<>();
		
		// Cycle through all class files
		for (Reflectable r : files) {
			
			// Cycle through all class files
			r.open();
			Class c = r.getJavaClass();
			
			// Get name of class
			String name = c.getName();
			
			// Store the name of any fields that are public
			Field[] fields = c.getDeclaredFields();
			for (Field field : fields) {
			    if (Modifier.isPublic(field.getModifiers()) && !(Modifier.isFinal(field.getModifiers()))) {
			    	publicFieldNames.add(name + ": " + "Field " + name + " " + field.getName());
			    	publicFieldsAmount += 1;
			    }
			}
			//if one or more public Fields exists, add smellData to smellInstances
			if (publicFieldsAmount > 0){
				setOneSmell(name, publicFieldsAmount, publicFieldsAmount);
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

	// reset class
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
