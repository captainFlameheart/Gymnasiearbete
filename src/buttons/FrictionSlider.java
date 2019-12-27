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
import main.Scene;
import java.awt.Color;

/**
 *
 * @author Jonatan Larsson
 * @author Mikael Persson
 */
public final class FrictionSlider extends Slider {

    private final Scene USED_SCENE;
    
    private static final Color DEFAULT_COLOR, HOVERED_OVER_COLOR, PRESSED_COLOR;
    
    static {
        DEFAULT_COLOR = Color.BLUE;
        HOVERED_OVER_COLOR = Color.CYAN;
        PRESSED_COLOR = Color.RED;
    }
    
    public FrictionSlider(double value, Vector2D start, Vector2D stop, double blobRadius, Scene usedScene) {
        super(0, 1000, value, start, stop, blobRadius, DEFAULT_COLOR, HOVERED_OVER_COLOR, PRESSED_COLOR);
        USED_SCENE = usedScene;
    }

    @Override
    void performAction() {
        USED_SCENE.setFriction(getValue());
    }

}
