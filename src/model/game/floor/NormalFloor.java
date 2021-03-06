package model.game.floor;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * @author Emanuel Gady, Alexis Richer
 * @version 1.0.1
 *
 * Case normale du labyrinthe
 */
public class NormalFloor extends Floor {

    public NormalFloor(Point p, int w, int h) throws IOException {
        super(p, w, h);
        im = ImageIO.read(new File("resources/images/normalfloor.png"));
    }
}
