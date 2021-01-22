package sample.screenControllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Paint;
import sample.models.Appointment;
import sample.controllers.AppointmentController;
import sample.models.Driver;
import sample.controllers.DriverController;
import sample.Helper;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import org.joda.time.LocalDate;

public class EditAppointmentController implements Initializable {

    @FXML private DatePicker dpAppointmentDate;
    @FXML private ChoiceBox<String> cbAppointmentTime;


    @FXML private Label lblTime;
    @FXML private TextField tbTime;
    @FXML private TextField tbSearch;

    @FXML private TableView<Driver> tvAllDrivers;
    @FXML private TableColumn<Driver, String> tcNameAll;
    @FXML private TableView<Driver> tvAddedDrivers;
    @FXML private TableColumn<Driver, String> tcNameAdded;


    private DriverController dc;
    private AppointmentController ac;
    private Appointment current;
    private Helper helper;

    private List<Driver> availableDriversList;
    private List<Driver> addedDriversList;


    public void initData(DriverController dc, AppointmentController ac, Appointment app) {
        this.dc = dc;
        this.ac = ac;
        //this.current = new Appointment(app.getId(), app.getDate(), app.getTime(), app.getDriverList());
        this.current = app;

        addedDriversList = new ArrayList<>();
        availableDriversList = new ArrayList<>();

        for (Driver d :dc.getAllDrivers()) {
            if(app.getDriverList().stream().anyMatch(o -> o.getId() == (d.getId()))){
                addedDriversList.add(d);
            }
            else if(!app.getDriverList().stream().anyMatch(o -> o.getId() == (d.getId()))){
                availableDriversList.add(d);
            }
        }
        fillInInfo();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        populateChoiceBox();
        this.helper = new Helper();
        tcNameAll.setCellValueFactory(new PropertyValueFactory<Driver, String>("name"));
        tcNameAdded.setCellValueFactory(new PropertyValueFactory<Driver, String>("name"));
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

    public void editAppointmentButtonClick(ActionEvent event) throws IOException {
        LocalTime time = null;
        LocalDate date = new LocalDate(dpAppointmentDate.getValue().getYear(), dpAppointmentDate.getValue().getMonthValue(), dpAppointmentDate.getValue().getDayOfMonth());


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
        else if(addedDriversList.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Appointment needs to have at least one driver!");
            alert.setContentText("Please add somebody from the list on top!");
            alert.showAndWait();
        }
        else{
            if(this.current.getTime() != time && time != null){
                ac.changeTime(time, this.current.getId());
            }

            if(this.current.getDate().compareTo(date) != 0) {
                ac.changeDate(dpAppointmentDate.getValue().getDayOfMonth(),
                        dpAppointmentDate.getValue().getMonthValue(),
                        dpAppointmentDate.getValue().getYear(), this.current.getId());
            }

            if(!this.current.getDriverList().equals(this.addedDriversList)){
                ac.changeDrivers(this.addedDriversList, this.current);
            }
            Scene scene = helper.createScene("view");
            ViewFormController cfc = helper.getFxmlLoader().getController();
            cfc.initData(dc, ac);
            helper.showScene(scene,event );
        }
    }

    public void buttonCancelClick(ActionEvent event) throws IOException, SQLException {
        Scene scene = helper.createScene("view");
        ViewFormController cfc = helper.getFxmlLoader().getController();
        cfc.initData(dc, ac);
        helper.showScene(scene, event);
    }

    public void buttonCreateDriverClick(MouseEvent event) throws IOException {
        Scene scene = helper.createScene("driver");
        AddDriverFormController cfc = helper.getFxmlLoader().getController();
        cfc.initData(dc, ac, this.current);
        helper.showSceneMouse(scene, event);
    }

    public void editDriver(MouseEvent event) throws IOException {
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

    //////////////////////
    //UI control methods//
    //////////////////////

    public void SelectTime(MouseEvent mouseEvent) {
        tbTime.setText("");
    }

    public void fillInInfo(){
        updateTables();
        setDate();
        setTime();
    }

    public void populateChoiceBox() {
        for (int i = 10; i <18 ; i++) {
            cbAppointmentTime.getItems().add(+i+":00");
        }
    }
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

    public void setDate(){
        DateTimeFormatter formatter = null;
        String formatString = "";
        if(this.current.getDate().getDayOfMonth() <=9){
            formatString+="d-";
        }
        else{
            formatString+="dd-";
        }
        if(this.current.getDate().getMonthOfYear() <= 9){

            formatString+="M-";
        }
        else{
            formatString+="MM-";
        }
        formatString+="yyyy";
        formatter = DateTimeFormatter.ofPattern(formatString);

        String info =
                + this.current.getDate().getDayOfMonth() + "-"
                        + this.current.getDate().getMonthOfYear() + "-"
                        + this.current.getDate().getYear();
        dpAppointmentDate.setValue(java.time.LocalDate.parse(info,formatter));
    }

    public void setTime(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        if(cbAppointmentTime.getItems().stream().anyMatch(o -> o.equals(this.current.getTime().format(formatter)))){
            cbAppointmentTime.setValue(this.current.getTime().format(formatter));
        }
        else{
            tbTime.setText(this.current.getTime().format(formatter));
        }
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
