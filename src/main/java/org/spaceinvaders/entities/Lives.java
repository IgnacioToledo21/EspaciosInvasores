package org.spaceinvaders.entities;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Lives {
    private int vidas;
    private final int MAX_VIDAS = 3;
    private Image heartFull, heartEmpty;

    public Lives() {
        this.vidas = MAX_VIDAS; // Comenzamos con 3 vidas
        heartFull = new Image(getClass().getResourceAsStream("/images/Vidas-1.png.png"));
        heartEmpty = new Image(getClass().getResourceAsStream("/images/Vidasrotaspng.png    "));
    }

    public void reducirVida() {
        if (vidas > 0) {
            vidas--;
        }
    }

    public int getVidas() {
        return vidas;
    }

    public void reiniciar() {
        vidas = MAX_VIDAS;
    }

    public void draw(GraphicsContext gc) {
        for (int i = 0; i < MAX_VIDAS; i++) { // Siempre dibujamos 3 corazones
            if (i < vidas) {
                gc.drawImage(heartFull, 20 + (i * 50), 20, 40, 40);
            } else {
                gc.drawImage(heartEmpty, 20 + (i * 50), 20, 40, 40);
            }
        }
    }
}
