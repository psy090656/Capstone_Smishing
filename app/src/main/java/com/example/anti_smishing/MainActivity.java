package com.example.anti_smishing;
import static com.example.anti_smishing.ApiExplorer.get;

import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Build;
import android.content.pm.PackageManager;
import android.widget.TextView;
import android.widget.Toast;

import com.example.anti_smishing.ApiExplorer;
import com.example.anti_smishing.SmSReceiver;
import com.example.anti_smishing.Notification;

public class MainActivity extends AppCompatActivity {

    final static int PERMISSON_REQUEST_CODE = 1000;

    //sms activity
    TextView tv_sender;
    TextView tv_content;
    //

    String permission_list[] = {
            Manifest.permission_group.SMS
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);

        permissionCheck();

        //smsactivity 임시테스트용
        setContentView(R.layout.activity_sms);

        tv_sender = findViewById(R.id.textView_sender);
        tv_content = findViewById(R.id.textView_content);

        Intent intent = getIntent();
        processCommand(intent);
    }


    private void permissionCheck() {
        if (android.os.Build.VERSION.SDK_INT >= 23) {
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
            } else {
                //Initialize Code
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
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

    //smsactivity
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        processCommand(intent);
    }

    //intent하는거 가져오는 메소드
    private void processCommand(Intent intent){
        if(intent != null){
            String sender = intent.getStringExtra("sender");
            String content = intent.getStringExtra("content");

            //content에서 url 파싱한 결과를 test변수에 저장
            String content_parse_save = get(extractUrl(content));

            tv_sender.setText(sender);
            //문자메시지 내용중 url만 출력하게함. 단, url은 한개의 url만 출력됨.

            tv_content.setText(content_parse_save);
        }
    }

    //content에서 url하는 메소드
    public static String extractUrl(String content){
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
}