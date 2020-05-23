package org.javagames.pacman;


import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import javax.swing.*;

import org.javagames.pacman.sprites.Sprite;

/**
 * GraphicsManager.java
 * Maneja los graficos y prioridades de dibujo de Sprites.
 *
 * @author Esteban Bett (estebanbett@gmail.com)
 */
public final class GraphicsManager implements Runnable {

    private JFrame window;

    private Graphics2D g2 = null;

    /**
     * Buffer virtual para dibujar
     */
    private BufferedImage buffer = null;

    /**
     * Mantiene un hash de tres listas de Sprites. Una por cada prioridad.
     */
    private HashMap<Integer, ArrayList<Sprite>> sprites = new HashMap<>(3);

    private int delay = 130;

    private boolean running = false;

    private Thread gameThread;

    /**
     * Constructor privado para respetar el singleton
     */
    public GraphicsManager(JFrame frame) {
        this.window = frame;
        sprites.put(Thread.MAX_PRIORITY, new ArrayList<>());
        sprites.put(Thread.NORM_PRIORITY, new ArrayList<>());
        sprites.put(Thread.MIN_PRIORITY, new ArrayList<>());
    }

    /**
     * Registra un sprite con prioridad definida
     */
    public void registerSprite(Sprite s, int priority) {
        if (priority != Thread.MAX_PRIORITY &&
                priority != Thread.MIN_PRIORITY &&
                priority != Thread.NORM_PRIORITY) {
            priority = Thread.NORM_PRIORITY;
        }
        sprites.get(priority).add(s);
    }

    public void paintWindow(Graphics g) {
        if (buffer == null) {
            buffer = createGraphics(window.getWidth(), window.getHeight());
        }

        g.drawImage(buffer, 0, 0, window.getWidth(), window.getHeight(), null);
    }

    public void start() {
        if (!running) {
            running = true;
            gameThread = new Thread(this);
            gameThread.start();
        }
    }

    public void stop() {
        running = false;
        Optional.ofNullable(gameThread).ifPresent(Thread::interrupt);
    }

    /**
     * Dibuja los sprites segun su prioridad
     */
    public void run() {
        if (buffer == null) {
            buffer = createGraphics(window.getWidth(), window.getHeight());
        }
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
        }
        while (running) {
            for (Sprite s : sprites.get(Thread.MAX_PRIORITY)) s.paint(g2);
            for (Sprite s : sprites.get(Thread.NORM_PRIORITY)) s.paint(g2);
            for (Sprite s : sprites.get(Thread.MIN_PRIORITY)) s.paint(g2);
            window.repaint();
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
            }
        }
    }

    private BufferedImage createGraphics(int w, int h) {
        if (buffer == null) {
            buffer = window.getGraphicsConfiguration().createCompatibleImage(w, h, Transparency.BITMASK);
            //buffer = window.getGraphicsConfiguration().createCompatibleImage(w, h, Transparency.TRANSLUCENT);
            g2 = createGraphics2D();
        }
        return buffer;
    }

    private Graphics2D createGraphics2D() {
        Graphics2D g = buffer.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_SPEED);

        return g;
    }

    public JFrame getJFrame() {
        return window;
    }
}
