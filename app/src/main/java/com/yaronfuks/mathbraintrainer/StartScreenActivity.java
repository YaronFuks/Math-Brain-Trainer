package com.yaronfuks.mathbraintrainer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class StartScreenActivity extends AppCompatActivity {

    private ImageButton startButton;

    public void startPlay(View view){

        Intent startIntent = new Intent(StartScreenActivity.this, MainActivity.class);

        startActivity(startIntent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);

        startButton = findViewById(R.id.imageButton);
    }
}
