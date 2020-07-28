package com.example.demo.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

@Service
public class AndroidPushNotificationService {

    private static final String firebase_server_key = "AAAA2YQhL1I:APA91bHPUPHTVap2__zqgdlAvhVusX-_au5kGbiSGwGAvaNlkTBe13VL-43I-qMy0zeNehi8nk2vYJAr_CROkws2x5YcCwecr10IAS75tgLVE8JFEKNplvMF50hFkXNuiOtAfHLqwTfn";
    private static final String firebase_api_url = "https://fcm.googleapis.com/fcm/send";

    @Async
    public CompletableFuture send(HttpEntity<String> entity){

        RestTemplate restTemplate = new RestTemplate();

        ArrayList<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();

        interceptors.add(new HeaderRequestInterceptor("Authorization", "key="+firebase_server_key));
        interceptors.add(new HeaderRequestInterceptor("Content-Type", "application/json; UTF-8 "));
        restTemplate.setInterceptors(interceptors);

        String firebaseResponse = restTemplate.postForObject(firebase_api_url,entity,String.class);

        return CompletableFuture.completedFuture(firebaseResponse);
    }
}
