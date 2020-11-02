package sample.classes;

import java.util.Calendar;
import java.util.List;

public class ShowcaseAppointment {
    public Appointment appointment;

    private int id;
    private String date;
    private String time;
    private String names;

    public ShowcaseAppointment(Appointment appointment) {
        List<Driver> dr = appointment.getDriverList();
        this.appointment = appointment;
        this.id = this.appointment.getId();
        this.date = appointment.date.get(Calendar.DAY_OF_MONTH)
                + "-" + appointment.date.get(Calendar.MONTH)
                + "-" + appointment.date.get(Calendar.YEAR);

        this.time = String.valueOf(appointment.getTime());
        this.names = "";
        for (Driver driver : dr) {
            if(dr.get(dr.size()-1) == driver){
                this.names += driver.getName();
            }
            else{
                this.names += driver.getName() + ", ";
            }

        }
    }

    public int GetId(){return this.id;}
    public String getDate() { return date; }

    public String getTime() { return time; }

    public String getNames() { return names; }
}
