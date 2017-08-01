package cardgames;

import java.util.*;

/** Varsinainen pokeripeli.
 * @author Juhani Vähä-Mäkilä, 2016 CC BY-NC-ND
 * http://creativecommons.org/licenses/by-nc-nd/4.0/
 * @version 0.5
 */
public class Pokeripeli {
	private static final Dealer j1=new Dealer();
	@SuppressWarnings("javadoc")
	public static void main(String[] args) {
		/* if (args.length!=1) {
			System.out.println("Käyttö:\njava -jar Pokeripeli cli.\ntai\njava -jar Pokeripeli gui.");
			return;
		} */
		//Dealer j1=new Dealer("Kalle");
	/*	if (args[0].equalsIgnoreCase("gui"))
			pokeriGUI(j1);
		else { 
			if (args[0].equalsIgnoreCase("cli")) {*/
		pokeriCLI();


		//return;}
	}
		//System.out.println("Virheellinen parametri.\nKäyttö:\njava Pokeripeli cli.\ntai\njava Pokeripeli gui."); 
	//}
	/**
	 * Tulevaisuuden tarpeita varten.
	 * @param j1
	 */
	private static void pokeriGUI() {
		// TODO Auto-generated method stub
		
	}
	/** CLI käyttöliittymä peliin.
	 * 
	 * @param j1 Dealer.
	 */
	@SuppressWarnings("resource")
	private static void pokeriCLI() {
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
		if (j1.getPakka().size()<5) {
			System.out.println("\nSekoitetaan uusi pakka!");
			j1.setPakka();
		} else {}
		p1.setHand(j1); 
		System.out.println("\n\nSait seuraavanlaisen käden.");
		//System.out.println(p1.getHand());
		try {
		for (int i=0; i<5; i++) {
		System.out.print("\t"+p1.getKortti(i)); }
		System.out.println("\n\t1\t\t2\t\t3\t\t4\t\t5"); 
		}
		catch (NullPointerException e) {
			System.out.println("\nTapahtui odottamaton virhe! Koitetaan palautua.");
			if (j1.getPakka().isEmpty()) {
				j1.setPakka();
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
			if (j1.getPakka().isEmpty()) {
				j1.setPakka();
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
	 * @param j1 Dealer.
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
			p1.vaihdaKortti(valinta-1, j1);
			vaihtoja++;
			break;
		case (2):
			p1.vaihdaKortti(valinta-1, j1);
			vaihtoja++;
			break;	
		case (3):
			p1.vaihdaKortti(valinta-1, j1);
			vaihtoja++;
			break;
		case (4):
			p1.vaihdaKortti(valinta-1, j1);
			vaihtoja++;
			break;
		case (5):
			p1.vaihdaKortti(valinta-1, j1);
			vaihtoja++;
			break;
		case (6):
			p1.setHand(j1);
			vaihtoja=5;
			break;
		default:
			vaihtoja=5;
		}
		}
	}
}