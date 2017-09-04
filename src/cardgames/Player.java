/*
	Class representing a player in a game.
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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
/** Models a player in a game.
 * @author Juhani Vähä-Mäkilä, 2017 GNU GPL v2
 * @version 0.5
 */
class Player implements Serializable, Comparable<Player> {
	//***********
	//Attributes*
	//***********
	/** Name of the player.*/
	private String name;
 	/** Dealt hand of cards.*/
 	private List<Card> hand;
	/** Collected points. Value depends on game. */
 	private int points;
 	/**Denotes whether it's this players turn in multiplayer scenarios. */
 	private boolean myTurn;
 	/**Players number. Used in multiplayer scenarios. */
	private int playerNum;
	private static final long serialVersionUID = 2L;

 
 	//************
 	//Constructor*
 	//************
/**
 * Constructs a new Player. Sets players number to what the parameter is and initializes the hand.
 * @param i Players number.
 */
 public Player(int i) {
	 this.playerNum=i;
	 this.name=null;
	 this.points=0;
	 this.hand=new ArrayList<Card>();
	 }

 /**
  * Only used with copyOf method.
  * @param player The Player to copy.
  */
 	private Player(Player player) {
		this.name=player.getName();
		this.points=player.getPoints();
		this.hand=null;
	}

	//********
 	//Getters*
 	//********
 
 /** Returns the player name.
 * @return Name of the player.
 */
 public String getName() {
	 return this.name;
 }
/** Returns players current points.
 * @return Players current points.
 */
public int getPoints() {
	return this.points;
}
 /** String representation of all the info of the player.
  * @return {@literal Player: this.name \t Points: this.points}
  */
@Override
public String toString() {
  return "Player: "+this.name+"\tPisteet: "+this.points;
}

/** Returns the desired card from hand.
 * @param i Index of the card we want from the hand.
* @return Desired card.
*/
public Card getCard(int i) {
	return this.hand.get(i);
}
/** Return true/false if it's this players turn in game.
 * @return True if it's this player turn in game. False if not.
 */
public boolean isMyTurn() {
	return myTurn;
}
/** Returns players number which is used in multiplayer scenarios.
 * @return Players number used in multiplayer situations.
 */
public int getPlayerNum(){
	return this.playerNum;
}
	//********
	//Setters*
	//********

/**Set whether it's this players turn in game.
 * @param myTurn True or False
 */
public void setMyTurn(boolean myTurn) {
	this.myTurn = myTurn;
}

/** Sets the players name.
 * @param name New name for player.
 */
public void setName(String name) {
	this.name=name;
}
/** Sets new points for player.
 * @param points Points to add.
 */
public void setPoints(int points) {
	this.points+=points;
}
	//************************************
	//Methods for handling the dealt hand*
	//************************************

/**
 * Adds a card to the hand.
 * @param card The card to add to the hand.
 */
public void addCard(Card card){
	this.hand.add(card);
}

/** Replaces the desired card in hand to another.
 * @param i Index of the card we want to replace.
 * @param card New card to replace the old one.
 */
public void changeCard (int i, Card card) {
	this.hand.set(i, card);
}
/**
 * Removes the specified card from hand.
 * @param i Index of the card to be removed.
 */
public void removeCard(int i){
	this.hand.remove(i);
}
/**
 * Empties the hand after a game.
 */
public void emptyHand() {
	this.hand.clear();
}

	//**************
	//Other methods*
	//**************
/** Checks if two object are equal.
 * @return True if they are, False if not.
 */
@Override
  public boolean equals(Object ob) {
    if (this==ob) return true;
    if (ob==null || !(ob instanceof Player)) return false;
    return this.compareTo((Player) ob)==0;
  }
/**
 * Compares player using points.
 * @param o The player to compare to.
 * @return {@literal <0 if smaller, 0 if equal, >0 if greater}
 */
@Override
public int compareTo(Player o) {
	if (this.points<o.points) return -1;
	if (this.points==o.points) return 0;
	else
		return 1;
}
/**
 * Makes a copy of the player object.
 * Used when saving Top10 lists within different games.
 * @param player The Player to copy.
 * @return Copy of the player.
 */
static Player copyOf(Player player){
	return new Player(player);
}

/**Returns the number of cards in hand.
 * @return The number of cards in hand.
 */
int getNumOfCards() {
	return hand.size();
}


}
