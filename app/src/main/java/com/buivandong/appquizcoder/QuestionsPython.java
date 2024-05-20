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
        list.add(new QuestionModel("Which is the most popular JavaScript framework?", "Angular", "React", "Svelte", "Vue", "React"));
        list.add(new QuestionModel("Which company invented React?", "Google", "Apple", "Netflix", "Facebook", "Facebook"));
        list.add(new QuestionModel("What's the fundamental building block of React apps?", "Components", "Blocks", "Elements", "Effects", "Components"));
        list.add(new QuestionModel("What's the name of the syntax we use to describe the UI in React components?", "FBJ", "Babel", "JSX", "ES2015", "JSX"));
        list.add(new QuestionModel("How does data flow naturally in React apps?", "From parents to children", "From children to parents", "Both ways", "The developers decides", "From parents to children"));
        list.add(new QuestionModel("How to pass data into a child component?", "State", "Props", "PropTypes", "Parameters", "Props"));
//        list.add(new QuestionModelReactJS("When to use derived state?", "Whenever the state should not trigger a re-render", "Whenever the state can be synchronized with an effect", "Whenever the state should be accessible to all components", "Whenever the state can be computed from another state variable", "Whenever the state can be computed from another state variable"));
//        list.add(new QuestionModelReactJS("What triggers a UI re-render in React?", "Running an effect", "Passing props", "Updating state", "Adding event listeners to DOM elements", "Updating state"));
//        list.add(new QuestionModelReactJS("When do we directly \\\"touch\\\" the DOM in React?", "When we need to listen to an event", "When we need to change the UI", "When we need to add styles", "Almost never", "Almost never"));
//        list.add(new QuestionModelReactJS("In what situation do we use a callback to update state?", "When updating the state will be slow", "When the updated state is very data-intensive", "When the state update should happen faster", "When the new state depends on the previous state", "When the new state depends on the previous state"));
//        list.add(new QuestionModelReactJS("If we pass a function to useState, when will that function be called?", "On each re-render", "Each time we update the state", "Only on the initial render", "The first time we update the state", "Only on the initial render"));
//        list.add(new QuestionModelReactJS("Which hook to use for an API request on the component's initial render?", "useState", "useEffect", "useRef", "useReducer", "useEffect"));
//        list.add(new QuestionModelReactJS("Which variables should go into the useEffect dependency array?", "Usually none", "All our state variables", "All state and props referenced in the effect", "All variables needed for clean up", "All state and props referenced in the effect"));
//        list.add(new QuestionModelReactJS("An effect will always run on the initial render.", "True", "It depends on the dependency array", "False", "In depends on the code in the effect", "True"));
//        list.add(new QuestionModelReactJS("When will an effect run if it doesn't have a dependency array?", "Only when the component mounts", "Only when the component unmounts", "The first time the component re-renders", "Each time the component is re-rendered", "Each time the component is re-rendered"));

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