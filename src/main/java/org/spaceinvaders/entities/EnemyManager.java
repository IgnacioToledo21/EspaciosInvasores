package org.spaceinvaders.entities;

import javafx.scene.canvas.GraphicsContext;

import java.util.*;

public class EnemyManager {

    private List<Enemy> enemies = new ArrayList<>();
    private List<EnemyProjectile> enemyProjectiles = new ArrayList<>();
    private double direction = 1;
    private int shootingIndex = 0; // Índice del enemigo que dispara
    private Timer enemyShootingTimer = new Timer(true);


    public EnemyManager() {
        createEnemies();
        scheduleEnemyShots(); // Configurar disparos cada 1 segundo
    }

    private void createEnemies() {
        int rows = 5;
        int cols = 10;
        double startX = 50;
        double startY = 50;
        Random random = new Random();

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                int tipo = random.nextInt(3) + 1;
                enemies.add(new Enemy(startX + col * 50, startY + row * 40, tipo));
            }
        }
    }

    public void moveEnemies() {
        boolean atEdge = false;

        for (Enemy enemy : enemies) {
            enemy.move(direction);

            if (enemy.getX() <= 10 || enemy.getX() >= 1200 - 40) {
                atEdge = true;
            }
        }

        if (atEdge) {
            for (Enemy enemy : enemies) {
                enemy.moveDown();
            }
            direction *= -1;
        }
    }

    //  Dispara cada 1 segundo un enemigo a la vez
    public void scheduleEnemyShots() {
        enemyShootingTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (!enemies.isEmpty()) {
                    int randomIndex = new Random().nextInt(enemies.size());
                    Enemy enemy = enemies.get(randomIndex);
                    enemy.fireProjectile(enemyProjectiles);
                }
            }
        }, 1000, 1000);
    }




    public void checkCollisions(List<Projectile> playerProjectiles) {
        List<Enemy> enemiesToRemove = new ArrayList<>();
        List<Projectile> projectilesToRemove = new ArrayList<>();

        for (Enemy enemy : enemies) {
            for (Projectile projectile : playerProjectiles) {
                if (projectile.collidesWith(enemy)) {
                    enemy.recibirImpacto();
                    projectilesToRemove.add(projectile);

                    if (enemy.estaDestruido()) {
                        enemiesToRemove.add(enemy);
                    }
                    break; // Un solo proyectil impacta a un enemigo
                }
            }
        }

        enemies.removeAll(enemiesToRemove);
        playerProjectiles.removeAll(projectilesToRemove);
    }


    public void updateProjectiles(List<Projectile> playerProjectiles) {
        System.out.println("📌 Proyectiles enemigos en juego: " + enemyProjectiles.size());

        enemyProjectiles.removeIf(p -> p.getY() > 700);
        enemyProjectiles.forEach(EnemyProjectile::update);

        checkCollisions(playerProjectiles);

    }

    public void drawProjectiles(GraphicsContext gc) {
        for (EnemyProjectile p : enemyProjectiles) {
            p.draw(gc);
        }
    }

    public void draw(GraphicsContext gc) {
        for (Enemy enemy : enemies) {
            enemy.draw(gc);
        }
        drawProjectiles(gc);
    }

    public void stopEnemyShooting() {
        enemyShootingTimer.cancel();
        enemyShootingTimer.purge();
    }


    public List<EnemyProjectile> getProjectiles() {
        return enemyProjectiles;
    }

    //Comprobar colisiones con la nave del jugador
    public boolean checkCollisionWithShip(Ship ship) {
        boolean impactado = false; // Variable para saber si hubo colisión

        Iterator<EnemyProjectile> iterator = enemyProjectiles.iterator();
        while (iterator.hasNext()) {
            EnemyProjectile projectile = iterator.next();
            if (projectile.collidesWith(ship)) {
                iterator.remove(); // Eliminar el proyectil al impactar
                ship.reducirVida(); // Reducir una vida en la nave (opcional)
                impactado = true; // Se detectó una colisión
            }
        }
        return impactado; // ✅ Devuelve `true` si hubo colisión
    }
}
