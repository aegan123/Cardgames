/*
	Main starting point of program
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

import java.util.HashMap;
import cardgames.Card.Rank;
/**
 * 
 * @author Juhani Vähä-Mäkilä, 2017 GNU GPL v2
 * @version 0.1
 *
 */
public class Cardgames {
	static final Dealer j1=new Dealer();
	static final HashMap<Rank,Integer> MAP=createMap();
	
	@SuppressWarnings("javadoc")
	public static void main(String[] args) {
		switch (args.length) {
		case (0):
			break;
		case (1):
			if (args[0].equals("test")) runTests();
			break;
		default:
			break;
		}
			

	}

	private static void runTests() {
		// TODO Auto-generated method stub
		
	}

	private static HashMap<Rank, Integer> createMap() {
		// TODO Auto-generated method stub
		return null;
	}

}
