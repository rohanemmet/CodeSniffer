import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class NoseJobUI extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception{

        UI.UI userInt = new UI.UI();

        primaryStage = userInt;
        primaryStage.setScene(new Scene(userInt.getRoot(), 1000, 600));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
