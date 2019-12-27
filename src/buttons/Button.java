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
import main.Sprite;

/**
 *
 * @author Jonatan Larsson
 * @author Mikael Persson
 */
public abstract class Button extends Sprite {

    enum State {
        DEFAULT, HOVERED_OVER, PRESSED
    }
    private State state = State.DEFAULT;

    State getState() {
        return state;
    }

    public final boolean isDefault() {
        return state == State.DEFAULT;
    }

    public final boolean isPressed() {
        return state == State.PRESSED;
    }

    public final boolean isHoveredOver() {
        return state == State.HOVERED_OVER;
    }

    public void processMousePos(Vector2D mousePos) {
        if (containsMouse(mousePos)) {
            tryToBecomeHoveredOver();
        } else {
            tryToBecomeDefault();
        }
    }

    abstract boolean containsMouse(Vector2D mousePos);

    final void tryToBecomeHoveredOver() {
        if (isDefault()) {
            state = State.HOVERED_OVER;
        }
    }

    final void tryToBecomeDefault() {
        if (canBecomeDefault()) {
            state = State.DEFAULT;
        }
    }

    abstract boolean canBecomeDefault();

    public final boolean processMousePress() {
        if (isHoveredOver()) {
            state = State.PRESSED;
            return true;
        }
        return false;
    }

    public final void processMouseRelease() {
        if (isPressed()) {
            performAction();
            state = State.HOVERED_OVER;
        }
    }

    abstract void performAction();

}
