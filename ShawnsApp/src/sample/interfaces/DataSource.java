package sample.interfaces;

import sample.classes.Appointment;
import sample.classes.Driver;

import java.sql.SQLException;
import java.time.LocalTime;
import java.util.List;

public interface DataSource {
    public int GetMaxDriverID() throws SQLException;
    public int GetMaxAppointmentID() throws SQLException;
    public void AddDriverToDB(String plate, String phone, String name);
    public void AddAppointmentToDB(int day, int month, int year, LocalTime time, List<Driver> driverList) throws SQLException;
    public void UpdateDB(Appointment appointment);
    public List<Driver> GetDrivers() throws SQLException;
    public List<Integer> GetDriversForAppointment(Appointment appointment) throws SQLException;
    public List<Appointment> GetAppointments() throws SQLException;
}
