package com.shann.ecom.adapter.impl;

import com.shann.ecom.adapter.MailAdapter;
import com.shann.ecom.libraries.Sendgrid;
import org.springframework.stereotype.Component;

@Component
public class EmailAdapter implements MailAdapter {

    @Override
    public void sendMail(String email, String username, String productName) {
        var sendgrid = new Sendgrid();
        var subject = productName+" back in stock!";
        var body = "Dear "+username+", "+productName+ "is now back in the stock. Grab it ASAP!";
        sendgrid.sendEmailAsync(email, subject, body);
    }
    
}
