package com.example.demo.controller;

import com.example.demo.service.AndroidPushNotificationService;
import com.example.demo.service.AndroidPushPeriodicNotifications;
import com.fasterxml.jackson.core.io.JsonEOFException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
public class NotificationController {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    AndroidPushNotificationService androidPushNotificationService;

    /*
    * /send 로 접속하여 동작이 실행되게 한다
    * firebase project의 server key를 가지고 디바이스 토큰으로 알림을 json 데이터 형식으로 firbase에게 요청
    * */
    @GetMapping(value ="/send")
    public @ResponseBody
    ResponseEntity<String> send() throws JsonEOFException, InterruptedException, UnsupportedEncodingException {
        String notifications = AndroidPushPeriodicNotifications.PeriodicNotificationJson();

        HttpEntity<String> request = new HttpEntity<>(notifications);

        CompletableFuture<String> pushNotification = androidPushNotificationService.send(request);
        CompletableFuture.allOf(pushNotification).join();

        try{
            String firebaseResponse = pushNotification.get();
            return new ResponseEntity<>(firebaseResponse, HttpStatus.OK);
        } catch (InterruptedException e){
            logger.debug("got interrupted");
            throw new InterruptedException();
        }catch (ExecutionException e){
            logger.debug("execution error");
        }
        return new ResponseEntity<>("Push Notification ERROR", HttpStatus.BAD_REQUEST);
    }

}
