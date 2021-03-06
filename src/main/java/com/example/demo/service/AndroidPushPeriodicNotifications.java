package com.example.demo.service;

import com.fasterxml.jackson.core.io.JsonEOFException;
import com.fasterxml.jackson.databind.util.JSONPObject;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class AndroidPushPeriodicNotifications {

    /* 하루에 한번씩 보내주는 주기적인 알림이다.
    * 현재코드에서 /send 로 접속을 할시에만 알림이 push된다.
    * 다중수신으로 구현되어있다.
    * 이후 DB를 연결시켜 디바이스토큰을 불러올것이기 때문
    * 현재는 sample data로 하드코딩
    *
    */
    public static String PeriodicNotificationJson(Map<String, String> params) throws JsonEOFException, UnsupportedEncodingException {
        LocalDate localDate = LocalDate.now();

        String sendTitle = String.valueOf(params.get("title"));
        String sendBody =String.valueOf(params.get("body"));

        String sampleData[] = {"deviceToken,deviceToken2,deviceToken3"};

        JSONObject body = new JSONObject();

        // 알림을 보낼 디바이스의 디바이스토큰을 넣는 list(registration_ids의 값으로 들어간다)

        List<String> tokenlist = new ArrayList<String>(Arrays.asList(sampleData));
        JSONArray array = new JSONArray();

        for (String s : tokenlist) {
            array.put(s);
        }
        body.put("registration_ids",array);

        JSONObject notification = new JSONObject();
        notification.put("title", URLEncoder.encode(sendTitle,"UTF-8"));
        notification.put("body", URLEncoder.encode(sendBody,"UTF-8"));
        notification.put("channel_id","Notice");


        body.put("data",notification);
        System.out.println(body.toString());

        return body.toString();
    }
}
