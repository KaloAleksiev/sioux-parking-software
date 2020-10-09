package sample.classes;

import sample.classes.Driver;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.time.LocalTime;

public class Appointment {
    int id;
    Calendar date;
    LocalTime time;
    List<Driver> driverList;

    public int getId() { return this.id; }
    public Calendar getDate() { return date; }
    public LocalTime getTime() { return time; }
    public List<Driver> getDriverList() { return driverList; }

    public Appointment(int day, int month, int year, LocalTime time, List<Driver> driverList) {
        date = Calendar.getInstance();
        date.set(year, month, day);
        this.time = time;
        this.driverList = new ArrayList<>();
        for (Driver driver : driverList) {
            this.driverList.add(driver);
        }
    }

    public Appointment(int day, int month, int year, LocalTime time, List<Driver> driverList, int id) {
        this.id = id;
        date = Calendar.getInstance();
        date.set(year, month, day);
        this.time = time;
        this.driverList = new ArrayList<>();
        for (Driver driver : driverList) {
            this.driverList.add(driver);
        }
    }

    public Appointment(int day, int month, int year, LocalTime time, int id) {
        this.id = id;
        date = Calendar.getInstance();
        date.set(year, month, day);
        this.time = time;
        this.driverList = new ArrayList<>();
    }

    public void addDriver(Driver driver) { driverList.add(driver); }

    public String GetInfo() {
        String info = "Date: " + this.date.get(Calendar.DAY_OF_MONTH) + "-" + this.date.get(Calendar.MONTH) + "-" + this.date.get(Calendar.YEAR) + ", " + "time: " + this.time.toString();
        if (driverList.size() == 0) {
            info += ", no drivers.";
        } else {
            info += ", drivers: ";
            for (Driver driver : driverList) {
                info += " " + driver.GetInfo() + "\n";
            }
            info += ".";
        }
        return info;
    }

    public ShowcaseAppointment getShowcaseAppointment() {
        return new ShowcaseAppointment(this);
    }
}
