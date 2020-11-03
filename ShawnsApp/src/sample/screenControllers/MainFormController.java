package sample.screenControllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.scene.Scene;
import sample.controllers.AppointmentController;
import sample.controllers.DriverController;
import sample.models.FXMLhelper;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public  class MainFormController implements Initializable {
    DriverController dc;
    AppointmentController ac;
    FXMLhelper fxmlHelper;

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
        fxmlHelper = new FXMLhelper();
    }

    public void openCreateFormButtonClick(ActionEvent event) throws IOException {
        Scene scene = fxmlHelper.createScene("create");
        CreateFormController cfc = fxmlHelper.getFxmlLoader().getController();
        cfc.initData(dc, ac);
        fxmlHelper.showScene(scene, event);
    }

    public void openViewFormButtonClick(ActionEvent event) throws IOException {
        Scene scene = fxmlHelper.createScene("view");
        ViewFormController vfc = fxmlHelper.getFxmlLoader().getController();
        vfc.initData(dc, ac);
        fxmlHelper.showScene(scene, event);
    }
}
