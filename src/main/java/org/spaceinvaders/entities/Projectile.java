package org.spaceinvaders.entities;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Projectile {
    private double x, y;
    private final double speed = 5;

    public Projectile(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void update() {
        y -= speed;  // Mover hacia arriba
    }

    public void draw(GraphicsContext gc) {
        gc.setFill(Color.RED);
        gc.fillRect(x, y, 4, 10);
    }

    public double getY() { return y; }

    public boolean collidesWith(Enemy enemy) {
        return x < enemy.getX() + 30 &&
                x + 4 > enemy.getX() &&
                y < enemy.getY() + 20 &&
                y + 10 > enemy.getY();
    }

}
