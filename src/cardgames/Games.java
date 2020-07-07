/*
	Superclass for all card game implementations.
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
package src.cardgames;

import java.util.Arrays;

/**Superclass for all card game implementations.
 * @author Juhani Vähä-Mäkilä
 * @version 0.1
 */
abstract class Games {
	/** Adds the desired player to the top score list of a game.
	 * @param player The player to be added to the list.
	 * @param list The list to add.
	 */
	protected void saveTopScore(Player player, Player[] list) {
		list[list.length-1]=Player.copyOf(player);
		Arrays.sort(list);
	}
	/**
	 * Creates a static array of players.
	 * @param howManyPlayers How many players are there in the game.
	 * @return An Array containing the desired amount of players.
	 */
	protected Player[] getPlayers(int howManyPlayers) {
		Player[] temp=new Player[howManyPlayers];
		for (int i=0;i<howManyPlayers;i++) {
			temp[i]=new Player(i+1);
		}
		return temp;
	}
	/**
	 * Returns the index of the player whose turn it is.
	 * @param list Array of players.
	 * @return Index of the player whose turn it is.
	 */
	protected int whosTurn(Player[] list) {
		int temp=-1;
		for (int i=0;i<list.length;i++) {
			if (list[i].isMyTurn()) temp=i;
		}
		return temp;
	}
}
