package com.example.msquizapp;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Collections;
import java.util.List;

import static com.example.msquizapp.R.drawable.option_bg;


public class GameActivity extends AppCompatActivity {
    RadioButton buttonA, buttonB, buttonC, buttonD;

    TextView questionText, QuizText, timeText, resultText, coinText;
    QuizHelper JAVAQuizHelper;
    QuestionActivity currentQuestion;
    List<QuestionActivity> list;
    int qid = 0;
    int timeValue = 20;
    int coinValue = 0;
    CountDownTimer countDownTimer;
    Typeface fontone, fonttwo;
    MediaPlayer  playercorrect,playerwrong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_main);

        //Initializing variables
        questionText = (TextView) findViewById(R.id.Question);
        buttonA = (RadioButton) findViewById(R.id.buttonA);
        buttonB = (RadioButton) findViewById(R.id.buttonB);
        buttonC = (RadioButton) findViewById(R.id.buttonC);
        buttonD = (RadioButton) findViewById(R.id.buttonD);
        QuizText = (TextView) findViewById(R.id.QuizText);
        timeText = (TextView) findViewById(R.id.timeText);
        resultText = (TextView) findViewById(R.id.resultText);
        coinText = (TextView) findViewById(R.id.coinText);
        playercorrect = MediaPlayer.create(this,  R.raw.correct);
        playerwrong = MediaPlayer.create(this,R.raw.wrong);



        //Setting typefaces for textview and buttons - this will give stylish fonts on textview and button etc
        fontone = Typeface.createFromAsset(getAssets(), "fonts/font_two.ttf");
        fonttwo = Typeface.createFromAsset(getAssets(), "fonts/main-Bold.otf");
        QuizText.setTypeface(fonttwo);
        questionText.setTypeface(fontone);
        buttonA.setTypeface(fontone);
        buttonB.setTypeface(fontone);
        buttonC.setTypeface(fontone);
        buttonD.setTypeface(fontone);
        timeText.setTypeface(fontone);
        resultText.setTypeface(fontone);
        coinText.setTypeface(fontone);



        //Our database helper class
        JAVAQuizHelper = new QuizHelper(this);
        //Make db writable
        JAVAQuizHelper.getWritableDatabase();

        //It will check if the ques,options are already added in table or not
        //If they are not added then the getAllOfTheQuestions() will return a list of size zero
        if (JAVAQuizHelper.getAllOfTheQuestions().size() == 0) {
            //If not added then add the ques,options in table
            JAVAQuizHelper.allQuestion();
        }

        //This will return us a list of data type Question
        list = JAVAQuizHelper.getAllOfTheQuestions();

        //Now we gonna shuffle the elements of the list so that we will get questions randomly
        Collections.shuffle(list);

        //currentQuestion will hold the que, 4 option and ans for particular id
        currentQuestion = list.get(qid);

        //countDownTimer
        countDownTimer = new CountDownTimer(22000, 1000) {
            public void onTick(long millisUntilFinished) {

                //here you can have your logic to set text to timeText
                timeText.setText(String.valueOf(timeValue) + "\"");

                //With each iteration decrement the time by 1 sec
                timeValue -= 1;

                //This means the user is out of time so onFinished will called after this iteration
                if (timeValue == -1) {

                    //Since user is out of time setText as time up
                    resultText.setText(getString(R.string.timeup));

                    //Since user is out of time he won't be able to click any buttons
                    //therefore we will disable all four options buttons using this method
                    disableButton();
                }
            }

            //Now user is out of time
            public void onFinish() {
                //We will navigate him to the time up activity using below method
                timeUp();
            }
        }.start();

        //This method will set the que and four options
        updateQueAndOptions();


    }


    public void updateQueAndOptions() {

        //This method will setText for que and options
        questionText.setText(currentQuestion.getQuestion());
        buttonA.setText(currentQuestion.getOptA());
        buttonB.setText(currentQuestion.getOptB());
        buttonC.setText(currentQuestion.getOptC());
        buttonD.setText(currentQuestion.getOptD());

// Here You Can Adjust time value

        timeValue = 20;

        //Now since the user has ans correct just reset timer back for another que- by cancel and start
        countDownTimer.cancel();
        countDownTimer.start();

        //set the value of coin text
        coinText.setText(String.valueOf(coinValue));
        //Now since user has ans correct increment the coinvalue
        coinValue = coinValue+5;



    }


    //Onclick listener for first button

    public void buttonA(View view) {
        //Compare the ans
        if (currentQuestion.getOptA().equals(currentQuestion.getAnswer())) {
            buttonA.setBackgroundResource(option_bg);
            //Check Question limit
            if (qid < list.size() - 1) {

                //disable all option button since user ans is correct
                //user won't be able to press another option button after pressing one button
                disableButton();

                //if answer correct show dialog button for next
                correctDialog();
                buttonA.setChecked(false);
            }
            //IF User select all answer correct
            else {

                gameWon();

                buttonA.setChecked(false);

            }
        }
        //When user select wrong ans. then navigate to play again
        else {

                gameLostPlayAgain();
                buttonA.setChecked(false);
                playerwrong.start();

        }
    }


    // All Function same as Button A for navigation
    //Onclick listener for sec button
    public void buttonB(View view) {
        if (currentQuestion.getOptB().equals(currentQuestion.getAnswer())) {
            buttonB.setBackgroundResource(option_bg);

            if (qid < list.size() - 1) {
                disableButton();
                correctDialog();
                buttonB.setChecked(false);
            } else {
                gameWon();
                buttonB.setChecked(false);
            }
        } else {
            gameLostPlayAgain();
            buttonB.setChecked(false);
            playerwrong.start();
        }
    }

    //Onclick listener for third button
    public void buttonC(View view) {
        if (currentQuestion.getOptC().equals(currentQuestion.getAnswer())) {
            buttonC.setBackgroundResource(option_bg);
            if (qid < list.size() - 1) {
                disableButton();
                correctDialog();
                buttonC.setChecked(false);
            } else {
                gameWon();
                buttonC.setChecked(false);
            }
        } else {

            gameLostPlayAgain();
            buttonC.setChecked(false);
            playerwrong.start();
        }
    }

    //Onclick listener for fourth button

    public void buttonD(View view) {
        if (currentQuestion.getOptD().equals(currentQuestion.getAnswer())) {
            buttonD.setBackgroundResource(option_bg);
            if (qid < list.size() - 1) {
                disableButton();
                correctDialog();
                buttonD.setChecked(false);
            } else {
                gameWon();

                buttonD.setChecked(false);
            }
        } else {
            gameLostPlayAgain();
            buttonD.setChecked(false);
            playerwrong.start();
        }
    }




    //This method will navigate from current activity to GameWon
    public void gameWon() {
        Intent intent = new Intent(this, WonActivity.class);
        String text = coinText.getText().toString();
        intent.putExtra("mytext",text);
        startActivity(intent);
        finish();
    }

    //This method is called when user ans is wrong
    //this method will navigate user to the activity PlayAgain
    public void gameLostPlayAgain() {
        Intent intent = new Intent(this, GameOverActivity.class);
        startActivity(intent);
        finish();
    }

    //This method is called when time is up
    //this method will navigate user to the activity Time_Up
    public void timeUp() {
        Intent intent = new Intent(this, TimeOverActivity.class);
        startActivity(intent);
        finish();
    }

    //If user press home button and come in the game from memory then this
    //method will continue the timer from the previous time it left
    @Override
    protected void onRestart() {
        super.onRestart();
        countDownTimer.start();
    }

    //When activity is destroyed then this will cancel the timer
    @Override
    protected void onStop() {
        super.onStop();
        countDownTimer.cancel();
    }

    //This will pause the time
    @Override
    protected void onPause() {
        super.onPause();
        countDownTimer.cancel();
    }

    //On BackPressed
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    //This dialog is show to the user after he ans correct
    public void correctDialog() {
        final Dialog dialogCorrect = new Dialog(GameActivity.this);
        dialogCorrect.requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (dialogCorrect.getWindow() != null) {
            ColorDrawable colorDrawable = new ColorDrawable(Color.TRANSPARENT);
            dialogCorrect.getWindow().setBackgroundDrawable(colorDrawable);
        }
        dialogCorrect.setContentView(R.layout.dialog_correct);
        dialogCorrect.setCancelable(false);
        dialogCorrect.show();
        playercorrect.start();


        //Since the dialog is show to user just pause the timer in background
        onPause();


        TextView correctText = (TextView) dialogCorrect.findViewById(R.id.correctText);
        Button buttonNext = (Button) dialogCorrect.findViewById(R.id.dialogNext);

        //Setting type faces
        correctText.setTypeface(fonttwo);
        buttonNext.setTypeface(fonttwo);

        //OnCLick listener to go next que
        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //This will dismiss the dialog
                dialogCorrect.dismiss();
                //it will increment the question number
                qid++;
                //get the que and 4 option and store in the currentQuestion
                currentQuestion = list.get(qid);
                //Now this method will set the new que and 4 options
                updateQueAndOptions();
                //reset the color of buttons back to white
                resetColor();
                //Enable button - remember we had disable them when user ans was correct in there particular button methods
                enableButton();
            }
        });
    }


    //This method will make button color white again since our one button color was turned green
    public void resetColor() {
        buttonA.setBackgroundResource(R.drawable.white_option_bg);
        buttonB.setBackgroundResource(R.drawable.white_option_bg);
        buttonC.setBackgroundResource(R.drawable.white_option_bg);
        buttonD.setBackgroundResource(R.drawable.white_option_bg);
    }

    //This method will disable all the option button
    public void disableButton() {
        buttonA.setEnabled(false);
        buttonB.setEnabled(false);
        buttonC.setEnabled(false);
        buttonD.setEnabled(false);
    }

    //This method will all enable the option buttons
    public void enableButton() {
        buttonA.setEnabled(true);
        buttonB.setEnabled(true);
        buttonC.setEnabled(true);
        buttonD.setEnabled(true);
    }


    //For Correct Answer Play Music


    public void setPlayer(MediaPlayer player) {
        player.setLooping(true);
        player.setVolume(100, 100);

    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        playercorrect.start();
        playerwrong.start();
        return 1;
    }


}
