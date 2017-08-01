/*
	Class representing a player in a game.
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

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import cardgames.Card.Arvo;
import cardgames.Card.Maa;
/** Mallintaa pokerinpelaajaa.
 * @author Juhani Vähä-Mäkilä, 2016 CC BY-NC-ND
 * http://creativecommons.org/licenses/by-nc-nd/4.0/
 * @version 1.0
 */
class Player {
	
/** HashMap taulukko kortin arvon numeerista vertailua varten.*/
protected static final HashMap<Arvo,Integer> MAP=luoMap();

	//************
	//Tietokentät*
	//************
	/** Pelaajan nimi.*/
	private String nimi;
 	/** Jaettu käsi.*/
 	private LinkedList<Card> hand;
	/** Saadut pisteet.
	 * {@literal Pisteet määräytyy seuraavasti: Pari 10, Kaksi paria 20, Kolmoset 30, Suora 40 Väri 50, Täyskäsi 60, Neloset 70,
	 * Värisuora 100, Kunkkuvärisuora 500.}
	 */
 	private int pisteet;

 
 	//**************
 	//Konstruktorit*
 	//**************
 /** Asettaa nimeksi tyhjän merkkijonon ja luo uuden tyhjän listan kättä varten.
  */
 public Player() {
	 this.nimi="";
	 this.pisteet=0;
	 this.hand=new LinkedList<Card>();
	 
 }
 /** Asettaa saadun nimen nimeksi ja luo uuden tyhjän listan kättä varten.
  * @param nimi Pelaajan nimi.
  */
 public Player(String nimi) {
	 this.nimi=nimi;
	 this.pisteet=0;
	 this.hand=new LinkedList<Card>();
 }

