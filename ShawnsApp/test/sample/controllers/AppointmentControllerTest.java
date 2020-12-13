package sample.controllers;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import sample.models.Appointment;
import sample.models.Driver;

import java.sql.SQLException;
import java.time.LocalTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class AppointmentControllerTest {

    AppointmentController appointmentController = Mockito.mock(AppointmentController.class);

    @Test
    void getAllAppointments() {
        ArrayList<Appointment> appointmentList = new ArrayList<Appointment>();
        appointmentList.add(new Appointment(21,10,2020, LocalTime.now(), 1));
        appointmentList.add(new Appointment(21,10,2020, LocalTime.now(), 2));;

        Mockito.when(appointmentController.getAllAppointments()).thenReturn(appointmentList);

        assertEquals(2,appointmentList.size());
    }

    @Test
    void changeDate() {
        appointmentController.changeDate(22,12,2020, 1);
        Mockito.verify(appointmentController, Mockito.times(1)).
                changeDate(22,12,2020, 1);
    }

    @Test
    void changeTime() {
        appointmentController.changeTime(LocalTime.of(12,50), 1);
        Mockito.verify(appointmentController, Mockito.times(1)).
                changeTime(LocalTime.of(12,50), 1);
    }

    @Test
    void changeDrivers() {
        ArrayList<Driver> driverList = new ArrayList<Driver>();
        driverList.add(new Driver("abcd","0000","John"));
        driverList.add(new Driver("abcde","00000","Jane"));

        Appointment appointment = new Appointment(21,10,2020, LocalTime.now(), 1);

        appointmentController.changeDrivers(driverList, appointment);
        Mockito.verify(appointmentController, Mockito.times(1)).
                changeDrivers(driverList, appointment);
    }

    @Test
    void getAppointmentById() {
        Appointment appointment = new Appointment(21,10,2020, LocalTime.now(), 1);

        Mockito.when(appointmentController.getAppointmentById(1)).thenReturn(appointment);

        assertEquals(appointmentController.getAppointmentById(1),appointment);
    }

    @Test
    void createAppointment() throws SQLException {

        ArrayList<Driver> driverList = new ArrayList<Driver>();
        driverList.add(new Driver("abcd","0000","John"));
        driverList.add(new Driver("abcde","00000","Jane"));

        Mockito.doThrow(new SQLException()).when(appointmentController).
                createAppointment(12,12,2020, LocalTime.now(), driverList);
    }

    @Test
    void deleteAppointment() {
        Appointment appointment = new Appointment(21,10,2020, LocalTime.now(), 1);

        appointmentController.deleteAppointment(appointment);
        Mockito.verify(appointmentController, Mockito.times(1)).
                deleteAppointment(appointment);
    }
}