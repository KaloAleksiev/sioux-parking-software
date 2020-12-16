package sample.screenControllers;

import sample.controllers.AppointmentController;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import sample.controllers.DriverController;
import sample.Helper;
import sample.models.Appointment;

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
    private Helper helper;
    private Appointment appointment;

    public void initData(DriverController dc, AppointmentController ac, Appointment appointment) {
        // When the Form is opened, the Controllers are transferred from the previous Form.
        this.dc = dc;
        this.ac = ac;
        this.appointment = appointment;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.helper = new Helper();
    }

    public void btnDoneClick(ActionEvent event) throws SQLException, IOException {
        // If all the Text Boxes are filled, creates a new Driver, otherwise throws an Error Notification.
        if (tbLicensePlate.getText() != "" && tbPhoneNumber.getText() != "" && tbDriverName.getText() != "" ) {
            dc.addDriver(tbPhoneNumber.getText(), tbLicensePlate.getText(), tbDriverName.getText());
            determineForm(event,this.appointment );
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Please fill in all the information!");
            alert.showAndWait();
        }
    }

    public void btnCancelClick(ActionEvent event) throws IOException {
        determineForm(event, this.appointment);
    }

    public void determineForm(ActionEvent event,Appointment app) throws IOException {

        if(app == null){
            Scene scene = helper.createScene("create");
            CreateFormController cfc = helper.getFxmlLoader().getController();
            cfc.initData(dc, ac);
            helper.showScene(scene, event);
        }
        else {
            Scene scene = helper.createScene("editAppointment");
            EditAppointmentController cfc = helper.getFxmlLoader().getController();
            cfc.initData(dc, ac, app);
            helper.showScene(scene, event);
        }

    }
}
