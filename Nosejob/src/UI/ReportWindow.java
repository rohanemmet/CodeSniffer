package UI;

import Analysis.SmellData;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class ReportWindow extends Stage{

    ReportWindow(Vector<SmellData> data){
        requestFocus();
        setAlwaysOnTop(true);
        setTitle("REPORT");

        StackPane root = new StackPane();
        BorderPane borderPane = new BorderPane();
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        VBox reportLines = new VBox();
        VBox graph = new VBox();
        HashMap<String, Integer> codeSmells = new HashMap<String, Integer>();

        for(SmellData sd : data) {

            codeSmells.merge(sd.getSmellType(), 1, (a, b) -> a + b);

            Label report = new Label(sd.getReport());
            report.setWrapText(true);
            reportLines.getChildren().add(new Label(sd.getSmellType()));
            reportLines.getChildren().add(report);
            reportLines.getChildren().add(new Label("Severity = " + sd.getSeverity()));
            reportLines.getChildren().add(new Label(""));
        }

        Label graphTitle = new Label("---Graph---");
        reportLines.getChildren().add(graphTitle);

        String color = "-fx-background-color: FF0000;";

        for(Map.Entry<String, Integer> entry : codeSmells.entrySet()) {
            String key = entry.getKey();
            Integer value = entry.getValue();

            if (color.contains("FF0000")){
                color = "-fx-background-color: DA11FF;";
                color = "-fx-background-color: blue";
            } else {
                color = "-fx-background-color: FF0000;";
            }

            HBox graphLine = new HBox();
            Label title = new Label(entry.getKey());
            title.setPrefWidth(140);
            graphLine.getChildren().add(title);

            for (int i = 0 ; i < entry.getValue() ; i++){
                Label line = new Label("\t");
                line.setStyle(color);
                graphLine.getChildren().add(line);
            }

            graphLine.getChildren().add(new Label("[" +  entry.getValue() + "]"));
            graph.getChildren().add(graphLine);
        }

        reportLines.getChildren().add(graph);

        HBox controls = new HBox();
        Button saveButton = new Button("SAVE");
        controls.setAlignment(Pos.CENTER);
        saveButton.setAlignment(Pos.CENTER);

        saveButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                /*

                Implement Save To Text File

                 */
            }
        });

        controls.getChildren().add(saveButton);
        scrollPane.setContent(reportLines);
        borderPane.setCenter(scrollPane);
        borderPane.setBottom(controls);
        root.getChildren().add(borderPane);
        setScene(new Scene(root, 600, 500));
        show();

        focusedProperty().addListener(new ChangeListener<Boolean>()
        {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue)
            {
                if (!newPropertyValue)
                    close();
            }
        });

    }
}
