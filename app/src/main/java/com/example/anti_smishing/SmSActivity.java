package com.example.anti_smishing;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class SmSActivity extends AppCompatActivity {

    TextView tv_sender;
    TextView tv_date;
    TextView tv_content;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 21번줄부터 26번줄까지 새로운 xml파일로 제작할 예정.
        setContentView(R.layout.activity_main);

        tv_sender = findViewById(R.id.textView_sender);
        tv_date = findViewById(R.id.textView_date);
        tv_content = findViewById(R.id.textView_content);

        Intent intent = getIntent();
        processCommand(intent);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        processCommand(intent);
    }

    private void processCommand(Intent intent){
        if(intent != null){
            String sender = intent.getStringExtra("sender");
            String date = intent.getStringExtra("date");
            String content = intent.getStringExtra("content");

            tv_sender.setText(sender);
            tv_date.setText(date);
            tv_content.setText(content);
        }
    }


}