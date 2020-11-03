package sample.screenControllers;

import sample.controllers.AppointmentController;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sample.controllers.DriverController;
import sample.models.FXMLhelper;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AddDriverFormController implements Initializable{
    // Initializing all the Text Boxes in the Form.
    @FXML private TextField tbDriverName;
    @FXML private TextField tbPhoneNumber;
    @FXML private TextField tbLicensePlate;

    DriverController dc;
    AppointmentController ac;
    private FXMLhelper fxmlHelper;

    public void initData(DriverController dc, AppointmentController ac) {
        // When the Form is opened, the Controllers are transferred from the previous Form.
        this.dc = dc;
        this.ac = ac;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.fxmlHelper = new FXMLhelper();
    }

    public void btnDoneClick() throws SQLException {
        // If all the Text Boxes are filled, creates a new Driver, otherwise throws an Error Notification.
        if (tbLicensePlate.getText() != "" && tbPhoneNumber.getText() != "" && tbDriverName.getText() != "" ) {
            dc.AddDriver(tbPhoneNumber.getText(), tbLicensePlate.getText(), tbDriverName.getText());

        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Please fill in all the information!");
            alert.showAndWait();
        }
    }

    public void btnCancelClick(ActionEvent event) throws IOException {
        openCreateForm(event);
    }

    public void resetForm() {
        // Resets all the Text Boxes in the Form.
        tbDriverName.clear();
        tbLicensePlate.clear();
        tbPhoneNumber.clear();
    }

    public void openCreateForm(ActionEvent event) throws IOException {
        Scene scene = fxmlHelper.createScene("create");
        CreateFormController cfc = fxmlHelper.getFxmlLoader().getController();
        cfc.initData(dc, ac);
        fxmlHelper.showScene(scene, event);
    }
}
