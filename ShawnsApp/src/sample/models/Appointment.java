package sample.models;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.time.LocalTime;

public class Appointment {
    private int id;
    private LocalTime time;
    private List<Driver> driverList;
    private LocalDate date;

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate dateTime) {
        this.date = dateTime;
    }

    public int getId() { return this.id; }
    public List<Driver> getDriverList() { return driverList; }

    public Appointment(int day, int month, int year, LocalTime time, List<Driver> driverList) {
        this.date = new LocalDate(year, month, day);
        this.time = time;
        this.driverList = new ArrayList<>();
        for (Driver driver : driverList) {
            this.driverList.add(driver);
        }
    }

    public Appointment(int day, int month, int year, LocalTime time, List<Driver> driverList, int id) {
        this.id = id;
        this.date = new LocalDate(year, month, day);
        this.time = time;
        this.driverList = new ArrayList<>();
        for (Driver driver : driverList) {
            this.driverList.add(driver);
        }
    }

    public Appointment(int id,LocalDate date, LocalTime time, List<Driver> driverList) {
        this.id = id;
        this.date = date;
        this.time = time;
        this.driverList = driverList;
    }

    public Appointment(int day, int month, int year, LocalTime time, int id) {
        this.id = id;
        this.date = new LocalDate(year, month, day );
        this.time = time;
        this.driverList = new ArrayList<>();
    }

    public void setDate(int day, int month, int year) {
        this.date = new LocalDate(year, month, day );
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public void setDriverList(List<Driver> driverList) {
        this.driverList = driverList;
    }

    public ShowcaseAppointment getShowcaseAppointment() {
        return new ShowcaseAppointment(this);
    }

    public LocalTime getTime() {
        return this.time;
    }
}
