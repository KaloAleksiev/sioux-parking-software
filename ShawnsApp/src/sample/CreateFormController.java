package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sample.classes.Appointment;
import sample.classes.Driver;
import sample.classes.DriverController;
import java.io.IOException;
import java.net.URL;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class CreateFormController implements Initializable {

    @FXML private TextField tbDriverName;
    @FXML private TextField tbPhoneNumber;
    @FXML private TextField tbLicensePlate;
    @FXML private DatePicker dpAppointmentDate;
    @FXML private ChoiceBox<String> cbAppointmentTime;

    private DriverController dc;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        populateChoiceBox();
    }

    public void populateChoiceBox() {
        cbAppointmentTime.getItems().add("12:00:00");
        cbAppointmentTime.getItems().add("13:00:00");
        cbAppointmentTime.getItems().add("14:00:00");
        cbAppointmentTime.getItems().add("15:00:00");
        cbAppointmentTime.getItems().add("16:00:00");
    }

    public void createAppointmentButtonClick() {
        if (tbLicensePlate.getText() != "" && tbPhoneNumber.getText() != "" && tbDriverName.getText() != "" && dpAppointmentDate.getValue() != null && cbAppointmentTime.getValue() != null) {
            //dc.addDriver(new Driver(tbLicensePlate.getText(), Integer.parseInt(tbPhoneNumber.getText()), tbDriverName.getText()));
            List<Driver> driverList = new ArrayList<>();
            driverList.add(new Driver(tbLicensePlate.getText(), Integer.parseInt(tbPhoneNumber.getText()), tbDriverName.getText()));
            //driverList.add(dc.getLastAddedDriver());
            Appointment appointment = new Appointment(dpAppointmentDate.getValue().getDayOfMonth(), dpAppointmentDate.getValue().getMonthValue(), dpAppointmentDate.getValue().getYear(), LocalTime.parse(cbAppointmentTime.getValue()), driverList);

            Alert alert = new Alert(Alert.AlertType.INFORMATION, appointment.GetInfo());
            alert.setHeaderText("Successfully created an appointment!");
            alert.showAndWait();
            resetForm();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Please fill in all the information!");
            alert.showAndWait();
        }
    }

    public void resetForm() {
        tbDriverName.clear();
        tbLicensePlate.clear();
        tbPhoneNumber.clear();
        dpAppointmentDate.setValue(null);
        cbAppointmentTime.setValue(null);
    }

    public void buttonCancelClick(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("screens/main.fxml"));
        Parent root = fxmlLoader.load();
        Scene createFormScene = new Scene(root);

        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(createFormScene);
        window.show();
    }
}
