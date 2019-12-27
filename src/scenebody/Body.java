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
import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.geom.GeneralPath;
import other.Convenience;
import main.Sprite;

/**
 *
 * @author Jonatan Larsson
 * @author Mikael Persson
 */
public abstract class Body extends Sprite {

    public final body.Body farmBody;
    public final Vector2D position;
    final Vector2D velocity;

    private static final Color[] AVAILABLE_COLORS, AVAILABLE_STROKE_COLORS;
    final Color color, strokeColor;
    final GeneralPath shape;

    public final BodyShadow shadow;

    static {
        AVAILABLE_COLORS = new Color[]{Color.CYAN.darker(), Color.GREEN, Color.BLUE, Color.PINK, Color.MAGENTA};
        AVAILABLE_STROKE_COLORS = new Color[AVAILABLE_COLORS.length];
        for (int i = 0; i < AVAILABLE_COLORS.length; i++) {
            AVAILABLE_STROKE_COLORS[i] = AVAILABLE_COLORS[i].darker();
        }
    }

    public Body(body.Body farmBody, GeneralPath shape) {
        this.farmBody = farmBody;
        position = this.farmBody.position;
        velocity = this.farmBody.velocity;

        int colorIndex = Convenience.randomInt(0, AVAILABLE_COLORS.length - 1);
        color = AVAILABLE_COLORS[colorIndex];
        strokeColor = AVAILABLE_STROKE_COLORS[colorIndex];
        this.shape = shape;

        shadow = new BodyShadow(this);
    }

    public void applyResistance(double maxResistance) {
        double speed = velocity.mag();
        if (speed == 0) {
            return;
        }
        Vector2D resistance = Vector2D.flip(velocity);
        resistance.setMag(Math.min(speed, maxResistance));
        farmBody.applyForce(resistance);
    }

    public void setVelocity(Vector2D velocity) {
        this.velocity.set(velocity);
    }

    public void stop() {
        setVelocity(new Vector2D());
    }

    public abstract void checkWarping(Vector2D boundary);
    
    public abstract boolean isOutsideBoundary(Vector2D boundary);

    public abstract boolean containsMouse(Vector2D mousePos);

    public void processLight(Vector2D lightSource) {
        Vector2D lightDir = Vector2D.drawing(lightSource, position);
        shadow.setPosition(lightDir);
    }

    @Override
    protected final void paint(Graphics2D g, double secondsSinceUpdate) {
        translateGraphicsWithVec(g, approximatedVisualPos(secondsSinceUpdate));
        g.rotate(approximatedVisualAngle(secondsSinceUpdate));
        
        g.setColor(color);
        g.fill(shape);
        g.setStroke(new BasicStroke(4));
        g.setColor(strokeColor);
        g.draw(shape);
    }

    Vector2D approximatedVisualPos(double secondsSinceUpdate) {
        Vector2D approximatedVisualPos = new Vector2D(position);
        approximatedVisualPos.add(Vector2D.multiplication(velocity, secondsSinceUpdate));
        return approximatedVisualPos;
    }
    
    double approximatedVisualAngle(double secondsSinceUpdate) {
        double approximatedVisualAngle = farmBody.angle;
        return approximatedVisualAngle;
    }

}
