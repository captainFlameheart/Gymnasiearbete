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
import convenience.Vector2D;
import java.awt.image.BufferedImage;
import main.Panel;
import other.Convenience;

/**
 *
 * @author Jonatan Larsson
 * @author Mikael Persson
 */
public final class PlayButton extends RectangularButton {

    private final Panel USED_PANEL;

    private static final Vector2D DEFAULT_HALF_SIZE, HOVERED_OVER_HALF_SIZE, PRESSED_HALF_SIZE;
    private static final Color DEFAULT_COLOR, HOVERED_OVER_COLOR, PRESSED_COLOR;
    private static final BufferedImage PAUSED_IMAGE, PLAYED_IMAGE;

    static {
        DEFAULT_HALF_SIZE = new Vector2D(70, 20);
        HOVERED_OVER_HALF_SIZE = new Vector2D(80, 20);
        PRESSED_HALF_SIZE = new Vector2D(75, 20);
        
        DEFAULT_COLOR = Color.CYAN;
        HOVERED_OVER_COLOR = DEFAULT_COLOR.darker();
        PRESSED_COLOR = HOVERED_OVER_COLOR.darker();
        
        PAUSED_IMAGE = Convenience.imageFromPathName("images/play.PNG");
        PLAYED_IMAGE = Convenience.imageFromPathName("images/pause.PNG");
    }

    public PlayButton(Vector2D position, Panel usedPanel) {
        super(position, DEFAULT_HALF_SIZE, HOVERED_OVER_HALF_SIZE, PRESSED_HALF_SIZE, DEFAULT_COLOR, HOVERED_OVER_COLOR, PRESSED_COLOR);
        USED_PANEL = usedPanel;
        setImage();
    }

    @Override
    void performAction() {
        USED_PANEL.toggleSceneUpdating();
        setImage();
    }

    private void setImage() {
        setImage((USED_PANEL.updatesScene()) ? PLAYED_IMAGE : PAUSED_IMAGE);
    }

}
