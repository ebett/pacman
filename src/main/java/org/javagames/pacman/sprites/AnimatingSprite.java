package org.javagames.pacman.sprites;

import java.awt.*;


/**
 * AnimatingSprite.java
 * Es un Sprite Animado.
 * Delega a las clases hijas lo que debe hacer en cada step de la animacion.
 *
 * @author Esteban Bett (eabett@yahoo.com.ar)
 */
public abstract class AnimatingSprite extends Sprite implements Runnable {

    protected boolean finish = true;
    protected boolean inPause = false;
    protected int delay = 100;
    protected Thread animation = null;

    public AnimatingSprite(String name) {
        super(name);
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public void start() {
        finish = false;
        inPause = false;
        animation = new Thread(this);
        animation.setName("Thread_" + name);
        animation.start();
    }

    public void stop() {
        finish = true;
        if (animation == null) return;
        animation.interrupt();
        if (inPause) inPause = false;
        animation = null;
    }

    public void pause(boolean b) {
        if (inPause == b) {
            return;
        }
        if (inPause) {
            synchronized (this) {
                notify();
            }
        }
        inPause = b;
    }

    public void restart() {
        stop();
        start();
    }

    public void run() {
        while (!finish) {
            if (inPause) {
                synchronized (this) {
                    try {
                        wait();
                    } catch (InterruptedException ex) {
                    }
                }

                if (finish) break;
            }

            step();

            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
            }
        }
    }
}
