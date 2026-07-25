package com.example.simongame;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.Random;

public class GameActivity extends AppCompatActivity {
    private ArrayList<Integer> gameSequence;
    private ArrayList<Integer> playerSequence;
    private Button[] colorButtons;
    private TextView scoreText;
    private int score = 0;
    private boolean isPlayingSequence = false;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private String username;
    private Button startButton;
    private boolean gameStarted = false;
    private boolean isMuted = false;
    private ImageButton muteButton;
    private ToneGenerator toneGenerator;
    private final int[] TONES = {
            ToneGenerator.TONE_DTMF_1, // Red
            ToneGenerator.TONE_DTMF_2, // Green
            ToneGenerator.TONE_DTMF_3, // Blue
            ToneGenerator.TONE_DTMF_4  // Yellow
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // Initialize ToneGenerator
        toneGenerator = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);

        // Get username from SharedPreferences
        SharedPreferences prefs = getSharedPreferences("SimonGame", MODE_PRIVATE);
        username = prefs.getString("CURRENT_USERNAME", "Unknown");

        // Initialize views
        scoreText = findViewById(R.id.scoreText);
        colorButtons = new Button[] {
                findViewById(R.id.redButton),
                findViewById(R.id.greenButton),
                findViewById(R.id.blueButton),
                findViewById(R.id.yellowButton)
        };

        // Initialize mute button
        muteButton = findViewById(R.id.muteButton);
        muteButton.setOnClickListener(v -> toggleMute());

        // Initialize game variables
        gameSequence = new ArrayList<>();
        playerSequence = new ArrayList<>();

        // Set up start button
        startButton = findViewById(R.id.startButton);
        startButton.setOnClickListener(v -> {
            if (!gameStarted) {
                startNewGame();
                gameStarted = true;
                startButton.setEnabled(false);
            }
        });

        // Set up color button listeners
        for (int i = 0; i < colorButtons.length; i++) {
            final int buttonIndex = i;
            colorButtons[i].setOnClickListener(v -> onPlayerInput(buttonIndex));
        }

        // Disable color buttons initially
        for (Button button : colorButtons) {
            button.setEnabled(false);
        }

        // Initialize score display
        updateScoreDisplay();
    }

    private void toggleMute() {
        isMuted = !isMuted;
        muteButton.setImageResource(isMuted ? R.drawable.ic_volume_off : R.drawable.ic_volume_up);
    }

    private void playSound(int buttonIndex) {
        if (!isMuted && toneGenerator != null) {
            toneGenerator.startTone(TONES[buttonIndex], 300);
        }
    }

    private void playErrorSound() {
        if (!isMuted && toneGenerator != null) {
            toneGenerator.startTone(ToneGenerator.TONE_PROP_BEEP, 1000);
        }
    }

    private void startNewGame() {
        gameSequence.clear();
        playerSequence.clear();
        score = 0;
        updateScoreDisplay();

        // Enable color buttons
        for (Button button : colorButtons) {
            button.setEnabled(true);
        }
        addToSequence();
    }

    private void addToSequence() {
        gameSequence.add(new Random().nextInt(4));
        playSequence();
    }

    private void playSequence() {
        isPlayingSequence = true;
        playerSequence.clear();
        int sequenceSize = gameSequence.size();
        for (int i = 0; i < sequenceSize; i++) {
            final int index = i;
            handler.postDelayed(() -> highlightButton(gameSequence.get(index)), 1000L * index);
        }
        // Release the input lock AFTER the last highlight's 500ms flash animation fully completes
        handler.postDelayed(() -> isPlayingSequence = false, 1000L * (sequenceSize - 1) + 600);
    }

    private void highlightButton(int buttonIndex) {
        Button button = colorButtons[buttonIndex];
        button.setAlpha(0.5f);
        playSound(buttonIndex);
        handler.postDelayed(() -> button.setAlpha(1.0f), 500);
    }

    private void onPlayerInput(int buttonIndex) {
        if (isPlayingSequence) return;

        playerSequence.add(buttonIndex);
        highlightButton(buttonIndex);

        if (buttonIndex != gameSequence.get(playerSequence.size() - 1)) {
            gameOver();
            return;
        }

        if (playerSequence.size() == gameSequence.size()) {
            score++;
            updateScoreDisplay();
            handler.postDelayed(this::addToSequence, 1000);
        }
    }

    private void updateScoreDisplay() {
        scoreText.setText(getString(R.string.score_text, score));
    }

    private void gameOver() {
        playErrorSound();
        // Disable all buttons
        for (Button button : colorButtons) {
            button.setEnabled(false);
        }

        // Save score
        SharedPreferences prefs = getSharedPreferences("SimonGame", MODE_PRIVATE);
        String existingScores = prefs.getString("SCORES", "");
        String newScore = username + ":" + score;
        String updatedScores = existingScores.isEmpty() ? newScore : existingScores + "," + newScore;

        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("SCORES", updatedScores);
        editor.apply();

        // Show game over message
        Toast.makeText(this, getString(R.string.game_over, score), Toast.LENGTH_SHORT).show();

        // Re-enable start button
        startButton.setEnabled(true);
        gameStarted = false;

        // Transition to scoreboard
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            Intent intent = new Intent(GameActivity.this, ScoreboardActivity.class);
            startActivity(intent);
            finish();
        }, 1000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
        if (toneGenerator != null) {
            toneGenerator.release();
        }
    }
}