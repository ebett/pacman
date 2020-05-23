package org.javagames.pacman;


import java.awt.*;

import org.javagames.pacman.ai.ArtificialInteligence;
import org.javagames.pacman.ai.RandomAI;
import org.javagames.pacman.sprites.AnimatingSprite;
import org.javagames.pacman.sprites.Sprite;

/**
 * Ghost.java
 * Representa un fantasma. Es un Sprite animado.
 *
 * @author Esteban Bett (estebanbett@gmail.com)
 *
 */
public class Ghost extends Sprite {

    //Tablero
    private Board board;

    //Por default inteligencia aleatoria.
    private ArtificialInteligence ai = new RandomAI();
    private boolean isDead = false;

    public Ghost(Board board, String name) {
        super(name);
        this.board = board;
    }

    public Ghost(Board board, String name, ArtificialInteligence ai) {
        this(board, name);
        this.ai = ai;
    }

    public void setAI(ArtificialInteligence ai) {
        this.ai = ai;
    }

    /**
     * Dibujar el fantasma, con efecto de transparencia.
     */
    @Override
    public void paint(Graphics g) {
        if(isDead) return;
        Graphics2D g2 = (Graphics2D) g;
        Composite comp = g2.getComposite();
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 0.7f));
        if (image == null) {
            g2.setColor(backGroundColour);
            g2.fill3DRect(x, y + 10, width, height - 10, true);
            g2.fillArc(x, y, width, height, 0, 180);
        } else {
            g2.drawImage(image, x, y, width, height, null);
        }
        g2.setComposite(comp);
    }

    @Override
    public void step() {
        if(isDead) return;

        int pieza; //aca coloca lo que hay en el tablero en la posicion a donde va a ir.
        int a =  getVelocity();//incremento de avance

        //Calculo la posicion real del fantasma en la matriz
        final int xx = (x - Game.POSX) / Game.SIZE_SPRITE,
                yy = (y - Game.POSY) / Game.SIZE_SPRITE;

        //Me fijo si puedo continuar en la direccion actual
        if (direction == UP) {
            pieza = board.get(xx, yy - 1);
            if (pieza == Board.BLOCK) {
                direction = getNextDirection();
            } else {
                y -= a;
            }
        } else if (direction == DOWN) {
            pieza = board.get(xx, yy + 1);
            if (pieza == Board.BLOCK) {
                direction = getNextDirection();
            } else {
                y += a;
            }
        } else if (direction == LEFT) {
            pieza = board.get(xx - 1, yy);
            if (pieza == Board.BLOCK) {
                direction = getNextDirection();
            } else {
                x -= a;
            }
        } else if (direction == RIGHT) {
            pieza = board.get(xx + 1, yy);
            if (pieza == Board.BLOCK) {
                direction = getNextDirection();
            } else {
                x += a;
            }
        }
    }

    /**
     * Obtiene la siugiente posicion para ir.
     */
    private int getNextDirection() {
        while (true) {
            int dir = ai.getNextDirection();
            if (dir == 0 && direction != LEFT && direction != RIGHT) return LEFT;
            if (dir == 1 && direction != RIGHT && direction != LEFT) return RIGHT;
            if (dir == 2 && direction != UP && direction != DOWN) return UP;
            if (dir == 3 && direction != DOWN && direction != UP) return DOWN;
        }
    }

    public void dead() {
        this.isDead = true;
    }

    public boolean isDead() {
        return isDead;
    }
}
