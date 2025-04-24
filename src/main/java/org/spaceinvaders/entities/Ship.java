package org.spaceinvaders.entities;

import javafx.application.Platform;
import javafx.geometry.BoundingBox;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import org.spaceinvaders.controllers.RootController;

import java.util.*;

public class Ship {

    private Image shipSprite;
    private Image projectileSprite;
    private Image bombSprite;
    private Image shieldSprite;

    private double x, y;
    private double velocidad = 2;
    private List<Projectile> projectiles = new ArrayList<>();
    private List<PowerUps> bombs = new ArrayList<>();

    private int vidas = 3;

    private long lastShotTime = 0;
    private final long SHOOT_COOLDOWN = 0000; // 0.5 segundos de cooldown

    private boolean shieldActive = false;
    private Timer shieldTimer;

    private boolean doubleShotActive = false;
    private Timer doubleShotTimer;

    private boolean bombActive = false;

    private int score = 0;

    private Inventory inventory;

    private RootController rootController;


    public void setRootController(RootController rootController) {
        this.rootController = rootController;
    }

    public RootController getRootController() {
        return rootController;
    }

    public Ship(Inventory inventory) {
        this.x = 600;
        this.y = 600;

        shipSprite = new Image(getClass().getResourceAsStream("/images/naves/Nave Principal40x50.png.png"));
        projectileSprite = new Image(getClass().getResourceAsStream("/images/projectiles/Bala aliada-1.png.png"));
        bombSprite = new Image(getClass().getResourceAsStream("/images/poderes/BOMB.png"));
        shieldSprite = new Image(getClass().getResourceAsStream("/images/poderes/SHIELD.png"));

        this.inventory = inventory;

    }

    public void setShipSprite(Image newSprite) {
        this.shipSprite = newSprite;
    }


    public Inventory getInventory() {
        return inventory;
    }

    public void moverIzquierda() {
        if (x > 0) {
            x -= velocidad;
        }
    }

    public void moverDerecha() {
        if (x < 1200 - 40) {
            x += velocidad;
        }
    }

    public void fireProjectile() {
        long now = System.currentTimeMillis();
        if (now - lastShotTime < SHOOT_COOLDOWN) return;

        double shipW       = 50;                        // ancho en pantalla
        double projW       = projectileSprite.getWidth(); // ancho real de la bala
        double separation  = 10;                        // separación deseada desde los bordes
        double y0          = y - projectileSprite.getHeight();

        if (doubleShotActive) {
            // Primera bala: un poquito a la derecha del borde izquierdo
            double x1 = x + separation;
            // Segunda bala: un poquito a la izquierda del borde derecho
            double x2 = x + shipW - projW - separation;

            projectiles.add(new Projectile(x1, y0));
            projectiles.add(new Projectile(x2, y0));

            System.out.println("[DEBUG] Added TWO projectiles at x1=" + x1 + ", x2=" + x2);
        } else {
            double x0 = x + (shipW / 2) - (projW / 2);
            projectiles.add(new Projectile(x0, y0));
            System.out.println("[DEBUG] Added ONE projectile at x0=" + x0);
        }

        lastShotTime = now;
    }


    public void fireBomb() {
        if (inventory.hasBomb()) { // Verifica si hay una bomba en el inventario
            double bombX = x + (shipSprite.getWidth() / 2) - (bombSprite.getWidth() / 2);
            double bombY = y - bombSprite.getHeight();
            PowerUps bomb = new PowerUps(bombX, bombY, PowerUps.PowerUpType.BOMB);
            bombs.add(bomb);

            inventory.useBomb(); // Asegura que la bomba se elimine del inventario
            bomb.setImage(bombSprite);
            System.out.println("🔥 Bomba lanzada y eliminada del inventario.");
        } else {
            System.out.println("🚫 No hay bomba en el inventario.");
        }
    }



