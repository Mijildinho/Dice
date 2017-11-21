package com.example.gee.dice;


import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;

import java.util.Random;


public class DiceActivity extends AppCompatActivity {
    private int userScore = 0;
    private int userTurn = 0;
    private int compScore = 0;
    private int compTurn = 0;
    private int winscore = 100;
    private Button rollButton, holdButton, resetButton;
    private TextView yourTextView, compTextView, turnText, turnScore;
    private ImageView diceImageView;
    private int[] imageArray = {R.drawable.dice1, R.drawable.dice2, R.drawable.dice3,
            R.drawable.dice4, R.drawable.dice5, R.drawable.dice6 };
    private Random rand = new Random();
    private boolean myTurn = true;
    long startTime = 0;
    private int arrayIndex = 9000;


    //runs without a timer by reposting this handler at the end of the runnable
    Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            timerHandler.postDelayed(this, 1000);
            randomSet();
            Log.i("FOOD", String.valueOf(userTurn));
            if(userTurn>= 20){
                userTurn = 0;
                onPause();
                setScore();
            }
            compTurn = userTurn;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dice);

        rollButton = (Button) findViewById(R.id.rollButton);
        holdButton = (Button) findViewById(R.id.holdButton);
        resetButton = (Button) findViewById(R.id.resetButton);
        yourTextView = (TextView) findViewById(R.id.your_score);
        compTextView = (TextView) findViewById(R.id.pc_score);
        turnText = (TextView) findViewById(R.id.turnText);
        turnScore = (TextView) findViewById(R.id.turnScore);
        diceImageView = (ImageView) findViewById(R.id.DiceImage);

        rollButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                turnText.setVisibility(View.VISIBLE);
                turnScore.setVisibility(View.VISIBLE);
                randomSet();

            }
        });

        holdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                rolledOne();

                if ((userScore>winscore)||(compScore>winscore))gameOver();
            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                turnText.setVisibility(View.INVISIBLE);
                turnScore.setVisibility(View.INVISIBLE);
                enableButtons();
                updateTurnText();
                yourTextView.setText("0");
                compTextView.setText("0");
                onPause();
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        timerHandler.removeCallbacks(timerRunnable);
    }

    public void computerTurn() {
        startTime = System.currentTimeMillis();
        timerHandler.postDelayed(timerRunnable, 0);
        //timerHandler.postDelayed(testing, 10000);
    }

    public void rolledOne() {
        if(myTurn){
            setScore();
            computerTurn();
        }else
            setScore();
    }
    public void setScore() {
        if(myTurn){
            userScore += userTurn;
            yourTextView.setText(String.valueOf(userScore));
            myTurn = false;
            turnText.setText("Comp turn score: ");
            disableButtons();
            turnScore.setText("0");
            userTurn = 0;
        }else {
            if(arrayIndex == 0){
                myTurn = true;
                turnText.setText("Your turn score: ");
                enableButtons();
                turnScore.setText("0");
                userTurn = 0;
                compTurn = 0;
                onPause();
            }else {
                compScore += compTurn;
                compTextView.setText(String.valueOf(compScore));
                myTurn = true;
                turnText.setText("Your turn score: ");
                enableButtons();
                turnScore.setText("0");
                userTurn = 0;
                compTurn = 0;
                onPause();
            }
        }
        if (userScore >= winscore){
            myTurn = false;
            yourTextView.setText(String.valueOf(userScore));
            turnText.setText("You win!");
            onPause();
        }else if(compScore>winscore){
            myTurn = false;
            compTextView.setText(String.valueOf(compScore));
            turnText.setText("Computer win!");
            onPause();
        }
    }
    public void updateTurnText() {
        if(myTurn) {
            turnText.setText("Comp turn score: ");
            myTurn = false;
        }
        else {
            turnText.setText("Your turn score: ");
            myTurn = true;
        }

    }
    public void enableButtons() {
        rollButton.setEnabled(true);
        holdButton.setEnabled(true);
    }
    public void disableButtons() {
        rollButton.setEnabled(false);
        holdButton.setEnabled(false);
    }
    public void randomSet() {
        arrayIndex = rand.nextInt(imageArray.length);
        int drawableInt = imageArray[arrayIndex];
        Drawable newDice = getResources().getDrawable(drawableInt);
        diceImageView.setImageDrawable(newDice);
        if (arrayIndex == 0) {
            rolledOne();
        }else {
            userTurn += arrayIndex+1;
            turnScore.setText(String.valueOf(userTurn));
        }
    }

    public void gameOver() {
        // disable roll & hold buttons
        Button rollButton = (Button) findViewById(R.id.rollButton);
        rollButton.setEnabled(false);
        Button holdButton = (Button) findViewById(R.id.holdButton);
        holdButton.setEnabled(false);

    }
    }


