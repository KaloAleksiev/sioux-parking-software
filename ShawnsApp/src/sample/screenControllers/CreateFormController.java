package sample.screenControllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import sample.controllers.AppointmentController;
import sample.datasources.DataBase;
import sample.models.Driver;
import sample.controllers.DriverController;
import sample.models.FXMLhelper;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
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

    private FXMLLoader fxmlLoader;
    private FXMLhelper fxmlHelper;


    public void initData(DriverController dc, AppointmentController ac) {
        this.dc = dc;
        this.ac = ac;
        fxmlLoader = new FXMLLoader();
        fxmlHelper = new FXMLhelper();
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
        cbAppointmentTime.getItems().add("10:00");
        cbAppointmentTime.getItems().add("11:00");
        cbAppointmentTime.getItems().add("12:00");
        cbAppointmentTime.getItems().add("13:00");
        cbAppointmentTime.getItems().add("14:00");
        cbAppointmentTime.getItems().add("15:00");
        cbAppointmentTime.getItems().add("16:00");
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

    public void createAppointmentButtonClick(ActionEvent event) throws SQLException, IOException {
        if (dpAppointmentDate.getValue() != null && cbAppointmentTime.getValue() != null) {
            ac.createAppointment(dpAppointmentDate.getValue().getDayOfMonth(),
                    dpAppointmentDate.getValue().getMonthValue(),
                    dpAppointmentDate.getValue().getYear(),
                    LocalTime.parse(cbAppointmentTime.getValue()+":00"),
                    addedDriversList);
            Scene scene = fxmlHelper.createScene("view");
            ViewFormController cfc = fxmlHelper.getFxmlLoader().getController();
            cfc.initData(dc, ac);
            fxmlHelper.showScene(scene, event);
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Please fill in all the information!");
            alert.showAndWait();
        }
        resetForm();
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
        Scene scene = fxmlHelper.createScene("main");
        MainFormController cfc = fxmlHelper.getFxmlLoader().getController();
        cfc.initData(dc, ac);
        fxmlHelper.showScene(scene, event);
    }

    public void buttonCreateDriverClick(ActionEvent event) throws IOException {
        Scene scene = fxmlHelper.createScene("driver");
        AddDriverFormController cfc = fxmlHelper.getFxmlLoader().getController();
        cfc.initData(dc, ac);
        fxmlHelper.showScene(scene, event);
    }

    public void goBack(MouseEvent event) throws IOException {
        Scene scene = fxmlHelper.createScene("main");
        MainFormController cfc = fxmlHelper.getFxmlLoader().getController();
        cfc.initData(dc, ac);
        fxmlHelper.showSceneMouse(scene, event);
    }
}
