package sample.models;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


import java.io.IOException;

public class FXMLhelper {

    private FXMLLoader fxmlLoader;

    public FXMLLoader getFxmlLoader() {
        return fxmlLoader;
    }

    public FXMLhelper() {
        this.fxmlLoader = new FXMLLoader();
    }

    public Scene createScene(String screen) throws IOException {
        this.fxmlLoader.setLocation(getClass().getResource("../screens/" + screen + ".fxml"));
        Parent root = this.fxmlLoader.load();
        Scene scene = new Scene(root);
        return  scene;
    }

    public void showScene(Scene scene, ActionEvent event){
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }
}
