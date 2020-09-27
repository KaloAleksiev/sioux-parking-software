package sample.classes;

import sample.classes.Driver;
import java.util.Calendar;
import java.util.List;
import java.time.LocalTime;

public class Appointment {
    int id;
    Calendar date;
    LocalTime time;
    List<Driver> driverList;

    public int getId() { return id; }
    public Calendar getDate() { return date; }
    public LocalTime getTime() { return time; }
    public List<Driver> getDriverList() { return driverList; }

    public Appointment(int day, int month, int year, LocalTime time, List<Driver> driverList) {
        date.set(year, month, day);
        this.time = time;
        this.driverList = driverList;
    }

    public Appointment(int day, int month, int year, LocalTime time, List<Driver> driverList, int id) {
        this.id = id;
        date.set(year, month, day);
        this.time = time;
        this.driverList = driverList;
    }

    public void addDriver(Driver driver) { driverList.add(driver); }

    public String GetInfo() {
        String info = "Date: " + this.date.toString() + ", time: " + this.time.toString();
        if (driverList.size() == 0) {
            info += ", no drivers.";
        } else {
            info += ", drivers: ";
            for (Driver driver : driverList) {
                info += " " + driver.name + "\n";
            }
            info += ".";
        }
        return info;
    }
}
