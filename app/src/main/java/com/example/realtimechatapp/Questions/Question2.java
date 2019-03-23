package com.example.realtimechatapp.Questions;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.View;

import com.example.realtimechatapp.R;

public class Question2 extends Activity {
    private AppCompatButton previous_question;
    private AppCompatButton next_question;
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.q2);

        previous_question = findViewById(R.id.previous_question);
        previous_question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Question2.this, Question1.class));
                finish();
            }

        });

        next_question = findViewById(R.id.next_question);
        next_question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Question2.this, Question3.class));
                finish();
            }
        });

    }
}
