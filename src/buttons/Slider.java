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
import other.Convenience;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.util.EnumMap;

/**
 *
 * @author Jonatan Larsson
 * @author Mikael Persson
 */
abstract class Slider extends Button {

    private final double MIN_VALUE, MAX_VALUE;
    private double value;

    private final Vector2D START, STOP;
    private final double LENGTH;
    private final Vector2D DIRECTION_UNIT_VECTOR;

    private final Vector2D BLOB_POSITION = new Vector2D();
    private final double BLOB_RADIUS;

    private final double MOUSE_CONTAINMENT_DIST_SQUARED;

    private final Line2D LINE;
    private final Ellipse2D BLOB;
    private final EnumMap<State, Color> COLOR_MAP = new EnumMap<>(State.class);

    Slider(double minValue, double maxValue, double value, Vector2D start, Vector2D stop, double blobRadius,
            Color defaultColor, Color hoveredOverColor, Color pressedColor) {
        
        MIN_VALUE = minValue;
        MAX_VALUE = maxValue;
        this.value = value;
        
        START = new Vector2D(start);
        STOP = new Vector2D(stop);
        DIRECTION_UNIT_VECTOR = Vector2D.drawing(START, STOP);
        LENGTH = DIRECTION_UNIT_VECTOR.mag();
        DIRECTION_UNIT_VECTOR.div(LENGTH);
        BLOB_RADIUS = blobRadius;

        MOUSE_CONTAINMENT_DIST_SQUARED = 2 * BLOB_RADIUS * BLOB_RADIUS;

        LINE = new Line2D.Double(START.x, START.y, STOP.x, STOP.y);
        BLOB = Convenience.circleAtOrigin(BLOB_RADIUS);
        COLOR_MAP.put(State.DEFAULT, defaultColor);
        COLOR_MAP.put(State.HOVERED_OVER, hoveredOverColor);
        COLOR_MAP.put(State.PRESSED, pressedColor);

        setBlobPosition(Convenience.map(value, 0, MAX_VALUE, 0, LENGTH));
    }

    @Override
    public void processMousePos(Vector2D mousePos) {
        super.processMousePos(mousePos);
        if (isPressed()) {
            beControlled(mousePos);
        }
    }

    @Override
    boolean canBecomeDefault() {
        return (getState() == State.HOVERED_OVER);
    }

    @Override
    boolean containsMouse(Vector2D mousePos) {
        double pixelValueFromMouse = pixelValueFromMouse(mousePos);
        Vector2D closestPoint = pointFromPixelValue(pixelValueFromMouse);
        return (closestPoint.distSquared(mousePos) < MOUSE_CONTAINMENT_DIST_SQUARED);
    }

    private void beControlled(Vector2D mousePos) {
        double pixelValue = pixelValueFromMouse(mousePos);
        value = Convenience.map(pixelValue, 0, LENGTH, MIN_VALUE, MAX_VALUE);
        setBlobPosition(pixelValue);
    }

    private void setBlobPosition(double pixelValue) {
        BLOB_POSITION.set(pointFromPixelValue(pixelValue));
    }

    private double pixelValueFromMouse(Vector2D mousePos) {
        Vector2D relativeMousePos = Vector2D.drawing(START, mousePos);
        double pixelValue = relativeMousePos.dot(DIRECTION_UNIT_VECTOR);
        pixelValue = Convenience.constrain(pixelValue, 0, LENGTH);
        return pixelValue;
    }

    private Vector2D pointFromPixelValue(double pixelValue) {
        Vector2D point = Vector2D.multiplication(DIRECTION_UNIT_VECTOR, pixelValue);
        point.add(START);
        return point;
    }

    double getValue() {
        return value;
    }

    @Override
    public void paint(Graphics2D g, double secondsSinceUpdate) {
        g.setColor(Color.BLACK);
        g.setStroke(new BasicStroke(2));
        g.draw(LINE);
        translateGraphicsWithVec(g, BLOB_POSITION);
        g.setColor(color());
        g.fill(BLOB);
        g.setStroke(new BasicStroke(4));
        g.setColor(Color.BLACK);
        g.draw(BLOB);
    }

    private Color color() {
        return COLOR_MAP.get(getState());
    }

}
