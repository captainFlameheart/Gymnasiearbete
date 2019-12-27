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
import java.awt.Color;
import java.awt.image.BufferedImage;
import main.MouseProcesser;
import main.MouseProcesser.Mode;
import other.Convenience;

/**
 *
 * @author Jonatan Larsson
 * @author Mikael Persson
 */
class MouseModeButton extends RectangularButton {

    private final MouseProcesser USED_MOUSE_PROCESSER;
    private final Mode MODE;

    private static final Vector2D DEFAULT_HALF_SIZE, HOVERED_OVER_HALF_SIZE, PRESSED_HALF_SIZE;
    private static final Color DEFAULT_COLOR, HOVERED_OVER_COLOR, PRESSED_COLOR;

    static {
        DEFAULT_HALF_SIZE = new Vector2D(30, 30);
        HOVERED_OVER_HALF_SIZE = Vector2D.multiplication(DEFAULT_HALF_SIZE, 1.3);
        PRESSED_HALF_SIZE = Vector2D.multiplication(HOVERED_OVER_HALF_SIZE, 1.2);

        DEFAULT_COLOR = Color.WHITE;
        HOVERED_OVER_COLOR = DEFAULT_COLOR.darker();
        PRESSED_COLOR = HOVERED_OVER_COLOR.darker();
    }

    MouseModeButton(Vector2D position, BufferedImage image, MouseProcesser usedMouseProcesser, Mode mode) {
        super(position, DEFAULT_HALF_SIZE, HOVERED_OVER_HALF_SIZE, PRESSED_HALF_SIZE, DEFAULT_COLOR, HOVERED_OVER_COLOR, PRESSED_COLOR);
        setImage(image);
        USED_MOUSE_PROCESSER = usedMouseProcesser;
        MODE = mode;
    }

    @Override
    void performAction() {
        USED_MOUSE_PROCESSER.setMode(MODE);
    }

}
