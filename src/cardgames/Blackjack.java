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

import java.util.HashMap;
import cardgames.Card.Rank;

/**Implementation of Blackjack.
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
class Blackjack extends Games implements Runnable{
	/**HashMap for counting the value of the hand.*/
	static final HashMap<Card.Rank,Integer> MAP=createMap();
	private static int howManyPlayers;
	/**Array of players.*/
	static Player[] players;
	private static boolean running;
	private static double bet=0;
	private static double credits=0;
	private static double insurance;
	/**Array to store numerical values of dealt hands. Slot 0 is dealers, all others are for players. */
	static int[] valueOfHands;
	private static boolean playerInsured=false;
	private static BlackjackGui gui;
	//private static final String rules=""; //TODO
	

public Blackjack(){
	gui=new BlackjackGui();

}

//***************
//Start the game*
//***************

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
private static void startSinglePlayer() {
	if(Cardgames.j1.getSize()<=15) {
	Cardgames.j1.setDeck(4);}
	players[0].setMyTurn(true);
	if (credits<=0) {
			credits=10.0;
			gui.setNumOfCredits(Double.toString(credits));
			gui.setNumOfCredits(true);
	}
	bet=gui.getBet(credits);
	credits-=bet;
	gui.setValueOfBet(Double.toString(bet));
	gui.setValueOfBet(true);
	gui.setNumOfCredits(Double.toString(credits));
	initialDeal();
	if (isBlackjack(players[0])) {
		valueOfHands[1]=21;
		gui.playersBlackjack();
		dealersTurn();
		gui.showMsg(checkWinnings(players[0]));
		//JOptionPane.showMessageDialog(GameGui.gameFrame, checkWinnings(players[0]));
		restartGame();
	} 
	else {
		if (canInsure()) {
			//JOptionPane.showConfirmDialog(GameGui.gameFrame, "Dealer might have a Blackjac.\nDo you want to insure your hand against it?\nInsurance is half of your current bet.", "Insure?", JOptionPane.YES_NO_OPTION);
			//if (JOptionPane.YES_OPTION==JOptionPane.showConfirmDialog(GameGui.gameFrame, "Dealer might have a Blackjack.\nDo you want to insure your hand against it?\nInsurance is half of your current bet.", "Insure?", JOptionPane.YES_NO_OPTION)) {
			if(gui.isYes("Dealer might have a Blackjack.\nDo you want to insure your hand against it?\nInsurance is half of your current bet.", "Insure?")) {	
				playerInsured=true;
				insurance=bet/2; }
		}
		gui.setMoreCards(true);
		gui.setStay(true);
		if (canSplit()) {gui.setSplit(true);}
		gui.setDoubleBet(true);
		gui.updateGui(2);
		//middlePanel.updateUI();
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
		initialDeal();
	
}
//**********************
//Hand checking methods*
//**********************
/**Checks if player has a Blackjack.
 * @return True if has. Otherwise false.
 */
private static boolean isBlackjack(Player player) {
	return checkHand(player)==11 && (player.getCard(0).getRank().equals(Card.Rank.A) || player.getCard(1).getRank().equals(Card.Rank.A));
}
/**Checks if dealer has a Blackjack.
 * @return True if has. Otherwise false.
*/
private static boolean isBlackjack(Dealer j1) {
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
private static boolean canSplit() {
	return players[0].getCard(0).getRank().equals(players[0].getCard(1).getRank());
}
/**Checks if player can insure against dealers Blackjack.
 * @return True/false.
 */
private static boolean canInsure() {
	return Cardgames.j1.getCard(0).getRank().equals(Card.Rank.A);
}
/**
*Checks if player has an ace.
* @param player The player whose hand we check.
* @return True/False.
*/
static boolean hasAce(Player player) {
	return player.getCard(0).getRank().equals(Card.Rank.A) || player.getCard(1).getRank().equals(Card.Rank.A);
}
/**
 * Checks if dealer has an ace.
 * @param j1 Dealer whose hand we check.
 * @return True/False.
 */
static  boolean hasAce(Dealer j1) {
	return j1.getCard(0).getRank().equals(Card.Rank.A);
}
/**Checks if players hand is a 10 card charlie (see rules).
 * @return True/False.
 */
static boolean isCharlie() {
	return players[0].getNumOfCards()==10 && valueOfHands[1]<21;
}

/**Counts the value of hand.
* @param j1 The Dealer.
* @return Total value of hand.
*/
private static int checkHand(Dealer j1) {
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
private static int checkHand(Player player) {
	int sum=0;
	for (int i=0;i<player.getNumOfCards();i++) {
		sum+=MAP.get(player.getCard(i).getRank()).intValue();
	}
	return sum;
}
/**
 * Checks if player has won. Adds winnings to credits.
 */
private static String checkWinnings(Player player) {
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
 */
private static void initialDeal() {
	for (int j=0;j<players.length;j++) {
		players[j].addCard(Cardgames.j1.dealCard());
		Cardgames.j1.addCard(Cardgames.j1.dealCard());
	}
	valueOfHands[0]=checkHand(Cardgames.j1);
	valueOfHands[1]=checkHand(players[0]);
	gui.initialDeal(players);
	
	/*
	BufferedImage img = null;
	gui.initialDeal();
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
	        gui.updateGui(2);
	        //middlePanel.updateUI();
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
        		gui.setSumOfDealer("1/11");
        	}
        	else
        		gui.setSumOfDealer(MAP.get(Cardgames.j1.getCard(k).getRank()).toString());
        
        }
        topPanel.add(new JLabel(new ImageIcon(img)));
        gui.updateGui(1);
        //topPanel.updateUI();
	}
	valueOfHands[0]=checkHand(Cardgames.j1);
	valueOfHands[1]=checkHand(players[0]);
	//****
    //TODO remove after testing
    gui.setSumOfDealer(Integer.toString(valueOfHands[0]));
    gui.updateGui(1);
    //topPanel.updateUI();
    //****
	if (hasAce(players[0])) gui.setSumOfPlayer(valueOfHands[1]+"/"+Integer.toString(valueOfHands[1]+10));
	else gui.setSumOfPlayer(Integer.toString(valueOfHands[1]));
	
	gui.updateGui(2);
	//middlePanel.updateUI();
	 */
}


/**
 * Does the dealers turn after player(s) have finished.
 */
private static void dealersTurn() {
	gui.dealersTurn(true, valueOfHands[0]);
	if (isBlackjack(Cardgames.j1)) { valueOfHands[0]=21; gui.dealersBlackjack();}
	else {
		while(valueOfHands[0]<17) {
			Cardgames.j1.addCard(Cardgames.j1.dealCard());
			valueOfHands[0]=checkHand(Cardgames.j1);
			gui.dealersTurn(false, valueOfHands[0]);
		}
	}
	/*
	topPanel.remove(2);
	try {
		topPanel.add(new JLabel(new ImageIcon(ImageIO.read(Cardgames.j1.getCard(1).getPic()))));
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	gui.setSumOfDealer(Integer.toString(valueOfHands[0]));
	gui.updateGui(1);
	//topPanel.updateUI();
	if (isBlackjack(Cardgames.j1)) { valueOfHands[0]=21; gui.setSumOfDealer("Blackjack!");	gui.updateGui(1);}
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
		gui.setSumOfDealer(Integer.toString(valueOfHands[0]));
		topPanel.add(new JLabel(new ImageIcon(img)));
		gui.updateGui(1);
	    //topPanel.updateUI();
        }
	}
	*/
	}

//**************
//Other methods*
//**************
/**
 * Asks player if they want to continue. Closes game or starts a new game.
 */
private static void restartGame() {
	//int temp=JOptionPane.showConfirmDialog(GameGui.gameFrame, "Play again?", "Continue?", JOptionPane.YES_NO_OPTION);
	if(gui.isYes("Play again?", "Continue?")) closeGame();
	//if (temp==JOptionPane.NO_OPTION) closeGame();
	else {
		gui.setNumOfCredits(Double.toString(credits));
		gui.setValueOfBet(Double.toString(bet));
		gui.resetGui();
		for (int i=0;i<players.length;i++) {
			players[i].emptyHand(); }
		Cardgames.j1.emptyHand(false);
		startSinglePlayer();
	}
	
}

/**
 * Ends the game and disposes of the window.
 */
static void closeGame() {
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
			//in=JOptionPane.showInputDialog("How many players?");
			in=gui.showInputDialog("How many players?");
			if (in==null) {
			//int joku=JOptionPane.showConfirmDialog(GameGui.gameFrame, "Are you sure you want to cancel?", "Cancel?", JOptionPane.YES_NO_OPTION);
			//if (joku==0) {
				if(gui.isYes("Are you sure you want to cancel?", "Cancel?")) {
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
			gui.showMsg("Error! Not a number.\nPlease enter a number.");
			//JOptionPane.showMessageDialog(GameGui.gameFrame, "Error! Not a number.\nPlease enter a number.");
			continue;
		}
		if (num<1) {
			gui.showMsg("Error!\nThere has to be at least ONE player.");
			//JOptionPane.showMessageDialog(GameGui.gameFrame, "Error!\nThere has to be at least ONE player.");
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

static void moreCards() {
	players[0].addCard(Cardgames.j1.dealCard());
	valueOfHands[1]=checkHand(players[0]);
	gui.newCardToPlayer(players, valueOfHands);
	if (isCharlie()) {
		gui.isCharlie(checkWinnings(players[0]));
		restartGame();}
	if (valueOfHands[1]>21) {
		gui.over21();
		dealersTurn();
		gui.showMsg(checkWinnings(players[0]));
		restartGame();}
}

static void noMoreCards() {
	if (hasAce(players[0])&&players[0].getNumOfCards()==2) {
		valueOfHands[1]+=10;
		gui.setSumOfPlayer(Integer.toString(valueOfHands[1]));
		}
	gui.noMoreCards();
	dealersTurn();
	gui.showMsg(checkWinnings(players[0]));
	restartGame();
	
}

static void doubleBet() {
	bet+=bet;
	gui.doubleBet(bet);
	players[0].addCard(Cardgames.j1.dealCard());
	dealersTurn();
	gui.showMsg(checkWinnings(players[0]));
	restartGame();
}


}
