package com.example.simongame;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

public class WelcomeActivity extends AppCompatActivity {
    private EditText usernameInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        usernameInput = findViewById(R.id.usernameInput);
        Button startGameButton = findViewById(R.id.startGameButton);

        startGameButton.setOnClickListener(v -> {
            String username = usernameInput.getText().toString().trim();
            if (username.isEmpty()) {
                usernameInput.setError("Please enter your name!");
            } else if (username.contains(":") || username.contains(",")) {
                usernameInput.setError("Name cannot contain ':' or ','");
            } else {
                // Save username
                SharedPreferences prefs = getSharedPreferences("SimonGame", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("CURRENT_USERNAME", username);
                editor.apply();

                // Start GameActivity
                Intent intent = new Intent(WelcomeActivity.this, GameActivity.class);
                startActivity(intent);
            }
        });
    }
}
