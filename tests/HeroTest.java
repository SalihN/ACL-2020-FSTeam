import engine.Cmd;
import model.game.Hero;
import model.game.Maze;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

/**
 * @author Richer Alexis
 * @version 1.0.1
 */

public class HeroTest {
    private Hero hero;
    private Maze maze;

    @Before
    public void setUp() throws Exception {
        maze = createMock(Maze.class);
        maze.tileWidth = 32;
        maze.tileHeight = 32;
        hero = new Hero();
        hero.setPosition(new Point(50,50));
    }

    @After
    public void tearDown() throws Exception {
    }


    @Test
    public void testAddScore(){
        hero.addScore(20);

        assertEquals(20, hero.getScore());
    }

    @Test
    public void testDeleteScore(){
        hero.addScore(20);
        hero.addScore(-10);

        assertEquals(10, hero.getScore());

        hero.addScore(-50);
        assertEquals(0, hero.getScore());
    }

    @Test
    public void resetScore(){
        hero.resetScore();

        assertEquals(0, hero.getScore());
    }


    /**
     * On teste la réaction du héros s'il veut bouger alors qu'il y a un mur
     */
    @Test
    public void testCheckWall() throws IOException {
        expect(maze.isAWall(anyInt(), anyInt())).andReturn(true).anyTimes();
        expect(maze.getWidth()).andReturn(500).anyTimes();
        expect(maze.getHeight()).andReturn(500).anyTimes();
        replay(maze);

        hero.action(Cmd.LEFT, maze);
        assertEquals(new Point(50, 50), hero.getPosition());
    }

    /**
     * On teste la réaction du héros s'il veut bouger alors qu'il peut
     */
    @Test
    public void testMove() throws IOException {
        expect(maze.isAWall(anyInt(), anyInt())).andReturn(false).anyTimes();
        expect(maze.getWidth()).andReturn(500).anyTimes();
        expect(maze.getHeight()).andReturn(500).anyTimes();
        replay(maze);

        hero.action(Cmd.RIGHT, maze);
        assertEquals(new Point(50 + hero.getStats().getSpeed(), 50), hero.getPosition());
    }

    /**
     * On teste la présence d'une boule de feu
     */
    @Test
    public void testFireBall() throws IOException {
        expect(maze.isAWall(anyInt(), anyInt())).andReturn(false).anyTimes();
        expect(maze.getWidth()).andReturn(500).anyTimes();
        expect(maze.getHeight()).andReturn(500).anyTimes();
        replay(maze);

        hero.action(Cmd.SPACE, maze);
        assertEquals(1, hero.getNbFireBalls());
    }

    @Test
    public void testDestroyFireBall() throws IOException {
        expect(maze.isAWall(anyInt(), anyInt())).andReturn(true).anyTimes();
        expect(maze.getWidth()).andReturn(500).anyTimes();
        expect(maze.getHeight()).andReturn(500).anyTimes();
        expect(maze.getListMonsters()).andReturn(new ArrayList<>()).anyTimes();
        replay(maze);

        hero.action(Cmd.SPACE, maze);
        hero.moveFireball(maze);
        hero.moveFireball(maze);
        assertEquals(0, hero.getNbFireBalls());
    }


}