package com.yaronfuks.mathbraintrainer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private static final String MESSAGE_ID = "messages_prefs";
    private int startCurrent = 0;
    private TextView correctQuestionTextView;
    private TextView TotalQuestionTextView;
    private TextView timer;
    private TextView bestScoreEverScore;
    private TextView questionTextView;
    private Button playAgainButton;
    private TextView resultTextView;
    private ArrayList<Integer> answers = new ArrayList<>();
    private int locationOfCorrectAnswer;
    private int score = 0;
    private int numberOfQuestions;
    private boolean gameIsActive = true;
    private SoundPool soundPool;
    private int endGameSound;
    private AudioAttributes audioAttributes;


    private Button button0;
    private Button button1;
    private Button button2;
    private Button button3;

    public void playAgain(View view) {
        gameIsActive = true;
        score = 0;
        numberOfQuestions = 0;

        timer.setText("60s");
        correctQuestionTextView.setText("Correct: " + startCurrent);

        resultTextView.setText("");
        playAgainButton.setVisibility(View.INVISIBLE);
        generateQuestion();

        new CountDownTimer(60100, 1000) {

            @Override
            public void onTick(long l) {
                timer.setText(String.valueOf(l / 1000) + "s");
            }

            @Override
            public void onFinish() {
                soundPool.play(endGameSound, 1, 1, 0, 0, 1);
                gameIsActive = false;
                playAgainButton.setVisibility(View.VISIBLE);
                timer.setText("0s");
                resultTextView.setText("Your Score is: " + Integer.toString(score));

                int currentScore = score;

                String bestScore = bestScoreEverScore.getText().toString();
                int bestScoreInt = Integer.parseInt(bestScore);

                if (currentScore > bestScoreInt) {

                    String message = String.valueOf(currentScore);
                    SharedPreferences sharedPreferences = getSharedPreferences(MESSAGE_ID, MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("message", message);

                    editor.apply(); //saving to disk
                    bestScoreEverScore.setText(String.valueOf(currentScore));
                }


            }
        }.start();


    }


    public void generateQuestion() {

        Random random = new Random();

        int a = random.nextInt(21);
        int b = random.nextInt(21);

        questionTextView.setText(Integer.toString(a) + "+" + Integer.toString(b));

        locationOfCorrectAnswer = random.nextInt(4);

        answers.clear();

        int incorrectAnswer;

        for (int i = 0; i < 4; i++) {
            if (i == locationOfCorrectAnswer) {

                answers.add(a + b);

            } else {

                incorrectAnswer = random.nextInt(41);

                while (incorrectAnswer == a + b) {

                    incorrectAnswer = random.nextInt(41);
                }
                answers.add(incorrectAnswer);
            }

        }

        button0.setText(Integer.toString(answers.get(0)));
        button1.setText(Integer.toString(answers.get(1)));
        button2.setText(Integer.toString(answers.get(2)));
        button3.setText(Integer.toString(answers.get(3)));


    }


    public void chooseAnswer(View view) {

        if (gameIsActive) {
            if (view.getTag().toString().equals(Integer.toString(locationOfCorrectAnswer))) {

                score++;
                resultTextView.setText("Correct!");

            } else {
                resultTextView.setText("Wrong!");

            }

            numberOfQuestions++;
            correctQuestionTextView.setText("Correct: " + Integer.toString(score));
            TotalQuestionTextView.setText("Total: " + Integer.toString(numberOfQuestions));
            generateQuestion();
        }


    }

    public void start() {
        playAgain(findViewById(R.id.playAgainButton));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();

        correctQuestionTextView = findViewById(R.id.correctQuestionTextView);
        TotalQuestionTextView = findViewById(R.id.TotalQuestionTextView);
        timer = findViewById(R.id.timer);
        bestScoreEverScore = findViewById(R.id.bestScoreEverScore);
        questionTextView = findViewById(R.id.questionTextView);

        button0 = findViewById(R.id.button0);
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        resultTextView = findViewById(R.id.resultTextView);
        playAgainButton = findViewById(R.id.playAgainButton);

        audioAttributes = new AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                .build();

        soundPool = new SoundPool.Builder()
                .setMaxStreams(1)
                .setAudioAttributes(audioAttributes)
                .build();

        endGameSound = soundPool.load(this, R.raw.end_game_sound, 1);

        SharedPreferences getShareData = getSharedPreferences(MESSAGE_ID, MODE_PRIVATE);
        String value = getShareData.getString("message", "0");

        bestScoreEverScore.setText(value);

        start();

    }
}
