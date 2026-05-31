package com.example.mini_game;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;
import java.util.ArrayList;
import java.util.Random;

public class SpaceInvadersView extends View {
    private Paint paint;
    private int screenWidth, screenHeight;
    private int playerX, playerY;
    private ArrayList<int[]> bullets = new ArrayList<>();
    private ArrayList<int[]> enemies = new ArrayList<>();
    private int score = 0;
    private float touchX = 0;
    private Random random = new Random();
    private static final int PLAYER_SIZE = 60;
    private static final int BULLET_SIZE = 10;
    private static final int ENEMY_SIZE = 50;
    private int enemySpeed = 3;
    private int spawnCounter = 0;
    
    public SpaceInvadersView(Context context) {
        super(context);
        paint = new Paint();
        setBackgroundColor(Color.BLACK);
    }
    
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        screenWidth = w;
        screenHeight = h;
        playerX = w / 2 - PLAYER_SIZE / 2;
        playerY = h - 150;
        
        // Create initial enemies
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                enemies.add(new int[]{100 + j * 100, 100 + i * 80});
            }
        }
    }
    
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        
        // Draw player
        paint.setColor(Color.GREEN);
        canvas.drawRect(playerX, playerY, playerX + PLAYER_SIZE, playerY + PLAYER_SIZE, paint);
        
        // Draw bullets
        paint.setColor(Color.YELLOW);
        for (int i = bullets.size() - 1; i >= 0; i--) {
            int[] bullet = bullets.get(i);
            bullet[1] -= 15;
            if (bullet[1] < 0) {
                bullets.remove(i);
            } else {
                canvas.drawRect(bullet[0], bullet[1], bullet[0] + BULLET_SIZE, bullet[1] + BULLET_SIZE, paint);
            }
        }
        
        // Draw enemies
        paint.setColor(Color.RED);
        for (int i = enemies.size() - 1; i >= 0; i--) {
            int[] enemy = enemies.get(i);
            enemy[0] += enemySpeed;
            
            // Check boundaries
            if (enemy[0] <= 0 || enemy[0] >= screenWidth - ENEMY_SIZE) {
                enemySpeed = -enemySpeed;
                enemy[1] += 50;
            }
            
            // Game over if enemy reaches bottom
            if (enemy[1] >= playerY) {
                paint.setColor(Color.WHITE);
                paint.setTextSize(80);
                canvas.drawText("GAME OVER", screenWidth / 4, screenHeight / 2, paint);
                return;
            }
            
            canvas.drawRect(enemy[0], enemy[1], enemy[0] + ENEMY_SIZE, enemy[1] + ENEMY_SIZE, paint);
        }
        
        // Move player
        if (touchX > 0) {
            playerX = (int) touchX - PLAYER_SIZE / 2;
            if (playerX < 0) playerX = 0;
            if (playerX + PLAYER_SIZE > screenWidth) playerX = screenWidth - PLAYER_SIZE;
        }
        
        // Check collisions
        checkCollisions();
        
        // Draw score
        paint.setColor(Color.WHITE);
        paint.setTextSize(40);
        canvas.drawText("Score: " + score, 20, 50, paint);
        
        // Win condition
        if (enemies.isEmpty()) {
            paint.setColor(Color.GREEN);
            paint.setTextSize(80);
            canvas.drawText("YOU WIN!", screenWidth / 4, screenHeight / 2, paint);
        }
        
        invalidate();
    }
    
    private void checkCollisions() {
        for (int i = bullets.size() - 1; i >= 0; i--) {
            int[] bullet = bullets.get(i);
            for (int j = enemies.size() - 1; j >= 0; j--) {
                int[] enemy = enemies.get(j);
                if (bullet[0] < enemy[0] + ENEMY_SIZE && 
                    bullet[0] + BULLET_SIZE > enemy[0] &&
                    bullet[1] < enemy[1] + ENEMY_SIZE && 
                    bullet[1] + BULLET_SIZE > enemy[1]) {
                    bullets.remove(i);
                    enemies.remove(j);
                    score += 100;
                    break;
                }
            }
        }
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE) {
            touchX = event.getX();
            
            // Shoot on tap
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                bullets.add(new int[]{playerX + PLAYER_SIZE / 2, playerY});
            }
        }
        return true;
    }
}