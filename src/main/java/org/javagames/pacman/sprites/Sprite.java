package org.javagames.pacman.sprites;

import java.awt.*;
import java.awt.event.KeyEvent;


/**
 * Sprite.java
 * Es un objeto con posicion, tamaño y velocidad
 * Delega a las clases hijas el paint del Sprite.
 *
 * @author Esteban Bett (estebanbett@gmail.com)
 */
public abstract class Sprite extends Rectangle {
    //Direcciones posibles
    public static final int RIGHT = KeyEvent.VK_RIGHT;
    public static final int LEFT = KeyEvent.VK_LEFT;
    public static final int DOWN = KeyEvent.VK_DOWN;
    public static final int UP = KeyEvent.VK_UP;

    protected Color backGroundColour = Color.BLACK;

    protected String name = "Sprite";

    protected Image image;

    protected int direction = KeyEvent.VK_LEFT;

    protected int velocity = 10;

    public Sprite(String name) {
        this.name = name;
    }

    public Sprite(String name, Color bgcolor) {
        this(name);
        this.backGroundColour = bgcolor;
    }

    public Sprite(String name, Image image) {
        this(name);
        this.image = image;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public int getDirection() {
        return direction;
    }

    public Color getBackGround() {
        return backGroundColour;
    }

    public void setBackGround(Color bgColor) {
        this.backGroundColour = bgColor;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    /**
     * Las clases hijas deben definir como dibujar el sprite
     */
    public abstract void paint(Graphics g);

    /**
     * Delega a las clases hijas definir que hacer en cada step de la animacion.
     */
    public abstract void step();

    public void setVelocity(int velocity){
        this.velocity = velocity;
    }

    public int getVelocity() {
        return velocity;
    }
}
