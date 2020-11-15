package sample.screenControllers;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import sample.controllers.AppointmentController;
import sample.controllers.DriverController;
import sample.Helper;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public  class MainFormController implements Initializable {
    DriverController dc;
    AppointmentController ac;
    Helper helper;

    public MainFormController() throws SQLException {
        dc = new DriverController();
        ac = new AppointmentController();
    }

    public void initData(DriverController dc, AppointmentController ac) {
        this.dc = dc;
        this.ac = ac;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        helper = new Helper();
    }

    public void openCreateFormButtonClick(ActionEvent event) throws IOException {
        Scene scene = helper.createScene("create");
        CreateFormController cfc = helper.getFxmlLoader().getController();
        cfc.initData(dc, ac);
        helper.showScene(scene, event);

    }

    public void openViewFormButtonClick(ActionEvent event) throws IOException {
        Scene scene = helper.createScene("view");
        ViewFormController vfc = helper.getFxmlLoader().getController();
        vfc.initData(dc, ac);
        helper.showScene(scene, event);
    }
}
