package sample.classes;

import sample.classes.Appointment;
import sample.classes.Driver;

import java.sql.SQLException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class AppointmentController {
    List<Appointment> appointmentList;

    DataControl dc;

    public AppointmentController() {
        dc = new DataControl();
        appointmentList = new ArrayList<>();
    }

    public void addAppointment(Appointment apt) {
        appointmentList.add(apt);
    }

    public List<Appointment> getAllAppointments() {
        return appointmentList;
    }

    public Appointment getAppointmentById(int id) {
        for ( Appointment apt : appointmentList ) {
            if (apt.id == id) {
                return apt;
            }
        }
        return null;
    }

    public Appointment getLastAddedAppointment() {
        return appointmentList.get(appointmentList.size() - 1);
    }

    public void AddAppointmentToDB(int day, int month, int year, LocalTime time, List<Driver> driverList) throws SQLException {
        dc.AddAppointmentToDB(day, month, year, time, driverList);
    }

    public int GetMaxAppointmentID() throws SQLException {
        return dc.GetMaxAppointmentID();
    }

    public void UpdateDB(Appointment appointment) {
        dc.UpdateDB(appointment);
    }

    public List<Appointment> GetAppointments() throws SQLException {
        return dc.GetAppointments();
    }

    public List<Integer> GetDriversForAppointment(Appointment appointment) throws SQLException {
        return dc.GetDriversForAppointment(appointment);
    }
}
