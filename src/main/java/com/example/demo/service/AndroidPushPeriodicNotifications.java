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
import java.util.List;

public class AndroidPushPeriodicNotifications {

    /* 하루에 한번씩 보내주는 주기적인 알림이다.
    * 현재코드에서 /send 로 접속을 할시에만 알림이 push된다.
    * 다중수신으로 구현되어있다.
    * 이후 DB를 연결시켜 디바이스토큰을 불러올것이기 때문
    * 현재는 sample data로 하드코딩
    *
    */
    public static String PeriodicNotificationJson() throws JsonEOFException, UnsupportedEncodingException {
        LocalDate localDate = LocalDate.now();

        String sampleData[] = {"d99ZwMPsMbs:APA91bElq2EM1r9gwr3UePWzMcwo9r3LcxzfBcllN2SiwL4WlWBD_LFL_ip8TEhcKj3eVRUKFVbbLbqhmM7KANFnZq4yI_YTi3cmdyv9tfGX85CUMCmDJgwdB5RyCHFVZcdAIz0erJ7T"
                ,"device token value 2","device token value 3"};

        JSONObject body = new JSONObject();

        // 알림을 보낼 디바이스의 디바이스토큰을 넣는 list(registration_ids의 값으로 들어간다)
        List<String> tokenlist = new ArrayList<String>();

        for(int i = 0; i< sampleData.length; i++){
            tokenlist.add(sampleData[i]);
        }

        JSONArray array = new JSONArray();

        for(int i =0; i < tokenlist.size();i++){
            array.put(tokenlist.get(i));
        }
        body.put("registration_ids",array);

        JSONObject notification = new JSONObject();
        notification.put("title", URLEncoder.encode("안녕하세용","UTF-8"));
        notification.put("body",URLEncoder.encode("오늘은 " + localDate.getDayOfWeek().name()+"입니다.", "UTF-8"));

        body.put("notification",notification);

        System.out.println(body.toString());

        return body.toString();
    }
}
