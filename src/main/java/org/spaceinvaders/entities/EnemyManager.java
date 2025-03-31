package org.spaceinvaders.entities;

import javafx.application.Platform;
import javafx.scene.canvas.GraphicsContext;
import org.spaceinvaders.controllers.RootController;

import java.util.*;

public class EnemyManager {

    //Variables de enemigos y proyectiles
    private List<Enemy> enemies = new ArrayList<>();
    private List<EnemyProjectile> enemyProjectiles = new ArrayList<>();
    private double direction = 1;
    private int shootingIndex = 0; // Índice del enemigo que dispara
    private Timer enemyShootingTimer = new Timer(true);
    private RootController rootController;

    //Sistema de oleadas
    private int oleada = 1;  // Inicia en la primera oleada
    private double velocidadEnemigos = 1;  // Velocidad base de los enemigos
    private int enemigosQueDisparan = 1;  // Solo 1 enemigo dispara en la primera oleada

    //Jefe final
    private Boss boss; // Nuevo jefe
    private boolean bossDerrotado = false;
    public boolean hayBoss() {
        return boss != null;
    }

    //Constructor
    public EnemyManager(RootController rootController) {
        this.rootController = rootController; // ✅ Asignar correctamente la referencia
        createEnemies();
        scheduleEnemyShots();
    }

    public List<Enemy> getEnemies() {
        return enemies;
    }

    public void stopEnemyShooting() {
        enemyShootingTimer.cancel();
        enemyShootingTimer.purge();
    }

    public List<EnemyProjectile> getProjectiles() {
        return enemyProjectiles;
    }

    //Reiniciar velocidad de enemigos
    public void resetSpeed() {
        velocidadEnemigos = 1;  // Velocidad base de los enemigos
        enemigosQueDisparan = 1;  // Solo 1 enemigo dispara en la primera oleada
    }

