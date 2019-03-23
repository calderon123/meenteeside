package com.example.realtimechatapp.Questions;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.AppCompatButton;
import android.view.View;

import com.example.realtimechatapp.MainActivities.Match;
import com.example.realtimechatapp.R;

public class Question3 extends Activity {


    private AppCompatButton next_question;

    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.q3);

        final ProgressDialog dialog = new ProgressDialog(this);

        dialog.setTitle("Matching Answers");
        dialog.setMessage("Fetching data...");

        next_question = findViewById(R.id.next_question);
        next_question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.show();

                Handler handler = new Handler();

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dialog.dismiss();
                        startActivity(new Intent(Question3.this, Match.class));
                        finish();
                    }
                }, 2000);


            }
        });

    }
}
