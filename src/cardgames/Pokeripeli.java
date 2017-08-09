/*
	Class for a single player "slots machine" type cli poker game
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

import java.util.*;

/** 
 * @author Juhani Vähä-Mäkilä, 2017
 * @version 0.5
 */
/*
 * {@literal Pisteet määräytyy seuraavasti: Pari 10, Kaksi paria 20, Kolmoset 30, Suora 40 Väri 50, Täyskäsi 60, Neloset 70,
	 * Värisuora 100, Kunkkuvärisuora 500.}
 */
public class Pokeripeli {
	/** CLI käyttöliittymä peliin.
	 */
	@SuppressWarnings("resource")
	static void pokeriCLI() {
		Scanner sc=new Scanner(System.in);
		System.out.println("Tervetuloa pelaamaan pokeria.");
		System.out.println("\nSinulle jaetaan viisi korttia ja tarkoitus on saada mahdollisimman hyvä käsi.");
		System.out.println("Mahdolliset kädet ovat (huonoimasta parhaimpaan):\npari, kaksi paria, kolmoset, suora, väri, täyskäsi, neloset, värisuora, kuningasvärisuora.");
		System.out.println("Mahdollisesta voitosta saa pisteitä. Koita kerätä mahdollisimman paljon pisteitä.\nAloitetaan!");
		System.out.print("\nAnna nimesi: ");
		Player p1=new Player(sc.nextLine());
		cliPeli(p1);
		while (true)  {
			char vastaus;
			System.out.print("\n\nKiitos pelaamisesta.\nHaluatko pelata uudestaan (k/e)? ");
			try {
				vastaus = sc.nextLine().toLowerCase().charAt(0);
			}
			catch (StringIndexOutOfBoundsException e) {
				System.out.println("Virhe! Anna k tai e kirjain.");
				continue;
				
			}
			if (vastaus=='k')
				cliPeli(p1);
			else
				break;
		/*	if (vastaus!='k')
				break; */
			
		}
		
	}
		
	/** Varsinainen pelilogiikka.
	 * 
	 * @param p1 Player.
	 */
	private static void cliPeli(Player p1) {
		if (Cardgames.j1.getPakka().size()<5) {
			System.out.println("\nSekoitetaan uusi pakka!");
			Cardgames.j1.setPakka();
		} else {}
		p1.setHand(Cardgames.j1); 
		System.out.println("\n\nSait seuraavanlaisen käden.");
		//System.out.println(p1.getHand());
		try {
		for (int i=0; i<5; i++) {
		System.out.print("\t"+p1.getKortti(i)); }
		System.out.println("\n\t1\t\t2\t\t3\t\t4\t\t5"); 
		}
		catch (NullPointerException e) {
			System.out.println("\nTapahtui odottamaton virhe! Koitetaan palautua.");
			if (Cardgames.j1.getPakka().isEmpty()) {
				Cardgames.j1.setPakka();
				return;
			}
		}
		kortinVaihto(p1);
		//System.out.println("\nUusi kätesi.\n"+p1.getHand());
		System.out.println("\nUusi kätesi:");
		try {
		for (int i=0; i<5; i++) {
			System.out.print(p1.getKortti(i)+" "); }
		}
		catch (NullPointerException f) {
			System.out.println("Tapahtui odottamaton virhe! Koitetaan palautua.");
			if (Cardgames.j1.getPakka().isEmpty()) {
				Cardgames.j1.setPakka();
			}
		}
		System.out.println("\n");
		kerroVoitto(p1.checkHand().split(","),p1);
		
	}
		

	/** Kertoo pelaajalle onko voittanut jotain.
	 * 
	 * @param voitto String[], jossa voiton kuvaus.
	 * @param p1 Player.
	 */
	private static void kerroVoitto(String[] voitto, Player p1) {
		int korkein_kortti=Integer.parseInt(voitto[0]);
	//	boolean jatketaanko=true;
	//	while (jatketaanko) {
			if (korkein_kortti==0) {
				System.out.println("Et voittanut mitään.");
				System.out.println("Suurin kortti oli: "+p1.suurin());
				System.out.println("Pisteesi on: "+p1.getPisteet());
			//	jatketaanko=false;
			//	continue;
			} else {
				System.out.println("Onneksi olkoon "+p1.getNimi()+"!\nSait "+voitto[1]+"\nPisteesi on: "+p1.getPisteet());
			}
		
	}

