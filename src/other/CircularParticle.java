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
package other;

import convenience.Vector2D;
import java.awt.Color;

/**
 *
 * @author Jonatan Larsson
 * @author Mikael Persson
 */
final class CircularParticle extends Particle {

    private final double RADIUS;
    private final Color INITIAL_COLOR;

    public CircularParticle(Vector2D position, Vector2D velocity, int initialHP, double radius, Color initialColor) {
        super(position, velocity, initialHP, Convenience.circleAtOrigin(radius));
        RADIUS = radius;
        SCALING.set(RADIUS, RADIUS);
        INITIAL_COLOR = initialColor;
    }

    @Override
    void processHp(double hp) {
        paint = Convenience.defaultRadialGradientPaint(1,
                Convenience.alphaAlteredColor(INITIAL_COLOR, (int) Math.round(Convenience.map(hp, 0, INITIAL_HP, hp, hp))), Convenience.alphaAlteredColor(INITIAL_COLOR, 0));
    }

}
