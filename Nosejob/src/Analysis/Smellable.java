package Analysis;

import java.io.IOException;
import java.util.Vector;

// Interface of all code smell test classes. Each smell test
// will produce a smellData object and be set detectable or not.

public interface Smellable {
    public boolean isDetectable();
    public void toggleDetectable();
    void testSmell() throws IOException, ClassNotFoundException;
    public Vector<SmellData> getData();

    // This method is used to clear the smell instance vector
    // of each test class between runs. Not doing so results in the same
    // smells being returned every time the test is run after they are found
    public void refresh();

    // All smellable objects are singletons and must be reset between runs
    // to avoid corruption of the current test
    public void reset();
}
