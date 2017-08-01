/*
	Class representing the dealer in cardgames
    Copyright (C) 2017  Juhani Vähä-Mäkilä, juhani@fmail.co.uk

    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License along
    with this program; if not, write to the Free Software Foundation, Inc.,
    51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
    */
package cardgames;
import java.util.ArrayList;
import java.util.List;
import cardgames.Card.Arvo;
import cardgames.Card.Maa;

/** 
 * @version 0.5
 */
class Dealer {
	//************
	//Attributes*
	//************
	
	/** Protodeck. One base deck to be created only once.*/
	private static final ArrayList<Card> protodeck=createProtoDeck();
	/**
	 * 
	 */
	private List<Card> deck;
	
	//**************
	//Constractor*
	//**************
	/** Constracts a new dealer.
	 * 
	 */
	public Dealer() {
	}

	/**
	 * Creates the protodeck with 52 cards.
	 * Exactly one card of any given type.
	 * @return
	 */
	private static ArrayList<Card> createProtoDeck() {
		ArrayList<Card> uusi=new ArrayList<Card>(52);
		for (Maa maa: Maa.values()) {
			for (Arvo arvo: Arvo.values()) {
				uusi.add(new Card(maa, arvo));
			}}
		return uusi;
	}

	//**************
	//Muutosmetodit*
	//**************
	/** Luo uuden pakan.
	 * @param lkm The number of 52 card deck needed.
	 * 
	 */
	public void setDeck(int lkm) {
		
	}


}
