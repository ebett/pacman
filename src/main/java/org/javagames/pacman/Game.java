package org.javagames.pacman;


import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.font.TextLayout;
import java.util.stream.Stream;
import javax.swing.*;

import org.javagames.pacman.sprites.AnimatingSprite;
import org.javagames.pacman.sprites.Sprite;

/**
 * @author Esteban Bett (estebanbett@gmail.com)
 */
public final class Game extends AnimatingSprite implements KeyListener {

    public static final int SIZE_SPRITE = 20;
    public static final int POSX = 10, POSY = 30;
    public static final int MAX_GHOSTS = 4;

    private int fruitTime = 12000;
    private int level = 0;
    private int score = 0;

    private long t1 = System.currentTimeMillis();

    private PacMan pacMan;

    private Ghost[] ghost = new Ghost[MAX_GHOSTS];

    private Board board;

    private Sprite[] sprites = new Sprite[MAX_GHOSTS + 1];

    private GraphicsManager graphicsManager;

    private JFrame frame;

    /**
     *
     */
    public Game(GraphicsManager graphicsManager) {
        super("PacMan-Game");
        this.graphicsManager = graphicsManager;
        this.frame = graphicsManager.getJFrame();
        this.frame.addKeyListener(this);
        setSize(SIZE_SPRITE * Board.NUM_COLUMNS, SIZE_SPRITE * Board.NUM_ROWS);
        setDelay(150);
        //Creating the board
        board = new Board(this);

        //Creating ghosts
        for (int i = 0; i < MAX_GHOSTS; i++) {
            ghost[i] = new Ghost(board, "Ghost " + i);
            ghost[i].setImage(ImageLoader.getImage("ghost" + i));
            sprites[i] = ghost[i];
        }
        //Creating pacman
        pacMan = new PacMan(board);
        sprites[MAX_GHOSTS] = pacMan;

        //Register Sprites for painting
        graphicsManager.registerSprite(this, Thread.NORM_PRIORITY);
        graphicsManager.registerSprite(board, Thread.MAX_PRIORITY);
        for (Sprite s : sprites) graphicsManager.registerSprite(s, Thread.MIN_PRIORITY);

        init();
    }

    private void init() {
        score = 0;

        board.init();

        pacMan.setBounds((SIZE_SPRITE * 13) + POSX, (SIZE_SPRITE * 28) + POSY, SIZE_SPRITE, SIZE_SPRITE);

        ghost[0].setBounds((SIZE_SPRITE * 13) + POSX, (SIZE_SPRITE * 13) + POSY, SIZE_SPRITE, SIZE_SPRITE);
        ghost[0].setDirection(Sprite.DOWN);

        ghost[1].setBounds((SIZE_SPRITE * 13) + POSX, (SIZE_SPRITE * 14) + POSY, SIZE_SPRITE, SIZE_SPRITE);
        ghost[1].setDirection(Sprite.UP);

        ghost[2].setBounds((SIZE_SPRITE * 14) + POSX, (SIZE_SPRITE * 13) + POSY, SIZE_SPRITE, SIZE_SPRITE);
        ghost[2].setDirection(Sprite.LEFT);

        ghost[3].setBounds((SIZE_SPRITE * 14) + POSX, (SIZE_SPRITE * 14) + POSY, SIZE_SPRITE, SIZE_SPRITE);
        ghost[3].setDirection(Sprite.RIGHT);

        //Setea la velocidad de los fantasmas segun el nivel, cada vez mas rapido.
        for (Ghost g : ghost) g.setVelocity((level + 1) * 10);
    }

    /**
     * Comienza la animacion del Juego y los sprites
     */
    public void startGame() {
        start();
    }

    /**
     * Fin del juego, detiene la animacion del Juego y los sprites
     */
    public void endGame() {
        stop();
        init();
    }

    /**
     * Fin del programa.
     */
    public void exitGame() {
        endGame();
        graphicsManager.stop();
    }

    public void incScore() {
        score++;
        if (score >= 323) {
            level++;
            finish = true;
        }
    }

    public void incScore(int value) {
        score += value;
    }

    /**
     * Implementa el metodo que dibuja la info del juego
     */
    @Override
    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.WHITE);

        if (inPause) {
            TextLayout tl = new TextLayout("Press P to continue...", new Font("Arial", Font.TRUETYPE_FONT, 22), g2.getFontRenderContext());
            tl.draw(g2, (float) (width / 2 - tl.getBounds().getWidth() / 2), (float) (height / 2 - tl.getBounds().getHeight() / 2));
        }

        if (finish) {
            TextLayout tl = new TextLayout("GAME OVER", new Font("Arial", Font.TRUETYPE_FONT, 28), g2.getFontRenderContext());
            tl.draw(g2, (float) (width / 2 - tl.getBounds().getWidth() / 2), (float) (height / 2 - tl.getBounds().getHeight() / 2));

            TextLayout tl2 = new TextLayout("Press S to START", new Font("Arial", Font.TRUETYPE_FONT, 28), g2.getFontRenderContext());
            tl2.draw(g2, (float) (width / 2 - tl2.getBounds().getWidth() / 2), (float) (height / 2 - tl2.getBounds().getHeight() / 2) + 80);

            TextLayout tl3 = new TextLayout("Press Q to End", new Font("Arial", Font.TRUETYPE_FONT, 28), g2.getFontRenderContext());
            tl3.draw(g2, (float) (width / 2 - tl3.getBounds().getWidth() / 2), (float) (height / 2 - tl3.getBounds().getHeight() / 2) + 130);
        }

        TextLayout tl = new TextLayout("Score: " + score, new Font("Arial", Font.TRUETYPE_FONT, 18), g2.getFontRenderContext());
        tl.draw(g2, (float) (width / 2 - tl.getBounds().getWidth() / 2), 43);

        tl = new TextLayout("Level: " + level, new Font("Arial", Font.TRUETYPE_FONT, 18), g2.getFontRenderContext());
        tl.draw(g2, (float) (width - tl.getBounds().getWidth() - 30), 43);
    }

    /**
     * Implementa el metodo que se ejecuta en cada iteracion de la animacion.
     */
    @Override
    public void step() {
        for (Sprite s : sprites) s.step();

        for (Ghost g : ghost) {
            if (!g.isDead() && pacMan.intersects(g)) {
                if (pacMan.isInvensible()) {
                    incScore(20);
                    board.eatGhost((int) pacMan.getX(), (int) pacMan.getY());
                    g.dead();
                } else {
                    endGame();
                    return;
                }
            }
        }

        //Chequeo si es tiempo de mostrar la fruta
        final long t2 = System.currentTimeMillis();
        if ((t2 - t1) >= fruitTime) {
            board.createFruit();
            t1 = t2;
        }
    }

    //Manejo los eventos de teclado
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        if (finish){
            if (code == KeyEvent.VK_S) {
                 startGame();
            }
        } else if (code == KeyEvent.VK_UP || code == KeyEvent.VK_DOWN ||
                code == KeyEvent.VK_RIGHT || code == KeyEvent.VK_LEFT) {
            pacMan.setDirection(code);
        } else if (code == KeyEvent.VK_P) {
            if (inPause) {
                inPause = false;
                synchronized (this) {
                    notify();
                }
                //for (AnimatingSprite s : animSprites) s.pause(false);
            } else {
                // for (AnimatingSprite s : animSprites) s.pause(true);
                inPause = true;
            }
        }  else if (code == KeyEvent.VK_ESCAPE) {
            endGame();
        }

    }

    public void keyReleased(KeyEvent e) {

    }

    public void keyTyped(KeyEvent e) {

    }


}
