package com.example.mini_game;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;

public class PongGameView extends View {
    private Paint paint;
    private int screenWidth, screenHeight;
    private int paddle1Y, paddle2Y;
    private int ballX, ballY;
    private int ballSpeedX = 10, ballSpeedY = 10;
    private int score1 = 0, score2 = 0;
    private float touch1Y = 0, touch2Y = 0;
    private static final int PADDLE_HEIGHT = 150;
    private static final int PADDLE_WIDTH = 20;
    private static final int BALL_SIZE = 20;
    
    public PongGameView(Context context) {
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
        
        // Draw paddles
        paint.setColor(Color.WHITE);
        canvas.drawRect(20, paddle1Y, 20 + PADDLE_WIDTH, paddle1Y + PADDLE_HEIGHT, paint);
        canvas.drawRect(screenWidth - 40, paddle2Y, screenWidth - 20, paddle2Y + PADDLE_HEIGHT, paint);
        
        // Draw ball
        canvas.drawCircle(ballX, ballY, BALL_SIZE / 2, paint);
        
        // Draw scores
        paint.setTextSize(50);
        canvas.drawText(String.valueOf(score1), screenWidth / 4, 80, paint);
        canvas.drawText(String.valueOf(score2), 3 * screenWidth / 4, 80, paint);
        
        // Update ball position
        ballX += ballSpeedX;
        ballY += ballSpeedY;
        
        // Ball collision with top/bottom
        if (ballY <= BALL_SIZE / 2 || ballY >= screenHeight - BALL_SIZE / 2) {
            ballSpeedY = -ballSpeedY;
        }
        
        // Ball collision with paddles
        if (ballX <= 40 && ballY >= paddle1Y && ballY <= paddle1Y + PADDLE_HEIGHT) {
            ballSpeedX = -ballSpeedX;
        }
        if (ballX >= screenWidth - 40 && ballY >= paddle2Y && ballY <= paddle2Y + PADDLE_HEIGHT) {
            ballSpeedX = -ballSpeedX;
        }
        
        // Scoring
        if (ballX < 0) {
            score2++;
            resetBall();
        }
        if (ballX > screenWidth) {
            score1++;
            resetBall();
        }
        
        // Move paddles based on touch
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