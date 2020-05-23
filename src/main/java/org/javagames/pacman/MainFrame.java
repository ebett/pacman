package org.javagames.pacman;


import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.*;

/**
 * MainFrame.java
 * Clase principal
 *
 * @author Esteban Bett (eabett@yahoo.com.ar)
 */
public class MainFrame extends JFrame {
    //Mantiene el graphics manager para redibujar la ventana
    private GraphicsManager graphicsManager;

    MainFrame() {
        setLayout(null);
        graphicsManager = new GraphicsManager(this);
        final Game game = new Game(graphicsManager);
        Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width - game.width) / 2, 20, game.width + 20, game.height + 20);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent evt) {
                game.exitGame();
                System.exit(0);
            }
        });
        setBackground(Color.BLACK);

        setResizable(false);
    }

    @Override
    public void setVisible(boolean b) {
        super.setVisible(b);
        if (b) {
            graphicsManager.start();
        }
    }

    @Override
    public void paint(Graphics g) {
        graphicsManager.paintWindow(g);
    }

    public static void main(String args[]) {
        System.setProperty("sun.java2d.translaccel", "true");
        System.setProperty("sun.java2d.ddforcevram", "true");
        System.setProperty("sun.java2d.opengl", "true");
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainFrame().setVisible(true);
            }
        });
    }
}
