package com.shann.ecom.adapter;

import org.springframework.stereotype.Component;

import com.shann.ecom.libraries.Sendgrid;
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
