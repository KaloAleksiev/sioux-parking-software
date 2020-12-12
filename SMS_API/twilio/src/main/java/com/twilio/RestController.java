package com.twilio;

import com.twilio.logic.DriverLogic;
import com.twilio.models.Driver;
import com.twilio.repository.IDriverAppointmentRepository;
import com.twilio.repository.IDriverRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:4200")
@org.springframework.web.bind.annotation.RestController
@RequestMapping("/sms")
public class RestController {

    private final Service service;

    @Autowired
    public RestController(Service service) {
        this.service = service;
    }

    @PostMapping()
    public void sendSms(@RequestBody String licensePlate){//@RequestBody SmsRequest smsRequest){
        service.sendSms(this.driverLogic.createSMS(licensePlate));
    }

    @Autowired
    public DriverLogic driverLogic;

    @GetMapping("/test")
    public Driver GetDriverByLicensePlate(@RequestBody String licensePlate) {
        return this.driverLogic.GetDriverByLicensePlate(licensePlate);
    }
}
