package com.example.mos_lab3;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    int Clicks = 0;

    TextView ClicksCount;
    Button ClicksButton;
    TextView TextMessage;
    EditText NewTextMessage;
    Button SetMessageButton;
    Button ShowToastButton;
    Button CloseAppButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initElements();

        ClicksCount.setText(getString(R.string.count_of_clicks, Clicks));
        TextMessage.setText(getString(R.string.default_message));
        NewTextMessage.setText("");
        SetMessageButton.setEnabled(false);

        setClickListeners();
    }

    protected void initElements()
    {
        ClicksCount = (TextView) findViewById(R.id.ClicksCount);
        ClicksButton = (Button) findViewById(R.id.ClicksButton);
        TextMessage = (TextView) findViewById(R.id.TextMessage);
        NewTextMessage = (EditText) findViewById(R.id.NewTextMessage);
        SetMessageButton = (Button) findViewById(R.id.SetMessageButton);
        ShowToastButton = (Button) findViewById(R.id.ShowToastButton);
        CloseAppButton = (Button) findViewById(R.id.CloseAppButton);
    }

    protected void setClickListeners()
    {
        ClicksButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    incrementClicks();
                }
                return false;
            }
        });

        NewTextMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                ;
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ;
            }

            @Override
            public void afterTextChanged(Editable s) {
                SetMessageButton.setEnabled(s.toString().length() != 0 && !s.toString().equals(TextMessage.getText().toString()));
            }
        });

        SetMessageButton.setOnClickListener(buttonClickListener);
        ShowToastButton.setOnClickListener(buttonClickListener);
        CloseAppButton.setOnClickListener(buttonClickListener);
    }

    View.OnClickListener buttonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch(view.getId()) {
                case R.id.SetMessageButton:
                    setNewMessage();
                    break;
                case R.id.ShowToastButton:
                    showToastMessage();
                    break;
                case R.id.CloseAppButton:
                    showAlertDialog();
                    break;
            }
        }
    };

    protected void incrementClicks()
    {
        Clicks += 1;
        ClicksCount.setText(getString(R.string.count_of_clicks, Clicks));
    }

    protected void setNewMessage()
    {
        if(NewTextMessage.getText().toString().length() != 0) {
            TextMessage.setText(NewTextMessage.getText().toString());
            NewTextMessage.setText("");
        }
    }

    protected void showToastMessage()
    {
        Toast.makeText(this, TextMessage.getText().toString(), Toast.LENGTH_SHORT).show();
    }

    protected void showAlertDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you want to close app?")
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
        alert.setTitle("App close");
        alert.show();
    }
}
