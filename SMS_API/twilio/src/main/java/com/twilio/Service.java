package com.twilio;

import org.springframework.beans.factory.annotation.Autowired;
import com.twilio.TwilioSmsSender;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration
public class Service {

    private final TwilioSmsSender twilioSmsSender;


    @Autowired
    public Service(TwilioSmsSender twilioSmsSender) {
        this.twilioSmsSender = twilioSmsSender;
    }

    public void sendSms(SmsRequest smsRequest){
        twilioSmsSender.SendSms(smsRequest);
    }
}
