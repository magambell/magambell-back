package com.magambell.server.user.infra;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.Message;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class SimpleEmailService {

    private final AmazonSimpleEmailService simpleEmailService;

    public static final String FROM = "magambell24@gmail.com";

    public void emailSend(
            String subject,
            String htmlBody,
            String to
    ) {

        SendEmailRequest request = new SendEmailRequest()
                .withDestination(
                        new Destination().withToAddresses(to))
                .withMessage(new Message()
                        .withBody(new Body()
                                .withHtml(new Content()
                                        .withCharset("UTF-8").withData(htmlBody)
                                )
                        )
                        .withSubject(new Content()
                                .withCharset("UTF-8").withData(subject)))
                .withSource(FROM);
        simpleEmailService.sendEmail(request);

    }
}
