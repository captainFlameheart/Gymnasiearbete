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

import convenience.Vector2D;
import java.awt.Graphics2D;

/**
 *
 * @author Jonatan Larsson
 * @author Mikael Persson
 */
public abstract class Sprite {
    
    public final void paintWithoutChangingGraphics(Graphics2D g, double secondsSinceUpdate) {
        paint(copyGraphics2D(g), secondsSinceUpdate);
    }
    
    protected static final Graphics2D copyGraphics2D(Graphics2D g) {
        return (Graphics2D) g.create();
    }
    
    protected abstract void paint(Graphics2D g, double secondsSinceUpdate);
    
    public static final void translateGraphicsWithVec(Graphics2D g, Vector2D translation) {
        g.translate(translation.x, translation.y);
    }
    
    protected static final void scaleGraphicsWithVec(Graphics2D g, Vector2D scale) {
        g.scale(scale.x, scale.y);
    }
    
}
