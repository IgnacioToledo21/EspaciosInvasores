package org.spaceinvaders.entities;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class EnemyProjectile {
    private double x, y;
    private final double speed = 1.5;
    private Image enemyProjectileSprite;

    public EnemyProjectile(double x, double y) {
        this.x = x;
        this.y = y;

        // Cargar el sprite del proyectil enemigo
        enemyProjectileSprite = new Image(getClass().getResourceAsStream("/images/Bala enemiga-1.png.png"));
    }

    public void update() {
        y += speed;  // Mover hacia abajo
    }

    public void draw(GraphicsContext gc) {
        gc.drawImage(enemyProjectileSprite, x, y, 50, 60); // Ajustar tamaño si es necesario
    }

    public boolean collidesWith(Ship ship) {
        return x < ship.getX() + 40 &&  // Ancho de la nave
                x + 50 > ship.getX() &&  // Ancho del proyectil
                y < ship.getY() + 40 &&  // Alto de la nave
                y + 60 > ship.getY();    // Alto del proyectil
    }


    public double getY() { return y; }
}
