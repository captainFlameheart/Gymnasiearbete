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

import body.CircularBodySeed;
import convenience.Vector2D;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import main.Scene;
import main.Sprite;
import other.Convenience;

/**
 *
 * @author Jonatan Larsson
 * @author Mikael Persson
 */
public final class CircularBodyTemplate extends Sprite {

    private final Scene USED_SCENE;

    private final Vector2D POSITION = new Vector2D();
    private static final double MAX_RADIUS = 100, MIN_RADIUS = 5;
    private double radius;

    private final Color COLOR = new Color(255, 0, 255, 100);
    private Ellipse2D shape = new Ellipse2D.Double();

    public CircularBodyTemplate(Scene usedScene, double radius) {
        USED_SCENE = usedScene;
        increaseRadius(radius);
    }

    public void setPosition(Vector2D position) {
        POSITION.set(position);
    }

    public void increaseRadius(double radiusChange) {
        radius += radiusChange;
        radius = Convenience.constrain(radius, MIN_RADIUS, MAX_RADIUS);
        shape = Convenience.circleAtOrigin(this.radius);
    }

    public double getRadius() {
        return radius;
    }

    public void createBody() {
        CircularBodySeed bodySeed = new CircularBodySeed();
        bodySeed.setBodyPosition(POSITION);
        bodySeed.setBodyRadius(radius);
        bodySeed.setDefaultBodyDensity();
        USED_SCENE.addCircularBody(bodySeed);
    }

    @Override
    protected void paint(Graphics2D g, double secondsSinceUpdate) {
        translateGraphicsWithVec(g, POSITION);
        g.setColor(COLOR);
        g.fill(shape);
    }

}
