/*
 * Copyright (C) 2020 Jonatan Larsson
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

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import main.Sprite;

/**
 *
 * @author Jonatan Larsson
 * @author Markus Hyllengren
 */
public final class ConvexMessage extends Sprite {

    private static final String TEXT = "Polygonen måste vara konvex!";

    private final double x;
    private double y;
    private final double speed;
    
    private final Font font;
    
    private int hp;

    public ConvexMessage(double x, double y, double speed, int fontSize, int hp) {
        this.x = x;
        this.y = y;
        this.speed = speed;
        
        font = new Font(Font.DIALOG, Font.BOLD, fontSize);
        
        this.hp = hp;
    }
    
    public void update() {
        y += speed;
        hp--;
    }

    @Override
    protected void paint(Graphics2D g, double secondsSinceUpdate) {
        g.setFont(font);
        FontMetrics metrics = g.getFontMetrics();
        int width = metrics.stringWidth(TEXT);
        int height = metrics.getHeight();
        int alpha = hp;
        if (alpha > 255) {
            alpha = 255;
        } else if (alpha < 0) {
            alpha = 0;
        }
        Color color = new Color(255, 0, 0, alpha);
        g.setColor(color);
        g.drawString(TEXT, (int) x - width / 2, (int) y + height / 2);
    }

    public boolean isDead() {
        return hp < 0;
    }

}
