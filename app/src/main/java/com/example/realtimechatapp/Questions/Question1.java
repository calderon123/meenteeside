package com.example.realtimechatapp.Questions;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;

import com.example.realtimechatapp.MainActivities.MenteeMainActivity;
import com.example.realtimechatapp.R;

public class Question1 extends Activity {

    private AppCompatButton next_question;
    private AppCompatTextView get_back;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.q1);

        next_question = findViewById(R.id.next_question);
        next_question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Question1.this, Question2.class));
                finish();
            }
        });

        get_back = findViewById(R.id.get_back);
        get_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Question1.this, MenteeMainActivity.class));
                finish();
            }
        });

    }
}
