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
import static java.lang.Math.sqrt;
import other.Convenience;

/**
 *
 * @author Jonatan Larsson
 * @author Mikael Persson
 */
public final class PolygonBody extends Body {

    private final double maxExtent;

    public PolygonBody(body.PolygonBody farmBody) {
        super(farmBody, Convenience.relativeShapeOfFarmPolygonBody(farmBody));

        Vector2D[] relativeVertices = farmBody.copyRelativeVertices();
        double maxExtentSquared = 0;
        for (Vector2D relativeVertex : relativeVertices) {
            double extentSquared = relativeVertex.magSquared();
            if (extentSquared > maxExtentSquared) {
                maxExtentSquared = extentSquared;
            }
        }
        maxExtent = sqrt(maxExtentSquared);
    }

    @Override
    public void checkWarping(Vector2D boundary) {
        if (position.x - maxExtent > boundary.x) {
            position.x = -maxExtent;
        } else if (position.x + maxExtent < 0) {
            position.x = boundary.x + maxExtent;
        }
        if (position.y - maxExtent > boundary.y) {
            position.y = -maxExtent;
        } else if (position.y + maxExtent < 0) {
            position.y = boundary.y + maxExtent;
        }
    }

    @Override
    public boolean isOutsideBoundary(Vector2D boundary) {
        return position.x - maxExtent > boundary.x
                || position.y - maxExtent > boundary.y
                || position.x + maxExtent < 0
                || position.y + maxExtent < 0;
    }

    @Override
    public boolean containsMouse(Vector2D mousePos) {
        return farmBody.containsPoint(mousePos);
    }

}
