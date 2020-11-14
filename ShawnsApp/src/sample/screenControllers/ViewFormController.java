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
import sample.controllers.AppointmentController;
import sample.controllers.DriverController;
import sample.models.*;

import java.io.IOException;
import java.net.URL;
import java.util.Calendar;
import java.util.Optional;
import java.util.ResourceBundle;

public class ViewFormController implements Initializable {
    @FXML private TableView<ShowcaseAppointment> tvAppointments;
    @FXML private TableColumn<ShowcaseAppointment, String> tcDate;
    @FXML private TableColumn<ShowcaseAppointment, String> tcTime;
    @FXML private TableColumn<ShowcaseAppointment, String> tcDriverNames;
    @FXML private DatePicker dpViewApp;
    @FXML private TextField tbSearchName;

    private DriverController dc;
    private AppointmentController ac;
    private Helper fxmlHelper;

    public void initData(DriverController dc, AppointmentController ac) {
        this.dc = dc;
        this.ac = ac;
        this.fxmlHelper = new Helper();
        tvAppointments.setItems(populateTableView());
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tcDate.setCellValueFactory(new PropertyValueFactory<ShowcaseAppointment, String>("date"));
        tcTime.setCellValueFactory(new PropertyValueFactory<ShowcaseAppointment, String>("time"));
        tcDriverNames.setCellValueFactory(new PropertyValueFactory<ShowcaseAppointment, String>("names"));
    }

    public ObservableList<ShowcaseAppointment> populateTableView() {
        ObservableList<ShowcaseAppointment> appointments = FXCollections.observableArrayList();
        for (Appointment apt: ac.getAllAppointments()) {
            appointments.add(apt.getShowcaseAppointment());
        }
        return appointments;
    }

    public void editAppointmentsClick(ActionEvent event) throws IOException {
        try{
            TableView.TableViewSelectionModel<ShowcaseAppointment> showApp = tvAppointments.getSelectionModel();
            Appointment app = ac.getAppointmentById(showApp.getSelectedItem().GetId());
            Scene scene = fxmlHelper.createScene("editAppointment");
            EditAppointmentController cfc = fxmlHelper.getFxmlLoader().getController();
            cfc.initData(dc, ac, app);
            fxmlHelper.showScene(scene, event);
        }
        catch(NullPointerException ex){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Please select an appointment!");
            alert.showAndWait();
        }
    }

    public void deleteAppointmentClick(ActionEvent event) {
        try{
            TableView.TableViewSelectionModel<ShowcaseAppointment> showApp = tvAppointments.getSelectionModel();
            Appointment app = ac.getAppointmentById(showApp.getSelectedItem().GetId());

            //Create the alert
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Appointment");
            alert.setHeaderText("Are you sure you want to delete this appointment?");
            alert.setContentText("All data would be lost!");
            Optional<ButtonType> res = alert.showAndWait();

            //get the result from the appointment
            if(res.get() == ButtonType.OK){
                ac.deleteAppointment(app);
                tvAppointments.getItems().remove(showApp);
                tvAppointments.setItems(populateTableView());
            }
        }
        catch(NullPointerException ex){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Please select an appointment!");
            alert.showAndWait();
        }
    }

    public void buttonCancelClick(ActionEvent event) throws IOException {
        Scene scene = fxmlHelper.createScene("main");
        MainFormController cfc = fxmlHelper.getFxmlLoader().getController();
        cfc.initData(dc, ac);
        fxmlHelper.showScene(scene, event);
    }

    public void SearchByName() {
        tvAppointments.getItems().clear();
        String str = tbSearchName.getText().toLowerCase();
        ObservableList<ShowcaseAppointment> allAppointments = FXCollections.observableArrayList();
        ObservableList<ShowcaseAppointment> filteredAppointments = FXCollections.observableArrayList();
        //GetAllAppointments
        for (Appointment apt: ac.getAllAppointments()) {
            allAppointments.add(apt.getShowcaseAppointment());
        }
        //Filter
        if(str == ""){
            tvAppointments.setItems(allAppointments);
        }else{
            for (ShowcaseAppointment a:allAppointments) {
                if(a.appointment.getDriverList().stream().anyMatch(d -> d.getName().toLowerCase().startsWith(str))){
                    filteredAppointments.add(a);
                }
            }
            tvAppointments.setItems(filteredAppointments);
        }
    }
    public void filterByDate() {
        ObservableList<ShowcaseAppointment> allAppointments = FXCollections.observableArrayList();
        ObservableList<ShowcaseAppointment> appointments = FXCollections.observableArrayList();
        //GetAllAppointments
        for (Appointment apt: ac.getAllAppointments()) {
            allAppointments.add(apt.getShowcaseAppointment());
        }
            //Filter
        if(dpViewApp.getValue() == null){
            tvAppointments.setItems(allAppointments);
        }
        else{
            Calendar date = Calendar.getInstance();
            date.clear();
            date.set(dpViewApp.getValue().getYear(), dpViewApp.getValue().getMonthValue(), dpViewApp.getValue().getDayOfMonth());

            for (ShowcaseAppointment a:allAppointments) {
                String date1= fxmlHelper.GetDateAsString(a.appointment.getDate());
                String date2= fxmlHelper.GetDateAsString(date);
                if(date1.equals(date2) ){
                    appointments.add(a);
                }
            }
            tvAppointments.setItems(appointments);
        }
    }
    public void goBack(MouseEvent event) throws IOException {
        Scene scene = fxmlHelper.createScene("main");
        MainFormController cfc = fxmlHelper.getFxmlLoader().getController();
        cfc.initData(dc, ac);
        fxmlHelper.showSceneMouse(scene, event);
    }

    public void resetFilters(MouseEvent mouseEvent) {
        dpViewApp.setValue(null);
        tbSearchName.setText("");
        SearchByName();
    }
}
