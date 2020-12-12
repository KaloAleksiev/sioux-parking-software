package com.twilio.logic;

import com.twilio.SmsRequest;
import com.twilio.models.Driver;
import com.twilio.repository.IDriverRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DriverLogic {

    @Autowired
    IDriverRepository idr;

    public Driver GetDriverByLicensePlate(String licensePlate) {
        return this.idr.getByLicensePlate(licensePlate);
    }

    public SmsRequest createSMS(String licensePlate) {
        Driver driver = this.GetDriverByLicensePlate(licensePlate);
        return new SmsRequest(driver.getPhoneNumber(), "u suck");
    }
}
