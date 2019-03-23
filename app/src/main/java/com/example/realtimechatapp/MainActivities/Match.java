package com.example.realtimechatapp.MainActivities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;

import com.example.realtimechatapp.R;

public class Match extends Activity {

    private Button user_mentee;
    private Button user_mentor;


    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.matching);


    }
}
