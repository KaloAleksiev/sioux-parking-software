package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;
import javafx.scene.Scene;
import sample.classes.Appointment;
import sample.classes.Driver;
import sample.classes.DriverController;
import javafx.scene.control.TextField;
import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Controller {
    @FXML private TextField tbDriverName;
    @FXML private TextField tbPhoneNumber;
    @FXML private TextField tbLicensePlate;
    @FXML private DatePicker dpAppointmentDate;
    @FXML private ChoiceBox cbAppointmentTime;

    private DriverController dc;

    public void openCreateFormButtonClick(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("screens/create.fxml"));
        Parent root = fxmlLoader.load();
        Scene createFormScene = new Scene(root);

        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(createFormScene);
        window.show();
    }

    public void populateChoiceBox() {
        cbAppointmentTime.getItems().add("16:00:00");
    }

    public void createAppointmentButtonClick() {
        dc.addDriver(new Driver(tbLicensePlate.getText(), Integer.parseInt(tbPhoneNumber.getText()), tbDriverName.getText()));
        List<Driver> driverList = new ArrayList<>();
        driverList.add(dc.getLastAddedDriver());
        Appointment appointment = new Appointment(dpAppointmentDate.getValue().getYear(), dpAppointmentDate.getValue().getMonthValue(), dpAppointmentDate.getValue().getDayOfMonth(), LocalTime.parse((String)cbAppointmentTime.getValue()), driverList);


        Alert alert = new Alert(Alert.AlertType.INFORMATION, appointment.GetInfo());
        //Alert alert = new Alert(Alert.AlertType.INFORMATION, Integer.toString(smth));
        alert.setHeaderText("Successfully created an appointment!");
        //alert.getDialogPane().setExpandableContent(new ScrollPane(new TextArea(appointment.GetInfo())));
        alert.showAndWait();
    }
}
