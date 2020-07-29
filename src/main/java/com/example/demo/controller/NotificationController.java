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
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
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

//    @Scheduled(fixedRate = 5000) //5초
    @GetMapping(value ="/send", produces = "application/json;")
    public @ResponseBody
    ResponseEntity<String> send(@RequestParam("sendTitle") String title, @RequestParam("sendBody") String body) throws JsonEOFException, InterruptedException, UnsupportedEncodingException {

        logger.debug("Titls is [{}], Body is [{}]",title,body);
        Map<String, String> map = new HashMap<String, String>();
        map.put("title",title);
        map.put("body",body);
        String notifications = AndroidPushPeriodicNotifications.PeriodicNotificationJson(map);

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
