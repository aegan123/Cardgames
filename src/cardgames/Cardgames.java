/*
	Main starting point of program
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

import java.io.File;
import java.time.LocalDate;
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
	private static final double versionNum=1.0;
	static final File license=new File("LICENSE");
	static final String copyrightNotice="Cardgames version "+versionNum+", Copyright (C) "+LocalDate.now().getYear()+" Juhani Vähä-Mäkilä.\nCardgames comes with ABSOLUTELY NO WARRANTY\nThis is free software licensed under GNU GPL v. 2.0, and you are welcome to redistribute it under certain conditions.";
	static boolean verbose;
	
	@SuppressWarnings("javadoc")
	public static void main(String[] args) {
		switch (args.length) {
		case (0):
			break;
		case (1):
			if (args[0].equals("test")) verbose=true; runTests();
			break;
		default:
			break;
		}
			

	}

	private static void runTests() {
		j1.setDeck(1);
		System.out.println(copyrightNotice);
		for (int i=0;i<52;i++){
		System.out.println(j1.getCard()); }
		
	}
	/** Creates a HashMap for numerical comparison of cards.
	 * @return HashMap that has values of 2-14 (ace is 14 so it'll be the largest) and their counterpart in Card.Rank.
	 * 
	 */
	private static HashMap<Rank, Integer> createMap() {
			HashMap<Rank,Integer> temp=new HashMap<Rank,Integer>(19);
			temp.put(Rank.A, new Integer(14));
			temp.put(Rank.II, new Integer(2));
			temp.put(Rank.III, new Integer(3));
			temp.put(Rank.IV, new Integer(4));
			temp.put(Rank.V, new Integer(5));
			temp.put(Rank.VI, new Integer(6));
			temp.put(Rank.VII, new Integer(7));
			temp.put(Rank.VIII, new Integer(8));
			temp.put(Rank.IX, new Integer(9));
			temp.put(Rank.X, new Integer(10));
			temp.put(Rank.J, new Integer(11));
			temp.put(Rank.Q, new Integer(12));
			temp.put(Rank.K, new Integer(13));
			
			return temp;
	}

}
