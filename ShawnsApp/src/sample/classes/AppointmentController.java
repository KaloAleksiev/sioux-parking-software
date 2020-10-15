package sample.classes;

import sample.datasources.DataBase;
import sample.interfaces.DataSource;

import java.sql.SQLException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class AppointmentController {
    List<Appointment> appointmentList;

    DataSource ds;

    public AppointmentController() {
        ds = new DataBase();
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
        ds.AddAppointmentToDB(day, month, year, time, driverList);
    }

    public int GetMaxAppointmentID() throws SQLException {
        return ds.GetMaxAppointmentID();
    }

    public void UpdateDB(Appointment appointment) {
        ds.UpdateDB(appointment);
    }

    public List<Appointment> GetAppointments() throws SQLException {
        return ds.GetAppointments();
    }

    public List<Integer> GetDriversForAppointment(Appointment appointment) throws SQLException {
        return ds.GetDriversForAppointment(appointment);
    }
}
