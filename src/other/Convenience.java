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

import java.awt.Point;
import convenience.Vector2D;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.RadialGradientPaint;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author Jonatan Larsson
 * @author Mikael Persson
 */
public final class Convenience {

    private Convenience() {
    }

    private static final float[] DEFAULT_RADIAL_GRADIENT_PAINT_FRACTIONS = {0, 1};

    public static Vector2D pointToVector(Point p) {
        return new Vector2D(p.x, p.y);
    }

    public static Point2D vectorToPoint(Vector2D mousePos) {
        return new Point2D.Double(mousePos.x, mousePos.y);
    }

    public static Dimension vectorToDimension(Vector2D v) {
        return new Dimension((int) v.x, (int) v.y);
    }

    public static double random(double min, double max) {
        double diff = max - min;
        return min + Math.random() * diff;
    }

    public static int randomInt(int min, int max) {
        return (int) random(min, max + 1);
    }

    public static double constrain(double value, double min, double max) {
        if (value < min) {
            value = min;
        } else if (value > max) {
            value = max;
        }
        return value;
    }

    public static double map(double value, int minA, double maxA, double minB, double maxB) {
        double diffA = maxA - minA;
        double diffB = maxB - minB;
        double placementDecimal = (value - minA) / diffA;
        return minB + placementDecimal * diffB;
    }

    public static Color alphaAlteredColor(Color c, int alpha) {
        return new Color(c.getRed(), c.getGreen(), c.getBlue(), alpha);
    }

    public static RadialGradientPaint defaultRadialGradientPaint(float radius, Color colorA, Color colorB) {
        return new RadialGradientPaint(0, 0, radius, DEFAULT_RADIAL_GRADIENT_PAINT_FRACTIONS, new Color[]{colorA, colorB});
    }

    public static Line2D lineFromVectors(Vector2D start, Vector2D stop) {
        return new Line2D.Double(vectorToPoint(start), vectorToPoint(stop));
    }

    public static Ellipse2D circleAtOrigin(double radius) {
        double diameter = 2 * radius;
        return new Ellipse2D.Double(-radius, -radius, diameter, diameter);
    }

    public static Rectangle2D squareAtOrigin(int length) {
        double halfLength = length / 2;
        return new Rectangle2D.Double(-halfLength, -halfLength, length, length);
    }

    public static GeneralPath relativeShapeOfFarmPolygonBody(body.PolygonBody farmPolygonBody) {
        Vector2D[] shapeVertices = farmPolygonBody.copyRelativeVertices();
        
        GeneralPath shape = new GeneralPath();
        shape.moveTo(shapeVertices[0].x, shapeVertices[0].y);
        for (int i = 1; i < shapeVertices.length; i++) {
            shape.lineTo(shapeVertices[i].x, shapeVertices[i].y);
        }
        shape.closePath();
        return shape;
    }

    public static <T> ArrayList<T> transferArrayListContent(ArrayList<T> arrayList) {
        ArrayList<T> transferedArrayList = new ArrayList<>(arrayList);
        arrayList.clear();
        return transferedArrayList;
    }

    public static BufferedImage imageFromPathName(String pathName) {
        File imageFile = new File(pathName);
        try {
            return ImageIO.read(imageFile);
        } catch (IOException ex) {
            Logger.getLogger(Convenience.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

}
