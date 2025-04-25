package org.spaceinvaders.models;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Projectile {
    private double x, y;
    private double speed = 4;
    private Image projectileSprite;
    private boolean active = true;

    // Constructor del proyectil
    public Projectile(double x, double y) {
        this.x = x;
        this.y = y;

        // Cargar el sprite del proyectil del jugador
        projectileSprite = new Image(getClass().getResourceAsStream("/images/projectiles/Bala aliada-1.png.png"));
    }
    // Movimiento del proyectil
    public void update() {
        y -= speed;  // Mover hacia arriba
    }

    public double getY() { return y; }

    public void resetSpeed() {
        speed = 4;  // Reset the speed to its initial value
    }

    // Dibujar el proyectil en la pantalla
    public void draw(GraphicsContext gc) {
        gc.drawImage(projectileSprite, x, y, 40, 20);
//        // Dibujar el borde de la hitbox del proyectil
//        gc.setStroke(Color.BLACK);
//        gc.strokeRect(x, y, 40, 20);
    }

    //Detectar colisiones con enemigos
    public boolean collidesWith(Enemy enemy) {
        return x < enemy.getX() + 30 &&
                x + 10 > enemy.getX() &&
                y < enemy.getY() + 20 &&
                y + 20 > enemy.getY();
    }

    // Detectar colisiones con el jefe
    public boolean collidesWith(Boss boss) {
        return x < boss.getX() + 100 &&
                x + 10 > boss.getX() &&
                y < boss.getY() + 20 && //Ajuste para la altura del jefe
                y + 20 > boss.getY();
    }

    // Obtener los límites del proyectil
    public Rectangle2D getBounds() {
        return new Rectangle2D(x, y, 40, 20);
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
