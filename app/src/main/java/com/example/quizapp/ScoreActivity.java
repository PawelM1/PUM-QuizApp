package com.example.quizapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ScoreActivity extends AppCompatActivity {

    private TextView scoredTextView;
    String total;
    private TextView resultMessageTextView;
    private Button okButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        scoredTextView = findViewById(R.id.scoreTextView);
        okButton = findViewById(R.id.okButton);
        resultMessageTextView = findViewById(R.id.resultMessageTextView);

        total = " / " + getIntent().getIntExtra("total", 0);
        scoredTextView.setText(getIntent().getIntExtra("score", 0) + total);

        if (getIntent().getIntExtra("score", 0) < ((int) Math.round(getIntent().getIntExtra("total", 1) / 2.0))) {
            resultMessageTextView.setText("Mogło być lepiej :)");
        } else {
            resultMessageTextView.setText("Gratulacje!!!");
        }

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}