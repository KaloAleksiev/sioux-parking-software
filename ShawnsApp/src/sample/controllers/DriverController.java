package sample.controllers;

import sample.datasources.DataBase;
import sample.interfaces.DataSource;
import sample.models.Driver;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DriverController {
    List<Driver> driverList;
    DataSource dc;

    public DriverController() throws SQLException {
        dc = new DataBase();
        driverList = new ArrayList<>();
        driverList = dc.GetDrivers();
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

    public int GetMaxDriverID() throws SQLException {
        return dc.GetMaxDriverID();
    }
    //    public void addDriver(Driver driver) {
//        driverList.add(driver);
//    }

//    public List<Driver> GetDriversFromDB() throws SQLException {
//        return dc.GetDrivers();
//    }

//    public Driver GetDriverById(int id) {
//        for (Driver driver : driverList ) {
//            if (driver.getId() == id) {
//                return driver;
//            }
//        }
//        return null;
//    }
//
//    public Driver getLastAddedDriver() {
//        return driverList.get(driverList.size() - 1);
//    }

//    public boolean removeDriver(int id) {
//        for (Driver driver : driverList) {
//            if (driver.getId() == id) {
//                driverList.remove(driver);
//                return true;
//            }
//        }
//        return false;
//    }
}
