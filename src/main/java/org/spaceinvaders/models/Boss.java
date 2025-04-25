package org.spaceinvaders.models;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.List;

public class Boss {
    // Posición y velocidad
    private double x, y;
    private double speed = 0.5;

    // Vida y fases
    private int vida = 20;
    private int maxVida = vida;

    // Sprites de animación
    private Image frame1, frame2;
    private boolean mostrarFrame1 = true;
    private long ultimoCambioFrame = 0;
    private static final long INTERVALO_ANIMACION = 500;

    public int getMaxVida() {
        return maxVida;
    }

    public Boss(double x, double y) {
        this.x = x;
        this.y = y;
        frame1 = new Image(getClass().getResourceAsStream("/images/bosses/Primer Boss-1.png.png"));
        frame2 = new Image(getClass().getResourceAsStream("/images/bosses/Primer Boss-2.png.png"));
    }

    /**
     * Movimiento horizontal con rebote y descenso al cambiar de dirección.
     * Velocidad puede aumentar según fase.
     */
    public void move() {
        x += speed;
        if (x <= 10 || x >= 1200 - 100) {
            speed *= -1;
            y += 20;
        }
    }

    /**
     * Dispara proyectiles según la fase actual:
     * - Fase 1: un solo disparo frontal
     * - Fase 2: ráfaga doble (dos proyectiles laterales)
     * - Fase 3: abanico de 5 proyectiles
     */
    public void fireProjectile(List<EnemyProjectile> globalProjectiles) {
        double px = x + 50;
        double py = y + 80;

        int phase = getPhase();
        if (phase == 1) {
            // Fase 1: disparo simple
            globalProjectiles.add(new EnemyProjectile(px, py));
        } else if (phase == 2) {
            // Fase 2: disparo doble
            globalProjectiles.add(new EnemyProjectile(px - 20, py));
            globalProjectiles.add(new EnemyProjectile(px + 20, py));
        } else {
            // Fase 3: abanico de 5 proyectiles
            int rays = 5;
            double startAngle = -0.3;  // ángulo izquierdo
            double step = 0.15;        // separación angular
            for (int i = 0; i < rays; i++) {
                double dx = startAngle + (step * i);
                globalProjectiles.add(new EnemyProjectile(px, py, dx));
            }
        }
    }


    /**
     * Recibe un impacto y actualiza vida. Aumenta velocidad si cambia de fase.
     */
    public void recibirImpacto() {
        vida--;
        System.out.println("🔥 Boss impactado. Vida restante: " + vida);
        int phase = getPhase();
        // Aumentar velocidad al entrar en fases 2 y 3
        if (phase == 2) speed = Math.signum(speed) * 0.8;
        if (phase == 3) speed = Math.signum(speed) * 1.2;
    }

    public boolean estaDestruido() {
        return vida <= 0;
    }

    public int getPhase() {
        double ratio = (double) vida / maxVida;
        if (ratio > 0.66) return 1;
        if (ratio > 0.33) return 2;
        return 3;
    }

    public int getVida() {
        return vida;
    }

    public double getX() { return x; }
    public double getY() { return y; }

    public void resetSpeed() {
        speed = 0.5;
    }

    public int getPoints() {
        // Puntos podría escalar según fases
        return 5000 * maxVida;
    }

    /**
     * Dibuja e intercala frames de animación.
     */
    public void draw(GraphicsContext gc) {
        long now = System.currentTimeMillis();
        if (now - ultimoCambioFrame >= INTERVALO_ANIMACION) {
            mostrarFrame1 = !mostrarFrame1;
            ultimoCambioFrame = now;
        }
        gc.drawImage(mostrarFrame1 ? frame1 : frame2, x, y, 100, 100);
    }
}
