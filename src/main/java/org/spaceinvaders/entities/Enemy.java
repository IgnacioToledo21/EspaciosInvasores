package org.spaceinvaders.entities;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import java.util.List;
import java.util.Random;

public class Enemy {
    private double x, y;
    private final double speed = 0.5;
    private int vida;
    private Image frame1, frame2;
    private boolean mostrarFrame1 = true; // Alternar entre los frames
    private long ultimoCambioFrame = 0;
    private static final long INTERVALO_ANIMACION = 500; // 0.5 segundos (500 ms)

    public Enemy(double x, double y, int tipo) {
        this.x = x;
        this.y = y;

        // Cargar los dos frames según el tipo de enemigo
        switch (tipo) {
            case 1:
                this.vida = 1;
                frame1 = new Image(getClass().getResourceAsStream("/images/Enemigo soldado-1.png.png"));
                frame2 = new Image(getClass().getResourceAsStream("/images/Enemigo soldado-2.png.png"));
                break;
            case 2:
                this.vida = 2;
                frame1 = new Image(getClass().getResourceAsStream("/images/Enemigocaballero1png.png"));
                frame2 = new Image(getClass().getResourceAsStream("/images/Enemigocaballero2png.png"));
                break;
            case 3:
                this.vida = 3;
                frame1 = new Image(getClass().getResourceAsStream("/images/Enemigo Tanque-1.png.png"));
                frame2 = new Image(getClass().getResourceAsStream("/images/Enemigo Tanque-2.png.png"));
                break;
        }
    }

    public void move(double direction) {
        x += speed * direction;
    }

    public void moveDown() {
        y += 20;
    }

    public void fireProjectile(List<EnemyProjectile> globalProjectiles) {
        EnemyProjectile projectile = new EnemyProjectile(x + 13, y + 20);
        globalProjectiles.add(projectile);
    }

    public void draw(GraphicsContext gc) {
        long tiempoActual = System.currentTimeMillis();

        // Alternar entre frame1 y frame2 cada 0.5 segundos
        if (tiempoActual - ultimoCambioFrame >= INTERVALO_ANIMACION) {
            mostrarFrame1 = !mostrarFrame1;
            ultimoCambioFrame = tiempoActual;
        }

        // Dibujar el frame correspondiente
        gc.drawImage(mostrarFrame1 ? frame1 : frame2, x, y, 40, 40);
    }

    public void recibirImpacto() {
        vida--;
    }

    public boolean estaDestruido() {
        return vida <= 0;
    }

    public double getX() { return x; }
    public double getY() { return y; }
}