 	//*******************
 	//Havainnoitimetodit*
 	//*******************
 /** Palauttaa pelaajan nimen.
 * @return Pelaajan nimen.
 */
 public String getNimi() {
	 return this.nimi;
 }
 /** Palauttaa pelaajan käden.
  * @return Merkkijonoesitys pelaajan kädestä.
  */
 public String getHand() {
 	return this.hand.toString();
 }
 
/** Palauttaa pelaajan pistesaldon.
 * @return Pelaajan pisteet int lukuna.
 */
public int getPisteet() {
	return this.pisteet;
}
 /** Palauttaa pelaajatiedot merkkijonona.
  * @return {@literal Merkkijonoesitys pelaajasta: Player: this.Nimi \n Pisteet: this.pisteet \n this.hand}
  */
@Override
public String toString() {
  return "Player: "+this.nimi+"\nPisteet: "+this.pisteet+"\n"+this.hand;
}
/** Palauttaa käden hain eli suurimman kortin.
 * @return Käden suurimman kortin.
 */
public Card suurin() {
	Collections.sort(this.hand);
	return this.hand.get(this.hand.size()-1);
	
/*	int suurin=1, indeksi=-1, luku;
	for (int i=0; i<this.hand.size();i++) {
		luku=MAP.get(this.hand.get(i).getArvo()).intValue();
		if (luku>suurin) {
				 indeksi=i;
				 suurin=luku;
			}
	}
	return this.hand.get(indeksi); */
}
/** Tarkistaa pelaajan käden mahdollisten voittojen varalta.
 * @return Korkeimman kortin numeroarvon ja sanallinen kuvaus voitosta merkkijonona pilkulla erotettuna.
 * Tulee käsitellä sopivasti, jotta pelaajalle voidaan
 * esittää mitä oli voittanut ja mikä oli korkein kortti. 
 * {@literal Palauttaa seuraavaa: RoaylFlush: -1,royalflush; StraighFlush: 2-14,straightflush; Neloset: 2-14,neloset; FullHouse: 2-14,fullhouse;
 * Flush: 2-14,flush; Suora: 2-14,suora; Kolmoset: 2-14,kolmoset; Kaksi paria: 2-14,kaksiparia; Pari: 2-14,pari. Jos mikään ei täsmää palauttaa 0,0 }
 */
public String checkHand() {
	LinkedList<Card> temp_list=new LinkedList<Card>(this.hand);
/*	for (int j=0; j<this.hand.size(); j++) {
		temp_list.add(this.hand.get(j));
	}*/
	Collections.sort(temp_list);
	int[] temp=new int[temp_list.size()];
	for (int i=0; i<temp_list.size(); i++) {
		temp[i]=MAP.get(temp_list.get(i).getArvo()).intValue();
	}
	if (onkoRoyalFlush(temp_list,temp)) {
		this.pisteet+=500;
		return "-1,kuningasvärisuoran";
	}
		
	if (onkoStraightFlush(temp_list,temp)) {
		this.pisteet+=100;
		if (temp[4]==14 && temp[0]==2)
			return temp[3]+",värisuoran"; 
		else
			return temp[4]+",värisuoran";
	}
	if (onkoNeloset(temp)) {
		this.pisteet+=70;
		return MAP.get(temp_list.get(indeksi(temp)).getArvo()).intValue()+",neloset";
	}
		
	if (onkoFullHouse(temp)) {
		this.pisteet+=60;
		return MAP.get(temp_list.get(temp_list.size()-1).getArvo()).intValue()+",täyskäden";
	}
		
	if (onkoFlush(temp_list)) {
		this.pisteet+=50;
		return temp[4]+",värin";
	}
		
	if (onkoSuora(temp)) {
		this.pisteet+=40;
		if (temp[4]==14)
			return temp[3]+",suoran"; 
		else
			return temp[4]+",suoran";
	}
	if (onkoKolmoset(temp)) {
		this.pisteet+=30;
		return MAP.get(temp_list.get(indeksi(temp)).getArvo()).intValue()+",kolmoset";
	}
		
	if (onkoKaksiParia(temp)) {
		this.pisteet+=20;
		return MAP.get(temp_list.get(indeksi(temp)).getArvo()).intValue()+",kaksi paria";
	}
		
	if (onkoPari(temp)) {
		this.pisteet+=10;
		return MAP.get(temp_list.get(indeksi(temp)).getArvo()).intValue()+",parin";
	}
		
	
	temp_list=null;
	temp=null;
	return "0,0";
}

/** Palauttaa halutun kortin merkkijonona.
 * @param indeksi Mikä Kortti kädestä halutaan.
* @return Haluttu Kortti merkkijonona.
*/
public String getKortti(int indeksi) {
	return this.hand.get(indeksi).toString();
}

//**************
//Muutosmetodit*
//**************

/** Asettaa pelaajan nimen.
 * @param nimi Pelaajan uusi nimi.
 * 
 */
public void setNimi(String nimi) {
	this.nimi=nimi;
}
/** Luo pelaajalle uuden käden. Ottaa kortit pakasta.
 * @param jakaja Pelin Jakaja-olio, jolla on pakka.
 * AE: Pakassa riittävästi kortteja.
 */
public void setHand(Dealer jakaja) {
	this.hand.clear();
	for (int i=0; i<5; i++) {
		this.hand.add(jakaja.getPakka().poll()); }
	}
/** Vaihtaa kädessä olevan kortin toiseksi pakasta otettuun.
 * @param indeksi Kortin sijainti kädessä.
 * @param jakaja Jakaja, jolla pakka.
 */
public void vaihdaKortti (int indeksi, Dealer jakaja) {
	this.hand.set(indeksi, jakaja.getPakka().poll());
}
/** Asettaa pelaajan uudet pisteet.
 * @param pisteet Pistemäärä, joka lisätään olemassa olevaan.
 * 
 */
public void setPisteet(int pisteet) {
	this.pisteet+=pisteet;
}
//************
//Muut metodit*
//************

/** Luo HashMapin kortin arvon numeerista vertailua varten.
 * V=Kortti.Arvo, K=Integer.
 * @return HashMap,joka sisältää kaikki arvot 2-14 (ässä on 14, jotta olisi suurin) ja niiden vastineet Kortti.Arvo:ssa.
 * 
 */
private static HashMap<Arvo,Integer> luoMap() {
	HashMap<Arvo,Integer> uusi=new HashMap<Arvo,Integer>(19);
	uusi.put(Arvo.A, new Integer(14));
	uusi.put(Arvo.II, new Integer(2));
	uusi.put(Arvo.III, new Integer(3));
	uusi.put(Arvo.IV, new Integer(4));
	uusi.put(Arvo.V, new Integer(5));
	uusi.put(Arvo.VI, new Integer(6));
	uusi.put(Arvo.VII, new Integer(7));
	uusi.put(Arvo.VIII, new Integer(8));
	uusi.put(Arvo.IX, new Integer(9));
	uusi.put(Arvo.X, new Integer(10));
	uusi.put(Arvo.J, new Integer(11));
	uusi.put(Arvo.Q, new Integer(12));
	uusi.put(Arvo.K, new Integer(13));
	
	return uusi;
}
//******************************************
//Käden tarkistamiseen liittyvät apumetodit*
//******************************************
/** Tarkistaa onko kädessä paria.
 * @param temp int[] , jossa pelaajan käden korttien numeeriset arvot (järjestettynä).
 * @return True jos on. False jos ei ole.
 */
private boolean onkoPari (int[] temp) {
	for (int i=0; i<temp.length-1; i++)
		if (temp[i]==temp[i+1]) {
			return true;
			}
	return false;
}
/** Tarkistaa onko kädessä kolmoset.
 * @param temp int[] , jossa pelaajan käden korttien numeeriset arvot (järjestettynä).
 * @return True jos on. False jos ei ole.
 */
private boolean onkoKolmoset(int[] temp) {
	for (int i=0; i<temp.length-2; i++)
		if (temp[i]==temp[i+1] && temp[i]==temp[i+2]) {
			return true;
			}
	return false;
}
/** Tarkistaa onko kädessä neloset.
 * @param temp int[] , jossa pelaajan käden korttien numeeriset arvot (järjestettynä).
 * @return True jos on. False jos ei ole.
 */
private boolean onkoNeloset(int[] temp) {
	for (int i=0; i<=1; i++)
		if ((temp[i]==temp[i+1] && temp[i]==temp[i+2]) && temp[i]==temp[i+3]) {
			return true;
			}
	return false;
}
/** Tarkistaa onko kädessä suora.
 * @param temp int[] , jossa pelaajan käden korttien numeeriset arvot (järjestettynä).
 * @return True jos on. False jos ei ole.
 */
private boolean onkoSuora(int[] temp) {
	if (((temp[0]==2 && temp[1]==3) && (temp[2]==4 && temp[3]==5)) && temp[4]==14)
		return true;
	if (((temp[0]+1)==temp[1] && (temp[1]+1)==temp[2]) && ((temp[2]+1)==temp[3] && (temp[3]+1)==temp[4]))
		return true;
	return false;
}
/** Tarkistaa onko kädessä täyskäsi.
 * @param temp int[] , jossa pelaajan käden korttien numeeriset arvot (järjestettynä).
 * @return True jos on. False jos ei ole.
 */
private boolean onkoFullHouse(int[] temp) {
		if ( (temp[0]==temp[1]) && (temp[2]==temp[3] && temp[2]==temp[4]) )
			return true;
		if	( (temp[0]==temp[1] && temp[0]==temp[2]) && temp[3]==temp[4] )
			return true;
	return false;
}
/** Tarkistaa onko kädessä kaksi paria.
 * @param temp int[] , jossa pelaajan käden korttien numeeriset arvot (järjestettynä).
 * @return True jos on. False jos ei ole.
 */
private boolean onkoKaksiParia(int[] temp) {
	int i=0; 
	boolean pari1 = false, pari2 = false;
	while (i<temp.length-1) {
		if (temp[i]==temp[i+1]) {
			pari1=true;
			i+=2;
			break; }
		else i++; }
	while (i<temp.length-1) {
		if (temp[i]==temp[i+1]) {
			pari2=true;
			break; }
		else i++; }
	
	return pari1&&pari2;
}
/** Tarkistaa onko kädessä väri.
 * @param temp_list Pelaajan käsi järjestettynä.
 * @return True jos on. False jos ei ole.
 */
private boolean onkoFlush(List<Card> temp_list) {
	return (temp_list.get(0).getMaa().equals(temp_list.get(1).getMaa()) && temp_list.get(0).getMaa().equals(temp_list.get(2).getMaa())) && (temp_list.get(0).getMaa().equals(temp_list.get(3).getMaa()) && temp_list.get(0).getMaa().equals(temp_list.get(4).getMaa()));
}
/** Tarkistaa onko värisuora.
 * @param temp_list Pelaajan käsi järjestettynä.
 * @param temp int[] jossa korttien numeeriset arvot (järjestettynä).
 * @return True jos on, False jos ei ole.
 */
private boolean onkoStraightFlush(List<Card> temp_list, int[] temp) {
	return onkoSuora(temp) && onkoFlush(temp_list);
}
/** Tarkistaa onko Kuningasvärisuora.
 * @param temp_list Pelaajan käsi järjestettynä.
 * @param temp int[] jossa korttien numeeriset arvot (järjestettynä).
 * @return True jos on, False jos ei ole.
 */
private boolean onkoRoyalFlush(List<Card> temp_list, int[] temp) {
	return (((temp[4]==14 && temp[3]==13 && temp[2]==12) && (temp[1]==11 && temp[0]==10)) && onkoFlush(temp_list)) && temp_list.get(0).getMaa().equals(Maa.HERTTA);
}
/** Taulukon indeksi, joka edustaa parin numeerista arvoa.
 * Eli millä kortilla tuli pari. Esim kolmospari.
 * @param temp int[] , jossa pelaajan käden korttien numeeriset arvot (järjestettynä).
 * @return taulukon indeksi, joka edustaa parin numeerista arvoa.
 */
private int indeksi(int[] temp) {
	int paluu=-1, luku=temp[0];
	for (int i=0; i<temp.length; i++) {
		if (temp[i]==luku) {
			paluu=i;
			}
		luku=temp[i];
	}
	return paluu;
}

}
