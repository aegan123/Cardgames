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
package cardgames;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import javax.imageio.ImageIO;
import javax.swing.*;
import cardgames.Card.Rank;

/**Implementation of Blackjack.
 * Including needed GUI definitions.
 * @author Juhani Vähä-Mäkilä
 * @version 0.1
 */
class Blackjack extends Games implements Runnable, ActionListener {
	/**HashMap for counting the value of the hand.*/
	private static final HashMap<Card.Rank,Integer> MAP=createMap();
	private static int howManyPlayers;
	/**Array of players.*/
	private static Player[] players;
	private boolean running;
	/**All the buttons needed.*/
	private JButton moreCards, stay, doubleBet, closeButton, split;
	/**Panels to divide the screen into three parts.*/
	private JPanel bottomPanel, middlePanel, topPanel;
	private JLabel sumOfPlayer=new JLabel(),sumOfDealer=new JLabel();
	private double bet, winnings=0;
	/**Array to store numerical values of dealt hands. Slot 0 is dealers, all others are for players. */
	private int[] valueOfHands;
	

public Blackjack(){
	initiateFrame();
	initiateButtons();
	GameGui.gameFrame.setVisible(true);

}

/**
 * 
 */
private void initiateFrame() {
	JPanel panel=new JPanel(new BorderLayout());
	//panel.setLayout(new BoxLayout(panel,BoxLayout.X_AXIS));
	GameGui.gameFrame.setContentPane(panel);
	bottomPanel = new JPanel();
	middlePanel=new JPanel();
	topPanel=new JPanel();
	panel.add( bottomPanel, BorderLayout.SOUTH );
	panel.add( middlePanel, BorderLayout.CENTER );
	panel.add( topPanel, BorderLayout.NORTH );
	
}

/**
 * 
 */
private void initiateButtons() {
	moreCards=new JButton("More");
	stay=new JButton("Stay");
	doubleBet=new JButton("Double down");
	closeButton=new JButton("Close game");
	split=new JButton("Split");
	moreCards.addActionListener(this);
	stay.addActionListener(this);
	doubleBet.addActionListener(this);
	closeButton.addActionListener(this);
	split.addActionListener(this);
	//closeButton.setSize(GameGui.buttonSize);
	moreCards.setActionCommand("1");
	stay.setActionCommand("2");
	doubleBet.setActionCommand("3");
	split.setActionCommand("4");
	doubleBet.setEnabled(false);
	split.setEnabled(false);
	closeButton.setActionCommand("0");
	bottomPanel.add(moreCards);
	bottomPanel.add(stay);
	bottomPanel.add(doubleBet);
	bottomPanel.add(split);
	bottomPanel.add(closeButton);
	
	
}
@Override
public void run(){
	//int sum;
	running=true;
	//TODO change back after testing!
	//howManyPlayers=getNumOfPlayers();
	howManyPlayers=1;
	valueOfHands=new int[howManyPlayers+1];
	
	players=getPlayers(howManyPlayers);
	if (howManyPlayers>1) startMultiPlayer();
	else startSinglePlayer();
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
	Cardgames.j1.setDeck(4);
	players[0].setMyTurn(true);
	if (winnings==0) winnings=10.0;
	bet=Double.parseDouble(JOptionPane.showInputDialog("Place your bet.\nYou have "+winnings+" credits."));
	initialDeal(1);

}

/**
 * 
 */
private void startMultiPlayer() {
	Cardgames.j1.setDeck(8);
	players[0].setMyTurn(true);
	for (int i=1;i<players.length;i++) {
		players[i].setMyTurn(false);
	}
		initialDeal(howManyPlayers);
	
}
/**
 * @param i Number of players in game.
 */
private void initialDeal(int i) {
	BufferedImage img = null;
	middlePanel.add(sumOfPlayer);
	topPanel.add(sumOfDealer);
	for (int k=0;k<2;k++) {
		for (int j=0;j<i;j++) {
			players[j].addCard(Cardgames.j1.dealCard());
			//TODO make visible on screen
			try {
				img = ImageIO.read(players[j].getCard(k).getPic());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        //ImageIcon icon=new ImageIcon(img);
	        //JLabel lbl=new JLabel(icon);
	        middlePanel.add(new JLabel(new ImageIcon(img))); 
	        middlePanel.updateUI();
		}
		Cardgames.j1.addCard(Cardgames.j1.dealCard());
		//TODO make visible on screen. second card face down.
		//BufferedImage img = null;
		try {
			if (k==1) img = ImageIO.read(Cardgames.j1.getCard(k).getBackArt());
			else img = ImageIO.read(Cardgames.j1.getCard(k).getPic());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        //ImageIcon icon=new ImageIcon(img);
        //JLabel lbl=new JLabel(icon);
        if (k==0) sumOfDealer.setText(MAP.get(Cardgames.j1.getCard(k).getRank()).toString());
        topPanel.add(new JLabel(new ImageIcon(img)));
        topPanel.updateUI();
	}
	//topPanel.add(new JLabel(Integer.toString((checkHand(Cardgames.j1)))));
	//topPanel.updateUI();
	valueOfHands[0]=checkHand(Cardgames.j1);
	valueOfHands[1]=checkHand(players[0]);
	if (hasAce(players[0])) sumOfPlayer.setText(valueOfHands[1]+"/"+valueOfHands[1]+10);
	else sumOfPlayer.setText(Integer.toString(valueOfHands[1]));
	
	middlePanel.updateUI();
}

/**
 * @param player 
 * @param player 
 * @return
 */
private boolean hasAce(Player player) {
	for (int i=0;i<player.getNumOfcards();i++) {
		if (player.getCard(i).getRank().equals(Card.Rank.A)) return true;
	}
	
	return false;
}
private boolean hasAce(Dealer j1) {
	for (int i=0;i<j1.getNumOfcards();i++) {
		if (j1.getCard(i).getRank().equals(Card.Rank.A)) return true;
	}
	
	return false;
}

/**
 * @param j1
 * @return
 */
private int checkHand(Dealer j1) {
	int sum=0;
	for (int i=0;i<j1.getNumOfcards();i++) {
		sum+=MAP.get(j1.getCard(i).getRank()).intValue();
	}
	return sum;
}

private int checkHand(Player player) {
	int sum=0;
	for (int i=0;i<player.getNumOfcards();i++) {
		sum+=MAP.get(player.getCard(i).getRank()).intValue();
	}
	return sum;
}
/*
private void jdksjd(){
	GameGui.gameFrame.getName();
}*/

@Override
public void actionPerformed(ActionEvent e) {
	switch (Integer.parseInt(e.getActionCommand())) {
	case(0):
		running=false;
		GameGui.gameFrame.dispose();
		break;
	case(1):
		newCardToPlayer();
		break;
	case(2):
		// TODO player doesn't take more cards
		break;
	case(3):
		// TODO doubles the bet + one card
		break;
	case(4):
		//TODO Split the deck
		break;
	default:
		break;
	}
	
}
/**
 * 
 */
private void newCardToPlayer() {
	BufferedImage img = null;
	players[0].addCard(Cardgames.j1.dealCard());
	try {
		img = ImageIO.read(players[0].getCard(players[0].getNumOfcards()-1).getPic());
	} catch (IOException f) {
		// TODO Auto-generated catch block
		f.printStackTrace();
	}
	middlePanel.add(new JLabel(new ImageIcon(img)));
	sumOfPlayer.setText(Integer.toString(checkHand(players[0])));
	middlePanel.updateUI();
	
}

/** Asks the user for number of player in the game.
 * Closes the game if user click cancel in the dialog.
 * @return Number of players.
 */
private int getNumOfPlayers() {
	String in=null;
	int num=-1;
	while (true){
		
		while(true){
			in=JOptionPane.showInputDialog("How many players?");
			if (in==null) {
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
/**
 * @return
 */
private static HashMap<Rank, Integer> createMap() {
	HashMap<Rank,Integer> temp=new HashMap<Rank,Integer>();
	temp.put(Rank.A, 1);
	temp.put(Rank.II, 2);
	temp.put(Rank.III, 3);
	temp.put(Rank.IV, 4);
	temp.put(Rank.V, 5);
	temp.put(Rank.VI, 6);
	temp.put(Rank.VII, 7);
	temp.put(Rank.VIII, 8);
	temp.put(Rank.IX, 9);
	temp.put(Rank.X, 10);
	temp.put(Rank.J, 10);
	temp.put(Rank.Q, 10);
	temp.put(Rank.K, 10);
	return temp;
}
}