package sample;

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
import javafx.stage.Stage;
import sample.classes.*;

import java.io.IOException;
import java.net.URL;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ViewFormController implements Initializable {
    @FXML private TableView<ShowcaseAppointment> tvAppointments;
    @FXML private TableColumn<ShowcaseAppointment, String> tcDate;
    @FXML private TableColumn<ShowcaseAppointment, String> tcTime;
    @FXML private TableColumn<ShowcaseAppointment, String> tcDriverNames;

    DriverController dc;
    AppointmentController ac;

    public void initData(DriverController dc, AppointmentController ac) {
        this.dc = dc;
        this.ac = ac;
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
}
