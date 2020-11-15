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
import sample.Helper;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
    private Helper helper;


    public void initData(DriverController dc, AppointmentController ac) {
        this.dc = dc;
        this.ac = ac;
        fxmlLoader = new FXMLLoader();
        helper = new Helper();
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
        for (int i = 10; i <18 ; i++) {
            cbAppointmentTime.getItems().add(+i+":00");
        }
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
        try{
            int selectedIndex = lvAllDrivers.getSelectionModel().getSelectedIndex();
            addedDriversList.add(availableDriversList.get(selectedIndex));
            availableDriversList.remove(selectedIndex);
            updateDriversLists();
        }
        catch(IndexOutOfBoundsException ex){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Driver not selected!");
            alert.setContentText("Please select a driver from the list on top!");
            alert.showAndWait();
        }
    }

    public void btnRemoveDriver() {
        try{
            int selectedIndex = lvAddedDrivers.getSelectionModel().getSelectedIndex();
            availableDriversList.add(addedDriversList.get(selectedIndex));
            addedDriversList.remove(selectedIndex);
            updateDriversLists();
        }
        catch(IndexOutOfBoundsException ex){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Driver not selected!");
            alert.setContentText("Please select a driver from the list on the bottom!");
            alert.showAndWait();
        }
    }

    public void createAppointmentButtonClick(ActionEvent event) throws SQLException, IOException {

        LocalTime time = null;
        if(cbAppointmentTime.getValue() == null){
            if(!tbTime.getText().isEmpty())
                if(helper.REGEXTime(tbTime.getText()))
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
            Scene scene = helper.createScene("view");
            ViewFormController cfc = helper.getFxmlLoader().getController();
            cfc.initData(dc, ac);
            helper.showScene(scene, event);
        }
        resetForm();
    }

    public void buttonDeleteDriverClick(MouseEvent event) {
        Driver d;
        try{
            d = availableDriversList.get(lvAllDrivers.getSelectionModel().getSelectedIndex());

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Driver");
            alert.setHeaderText("Are you sure you want to delete this driver?");
            alert.setContentText("All data would be lost!");
            Optional<ButtonType> res = alert.showAndWait();

            //get the result from the appointment
            if(res.get() == ButtonType.OK){
                dc.deleteDriver(d.getId());
                availableDriversList.remove(d);
                updateDriversLists();
            }
        }
        catch(IndexOutOfBoundsException ex){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Driver not selected!");
            alert.setContentText("Please select a driver from the list on top!");
            alert.showAndWait();
        }
    }

    public void buttonCancelClick(ActionEvent event) throws IOException {
        Scene scene = helper.createScene("main");
        MainFormController cfc = helper.getFxmlLoader().getController();
        cfc.initData(dc, ac);
        helper.showScene(scene, event);
    }

    public void buttonCreateDriverClick(MouseEvent event) throws IOException {
        Scene scene = helper.createScene("driver");
        AddDriverFormController cfc = helper.getFxmlLoader().getController();
        cfc.initData(dc, ac, null);
        helper.showSceneMouse(scene, event);
    }

    public void goBack(MouseEvent event) throws IOException {
        Scene scene = helper.createScene("main");
        MainFormController cfc = helper.getFxmlLoader().getController();
        cfc.initData(dc, ac);
        helper.showSceneMouse(scene, event);
    }

    public void EditDriver(MouseEvent event) throws IOException {
        Driver d = null;
        try{
            d = availableDriversList.get(lvAllDrivers.getSelectionModel().getSelectedIndex());
            Scene scene = helper.createScene("editDriver");
            EditDriverFromController cfc = helper.getFxmlLoader().getController();
            cfc.initData(dc, ac, d, null);
            helper.showSceneMouse(scene, event);
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
        if(helper.REGEXTime(tbTime.getText())){
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
}
