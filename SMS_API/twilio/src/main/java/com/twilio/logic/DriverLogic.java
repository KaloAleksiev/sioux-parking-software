package com.twilio.logic;

import com.twilio.SmsRequest;
import com.twilio.models.Driver;
import com.twilio.repository.IDriverRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class DriverLogic {

    @Autowired
    IDriverRepository idr;

    public Driver GetDriverByLicensePlate(String licensePlate) {
        return this.idr.getByLicensePlate(licensePlate);
    }

    public SmsRequest createSMS(String licensePlate) {
        Driver driver = this.GetDriverByLicensePlate(licensePlate);
        Random rn = new Random();
        int rndNumber = rn.nextInt(400 - 300 + 1) + 300;
        return new SmsRequest(driver.getPhoneNumber(), "Hello, " + driver.getName() + "! Your parking spot is B" + rndNumber);
    }
}
