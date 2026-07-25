package com.example.simongame;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.Locale;

public class ScoreboardActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scoreboard);

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                exitGame();
            }
        });

        ListView scoreList = findViewById(R.id.scoreList);
        Button playAgainButton = findViewById(R.id.playAgainButton);
        Button exitButton = findViewById(R.id.exitButton);

        ArrayList<String> scores = loadScores();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, 
            R.layout.score_item, R.id.playerNameText, scores) {
            @NonNull
            @Override
            public View getView(int position, View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                String[] parts = scores.get(position).split(":");
                String playerName = parts[0].trim();
                String score = parts[1].trim();

                TextView rankText = view.findViewById(R.id.rankText);
                TextView playerNameText = view.findViewById(R.id.playerNameText);
                TextView scoreText = view.findViewById(R.id.scoreText);

                rankText.setText(String.format(Locale.getDefault(), "#%d", position + 1));
                playerNameText.setText(playerName);
                scoreText.setText(score);
                return view;
            }
        };

        scoreList.setAdapter(adapter);
        playAgainButton.setOnClickListener(v -> restartGame());
        exitButton.setOnClickListener(v -> exitGame());
    }

    private void restartGame() {
        Intent intent = new Intent(ScoreboardActivity.this, GameActivity.class);
        startActivity(intent);
        finish();
    }

    private void exitGame() {
        finishAffinity();
    }

    private ArrayList<String> loadScores() {
        SharedPreferences prefs = getSharedPreferences("SimonGame", MODE_PRIVATE);
        String scoresString = prefs.getString("SCORES", "");
        String[] entries = scoresString.split(",");

        ArrayList<String> scores = new ArrayList<>();
        for (String entry : entries) {
            if (!entry.isEmpty()) {
                String[] parts = entry.split(":");
                if (parts.length == 2) {
                    String username = parts[0].trim();
                    String points = parts[1].trim();
                    scores.add(String.format(Locale.getDefault(), "%s: %s points", username, points));
                }
            }
        }

        scores.sort((o1, o2) -> {
            try {
                int score1 = Integer.parseInt(o1.split(": ")[1].replace(" points", ""));
                int score2 = Integer.parseInt(o2.split(": ")[1].replace(" points", ""));
                return Integer.compare(score2, score1);
            } catch (Exception e) {
                return 0;
            }
        });

        return new ArrayList<>(scores.subList(0, Math.min(scores.size(), 10)));
    }
}
