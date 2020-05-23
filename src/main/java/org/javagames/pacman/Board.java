package org.javagames.pacman;


import java.awt.*;

import org.javagames.pacman.sprites.Sprite;

/**
 * Board.java
 * Representa el Tablero. Es un Sprite.
 *
 * @author Esteban Bett (estebanbett@gmail.com)
 */
public final class Board extends Sprite {

    public static final int NUM_ROWS = 31, NUM_COLUMNS = 28;

    /**
     * Tipos de contenido de las celdas
     */
    public static final int
            BLOCK = 0,
            PILDORA = 1,
            GHOST = 2,
            FRUIT = 3,
            PACMAN = 4,
            VOID = 5,
            PORTAL_IZ = 6,
            PORTAL_DER = 7,
            PILDORA_INVENSIBLE = 8;

    /**
     * Tablero
     */
    private int[][] tablero = new int[NUM_COLUMNS][NUM_ROWS];

    /**
     * La imagen del tablero reescaled (no encontre una que se ajuste al tamaño)
     */
    private Image scaledImage;

    private Game game;

    public Board(Game game) {
        //super("Board", ImageLoader.getImage("bgImage"));
        super("Board", Color.BLACK);
        setSize(Game.SIZE_SPRITE * NUM_COLUMNS, Game.SIZE_SPRITE * NUM_ROWS);
        this.game = game;
        if (image != null) {
            scaledImage = image.getScaledInstance(width - 5, height - 25, 0);
        }
        for (int i = 0; i < NUM_COLUMNS; i++) {
            for (int j = 0; j < NUM_ROWS; j++) {
                tablero[i][j] = BLOCK;
            }
        }
        init();
    }

    public void createFruit() {
        tablero[14][17] = FRUIT;
    }

    public void eatPildora(int x, int y) {
        tablero[x][y] = VOID;
        game.incScore();
    }

    public void eatPildoraInvensible(int x, int y) {
        tablero[x][y] = VOID;
        game.incScore(30);
    }

    public void eatFruit(int x, int y) {
        tablero[x][y] = VOID;
        game.incScore(50);
    }

    public Point getPortalIZPosition() {
        return new Point(0, 14);
    }

    public Point getPortalDERPosition() {
        return new Point(NUM_COLUMNS - 1, 14);
    }

    /**
     * Coloca los bloques en sus posiciones
     */
    public void init() {

        for (int i = 0; i < 28; i++) {
            if (i != 13 && i != 14) {
                tablero[i][1] = tablero[i][20] = tablero[i][28] = PILDORA;
            }

            if (i == 1 || i == 6 || i == 12 || i == 15 || i == 21 || i == 26) {
                tablero[i][2] = tablero[i][3] = tablero[i][4] = tablero[i][21] = tablero[i][22] = PILDORA;
            }

            tablero[i][5] = PILDORA;

            if (i == 1 || i == 6 || i == 9 || i == 18 || i == 21 || i == 26) {
                tablero[i][6] = tablero[i][7] = PILDORA;
            }

            if (i != 7 && i != 8 && i != 13 && i != 14 && i != 19 && i != 20) {
                tablero[i][8] = tablero[i][26] = PILDORA;
            }

            if (i == 6 || i == 12 || i == 15 || i == 21) {
                tablero[i][9] = tablero[i][10] = PILDORA;
            }

            if (i == 6 || (i >= 9 && i <= 18) || i == 21) {
                tablero[i][11] = tablero[i][17] = PILDORA;
            }

            if (i == 6 || i == 9 || i == 18 || i == 21) {
                tablero[i][12] = tablero[i][13] = tablero[i][15] = tablero[i][16] = tablero[i][18] = tablero[i][19] = PILDORA;
            }

            if (i != 4 && i != 5 && i != 22 && i != 23) {
                tablero[i][23] = PILDORA;
            }

            if (i == 3 || i == 6 || i == 9 || i == 18 || i == 21 || i == 24) {
                tablero[i][24] = tablero[i][25] = PILDORA;
            }
        }

        for (int i = 0; i < NUM_COLUMNS; i++) {
            if (i < 10 || i > 17) {
                tablero[i][14] = VOID;
            }
            if (i == 1 || i == 12 || i == 15 || i == 26) {
                tablero[i][27] = PILDORA;
            }
            if (i > 10 && i < 17) {
                tablero[i][13] = tablero[i][14] = tablero[i][15] = VOID;
            }
        }

        tablero[13][12] = tablero[14][12] = tablero[14][28] = tablero[13][28] = VOID;
        tablero[0][14] = PORTAL_IZ;
        tablero[NUM_COLUMNS - 1][14] = PORTAL_DER;
        tablero[0][0] = PILDORA_INVENSIBLE;
        tablero[0][NUM_ROWS - 1] = PILDORA_INVENSIBLE;
        tablero[NUM_COLUMNS - 1][0] = PILDORA_INVENSIBLE;
        tablero[NUM_COLUMNS - 1][NUM_ROWS - 1] = PILDORA_INVENSIBLE;
    }

