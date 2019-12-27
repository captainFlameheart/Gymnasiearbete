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

import java.awt.HeadlessException;
import javax.swing.JFrame;

/**
 *
 * @author Jonatan Larsson
 * @author Mikael Persson
 */
final class FarmEngineTestbed extends JFrame {

    private static final String TITLE = "Farm Engine Testbed v3";
    
    private final Panel PANEL = new Panel(new Scene(Panel.SIZE), (long) (1E9 / 90));
    
    private FarmEngineTestbed() throws HeadlessException {        
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle(TITLE);
        setResizable(false);
        add(PANEL);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        FarmEngineTestbed application = new FarmEngineTestbed();
        application.PANEL.toggleAnimationActivation();
    }

}
