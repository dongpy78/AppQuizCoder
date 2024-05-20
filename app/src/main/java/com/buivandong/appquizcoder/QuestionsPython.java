package com.buivandong.appquizcoder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuestionsPython extends AppCompatActivity {
    private CountDownTimer countDownTimer;
    ProgressBar progressBar;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private int highScore;
    private static final String SHARED_PREFS = "sharedPrefs";
    private static final String HIGH_SCORE_KEY = "highScore";
    private static final long START_TIME_IN_MILLIS = 90000; // 5 minutes in milliseconds
    private long timeLeftInMinus = START_TIME_IN_MILLIS;
    private TextView question, noIndicator, scoreView, timerView;
    private LinearLayout optionsContainer;
    private Button nextBtn;
    private Button exitBtn;
    List<QuestionModel> list;
    private int count = 0;
    int position = 0;
    private int score = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions_python);
        findsView();
        progressBar.setProgress(0);

        sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        editor = sharedPreferences.edit();
        highScore = sharedPreferences.getInt(HIGH_SCORE_KEY, 0);

        list_questions();
        update_question_score();

        optionContainer();
        playAnim(question, 0, list.get(position).getQuestion());
        nextBtnFunction();
        exitBtnFunction();

        startTimer();
    }
    private void exitBtnFunction() {
        exitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private void optionContainer() {
        for(int i = 0; i < 4; i++) {
            optionsContainer.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    checkAnswer((Button)view);
                }
            });
        }
    }

    private void nextBtnFunction() {
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextBtn.setVisibility(View.INVISIBLE);
                nextBtn.setEnabled(false);
                nextBtn.setAlpha(0.7f);
                enableOptions(true);
                position++;
                if(position == list.size()) {
                    // score activity
                    finishQuiz();
                    updateProgressBar();
                    return;

                }
                count = 0;
                playAnim(question, 0, list.get(position).getQuestion());

            }
        });
    }

    private void findsView() {
        question = findViewById(R.id.question);
        noIndicator = findViewById(R.id.no_indicator);
        optionsContainer = findViewById(R.id.options_container);
        exitBtn = findViewById(R.id.exit_btn);
        nextBtn = findViewById(R.id.next_btn);
        exitBtn = findViewById(R.id.exit_btn);
        timerView = findViewById(R.id.timer);
        progressBar = findViewById(R.id.progressBar);
        scoreView = findViewById(R.id.score);

        list = new ArrayList<>();
    }

    private void update_question_score() {
        noIndicator.setText("Questions: " + (position+1) +"/"+ list.size());
        scoreView.setText("Score: " + score + "/ 100");
    }

    private void list_questions() {
        list.add(new QuestionModel("What is the output of this code ? x = [1,3,5]; print(x in x)", "[1,3,5]", "False", "Error", "True", "False"));
        list.add(new QuestionModel("What is the output of this code ? x = 0b0010; print(x)", "0", "0b1010", "1", "2", "2"));
        list.add(new QuestionModel("What is the output of this code ? print(2**(1==\"1\"))", "2", "1", "Error", "True", "1"));
        list.add(new QuestionModel("Which of these variable names is valid ?", "print", "12foo", "good_days", "a-variable", "good_days"));
        list.add(new QuestionModel("What is the output of this code ? var = \"70.0\"; print(int(var))", "Error", "\"70\"", "70", "70.0", "Error"));
        list.add(new QuestionModel("What is the output of this code ? print(3**3%7)", "3", "0", "6", "Error", "6"));
        list.add(new QuestionModel("What is the output of this code ? a = 'a'; print(type(a))", "<class 'varchar'>", "<class 'string'>", "<class 'str'>", "<class 'char'>", "<class 'str'>"));

        // XÁO TRỘN DANH SÁCH CÂU HỎI
        Collections.shuffle(list);
    }

    private void playAnim(final View view, final int value, String data) {
        view.animate().alpha(value).scaleX(value).scaleY(value).setDuration(500).setStartDelay(100).setInterpolator(new DecelerateInterpolator()).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(@NonNull Animator animator) {
                if(value == 0 && count < 4) {
                    String option = "";
                    if(count == 0) {
                        option = list.get(position).getOptionA();
                    } else if(count == 1) {
                        option = list.get(position).getOptionB();
                    } else if(count == 2) {
                        option = list.get(position).getOptionC();
                    } else if(count == 3) {
                        option = list.get(position).getOptionD();
                    }
                    playAnim(optionsContainer.getChildAt(count), 0, option);
                    count++;
                }
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onAnimationEnd(@NonNull Animator animator) {
                // data change
                if(value == 0) {
                    try {
                        ((TextView)view).setText(data);
                        noIndicator.setText("Questions: " + (position+1) +"/"+ list.size());

                    } catch (ClassCastException ex) {
                        ((Button)view).setText(data);
                    }
                    view.setTag(data);
                    playAnim(view, 1, data);
                }
            }

            @Override
            public void onAnimationCancel(@NonNull Animator animator) {

            }

            @Override
            public void onAnimationRepeat(@NonNull Animator animator) {

            }
        });
    }

    private void checkAnswer(Button selectOption) {
        enableOptions(false);
        nextBtn.setEnabled(true);
        nextBtn.setAlpha(1);
        if(selectOption.getText().toString().equals(list.get(position).getCorrectAnswer())) {
            // corect
            score++;
            selectOption.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#1cb18a")));
            updateProgressBar();
        } else {
            // incorrect
            selectOption.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ff6b6b")));
            Button correctOption = (Button) optionsContainer.findViewWithTag(list.get(position).getCorrectAnswer());
            correctOption.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#1cb18a")));
        }

        // Update score on UI
        updateScore();

        // Make nextBtn visible when an option is clicked
        nextBtn.setVisibility(View.VISIBLE);

        if (position == list.size() - 1) {
            nextBtn.setText("Finish");
            nextBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finishQuiz();
                }

            });
        }
    }

    private void updateScore() {
        // Calculate the score in percentage
        int scorePercentage = (int) ((score / (double) list.size()) * 100);
        scoreView.setText("Score: " + scorePercentage + "/100");
    }

    private void enableOptions(boolean enable) {
        for(int i = 0; i < 4; i++) {
            optionsContainer.getChildAt(i).setEnabled(enable);

            if(enable) {
                // Trích xuất màu từ tập tin XML
                optionsContainer.getChildAt(i).setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#2291B1")));
            }
        }
    }
    private void finishQuiz() {
        int scorePercentage = (int) ((score / (double) list.size()) * 100); // Calculate the score percentage

        // Cập nhật điểm cao nhất nếu điểm hiện tại cao hơn
        if (scorePercentage > highScore) {
            highScore = scorePercentage;
            editor.putInt(HIGH_SCORE_KEY, highScore);
            editor.apply();
        }

        Intent intent = new Intent(QuestionsPython.this, TongKetActivity.class);
        intent.putExtra("PERCENT_ACHIEVE", scorePercentage);
        intent.putExtra("HIGH_SCORE", highScore); // Truyền điểm cao nhất sang activity khác
        startActivity(intent);
        finish();
    }

    private void startTimer() {
        countDownTimer = new CountDownTimer(timeLeftInMinus, 1000) {
            @Override
            public void onTick(long l) {
                timeLeftInMinus = l;
                updateTimer();
            }
            @SuppressLint("SetTextI18n")
            @Override
            public void onFinish() {
                timerView.setText("00:00");
                finishQuiz();
            }
        }.start();
    }

    @SuppressLint("SetTextI18n")
    private void updateTimer() {
        int minutes = (int) (timeLeftInMinus / 1000) / 60;
        int seconds = (int) (timeLeftInMinus / 1000) % 60;
        String timeLeftFormatted = String.format("%02d:%02d", minutes, seconds);
        timerView.setText(timeLeftFormatted);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    private void updateProgressBar() {
        int progress = (int) (((double) score / list.size()) * 100);
        progressBar.setProgress(progress);
    }
}