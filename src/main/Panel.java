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

import body.CircularBodySeed;
import buttons.CircularBodyButton;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.util.ArrayList;
import javax.swing.JPanel;
import convenience.Vector2D;
import buttons.Button;
import buttons.ClearButton;
import buttons.ForceArrowButton;
import other.Convenience;
import other.ForceArrow;
import buttons.FrictionSlider;
import buttons.MenuVisibilityButton;
import buttons.PlayButton;
import buttons.PolygonBodyButton;
import buttons.TimeStepSlider;
import buttons.UpdateButton;
import buttons.WarpButton;
import java.awt.image.BufferedImage;
import scenebody.Body;
import scenebody.CircularBodyTemplate;
import scenebody.PolygonBodyTemplate;

/**
 *
 * @author Jonatan Larsson
 * @author Mikael Persson
 */
public final class Panel extends JPanel {

    static final Vector2D SIZE = new Vector2D(1800, 900);

    final ArrayList<Button> BUTTONS = new ArrayList<>();
    final MenuVisibilityButton MENU_VISIBILTY_BUTTON;
    ForceArrow forceArrow;
    CircularBodyTemplate circularBodyTemplate;
    PolygonBodyTemplate polygonBodyTemplate;

    final Scene SCENE;
    private boolean sceneIsPlaying = true;
    private boolean menuIsVisible = true;

    private final Animation ANIMATION;

    private final MouseProcesser MOUSE_PROCESSER;

    private final RenderingHints RENDERING_HINTS;

    Panel(Scene scene, long animationNanosecondsPerUpdate) {
        setPreferredSize(Convenience.vectorToDimension(SIZE));

        SCENE = scene;

        ANIMATION = new Animation(this, animationNanosecondsPerUpdate);

        MOUSE_PROCESSER = new MouseProcesser(this);
        addMouseListener(MOUSE_PROCESSER);
        addMouseWheelListener(MOUSE_PROCESSER);

        addButton(new PlayButton(new Vector2D(100, 100), this));
        UpdateButton updateButton = new UpdateButton(new Vector2D(100, 200), this);
        addButton(updateButton);
        addButton(new TimeStepSlider(.5, new Vector2D(30, 240), new Vector2D(170, 240), 10, updateButton));
        addButton(new WarpButton(new Vector2D(100, 300), SCENE));
        addButton(new ClearButton(new Vector2D(100, 400), SCENE));
        addButton(new FrictionSlider(0, new Vector2D(SIZE.x - 60, 300), new Vector2D(SIZE.x - 60, 80), 20, SCENE));

        addButton(new ForceArrowButton(new Vector2D(SIZE.x / 2 - 100, SIZE.y - 60), MOUSE_PROCESSER));
        addButton(new CircularBodyButton(new Vector2D(SIZE.x / 2, SIZE.y - 60), MOUSE_PROCESSER));
        addButton(new PolygonBodyButton(new Vector2D(SIZE.x / 2 + 100, SIZE.y - 60), MOUSE_PROCESSER));

        MENU_VISIBILTY_BUTTON = new MenuVisibilityButton(new Vector2D(SIZE.x - 60, SIZE.y - 60), this);
        addButton(MENU_VISIBILTY_BUTTON);

        setBackground(Color.WHITE);
        RENDERING_HINTS = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    }

    private void addButton(Button b) {
        BUTTONS.add(b);
    }

    void toggleAnimationActivation() {
        ANIMATION.toggleActivation();
    }

    public void toggleSceneUpdating() {
        sceneIsPlaying = !sceneIsPlaying;
    }

    public boolean updatesScene() {
        return sceneIsPlaying;
    }

    public void toggleMenuVisibility() {
        menuIsVisible = !menuIsVisible;
    }

    public boolean menuIsVisible() {
        return menuIsVisible;
    }

    public void addBodyToCircularScene(CircularBodySeed bodySeed) {
        SCENE.addCircularBody(bodySeed);
    }

    void destroyBodyFromScene(Body body) {
        SCENE.removeBody(body);
    }

    void update(double seconds) {
        processMousePos();
        possiblyMoveForceArrowTexture(seconds);
        updateSceneBodyList();
        possiblyUpdateScene(seconds);
    }

    private void processMousePos() {
        Point mousePoint = getMousePosition();
        if (mousePoint != null) {
            Vector2D mousePos = Convenience.pointToVector(mousePoint);
            MOUSE_PROCESSER.processMousePos(mousePos);
        }
    }

    private void possiblyMoveForceArrowTexture(double seconds) {
        if (hasAForceArrow()) {
            forceArrow.moveTexture(seconds);
        }
    }

    private void updateSceneBodyList() {
        SCENE.updateBodyList();
    }

    private void possiblyUpdateScene(double seconds) {
        if (sceneIsPlaying) {
            updateScene(seconds);
        }
    }

    public void updateScene(double seconds) {
        SCENE.update(seconds);
    }

    void paint(double secondsSinceUpdate) {
        BufferedImage offscreenImage = new BufferedImage((int) SIZE.x, (int) SIZE.y, BufferedImage.TYPE_INT_ARGB);
        Graphics g = offscreenImage.createGraphics();

        Graphics2D modifiedG = modifyGraphics(g);
        super.paintComponent(modifiedG);

        paintScene(modifiedG, (sceneIsPlaying) ? secondsSinceUpdate : 0);
        paintMouseProcessing(modifiedG, secondsSinceUpdate);
        paintButtons(modifiedG, secondsSinceUpdate);

        getGraphics().drawImage(offscreenImage, 0, 0, null);
    }

    private Graphics2D modifyGraphics(Graphics g) {
        Graphics2D modifiedG = (Graphics2D) g;
        modifiedG.addRenderingHints(RENDERING_HINTS);
        return modifiedG;
    }

    private void paintScene(Graphics2D g, double secondsSinceUpdate) {
        Vector2D lightSource = new Vector2D();
        SCENE.paint(g, secondsSinceUpdate, lightSource);
    }

    private void paintMouseProcessing(Graphics2D g, double secondsSinceUpdate) {
        switch (MOUSE_PROCESSER.getMode()) {
            case APPLYING_FORCE:
                tryToPaintForceArrow(g, secondsSinceUpdate);
                break;
            case ADDING_CIRCULAR_BODY:
                circularBodyTemplate.paintWithoutChangingGraphics(g, secondsSinceUpdate);
                break;
            case ADDING_POLYGON_BODY:
                polygonBodyTemplate.paintWithoutChangingGraphics(g, secondsSinceUpdate);
                break;
            default:
                throw new AssertionError();
        }
    }

    private void tryToPaintForceArrow(Graphics2D g, double secondsSinceUpdate) {
        if (hasAForceArrow()) {
            forceArrow.paintWithoutChangingGraphics(g, secondsSinceUpdate);
        }
    }

    boolean hasAForceArrow() {
        return (forceArrow != null);
    }

    private void paintButtons(Graphics2D g, double extrapolation) {
        if (menuIsVisible) {
            for (Button button : BUTTONS) {
                button.paintWithoutChangingGraphics(g, extrapolation);
            }
        } else {
            MENU_VISIBILTY_BUTTON.paint(g, extrapolation);
        }
    }

}
