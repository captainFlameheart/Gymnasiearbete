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

import main.Sprite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import convenience.Vector2D;
import java.awt.BasicStroke;
import scenebody.Body;

/**
 *
 * @author Jonatan Larsson
 * @author Mikael Persson
 */
public final class ForceArrow extends Sprite {

    private final Body BODY;

    private final Vector2D TIP_POSITION;
    private final double TIP_LINE_ANGLE = .8 * Math.PI;

    private float textureDistance;
    private final double TEXTURE_SPEED = 50;

    public ForceArrow(Body body) {
        BODY = body;
        TIP_POSITION = new Vector2D(BODY.position);
    }

    public void setTipPosition(Vector2D tipPosition) {
        TIP_POSITION.set(tipPosition);
    }

    public void applyForce() {
        BODY.setVelocity(force());
    }

    private Vector2D force() {
        return Vector2D.drawing(BODY.position, TIP_POSITION);
    }

    public void moveTexture(double seconds) {
        textureDistance += seconds * TEXTURE_SPEED;
    }

    @Override
    public void paint(Graphics2D g, double secondsSinceUpdate) {
        g.setStroke(new BasicStroke(4, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{10, 5}, textureDistance));
        g.setColor(Color.RED);
        g.draw(mainLine());
        for (Line2D tipLine : tipLines()) {
            g.draw(tipLine);
        }
    }

    private Line2D mainLine() {
        Vector2D tailPos = BODY.position;
        return new Line2D.Double(TIP_POSITION.x, TIP_POSITION.y, tailPos.x, tailPos.y);
    }

    private Line2D[] tipLines() {
        Line2D[] tipLines = new Line2D[2];
        Vector2D nonRotatedTipLineVec = Vector2D.multiplication(force(), .2);
        tipLines[0] = tipLine(nonRotatedTipLineVec, true);
        tipLines[1] = tipLine(nonRotatedTipLineVec, false);
        return tipLines;
    }

    private Line2D tipLine(Vector2D nonRotatedTipLineVec, boolean positiveAngle) {
        Vector2D tipLineVec = Vector2D.rotation(nonRotatedTipLineVec, (positiveAngle) ? TIP_LINE_ANGLE : -TIP_LINE_ANGLE);
        Vector2D tipLineEnd = Vector2D.addition(TIP_POSITION, tipLineVec);
        return new Line2D.Double(TIP_POSITION.x, TIP_POSITION.y, tipLineEnd.x, tipLineEnd.y);
    }

}
