package sample.controllers;

import sample.datasources.DataBase;
import sample.datasources.LocalDB;
import sample.interfaces.DataSource;
import sample.models.Driver;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DriverController {
    private List<Driver> driverList;
    private DataSource dc;

    public DriverController() throws SQLException {
        dc = new DataBase();
        driverList = new ArrayList<>();
        driverList = new ArrayList<Driver>();
        driverList.addAll(dc.GetDrivers());
    }

    public List<Driver> getAllDrivers() {
        return driverList;
    }

    public void AddDriver(String phone, String plate, String name) throws SQLException {
        dc.AddDriverToDB(plate, phone, name);
        int id = dc.GetMaxDriverID();
        Driver d = new Driver(plate, phone, name, id);
        driverList.add(d);
    }
    public void ChangeName(String name, int id){
        this.GetDriverById(id).setName(name);
        dc.ChangeDriverName(name, id);
    }
    public void ChangeLicense(String license, int id){
        this.GetDriverById(id).setLicencePlate(license);
        dc.ChangeDriverLicensePlate(license, id);
    }
    public void ChangeNumber(String number, int id){
        this.GetDriverById(id).setPhoneNumber(number);
        dc.ChangeDriverNumber(number, id);
    }

    public Driver GetDriverById(int id) {
        for (Driver driver : driverList ) {
            if (driver.getId() == id) {
                return driver;
            }
        }
        return null;
    }

    public void removeDriver(int id) {
        Driver d = GetDriverById(id);
        this.driverList.remove(d);
    }
}
