package UI;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.io.FileInputStream;
import java.util.Vector;


public class FileSelectorPanel extends ScrollPane{

    private TilePane tile = new TilePane();
    private Image fileImage;


    FileSelectorPanel( ) throws Exception{

        try {
            fileImage = new imageLoader().loadImage();
        } catch (NullPointerException e) {
            fileImage = new Image("file:images/fileimage.png");
        }

        tile.setPadding(new Insets(10,10,10,10));
        tile.setVgap(6);
        tile.setHgap(6);
        tile.setPrefColumns(1);

        setVbarPolicy(ScrollBarPolicy.ALWAYS);
        setStyle("-fx-background: DAE6F3;");
        setContent(tile);
        setMaxWidth(200);
        setMinWidth(100);
        setVisible(false);
        setFitToWidth(true);
    }

    public void loadFiles(Vector<CodeDisplayPanel> codePanels , BorderPane borderPane){
        setVisible(true);

        int NUMBER_OF_FILES = codePanels.size();

        ImageView[] pagesImage = new ImageView[NUMBER_OF_FILES];
        VBox[] pages = new VBox[NUMBER_OF_FILES];

        for (int i = 0; i < NUMBER_OF_FILES; i++){
            Integer in = i;

            pagesImage[i] = new ImageView(fileImage);
            pagesImage[i].setFitHeight(60);
            pagesImage[i].setFitWidth(44);
            pages[i] = new VBox();

            String javaName = codePanels.elementAt(i).getFileName();
            Label label = new Label();

            // Shortens displayed file name and adds tooltip with full file name
            if (javaName.length() > 18) {
                label.setTooltip(new Tooltip(javaName));
                javaName = javaName.substring(0, 18).concat("...");
            }

            label.setText(javaName);

            pages[i].getChildren().addAll(pagesImage[i], label);
            pages[i].setPadding(new Insets(5,5,5,5));
            pages[i].setOnMouseEntered(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    pages[in].setBorder(new Border( new BorderStroke( Color.BLUE, BorderStrokeStyle.SOLID, null, null ) ));
                }
            });

            pages[i].setOnMouseExited(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    pages[in].setBorder(new Border( new BorderStroke( null, BorderStrokeStyle.NONE, null, null ) ));
                }
            });

            pages[i].setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    borderPane.setCenter(codePanels.elementAt(in));
                }
            });

            tile.getChildren().addAll(pages[i]);
        }

        borderPane.setCenter(codePanels.elementAt(0));
    }


    private class imageLoader{
        FileInputStream input;
        imageLoader() throws Exception{
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            String path = classLoader.getResource("images/fileimage.png").getPath();
            input = new FileInputStream(path);
        }

        private Image loadImage(){
            return new Image(input);
        }
    }
}
