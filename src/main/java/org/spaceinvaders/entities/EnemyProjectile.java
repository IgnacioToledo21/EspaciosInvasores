package org.spaceinvaders.entities;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class EnemyProjectile {

    private double x, y;
    private final double speed = 3;  // Velocidad más lenta que la de la nave

    public EnemyProjectile(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void update() {
        y += speed;  // Mover hacia abajo
    }

    public void draw(GraphicsContext gc) {
        gc.setFill(Color.ORANGE);
        gc.fillRect(x, y, 4, 10);
    }

    public double getY() { return y; }
    public double getX() { return x; }
}
