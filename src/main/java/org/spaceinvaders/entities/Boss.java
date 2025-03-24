package org.spaceinvaders.entities;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.util.List;

public class Boss {

    // Variables del jefe
    private double x, y;
    private double speed = 0.5; // Velocidad de movimiento
    private int vida = 20; // Más vida que los enemigos normales
    private Image frame1, frame2;
    private boolean mostrarFrame1 = true; // Alternar entre los frames
    private long ultimoCambioFrame = 0;
    private static final long INTERVALO_ANIMACION = 500; // 0.5 segundos (500 ms)

    // Constructor del jefe
    public Boss(double x, double y) {
        this.x = x;
        this.y = y;
        frame1 = new Image(getClass().getResourceAsStream("/images/Primer Boss-1.png.png"));
        frame2 = new Image(getClass().getResourceAsStream("/images/Primer Boss-2.png.png"));
    }
    //Movimientos del jefe
    public void move() {
        x += speed;
        if (x <= 10 || x >= 1200 - 100) { // Si toca los bordes, cambia de dirección
            speed *= -1;
            y += 20; // ✅ Mover hacia abajo al cambiar de dirección
        }
    }

    public int getPoints() {
        return 1000; //Cambiar para pruebas
    }

    // Disparar proyectil
    public void fireProjectile(List<EnemyProjectile> globalProjectiles) {
        globalProjectiles.add(new EnemyProjectile(x + 50, y + 80));
    }

    public void recibirImpacto() {
        vida--;
        System.out.println("🔥 Boss impactado. Vida restante: " + vida); // Depuración en consola
    }

    public boolean estaDestruido() {
        return vida <= 0;
    }

    public double getX() { return x; }
    public double getY() { return y; }

    public int getVida() {
        return vida;
    }

    public void resetSpeed() {
        speed = 0.5;
    }

    // Dibujar el jefe en la pantalla
    public void draw(GraphicsContext gc) {
        long tiempoActual = System.currentTimeMillis();

        // Alternar entre frame1 y frame2 cada 0.5 segundos
        if (tiempoActual - ultimoCambioFrame >= INTERVALO_ANIMACION) {
            mostrarFrame1 = !mostrarFrame1;
            ultimoCambioFrame = tiempoActual;
        }

//        // Dibujar un rectángulo en lugar del sprite
//        gc.setFill(Color.RED);
//        gc.fillRect(x, y, 100, 100);
//
//        // Dibujar el borde de la hitbox
//        gc.setStroke(Color.BLACK);
//        gc.strokeRect(x, y, 100, 100);

        // Dibujar el frame correspondiente
        gc.drawImage(mostrarFrame1 ? frame1 : frame2, x, y, 100, 100);
    }

}