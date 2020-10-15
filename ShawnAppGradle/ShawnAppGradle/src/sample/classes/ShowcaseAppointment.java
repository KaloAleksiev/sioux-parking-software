package sample.classes;

import java.util.Calendar;

public class ShowcaseAppointment {
    public Appointment appointment;

    public String date;
    public String time;
    public String names;

    public ShowcaseAppointment(Appointment appointment) {
        this.appointment = appointment;
        this.date = appointment.date.get(Calendar.DAY_OF_MONTH) + "-" + appointment.date.get(Calendar.MONTH) + "-" + appointment.date.get(Calendar.YEAR);
        this.time = String.valueOf(appointment.getTime());
        this.names = "";
        for (Driver driver : appointment.getDriverList()) {
            this.names += driver.getName() + ", ";
        }
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getNames() {
        return names;
    }
}
