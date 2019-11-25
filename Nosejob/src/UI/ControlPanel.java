package UI;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

public class ControlPanel extends BorderPane{
    private Button runButton = new Button("RUN");
    private Button reportButton = new Button("REPORT");
    private Button optionsButton = new Button("+");
    private HBox analysisControls = new HBox();
    private HBox fileLoader = new HBox();
    private TextField fileLocation = new TextField();
    private Button openFilesButton = new Button(">>");

    ControlPanel(){

        fileLoader.setPadding(new Insets(15,12,15,12));
        fileLoader.setSpacing(10);
        fileLoader.setStyle("-fx-background-color: DAFFF3;");

        fileLocation.setPrefWidth(450);
        fileLocation.setPrefHeight(20);
        openFilesButton.setPrefSize(40, 20);

        fileLoader.getChildren().addAll(fileLocation, openFilesButton);
        fileLoader.setAlignment(Pos.CENTER);

        fileLocation.setOnMouseClicked(event -> fileLocation.setText(""));

        analysisControls.setPadding(new Insets(15,12,15,12));
        analysisControls.setSpacing(10);
        analysisControls.setStyle("-fx-background-color: DAFFF3;");

        runButton.setPrefSize(200, 20);
        runButton.setStyle("-fx-background-color: #00ff00");

        reportButton.setPrefSize(100, 20);
        reportButton.setDisable(true);
        optionsButton.setPrefSize(20, 20);

        analysisControls.setSpacing(20);
        analysisControls.getChildren().addAll(reportButton, runButton);
        analysisControls.setAlignment(Pos.CENTER);

        setCenter(fileLoader);
    }

    public void switchControlsToAnalysisControls(){ setCenter(analysisControls); }

    public void switchOnReportButton() {reportButton.setDisable(false);}

    public void setFileLoaderButtonEvent(EventHandler eventHandler){
        openFilesButton.setOnAction(eventHandler);
    }

    public void setReportButton(EventHandler eventHandler) {reportButton.setOnAction(eventHandler);}

    public void setRunButtonEvent(EventHandler eventHandler){
        runButton.setOnAction(eventHandler);
    }

    public void showNotValidError(){
        fileLocation.setText("Not Valid Path");
    }

    public String getFilePath(){
        return fileLocation.getText();
    }
}
