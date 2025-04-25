package org.spaceinvaders.models;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class EnemyProjectile {

    private double x, y;
    private final double speed = 1.5;
    private double dx = 0;              // Velocidad horizontal extra
    private Image enemyProjectileSprite;
    private boolean active = true;

    // Constructor por defecto (disparo vertical)
    public EnemyProjectile(double x, double y) {
        this(x, y, 0);
    }

    // Nuevo constructor con dirección horizontal
    public EnemyProjectile(double x, double y, double dx) {
        this.x = x;
        this.y = y;
        this.dx = dx;
        enemyProjectileSprite = new Image(getClass().getResourceAsStream("/images/projectiles/Bala enemigaverde.png"));
    }

    public void update() {
        x += dx * speed;  // Mover en x según ángulo
        y += speed;       // Siempre baja
    }

    public double getY() { return y; }

    public void draw(GraphicsContext gc) {
        gc.drawImage(enemyProjectileSprite, x, y, 40, 50);
    }

    public boolean collidesWith(Ship ship) {
        return x < ship.getX() + 40 &&
                x + 10 > ship.getX() &&
                y < ship.getY() + 40 &&
                y + 20 > ship.getY();
    }

    public Rectangle2D getBounds() {
        double offsetX = (40 - 20) / 2;
        return new Rectangle2D(x + offsetX, y, 20, 50);
    }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}
