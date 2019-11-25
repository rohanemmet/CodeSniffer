package Analysis;

import Filetypes.Reflectable;

import java.io.IOException;
import java.util.Vector;

public class ClassTestBattery implements TestRunnable {
    private Vector<Reflectable> classFiles;
    private Vector<ReflectSmellable> individualSmellTests;
    private Vector<BatchSmellable<Reflectable>> batchSmellTests;
    private Vector<SmellData> reflectSmellData;

    public ClassTestBattery(Vector<Reflectable> files) {
        this.classFiles = files;
        this.individualSmellTests = new Vector<>();
        this.batchSmellTests = new Vector<>();
        this.reflectSmellData = new Vector<>();

        batchSmellTests.add(BloatedClass.instantiate());
        individualSmellTests.add(ParameterBloat.instantiate(3));
        batchSmellTests.add(PrimitiveObsession.instantiate());
        batchSmellTests.add(longIdentifiers.instantiate());
        batchSmellTests.add(shortIdentifiers.instantiate());
    }

    public void testSmells() throws IOException, ClassNotFoundException {
        for(BatchSmellable<Reflectable> t : batchSmellTests) {
            t.addFiles(classFiles);
            t.testSmell();
            this.reflectSmellData.addAll(t.getData());
            t.refresh();
        }

        for(Reflectable r : classFiles) {
            for(ReflectSmellable ts : individualSmellTests) {
                ts.setFile(r);
                ts.testSmell();
                this.reflectSmellData.addAll(ts.getData());
                ts.refresh();
            }
        }
    }

    public Vector<SmellData> getSmells() {
        return this.reflectSmellData;
    }
}
