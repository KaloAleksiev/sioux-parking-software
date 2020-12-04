package sample.screenControllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
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
    @FXML private ListView<String> lvAllDrivers;
    @FXML private ListView<String> lvAddedDrivers;
    @FXML private Label lblTime;
    @FXML private TextField tbTime;

    private DriverController dc;
    private AppointmentController ac;
    private Appointment current;
    private Helper helper;

    private List<Driver> availableDriversList;
    private List<Driver> addedDriversList;


    public void initData(DriverController dc, AppointmentController ac, Appointment app) {
        this.dc = dc;
        this.ac = ac;
        this.current = new Appointment(app.getId(), app.getDate(), app.getTime(), app.getDriverList());

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

    public void editAppointmentButtonClick(ActionEvent event) throws SQLException, IOException {

        if(!this.current.getDriverList().equals(this.addedDriversList)){
            ac.changeDrivers(this.addedDriversList, this.current);
        }

        LocalTime time = null;
        if(cbAppointmentTime.getValue() == null){
            if(!tbTime.getText().isEmpty())
                if(helper.REGEXTime(tbTime.getText()))
                    time = LocalTime.parse(tbTime.getText()+":00");
        }
        else{
            time = LocalTime.parse(cbAppointmentTime.getValue()+":00");
        }

        if(this.current.getTime() != time && time != null){
            ac.changeTime(time, this.current.getId());
        }

        LocalDate date = new LocalDate(dpAppointmentDate.getValue().getYear(), dpAppointmentDate.getValue().getMonthValue(), dpAppointmentDate.getValue().getDayOfMonth());

        if(this.current.getDate().compareTo(date) != 0) {
            ac.changeDate(dpAppointmentDate.getValue().getDayOfMonth(),
                    dpAppointmentDate.getValue().getMonthValue(),
                    dpAppointmentDate.getValue().getYear(), this.current.getId());
        }
        Scene scene = helper.createScene("view");
        ViewFormController cfc = helper.getFxmlLoader().getController();
        cfc.initData(dc, ac);
        helper.showScene(scene,event );
    }

    public void buttonCancelClick(ActionEvent event) throws IOException, SQLException {
        Scene scene = helper.createScene("view");
        ViewFormController cfc = helper.getFxmlLoader().getController();
        cfc.initData(dc, ac);
        helper.showScene(scene, event);
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

    public void buttonCreateDriverClick(MouseEvent event) throws IOException {
        Scene scene = helper.createScene("driver");
        AddDriverFormController cfc = helper.getFxmlLoader().getController();
        cfc.initData(dc, ac, this.current);
        helper.showSceneMouse(scene, event);
    }

    public void editDriver(MouseEvent event) {
        Driver d = null;
        try{
            d = availableDriversList.get(lvAllDrivers.getSelectionModel().getSelectedIndex());
            Scene scene = helper.createScene("editDriver");
            EditDriverFromController cfc = helper.getFxmlLoader().getController();
            cfc.initData(dc, ac, d, this.current);
            helper.showSceneMouse(scene, event);
        }
        catch(IndexOutOfBoundsException | IOException ex){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Driver not selected!");
            alert.setContentText("Please select a driver from the list on top!");
            alert.showAndWait();
        }
    }

    public void buttonDeleteDriverClick(MouseEvent event) {
        Driver d = null;
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

    //////////////////////
    //UI control methods//
    //////////////////////

    public void SelectTime(MouseEvent mouseEvent) {
        tbTime.setText("");
        lblTime.visibleProperty().setValue(false);
    }

    public void fillInInfo(){
        updateDriversLists();
        setDate();
        setTime();
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


}
