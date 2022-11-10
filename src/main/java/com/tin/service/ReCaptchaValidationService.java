package com.tin.service;

import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.tin.entity.ReCaptchResponseType;

@Service
public class ReCaptchaValidationService {

private static final String GOOGLE_RECAPTCHA_ENDPOINT = "https://www.google.com/recaptcha/api/siteverify";
    
private final String RECAPTCHA_SECRET = "6LefqPYiAAAAANi5yL7usoMRUEQ1z8tDfg5Iwnw0";

    public boolean validateCaptcha(String captchaResponse){
        RestTemplate restTemplate = new RestTemplate();

        MultiValueMap<String, String> requestMap = new LinkedMultiValueMap<>();
        requestMap.add("secret", RECAPTCHA_SECRET);
        requestMap.add("response", captchaResponse);

        ReCaptchResponseType apiResponse = restTemplate.postForObject(GOOGLE_RECAPTCHA_ENDPOINT, requestMap, ReCaptchResponseType.class);
        if(apiResponse == null){
        	System.out.println("false");
            return false;
        }

        return Boolean.TRUE.equals(apiResponse.isSuccess());
    }
}
