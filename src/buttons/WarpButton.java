/*
 * Copyright (C) 2019 Jonatan Larsson, Mikael Persson
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package buttons;

import convenience.Vector2D;
import main.Scene;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import other.Convenience;

/**
 *
 * @author Jonatan Larsson
 * @author Mikael Persson
 */
public final class WarpButton extends RectangularButton {

    private final Scene USED_SCENE;

    private static final Vector2D DEFAULT_HALF_SIZE, HOVERED_OVER_HALF_SIZE, PRESSED_HALF_SIZE;
    private static final Color DEFAULT_COLOR, HOVERED_OVER_COLOR, PRESSED_COLOR;
    private static final BufferedImage WARP_IMAGE, NON_WARP_IMAGE;

    static {
        DEFAULT_HALF_SIZE = new Vector2D(80, 20);
        HOVERED_OVER_HALF_SIZE = new Vector2D(90, 20);
        PRESSED_HALF_SIZE = new Vector2D(80, 25);

        DEFAULT_COLOR = Color.YELLOW;
        HOVERED_OVER_COLOR = DEFAULT_COLOR.darker();
        PRESSED_COLOR = Color.ORANGE;

        WARP_IMAGE = Convenience.imageFromPathName("images/warp.PNG");
        NON_WARP_IMAGE = Convenience.imageFromPathName("images/nonwarp.PNG");
    }

    public WarpButton(Vector2D position, Scene usedScene) {
        super(position, DEFAULT_HALF_SIZE, HOVERED_OVER_HALF_SIZE, PRESSED_HALF_SIZE, DEFAULT_COLOR, HOVERED_OVER_COLOR, PRESSED_COLOR);
        USED_SCENE = usedScene;
        setImage();
    }

    @Override
    void performAction() {
        USED_SCENE.switchBodyWarping();
        setImage();
    }

    private void setImage() {
        setImage((USED_SCENE.bodyWarping()) ? NON_WARP_IMAGE : WARP_IMAGE);
    }

}
