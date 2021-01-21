package sample.screenControllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
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
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

public class CreateFormController implements Initializable {
    @FXML private DatePicker dpAppointmentDate;
    @FXML private ChoiceBox<String> cbAppointmentTime;

    @FXML private TableView<Driver> tvAllDrivers;
    @FXML private TableColumn<Driver, String> tcNameAll;
    @FXML private TableView<Driver> tvAddedDrivers;
    @FXML private TableColumn<Driver, String> tcNameAdded;

    @FXML private TextField tbSearch;
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
        tvAllDrivers.setItems(populateTableView());
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        populateChoiceBox();
        tvAllDrivers.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tcNameAll.setCellValueFactory(new PropertyValueFactory<Driver, String>("name"));

        tvAddedDrivers.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tcNameAdded.setCellValueFactory(new PropertyValueFactory<Driver, String>("name"));
    }

    public ObservableList<Driver> populateTableView() {
        ObservableList<Driver> drivers = FXCollections.observableArrayList();
        for (Driver d: dc.getAllDrivers()) {
            drivers.add(d);
        }
        return drivers;
    }

    public void populateChoiceBox() {
        for (int i = 10; i <18 ; i++) {
            cbAppointmentTime.getItems().add(+i+":00");
        }
    }

//
    public void updateTables(){
        tvAllDrivers.getItems().clear();
        tvAddedDrivers.getItems().clear();

        ObservableList<Driver> availableDrivers = FXCollections.observableArrayList();
        for (Driver d: availableDriversList) {
            availableDrivers.add(d);
        }
        tvAllDrivers.setItems(availableDrivers);

        tvAddedDrivers.getItems().clear();
        ObservableList<Driver> chosenDrivers = FXCollections.observableArrayList();
        for (Driver d: addedDriversList) {
            chosenDrivers.add(d);
        }
        tvAddedDrivers.setItems(chosenDrivers);
    }

    public void btnAddDriver() {

        Driver d = tvAllDrivers.getSelectionModel().getSelectedItem();
        if(d != null){
            addedDriversList.add(d);
            availableDriversList.remove(d);
            updateTables();
        }
        else{
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Driver not selected!");
            alert.setContentText("Please select a driver from the list on top!");
            alert.showAndWait();
        }
    }

    public void btnRemoveDriver() {

        Driver d = tvAddedDrivers.getSelectionModel().getSelectedItem();
        if(d != null){
            addedDriversList.remove(d);
            availableDriversList.add(d);
            updateTables();
        }
        else{
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
            alert.setHeaderText("Time is not selected or is incorrectly inputted!");
            alert.setContentText("Please select a timeslot from the selection box or type in a correct time!");
            alert.showAndWait();
        }
        else if(dpAppointmentDate.getValue() == null){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Date is not selected!");
            alert.setContentText("Please select a date from the calendar!");
            alert.showAndWait();
        }
        else if(!dpAppointmentDate.getValue().isAfter(LocalDate.now())){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Selected date should be after todays date!");
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
        Driver d = tvAllDrivers.getSelectionModel().getSelectedItem();
        if(d != null){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Driver");
            alert.setHeaderText("Are you sure you want to delete this driver?");
            Optional<ButtonType> res = alert.showAndWait();
                if(res.get() == ButtonType.OK){
                    dc.deleteDriver(d.getId());
                    availableDriversList.remove(d);
                    updateTables();
                }
        }
        else{
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
        Driver d=tvAllDrivers.getSelectionModel().getSelectedItem();
        if(d != null) {
            Scene scene = helper.createScene("editDriver");
            EditDriverFromController cfc = helper.getFxmlLoader().getController();
            cfc.initData(dc, ac, d, null);
            helper.showSceneMouse(scene, event);
        }
        else{
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Please select a driver!");
            alert.showAndWait();
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
        updateTables();
    }

    public void filterByName(KeyEvent keyEvent) {
        tvAllDrivers.getItems().clear();
        String str = tbSearch.getText().toLowerCase();
        ObservableList<Driver> allDrivers = FXCollections.observableArrayList();
        ObservableList<Driver> filteredDrivers = FXCollections.observableArrayList();

        for (Driver d: availableDriversList) {
            allDrivers.add(d);
        }

        //Filter
        if(str==""){
            tvAllDrivers.setItems(allDrivers);
            availableDriversList = dc.getAllDrivers();
        }
        else{
            for (Driver d:allDrivers) {
                if(d.getName().toLowerCase().contains(str)){
                    filteredDrivers.add(d);
                }
            }
            tvAllDrivers.setItems(filteredDrivers);

        }
    }

    public void timeManual(KeyEvent keyEvent) {
        cbAppointmentTime.getSelectionModel().clearSelection();
    }
}
