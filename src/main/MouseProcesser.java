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

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import convenience.Vector2D;
import buttons.Button;
import java.awt.Cursor;
import java.awt.event.MouseWheelEvent;
import java.util.EnumMap;
import other.Convenience;
import other.ForceArrow;
import scenebody.Body;
import scenebody.CircularBodyTemplate;
import scenebody.PolygonBodyTemplate;

/**
 *
 * @author Jonatan Larsson
 * @author Mikael Persson
 */
public final class MouseProcesser extends MouseAdapter {

    private final Panel USED_PANEL;

    Mode getMode() {
        return mode;
    }

    public enum Mode {
        APPLYING_FORCE, ADDING_CIRCULAR_BODY, ADDING_POLYGON_BODY
    }
    private Mode mode;
    private final EnumMap<Mode, Cursor> MODE_CURSOR_MAP = new EnumMap<>(Mode.class);
    private double upcomingCircularBodyTemplateRadius = 30;

    private final Cursor HOVERED_OVER_BUTTON_CURSOR = new Cursor(Cursor.HAND_CURSOR);

    MouseProcesser(Panel usedScenePanel) {
        USED_PANEL = usedScenePanel;

        MODE_CURSOR_MAP.put(Mode.APPLYING_FORCE, new Cursor(Cursor.CROSSHAIR_CURSOR));
        MODE_CURSOR_MAP.put(Mode.ADDING_CIRCULAR_BODY, new Cursor(Cursor.MOVE_CURSOR));
        MODE_CURSOR_MAP.put(Mode.ADDING_POLYGON_BODY, new Cursor(Cursor.HAND_CURSOR));
        setMode(Mode.APPLYING_FORCE);
    }

    public void setMode(Mode mode) {
        switch (mode) {
            case APPLYING_FORCE:
                if (this.mode == Mode.ADDING_CIRCULAR_BODY) {
                    upcomingCircularBodyTemplateRadius = USED_PANEL.circularBodyTemplate.getRadius();
                    USED_PANEL.circularBodyTemplate = null;
                }
                break;
            case ADDING_CIRCULAR_BODY:
                setPolygonTemplates(new CircularBodyTemplate(USED_PANEL.SCENE, upcomingCircularBodyTemplateRadius), null);
                break;
            case ADDING_POLYGON_BODY:
                setPolygonTemplates(null, new PolygonBodyTemplate(USED_PANEL.SCENE));
                break;
            default:
                throw new AssertionError();
        }
        this.mode = mode;
        setCursor(modeCursor());
    }

    private void setPolygonTemplates(CircularBodyTemplate circularBodyTemplate, PolygonBodyTemplate polygonBodyTemplate) {
        USED_PANEL.circularBodyTemplate = circularBodyTemplate;
        USED_PANEL.polygonBodyTemplate = polygonBodyTemplate;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        Vector2D mousePos = Convenience.pointToVector(e.getPoint());

        if (e.getButton() == MouseEvent.BUTTON1) {
            if (tryToPressButton()) {
                return;
            }
            switch (mode) {
                case APPLYING_FORCE:
                    tryToCreateForceArrow(mousePos);
                    break;
                case ADDING_CIRCULAR_BODY:
                    USED_PANEL.circularBodyTemplate.createBody();
                    break;
                case ADDING_POLYGON_BODY:
                    tryToFinalizePolygonBody();
                    break;
                default:
                    throw new AssertionError();
            }
        } else if (e.getButton() == MouseEvent.BUTTON3) {
            tryToDestroyBody(mousePos);
        }

    }

    private boolean tryToPressButton() {
        if (USED_PANEL.menuIsVisible()) {
            for (Button button : USED_PANEL.BUTTONS) {
                if (button.processMousePress()) {
                    return true;
                }
            }
            return false;
        } else {
            return USED_PANEL.MENU_VISIBILTY_BUTTON.processMousePress();
        }
    }

    private void tryToCreateForceArrow(Vector2D mousePos) {
        Body hoveredOverBody = hoveredOverBody(mousePos);
        if (hoveredOverBody != null) {
            hoveredOverBody.stop();
            USED_PANEL.forceArrow = new ForceArrow(hoveredOverBody);
        }
    }

    private void tryToFinalizePolygonBody() {
        if (USED_PANEL.polygonBodyTemplate.develope()) {
            if (USED_PANEL.polygonBodyTemplate.isConcave()) {
                USED_PANEL.messageAboutConvexity();
            }
            USED_PANEL.polygonBodyTemplate = new PolygonBodyTemplate(USED_PANEL.SCENE);
        }
    }

    private boolean tryToDestroyBody(Vector2D mousePos) {
        Body hoveredOverBody = hoveredOverBody(mousePos);
        if (hoveredOverBody != null) {
            USED_PANEL.destroyBodyFromScene(hoveredOverBody);
            return true;
        }
        return false;
    }

    private Body hoveredOverBody(Vector2D mousePos) {
        for (Body body : USED_PANEL.SCENE.bodies()) {
            if (body.containsMouse(mousePos)) {
                return body;
            }
        }
        return null;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (tryToReleaseForceArrow()) {
            return;
        }
        tryToReleaseButton();
    }

    private boolean tryToReleaseForceArrow() {
        if (USED_PANEL.hasAForceArrow()) {
            USED_PANEL.forceArrow.applyForce();
            USED_PANEL.forceArrow = null;
            return true;
        }
        return false;
    }

    private void tryToReleaseButton() {
        if (USED_PANEL.menuIsVisible()) {
            for (Button button : USED_PANEL.BUTTONS) {
                button.processMouseRelease();
            }
        } else {
            USED_PANEL.MENU_VISIBILTY_BUTTON.processMouseRelease();
        }
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        if (mode == Mode.ADDING_CIRCULAR_BODY) {
            USED_PANEL.circularBodyTemplate.increaseRadius(2 * e.getPreciseWheelRotation());
        }
    }

    void processMousePos(Vector2D mousePos) {
        switch (mode) {
            case APPLYING_FORCE:
                if (tryToSetForceArrowTipPos(mousePos)) {
                    return;
                }
                break;
            case ADDING_CIRCULAR_BODY:
                USED_PANEL.circularBodyTemplate.setPosition(mousePos);
                break;
            case ADDING_POLYGON_BODY:
                USED_PANEL.polygonBodyTemplate.setVertexTemplatePos(mousePos);
                break;
            default:
                throw new AssertionError();
        }

        boolean buttonHighlighted = tryToHighlightButton(mousePos);
        setCursor((buttonHighlighted) ? HOVERED_OVER_BUTTON_CURSOR : modeCursor());
    }

    private boolean tryToSetForceArrowTipPos(Vector2D mousePos) {
        if (USED_PANEL.hasAForceArrow()) {
            USED_PANEL.forceArrow.setTipPosition(mousePos);
            return true;
        }
        return false;
    }

    private boolean tryToHighlightButton(Vector2D mousePos) {
        if (USED_PANEL.menuIsVisible()) {
            for (Button button : USED_PANEL.BUTTONS) {
                button.processMousePos(mousePos);
                if (!button.isDefault()) {
                    return true;
                }
            }
            return false;
        } else {
            USED_PANEL.MENU_VISIBILTY_BUTTON.processMousePos(mousePos);
            return !USED_PANEL.MENU_VISIBILTY_BUTTON.isDefault();
        }
    }

    private void setCursor(Cursor c) {
        USED_PANEL.setCursor(c);
    }

    private Cursor modeCursor() {
        return MODE_CURSOR_MAP.get(mode);
    }

}
