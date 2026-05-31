package com.example.mini_game;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;

public class PongGameViewEnhanced extends View {
    private Paint paint;
    private int screenWidth, screenHeight;
    private int paddle1Y, paddle2Y;
    private int ballX, ballY;
    private int ballSpeedX = 12, ballSpeedY = 12;
    private int score1 = 0, score2 = 0;
    private float touch1Y = 0, touch2Y = 0;
    private int level = 1;
    private int difficulty = 1;
    private static final int PADDLE_HEIGHT = 150;
    private static final int PADDLE_WIDTH = 20;
    private static final int BALL_SIZE = 20;
    
    public PongGameViewEnhanced(Context context) {
        super(context);
        paint = new Paint();
        setBackgroundColor(Color.BLACK);
    }
    
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        screenWidth = w;
        screenHeight = h;
        paddle1Y = h / 2 - PADDLE_HEIGHT / 2;
        paddle2Y = h / 2 - PADDLE_HEIGHT / 2;
        ballX = w / 2;
        ballY = h / 2;
    }
    
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        
        // Draw center line
        paint.setColor(Color.GRAY);
        for (int i = 0; i < screenHeight; i += 20) {
            canvas.drawRect(screenWidth / 2 - 2, i, screenWidth / 2 + 2, i + 10, paint);
        }
        
        // Draw paddles with gradient
        paint.setColor(Color.GREEN);
        canvas.drawRect(20, paddle1Y, 20 + PADDLE_WIDTH, paddle1Y + PADDLE_HEIGHT, paint);
        canvas.drawRect(screenWidth - 40, paddle2Y, screenWidth - 20, paddle2Y + PADDLE_HEIGHT, paint);
        
        // Draw ball with glow
        paint.setColor(Color.YELLOW);
        canvas.drawCircle(ballX, ballY, BALL_SIZE / 2, paint);
        paint.setColor(Color.rgb(255, 200, 0));
        canvas.drawCircle(ballX, ballY, BALL_SIZE / 2 - 3, paint);
        
        // Draw scores
        paint.setColor(Color.WHITE);
        paint.setTextSize(60);
        canvas.drawText(String.valueOf(score1), screenWidth / 4 - 30, 80, paint);
        canvas.drawText(String.valueOf(score2), 3 * screenWidth / 4 - 30, 80, paint);
        
        // Draw level and difficulty
        paint.setTextSize(20);
        canvas.drawText("Level: " + level, 20, screenHeight - 20, paint);
        canvas.drawText("Difficulty: " + difficulty, screenWidth - 150, screenHeight - 20, paint);
        
        // Update ball
        ballX += ballSpeedX;
        ballY += ballSpeedY;
        
        // Ball collision with top/bottom
        if (ballY <= BALL_SIZE / 2 || ballY >= screenHeight - BALL_SIZE / 2) {
            ballSpeedY = -ballSpeedY;
        }
        
        // Ball collision with paddles
        if (ballX <= 40 && ballY >= paddle1Y && ballY <= paddle1Y + PADDLE_HEIGHT) {
            ballSpeedX = -ballSpeedX;
            ballSpeedX += difficulty;
        }
        if (ballX >= screenWidth - 40 && ballY >= paddle2Y && ballY <= paddle2Y + PADDLE_HEIGHT) {
            ballSpeedX = -ballSpeedX;
            ballSpeedX -= difficulty;
        }
        
        // Scoring
        if (ballX < 0) {
            score2++;
            checkLevelUp();
            resetBall();
        }
        if (ballX > screenWidth) {
            score1++;
            checkLevelUp();
            resetBall();
        }
        
        // Move paddles
        if (touch1Y > 0) {
            paddle1Y = (int) touch1Y - PADDLE_HEIGHT / 2;
            if (paddle1Y < 0) paddle1Y = 0;
            if (paddle1Y + PADDLE_HEIGHT > screenHeight) paddle1Y = screenHeight - PADDLE_HEIGHT;
        }
        if (touch2Y > 0) {
            paddle2Y = (int) touch2Y - PADDLE_HEIGHT / 2;
            if (paddle2Y < 0) paddle2Y = 0;
            if (paddle2Y + PADDLE_HEIGHT > screenHeight) paddle2Y = screenHeight - PADDLE_HEIGHT;
        }
        
        invalidate();
    }
    
    private void checkLevelUp() {
        int totalScore = score1 + score2;
        if (totalScore > 0 && totalScore % 5 == 0) {
            level++;
            difficulty++;
            ballSpeedX += 2;
            ballSpeedY += 1;
        }
    }
    
    private void resetBall() {
        ballX = screenWidth / 2;
        ballY = screenHeight / 2;
        ballSpeedX = -ballSpeedX;
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        
        if (x < screenWidth / 2) {
            touch1Y = y;
        } else {
            touch2Y = y;
        }
        return true;
    }
}