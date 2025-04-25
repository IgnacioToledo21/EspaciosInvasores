package org.spaceinvaders.models;

import javafx.scene.image.Image;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.Rectangle;

public class PowerUps {
    public enum PowerUpType {
        SHIELD, DOUBLE_SHOT, BOMB
    }

    private double x, y;
    private PowerUpType type;
    private Image image;
    private boolean active;

    public PowerUps(double x, double y, PowerUpType type) {
        this.x = x;
        this.y = y;
        this.type = type;
        this.active = true;

        switch (type) {
            case SHIELD:
                this.image = new Image(getClass().getResourceAsStream("/images/poderes/SHIELDBASE.png"));
                break;
            case DOUBLE_SHOT:
                this.image = new Image(getClass().getResourceAsStream("/images/poderes/DOUBLESHOT.png"));
                break;
            case BOMB:
                this.image = new Image(getClass().getResourceAsStream("/images/poderes/BOMBBASE.png"));
                break;
        }
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public void draw(GraphicsContext gc) {
        if (active) {
            gc.drawImage(image, x, y);
        }
    }

    public void update() {
        y += 2; // Move the power-up downwards
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, image.getWidth(), image.getHeight());
    }

    public PowerUpType getType() {
        return type;
    }

    public boolean isActive() {
        return active;
    }

    public void deactivate() {
        this.active = false;
    }

    public void applyEffect(Ship ship) {
        switch (type) {
            case SHIELD:
                ship.activateShield();
                ship.getInventory().collectShield();
                break;
            case DOUBLE_SHOT:
                ship.activateDoubleShot();
                ship.getInventory().collectDoubleShot();
                break;
            case BOMB:
                ship.getInventory().collectBomb();
                break;
        }
        deactivate();
    }


    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }
}
