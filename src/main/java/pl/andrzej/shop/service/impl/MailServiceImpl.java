package pl.andrzej.shop.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.context.Context;
import pl.andrzej.shop.model.dao.Template;
import pl.andrzej.shop.service.MailService;
import pl.andrzej.shop.service.TemplateService;

import java.util.Locale;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {

    private final TemplateService templateService;
    private final JavaMailSender javaMailSender;
    private final ITemplateEngine iTemplateEngine;

    @Override
    @Async
    public void sendEmail(Map<String, Object> variables, String templateName, String emailReceiver) {
        Template template = templateService.searchByName(templateName);
        Context context = new Context(Locale.getDefault(), variables);
        String bodyMail = iTemplateEngine.process(template.getBody(), context);
        javaMailSender.send(mimeMessage -> {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
            mimeMessageHelper.setTo(emailReceiver);
            mimeMessageHelper.setSubject(template.getTitle());
            mimeMessageHelper.setText(bodyMail, true);
        });
    }
}
