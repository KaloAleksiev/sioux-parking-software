package com.twilio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

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
    public void sendSms(@RequestBody SmsRequest smsRequest){
        service.sendSms(smsRequest);
    }
}
