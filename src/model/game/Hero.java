package model.game;

import engine.Cmd;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * @author Alexis Richer, Emanuel Gady, Goetz Alexandre
 * @version 2.0.1
 *
 * Heros present dans le labyrinthe
 */

public class Hero extends MovingObject {

    private Point heroStartingPos;
    private boolean isInvincible;
    private boolean isCatched;
    private final File image;
    private final File image2;

    public enum Orientation{UP,DOWN,RIGHT,LEFT}
    private Orientation orientation;
    private double animeCap;
    public static int score;


    private Collection<FireBall> fireBalls;
    private boolean launchedFireBall;

    private int timeOfInvincibility; //En millisecondes

    public Hero() throws IOException {
        stats = new Stats(5,5);
        heroStartingPos = new Point(0,0);
        position = new Point(0,0);
        width = 20;
        height = 20;
        score = 0;
        nbAnimation=16;
        orientation = Orientation.DOWN;
        animeCap = System.currentTimeMillis();

        isInvincible = false;
        isCatched = false;

        timeOfInvincibility = 500;

        image = new File("resources/images/newhero.png");
        image2 = new File("resources/images/newheroinvincible.png");

        im = ImageIO.read(image);

        fireBalls = new ArrayList<>();
        launchedFireBall = true;
    }

    /**
     * Déplacement du héro en fonction de la commande donnée dans le labyrinth
     * @param commande commande reçu par le clavier
     * @param maze labyrinthe dans lequel le héro évolue
     */
    public void action(Cmd commande, Maze maze) throws IOException {
        int x=0;
        int y=0;

        if(commande == Cmd.UP) {
            y -= this.getStats().getSpeed();
        }
        if(commande == Cmd.DOWN){
            y += this.getStats().getSpeed();
        }
        if(commande == Cmd.LEFT){
            x -= this.getStats().getSpeed();
        }
        if(commande == Cmd.RIGHT){
            x +=  this.getStats().getSpeed();
        }
        if(commande == Cmd.SPACE && launchedFireBall && !isCatched){
            fireBalls.add(new FireBall(this,position.x, position.y));
            Maze.sound("fire.wav");
            launchedFireBall = false;
            Timer timer = new Timer();
            TimerTask decount = new TimerTask() {
                @Override
                public void run() {
                    launchedFireBall = true;
                }
            };
            timer.schedule(decount, 500);
        }
        if(System.currentTimeMillis() - animeCap > 1000/12) {
            updateAnimation(commande);
            animeCap = System.currentTimeMillis();
        }
        if(this.checkWall(x,y,maze) && !isCatched){
            moveTo(x,y,maze);
        }
    }

    private void updateAnimation(Cmd commande){
        if(commande == Cmd.UP){
            orientation = Orientation.UP;
            if(currentAnimation == 10)
                currentAnimation = 11;
            else
                currentAnimation = 10;
        }
        if(commande == Cmd.DOWN){
            orientation = Orientation.DOWN;
            if(currentAnimation == 1)
                currentAnimation = 2;
            else
                currentAnimation = 1;
        }
        if(commande == Cmd.LEFT){
            orientation = Orientation.LEFT;
            if(currentAnimation == 7)
                currentAnimation = 8;
            else
                currentAnimation = 7;
        }
        if(commande == Cmd.RIGHT){
            orientation = Orientation.RIGHT;
            if(currentAnimation == 4)
                currentAnimation = 5;
            else
                currentAnimation = 4;
        }
        if(commande == Cmd.IDLE){
            if(orientation == Orientation.UP)
                currentAnimation = 9;
            if(orientation == Orientation.DOWN)
                currentAnimation = 0;
            if(orientation == Orientation.LEFT)
                currentAnimation = 6;
            if(orientation == Orientation.RIGHT)
                currentAnimation = 3;

        }
    }

    @Override
    public void draw(BufferedImage im) {
        super.draw(im);
        for(FireBall f : fireBalls){
            f.draw(im);
        }
    }

    public void takeDammages() throws IOException {
        if(!isInvincible() && !isCatched()){
            getStats().hit(1);
            addScore(-20);
            setInvincible(true);
            Maze.sound("ouf.wav");
            Timer timer = new Timer();
            TimerTask decount = new TimerTask() {
                @Override
                public void run() {
                    try {
                        setInvincible(false);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            };
            timer.schedule(decount, getTimeOfInvincibility());
        }
    }


    /**
     * Ajoute du score
     * @param score
     */
    public void addScore(int score) {
        Hero.score = Math.max(0, Hero.score + score);
    }

    /**
     * Réinitialise le compteur de score
     */
    public void resetScore(){
        score = 0;
    }


    /**
     * Permet de faire bouger les boules de feu
     * @param maze
     */
    public void moveFireball(Maze maze) throws IOException {
        Iterator iterator = fireBalls.iterator();
        while (iterator.hasNext()){
            FireBall f = (FireBall) iterator.next();
            if(!f.isDestroyed()) {
                f.move(maze);
            } else {
                iterator.remove();
            }
        }
    }

    /**
     *
     * @return La position à laquelle le héro commence le labyrinthe
     */
    public Point getHeroStartingPos() {
        return heroStartingPos;
    }

    /**
     *
     * @param heroStartingPos position à laquelle le héro commence le labyrinthe
     */
    public void setHeroStartingPos(Point heroStartingPos) {
        this.heroStartingPos = heroStartingPos;
    }

    /**
     * Permet de savoir sur le heros est mort ou non
     * @return retourne 0 si les points de vie du héro son inférieur ou égal à 0
     */
    public boolean isDead(){
        return stats.getHp() <= 0;
    }

    /**
     *
     * @return l'état invicible du héro
     */
    public boolean isInvincible() {
        return isInvincible;
    }

    /**
     * change le sprite du héro quand il est en invincibilité
     * @param invincible nouvel état
     * @throws IOException Impossible de lire l'image
     */
    public void setInvincible(boolean invincible) throws IOException {
        isInvincible = invincible;
        if(invincible){
            im = ImageIO.read(image2);
        } else {
            im = ImageIO.read(image);
        }
    }

    /**
     *
     * @return temps d'invincibilité du héro
     */
    public int getTimeOfInvincibility() {
        return timeOfInvincibility;
    }

    /**
     *
     * @return Héro attrapé par un monstre ou non
     */
    public boolean isCatched() {
        return isCatched;
    }

    /**
     * Permet de changer l'état d'attrapé à non attrapé
     * @param catched nouvelle valeur pour isCatched
     */
    public void setCatched(boolean catched) {
        isCatched = catched;
    }

    public int getScore() {
        return score;
    }

    /**
     * @return la direction dans laquelle regarde le heros
     */
    public Orientation getOrientation() {
        return orientation;
    }

    public int getNbFireBalls() {
        return fireBalls.size();
    }
}
