package sample.interfaces;

import sample.classes.Appointment;
import sample.classes.Driver;

import java.sql.SQLException;
import java.time.LocalTime;
import java.util.List;

public interface DataSource {
    int GetMaxDriverID() throws SQLException;
    int GetMaxAppointmentID() throws SQLException;
    void AddDriverToDB(String plate, String phone, String name);
    void AddAppointmentToDB(int day, int month, int year, LocalTime time, List<Driver> driverList) throws SQLException;
    void UpdateDB(Appointment appointment);
    List<Driver> GetDrivers() throws SQLException;
    List<Driver> GetDriversForAppointment(Appointment appointment) throws SQLException;
    List<Appointment> GetAppointments() throws SQLException;
    void ChangeDate(int day, int month, int year, int appId);
    void ChangeTime(LocalTime time, int appId);
    void ChangeDrivers(List<Driver> newDrivers, Appointment ap);
}
