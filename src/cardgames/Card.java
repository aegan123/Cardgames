/*
	Class representing a playing card.
    Copyright (C) 2017  Juhani Vähä-Mäkilä

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

/** Models a playing card
 * @author Juhani Vähä-Mäkilä, 2017 GNU GPL v2
 * @version 1.0
 */
class Card implements Comparable<Card> {
	//************
	//Tietokentät*
	//************
	/** Kortin maa*/
	private Maa maa;
	/** Kortin arvo*/
	private Arvo arvo;
	
	//**************************************
	//Kortin mahdolliset arvot sisäluokkina*
	//**************************************
	/** Sisäluokka Kortin maata varten.
	 * 
	 * @author Juhani Vähä-Mäkilä, 2016 CC BY-NC-ND
	 *
	 */
	public enum Maa {
		HERTTA, RISTI, RUUTU, PATA
		}
	/** Sisäluokka Kortin arvoa varten.
	 * 
	 * @author Juhani Vähä-Mäkilä, 2016 CC BY-NC-ND
	 *
	 */
	public enum Arvo {
		A,II,III,IV,V,VI,VII,VIII,IX,X,J,Q,K
}
	 //*************
	 //Konstruktori*
	 //*************
	/** Luo uuden Kortin.
	 * @param maa Kortin maa (tyyppiä enum)
	 * @param arvo Kortin arvo (tyyppiä enum)
	 */
	public Card(Maa maa, Arvo arvo) {
		this.maa=maa;
		this.arvo=arvo;
	}
	//********************
	 //Havainnoitimetodit*
	 //*******************
	 /** Palauttaa kortin arvon.
	 * @return Kortin Arvo. Arvo on enum tyyppiä.
	 */
	public Arvo getArvo() {
		return this.arvo;
	}
	/** Palauttaa kortin maan.
	 * @return Kortin maa.
	 */
	public Maa getMaa() {
		return this.maa;
}
	/** Palauttaa Kortin tiedot merkkijonona.
	 * @return {@literal <Maa Arvo>}
	 */
	@Override
	public String toString() {
		return "<"+this.maa+" "+this.arvo+">";
	}
	/** Kortti-olioiden yhtäsuuruuden vertaus.
	 * @return true jos yhtäsuuri, false jos ei.
	 */
	@Override
	  public boolean equals(Object ob) {
	    if (this==ob) return true;
	    if (ob==null || !(ob instanceof Card)) return false;
	    Card k=(Card) ob;
	    return this.compareTo(k)==0;
	  }
	
	/** Comparable rajapintaa varten.
	 * @param kortti Kortti johon verrataan.
	 * @return {@literal <0 jos pienempi, 0 jos yhtäsuuri, >0 jos suurempi}
	 * @throws NullPointerException Mikäli verrattava Kortti==null.
	 */
	@Override
	public int compareTo(Card kortti) throws NullPointerException {
		{
			if (kortti==null) throw new NullPointerException();
			if (Player.MAP.get(this.arvo).intValue()<Player.MAP.get(kortti.arvo).intValue()) return -1;
			if (Player.MAP.get(this.arvo).intValue()==Player.MAP.get(kortti.arvo).intValue()) return 0;
			else
				return 1;

			}
		
	}

}
