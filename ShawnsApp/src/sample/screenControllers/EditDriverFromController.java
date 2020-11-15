package sample.screenControllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import sample.controllers.AppointmentController;
import sample.controllers.DriverController;
import sample.models.Appointment;
import sample.models.Driver;
import sample.Helper;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class EditDriverFromController implements Initializable {

    @FXML TextField tbDriverName;
    @FXML TextField tbPhoneNumber;
    @FXML TextField  tbLicensePlate;

    private Driver forEditing;
    private DriverController dc;
    private AppointmentController ac;
    private Helper helper;
    private Appointment appointment;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.helper = new Helper();
    }

    public void initData(DriverController dc, AppointmentController ac, Driver d, Appointment appointment) {
        this.forEditing = d;
        this.dc = dc;
        this.ac = ac;
        this.appointment = appointment;

        tbDriverName.setText(this.forEditing.getName());
        tbLicensePlate.setText(this.forEditing.getLicencePlate());
        tbPhoneNumber.setText(this.forEditing.getPhoneNumber());
    }

    public void btnDoneClick(ActionEvent event) throws IOException {
        if(tbDriverName.getText() != this.forEditing.getName()){
            dc.changeName(tbDriverName.getText(), this.forEditing.getId());
        }
        if(tbPhoneNumber.getText() != this.forEditing.getPhoneNumber()){
            dc.changeNumber(tbPhoneNumber.getText(), this.forEditing.getId());
        }
        if(tbLicensePlate.getText() != this.forEditing.getLicencePlate()){
            dc.changeLicense(tbLicensePlate.getText(), this.forEditing.getId());
        }
        determineForm(event, this.appointment);
    }

    public void btnCancelClick(ActionEvent event) throws IOException {
        determineForm(event, this.appointment);
    }

    public void determineForm(ActionEvent event, Appointment app) throws IOException {

        if(app == null){
            Scene scene = helper.createScene("create");
            CreateFormController cfc = helper.getFxmlLoader().getController();
            cfc.initData(dc, ac);
            helper.showScene(scene, event);
        }
        else {
            Scene scene = helper.createScene("editAppointment");
            EditAppointmentController cfc = helper.getFxmlLoader().getController();
            cfc.initData(dc, ac, app);
            helper.showScene(scene, event);
        }
    }
}
