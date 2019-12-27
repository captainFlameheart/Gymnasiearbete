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

import convenience.Vector2D;
import other.Convenience;
import main.Sprite;
import java.awt.Color;
import java.awt.Graphics2D;

/**
 *
 * @author Jonatan Larsson
 * @author Mikael Persson
 */
public final class BodyShadow extends Sprite {

    private static final Color COLOR = Convenience.alphaAlteredColor(Color.BLACK, 50);
    
    private final Body CREATOR;
    private final Vector2D OFFSET = new Vector2D();
    private final double OFFSET_LIMIT;

    BodyShadow(Body creator) {
        CREATOR = creator;
        OFFSET_LIMIT = 20;
    }
    
    void setPosition(Vector2D lightBeamVec) {
        OFFSET.set(CREATOR.position);
        OFFSET.set(Vector2D.magLimiting(lightBeamVec, OFFSET_LIMIT));
    }
    
    @Override
    public void paint(Graphics2D g, double secondsSinceUpdate) {
        translateGraphicsWithVec(g, approximatedVisualPos(secondsSinceUpdate));
        g.rotate(approximatedVisualAngle(secondsSinceUpdate));
        g.setColor(COLOR);
        g.fill(CREATOR.shape);
    }
    
    private Vector2D approximatedVisualPos(double secondsSinceUpdate) {
        return Vector2D.addition(CREATOR.approximatedVisualPos(secondsSinceUpdate), OFFSET);
    }
    
    private double approximatedVisualAngle(double secondsSinceUpdate) {
        return CREATOR.approximatedVisualAngle(secondsSinceUpdate);
    }

}
