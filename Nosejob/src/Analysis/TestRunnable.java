package Analysis;

import java.io.IOException;
import java.util.Vector;

/* Public interface of the test battery classes
 */

public interface TestRunnable {
    public void testSmells() throws IOException, ClassNotFoundException;
    public Vector<SmellData> getSmells();
}