    //Generar enemigos
    public void createEnemies() {
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

    //Movimientos de los enemigos
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

    //Disparos de los enemigos
    public void scheduleEnemyShots() {
        enemyShootingTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (!enemies.isEmpty()) {
                    List<Enemy> enemigosSeleccionados = new ArrayList<>();
                    Collections.shuffle(enemies); // Mezclar los enemigos

                    for (int i = 0; i < enemigosQueDisparan && i < enemies.size(); i++) {
                        enemigosSeleccionados.add(enemies.get(i));
                    }

                    for (Enemy enemy : enemigosSeleccionados) {
                        enemy.fireProjectile(enemyProjectiles);
                    }
                }
            }
        }, 1000, 1000); // Disparan cada 1 segundo
    }

    //Comprobar si hay una nueva oleada
    public void checkNextWave() {
        if (enemies.isEmpty() && boss == null) {
            if (oleada < 3) {
                rootController.stopGame(); // Pausar el juego al terminar la oleada
                Platform.runLater(() -> {
                    rootController.mostrarMensajeOleada("Oleada " + (oleada + 1));
                    rootController.mostrarBotonSiguienteOleada();
                });
            } else if (oleada == 3 && boss == null && !bossDerrotado) {
                rootController.stopGame(); // Pausar el juego antes del boss
                Platform.runLater(() -> {
                    rootController.mostrarMensajeOleada("Has llegado al boss!");
                    rootController.mostrarBotonFaseFinal();
                });
            }
        }
    }

    //Iniciar la siguiente oleada
    public void iniciarSiguienteOleada() {
        oleada++; //Incrementar oleada
        enemigosQueDisparan++; //Aumentar disparos de los enemigos

        // Limpiar las listas de proyectiles
        enemyProjectiles.clear();
        rootController.getShip().getProjectiles().clear();

        // Resetear el cooldown y desactivar poderes de la nave
        rootController.resetCooldown();
        rootController.getShip().deactivatePowerUps();

        // Resetear el inventario de la nave
        rootController.getShip().getInventory().reset();

        createEnemies(); //Generar nuevos enemigos
        scheduleEnemyShots(); //Comenzar disparos enemigos
    }

    public void iniciarBossFinal() {
        if (boss == null) { // ✅ Asegurar que no se cree múltiples veces
            boss = new Boss(550, 50);
            System.out.println("👹 ¡Ha aparecido el jefe final!");

            // Limpiar las listas de proyectiles
            enemyProjectiles.clear();
            rootController.getShip().getProjectiles().clear();

            // Resetear el cooldown y desactivar poderes de la nave
            rootController.resetCooldown();
            rootController.getShip().deactivatePowerUps();

            // Resetear el inventario de la nave
            rootController.getShip().getInventory().reset();
        }
    }


    //Actualizar proyectiles
    public void updateProjectiles(List<Projectile> playerProjectiles) {
        enemyProjectiles.removeIf(p -> p.getY() > 700);
        enemyProjectiles.forEach(EnemyProjectile::update);

        if (boss != null) {
            boss.move(); // ✅ Ahora el Boss se moverá correctamente
            if (Math.random() < 0.05) { // Dispara con probabilidad del 5% por frame
                boss.fireProjectile(enemyProjectiles);
            }
            checkBossCollision(playerProjectiles);
        }

        checkCollisions(playerProjectiles);
        checkNextWave();
    }

    //Dibujar proyectiles
    public void drawProjectiles(GraphicsContext gc) {
        for (EnemyProjectile p : enemyProjectiles) {
            p.draw(gc);
        }
    }

    //Dibujar enemigos
    public void draw(GraphicsContext gc) {
        for (Enemy enemy : enemies) {
            enemy.draw(gc);
        }
        drawProjectiles(gc);

        if (boss != null) {
            boss.draw(gc);
        }
    }

    public boolean checkCollisionWithShip(Ship ship) {
        boolean impactado = false;

        Iterator<EnemyProjectile> iterator = enemyProjectiles.iterator();
        while (iterator.hasNext()) {
            EnemyProjectile projectile = iterator.next();
            if (projectile.collidesWith(ship)) {
                iterator.remove(); // Remove the projectile upon impact
                if (!ship.isShieldActive()) {
                    System.out.println("El escudo está desactivado, reduciendo vida...");

                    ship.reducirVida(); // Reduce a life on the ship only if the shield is not active
                } else {
                    System.out.println("Impact blocked by the shield.");
                }
                impactado = true; // A collision was detected
            }
        }
        return impactado; // Return `true` if there was a collision
    }

    //Comprobar colisiones de proyectiles
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
                        int puntos = enemy.getPoints();

                        rootController.getShip().addScore(puntos); // ✅ Sumar puntos a la nave
                        System.out.println("🔥 Enemigo derrotado: +" + puntos + " puntos");
                        System.out.println("🏆 Puntuación acumulada: " + rootController.getShip().getScore()); // Add points to the player's score

                        // Generate power-up with 10% probability
                        rootController.generarPowerUp(enemy.getX(), enemy.getY());


                    }
                    break; // Un solo proyectil impacta a un enemigo
                }
            }
        }

        enemies.removeAll(enemiesToRemove);
        playerProjectiles.removeAll(projectilesToRemove);
    }

    //Comprobar colisiones con el jefe
    private void checkBossCollision(List<Projectile> playerProjectiles) {
        if (boss == null) return; //Evitar colisiones si el Boss ya fue eliminado

        List<Projectile> projectilesToRemove = new ArrayList<>();
        for (Projectile projectile : playerProjectiles) {
            if (projectile.collidesWith(boss)) {
                boss.recibirImpacto();
                projectilesToRemove.add(projectile);
                System.out.println("🔥 Boss impactado. Vida restante: " + boss.getVida());

                if (boss.estaDestruido()) {


                    int puntosBoss = boss.getPoints(); // ✅ Puntos del Boss
                    rootController.getShip().addScore(puntosBoss); // ✅ Sumar puntos del Boss a los obtenidos antes

                    System.out.println("🏆 Puntuación final (Enemigos + Boss): " + rootController.getShip().getScore());

                    boss = null;
                    bossDerrotado = true;
                    System.out.println("🎉 ¡Has derrotado al jefe! ¡Nivel completado!");
                    rootController.bossDefeated();
                    break;
                }
            }
        }
        playerProjectiles.removeAll(projectilesToRemove);
    }

    //Eliminar enemigos
    public void removeEnemy(Enemy enemy) {
        enemies.remove(enemy);
    }

}
