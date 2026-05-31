package com.example.mini_game;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class PongActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new PongGameViewEnhanced(this));
    }
}