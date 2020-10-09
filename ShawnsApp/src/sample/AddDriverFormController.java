package sample;

import sample.classes.*;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sample.classes.DriverController;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AddDriverFormController implements Initializable{
    @FXML private TextField tbDriverName;
    @FXML private TextField tbPhoneNumber;
    @FXML private TextField tbLicensePlate;

    DriverController dc;
    AppointmentController ac;

    public void initData(DriverController dc, AppointmentController ac) {
        this.dc = dc;
        this.ac = ac;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) { }

    public void btnDoneClick() {
        if (tbLicensePlate.getText() != "" && tbPhoneNumber.getText() != "" && tbDriverName.getText() != "" ) {
            dc.addDriver(
                    new Driver(
                            tbLicensePlate.getText(),
                            Integer.parseInt(tbPhoneNumber.getText()),
                            tbDriverName.getText()
                    )
            );
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Successfully created a driver!");
            alert.showAndWait();
            resetForm();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Please fill in all the information!");
            alert.showAndWait();
        }
    }

    public void btnCancelClick(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("screens/create.fxml"));
        Parent root = fxmlLoader.load();
        Scene createFormScene = new Scene(root);

        CreateFormController cfc = fxmlLoader.getController();
        cfc.initData(dc, ac);

        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(createFormScene);
        window.show();
    }

    public void resetForm() {
        tbDriverName.clear();
        tbLicensePlate.clear();
        tbPhoneNumber.clear();
    }
}
