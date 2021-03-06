package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import org.joda.time.LocalDate;

import java.io.IOException;

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

    public boolean REGEXTime(String timestr){
        boolean corrFlag = true;
        if (timestr.length() == 5 && timestr.toCharArray()[0] != '-') {
            if (timestr.toCharArray()[2] != ':') {
                corrFlag = false;
            } else if (timestr.toCharArray().length != 5) {
                corrFlag = false;
            } else {
                int hh = 0;
                String s = "";
                s = timestr.toCharArray()[0]+""+timestr.toCharArray()[1];
                try {
                    hh = Integer.parseInt(s);
                } catch(Exception e) {
                    corrFlag = false;
                }
                if (hh < 0 || hh > 23) {
                    corrFlag = false;
                } else {
                    int mm = 0;
                    String ss = "";
                    ss = timestr.toCharArray()[3]+""+timestr.toCharArray()[4];
                    try {
                        mm = Integer.parseInt(ss);
                    } catch(Exception e) {
                        corrFlag = false;
                    }
                    if (mm < 0 || mm > 59) {
                        corrFlag = false;
                    }
                }
            }
        } else {
            corrFlag = false;
        }
        return corrFlag;
    }
}