    public int get(int i, int j) {
        if (i == 0 && j == 14) return PORTAL_IZ;
        if (i == (NUM_COLUMNS - 1) && j == 14) return PORTAL_DER;

        return (i < 0 || i >= NUM_COLUMNS || j < 0 || j >= NUM_ROWS) ? BLOCK : tablero[i][j];
    }

    public void paint(Graphics g) {
        if (scaledImage != null) {
            g.drawImage(scaledImage, Game.POSX, Game.POSY, null);
            //g.drawImage(imgFondo, Game.POSX+5, Game.POSY, game.getWidth()-5, game.getHeight()-22, null);
            //g.drawImage(imgFondo, Game.POSX+5, Game.POSY, game.getWidth()-5, game.getHeight()-25, null);
        } else {
            g.setColor(getBackGround());
            g.fillRect(Game.POSX, Game.POSY, width, height);
        }

        for (int i = 0; i < NUM_COLUMNS; i++) {
            for (int j = 0; j < NUM_ROWS; j++) {
                if (tablero[i][j] == BLOCK) {
                    g.setColor(Color.DARK_GRAY);
                    g.fillRoundRect((i * Game.SIZE_SPRITE) + Game.POSX + 5, (j * Game.SIZE_SPRITE) + Game.POSY + 2, Game.SIZE_SPRITE - 12, Game.SIZE_SPRITE - 12, 0, 360);
                } else if (tablero[i][j] == PILDORA) {
                    g.setColor(Color.YELLOW.darker());
                    g.fillArc((i * Game.SIZE_SPRITE) + Game.POSX + 5, (j * Game.SIZE_SPRITE) + Game.POSY + 2, Game.SIZE_SPRITE - 12, Game.SIZE_SPRITE - 12, 0, 360);
                } else if (tablero[i][j] == PILDORA_INVENSIBLE) {
                    g.setColor(Color.RED.darker());
                    g.fillArc((i * Game.SIZE_SPRITE) + Game.POSX + 5, (j * Game.SIZE_SPRITE) + Game.POSY + 2, Game.SIZE_SPRITE - 8, Game.SIZE_SPRITE - 8, 0, 360);
                } else if (tablero[i][j] == PORTAL_IZ || tablero[i][j] == PORTAL_DER) {
                    g.setColor(Color.GREEN.darker());
                    g.fill3DRect(i * Game.SIZE_SPRITE + Game.POSX + 5, (j * Game.SIZE_SPRITE) + Game.POSY - 6, 5, Game.SIZE_SPRITE + 5, true);
                } else if (tablero[i][j] == FRUIT) {
                    g.setColor(Color.PINK);
                    g.fillArc((i * Game.SIZE_SPRITE) + Game.POSX + 5, (j * Game.SIZE_SPRITE) + Game.POSY, Game.SIZE_SPRITE - 8, Game.SIZE_SPRITE - 8, 0, 360);
                }
            }
        }
    }

    @Override
    public void step() {

    }

    public void eatGhost(int x, int y) {
        final int xx = (x - Game.POSX) / Game.SIZE_SPRITE,
                yy = (y - Game.POSY) / Game.SIZE_SPRITE;
        tablero[xx][yy] = VOID;
    }
}
