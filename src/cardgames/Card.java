/*
	Class representing a playing card.
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

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import javax.imageio.ImageIO;

/** Models a playing card
 * @author Juhani Vähä-Mäkilä, 2017 GNU GPL v2
 * @version 1.0
 */
final class Card implements Comparable<Card>, Serializable {

	//***********
	//Attributes*
	//***********
	/** Suit of card*/
	private Suit suit;
	/** Rank of card*/
	private Rank rank;
	/**	Picture representing the card*/
	private BufferedImage pic;
	private static final long serialVersionUID = 1L;
	/**Common back art for all cards.*/
	private static final BufferedImage backArt=setBackArt();
	
	//************************************************************
	//Rank-Suit combinations of cards as inner emun type classes*
	//************************************************************
	/** Suit of card
	 * 
	 * @author Juhani Vähä-Mäkilä, 2017 GNU GPL v2
	 * @version final
	 */
	public enum Suit {
		HEARTS, CLUBS, DIAMONDS, SPADES
		}
	/** Rank of card
	 * 
	 * @author Juhani Vähä-Mäkilä, 2017 GNU GPL v2
	 * @version final
	 */
	public enum Rank {
		A,II,III,IV,V,VI,VII,VIII,IX,X,J,Q,K
}
	 //************
	 //Konstructor*
	 //************
	/** Creates a new card.
	 * @param suit Suit of card.
	 * @param rank Rank of card.
	 */
	public Card(Suit suit, Rank rank) {
		this.suit=suit;
		this.rank=rank;
		this.pic=setPic();
	}
	//********
	//Setters*
	//********
	/**
	 * Sets the back art of all cards.
	 * @return Image of the back art.
	 */
	private static BufferedImage setBackArt() {
		BufferedImage temp=null;
		try {
			temp=ImageIO.read(new File("img/BackArt.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return temp;
	}
	/**
	 * Sets the right picture file for the card.
	 * @return
	 */
	private BufferedImage setPic() {
		BufferedImage img=null;
		try {
		    img = ImageIO.read(new File("img/"+this.asString()+".png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return img;
	}
	/**
	 * Returns a standardized string to be used in a file name.
	 * @return A standardized string to be used in a file name.
	 */
	private String asString() {
		return this.rank+"_of_"+this.suit;
	}
	//*********
	 //Getters*
	 //********
	/**
	 * Returns the image representing the card.
	 * @return Image representing the card.
	 */
	public BufferedImage getPic() {
		return this.pic;
		
	}
	/**
	 * Returns the back art of card.
	 * @return Image representing the back art of card.
	 */
	public BufferedImage getBackArt() {
		return Card.backArt;
	}
	/** String representation of the card.
	 * @return {@literal <RANK of SUIT>}
	 */
	@Override
	public String toString() {
		return "<"+this.rank+" of "+this.suit+">";
	}
	/** Checks if two object are equal.
	 * @return True if they are, False if not.
	 */
	@Override
	  public boolean equals(Object ob) {
	    if (this==ob) return true;
	    if (ob==null || !(ob instanceof Card)) return false;
	    Card k=(Card) ob;
	    return this.compareTo(k)==0;
	  }
	
	/** Implements the Comparable interface.
	 * @param card The card what to compare against.
	 * @return {@literal <0 if smaller, 0 if equal, >0 if larger}
	 */
	@Override
	public int compareTo(Card card) {
		{
			if (Cardgames.MAP.get(this.rank).intValue()<Cardgames.MAP.get(card.rank).intValue()) return -1;
			if (Cardgames.MAP.get(this.rank).intValue()==Cardgames.MAP.get(card.rank).intValue()) return 0;
			else
				return 1;

			}
		
	}

}
