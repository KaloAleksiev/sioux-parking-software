package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.scene.Scene;
import sample.classes.Appointment;
import sample.classes.AppointmentController;
import sample.classes.Driver;
import sample.classes.DriverController;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public  class MainFormController implements Initializable {
    DriverController dc;
    AppointmentController ac;

    public MainFormController() throws SQLException {
        dc = new DriverController();
        ac = new AppointmentController();
    }

    public void initData(DriverController dc, AppointmentController ac) {
        this.dc = dc;
        this.ac = ac;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

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

    public void openViewFormButtonClick(ActionEvent event) throws IOException, SQLException {
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
