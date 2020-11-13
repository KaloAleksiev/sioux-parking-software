package com.twilio;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.logging.Logger;

@Configuration
public class TwilioIntitializer {

//    private final static Logger LOGGER = LoggerFactory.getLogger(TwilioIntitializer.class);

    private final twilioConfig twilioConfig;

    @Autowired
    public TwilioIntitializer(com.twilio.twilioConfig twilioConfig) {
        this.twilioConfig = twilioConfig;
        Twilio.init(
                twilioConfig.getAccountSid(),
                twilioConfig.getAuthToken()
        );
    }
}
