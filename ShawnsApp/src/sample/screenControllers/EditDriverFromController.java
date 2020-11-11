package sample.screenControllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import sample.controllers.AppointmentController;
import sample.controllers.DriverController;
import sample.models.Driver;
import sample.models.Helper;

import java.awt.*;
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

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.helper = new Helper();
    }

    public void initData(DriverController dc, AppointmentController ac, Driver d) {
        this.forEditing = d;
        this.dc = dc;
        this.ac = ac;

        tbDriverName.setText(this.forEditing.getName());
        tbLicensePlate.setText(this.forEditing.getLicencePlate());
        tbPhoneNumber.setText(this.forEditing.getPhoneNumber());
    }

    public void btnDoneClick(ActionEvent event) throws IOException {
        if(tbDriverName.getText() != this.forEditing.getName()){
            dc.ChangeName(tbDriverName.getText(), this.forEditing.getId());
        }
        if(tbPhoneNumber.getText() != this.forEditing.getPhoneNumber()){
            dc.ChangeNumber(tbPhoneNumber.getText(), this.forEditing.getId());
        }
        if(tbLicensePlate.getText() != this.forEditing.getLicencePlate()){
            dc.ChangeLicense(tbLicensePlate.getText(), this.forEditing.getId());
        }

        Scene scene = helper.createScene("create");
        CreateFormController cfc = helper.getFxmlLoader().getController();
        cfc.initData(dc,ac);
        helper.showScene(scene, event);
    }

    public void btnCancelClick(ActionEvent event) throws IOException {
        Scene scene = helper.createScene("create");
        CreateFormController cfc = helper.getFxmlLoader().getController();
        cfc.initData(dc, ac);
        helper.showScene(scene, event);
    }
}
