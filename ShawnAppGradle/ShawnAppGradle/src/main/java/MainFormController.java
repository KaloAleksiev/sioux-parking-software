package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.scene.Scene;
import java.io.IOException;

public  class MainFormController {

    public void openCreateFormButtonClick(ActionEvent event) throws IOException {
        openForm(event, "screens/create.fxml");
    }

    public void openViewFormButtonClick(ActionEvent event) throws IOException {
        openForm(event, "screens/view.fxml");
    }

    public void openForm(ActionEvent event, String path) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(path));
        Parent root = fxmlLoader.load();
        Scene createFormScene = new Scene(root);

        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(createFormScene);
        window.show();
    }
}
