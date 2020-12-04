package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.joda.time.LocalDate;
import sample.models.Appointment;
import sample.screenControllers.CreateFormController;
import sample.screenControllers.EditAppointmentController;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Helper {

    private FXMLLoader fxmlLoader;

    public FXMLLoader getFxmlLoader() {
        return fxmlLoader;
    }

    public Helper() {
        this.fxmlLoader = new FXMLLoader();
    }

    public Scene createScene(String screen) throws IOException {
        this.fxmlLoader.setLocation(getClass().getResource("screens/" + screen + ".fxml"));
        Parent root = this.fxmlLoader.load();
        Scene scene = new Scene(root);
        return  scene;
    }

    public void showScene(Scene scene, ActionEvent event){
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(scene);
        scene.getStylesheets().add
                (this.getClass().getResource("styles/styles.css").toExternalForm());
        window.show();
    }

    public String GetDateAsString(LocalDate date){
        String month = Integer.toString(date.getMonthOfYear());
        String day = Integer.toString(date.getDayOfMonth());
        String year = Integer.toString(date.getYear());
        if(date.dayOfMonth().get() < 10){
            day = "0"+ day;
        }
        if(date.monthOfYear().get() < 10){
            month = "0" + month;
        }
        String str = day+month+year;
        return str;
    }

    public void showSceneMouse(Scene scene, MouseEvent event){
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(scene);
        scene.getStylesheets().add
                (this.getClass().getResource("styles/styles.css").toExternalForm());
        window.show();
    }

    public boolean REGEXTime(String s){
        Pattern p = Pattern.compile("(?:^$)|(?:^([0-9]|0[1-9]|1[0-9]|2[0-4])(:|\\.)[0-5][0-9]$)|(?:^([1-9]|0[1-9]|1[0-2])(:|\\.)[0-5][0-9] (A|a|P|p)(M|m)$)");
        Matcher m = p.matcher(s);
        boolean b = m.matches();
        if(b == true){
            return true;
        }
        return false;
    }
}
