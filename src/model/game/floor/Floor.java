package model.game.floor;

import model.game.GameObject;

import java.awt.*;

/**
 * @author Emanuel Gady, Alexis Richer, Goetz Alexandre
 * @version 1.0.1
 *
 * Ensemble des cases du labyrinthe
 */
public abstract class Floor extends GameObject {

    public Floor(Point p, int w, int h){
        position = p;
        width = w;
        height = h;
    }

    public Point getPosition() {
        return position;
    }

    public boolean isTreasorFloor(){
        return false;
    }

    public boolean isActivateFloor(){
        return false;
    }

    public boolean isWall(){
        return false;
    }
}
