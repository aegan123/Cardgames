/*
	Implementation of BlackJack.
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
 * 
 */
package cardgames;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;

/**
 * @author Juhani Vähä-Mäkilä
 * @version 0.1
 */
class BlackJack extends Games implements Runnable, ActionListener {
	
private static int howManyPlayers;
private static Player[] players;
private boolean running;
private JButton moreCards, stay, doubleBet, closeButton;

public BlackJack(){
	initiateButtons();

}
/**
 * 
 */
private void initiateButtons() {
	moreCards=new JButton("More");
	stay=new JButton("Stay");
	doubleBet=new JButton("Double down");
	moreCards.addActionListener(this);
	stay.addActionListener(this);
	doubleBet.addActionListener(this);
	closeButton.addActionListener(this);
	closeButton.setSize(GameGui.buttonSize);
	moreCards.setActionCommand("1");
	stay.setActionCommand("2");
	doubleBet.setActionCommand("3");
	doubleBet.setEnabled(false);
	closeButton.setActionCommand("0");
	GameGui.gameFrame.getContentPane().add(moreCards);
	GameGui.gameFrame.getContentPane().add(stay);
	GameGui.gameFrame.getContentPane().add(doubleBet);
	GameGui.gameFrame.getContentPane().add(closeButton);
	
}
@Override
public void run(){
	running=true;
	howManyPlayers=getNumOfPlayers();
	
	players=getPlayers(howManyPlayers);
	if (players.length>1) startMultiPlayer();
	else startSinglePlayer();
		//JOptionPane.showMessageDialog(GameGui.gameFrame, players.length);
		while (running){
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
}
}

/**
 * 
 */
private void startSinglePlayer() {
	// TODO Auto-generated method stub

}

/**
 * 
 */
private void startMultiPlayer() {
	// TODO Auto-generated method stub
	
}
private void jdksjd(){
	GameGui.gameFrame.getName();
}

@Override
public void actionPerformed(ActionEvent e) {
	switch (Integer.parseInt(e.getActionCommand())) {
	case(0):
		running=false;
		GameGui.gameFrame.dispose();
		break;
	case(1):
		// TODO add card to player
		break;
	case(2):
		// TODO player doesn't take more cards
		break;
	case(3):
		// TODO doubles the bet + one card
		break;
	default:
		break;
	}
	
}
/**
 * @return
 */
private int getNumOfPlayers() {
	String in=null;
	int num=-1;
	while (true){
		
		while(true){
			in=JOptionPane.showInputDialog("How many players?");
			if (in==null) {
			//JOptionPane.showConfirmDialog(GameGui.gameFrame, "Are you sure you want to cancel?");
			int joku=JOptionPane.showConfirmDialog(GameGui.gameFrame, "Are you sure you want to cancel?", "Cancel?", JOptionPane.YES_NO_OPTION);
			if (joku==0) {
			GameGui.gameFrame.dispose();
			try {
				Thread.currentThread().join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}} else continue;
			
		}
			break;
		}
		try {
			num=Integer.parseInt(in);
		}
		catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(GameGui.gameFrame, "Error! Not a number.\nPlease enter a number.");
			continue;
		}
		if (num<1) {
			JOptionPane.showMessageDialog(GameGui.gameFrame, "Error!\nThere has to be at least ONE player.");
			continue;
		}
		break;
	}
	return num;
}

}
