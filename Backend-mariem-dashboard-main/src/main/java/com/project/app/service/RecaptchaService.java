package com.project.app.service;


import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class RecaptchaService {
	
	@Value("${recaptcha.secret-key}") // Clé secrète stockée dans application.properties
    private String recaptchaSecret;

    private static final String GOOGLE_RECAPTCHA_VERIFY_URL =
            "https://www.google.com/recaptcha/api/siteverify";

    public boolean verifyCaptcha(String captchaToken) {
        if (captchaToken == null || captchaToken.isEmpty()) {
            return false;
        }

        RestTemplate restTemplate = new RestTemplate();
        String requestUrl = GOOGLE_RECAPTCHA_VERIFY_URL +
                "?secret=" + recaptchaSecret +
                "&response=" + captchaToken;

        Map<String, Object> response = restTemplate.postForObject(requestUrl, null, Map.class);

        return response != null && (Boolean) response.get("success");
    }

}