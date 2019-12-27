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
package other;

import convenience.Vector2D;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import main.Sprite;

/**
 *
 * @author Jonatan Larsson
 * @author Mikael Persson
 */
abstract class Particle extends Sprite {

    private final Vector2D POSITION, VELOCITY;

    final int INITIAL_HP;
    private double hp;

    private final Shape SHAPE;
    final Vector2D SCALING = new Vector2D();
    Paint paint;

    Particle(Vector2D position, Vector2D velocity, int initialHP, Shape shape) {
        POSITION = new Vector2D(position);
        VELOCITY = new Vector2D(velocity);

        INITIAL_HP = initialHP;
        hp = INITIAL_HP;
        SHAPE = shape;
    }

    public boolean update(double seconds) {
        move(seconds);
        return age(seconds);
    }

    private void move(double seconds) {
        POSITION.add(Vector2D.multiplication(VELOCITY, seconds));
    }

    private boolean age(double seconds) {
        hp -= seconds;
        processHp(hp);
        return (hp < 0);
    }
    
    abstract void processHp(double hp);
    
    @Override
    protected void paint(Graphics2D g, double secondsSinceUpdate) {
        translateGraphicsWithVec(g, POSITION);
        scaleGraphicsWithVec(g, SCALING);
        g.setPaint(paint);
        g.fill(SHAPE);
    }

}
