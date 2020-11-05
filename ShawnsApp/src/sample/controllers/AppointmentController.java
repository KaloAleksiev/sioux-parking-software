package sample.controllers;

import sample.datasources.DataBase;
import sample.interfaces.DataSource;
import sample.models.Appointment;
import sample.models.Driver;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class AppointmentController {
    List<Appointment> appointmentList;

    DataSource ds;

    public AppointmentController() throws SQLException {
        ds = new DataBase();
        appointmentList = new ArrayList<>();
        appointmentList = ds.GetAppointments();
        for (Appointment a:appointmentList) {
            List<Driver> drivers = ds.GetDriversForAppointment(a);
            a.setDriverList(drivers);
        }
    }

    public List<Appointment> getAllAppointments() {
        return appointmentList;
    }

    public void changeDate(int day, int month, int year, int id){
        getAppointmentById(id).setDate(day, month, year);
        ds.ChangeDate(day, month, year, id);
    }

    public void changeTime(LocalTime time, int id){
        getAppointmentById(id).setTime(time);
        ds.ChangeTime(time,id);
    }

    public void ChangeDrivers(List<Driver>newDrivers, Appointment app){
        getAppointmentById(app.getId()).setDriverList(newDrivers);
        ds.ChangeDrivers(newDrivers, app);
    }

    public Appointment getAppointmentById(int id) {
        for ( Appointment apt : appointmentList ) {
            if (apt.getId() == id) {
                return apt;
            }
        }
        return null;
    }

    public Appointment getLastAddedAppointment() {
        return appointmentList.get(appointmentList.size() - 1);
    }

    public void createAppointment(int day, int month, int year, LocalTime time, List<Driver> driverList) throws SQLException {
        ds.AddAppointmentToDB(day, month, year, time, driverList);
        int id = ds.GetMaxAppointmentID();
        Appointment app = new Appointment(day, month, year, time, driverList, id);
        appointmentList.add(app);
        ds.UpdateDB(app);
    }

    public void UpdateDB(Appointment appointment) {
        ds.UpdateDB(appointment);
    }
//    public int GetMaxAppointmentID() throws SQLException {
//        return ds.GetMaxAppointmentID();
//    }
//    public List<Appointment> GetAppointments() throws SQLException {
//        return ds.GetAppointments();
//    }
//
//    public List<Driver> GetDriversForAppointment(Appointment appointment) throws SQLException {
//        return ds.GetDriversForAppointment(appointment);
//    }
}
