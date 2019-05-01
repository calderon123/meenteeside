package com.example.realtimechatapp.MainActivities.questions;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.realtimechatapp.R;


public class Question2_2_2 extends AppCompatActivity {

    private Button btn_ans1,btn_answer2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question2_2_2);

        btn_ans1 = findViewById(R.id.btn_ans1);
        btn_answer2 = findViewById(R.id.btn_answer2);


        btn_ans1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Question2_2_2.this,  Question2_2_2_2.class));
            }
        });

        btn_answer2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Question2_2_2.this,  TwiceAWeek.class));
            }
        });

    }
}
