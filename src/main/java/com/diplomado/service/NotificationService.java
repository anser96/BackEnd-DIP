package com.diplomado.service;


import com.sendgrid.*;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.*;
import com.sendgrid.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.io.IOException;

import static org.aspectj.lang.reflect.DeclareAnnotation.Kind.Method;

@Service
public class NotificationService {
    @Autowired
    private JavaMailSender mailSender;

    private static final String SENDGRID_API_KEY = "${{secrets.EMAIL_SECRETS}}";

    public void sendEmail(String recipientEmail, String subject, String text) throws IOException {
        Email from = new Email("asaenzcordero@gmail.com");
        Email to = new Email(recipientEmail);
        Content content = new Content("text/plain", text);
        Mail mail = new Mail(from, subject, to, content);

        SendGrid sg = new SendGrid(SENDGRID_API_KEY);
        Request request = new Request();
        try {
            request.setMethod(com.sendgrid.Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);
            System.out.println("Status Code: " + response.getStatusCode());
            System.out.println("Response Body: " + response.getBody());
            System.out.println("Response Headers: " + response.getHeaders());
        } catch (IOException ex) {
            System.err.println("Error sending email: " + ex.getMessage());
        }
    }
}
