package sample.controllers;

import sample.datasources.DataBase;
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

    public void addDriver(String phone, String plate, String name) throws SQLException {
        dc.AddDriverToDB(plate, phone, name);
        int id = dc.GetMaxDriverID();
        Driver d = new Driver(plate, phone, name, id);
        driverList.add(d);
    }
    public void changeName(String name, int id){
        this.getDriverById(id).setName(name);
        dc.ChangeDriverName(name, id);
    }
    public void changeLicense(String license, int id){
        this.getDriverById(id).setLicencePlate(license);
        dc.ChangeDriverLicensePlate(license, id);
    }
    public void changeNumber(String number, int id){
        this.getDriverById(id).setPhoneNumber(number);
        dc.ChangeDriverNumber(number, id);
    }

    public Driver getDriverById(int id) {
        for (Driver driver : driverList ) {
            if (driver.getId() == id) {
                return driver;
            }
        }
        return null;
    }

    public void deleteDriver(int id) {
        this.driverList.removeIf(d -> d.getId() == id );
        dc.DeleteDriver(id);
    }
}
