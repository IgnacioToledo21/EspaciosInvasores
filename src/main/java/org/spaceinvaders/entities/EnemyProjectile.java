package org.spaceinvaders.entities;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class EnemyProjectile {

    // Variables de posición y velocidad
    private double x, y;
    private final double speed = 1.5;
    private Image enemyProjectileSprite;
    private boolean active = true;

    // Constructor del proyectil enemigo
    public EnemyProjectile(double x, double y) {
        this.x = x;
        this.y = y;

        // Cargar el sprite del proyectil enemigo
        enemyProjectileSprite = new Image(getClass().getResourceAsStream("/images/projectiles/Bala enemigaverde.png"));
    }

    // Movimiento del proyectil enemigo
    public void update() {
        y += speed;  // Mover hacia abajo
    }

    public double getY() { return y; }

    // Dibujar el proyectil en la pantalla
    public void draw(GraphicsContext gc) {
        gc.drawImage(enemyProjectileSprite, x, y, 40, 50); // Ajustar tamaño del proyectil
    }

    // Detectar colisiones con la nave
    public boolean collidesWith(Ship ship) {
        return x < ship.getX() + 40 &&  // Ancho de la nave
                x + 10 > ship.getX() &&  // Ancho del proyectil ajustado
                y < ship.getY() + 40 &&  // Alto de la nave
                y + 20 > ship.getY();    // Alto del proyectil ajustado
    }

    public Rectangle2D getBounds() {
        // Ajustar el rectángulo de colisión para que esté centrado en la bala
        double offsetX = (40 - 20) / 2; // La diferencia entre el ancho del sprite y el ancho del rectángulo de colisión
        return new Rectangle2D(x + offsetX, y, 20, 50);
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}