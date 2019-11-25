package Analysis;

import java.util.Vector;

public class SmellData {
	
	// Only set if hasLineVector == true
    private String fileName = "UNDEFINED";
    
    // Mandatory to set: in the constructor- SmellData(String)
    private String smellType;
    
    // Mandatory to set: use setReport(String)
    private String report;
    
    // Only set if hasLineVector == true
    private Vector<Integer> lines;
    
    // Mandatory to set: use setSeverity(int)
    private int severity = 1;
    
    // Mandatory to set: use setHasLineVector(boolean)
    private boolean hasLineVector;

    SmellData(String smellType){
        this.smellType = smellType;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setHasLineVector(boolean hasLineVector) {
        this.hasLineVector = hasLineVector;
    }

    public boolean HasLines() {
        return hasLineVector;
    }

    public String getSmellType() {
        return smellType;
    }

    public void setReport(String report){
        this.report = report;
    }

    public String getReport() {
        return report;
    }

    /* Certain smells occur over a number of lines, this method taking a
       vector allows us to specify the range and specific set of lines.
       A smell which occurs inline can be set using the same line number twice
     */
    public void setLines(Vector<Integer> lines) {
        this.lines = lines;
    }

    public Vector<Integer> getLines(){
        return lines;
    }
    
    public void setSeverity(int x) {
    	if (x > 10) x = 10;
    	if (x < 1) x = 1;
    	this.severity = x;
    }
    
    public int getSeverity() {
    	return severity;
    }

    public String getTrueName() {
        if (fileName.lastIndexOf('/') == -1 | fileName.lastIndexOf('.') == -1){
            return fileName;
        } else {
            return fileName.substring(fileName.lastIndexOf('/') + 1, fileName.lastIndexOf('.'));
        }
    }
}
