package com.example.realtimechatapp.MainActivities.questions;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.realtimechatapp.QuestionAvailability;
import com.example.realtimechatapp.R;

public class Question2_1_1_1 extends AppCompatActivity {

    private Button btn_ans1,btn_ans2,btn_ans3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question2_1_1_1);


        btn_ans1 = findViewById(R.id.btn_ans1);
        btn_ans2 = findViewById(R.id.btn_ans2);

        btn_ans1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Question2_1_1_1.this, Question2_1_1_1_1.class));

            }
        });
        btn_ans2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Question2_1_1_1.this, QuestionRate1.class));

            }
        });
    }
}
