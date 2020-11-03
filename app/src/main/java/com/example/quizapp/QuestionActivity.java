package com.example.quizapp;

import android.animation.Animator;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuestionActivity extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();

    private final int HOW_MANY_QUESTION = 6;
    private TextView questionTextView;
    private TextView noIndicator;
    private LinearLayout answerContainer;
    private Button nextButton;
    List<QuestionModel> questionModelList;
    private int count;
    private int position = 0;
    private int score = 0;
    private String category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        questionTextView = findViewById(R.id.questionTextView);
        noIndicator = findViewById(R.id.no_indicator);
        answerContainer = findViewById(R.id.options_container);
        nextButton = findViewById(R.id.nextButton);

        category = getIntent().getStringExtra("category_name");

        for (int i = 0; i < 4; i++) {
            answerContainer.getChildAt(i).setOnClickListener(v -> checkAnswer(((Button) v)));
        }

        questionModelList = new ArrayList<>();

        String path = "Questions/" + category;
        myRef = database.getReference(path);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    questionModelList.add(dataSnapshot.getValue(QuestionModel.class));

                }
                Collections.shuffle(questionModelList);

                if (questionModelList.size() > HOW_MANY_QUESTION) {
                    questionModelList = questionModelList.subList(0, HOW_MANY_QUESTION);
                }

                if (questionModelList.size() > 0) {
                    for (int i = 0; i < 4; i++) {
                        answerContainer.getChildAt(i).setOnClickListener(v -> checkAnswer((Button) v));
                    }

                    playAnim(questionTextView, 0, questionModelList.get(position).getQuestion());
                    nextButton.setOnClickListener(v -> {
                        nextButton.setEnabled(false);
                        nextButton.setAlpha(0.7f);
                        enableOption(true);
                        position++;
                        if (position == questionModelList.size()) {
                            //Return activity with user score
                            Intent scoreIntent = new Intent(QuestionActivity.this, ScoreActivity.class);
                            scoreIntent.putExtra("score", score);
                            scoreIntent.putExtra("total", questionModelList.size());
                            startActivity(scoreIntent);
                            finish();
                            return;
                        }
                        count = 0;
                        playAnim(questionTextView, 0, questionModelList.get(position).getQuestion());
                    });
                } else {
                    finish();
                    Toast.makeText(QuestionActivity.this, "Brak pytania", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(QuestionActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void playAnim(View view, final int value, final String data) {
        view.animate().alpha(value).scaleX(value).scaleY(value).setDuration(500).setStartDelay(100)
                .setInterpolator(new DecelerateInterpolator()).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                if (value == 0 && count < 4) {
                    String option = "";
                    if (count == 0) {
                        option = questionModelList.get(position).getAnswerA();
                    } else if (count == 1) {
                        option = questionModelList.get(position).getAnswerB();
                    } else if (count == 2) {
                        option = questionModelList.get(position).getAnswerC();
                    } else if (count == 3) {
                        option = questionModelList.get(position).getAnswerD();
                    }
                    playAnim(answerContainer.getChildAt(count), 0, option);
                    count++;
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (value == 0) {
                    try {
                        ((TextView) view).setText(data);
                        noIndicator.setText(position + 1 + "/" + questionModelList.size());
                    } catch (ClassCastException e) {
                        ((Button) view).setText(data);
                    }
                    view.setTag(data);
                    playAnim(view, 1, data);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    private void checkAnswer(Button selectOption) {
        enableOption(false);
        nextButton.setEnabled(true);
        nextButton.setAlpha(1);
        if (selectOption.getText().toString().equals(questionModelList.get(position).getCorrectAnswer())) {
            //if user choose correct answer green button
            score++;
            selectOption.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#32CD32")));
        } else {
            //if user choose correct answer green button
            selectOption.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ff0000")));
            Button correctAns = answerContainer.findViewWithTag(questionModelList.get(position).getCorrectAnswer());
            correctAns.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#32CD32")));
        }
    }

    private void enableOption(boolean enable) {
        for (int i = 0; i < 4; i++) {
            answerContainer.getChildAt(i).setEnabled(enable);
            if (enable) {
                answerContainer.getChildAt(i).setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#2F6387")));
            }
        }
    }
}