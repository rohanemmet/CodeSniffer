package UI;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;

public class CodeLineVectorConstructor {
    private Vector<String> CodeLines = new Vector<>();

    CodeLineVectorConstructor(String fileName) throws IOException {

        // This will reference one line at a time
        String line = null;

        try {
            // FileReader reads text files in the default encoding.
            FileReader fileReader =
                    new FileReader(fileName);

            // Always wrap FileReader in BufferedReader.
            BufferedReader bufferedReader =
                    new BufferedReader(fileReader);

            while ((line = bufferedReader.readLine()) != null) {
                CodeLines.add(line);
            }

            // Close file
            bufferedReader.close();
        } finally { }
    }

    public void printCodeLines(){
        for (String line : getCodeLines()){
            System.out.println(line);
        }
    }

    public Vector<String> getCodeLines() {
        return CodeLines;
    }
}
