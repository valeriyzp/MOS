package com.example.mos_lab4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Game extends AppCompatActivity implements View.OnTouchListener {
    private LinearLayout GameLayout;
    private GameView gameView;

    private ImageButton RestartButton;
    private ImageButton MenuButton;

    private LinearLayout GreyLayout;
    private TextView GameResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        GreyLayout = (LinearLayout) findViewById(R.id.GreyForeground);
        GameResults = (TextView) findViewById(R.id.GameResults);

        RestartButton = (ImageButton) findViewById(R.id.RestartButton);
        MenuButton = (ImageButton) findViewById(R.id.MenuButton);
        RestartButton.setOnTouchListener(this);
        MenuButton.setOnTouchListener(this);

        GameLayout = (LinearLayout) findViewById(R.id.GameLayout);
        GameLayout.setOnTouchListener(this);

        startGame();
    }

    public void startGame() {
        GreyLayout.setVisibility(View.GONE);
        GameLayout.setEnabled(true);

        if(gameView != null) {
            GameLayout.removeView(gameView);
            gameView.destroyDrawingCache();
        }

        gameView = new GameView(this);
        GameLayout.addView(gameView);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(v.getId() == R.id.RestartButton && event.getAction() == MotionEvent.ACTION_DOWN) {
            startGame();
        }
        else if(v.getId() == R.id.MenuButton && event.getAction() == MotionEvent.ACTION_DOWN) {
            Intent gameMenu = new Intent(this, MainMenu.class);
            gameMenu.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(gameMenu);
            finish();
            overridePendingTransition(0, 0);
        }
        else if(v.getId() == R.id.GameLayout && event.getAction() == MotionEvent.ACTION_DOWN)
            gameView.performTouch();

        return false;
    }

    public void showGameOver(int Score) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                GameLayout.setEnabled(false);
                GreyLayout.setVisibility(View.VISIBLE);
                GameResults.setText(getString(R.string.game_results, Score));
            }
        });
    }
}
