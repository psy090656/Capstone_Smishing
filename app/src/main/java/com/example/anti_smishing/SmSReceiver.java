package com.example.anti_smishing;

import static com.example.anti_smishing.ApiExplorer.get;
import static com.example.anti_smishing.MainActivity.extractUrl;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SmSReceiver extends BroadcastReceiver {

    private static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
    private static final String TAG = "SmSReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {

//        if (intent.getAction().equals(SMS_RECEIVED)) {
            Log.d(TAG, "onReceive() called");

            // Bundle을 이용해서 메세지 내용을 가져옴
            Bundle bundle = intent.getExtras();
            SmsMessage[] messages = parseSmsMessage(bundle);

            // 메세지가 있을 경우 내용을 로그로 출력해 봄
            if (messages.length > 0) {
                // 메세지의 내용을 가져옴
                String sender = messages[0].getOriginatingAddress();
                String content = messages[0].getMessageBody().toString();

                // 로그를 찍어보는 과정이므로 생략해도 됨
                Log.d(TAG, "Sender :" + sender);
                Log.d(TAG, "contents : " + content);

                //test
//                String test = get(extractUrl(content));

                // 액티비티로 메세지의 내용을 전달해줌
                sendToActivity(context, sender, content);
            }
//        }
    }


    // 액티비티로 메세지의 내용을 전달해줌
    private void sendToActivity(Context context, String sender, String content) {
        Intent intent = new Intent(context, MainActivity.class);
        // Flag 설정
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_SINGLE_TOP
                | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // 메세지의 내용을 넣어줌고 intent준비
        intent.putExtra("sender", sender);
        intent.putExtra("content", content);

        context.startActivity(intent);
    }

    //문자메세지 파싱 함수
    private SmsMessage[] parseSmsMessage(Bundle bundle) {
        Object[] objs = (Object[]) bundle.get("pdus");
        SmsMessage[] messages = new SmsMessage[objs.length];
        //objs.length == 문자메시지길이

        for(int i = 0; i < objs.length; i++) {
            //마쉬멜로우 버전인지 체크
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                String format = bundle.getString("format");
                messages[i] = SmsMessage.createFromPdu((byte[]) objs[i], format);
            }
            else {
                messages[i] = SmsMessage.createFromPdu((byte[]) objs[i]);
            }
        }

        return messages;
    }




}
