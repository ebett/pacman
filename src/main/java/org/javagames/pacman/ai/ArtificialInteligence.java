/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.javagames.pacman.ai;

/**
 * Modela el comportamiento de la heuristica de un personaje.
 * 
 * @author Esteban Bett
 */
public interface ArtificialInteligence {

    /** Determina la siguiente posicion para ir */
    int getNextDirection();
}
