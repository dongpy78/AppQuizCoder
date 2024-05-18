package com.buivandong.appquizcoder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class TongKetActivity extends AppCompatActivity {
    private TextView textResultPercent, textEvaluate, textHighScore;
    private Button btnAgain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tong_ket);

        textResultPercent = findViewById(R.id.text_result_percent);
        textEvaluate = findViewById(R.id.text_evaluate);
        textHighScore = findViewById(R.id.text_high_score);
        btnAgain = findViewById(R.id.btn_again);

        // Receive data from Intent
        Intent intent = getIntent();
        int totalQuestions = intent.getIntExtra("TOTAL_QUESTIONS", 0);
        int highScore = intent.getIntExtra("HIGH_SCORE", 0); // Nhận điểm cao nhất
        // Nhận điểm số phần trăm từ Intent
        int scorePercentage = getIntent().getIntExtra("PERCENT_ACHIEVE", 0);

        // Calculate percentage

        // Update UI

        textResultPercent.setText("Final Score: " + scorePercentage + "/100 (" + scorePercentage + "%)");
        textHighScore.setText("High Score: " + highScore + "/100 (" + highScore + "%)");

        textEvaluate.setText(getEvaluationMessage(scorePercentage));


        btnAgain.setOnClickListener(v -> {
            Intent playAgainIntent = new Intent(TongKetActivity.this, QuestionsReactJS.class);
            startActivity(playAgainIntent);
            finish();
        });

    }

    private String getEvaluationMessage(int percent) {
        if (percent >= 90) {
            return "Excellent 🥇";
        } else if (percent >= 75) {
            return "Well done that's great 👏😘";
        } else if (percent >= 50) {
            return "You are doing well but a little of work will make you better 😘";
        } else {
            return "You have to work harder 😥";
        }
    }
}