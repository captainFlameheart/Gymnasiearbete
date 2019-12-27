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
package scenebody;

import java.awt.Graphics2D;
import main.Sprite;

/**
 *
 * @author Jonatan Larsson
 * @author Mikael Persson
 */
class Vertex extends Sprite {

    private final double RADIUS;

    Vertex(double radius) {
        RADIUS = radius;
    }

    @Override
    protected void paint(Graphics2D g, double secondsSinceUpdate) {
    }

}
