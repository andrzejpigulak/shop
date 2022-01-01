package pl.andrzej.shop.service;

import java.util.Map;

public interface MailService {

    void sendEmail(Map<String, Object> variables, String templateName, String emailReceiver);
}
