package UI;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

import java.util.Vector;

public class CodeDisplayPanel extends ScrollPane{
    private String fileName;
    private Vector<Label> codeLines = new Vector<>();

    CodeDisplayPanel(String fileName, Vector<String> codeLines){
        this.fileName = fileName;
        VBox vBox = new VBox();
        vBox.setPadding(new Insets(4));
        vBox.setSpacing(0);

        Label fileTitle = new Label(fileName);
        fileTitle.setStyle("-fx-font-weight: bold;");
        this.codeLines.add(fileTitle);

        for (int i = 0 ; i < codeLines.size() ; i++){
                this.codeLines.add(new Label("  " + (i + 1) + "\t\t" + codeLines.elementAt(i)));
        }

        for (Label label : this.codeLines){
            VBox.setMargin(label, new Insets(0, 0, 0, 4));
            vBox.getChildren().add(label);
        }

        setContent(vBox);
    }

    public String getFileName() {
        return fileName;
    }

    public boolean isFile(String key){
        return fileName.equals(key);
    }

    public void setRed(Vector<Integer> lines){
        for (Integer line : lines){
            codeLines.elementAt(line).setStyle("-fx-background-color: FF0000;");
        }
    }

    public String getTrueName() {
        if (fileName.lastIndexOf('/') == -1 | fileName.lastIndexOf('.') == -1){
            return fileName;
        } else {
            return fileName.substring(fileName.lastIndexOf('/') + 1, fileName.lastIndexOf('.'));
        }
    }
}
