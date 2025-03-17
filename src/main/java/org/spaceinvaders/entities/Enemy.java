package org.spaceinvaders.entities;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Enemy {

    private double x, y;
    private final double speed = 1;
    private List<EnemyProjectile> projectiles = new ArrayList<>();
    private Random random = new Random();

    public Enemy(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void move(double direction) {
        x += speed * direction;
    }

    public void moveDown() {
        y += 20;
    }

    public void fireProjectile(List<EnemyProjectile> globalProjectiles) {
        EnemyProjectile projectile = new EnemyProjectile(x + 13, y + 20);
        globalProjectiles.add(projectile); // Ahora siempre se añade el proyectil
    }


    public void draw(GraphicsContext gc) {
        gc.setFill(Color.BLUE);
        gc.fillRect(x, y, 30, 20); // Representación del enemigo

        // Dibujar los proyectiles disparados por este enemigo
        gc.setFill(Color.ORANGE);
        for (EnemyProjectile p : projectiles) {
            p.draw(gc);
        }
    }

    public double getX() { return x; }
    public double getY() { return y; }
    public List<EnemyProjectile> getProjectiles() { return projectiles; }
}
