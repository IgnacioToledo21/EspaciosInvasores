package org.spaceinvaders.entities;

import javafx.application.Application;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import java.util.ArrayList;
import java.util.List;

public class Ship {

    private Image shipSprite;

    private double x, y;
    private final double velocidad = 2;
    private List<Projectile> projectiles = new ArrayList<>();
    private int vidas = 3;

    private long lastShotTime = 0;
    private final long SHOOT_COOLDOWN = 1000; // 1 segundo de cooldown

    public Ship() {
        this.x = 600;
        this.y = 600;

        //Cargar el sprite
        shipSprite = new Image(getClass().getResourceAsStream("/images/NavePrincipal.png"));

    }

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

    public void fireProjectile() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastShotTime >= SHOOT_COOLDOWN) {
            projectiles.add(new Projectile(x + 18, y - 10));
            lastShotTime = currentTime;
        }
    }

    public void updateProjectiles() {
        projectiles.removeIf(p -> p.getY() < 0); // Eliminar proyectiles fuera de la pantalla
        projectiles.forEach(Projectile::update);
    }

    public void reducirVida() {
        vidas--;
        System.out.println("La nave ha sido impactada. Vidas restantes: " + vidas);
    }

    public void draw(GraphicsContext gc) {
        gc.drawImage(shipSprite, x, y, 40, 40); // ✅ Usar el sprite en lugar del rectángulo
        gc.setFill(Color.RED);
        for (Projectile p : projectiles) {
            p.draw(gc);
        }
    }

    public List<Projectile> getProjectiles() {
        return projectiles;
    }

    public double getX() { return x; }
    public double getY() { return y; }

    public int getVidas() {
        return vidas;
    }

}
