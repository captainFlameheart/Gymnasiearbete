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

import java.awt.Color;
import convenience.Vector2D;
import java.awt.RadialGradientPaint;
import java.awt.geom.GeneralPath;
import other.Convenience;

/**
 *
 * @author Jonatan Larsson
 * @author Mikael Persson
 */
public final class CircularBody extends Body {

//    public final body.CircularBody farmBody;
//    private final Vector2D position;
//    private final Vector2D velocity;
    final double RADIUS;
    private final double DIAMETER;
//    final Ellipse2D CIRCLE;

//    private static final Color[] AVAILABLE_COLORS, AVAILABLE_STROKE_COLORS;
//    private final Color color, strokeColor;
    private static final float[] PAINT_FRACTIONS = {0, 1};
    private final Color[] PAINT_COLORS;
    private final Vector2D RELATIVE_SHINE_POSITION = new Vector2D();
    private RadialGradientPaint paint;

//    public final BodyShadow shadow;

    static {
//        AVAILABLE_COLORS = new Color[]{Color.RED, Color.GREEN, Color.BLUE, Color.GRAY, Color.MAGENTA};
//        AVAILABLE_STROKE_COLORS = new Color[AVAILABLE_COLORS.length];
//        for (int i = 0; i < AVAILABLE_COLORS.length; i++) {
//            AVAILABLE_STROKE_COLORS[i] = AVAILABLE_COLORS[i].darker();
//        }
    }

    public CircularBody(body.CircularBody farmBody) {
        super(farmBody, new GeneralPath(Convenience.circleAtOrigin(farmBody.radius)));
//        farmBody = farmBody;
//        position = farmBody.position;
//        velocity = farmBody.velocity;
        RADIUS = farmBody.radius;
        DIAMETER = 2 * RADIUS;
//        CIRCLE = new Ellipse2D.Double(-radius, -radius, DIAMETER, DIAMETER);

//        int colorIndex = Convenience.randomInt(0, AVAILABLE_COLORS.length - 1);
//        color = AVAILABLE_COLORS[colorIndex];
//        strokeColor = AVAILABLE_STROKE_COLORS[colorIndex];
        PAINT_COLORS = new Color[]{Color.WHITE, color};

//        shadow = new BodyShadow(this);
    }
    
//    public Vector2D position() {
//        return new Vector2D(position);
//    }

//    public void applyResistance(double maxResistance) {
//        double speed = farmBody.velocity.mag();
//        if (speed == 0) {
//            return;
//        }
//        Vector2D resistance = Vector2D.flip(velocity);
//        resistance.setMag(Math.min(speed, maxResistance));
//        farmBody.applyForce(resistance);
//    }

//    public void setVelocity(Vector2D velocity) {
//        velocity.set(velocity);
//    }

    @Override
    public void checkWarping(Vector2D boundary) {
        if (position.x - RADIUS > boundary.x) {
            position.x = -RADIUS;
        } else if (position.x < -RADIUS) {
            position.x = boundary.x + RADIUS;
        }
        if (position.y - RADIUS > boundary.y) {
            position.y = -RADIUS;
        } else if (position.y < -RADIUS) {
            position.y = boundary.y + RADIUS;
        }
    }

    @Override
    public boolean isOutsideBoundary(Vector2D boundary) {
        return (position.x - RADIUS > boundary.x || position.y - RADIUS > boundary.y || position.x + RADIUS < 0 || position.y + RADIUS < 0);
    }

    @Override
    public boolean containsMouse(Vector2D mousePos) {
        return position.dist(mousePos) < RADIUS;
    }

 

//    @Override
//    public void paint(Graphics2D g, double secondsSinceUpdate) {
//        translateGraphicsWithVec(g, approximatedVisualPos(secondsSinceUpdate));
//        updatePaint();
//        g.setPaint(paint);
//        g.fill(CIRCLE);
//        g.setStroke(new BasicStroke(5));
//        g.setColor(strokeColor);
//        g.draw(CIRCLE);
//    }
    
//    Vector2D approximatedVisualPos(double secondsSinceUpdate) {
//        Vector2D approximatedVisualPos = new Vector2D(position());
//        approximatedVisualPos.add(Vector2D.multiplication(velocity(), secondsSinceUpdate));
//        return approximatedVisualPos;
//    }

    private void updatePaint() {
        paint = new RadialGradientPaint((float) RELATIVE_SHINE_POSITION.x, (float) RELATIVE_SHINE_POSITION.y, (float) RADIUS, PAINT_FRACTIONS, PAINT_COLORS);
    }

}
