package Analysis;

import Filetypes.Reflectable;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Vector;

/* This class tests for excessively long parameter lists to methods in the
   input class using Reflection

   This test is run on files individually
 */

public class ParameterBloat implements ReflectSmellable {
    private static final String NAME = "Parameter Bloat";
    private static ParameterBloat instanceCache;
    private Reflectable file;
    private Vector<SmellData> smellInstances;
    private boolean detectable;
    private int threshold;

    public static ParameterBloat instantiate(int threshold) {
        if(instanceCache == null)
            instanceCache = new ParameterBloat(threshold);
        return instanceCache;
    }

    private ParameterBloat(int threshold) {
        this.smellInstances = new Vector<>();
        this.detectable = true;
        this.threshold = threshold;
    }

    @Override
    public void setFile(Reflectable t) {
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
    public void testSmell() throws IOException, ClassNotFoundException {
        Class cls = this.file.getJavaClass();
        Method[] methods = cls.getDeclaredMethods();
        for(Method m : methods) {
            Parameter[] params = m.getParameters();
            if(params.length > threshold) {
                instantiateSmell(cls.getName(), m.getName(), params.length);
            }
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
        this.smellInstances.clear();
        this.file = null;
        this.detectable = true;
    }

    private void instantiateSmell(String cls, String mtd, int numparams) {
        SmellData data = new SmellData(NAME);
        data.setFileName(this.file.getName());
        data.setHasLineVector(false);
        data.setLines(null);
        data.setReport(report(cls, mtd, numparams));
        this.smellInstances.add(data);
    }

    private String report(String cls, String mtd, int len) {
        return "In class " + cls + " method " + mtd + "() has a parameter list of length " + len
                + "\nConsider creating a wrapper object for related parameters to enhance readability.";
    }
}
