package org.javagames.pacman;


import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;

import org.javagames.pacman.sprites.Sprite;

/**
 * PacMan.java
 * Representa el PacMan. Es un Sprite animado.
 *
 * @author Esteban Bett (eabett@yahoo.com.ar)
 */
public final class PacMan extends Sprite {

    private static final int CLOSE = 0;
    private static final int OPEN = 1;
    private static final int TIME_INVENSIBLE = 15;
    private static final int VELOCITY = 10;
    private static final int VELOCITY_INVENSIBLE = 20;
    private static final Color COLOR = Color.YELLOW;
    private static final Color COLOR_INVENSIBLE = Color.ORANGE.darker();

    //Forma el pacman usando un Arc2D y variando su angulo.
    private Arc2D shape = new Arc2D.Float(Arc2D.PIE);
    private int angleStart = 45;
    private int angleExtent = 270;
    //Puede estar cerrando o abriendose.
    private int mouth = CLOSE;
    private boolean invensible = false;
    private Long tinvensible = null;

    private Board board;

    public PacMan() {
        super("PacMan", COLOR);
    }

    public PacMan(Board board) {
        this();
        this.board = board;
        this.velocity = VELOCITY;
    }

    /**
     * Segun la direccion, hay que dibujar el Pacman mirando hacia el lado correcto.
     *
     * @return el proximo angulo
     */
    private double nextAngle() {
        double angle = 0.0;
        switch (direction) {
            case DOWN:
                angle = 90.0;
                break;
            case LEFT:
                angle = 180.0;
                break;
            case UP:
                angle = 270.0;
                break;
            default:
                angle = 0.0;
        }
        return angle;
    }

    /**
     * Dibuja el Pacman.
     */
    @Override
    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        if (g2 == null) return;
        shape.setAngleStart(angleStart);
        shape.setAngleExtent(angleExtent);
        int a = 1, b = -3;
        shape.setFrame(x + a, y + b, width - 1, height - 2);

        //Crea virtualmente el pacman rotado hacia el lado correcto.
        AffineTransform saved = g2.getTransform();
        AffineTransform rotate = AffineTransform.getRotateInstance(Math.toRadians(nextAngle()), x + a + Game.SIZE_SPRITE / 2, y + b + Game.SIZE_SPRITE / 2);
        g2.transform(rotate);

        g2.setColor(backGroundColour);
        g2.fill(shape);
        g2.setTransform(saved);
    }

    // @Override
    public void step() {
        int pieza; //aca coloca lo que hay en el tablero en la posicion a donde va a ir.
        int a = getVelocity(); //incremento de avance.
        final long t = System.currentTimeMillis();
        if (invensible && ((t - tinvensible.longValue()) / 1000 > TIME_INVENSIBLE)) {
            setInvensible(false);
        }

        //Calculo la posicion real del pacman en la matriz
        final int xx = (x - Game.POSX) / Game.SIZE_SPRITE,
                yy = (y - Game.POSY) / Game.SIZE_SPRITE;

        //Si hay pildora la como.
        if (board.get(xx, yy) == Board.PILDORA) {
            board.eatPildora(xx, yy);
        }

        //Si hay fruta la como.
        if (board.get(xx, yy) == Board.FRUIT) {
            board.eatFruit(xx, yy);
        }

        //Si hay pildora invensible la como.
        if (board.get(xx, yy) == Board.PILDORA_INVENSIBLE) {
            board.eatPildoraInvensible(xx, yy);
            setInvensible(true);
        }

        //Actualizo el tamaño de la boca mientras se abre o se cierra.
        if (mouth == CLOSE) {
            angleStart -= 5;
            angleExtent += 10;
        }

        if (mouth == OPEN) {
            angleStart += 5;
            angleExtent -= 10;
        }

        if (angleStart == 0) {
            mouth = OPEN;
        }

        if (angleStart > 45) {
            mouth = CLOSE;
        }

        //Me fijo si puedo continuar en la direccion actual
        if (direction == UP) {
            pieza = board.get(xx, yy - 1);
            if (pieza == Board.BLOCK) {
                return;
                //direction = getNextDirection();
            }
            y -= a;
        } else if (direction == DOWN) {
            pieza = board.get(xx, yy + 1);
            if (pieza == Board.BLOCK) {
                return;//direction = getNextDirection();
            }
            y += a;
        } else if (direction == LEFT) {
            pieza = board.get(xx - 1, yy);
            if (pieza == Board.BLOCK) {
                return;//direction = getNextDirection();
            }
            if (pieza == Board.PORTAL_IZ) {
                Point p = board.getPortalDERPosition();
                x = p.x * Game.SIZE_SPRITE + Game.POSX;
            }
            x -= a;
        } else if (direction == RIGHT) {
            pieza = board.get(xx + 1, yy);
            if (pieza == Board.BLOCK) {
                return;//direction = getNextDirection();
            }
            if (pieza == Board.PORTAL_DER) {
                Point p = board.getPortalIZPosition();
                x = p.x * Game.SIZE_SPRITE + Game.POSX;
            }
            x += a;
        }
    }

    public void setInvensible(boolean b) {
        tinvensible = b ? System.currentTimeMillis() : null;
        this.velocity = b ? VELOCITY_INVENSIBLE : VELOCITY;
        setBackGround(b ? COLOR_INVENSIBLE: COLOR);
        this.invensible = b;
    }

    public boolean isInvensible() {
        return invensible;
    }
}
