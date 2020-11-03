package sample.screenControllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import sample.models.Appointment;
import sample.controllers.AppointmentController;
import sample.models.Driver;
import sample.controllers.DriverController;
import sample.models.FXMLhelper;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.ResourceBundle;

public class EditAppointmentController implements Initializable {


    @FXML private DatePicker dpAppointmentDate;
    @FXML private ChoiceBox<String> cbAppointmentTime;
    @FXML private ListView<String> lvAllDrivers;
    @FXML private ListView<String> lvAddedDrivers;

    private DriverController dc;
    private AppointmentController ac;
    private Appointment current;
    private FXMLhelper fxmlHelper;

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
        this.fxmlHelper = new FXMLhelper();
    }

    public void fillInInfo(){
        updateDriversLists();
        SetDate();
        SetTime();
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
    public void SetDate(){
        DateTimeFormatter formatter = null;
        String formatString = "";
        if(this.current.getDate().get(Calendar.DAY_OF_MONTH) < 9){
            formatString+="d-";
        }
        else{
            formatString+="dd-";
        }
        int tashak = this.current.getDate().get(Calendar.MONTH);
        if(this.current.getDate().get(Calendar.MONTH) < 9){

            formatString+="M-";
        }
        else{
            formatString+="MM-";
        }
        formatString+="yyyy";
        formatter = DateTimeFormatter.ofPattern(formatString);

        String info =
                + this.current.getDate().get(Calendar.DAY_OF_MONTH) + "-"
                + this.current.getDate().get(Calendar.MONTH) + "-"
                + this.current.getDate().get(Calendar.YEAR);
        LocalDate localDate = LocalDate.parse(info, formatter);
        dpAppointmentDate.setValue(localDate);
    }

    public void SetTime(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        cbAppointmentTime.setValue(this.current.getTime().format(formatter));

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

    public void editAppointmentButtonClick() throws SQLException {

        if(!this.current.getDriverList().equals(this.addedDriversList)){
            ac.ChangeDrivers(this.addedDriversList, this.current);
        }

        if(this.current.getTime() != LocalTime.parse(cbAppointmentTime.getValue())){
            ac.changeTime(LocalTime.parse(cbAppointmentTime.getValue()), this.current.getId());
        }

        Calendar date = Calendar.getInstance();
        date.set(dpAppointmentDate.getValue().getYear(), dpAppointmentDate.getValue().getMonthValue(), dpAppointmentDate.getValue().getDayOfMonth());
        if(this.current.getDate().compareTo(date) != 0) {
            ac.changeDate(dpAppointmentDate.getValue().getDayOfMonth(),
                    dpAppointmentDate.getValue().getMonthValue(),
                    dpAppointmentDate.getValue().getYear(), this.current.getId());
        }
//        } else {
//            Alert alert = new Alert(Alert.AlertType.ERROR);
//            alert.setHeaderText("Please fill in all the information!");
//            alert.showAndWait();
//        }
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

    public void buttonCancelClick(ActionEvent event) throws IOException, SQLException {
        Scene scene = fxmlHelper.createScene("view");
        ViewFormController cfc = fxmlHelper.getFxmlLoader().getController();
        cfc.initData(dc, ac);
        fxmlHelper.showScene(scene, event);
    }

    public void buttonCreateDriverClick(ActionEvent event) throws IOException {
        Scene scene = fxmlHelper.createScene("driver");
        AddDriverFormController cfc = fxmlHelper.getFxmlLoader().getController();
        cfc.initData(dc, ac);
        fxmlHelper.showScene(scene, event);
    }
}
