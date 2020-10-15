package sample.classes;

import sample.datasources.DataBase;
import sample.interfaces.DataSource;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DriverController {
    List<Driver> driverList;
    DataSource dc;

    public DriverController() {
        dc = new DataBase();
        driverList = new ArrayList<>();
    }

    public void addDriver(Driver driver) {
        driverList.add(driver);
    }

    public List<Driver> getAllDrivers() {
        return driverList;
    }

    public Driver getLastAddedDriver() {
        return driverList.get(driverList.size() - 1);
    }

    public boolean removeDriver(int id) {
        for (Driver driver : driverList) {
            if (driver.getId() == id) {
                driverList.remove(driver);
                return true;
            }
        }
        return false;
    }

    public void AddDriverToDB(String phone, String plate, String name) {
        dc.AddDriverToDB(plate, phone, name);
    }

    public int GetMaxDriverID() throws SQLException {
        return dc.GetMaxDriverID();
    }

    public List<Driver> GetDriversFromDB() throws SQLException {
        return dc.GetDrivers();
    }

    public Driver GetDriverById(int id) {
        for (Driver driver : driverList ) {
            if (driver.getId() == id) {
                return driver;
            }
        }
        return null;
    }
}
