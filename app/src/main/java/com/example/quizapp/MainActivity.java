package com.example.quizapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Button startButton;
    private Button aboutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startButton = findViewById(R.id.startButton);
        aboutButton = findViewById(R.id.aboutButton);

        startButton.setOnClickListener(v -> {
            Intent categoryIntent = new Intent(MainActivity.this, CategoriesActivity.class);
            startActivity(categoryIntent);
        });

        aboutButton.setOnClickListener(v -> {
            Intent aboutIntent = new Intent(MainActivity.this, AboutActivity.class);
            startActivity(aboutIntent);
        });
    }
}