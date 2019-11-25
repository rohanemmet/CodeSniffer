package UI;

import Analysis.Analyser;
import Analysis.SmellData;
import Filetypes.FileList;
import Filetypes.IncorrectFileTypeException;
import Filetypes.TextReadable;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Vector;

public class UI extends Stage{
    private StackPane root = new StackPane();
    private Vector<CodeDisplayPanel> codePanels = new Vector<>();
    private FileSelectorPanel fileSelector = new FileSelectorPanel();;
    private ControlPanel controlPanel = new ControlPanel();
    private BorderPane borderPaneRoot = new BorderPane();
    private String fileName;
    private FileList list = FileList.instantiate();
    private Vector<SmellData> data;



    public UI() throws Exception{
        setTitle("Nose Job");

        borderPaneRoot.setBottom(controlPanel);

        controlPanel.setFileLoaderButtonEvent(new EventHandler() {
            @Override
            public void handle(Event event) {

                fileName = controlPanel.getFilePath();

                if (controlPanel.getFilePath().isEmpty()) {
                    controlPanel.showNotValidError();
                } else {
                    controlPanel.switchControlsToAnalysisControls();
                    try {
                        fileTextToCodePanels(fileName);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    fileSelector.loadFiles(codePanels, borderPaneRoot);
                }
            }
        });

        controlPanel.setRunButtonEvent(new EventHandler() {
            @Override
            public void handle(Event event){
                try {
                    runJavaTestBattery();
                    controlPanel.switchOnReportButton();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

                for (SmellData d : data){
                    if (d.HasLines()) {
                        for (CodeDisplayPanel codeReference : codePanels) {
                            String fileName = codeReference.getTrueName();
                            System.out.println(fileName);
                            if (fileName.equals(d.getTrueName())) {
                                codeReference.setRed(d.getLines());
                            }
                        }
                    }
                }
            }
        });

        controlPanel.setReportButton(new EventHandler() {
            @Override
            public void handle(Event event) {
                new ReportWindow(data);
            }
        });

        root.getChildren().add(borderPaneRoot);

        borderPaneRoot.setLeft(fileSelector);
    }

    public void fileTextToCodePanels (String fileName) throws IOException {
        try {
            list.addDirectory(fileName, true);
        } catch (IncorrectFileTypeException | FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
        list.constructFromDir();
        Vector<Vector<String>> codes = new Vector<>();
        Vector<String> javaFileNames = new Vector<>();

        for (TextReadable file : list.getJavaFiles()){
            javaFileNames.add(getJavaFileName(file.getName()));
            codes.add(new CodeLineVectorConstructor(file.getName()).getCodeLines());
        }

        loadCodePanels(javaFileNames, codes);
    }

    private String getJavaFileName(String path){
        String javaName = path.substring(fileName.length());
        javaName = javaName.substring(javaName.lastIndexOf('/') + 1, javaName.lastIndexOf('.'));

        return javaName;
    }

    private void loadCodePanels(Vector<String> javaFileNames, Vector<Vector<String>> CodesLines){
        int i = 0;
        for (Vector<String> Codes : CodesLines){
            codePanels.add(new CodeDisplayPanel(javaFileNames.elementAt(i++), Codes));
        }
    }

    private void runJavaTestBattery() throws IOException, ClassNotFoundException {
        String directoryPath = fileName;

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
           /*

           Use Highlight Red Method To Highlight Lines

            */
        }
    }

    public void loadCodeView(CodeDisplayPanel codeView){
        borderPaneRoot.setCenter(codeView);
    }

    public StackPane getRoot() {
        return root;
    }
}
