package popolatori;

import static supporto.Costanti.CARATTERE_CASELLA_CIBO;
import static supporto.Costanti.CARATTERE_CASELLA_VUOTA;
import static supporto.Costanti.DIMENSIONE_STANZA_DEFAULT;
import static supporto.Costanti.LUNGHEZZA_MINIMA_PER_TESTA_SERPENTE;

import java.util.Collections;
import java.util.List;

import supporto.Posizione;
import supporto.Utility;
import terrenoDiGioco.Casella;
import terrenoDiGioco.CasellaManager;
import terrenoDiGioco.Mappa;
import terrenoDiGioco.Stanza;

public class PopolatoreCibo {
	
	public static void aggiungiCiboNellaMappa(Mappa m) {
		for(Stanza s:m.getStanze()){
			aggiungiCiboInPosizioneCasuale(s);
		}
	}

	public static void aggiungiCiboInPosizioneCasuale(Stanza s) {
		int posX = (int)(Math.random() * DIMENSIONE_STANZA_DEFAULT) ;     // da 0 a N-1 compresi
		int posY = (int)(Math.random() * DIMENSIONE_STANZA_DEFAULT) ;
		Posizione pos = new Posizione(posX, posY);
		Casella c = s.getCaselle().get(pos);
		// posiziono il cibo solo in caselle libere e con posizione pari
		if (CasellaManager.isVuota(c)){
			if(posizioneValidaPerCibo(pos)){
				CasellaManager.libera(c);
				c.setStato(CARATTERE_CASELLA_CIBO);
			}
		}
	}
	public static void rilasciaCiboNelleCaselleDelSerpente(List<Casella> caselle){
		boolean inserisciTestaDiSerpente = false;
		if(caselle.size() > LUNGHEZZA_MINIMA_PER_TESTA_SERPENTE) {
			inserisciTestaDiSerpente = true;
			ComparatoreCasellePerVita comparator = new ComparatoreCasellePerVita();
			Collections.sort(caselle, comparator);
		}
		for(Casella c:caselle){
			if(posizioneValidaPerCibo(c.getPosizione())){
				c.setVita(0);
				CasellaManager.libera(c);
				c.setStato(CARATTERE_CASELLA_CIBO);
				if(inserisciTestaDiSerpente) {
					c.setTestaDiSerpente(true);
					inserisciTestaDiSerpente = false;
				}
			} else {
				c.setVita(0);
				CasellaManager.libera(c);
				c.setStato(CARATTERE_CASELLA_VUOTA);
			}
		}
	}

	public static boolean posizioneValidaPerCibo(Posizione p){
		// pre: casella vuota
		int x = p.getX();
		int y = p.getY();
		if(Utility.isPari(x)&&Utility.isPari(y)) {
			return true;
		} else if(!Utility.isPari(x)&&(!Utility.isPari(y))){
			return true;
		} else {
			return false;
		}
	}
}
