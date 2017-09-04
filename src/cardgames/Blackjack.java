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
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import javax.imageio.ImageIO;
import javax.swing.*;
import cardgames.Card.Rank;

/**Implementation of Blackjack.
 * Including needed GUI definitions.
 * @author Juhani Vähä-Mäkilä
 * @version 0.5
 */
/* Rules:
 * - Blackjack pays 3:2 except if dealer has Blackjack, see even hand.
 * - Player winning pays 2:1.
 * - Even hands pays 1:1.
 * - 10 card charlie: Player wins if player has 10 cards in hand and value is <21, except if dealer has a Blackjack.
 * - Hand can be insured against dealers Blackjack. Insurance is half of bet.
 * - Split allowed for same rank cards. If splitting aces get only one card. //TODO
 * - Doubling of bet is allowed for initial deal. Player gets one extra card.
 * - Dealer takes new cards until the value of hand is at least 17.
 */
class Blackjack extends Games implements Runnable, ActionListener, WindowListener  {
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
	/**Different labels needed all over the GUI*/
	private JLabel sumOfPlayer=new JLabel(),sumOfDealer=new JLabel(),valueOfBet=new JLabel(),numOfCredits=new JLabel();
	private double bet=0, credits=0,insurance;
	/**Array to store numerical values of dealt hands. Slot 0 is dealers, all others are for players. */
	private int[] valueOfHands;
	private boolean playerInsured=false;
	//private static final String rules=""; //TODO
	

public Blackjack(){
	initiateFrame();
	initiateLabels();
	initiateButtons();
	GameGui.gameFrame.addWindowListener(this);
	GameGui.gameFrame.setVisible(true);

}

//************************************
//Initiate the GUI and start the game*
//************************************
/**
 * Initiates the gameframe.
 */
private void initiateFrame() {
	JPanel panel=new JPanel(new BorderLayout());
	GameGui.gameFrame.setContentPane(panel);
	bottomPanel = new JPanel();
	middlePanel=new JPanel();
	topPanel=new JPanel();
	panel.add( bottomPanel, BorderLayout.SOUTH );
	panel.add( middlePanel, BorderLayout.CENTER );
	panel.add( topPanel, BorderLayout.NORTH );
	
}

/**
 * Initiates all the buttons used.
 */
private void initiateButtons() {
	moreCards=new JButton("More");
	stay=new JButton("Stay");
	doubleBet=new JButton("Double down");
	//closeButton=new JButton("Close game");
	split=new JButton("Split");
	moreCards.addActionListener(this);
	stay.addActionListener(this);
	doubleBet.addActionListener(this);
	//closeButton.addActionListener(this);
	split.addActionListener(this);
	//closeButton.setSize(GameGui.buttonSize);
	moreCards.setActionCommand("1");
	stay.setActionCommand("2");
	doubleBet.setActionCommand("3");
	split.setActionCommand("4");
	doubleBet.setVisible(false);
	split.setVisible(false);
	moreCards.setVisible(false);
	stay.setVisible(false);
	//closeButton.setActionCommand("0");
	//closeButton.setLocation(0, 0);
	bottomPanel.add(moreCards);
	bottomPanel.add(stay);
	bottomPanel.add(doubleBet);
	bottomPanel.add(split);
	//bottomPanel.add(closeButton);	
}
/**
 * Initiates labels used.
 */
private void initiateLabels() {
	valueOfBet.setVisible(false);
	bottomPanel.add(new JLabel("Current credits:"));
	bottomPanel.add(numOfCredits);
	bottomPanel.add(new JLabel("Current bet:"));
	bottomPanel.add(valueOfBet);
	
}
@Override
public void run(){
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
	if(Cardgames.j1.getSize()<=15) {
	Cardgames.j1.setDeck(4);}
	players[0].setMyTurn(true);
	if (credits<=0) {
			credits=10.0;
			numOfCredits.setText(Double.toString(credits));
			numOfCredits.setVisible(true);
	}
	bet=getBet();
	credits-=bet;
	valueOfBet.setText(Double.toString(bet));
	valueOfBet.setVisible(true);
	numOfCredits.setText(Double.toString(credits));
	initialDeal(1);
	if (isBlackjack(players[0])) {
		moreCards.setVisible(false);
		stay.setVisible(false);
		doubleBet.setVisible(false);
		sumOfPlayer.setText("Blackjack!");
		middlePanel.updateUI();
		valueOfHands[1]=21;
		dealersTurn();
		JOptionPane.showMessageDialog(GameGui.gameFrame, checkWinnings(players[0]));
		restartGame();
	} 
	else {
		if (canInsure()) {
			//JOptionPane.showConfirmDialog(GameGui.gameFrame, "Dealer might have a Blackjac.\nDo you want to insure your hand against it?\nInsurance is half of your current bet.", "Insure?", JOptionPane.YES_NO_OPTION);
			if (JOptionPane.YES_OPTION==JOptionPane.showConfirmDialog(GameGui.gameFrame, "Dealer might have a Blackjack.\nDo you want to insure your hand against it?\nInsurance is half of your current bet.", "Insure?", JOptionPane.YES_NO_OPTION)) {
				playerInsured=true;
				insurance=bet/2; }
		}
		moreCards.setVisible(true);
		stay.setVisible(true);
		if (canSplit()) {split.setVisible(true);}
		doubleBet.setVisible(true);
		middlePanel.updateUI();
	}

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
//**********************
//Hand checking methods*
//**********************
/**Checks if player has a Blackjack.
 * @return True if has. Otherwise false.
 */
private boolean isBlackjack(Player player) {
	return checkHand(player)==11 && (player.getCard(0).getRank().equals(Card.Rank.A) || player.getCard(1).getRank().equals(Card.Rank.A));
}
/**Checks if dealer has a Blackjack.
 * @return True if has. Otherwise false.
*/
private boolean isBlackjack(Dealer j1) {
	return checkHand(j1)==11 && (j1.getCard(0).getRank().equals(Card.Rank.A) || j1.getCard(1).getRank().equals(Card.Rank.A));
}
/**Checks if player can double. Currently not in use.
 * @return
 */
private boolean canDouble() {
	// TODO Auto-generated method stub
	return false;
}

/**Checks if player can split the hand.
 * @return True if hand can be split. False if not.
 */
private boolean canSplit() {
	return players[0].getCard(0).getRank().equals(players[0].getCard(1).getRank());
}
/**Checks if player can insure against dealers Blackjack.
 * @return True/false.
 */
private boolean canInsure() {
	return Cardgames.j1.getCard(0).getRank().equals(Card.Rank.A);
}
/**
*Checks if player has an ace.
* @param player The player whose hand we check.
* @return True/False.
*/
private boolean hasAce(Player player) {
	return player.getCard(0).getRank().equals(Card.Rank.A) || player.getCard(1).getRank().equals(Card.Rank.A);
}
/**
 * Checks if dealer has an ace.
 * @param j1 Dealer whose hand we check.
 * @return True/False.
 */
private boolean hasAce(Dealer j1) {
	return j1.getCard(0).getRank().equals(Card.Rank.A);
}
/**Checks if players hand is a 10 card charlie (see rules).
 * @return True/False.
 */
private boolean isCharlie() {
	return players[0].getNumOfCards()==10 && valueOfHands[1]<21;
}

/**Counts the value of hand.
* @param j1 The Dealer.
* @return Total value of hand.
*/
private int checkHand(Dealer j1) {
	int sum=0;
	for (int i=0;i<j1.getNumOfCards();i++) {
		sum+=MAP.get(j1.getCard(i).getRank()).intValue();
	}
	return sum;
}
/**
 * Count the value of hand.
 * @param player The player to check.
 * @return Total value of hand.
 */
private int checkHand(Player player) {
	int sum=0;
	for (int i=0;i<player.getNumOfCards();i++) {
		sum+=MAP.get(player.getCard(i).getRank()).intValue();
	}
	return sum;
}
/**
 * Checks if player has won. Adds winnings to credits.
 */
private String checkWinnings(Player player) {
	if (valueOfHands[player.getPlayerNum()]>21) {
		return "You lost.";
	}
	if (isCharlie() && !isBlackjack(Cardgames.j1)) {
		credits+=2*bet;
		return "You won "+Double.toString(2*bet)+" credits.";
	}
	if (isBlackjack(player) && isBlackjack(Cardgames.j1)) {
		credits+=bet;
		return "You tied with the dealer.\nYou won "+Double.toString(bet)+" credits.";
	}
	if (isBlackjack(player)) {
		credits+=2.5*bet;
		return "You got a BlackJack!\nYou won "+Double.toString(2.5*bet)+" credits.";
	}
	if (isBlackjack(Cardgames.j1) && !playerInsured) {
		return "You lost.";
	}
	if (isBlackjack(Cardgames.j1) && playerInsured) {
		credits+=2*insurance;
		return "Dealer got a Blackjack.\nYour insurance pays you "+Double.toString(2*insurance)+" credits.";
	}
	if (valueOfHands[player.getPlayerNum()]>valueOfHands[0]) {
		credits+=2*bet;
		return "You won "+Double.toString(2*bet)+" credits.";
	}
	if (valueOfHands[0]>valueOfHands[player.getPlayerNum()] && valueOfHands[0]<=21) {
		return "You lost.";
	}
	if (valueOfHands[0]==valueOfHands[player.getPlayerNum()]) {
		credits+=bet;
		return "You tied with the dealer.\nYou won "+Double.toString(bet)+" credits.";
	}
	if (valueOfHands[0]>21) {
		credits+=2*bet;
		return "You won "+Double.toString(2*bet)+" credits.";
	}
	
	return "Not supposed to happen!";
	
}
//*************************
//Gameplay related methods*
//*************************

/**Makes the initial deal of the game. Two cards for each player (incl Dealer).
 * @param i Number of players in game.
 */
private void initialDeal(int i) {
	BufferedImage img = null;
	middlePanel.add(sumOfPlayer);
	topPanel.add(sumOfDealer);
	for (int k=0;k<2;k++) {
		for (int j=0;j<i;j++) {
			players[j].addCard(Cardgames.j1.dealCard());
			try {
				img = ImageIO.read(players[j].getCard(k).getPic());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        middlePanel.add(new JLabel(new ImageIcon(img))); 
	        middlePanel.updateUI();
		}
		Cardgames.j1.addCard(Cardgames.j1.dealCard());
		//second card face down.
		try {
			if (k==1) img = ImageIO.read(Cardgames.j1.getCard(k).getBackArt());
			else img = ImageIO.read(Cardgames.j1.getCard(k).getPic());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        if (k==0) {
        	if (hasAce(Cardgames.j1)) {
        		sumOfDealer.setText("1/11");
        	}
        	else
        		sumOfDealer.setText(MAP.get(Cardgames.j1.getCard(k).getRank()).toString());
        
        }
        topPanel.add(new JLabel(new ImageIcon(img)));
        topPanel.updateUI();
	}
	valueOfHands[0]=checkHand(Cardgames.j1);
	valueOfHands[1]=checkHand(players[0]);
	//****
    //TODO remove after testing
    sumOfDealer.setText(Integer.toString(valueOfHands[0]));
    topPanel.updateUI();
    //****
	if (hasAce(players[0])) sumOfPlayer.setText(valueOfHands[1]+"/"+Integer.toString(valueOfHands[1]+10));
	else sumOfPlayer.setText(Integer.toString(valueOfHands[1]));
	
	middlePanel.updateUI();
}

/**
 * Adds a new Card to players hand. Also updates GUI.
 */
private void newCardToPlayer() {
	BufferedImage img = null;
	players[0].addCard(Cardgames.j1.dealCard());
	try {
		img = ImageIO.read(players[0].getCard(players[0].getNumOfCards()-1).getPic());
	} catch (IOException f) {
		// TODO Auto-generated catch block
		f.printStackTrace();
	}
	middlePanel.add(new JLabel(new ImageIcon(img)));
	valueOfHands[1]=checkHand(players[0]);
	sumOfPlayer.setText(Integer.toString(valueOfHands[1]));
	middlePanel.updateUI();
	
}
/**
 * Does the dealers turn after player(s) have finished. Also updates GUI.
 */
private void dealersTurn() {
	topPanel.remove(2);
	try {
		topPanel.add(new JLabel(new ImageIcon(ImageIO.read(Cardgames.j1.getCard(1).getPic()))));
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	sumOfDealer.setText(Integer.toString(valueOfHands[0]));
	topPanel.updateUI();
	if (isBlackjack(Cardgames.j1)) { valueOfHands[0]=21; sumOfDealer.setText("Blackjack!");	topPanel.updateUI();}
	else {
	BufferedImage img=null;
	while(valueOfHands[0]<17) {
		Cardgames.j1.addCard(Cardgames.j1.dealCard());
		try {
			img = ImageIO.read(Cardgames.j1.getCard(Cardgames.j1.getNumOfCards()-1).getPic());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		valueOfHands[0]=checkHand(Cardgames.j1);
		sumOfDealer.setText(Integer.toString(valueOfHands[0]));
		topPanel.add(new JLabel(new ImageIcon(img)));
	    topPanel.updateUI();
        }
	}
	}

/*
private void jdksjd(){
	GameGui.gameFrame.getName();
}*/



//**************
//Other methods*
//**************
/**
 * 
 */
@Override
public void actionPerformed(ActionEvent e) {
	//Do something based on button pressed.
	switch (Integer.parseInt(e.getActionCommand())) {
	case(0):
		//Close game.
		closeGame();
		break;
	case(1):
		//Player takes more cards.
		doubleBet.setVisible(false);
		newCardToPlayer();
		if (isCharlie()) {
			moreCards.setVisible(false);
			stay.setVisible(false);
			bottomPanel.updateUI();
			JOptionPane.showMessageDialog(GameGui.gameFrame, checkWinnings(players[0]));
			restartGame();}
		if (valueOfHands[1]>21) {
			moreCards.setVisible(false);
			stay.setVisible(false);
			bottomPanel.updateUI();
			dealersTurn();
			JOptionPane.showMessageDialog(GameGui.gameFrame, checkWinnings(players[0]));
			restartGame();}
		break;
	case(2):
		//player doesn't take more cards.
		if (hasAce(players[0])&&players[0].getNumOfCards()==2) { valueOfHands[1]+=10;sumOfPlayer.setText(Integer.toString(valueOfHands[1]));}
		doubleBet.setVisible(false);
		moreCards.setVisible(false);
		stay.setVisible(false);
		bottomPanel.updateUI();
		dealersTurn();
		JOptionPane.showMessageDialog(GameGui.gameFrame, checkWinnings(players[0]));
		restartGame();
		break;
	case(3):
		//Doubles the bet + one card
		bet+=bet;
		valueOfBet.setText(Double.toString(bet));
		doubleBet.setVisible(false);
		moreCards.setVisible(false);
		stay.setVisible(false);
		bottomPanel.updateUI();
		newCardToPlayer();
		dealersTurn();
		JOptionPane.showMessageDialog(GameGui.gameFrame, checkWinnings(players[0]));
		restartGame();
		break;
	case(4):
		//TODO Split the deck
		break;
	default:
		break;
	}
	
}


/**
 * Asks player if they want to continue. Closes game or starts a new game.
 */
private void restartGame() {
	int temp=JOptionPane.showConfirmDialog(GameGui.gameFrame, "Play again?", "Continue?", JOptionPane.YES_NO_OPTION);
	if (temp==JOptionPane.NO_OPTION) closeGame();
	else {
		numOfCredits.setText(Double.toString(credits));
		valueOfBet.setText(Double.toString(bet));
		bottomPanel.updateUI();
		topPanel.removeAll();
		topPanel.updateUI();
		middlePanel.removeAll();
		middlePanel.updateUI();
		for (int i=0;i<players.length;i++) {
			players[i].emptyHand(); }
		Cardgames.j1.emptyHand(false);
		startSinglePlayer();
	}
	
}
@Override
public void windowClosing(WindowEvent e) {
	closeGame();
}
/**
 * Ends the game and disposes of the window.
 */
private void closeGame() {
	running=false;
	Cardgames.j1.emptyHand(true);
	GameGui.gameFrame.dispose();
	
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
/**Asks user for bet to play with.
 * @return The desired bet.
 */
private double getBet() {
	double num;
	String in=null;
	while (true){
		
		while(true){
			in=JOptionPane.showInputDialog("Place your bet.\nYou have "+credits+" credits.");
			if (in==null) {
			int temp=JOptionPane.showConfirmDialog(GameGui.gameFrame, "Are you sure you want to cancel?", "Cancel?", JOptionPane.YES_NO_OPTION);
			if (temp==JOptionPane.YES_OPTION) {
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
			num=Double.parseDouble(in);
		}
		catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(GameGui.gameFrame, "Error! Not a number.\nPlease enter a number.");
			continue;
		}
		if (num<0.5) {
			JOptionPane.showMessageDialog(GameGui.gameFrame, "Error!\nYou have bet at least 0.5.");
			continue;
		}
		break;
	}
	
	return num;
}
/**Creates the HashMap for for card value counting.
 * @return HashMap
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
//**********
//Not used.*
//**********
@Override
public void windowOpened(WindowEvent e) {
	// TODO Auto-generated method stub
	
}

@Override
public void windowClosed(WindowEvent e) {
	//TODO Auto-generated method stub
	
}


@Override
public void windowIconified(WindowEvent e) {
	// TODO Auto-generated method stub
	
}


@Override
public void windowDeiconified(WindowEvent e) {
	// TODO Auto-generated method stub
	
}


@Override
public void windowActivated(WindowEvent e) {
	// TODO Auto-generated method stub
	
}


@Override
public void windowDeactivated(WindowEvent e) {
	// TODO Auto-generated method stub
	
}

}
