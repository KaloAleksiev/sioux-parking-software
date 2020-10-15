package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import sample.classes.Appointment;
import sample.classes.AppointmentController;
import sample.classes.DataControl;
import sample.classes.Driver;
import sample.classes.DriverController;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class CreateFormController implements Initializable {
    @FXML private DatePicker dpAppointmentDate;
    @FXML private ChoiceBox<String> cbAppointmentTime;
    @FXML private ListView<String> lvAllDrivers;
    @FXML private ListView<String> lvAddedDrivers;

    private DriverController dc;
    private AppointmentController ac;

    private List<Driver> availableDriversList;
    private List<Driver> addedDriversList;

    private DataControl dataController;


    public void initData(DriverController dc, AppointmentController ac) {
        this.dc = dc;
        this.ac = ac;
        addedDriversList = new ArrayList<>();
        availableDriversList = new ArrayList<>();
        availableDriversList = this.dc.getAllDrivers();
        populateListView();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        populateChoiceBox();
    }

    public void populateChoiceBox() {
        cbAppointmentTime.getItems().add("10:00:00");
        cbAppointmentTime.getItems().add("11:00:00");
        cbAppointmentTime.getItems().add("12:00:00");
        cbAppointmentTime.getItems().add("13:00:00");
        cbAppointmentTime.getItems().add("14:00:00");
        cbAppointmentTime.getItems().add("15:00:00");
        cbAppointmentTime.getItems().add("16:00:00");
    }

    public void updateDriversLists() {
        lvAllDrivers.getItems().clear();
        lvAddedDrivers.getItems().clear();

        for (Driver driver : availableDriversList) {
            lvAllDrivers.getItems().add(driver.GetInfo());
        }
        for (Driver driver : addedDriversList) {
            lvAddedDrivers.getItems().add(driver.GetInfo());
        }
    }

    public void populateListView() {
        for (Driver driver : availableDriversList) {
            lvAllDrivers.getItems().add(driver.GetInfo());
        }
    }

    public void btnAddDriver() {
        int selectedIndex = lvAllDrivers.getSelectionModel().getSelectedIndex();
        addedDriversList.add(availableDriversList.get(selectedIndex));
        availableDriversList.remove(selectedIndex);
        updateDriversLists();
    }

    public void btnRemoveDriver() {
        int selectedIndex = lvAddedDrivers.getSelectionModel().getSelectedIndex();
        availableDriversList.add(addedDriversList.get(selectedIndex));
        addedDriversList.remove(selectedIndex);
        updateDriversLists();
    }

    public void createAppointmentButtonClick() throws SQLException {
        if (dpAppointmentDate.getValue() != null && cbAppointmentTime.getValue() != null) {
            ac.AddAppointmentToDB(dpAppointmentDate.getValue().getDayOfMonth(),
                    dpAppointmentDate.getValue().getMonthValue(),
                    dpAppointmentDate.getValue().getYear(),
                    LocalTime.parse(cbAppointmentTime.getValue()),
                    addedDriversList);
            int maxId = ac.GetMaxAppointmentID();
            ac.addAppointment(
                    new Appointment(
                            dpAppointmentDate.getValue().getDayOfMonth(),
                            dpAppointmentDate.getValue().getMonthValue(),
                            dpAppointmentDate.getValue().getYear(),
                            LocalTime.parse(cbAppointmentTime.getValue()),
                            addedDriversList,
                            maxId
                    )
            );
            Appointment appointment = ac.getLastAddedAppointment();
            ac.UpdateDB(appointment);
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Please fill in all the information!");
            alert.showAndWait();
        }
    }

    public void resetForm() {
        dpAppointmentDate.setValue(null);
        cbAppointmentTime.setValue(null);
        List<Driver> standInList = new ArrayList<>();
        for (Driver driver : addedDriversList) {
            standInList.add(driver);
        }
        for (Driver driver : standInList) {
            addedDriversList.remove(driver);
            availableDriversList.add(driver);
        }
        updateDriversLists();
    }

    public void buttonCancelClick(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("screens/main.fxml"));
        Parent root = fxmlLoader.load();
        Scene createFormScene = new Scene(root);

        MainFormController mfc = fxmlLoader.getController();
        mfc.initData(dc, ac);

        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(createFormScene);
        window.show();
    }

    public void buttonCreateDriverClick(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("screens/driver.fxml"));
        Parent root = fxmlLoader.load();
        Scene createFormScene = new Scene(root);

        AddDriverFormController adfc = fxmlLoader.getController();
        adfc.initData(dc, ac);

        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(createFormScene);
        window.show();
    }
}
