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
package main;

import java.awt.Graphics2D;
import java.util.ArrayList;
import body.CircularBodySeed;
import body.PolygonBodySeed;
import convenience.Vector2D;
import other.Convenience;
import scenebody.Body;
import scenebody.CircularBody;
import scenebody.PolygonBody;

/**
 *
 * @author Jonatan Larsson
 * @author Mikael Persson
 */
public final class Scene {

    private final Farm PAINTED_FARM = new Farm();

    private final ArrayList<Body> BODIES = new ArrayList<>();
    private final ArrayList<PolygonBodySeed> POLYGON_BODY_SEEDS_TO_ADD = new ArrayList<>();
    private final ArrayList<CircularBodySeed> CIRCULAR_BODY_SEEDS_TO_ADD = new ArrayList<>();
    private final ArrayList<Body> BODIES_TO_REMOVE = new ArrayList<>();
    private final Vector2D BODY_BOUNDARY;
    private boolean bodyWarping;
    private double frictionCoefficient;

    public Scene(Vector2D bodyBoundary) {
        BODY_BOUNDARY = bodyBoundary;

        PolygonBodySeed seedA = new PolygonBodySeed();
        seedA.setBodyRelativeVertices(new Vector2D(-50, -50), new Vector2D(50, -50), new Vector2D(50, 50), new Vector2D(-50, 50));
        seedA.setBodyPosition(300, 200);
        seedA.setBodyVelocity(0, 10);
        seedA.setDefaultBodyDensity();

        PolygonBodySeed seedB = new PolygonBodySeed();
        seedB.setBodyRelativeVertices(new Vector2D(-200, -50), new Vector2D(200, -50), new Vector2D(200, 50), new Vector2D(-200, 50));
        seedB.setBodyPosition(300, 600);
        seedB.setDefaultBodyDensity();

        addPolygonBody(seedA);
        addPolygonBody(seedB);
    }

    public ArrayList<Body> bodies() {
        return new ArrayList<>(BODIES);
    }

    public void addCircularBody(CircularBodySeed bodySeed) {
        CIRCULAR_BODY_SEEDS_TO_ADD.add(bodySeed);
    }

    public void addPolygonBody(PolygonBodySeed bodySeed) {
        POLYGON_BODY_SEEDS_TO_ADD.add(bodySeed);
    }

    public void removeBody(Body body) {
        BODIES_TO_REMOVE.add(body);
    }

    public void clearBodies() {
        removeBodies(BODIES);
    }

    public void updateBodyList() {
        addPolygonBodies();
        addCircularBodies();

        for (Body body : BODIES_TO_REMOVE) {
            PAINTED_FARM.destroyBody(body.farmBody);
            BODIES.remove(body);
        }
        BODIES_TO_REMOVE.clear();
    }

    private void addPolygonBodies() {
        ArrayList<PolygonBodySeed> snapshotOfBodySeedsToAdd = Convenience.transferArrayListContent(POLYGON_BODY_SEEDS_TO_ADD);

        for (PolygonBodySeed bodySeed : snapshotOfBodySeedsToAdd) {
            body.PolygonBody farmBody = PAINTED_FARM.growPolygonBody(bodySeed);
            BODIES.add(new PolygonBody(farmBody));
        }
    }

    private void addCircularBodies() {
        ArrayList<CircularBodySeed> snapshotOfBodySeedsToAdd = Convenience.transferArrayListContent(CIRCULAR_BODY_SEEDS_TO_ADD);

        for (CircularBodySeed bodySeed : snapshotOfBodySeedsToAdd) {
            body.CircularBody farmBody = PAINTED_FARM.growCircularBody(bodySeed);
            BODIES.add(new CircularBody(farmBody));
        }
    }

    public void update(double seconds) {
        for (Body body : BODIES) {
            body.applyResistance(frictionCoefficient);
        }
        PAINTED_FARM.update(seconds);
        processBodyBoundary();
    }

    private void processBodyBoundary() {
        if (bodyWarping) {
            useBoundaryToWarp();
        } else {
            useBoundaryToDestroy();
        }
    }

    private void useBoundaryToWarp() {
        for (Body body : BODIES) {
            body.checkWarping(BODY_BOUNDARY);
        }
    }

    private void useBoundaryToDestroy() {
        ArrayList<Body> bodiesToDestroy = new ArrayList<>();
        for (Body body : BODIES) {
            if (body.isOutsideBoundary(BODY_BOUNDARY)) {
                bodiesToDestroy.add(body);
            }
        }
        removeBodies(bodiesToDestroy);
    }

    private void removeBodies(ArrayList<Body> bodies) {
        for (Body body : bodies) {
            removeBody(body);
        }
    }

    public void switchBodyWarping() {
        bodyWarping = !bodyWarping;
    }

    public boolean bodyWarping() {
        return bodyWarping;
    }

    public void paint(Graphics2D g, double secondsSinceUpdate, Vector2D lightSource) {
        processLight(lightSource);
        paintBodyShadows(g, secondsSinceUpdate);
        paintBodies(g, secondsSinceUpdate);
    }

    private void processLight(Vector2D lightSource) {
        for (Body body : BODIES) {
            body.processLight(lightSource);
        }
    }

    private void paintBodyShadows(Graphics2D g, double secondsSinceUpdate) {
        for (Body body : BODIES) {
            body.shadow.paintWithoutChangingGraphics(g, secondsSinceUpdate);
        }
    }

    private void paintBodies(Graphics2D g, double extrapolation) {
        for (Body body : BODIES) {
            body.paintWithoutChangingGraphics(g, extrapolation);
        }
    }

    public void setFriction(double frictionCoefficient) {
        this.frictionCoefficient = frictionCoefficient;
    }

}
