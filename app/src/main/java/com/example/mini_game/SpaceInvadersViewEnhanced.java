package com.example.mini_game;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;
import java.util.ArrayList;

public class SpaceInvadersViewEnhanced extends View {
    private Paint paint;
    private int screenWidth, screenHeight;
    private int playerX, playerY;
    private ArrayList<int[]> bullets = new ArrayList<>();
    private ArrayList<int[]> enemies = new ArrayList<>();
    private ArrayList<int[]> powerUps = new ArrayList<>();
    private int score = 0;
    private int level = 1;
    private int enemiesKilled = 0;
    private float touchX = 0;
    private int enemySpeed = 3;
    private int spawnCounter = 0;
    private boolean hasShield = false;
    private int shieldTime = 0;
    private static final int PLAYER_SIZE = 60;
    private static final int BULLET_SIZE = 10;
    private static final int ENEMY_SIZE = 50;
    private static final int POWERUP_SIZE = 30;
    
    public SpaceInvadersViewEnhanced(Context context) {
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
        
        createEnemyWave();
    }
    
    private void createEnemyWave() {
        enemies.clear();
        int cols = 4 + level;
        int rows = 2 + (level / 2);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                enemies.add(new int[]{100 + j * 80, 50 + i * 60});
            }
        }
    }
    
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        
        // Draw player
        paint.setColor(Color.GREEN);
        canvas.drawRect(playerX, playerY, playerX + PLAYER_SIZE, playerY + PLAYER_SIZE, paint);
        
        // Draw shield if active
        if (hasShield) {
            paint.setColor(Color.CYAN);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(3);
            canvas.drawRect(playerX - 10, playerY - 10, playerX + PLAYER_SIZE + 10, playerY + PLAYER_SIZE + 10, paint);
            paint.setStyle(Paint.Style.FILL);
            shieldTime--;
            if (shieldTime <= 0) hasShield = false;
        }
        
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
        
        // Draw power-ups
        paint.setColor(Color.MAGENTA);
        for (int i = powerUps.size() - 1; i >= 0; i--) {
            int[] pu = powerUps.get(i);
            pu[1] += 3;
            if (pu[1] > screenHeight) {
                powerUps.remove(i);
            } else {
                canvas.drawCircle(pu[0], pu[1], POWERUP_SIZE / 2, paint);
            }
        }
        
        // Draw enemies
        paint.setColor(Color.RED);
        for (int i = enemies.size() - 1; i >= 0; i--) {
            int[] enemy = enemies.get(i);
            enemy[0] += enemySpeed;
            
            if (enemy[0] <= 0 || enemy[0] >= screenWidth - ENEMY_SIZE) {
                enemySpeed = -enemySpeed;
                enemy[1] += 50;
            }
            
            if (enemy[1] >= playerY) {
                gameOver(canvas);
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
        
        // Draw HUD
        paint.setColor(Color.WHITE);
        paint.setTextSize(40);
        canvas.drawText("Score: " + score, 20, 50, paint);
        canvas.drawText("Level: " + level, screenWidth - 200, 50, paint);
        
        // Win condition
        if (enemies.isEmpty()) {
            level++;
            enemySpeed += 1;
            createEnemyWave();
        }
        
        invalidate();
    }
    
    private void checkCollisions() {
        // Bullet-enemy collisions
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
                    score += 100 * level;
                    enemiesKilled++;
                    
                    // Random power-up drop
                    if (Math.random() < 0.2) {
                        powerUps.add(new int[]{enemy[0], enemy[1]});
                    }
                    break;
                }
            }
        }
        
        // Power-up collection
        for (int i = powerUps.size() - 1; i >= 0; i--) {
            int[] pu = powerUps.get(i);
            if (pu[0] > playerX && pu[0] < playerX + PLAYER_SIZE &&
                pu[1] > playerY && pu[1] < playerY + PLAYER_SIZE) {
                powerUps.remove(i);
                hasShield = true;
                shieldTime = 300;
            }
        }
    }
    
    private void gameOver(Canvas canvas) {
        paint.setColor(Color.RED);
        paint.setTextSize(80);
        canvas.drawText("GAME OVER", screenWidth / 4, screenHeight / 2, paint);
        paint.setTextSize(40);
        canvas.drawText("Score: " + score + " | Level: " + level, screenWidth / 4, screenHeight / 2 + 80, paint);
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE) {
            touchX = event.getX();
            
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                bullets.add(new int[]{playerX + PLAYER_SIZE / 2, playerY});
            }
        }
        return true;
    }
}