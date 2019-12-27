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

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import convenience.Vector2D;
import java.awt.BasicStroke;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.EnumMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author Jonatan Larsson
 * @author Mikael Persson
 */
abstract class RectangularButton extends Button {

    private final Vector2D POSITION;

    private final EnumMap<State, StateAssociation> STATE_ASSOCIATION_MAP = new EnumMap<>(State.class);

    private BufferedImage image;
    private final Vector2D IMAGE_HALF_SIZE = new Vector2D();

    RectangularButton(Vector2D position,
            Vector2D defaultHalfSize, Vector2D hoveredOverHalfSize, Vector2D pressedHalfSize,
            Color defaultColor, Color hoveredOverColor, Color pressedColor) {

        POSITION = position;

        STATE_ASSOCIATION_MAP.put(State.DEFAULT, new StateAssociation(new Vector2D(defaultHalfSize), defaultColor));
        STATE_ASSOCIATION_MAP.put(State.HOVERED_OVER, new StateAssociation(new Vector2D(hoveredOverHalfSize), hoveredOverColor));
        STATE_ASSOCIATION_MAP.put(State.PRESSED, new StateAssociation(new Vector2D(pressedHalfSize), pressedColor));
    }

    Vector2D position() {
        return new Vector2D(POSITION);
    }

    @Override
    boolean containsMouse(Vector2D mousePos) {
        Vector2D relativeMousePos = Vector2D.drawing(POSITION, mousePos);
        Vector2D halfSize = halfSize();
        return (relativeMousePos.x < halfSize.x && relativeMousePos.y < halfSize.y
                && relativeMousePos.x > -halfSize.x && relativeMousePos.y > -halfSize.y);
    }

    private Vector2D halfSize() {
        return stateAssociation().HALF_SIZE;
    }

    @Override
    boolean canBecomeDefault() {
        return true;
    }

    final void setImage(BufferedImage image) {
        this.image = image;
        IMAGE_HALF_SIZE.set(image.getWidth() / 2, image.getHeight() / 2);
    }

    @Override
    public void paint(Graphics2D g, double secondsSinceUpdate) {
        Rectangle2D shape = shape();

        translateGraphicsWithVec(g, POSITION);
        g.setColor(color());
        g.fill(shape);
        g.setColor(Color.BLACK);
        g.setStroke(new BasicStroke(3));
        g.draw(shape);
        translateGraphicsWithVec(g, Vector2D.flip(IMAGE_HALF_SIZE));
        g.drawImage(image, 0, 0, null);
    }

    private Color color() {
        return stateAssociation().COLOR;
    }

    Rectangle2D shape() {
        return stateAssociation().SHAPE;
    }

    private StateAssociation stateAssociation() {
        return STATE_ASSOCIATION_MAP.get(getState());
    }

    private class StateAssociation {

        private final Vector2D HALF_SIZE;
        private final Rectangle2D SHAPE;
        private final Color COLOR;

        public StateAssociation(Vector2D halfSize, Color color) {
            HALF_SIZE = halfSize;
            SHAPE = new Rectangle2D.Double(-HALF_SIZE.x, -HALF_SIZE.y, 2 * HALF_SIZE.x, 2 * HALF_SIZE.y);
            COLOR = color;
        }

    }

}