	/** Vaihtaa kädestä käyttäjän määrittelemät kortit.
	 * Ei anna vaihtaa samaa korttia useasti.
	 * @param p1 Player.
	 * @param Cardgames.j1 Dealer.
	 */
	@SuppressWarnings("resource")
	private static void kortinVaihto(Player p1) {
		Scanner in=new Scanner(System.in);
		int valinta=-1, vaihtoja=0, vaihdettu=-1;
		String input;
		System.out.println("Voit vaihtaa 0-5 korttia.");
		while (vaihtoja<5) {
			System.out.print("Minkä kortin haluat vaihtaa (1-5) (anna 6, jos vaihdat kaikki)\n(anna nolla, jos et vaihda)? ");
			input=in.nextLine();
		try {
			valinta=Integer.parseInt(input);
		}
		catch (NumberFormatException e) {
			//in.nextLine();
			System.out.println("\nVirheellinen syöte. Syötä kokonaisluku.");
			continue;
		}
		if (valinta<0 || valinta>6) {
			System.out.println("\nAnna luku väliltä 0-6.");
			continue;
		}
		if (valinta==vaihdettu) {
			System.out.println("\nEt voi vaihtaa samaa korttia uudestaan.\nYritä uudestaan.");
			continue;
		}
		vaihdettu=valinta;
		if (vaihtoja>0 && valinta==6) {
			System.out.println("\nVirhe! Olet jo vaihtanut yksittäisiä kortteja. Et voi vaihtaa koko kättä enään.");
			continue;
		}
		
		switch (valinta) {
		case (1):
			p1.vaihdaKortti(valinta-1, Cardgames.j1);
			vaihtoja++;
			break;
		case (2):
			p1.vaihdaKortti(valinta-1, Cardgames.j1);
			vaihtoja++;
			break;	
		case (3):
			p1.vaihdaKortti(valinta-1, Cardgames.j1);
			vaihtoja++;
			break;
		case (4):
			p1.vaihdaKortti(valinta-1, Cardgames.j1);
			vaihtoja++;
			break;
		case (5):
			p1.vaihdaKortti(valinta-1, Cardgames.j1);
			vaihtoja++;
			break;
		case (6):
			p1.setHand(Cardgames.j1);
			vaihtoja=5;
			break;
		default:
			vaihtoja=5;
		}
		}
	}
	
	//*********************************
	
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

	/** Palauttaa käden hain eli suurimman kortin.
	 * @return Käden suurimman kortin.
	 */
	public Card suurin() {
		Collections.sort(this.hand);
		return this.hand.get(this.hand.size()-1);
		
	/*	int suurin=1, indeksi=-1, luku;
		for (int i=0; i<this.hand.size();i++) {
			luku=Cardgames.MAP.get(this.hand.get(i).getArvo()).intValue();
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
			temp[i]=Cardgames.MAP.get(temp_list.get(i).getArvo()).intValue();
		}
		if (onkoRoyalFlush(temp_list,temp)) {
			this.points+=500;
			return "-1,kuningasvärisuoran";
		}
			
		if (onkoStraightFlush(temp_list,temp)) {
			this.points+=100;
			if (temp[4]==14 && temp[0]==2)
				return temp[3]+",värisuoran"; 
			else
				return temp[4]+",värisuoran";
		}
		if (onkoNeloset(temp)) {
			this.points+=70;
			return Cardgames.MAP.get(temp_list.get(indeksi(temp)).getArvo()).intValue()+",neloset";
		}
			
		if (onkoFullHouse(temp)) {
			this.points+=60;
			return Cardgames.MAP.get(temp_list.get(temp_list.size()-1).getArvo()).intValue()+",täyskäden";
		}
			
		if (onkoFlush(temp_list)) {
			this.points+=50;
			return temp[4]+",värin";
		}
			
		if (onkoSuora(temp)) {
			this.points+=40;
			if (temp[4]==14)
				return temp[3]+",suoran"; 
			else
				return temp[4]+",suoran";
		}
		if (onkoKolmoset(temp)) {
			this.points+=30;
			return Cardgames.MAP.get(temp_list.get(indeksi(temp)).getArvo()).intValue()+",kolmoset";
		}
			
		if (onkoKaksiParia(temp)) {
			this.points+=20;
			return Cardgames.MAP.get(temp_list.get(indeksi(temp)).getArvo()).intValue()+",kaksi paria";
		}
			
		if (onkoPari(temp)) {
			this.points+=10;
			return Cardgames.MAP.get(temp_list.get(indeksi(temp)).getArvo()).intValue()+",parin";
		}
			
		
		temp_list=null;
		temp=null;
		return "0,0";
	}

	
	//*********************************
}
