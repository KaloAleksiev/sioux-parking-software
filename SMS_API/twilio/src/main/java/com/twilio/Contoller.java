package com.twilio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("sms")
public class Contoller {

private final Service service;

    @Autowired
    public Contoller(Service service) {
        this.service = service;
    }
}
