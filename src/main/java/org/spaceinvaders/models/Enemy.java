package org.spaceinvaders.models;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.List;

public class Enemy {

    //Variables de enemigos
    private double x, y;
    private final double speed = 0.5;
    private int vida;

    private final double width = 30;
    private final double height = 30;


    private int puntosBase; // ✅ Nueva variable fija para puntos

    private Image frame1, frame2;
    private boolean mostrarFrame1 = true; // Alternar entre los frames
    private long ultimoCambioFrame = 0;
    private static final long INTERVALO_ANIMACION = 500; // 0.5 segundos (500 ms)

    // Constructor del enemigo
    public Enemy(double x, double y, int tipo) {
        this.x = x;
        this.y = y;

        // Cargar los dos frames según el tipo de enemigo
        switch (tipo) {
            case 1:
                this.vida = 1;
                this.puntosBase = 5;
                frame1 = new Image(getClass().getResourceAsStream("/images/enemigos/Enemigo soldado-1.png.png"));
                frame2 = new Image(getClass().getResourceAsStream("/images/enemigos/Enemigo soldado-2.png.png"));
                break;
            case 2:
                this.vida = 2;
                this.puntosBase = 10;
                frame1 = new Image(getClass().getResourceAsStream("/images/enemigos/Enemigocaballero1png.png"));
                frame2 = new Image(getClass().getResourceAsStream("/images/enemigos/Enemigocaballero2png.png"));
                break;
            case 3:
                this.vida = 3;
                this.puntosBase = 50;
                frame1 = new Image(getClass().getResourceAsStream("/images/enemigos/Enemigo Tanque-1.png.png"));
                frame2 = new Image(getClass().getResourceAsStream("/images/enemigos/Enemigo Tanque-2.png.png"));
                break;
        }
    }

    public int getPoints() {
            return puntosBase;
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

    public void recibirImpacto() {
        vida--;
    }

    public boolean estaDestruido() {
        return vida <= 0;
    }

    public double getX() { return x; }
    public double getY() { return y; }


    //Dibujar enemigos
    public void draw(GraphicsContext gc) {
        long tiempoActual = System.currentTimeMillis();

        // Alternar entre frame1 y frame2 cada 0.5 segundos
        if (tiempoActual - ultimoCambioFrame >= INTERVALO_ANIMACION) {
            mostrarFrame1 = !mostrarFrame1;
            ultimoCambioFrame = tiempoActual;
        }

//        // Dibujar un rectángulo rojo en lugar de los frames
//        gc.setFill(Color.RED);
//        gc.fillRect(x, y, 30, 30);

//        // Dibujar el borde de la hitbox
//        gc.setStroke(Color.BLACK);
//        gc.strokeRect(x, y, 30, 30);

        // Dibujar el frame correspondiente
        gc.drawImage(mostrarFrame1 ? frame1 : frame2, x, y, 30, 30);
    }

    public Rectangle2D getBounds() {
        return new Rectangle2D(x, y, width, height); // Ajusta width y height según el tamaño del enemigo
    }

}