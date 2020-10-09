package sample.classes;

import sample.classes.Appointment;
import sample.classes.Driver;

import java.util.ArrayList;
import java.util.List;

public class AppointmentController {
    List<Appointment> appointmentList;

    public AppointmentController() { appointmentList = new ArrayList<>(); }

    public void addAppointment(Appointment apt) {
        appointmentList.add(apt);
    }

    public List<Appointment> getAllAppointments() {
        return appointmentList;
    }

    public Appointment getAppointmentById(int id) {
        for ( Appointment apt : appointmentList ) {
            if (apt.id == id) {
                return apt;
            }
        }
        return null;
    }

    public Appointment getLastAddedAppointment() {
        return appointmentList.get(appointmentList.size() - 1);
    }
}
