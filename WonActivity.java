package com.example.msquizapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class WonActivity extends Activity {

    TextView textView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_won);

        textView = (TextView)findViewById(R.id.score_txt);

        textView.setText(getIntent().getStringExtra("mytext")+" + 5");

    }



    //This Button For Open Game Activity And Finish Won Activity
    public void PlayAgain(View view) {
        Intent intent = new Intent(WonActivity.this, GameActivity.class);
        startActivity(intent);
        finish();
    }



}
