import Analysis.Analyser;
import Analysis.SmellData;
import Filetypes.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Vector;

public class Nosejob {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        //Example data flow

        FileList list = FileList.instantiate();
        Vector<SmellData> data;
        String directoryPath = "/Users/LilDragon/Documents/GitHub/NoseJob/Nosejob";

        try {
            list.addDirectory(directoryPath, false);
        } catch (FileNotFoundException | IncorrectFileTypeException e) {
            System.out.println(e.getMessage());
        }
        list.constructFromDir();

        Analyser smellAnalyser = new Analyser(list);
        smellAnalyser.analyse();
        data = smellAnalyser.analysisResults();

        for(SmellData sd : data) {
            System.out.println(sd.getReport() + "\n");
        }

        System.exit(0);
    }
}