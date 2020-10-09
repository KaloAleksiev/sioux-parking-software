package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.scene.Scene;
import sample.classes.AppointmentController;
import sample.classes.DriverController;

import java.io.IOException;

public  class MainFormController {
    DriverController dc = new DriverController();
    AppointmentController ac = new AppointmentController();

    public void initData(DriverController dc, AppointmentController ac) {
        this.dc = dc;
        this.ac = ac;
    }

    public void openCreateFormButtonClick(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("screens/create.fxml"));
        Parent root = fxmlLoader.load();
        Scene createFormScene = new Scene(root);

        CreateFormController cfc = fxmlLoader.getController();
        cfc.initData(dc, ac);

        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(createFormScene);
        window.show();
    }

    public void openViewFormButtonClick(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("screens/view.fxml"));
        Parent root = fxmlLoader.load();
        Scene createFormScene = new Scene(root);

        ViewFormController vfc = fxmlLoader.getController();
        vfc.initData(dc, ac);

        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(createFormScene);
        window.show();
    }
}
