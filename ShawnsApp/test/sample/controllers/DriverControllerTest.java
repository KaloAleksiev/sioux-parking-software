package sample.controllers;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import sample.models.Driver;

import java.sql.SQLException;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.assertEquals;

class DriverControllerTest {

    DriverController driverController = Mockito.mock(DriverController.class);

    @Test
    void getAllDrivers() {
        ArrayList<Driver> driverList = new ArrayList<Driver>();
        driverList.add(new Driver("abcd","0000","John"));
        driverList.add(new Driver("abcde","00000","Jane"));

        Mockito.when(driverController.getAllDrivers()).thenReturn(driverList);

        assertEquals(2,driverList.size());
    }

    @Test
    void addDriver() throws SQLException {
        Mockito.doThrow(new SQLException()).when(driverController).
                addDriver("abcd","0000","John");
    }

    @Test
    void changeName() {
        driverController.changeName("Sam", 1);
        Mockito.verify(driverController, Mockito.times(1)).
                changeName("Sam",1);
    }

    @Test
    void changeLicense() {
        driverController.changeLicense("0000", 1);
        Mockito.verify(driverController, Mockito.times(1)).
                changeLicense("0000",1);
    }

    @Test
    void changeNumber() {
        driverController.changeNumber("123456789", 1);
        Mockito.verify(driverController, Mockito.times(1)).
                changeNumber("123456789",1);
    }

    @Test
    void getDriverById() {
        Driver returnedDriver = new Driver("123456789", "1111", "Tom");

        Mockito.when(driverController.getDriverById(1)).thenReturn(returnedDriver);

        assertEquals(driverController.getDriverById(1),returnedDriver);
    }

    @Test
    void deleteDriver() {
        driverController.deleteDriver(1);
        Mockito.verify(driverController, Mockito.times(1)).
                deleteDriver(1);
    }
}