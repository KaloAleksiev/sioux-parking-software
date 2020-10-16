package com.twilio;

import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TwilioSmsSender implements ISmsSender {

    private final twilioConfig twilioConfig;

    @Autowired
    public TwilioSmsSender(com.twilio.twilioConfig twilioConfig) {
        this.twilioConfig = twilioConfig;
    }

    @Override
    public void SendSms(SmsRequest SmsRequest) {

        PhoneNumber to = new PhoneNumber(SmsRequest.getPhoneNumber());
        PhoneNumber from = new PhoneNumber(twilioConfig.getTrialNumber());
        String message = SmsRequest.getMessage();

        Message.creator(to,from,message).create();
    }
}
