package org.spaceinvaders.models;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Inventory {
    private double x, y;
    private double width, height;
    private Image bombImage, doubleShotImage, shieldImage;
    private boolean bombCollected, doubleShotCollected, shieldCollected;
    private Image InventoryImage;

    private Ship ship;

    public Inventory(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.bombImage = new Image(getClass().getResourceAsStream("/images/poderes/BOMBBASE.png"));
        this.doubleShotImage = new Image(getClass().getResourceAsStream("/images/poderes/DOUBLESHOT.png"));
        this.shieldImage = new Image(getClass().getResourceAsStream("/images/poderes/SHIELDBASE.png"));
        this.bombCollected = false;
        this.doubleShotCollected = false;
        this.shieldCollected = false;
        this.InventoryImage = new Image(getClass().getResourceAsStream("/images/inventario/Inventario.png"));
    }

    public void reset() {
        this.bombCollected = false;
        this.doubleShotCollected = false;
        this.shieldCollected = false;
    }

    public void resetShield() {
        this.shieldCollected = false;
    }

    public void resetDoubleShot() {
        this.doubleShotCollected = false;
    }

    public void resetBomb() {
        this.bombCollected = false;
    }

    public void collectBomb() {
        this.bombCollected = true;
    }

    public boolean hasBomb() {
        return bombCollected;
    }

    public void useBomb() {
        if (bombCollected) { // Solo elimina la bomba si hay una en el inventario
            this.bombCollected = false; // Elimina la bomba del inventario
            System.out.println("🔥 Bomba eliminada del inventario.");
        } else {
            System.out.println("🚫 No hay bomba en el inventario.");
        }
    }


    public void collectDoubleShot() {
        this.doubleShotCollected = true;
    }

    public void collectShield() {
        this.shieldCollected = true;
    }

    public void draw(GraphicsContext gc) {

        gc.clearRect(x, y, width, height);

//        gc.setStroke(Color.BLUE);
//        gc.strokeRect(x, y, width, height);

        gc.drawImage(InventoryImage, x, y, width, height);

        if (bombCollected) {
            gc.drawImage(bombImage, x, y, width, height);
        }

        if (doubleShotCollected) {
            gc.drawImage(doubleShotImage, x, y, width, height);
        }

        if (shieldCollected) {
            gc.drawImage(shieldImage, x, y, width, height);
        }
    }


}