    public void updateBombs(List<Enemy> enemies) {
        Iterator<PowerUps> bombIterator = bombs.iterator();
        while (bombIterator.hasNext()) {
            PowerUps bomb = bombIterator.next();
            bomb.update();
            bomb.setY(bomb.getY() - 5); // Mover la bomba hacia arriba

            if (bomb.getY() < 0) {
                bombIterator.remove();
                continue;
            }

            for (Enemy enemy : enemies) {
                if (bomb.getBounds().intersects(new BoundingBox(enemy.getX(), enemy.getY(), enemy.getBounds().getWidth(), enemy.getBounds().getHeight()))) {
                    activateBomb(bomb.getX(), bomb.getY()); // Llamar a la explosión en la ubicación de la bomba
                    bombIterator.remove();
                    break;
                }
            }
        }
    }


    public void updateProjectiles() {
        projectiles.removeIf(p -> p.getY() < 0);
        projectiles.forEach(Projectile::update);
    }

    public void reducirVida() {
        if (!shieldActive) {
            vidas--;
            System.out.println("La nave ha sido impactada. Vidas restantes: " + vidas);
        } else {
            System.out.println("Impacto bloqueado por el escudo.");
        }
    }

    public int getVidas() {
        return vidas;
    }

    public void draw(GraphicsContext gc) {
        gc.drawImage(shipSprite, x, y, 50, 40);

        if (shieldActive) {
            gc.drawImage(shieldSprite, x , y - 5,
                    shipSprite.getWidth() + 10, shipSprite.getHeight() + 10);
        }

        for (Projectile p : projectiles) {
            p.draw(gc);
        }
        for (PowerUps b : bombs) {
            b.draw(gc);
        }
    }


    public List<Projectile> getProjectiles() {
        return projectiles;
    }

    public void resetSpeed() {
        velocidad = 2;
    }

    public List<PowerUps> getBombs() {
        return bombs;
    }

    public double getX() { return x; }
    public double getY() { return y; }

    public int getScore() {
        return score;
    }

    public void addScore(int points) {
        score += points;
    }

    public Rectangle2D getBounds() {
        return new Rectangle2D(x, y, 50, 40);
    }

    public boolean isShieldActive() {
        return shieldActive;
    }

    public boolean isDoubleShotActive() {
        return doubleShotActive;
    }

    public boolean isBombActive() {
        return bombActive;
    }

    public void activateShield() {
        shieldActive = true;
        if (shieldTimer != null) {
            shieldTimer.cancel();
        }
        shieldTimer = new Timer();
        shieldTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    shieldActive = false;
                    inventory.resetShield(); // Clear the shield from the inventory
                });
            }
        }, 10000);
    }

    public void activateDoubleShot() {
        doubleShotActive = true;
        if (doubleShotTimer != null) {
            doubleShotTimer.cancel();
        }
        doubleShotTimer = new Timer();
        doubleShotTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    doubleShotActive = false;
                    inventory.resetDoubleShot();
                });
            }
        }, 10000);
    }

    public void activateBomb(double bombX, double bombY) {
        double bombRadius = 100; // Ajusta el radio de explosión
        List<Enemy> enemiesToRemove = new ArrayList<>();

        for (Enemy enemy : rootController.getEnemyManager().getEnemies()) {
            double distance = Math.sqrt(Math.pow(enemy.getX() - bombX, 2) + Math.pow(enemy.getY() - bombY, 2));
            if (distance <= bombRadius) {
                enemiesToRemove.add(enemy);
            }
        }

        Platform.runLater(() -> {
            for (Enemy enemy : enemiesToRemove) {
                rootController.getEnemyManager().removeEnemy(enemy);
                addScore(enemy.getPoints()); // Add points for each eliminated enemy
            }

            // Clear the bomb from the inventory and set bombActive to false
            inventory.resetBomb();
            bombActive = false;

            System.out.println("💣 Bomb exploded! Enemies eliminated: " + enemiesToRemove.size());
        });
    }


    public void deactivatePowerUps() {
        shieldActive = false;
        doubleShotActive = false;
        if (shieldTimer != null) {
            shieldTimer.cancel();
        }
        if (doubleShotTimer != null) {
            doubleShotTimer.cancel();
        }
    }
}