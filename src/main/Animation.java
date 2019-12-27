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

/**
 *
 * @author Jonatan Larsson
 * @author Mikael Persson
 */
final class Animation implements Runnable {

    private volatile boolean isActive, isRunning;

    private final Panel USED_PANEL;

    private final long NANOSECONDS_PER_UPDATE;
    private final double SECONDS_PER_UPDATE;

    Animation(Panel affectedPanel, long nanosecondsPerUpdate) {
        USED_PANEL = affectedPanel;
        NANOSECONDS_PER_UPDATE = nanosecondsPerUpdate;
        SECONDS_PER_UPDATE = (1 / 1E9) * NANOSECONDS_PER_UPDATE;
    }

    void toggleActivation() {
        if (isActive) {
            stop();
        } else {
            start();
        }
    }

    private void stop() {
        isActive = false;
    }

    private void start() {
        isActive = true;
        if (!isRunning) {
            Thread thread = new Thread(this);
            thread.start();
        }
    }

    @Override
    public void run() {
        isRunning = true;
        
        long lastTime = System.nanoTime();
        long lag = 0;
        
        while (isActive) {
            long currentTime = System.nanoTime();
            long elapsedTime = currentTime - lastTime;
            lag += elapsedTime;
            
            while (lag >= NANOSECONDS_PER_UPDATE) {
                USED_PANEL.update(SECONDS_PER_UPDATE);
                lag -= NANOSECONDS_PER_UPDATE;
            }

            double secondsSinceUpdate = (double) lag / NANOSECONDS_PER_UPDATE;
            secondsSinceUpdate *= SECONDS_PER_UPDATE;
            USED_PANEL.paint(secondsSinceUpdate);

            lastTime = currentTime;
        }
        
        isRunning = false;
    }

}
