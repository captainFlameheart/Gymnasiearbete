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
import java.awt.image.BufferedImage;
import main.MouseProcesser;
import other.Convenience;

/**
 *
 * @author Jonatan Larsson
 * @author Mikael Persson
 */
public final class PolygonBodyButton extends MouseModeButton {

    private static final BufferedImage IMAGE = Convenience.imageFromPathName("images/polygonbody.PNG");

    public PolygonBodyButton(Vector2D position, MouseProcesser usedMouseProcesser) {
        super(position, IMAGE, usedMouseProcesser, MouseProcesser.Mode.ADDING_POLYGON_BODY);
    }

}
