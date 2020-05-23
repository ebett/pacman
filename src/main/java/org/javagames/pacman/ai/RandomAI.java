package org.javagames.pacman.ai;

import java.util.Random;

/**
 * RandomAI.java
 * Determina en forma aleatoria la proxima posicion.
 * 
 * @author Esteban Bett
 */
public final class RandomAI implements ArtificialInteligence {

    private final Random rnd = new Random(System.currentTimeMillis());

    /**      
     * @return un entero de 0 a 3, indicando a que posicion ir
     */
    public int getNextDirection(){        
        return rnd.nextInt(4);
    }

}
