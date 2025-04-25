package org.spaceinvaders.models;

import javafx.scene.image.Image;

public class DefenseWall {
    private double x, y; // Position of the wall
    private double width, height; // Size of the wall
    private int resistencia; // Number of hits the wall can take
    private Image[] wallImages; // Array of images for the wall
    private Image currentImage; // Current image of the wall

    public DefenseWall(double x, double y, double width, double height, int resistencia) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.resistencia = resistencia;

        // Cargar las imágenes de los muros
        wallImages = new Image[]{
                new Image(getClass().getResourceAsStream("/images/muros/MuroBase.png")),  // Resistencia 20
                new Image(getClass().getResourceAsStream("/images/muros/MuroHit1.png")),  // Resistencia 19
                new Image(getClass().getResourceAsStream("/images/muros/MuroHit2.png")),  // Resistencia 18
                new Image(getClass().getResourceAsStream("/images/muros/MuroHit3.png")),  // Resistencia 17
                new Image(getClass().getResourceAsStream("/images/muros/MuroHit4.png")),  // Resistencia 16
                new Image(getClass().getResourceAsStream("/images/muros/MuroHit5.png")),  // Resistencia 15
                new Image(getClass().getResourceAsStream("/images/muros/MuroHit6.png")),  // Resistencia 14
                new Image(getClass().getResourceAsStream("/images/muros/MuroHit7.png")),  // Resistencia 13
                new Image(getClass().getResourceAsStream("/images/muros/MuroHit8.png")),  // Resistencia 12
                new Image(getClass().getResourceAsStream("/images/muros/MuroHit9.png")),  // Resistencia 11
                new Image(getClass().getResourceAsStream("/images/muros/MuroHit10.png")),  // Resistencia 10
                new Image(getClass().getResourceAsStream("/images/muros/MuroHit11.png")),  // Resistencia 9
                new Image(getClass().getResourceAsStream("/images/muros/MuroHit12.png")),  // Resistencia 8
                new Image(getClass().getResourceAsStream("/images/muros/MuroHit13.png")),  // Resistencia 7
                new Image(getClass().getResourceAsStream("/images/muros/MuroHit14.png")),  // Resistencia 6
                new Image(getClass().getResourceAsStream("/images/muros/MuroHit15.png")),  // Resistencia 5
                new Image(getClass().getResourceAsStream("/images/muros/MuroHit16.png")),  // Resistencia 4
                new Image(getClass().getResourceAsStream("/images/muros/MuroHit17.png")),  // Resistencia 3
                new Image(getClass().getResourceAsStream("/images/muros/MuroHit18.png")),  // Resistencia 2
                new Image(getClass().getResourceAsStream("/images/muros/MuroHit20.png")),  // Resistencia 1

        };

        currentImage = wallImages[0]; // Imagen inicial (intacto)
    }

    public double getX() { return x; }
    public double getY() { return y; }
    public double getWidth() { return width; }
    public double getHeight() { return height; }
    public int getResistencia() { return resistencia; }
    public Image getCurrentImage() { return currentImage; }

    public void recibirImpacto() {
        if (resistencia > 0) {
            resistencia--;
            updateImage(); // Actualizar la imagen cuando reciba daño
        }
    }

    public boolean estaDestruido() {
        return resistencia <= 0;
    }

    private void updateImage() {
        int index = Math.max(0, Math.min(20 - resistencia, wallImages.length - 1)); // Ajustar el índice al rango válido
        currentImage = wallImages[index];
    }


    public boolean checkCollision(Projectile projectile) {
        if (projectile.getBounds().intersects(x, y, width, height)) {
            projectile.setActive(false); // La bala desaparece al chocar con el muro pero no lo daña
            return true;
        }
        return false;
    }

    public boolean checkCollision(EnemyProjectile enemyProjectile) {
        if (enemyProjectile.getBounds().intersects(x, y, width, height)) {
            recibirImpacto(); // Reduce la resistencia del muro
            enemyProjectile.setActive(false); // Desactivar la bala enemiga
            return true;
        }
        return false;
    }

    public boolean checkCollision(Enemy enemy) {
        if (enemy.getBounds().intersects(x, y, width, height)) {
            resistencia = 0; // El muro es destruido inmediatamente
            updateImage(); // Asegurar que desaparezca
            return true;
        }
        return false;
    }
}