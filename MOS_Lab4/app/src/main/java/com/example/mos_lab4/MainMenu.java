package com.example.mos_lab4;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;

public class MainMenu extends AppCompatActivity implements View.OnTouchListener {

    protected ImageButton StartButton;
    protected ImageButton CloseButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        StartButton = (ImageButton) findViewById(R.id.StartButton);
        CloseButton = (ImageButton) findViewById(R.id.CloseButton);
        StartButton.setOnTouchListener(this);
        CloseButton.setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(v.getId() == R.id.StartButton && event.getAction() == MotionEvent.ACTION_DOWN)
        {
            Intent game = new Intent(this, Game.class);
            game.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(game);
            finish();
            overridePendingTransition(0, 0);
        }
        if(v.getId() == R.id.CloseButton && event.getAction() == MotionEvent.ACTION_DOWN){
            closeDialog();
        }

        return false;
    }

    protected void closeDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you want to close Game Bird?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.setTitle("Game Bird close");
        alert.show();
    }
}
