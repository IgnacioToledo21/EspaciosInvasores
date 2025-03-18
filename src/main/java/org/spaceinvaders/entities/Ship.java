package org.spaceinvaders.entities;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import java.util.ArrayList;
import java.util.List;

public class Ship {

    private double x, y;
    private final double velocidad = 10;
    private List<Projectile> projectiles = new ArrayList<>();
    private int vidas = 3;

    public Ship() {
        this.x = 600;
        this.y = 600;
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
        projectiles.add(new Projectile(x + 18, y - 10));
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
        gc.setFill(Color.GREEN);
        gc.fillRect(x, y, 40, 20);  // Cuerpo de la nave
        gc.setFill(Color.DARKGREEN);
        gc.fillPolygon(
                new double[]{x + 10, x + 20, x + 30},  // Coordenadas X
                new double[]{y, y - 20, y},  // Coordenadas Y
                3  // Triángulo superior de la nave
        );

        // Dibujar los proyectiles
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
