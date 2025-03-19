package org.spaceinvaders.entities;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Projectile {
    private double x, y;
    private final double speed = 8;
    private Image projectileSprite;

    public Projectile(double x, double y) {
        this.x = x;
        this.y = y;

        // Cargar el sprite del proyectil del jugador
        projectileSprite = new Image(getClass().getResourceAsStream("/images/Bala aliada-1.png.png"));
    }

    public void update() {
        y -= speed;  // Mover hacia arriba
    }

    public void draw(GraphicsContext gc) {
        gc.drawImage(projectileSprite, x, y, 40, 50);
    }

    public double getY() { return y; }

    //Detectar colisiones con enemigos
    public boolean collidesWith(Enemy enemy) {
        return x < enemy.getX() + 30 &&
                x + 4 > enemy.getX() &&
                y < enemy.getY() + 20 &&
                y + 2 > enemy.getY();
    }
}
