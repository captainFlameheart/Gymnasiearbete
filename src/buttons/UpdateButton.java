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

import main.Panel;
import java.awt.Color;
import convenience.Vector2D;
import java.awt.image.BufferedImage;
import other.Convenience;

/**
 *
 * @author Jonatan Larsson
 * @author Mikael Persson
 */
public final class UpdateButton extends RectangularButton {

    private final Panel USED_PANEL;
    private double secondsPerUpdate;

    private static final Color DEFAULT_COLOR, HOVERED_OVER_COLOR, PRESSED_COLOR;
    private static final Vector2D DEFAULT_HALF_SIZE, HOVERED_OVER_HALF_SIZE, PRESSED_HALF_SIZE;
    private static final BufferedImage IMAGE;

    static {
        DEFAULT_COLOR = Color.CYAN;
        HOVERED_OVER_COLOR = Color.GRAY;
        PRESSED_COLOR = Color.GREEN;

        DEFAULT_HALF_SIZE = new Vector2D(70, 20);
        HOVERED_OVER_HALF_SIZE = new Vector2D(80, 20);
        PRESSED_HALF_SIZE = new Vector2D(75, 20);

        IMAGE = Convenience.imageFromPathName("images/update.PNG");
    }

    public UpdateButton(Vector2D position, Panel controlledScenePanel) {
        super(position, DEFAULT_HALF_SIZE, HOVERED_OVER_HALF_SIZE, PRESSED_HALF_SIZE, DEFAULT_COLOR, HOVERED_OVER_COLOR, PRESSED_COLOR);
        USED_PANEL = controlledScenePanel;

        setImage(IMAGE);
    }

    void setSecondsPerUpdate(double secondsPerUpdate) {
        this.secondsPerUpdate = secondsPerUpdate;
    }

    @Override
    void performAction() {
        USED_PANEL.updateScene(secondsPerUpdate);
        USED_PANEL.repaint();
    }

}
