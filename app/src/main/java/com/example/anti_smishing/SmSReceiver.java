package com.example.anti_smishing;

import static com.example.anti_smishing.ApiExplorer.get;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SmSReceiver extends BroadcastReceiver {

    private static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
    private static final String TAG = "SmSReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals(SMS_RECEIVED)) {
            Log.d(TAG, "onReceive() called");

            // Bundle을 이용해서 메세지 내용을 가져옴
            Bundle bundle = intent.getExtras();
            SmsMessage[] messages = parseSmsMessage(bundle);

            // 메세지가 있을 경우 내용을 로그로 출력해 봄
            if (messages.length > 0) {
                // 메세지의 내용을 가져옴
                String sender = messages[0].getOriginatingAddress();
                String content = messages[0].getMessageBody();

                // 로그를 찍어보는 과정이므로 생략해도 됨
                Log.d(TAG, "Sender :" + sender);
                Log.d(TAG, "contents : " + content);

                //test

                Thread th = new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        createNotification(sender, content);
                    }
                };
                th.start();

                // 액티비티로 메세지의 내용을 전달해줌 비상시 재활성
//                sendToActivity(context, sender, content);
//                Log.d(TAG, "sendToActivity() called");
            }
        }



    }

    private void createNotification(String sender, String content) {
        Log.d(TAG, "createNotification() called");

            NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity.getAppContext(), "default");
            NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle(builder);
            String content_parse_save = extraction(get(extractUrl(content)));

            builder.setSmallIcon(R.mipmap.ic_launcher);
            builder.setContentTitle("발신번호 : "+sender);
            bigTextStyle.bigText(content_parse_save);

            //아이콘 색상
            builder.setColor(Color.RED);

            // 사용자가 탭을 클릭하면 자동 제거
            builder.setAutoCancel(true);

            // 알림 표시 (Oreo 버전 이상은 채널설정 해줘야함.)
            NotificationManager notificationManager = (NotificationManager) MainActivity.getAppContext().getSystemService(Context.NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                notificationManager.createNotificationChannel(new NotificationChannel("default", "기본 채널", NotificationManager.IMPORTANCE_DEFAULT));
            }


            // id값은
            // 정의해야하는 각 알림의 고유한 int값
            notificationManager.notify(7, builder.build());


    }

    //문자메세지 파싱 함수
    private SmsMessage[] parseSmsMessage(Bundle bundle) {
        Log.d(TAG, "parseSmsMessage() called");
        Object[] objs = (Object[]) bundle.get("pdus");
        SmsMessage[] messages = new SmsMessage[objs.length];
        //objs.length == 문자메시지길이

        for(int i = 0; i < objs.length; i++) {
            //마쉬멜로우 버전인지 체크
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                String format = bundle.getString("format");
                messages[i] = SmsMessage.createFromPdu((byte[]) objs[i], format);
            }
        }

        return messages;
    }

    //content에서 url하는 메소드
    public static String extractUrl(String content){
        Log.d(TAG, "extractUrl() called");
        try {
            String REGEX = "\\b(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
            Pattern p = Pattern.compile(REGEX, Pattern.CASE_INSENSITIVE);
            Matcher m = p.matcher(content);
            if (m.find()) {
                return m.group();
            }
            return "";
        } catch (Exception e) {
            return "";
        }
    }


    //결과 판별
    public static String extraction(String message){
        Log.d(TAG, "extraction() called");
        String malware = "\"result\": \"malware\"";
        String phishing = "\"result\": \"phishing\"";
        String malicious = "\"result\": \"malicious\"";

        String doubt="Url 검사결과";
        String result1= "";
        String result2= "";
        String result3= "";
        String result4= "";
        String result5= "";
        String result6= "";

        if(message.indexOf(malware)>=0)
            result1 = "\nmalware 감지";
        else
            result2 = "\nmalware 없음";
        if(message.indexOf(malicious) >= 0)
            result3 = "\nmalicious 감지";
        else
            result4= "\nmalicious 없음";
        if(message.indexOf(phishing) >= 0)
            result5 = "\nphishing 감지";
        else
            result6 = "\nphishing 없음";

        return doubt+result1+result2+result3+result4+result5+result6;


    }


//    // 액티비티로 메세지의 내용을 전달해줌
//    private void sendToActivity(Context context, String sender, String content) {
//        Intent intent = new Intent(context, SmSReceiver.class);
//        // Flag 설정
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
//                | Intent.FLAG_ACTIVITY_SINGLE_TOP
//                | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//
//        // 메세지의 내용을 넣어줌고 intent준비
//        intent.putExtra("sender", sender);
//        intent.putExtra("content", content);
//
//        context.startActivity(intent);
//    }



}
