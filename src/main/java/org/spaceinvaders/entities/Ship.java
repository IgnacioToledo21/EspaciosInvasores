package org.spaceinvaders.entities;

import javafx.application.Application;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import java.util.ArrayList;
import java.util.List;

public class Ship {

    //Cargar sprites
    private Image shipSprite;
    private Image projectileSprite;

    //Variables de posición
    private double x, y;
    private double velocidad = 2;
    private List<Projectile> projectiles = new ArrayList<>();

    //Vidas de la nave
    private int vidas = 3;

    //Variables de tiempo de disparo
    private long lastShotTime = 0;
    private final long SHOOT_COOLDOWN = 0000; // 0.8 segundos de cooldown

    //Puntuacion de la nave
    private int score = 0;

    //Constructor de la nave
    public Ship() {
        this.x = 600;
        this.y = 600;

        //Cargar el sprite
        shipSprite = new Image(getClass().getResourceAsStream("/images/NavePrincipal.png"));
        projectileSprite = new Image(getClass().getResourceAsStream("/images/Bala aliada-1.png.png"));

    }

    //Metodos de movimiento de la nave
    public void moverIzquierda() {
        if (x > 0) {
            x -= velocidad;
        }
    }

    public void moverDerecha() {
        if (x < 1200 - 40) {  // Asegurar que no salga de la pantalla
            x += velocidad;
        }
    }

    //Metodo de disparo
    public void fireProjectile() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastShotTime >= SHOOT_COOLDOWN) {
            double projectileX = x + (shipSprite.getWidth() / 2) - (projectileSprite.getWidth() / 2);
            double projectileY = y - projectileSprite.getHeight();
            projectiles.add(new Projectile(projectileX, projectileY));
            lastShotTime = currentTime;
        }
    }

    //Metodo de actualización de proyectiles
    public void updateProjectiles() {
        projectiles.removeIf(p -> p.getY() < 0); // Eliminar proyectiles fuera de la pantalla
        projectiles.forEach(Projectile::update);
    }

    //Metodo de reducción de vida
    public void reducirVida() {
        vidas--;
        System.out.println("La nave ha sido impactada. Vidas restantes: " + vidas);
    }

    public int getVidas() {
        return vidas;
    }

    //Metodo de dibujo de la nave
    public void draw(GraphicsContext gc) {
        gc.drawImage(shipSprite, x, y, 50, 40); // ✅ Usar el sprite en lugar del rectángulo

        //gc.fillRect(x, y, 40, 40);
        //gc.setFill(Color.RED);

        for (Projectile p : projectiles) {
            p.draw(gc);
        }
    }

    //Lista de proyectiles
    public List<Projectile> getProjectiles() {
        return projectiles;
    }

    public void resetSpeed() {
        velocidad = 2;  // Velocidad base de la nave
    }

    public double getX() { return x; }
    public double getY() { return y; }

    public int getScore() { // Add getScore method
        return score;
    }

    public void addScore(int points) { // Add method to increase score
        score += points;
    }

}
