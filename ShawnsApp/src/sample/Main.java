package sample;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    public Main() {
    }

    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("screens/main.fxml"));
        Scene scene = new Scene(root, 900, 650);
        scene.getStylesheets().add
                (this.getClass().getResource("styles/styles.css").toExternalForm());
        primaryStage.setTitle("Sioux Parking");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
