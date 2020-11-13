package sample.screenControllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Paint;
import sample.controllers.AppointmentController;
import sample.models.Driver;
import sample.controllers.DriverController;
import sample.models.Helper;

import java.awt.*;
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
    @FXML private TextField tbTime;
    @FXML private Label lblTime;


    private DriverController dc;
    private AppointmentController ac;

    private List<Driver> availableDriversList;
    private List<Driver> addedDriversList;

    private FXMLLoader fxmlLoader;
    private Helper fxmlHelper;


    public void initData(DriverController dc, AppointmentController ac) {
        this.dc = dc;
        this.ac = ac;
        fxmlLoader = new FXMLLoader();
        fxmlHelper = new Helper();
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

        LocalTime time = null;
        if(cbAppointmentTime.getValue() == null){
            if(!tbTime.getText().isEmpty())
                if(fxmlHelper.REGEXTime(tbTime.getText()))
                    time = LocalTime.parse(tbTime.getText()+":00");
        }
        else{
            time = LocalTime.parse(cbAppointmentTime.getValue()+":00");
        }

        if(time == null){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Time is not selected/incorrect!");
            alert.setContentText("Please select a timeslot from the selection box or type in a correct time!");
            alert.showAndWait();
        }
        else if(dpAppointmentDate.getValue() == null){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Date is not selected!");
            alert.setContentText("Please select a date from the calendar!");
            alert.showAndWait();
        }
        else if(addedDriversList.size() == 0){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("No drivers for the current appointment");
            alert.setContentText("Please add at least one driver to the appointment!");
            alert.showAndWait();
        }
        else {
            ac.createAppointment(dpAppointmentDate.getValue().getDayOfMonth(),
                    dpAppointmentDate.getValue().getMonthValue(),
                    dpAppointmentDate.getValue().getYear(),
                    time,
                    addedDriversList);
            Scene scene = fxmlHelper.createScene("view");
            ViewFormController cfc = fxmlHelper.getFxmlLoader().getController();
            cfc.initData(dc, ac);
            fxmlHelper.showScene(scene, event);
        }
        resetForm();
    }

    public void resetForm() {
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

    public void buttonCreateDriverClick(MouseEvent event) throws IOException {
        Scene scene = fxmlHelper.createScene("driver");
        AddDriverFormController cfc = fxmlHelper.getFxmlLoader().getController();
        cfc.initData(dc, ac);
        fxmlHelper.showSceneMouse(scene, event);
    }

    public void goBack(MouseEvent event) throws IOException {
        Scene scene = fxmlHelper.createScene("main");
        MainFormController cfc = fxmlHelper.getFxmlLoader().getController();
        cfc.initData(dc, ac);
        fxmlHelper.showSceneMouse(scene, event);
    }

    public void EditDriver(MouseEvent event) throws IOException {
        Driver d = null;
        try{
            d = availableDriversList.get(lvAllDrivers.getSelectionModel().getSelectedIndex());
            Scene scene = fxmlHelper.createScene("editDriver");
            EditDriverFromController cfc = fxmlHelper.getFxmlLoader().getController();
            cfc.initData(dc, ac, d);
            fxmlHelper.showSceneMouse(scene, event);
        }
        catch(IndexOutOfBoundsException ex){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Please select a driver!");
            alert.showAndWait();
        }
    }

    public void timeManual(KeyEvent keyEvent) {
        cbAppointmentTime.getSelectionModel().clearSelection();
        lblTime.visibleProperty().setValue(true);
        if(fxmlHelper.REGEXTime(tbTime.getText())){
            lblTime.setTextFill(Paint.valueOf("#32CD32"));
        }
        else{
            lblTime.setTextFill(Paint.valueOf("#FF0000"));
        }
    }

    public void SelectTime(MouseEvent mouseEvent) {
        tbTime.setText("");
        lblTime.visibleProperty().setValue(false);
    }
}
