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
import java.awt.Color;

/**
 *
 * @author Jonatan Larsson
 * @author Mikael Persson
 */
public final class TimeStepSlider extends Slider {

    private final UpdateButton USED_UPDATE_BUTTON;
    
    private static final Color DEFAULT_COLOR, HOVERED_OVER_COLOR, PRESSED_COLOR;
    
    static {
        DEFAULT_COLOR = Color.GREEN;
        HOVERED_OVER_COLOR = Color.MAGENTA;
        PRESSED_COLOR = Color.RED;
    }
    
    public TimeStepSlider(double value, Vector2D start, Vector2D stop, double blobRadius, UpdateButton usedUpdateButton) {
        super(0, 2, value, start, stop, blobRadius, DEFAULT_COLOR, HOVERED_OVER_COLOR, PRESSED_COLOR);
        USED_UPDATE_BUTTON = usedUpdateButton;
        performAction();
    }

    @Override
    void performAction() {
        USED_UPDATE_BUTTON.setSecondsPerUpdate(getValue());
    }

}
