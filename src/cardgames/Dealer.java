/*
	Class representing the dealer in card games.
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
package src.cardgames;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

import src.cardgames.Card.Rank;
import src.cardgames.Card.Suit;

/** Class representing the dealer in card games.
 * @version 0.5
 */
class Dealer implements Serializable {
    //************
    //Attributes*
    //************

    private static final long serialVersionUID = 1L;
    /** Protodeck. One base deck to be created only once.
     * All decks used in games are copies of this.
     */
    private static final Card[] protodeck=createProtoDeck();
    /**The actual deck used within games. */
    private List<Card> deck;
    /**Dealt hand of cards. */
    private List<Card> hand;

    //************
    //Constructor*
    //************
    /** Constructs a new dealer. */
    public Dealer() {
        this.deck=null;
    }

    //*********
    //Setters*
    //********

    /**
     * Creates the protodeck with 52 cards.
     * Exactly one card of any given type.
     * @return
     */
    @SuppressWarnings("unchecked")
    private static Card[] createProtoDeck() {
        List<Card> temp=null;
        File f=new File("protodeck.dat");
        if (f.exists() && f.canRead()) {
            if (Cardgames.verbose) System.out.println("Existing file for protodeck found. Loading it.");
            ObjectInputStream ois=null;
            try {
                ois=new ObjectInputStream(new FileInputStream(f));
                temp=(ArrayList<Card>) ois.readObject();
                ois.close();
                if (Cardgames.verbose) System.out.println("protodeck.size == "+temp.size());
            } catch (ClassNotFoundException | IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        } else {
            if (Cardgames.verbose) System.out.println("Protodeck file not found. Creating new deck and file.");
            ObjectOutputStream oos=null;
            temp=new ArrayList<Card>(52);
            for (Suit suit: Suit.values()) {
                for (Rank rank: Rank.values()) {
                    temp.add(new Card(suit, rank));
                }}
            try {
                oos=new ObjectOutputStream(new FileOutputStream(f));
                oos.writeObject(temp);
                oos.flush();
                oos.close();
                if (Cardgames.verbose) System.out.println("protodeck.size == "+temp.size());
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }}
        return temp.toArray(new Card[52]);
    }
    /** Creates a new deck and shuffles it.
     * @param lkm The number of 52 card deck(s) needed. Will default to 1 if parameter is <=0 or >10.
     *
     */
    public void setDeck(int lkm) {
        if (lkm<=0 || lkm>10) this.deck=copyProtoDeck(1);
        else this.deck=copyProtoDeck(lkm);
        if (Cardgames.verbose) System.out.println("deck.size == "+deck.size());
        for (int i=0;i<20;i++){
            Collections.shuffle(deck);
        }

    }
    /**
     * Copies the protodeck for use.
     * @param lkm The number of 52 card decks we want. Is always >=1.
     * @return The desired deck.
     */
    private Stack<Card> copyProtoDeck(int lkm) {
        Stack<Card> temp=new Stack<Card>();
        for (int i=0; i<lkm;i++) {
            for (int j=0;j<protodeck.length;j++){
                temp.add(protodeck[j]);
            }
        }
        return temp;
    }
    /**
     * Adds a card to the hand.
     * @param card The card to add.
     */
    public void addCard(Card card) {
        if (this.hand==null) hand=new ArrayList<Card>();
        hand.add(card);
    }
    /**
     * Empties the hand from all card. Or completely nulls the hand.
     * @param i True if the game is completely over and hand is no longer needed. False if hand is needed still.
     */
    public void emptyHand(boolean i) {
        if (i) this.hand=null;
        else this.hand.clear();
    }
    //********
    //Getters*
    //********
    /**
     * Returns the first card from the deck and removes it from the deck.
     * @return The first card from the deck.
     */
    public Card dealCard(){
        return  ((Stack<Card>) this.deck).pop();
    }
    /**
     * Returns the size of the deck.
     * @return Size of the deck.
     */
    public int getSize(){
        if (deck==null) return 0;
        return deck.size();
    }
    /**
     * Returns the desired card from hand.
     * @param i Index of the card wanted.
     * @return The desired card from hand.
     */
    public Card getCard(int i) {
        return hand.get(i);
    }

    /**Returns the number of cards in hand.
     * @return Returns the number of cards in hand.
     */
    int getNumOfCards() {
        return hand.size();
    }
}
