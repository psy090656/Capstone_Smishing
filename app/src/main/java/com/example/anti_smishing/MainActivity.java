package com.example.anti_smishing;
import static com.example.anti_smishing.ApiExplorer.get;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.content.pm.PackageManager;
import android.util.Log;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    final static int PERMISSON_REQUEST_CODE = 1000;
    private static final String TAG = "MainActivity";
    private static Context context;
    Intent intent;

    //notification

    String permission_list[] = {
            Manifest.permission_group.SMS
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.d(TAG, "onCreate() called");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MainActivity.context = getApplicationContext();
        permissionCheck();
//        intent = getIntent();
//
//           new Thread(() -> {
//                createNotification(intent,sender, content);
//           }).start();

    }


    public static Context getAppContext() {
        return MainActivity.context;
    }

    private void permissionCheck() {
        Log.d(TAG, "permissionCheck() called");
        if (android.os.Build.VERSION.SDK_INT >= 27) {
            int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS);
            ArrayList<String> arrayPermission = new ArrayList<String>();

            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                arrayPermission.add(Manifest.permission.RECEIVE_SMS);
            }

            permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS);
            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                arrayPermission.add(Manifest.permission.READ_SMS);
            }

            if (arrayPermission.size() > 0) {
                String strArray[] = new String[arrayPermission.size()];
                strArray = arrayPermission.toArray(strArray);
                ActivityCompat.requestPermissions(this, strArray, PERMISSON_REQUEST_CODE);
            }
//            else {
//                //Initialize Code
//            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult called");
        switch (requestCode) {
            case PERMISSON_REQUEST_CODE: {
                if (grantResults.length < 1) {
                    Toast.makeText(this, "Failed get permission", Toast.LENGTH_SHORT).show();
                    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                    return;
                }

                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, "Permission is denied : " + permissions[i], Toast.LENGTH_SHORT).show();
                        finish();
                        return;
                    }
                }

                Toast.makeText(this, "Permission is granted", Toast.LENGTH_SHORT).show();
                //Initialize Code
            }
            break;
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

//    //content에서 url하는 메소드
//    public static String extractUrl(String content){
//        Log.d(TAG, "extractUrl() called");
//        try {
//            String REGEX = "\\b(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
//            Pattern p = Pattern.compile(REGEX, Pattern.CASE_INSENSITIVE);
//            Matcher m = p.matcher(content);
//            if (m.find()) {
//                return m.group();
//            }
//            return "";
//        } catch (Exception e) {
//            return "";
//        }
//    }
//
//
//    //결과 판별
//    public static String extraction(String message){
//        Log.d(TAG, "extraction() called");
//        String malware = "\"result\": \"malware\"";
//        String phishing = "\"result\": \"phishing\"";
//        String malicious = "\"result\": \"malicious\"";
//
//        String doubt="Url 검사결과";
//        if(message.indexOf(malware)>=0)
//            doubt = doubt + "\n malware 감지";
//        else
//            doubt = doubt + "\n malware 없음";
//        if(message.indexOf(malicious) >= 0)
//            doubt = doubt + "\n malicious 감지";
//        else
//            doubt = doubt + "\n malicious 없음";
//        if(message.indexOf(phishing) >= 0)
//            doubt = doubt + "\n phishing 감지";
//        else
//            doubt = doubt + "\n phishing 없음";
//
//        return doubt;
//
//    }

//    // 알림 생성자.
//    public static void createNotification(Intent intent) {
//        Log.d(TAG, "createNotification() called");
////        new Thread(() -> {
//            if (intent != null) {
//                NotificationCompat.Builder builder = new NotificationCompat.Builder(getAppContext(), "default");
////                sender = intent.getStringExtra("sender");
////                content = intent.getStringExtra("content");
//
//                String sender = intent.getStringExtra("sender");
//                String content = intent.getStringExtra("content");
//
//
//                String content_parse_save = extraction(get(extractUrl(content)));
////                String content_parse_save = extractUrl(content);
//                builder.setSmallIcon(R.mipmap.ic_launcher);
//                builder.setContentTitle(sender);
////        builder.setContentText("알람 세부 텍스트");
//
//                builder.setContentText(content_parse_save);
//
//                builder.setColor(Color.RED);
//                // 사용자가 탭을 클릭하면 자동 제거
//                builder.setAutoCancel(true);
//
//                // 알림 표시
//                NotificationManager notificationManager = (NotificationManager) getAppContext().getSystemService(Context.NOTIFICATION_SERVICE);
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                    notificationManager.createNotificationChannel(new NotificationChannel("default", "기본 채널", NotificationManager.IMPORTANCE_DEFAULT));
//                }
//
//                // id값은
//                // 정의해야하는 각 알림의 고유한 int값
//                notificationManager.notify(7, builder.build());
//
//            }
////        }).start();
//    }

}