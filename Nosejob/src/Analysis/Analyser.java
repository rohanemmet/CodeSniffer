package Analysis;

import Filetypes.FileList;

import java.io.IOException;
import java.util.Vector;

public class Analyser {
    private JavaTestBattery jbattery;
    private ClassTestBattery cbattery;
    private Vector<SmellData> data;

    public Analyser(FileList fileList) {
        this.data = new Vector<>();
        this.jbattery = new JavaTestBattery(fileList.getJavaFiles());
        this.cbattery = new ClassTestBattery(fileList.getClassFiles());
    }

    public void analyse() throws IOException, ClassNotFoundException {
        this.testJavaSmells();
        this.testClassSmells();
    }

    public Vector<SmellData> analysisResults() {
        this.collateData();
        return this.data;
    }

    private void collateData() {
        this.data.addAll(jbattery.getSmells());
        this.data.addAll(cbattery.getSmells());
    }

    private void testJavaSmells() throws IOException, ClassNotFoundException {
        this.jbattery.testSmells();
    }

    private void testClassSmells() throws IOException, ClassNotFoundException {
        this.cbattery.testSmells();
    }
}