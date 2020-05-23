package org.javagames.pacman;


import java.awt.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;


/**
 * ImageLoader.java
 * Carga y mantiene un hash de imagenes para los Sprites.
 *
 * @author Esteban Bett (estebanbett@gmail.com)
 */
public class ImageLoader {

    private static Map<String, Image> images = new HashMap<>();

    static {
        //images.put("bgImage", loadImage("/images/bg4.JPG"));
        images.put("ghost0", loadImage("/images/ghostCyan.JPG"));
        images.put("ghost1", loadImage("/images/ghostOrange.JPG"));
        images.put("ghost2", loadImage("/images/ghostPink.JPG"));
        images.put("ghost3", loadImage("/images/ghostRed.JPG"));
    }

    public static Image getImage(String name) {
        return images.get(name);
    }

    public static Image loadImage(String path) {
        Image img = null;
        try {
            img = ImageIO.read(ImageLoader.class.getResource(path));
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return img;
    }
}
