package sample.screenControllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import sample.controllers.AppointmentController;
import sample.controllers.DriverController;
import sample.models.*;
import sample.screenControllers.EditAppointmentController;
import sample.screenControllers.MainFormController;

import java.io.IOException;
import java.net.URL;
import java.util.Calendar;
import java.util.ResourceBundle;

public class ViewFormController implements Initializable {
    @FXML private TableView<ShowcaseAppointment> tvAppointments;
    @FXML private TableColumn<ShowcaseAppointment, String> tcDate;
    @FXML private TableColumn<ShowcaseAppointment, String> tcTime;
    @FXML private TableColumn<ShowcaseAppointment, String> tcDriverNames;
    @FXML private DatePicker dpViewApp;
    @FXML private TextField tbSearchName;
    @FXML private Button backBt;

    private DriverController dc;
    private AppointmentController ac;
    private FXMLhelper fxmlHelper;

    public void initData(DriverController dc, AppointmentController ac) {
        this.dc = dc;
        this.ac = ac;
        this.fxmlHelper = new FXMLhelper();
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
        TableView.TableViewSelectionModel<ShowcaseAppointment> showApp = tvAppointments.getSelectionModel();
        Appointment app = ac.getAppointmentById(showApp.getSelectedItem().GetId());
        Scene scene = fxmlHelper.createScene("editAppointment");
        EditAppointmentController cfc = fxmlHelper.getFxmlLoader().getController();
        cfc.initData(dc, ac, app);
        fxmlHelper.showScene(scene, event);
    }

    public void buttonCancelClick(ActionEvent event) throws IOException {
        Scene scene = fxmlHelper.createScene("main");
        MainFormController cfc = fxmlHelper.getFxmlLoader().getController();
        cfc.initData(dc, ac);
        fxmlHelper.showScene(scene, event);
    }
    

    public void SearchByName(KeyEvent keyEvent) {
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
    public void filterByDate(ActionEvent event) {
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
                date.set(dpViewApp.getValue().getYear(), dpViewApp.getValue().getMonthValue(), dpViewApp.getValue().getDayOfMonth());
                for (ShowcaseAppointment a:allAppointments) {
                    if(a.appointment.getDate().compareTo(date) == 0){
                        appointments.add(a);
                    }
                }
                tvAppointments.setItems(appointments);
            }
        }



    public void goBack(ActionEvent event) throws IOException {
        Scene scene = fxmlHelper.createScene("main");
        MainFormController cfc = fxmlHelper.getFxmlLoader().getController();
        cfc.initData(dc, ac);
        fxmlHelper.showScene(scene, event);
    }


    public void resetFilter(ActionEvent event) {
        dpViewApp.setValue(null);
        tbSearchName.setText("");

    }
}
