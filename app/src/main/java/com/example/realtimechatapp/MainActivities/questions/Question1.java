package com.example.realtimechatapp.MainActivities.questions;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;



import com.example.realtimechatapp.R;



public class Question1 extends AppCompatActivity {
    private Button btn_ans1, btn_ans2;
    private Toolbar toolbar;
    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question1);

        toolbar = findViewById(R.id.toolbar);
        btn_ans1 = findViewById(R.id.btn_ans1);
        btn_ans2 = findViewById(R.id.btn_ans2);

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);

        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btn_ans1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_ans1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(Question1.this, Question2.class));
                    }
                });
            }
        });
        btn_ans2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Question1.this, Question2.class));
            }
        });


    }
}
