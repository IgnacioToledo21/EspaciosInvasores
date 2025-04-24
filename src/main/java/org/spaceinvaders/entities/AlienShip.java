package org.spaceinvaders.entities;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.geometry.Rectangle2D;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AlienShip {
    private double x, y;
    private double speed;       
    private int dirX;
    private Image sprite;
    private int puntosBase = 100;
    private Random rnd = new Random();

    private Image sprite1;
    private Image sprite2;
    private int spriteIndex = 0;

    private int changeDirectionTimer = 0; // Temporizador para cambiar la dirección aleatoriamente
    private static final double MAX_X = 1200;  // Límite derecho (margen de 1200px)
    private static final double MIN_X = 0;     // Límite izquierdo
    private static final double WIDTH = 60;    // Ancho del sprite de la nave

    public AlienShip() {
        this.x = rnd.nextDouble() * (MAX_X - WIDTH);  // Posición aleatoria horizontal dentro del margen
        this.y = 10;  // Posición inicial en el eje Y
        this.dirX = rnd.nextBoolean() ? 1 : -1;  // Aleatorio, izquierda o derecha
        this.speed = 1 + rnd.nextDouble() * 2;   // Velocidad aleatoria

        this.sprite1 = new Image(getClass().getResourceAsStream("/images/enemigos/NaveAlienigena.png"));
        this.sprite2 = new Image(getClass().getResourceAsStream("/images/enemigos/NaveAlienigena2.png"));
        this.sprite = sprite1;
    }

    public void switchSprite() {
        switch (spriteIndex) {
            case 0:
                this.sprite = sprite1;
                spriteIndex = 1;
                break;
            case 1:
                this.sprite = sprite2;
                spriteIndex = 0;
                break;
        }
    }

    private int frameCounter = 0;

    public void update() {
        // Movimiento en una dirección aleatoria (izquierda o derecha)
        x += dirX * speed;

        // Comprobar que la nave no se salga del margen (0 a 1200)
        if (x <= MIN_X) {  // Si la nave llega al borde izquierdo
            x = MIN_X;      // Se ajusta al límite izquierdo
            dirX = 1;       // Cambia la dirección a derecha
        } else if (x >= MAX_X - WIDTH) {  // Si la nave llega al borde derecho
            x = MAX_X - WIDTH;  // Se ajusta al límite derecho
            dirX = -1;          // Cambia la dirección a izquierda
        }

        // Temporizador para cambiar de dirección aleatoriamente
        changeDirectionTimer++;

        if (changeDirectionTimer >= rnd.nextInt(100) + 50) {  // Cambia de dirección cada 50 a 150 frames
            dirX = rnd.nextBoolean() ? 1 : -1;  // Cambia aleatoriamente la dirección (izquierda o derecha)
            changeDirectionTimer = 0;  // Resetear el temporizador
        }

        // Alternar entre los dos sprites en intervalos de 40 frames
        frameCounter++;
        if (frameCounter % 40 == 0) {
            switchSprite();
        }
    }

    public void draw(GraphicsContext gc) {
        gc.drawImage(sprite, x, y);
    }

    public Rectangle2D getBounds() {
        return new Rectangle2D(x, y, sprite.getWidth(), sprite.getHeight());
    }

    public int getPoints() {
        return puntosBase;
    }
}

