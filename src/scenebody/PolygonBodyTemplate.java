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

import body.PolygonBodySeed;
import convenience.Vector2D;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Collections;
import main.Scene;
import main.Sprite;
import other.Convenience;

/**
 *
 * @author Jonatan Larsson
 * @author Mikael Persson
 */
public final class PolygonBodyTemplate extends Sprite {

    private final Scene USED_SCENE;

    private static final int VERTEX_TEMPLATE_LOCK_DIST_SQUARED = 150;

    private static final Ellipse2D VERTEX_SHAPE = Convenience.circleAtOrigin(5), VERTEX_TEMPLATE_SHAPE = Convenience.circleAtOrigin(8);
    private static final Color VERTEX_COLOR = Color.BLACK, LINE_SEGMENT_COLOR = Color.BLACK,
            VERTEX_TEMPLATE_COLOR = Color.GRAY, LINE_SEGMENT_TEMPLATE_COLOR = Color.GRAY;
    private static final BasicStroke LINE_SEGMENT_STROKE = new BasicStroke(3),
            LINE_SEGMENT_TEMPLATE_STROKE = new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 1, new float[]{5, 5}, 0);

    private int vertexCount;
    private final ArrayList<Vertex> VERTICES = new ArrayList<>();
    private final ArrayList<LineSegment> LINE_SEGMENTS = new ArrayList<>();
    private final VertexTemplate VERTEX_TEMPLATE = new VertexTemplate();
    private boolean hasLineSegmentTemplate;
    private LineSegmentTemplate lineSegmentTemplate;

    public PolygonBodyTemplate(Scene usedScene) {
        USED_SCENE = usedScene;
    }

    public void setVertexTemplatePos(Vector2D pos) {
        VERTEX_TEMPLATE.setPosition(pos);
        if (hasLineSegmentTemplate) {
            lineSegmentTemplate.updateShape();
        }
    }

    public boolean develope() {
        if (VERTEX_TEMPLATE.isLockedToFinalDestination) {
            createBody();
            return true;
        } else {
            VERTEX_TEMPLATE.createVertex();
            return false;
        }
    }

    public void undoVertex() {
        VERTICES.remove(vertexCount - 1);
    }

    void createBody() {
        if (!isClockwise()) {
            Collections.reverse(VERTICES);
        }
        if (isConcave()) {
            return;
        }

        PolygonBodySeed bodySeed = new PolygonBodySeed();
        bodySeed.setBodyPosition(bodyCenterOfMass());
        bodySeed.setBodyRelativeVertices(bodyRelativeVertices(bodySeed.bodyPosition));
        bodySeed.setDefaultBodyDensity();

        USED_SCENE.addPolygonBody(bodySeed);
    }

    boolean isClockwise() {
        int clockwiseValue = 0;
        for (int i = 0; i < vertexCount; i++) {
            Vector2D vertexA = VERTICES.get(i).POSITION;
            Vector2D vertexB = VERTICES.get((i + 1) % vertexCount).POSITION;
            clockwiseValue += (vertexB.x - vertexA.x) * (vertexB.y + vertexA.y);
        }
        return clockwiseValue < 0;
    }

    public boolean isConcave() {
        boolean gotPositive = false, gotNegative = false;
        for (int i = 0; i < vertexCount; i++) {
            Vector2D a = VERTICES.get(i).POSITION;
            Vector2D b = VERTICES.get((i + 1) % vertexCount).POSITION;
            Vector2D c = VERTICES.get((i + 2) % vertexCount).POSITION;
            Vector2D ab = Vector2D.drawing(a, b);
            Vector2D bc = Vector2D.drawing(b, c);
            double crossProduct = Vector2D.crossProduct(ab, bc);
            if (crossProduct > 0) {
                gotPositive = true;
            } else if (crossProduct < 0) {
                gotNegative = true;
            }
            if (gotPositive && gotNegative) {
                return true;
            }
        }
        return false;
    }

    private Vector2D bodyCenterOfMass() {
        Vector2D centerOfMass = new Vector2D();
        for (Vertex vertex : VERTICES) {
            centerOfMass.add(vertex.POSITION);
        }
        centerOfMass.div(vertexCount);
        return centerOfMass;
    }

    private Vector2D[] bodyRelativeVertices(Vector2D bodyPos) {
        Object[] vertexArray = VERTICES.toArray();
        Vector2D[] bodyRelativeVertices = new Vector2D[vertexCount];
        for (int i = 0; i < vertexCount; i++) {
            bodyRelativeVertices[i] = ((Vertex) vertexArray[i]).POSITION;
            bodyRelativeVertices[i].sub(bodyPos);
        }
        return bodyRelativeVertices;
    }

    @Override
    protected void paint(Graphics2D g, double secondsSinceUpdate) {
        ArrayList<LineSegment> snapshotOfLineSegments = new ArrayList<>(LINE_SEGMENTS);
        for (LineSegment lineSegment : snapshotOfLineSegments) {
            lineSegment.paintWithoutChangingGraphics(g, secondsSinceUpdate);
        }
        if (hasLineSegmentTemplate) {
            lineSegmentTemplate.paintWithoutChangingGraphics(g, secondsSinceUpdate);
        }
        ArrayList<Vertex> snapshotOfVertices = new ArrayList<>(VERTICES);
        for (Vertex vertex : snapshotOfVertices) {
            vertex.paintWithoutChangingGraphics(g, secondsSinceUpdate);
        }
        VERTEX_TEMPLATE.paintWithoutChangingGraphics(g, secondsSinceUpdate);
    }

    private final class Vertex extends Sprite {

        private final Vector2D POSITION;

        private Vertex(Vector2D position) {
            POSITION = new Vector2D(position);
        }

        @Override
        protected void paint(Graphics2D g, double secondsSinceUpdate) {
            translateGraphicsWithVec(g, POSITION);
            g.setColor(VERTEX_COLOR);
            g.fill(VERTEX_SHAPE);
        }

    }

    private final class LineSegment extends Sprite {

        private final Line2D SHAPE;

        LineSegment(Vector2D start, Vector2D stop) {
            SHAPE = Convenience.lineFromVectors(start, stop);
        }

        @Override
        protected void paint(Graphics2D g, double secondsSinceUpdate) {
            g.setStroke(LINE_SEGMENT_STROKE);
            g.setColor(LINE_SEGMENT_COLOR);
            g.draw(SHAPE);
        }

    }

    private final class VertexTemplate extends Sprite {

        private final Vector2D POSITION = new Vector2D();
        private Vector2D finalDestination;
        private boolean isLockedToFinalDestination;

        private void setPosition(Vector2D desiredPosition) {
            POSITION.set(desiredPosition);
            if (vertexCount >= 3 && POSITION.distSquared(finalDestination) < VERTEX_TEMPLATE_LOCK_DIST_SQUARED) {
                POSITION.set(finalDestination);
                isLockedToFinalDestination = true;
            } else {
                isLockedToFinalDestination = false;
            }
        }

        private void createVertex() {
            Vertex vertex = new Vertex(POSITION);
            VERTICES.add(vertex);
            vertexCount++;
            if (vertexCount == 1) {
                finalDestination = vertex.POSITION;
                lineSegmentTemplate = new LineSegmentTemplate(vertex.POSITION, VERTEX_TEMPLATE.POSITION);
                hasLineSegmentTemplate = true;
            } else {
                LINE_SEGMENTS.add(new LineSegment(VERTICES.get(vertexCount - 2).POSITION, vertex.POSITION));
                lineSegmentTemplate.setStart(vertex.POSITION);
            }
        }

        @Override
        protected void paint(Graphics2D g, double secondsSinceUpdate) {
            translateGraphicsWithVec(g, POSITION);
            g.setColor(VERTEX_TEMPLATE_COLOR);
            g.fill(VERTEX_TEMPLATE_SHAPE);
        }

    }

    private final class LineSegmentTemplate extends Sprite {

        private final Vector2D START = new Vector2D(), STOP;
        private final Line2D SHAPE = new Line2D.Double();

        public LineSegmentTemplate(Vector2D start, Vector2D stop) {
            setStart(start);
            STOP = stop;
        }

        private void setStart(Vector2D start) {
            START.set(start);
        }

        private void updateShape() {
            SHAPE.setLine(Convenience.lineFromVectors(START, STOP));
        }

        @Override
        protected void paint(Graphics2D g, double secondsSinceUpdate) {
            g.setStroke(LINE_SEGMENT_TEMPLATE_STROKE);
            g.setColor(LINE_SEGMENT_TEMPLATE_COLOR);
            g.draw(SHAPE);
        }

    }

}
