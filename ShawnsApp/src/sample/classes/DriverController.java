package sample.classes;

import sample.classes.Driver;

import java.util.ArrayList;
import java.util.List;

public class DriverController {
    List<Driver> driverList;

    public DriverController() {
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
}
