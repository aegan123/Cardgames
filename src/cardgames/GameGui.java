/*
	Definitions for graphical user interface for games.
    Copyright (C) 2017  Juhani Vähä-Mäkilä, juhani@fmail.co.uk

    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; version 2 of the License.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License along
    with this program; if not, write to the Free Software Foundation, Inc.,
    51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
    */
/**
 * Definitions for graphical user interface for games.
 */
package cardgames;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

/**
 * @author Juhani Vähä-Mäkilä
 * @version 0.1
 */
class GameGui extends JFrame implements ActionListener {

	private static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	private static final long serialVersionUID = 1L;
	static int screenWidth=(int) screenSize.getWidth();
	static int screenHeight=(int) screenSize.getHeight();
	
	private static JFrame frame;
	static JFrame gameFrame;
	private static JButton peli1;
	private static JButton peli2;
	static JButton closeButton=new JButton("Close game");
	
	public GameGui() {
		frame = new JFrame(Cardgames.programName);
		
	}
	void createAndShowGUI() {
		
        //Create and set up the window.
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        frame.getContentPane().setLayout(new FlowLayout());
        peli1=new JButton("Peli1");
        peli2=new JButton("Peli2");
        
        //JLabel emptyLabel = new JLabel("Please select a game.");
        //emptyLabel.setPreferredSize(new Dimension(500,500));
        //frame.getContentPane().add(emptyLabel, BorderLayout.CENTER);
        //JTextField hello=new JTextField("Please select a game.",1);
        //hello.validate();
        peli1.setSize(new Dimension((int) 0.2*screenWidth,(int) 0.2*screenHeight));
        peli1.validate();
        peli1.setActionCommand("1");
        peli2.setActionCommand("2");
        peli1.addActionListener(this);
        peli2.addActionListener(this);
        //frame.getContentPane().add(hello);
        frame.getContentPane().add(peli1);
        frame.getContentPane().add(peli2);
        
 
        //Display the window.
        frame.setSize(screenWidth/2, screenHeight/2);
        frame.setLocation(screenWidth/4, screenHeight/4);
        //frame.pack();
        frame.setVisible(true);
    }

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		switch (Integer.parseInt(e.getActionCommand())) {
		case(1):
			initializeGame("BlackJack");
			new Thread(new BlackJack()).start();
			break;
		case(2):
			JOptionPane.showMessageDialog(null,"You pressed the peli2!");
			break;
		default:
			break;
		}
		
		//if ("peli1".equals(e.getActionCommand())) JOptionPane.showMessageDialog(null,"You pressed the peli1!");
		//else JOptionPane.showMessageDialog(null,"You pressed the peli2!");
		
	}
	/**
	 * @param string
	 */
	private void initializeGame(String name) {
		gameFrame=new JFrame(name);
		gameFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		gameFrame.setSize(screenWidth/2, screenHeight/2);
        gameFrame.setLocation(screenWidth/4, screenHeight/4);
        gameFrame.getContentPane().add(closeButton);
        gameFrame.setVisible(true);
		
	}

}
