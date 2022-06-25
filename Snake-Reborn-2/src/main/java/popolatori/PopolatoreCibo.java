package popolatori;

import static supporto.Costanti.DIMENSIONE_STANZA_DEFAULT;
import static supporto.Costanti.LUNGHEZZA_MINIMA_PER_TESTA_SERPENTE;
import static supporto.Costanti.QTY_SPECIAL_FOOD;
import static supporto.Costanti.QTY_STANDARD_FOOD;

import java.util.Collections;
import java.util.List;

import supporto.Posizione;
import supporto.Utility;
import terrenoDiGioco.Casella;
import terrenoDiGioco.Mappa;
import terrenoDiGioco.Stanza;

public class PopolatoreCibo {
	
	public static void aggiungiCiboNellaMappa(Mappa m) {
		for(Stanza s:m.getStanze()){
			aggiungiCiboInPosizioneCasuale(s);
		}
	}

	public static void aggiungiCiboInPosizioneCasuale(Stanza s) {
		int posX = (int)(Math.random() * DIMENSIONE_STANZA_DEFAULT) ;
		int posY = (int)(Math.random() * DIMENSIONE_STANZA_DEFAULT) ;
		Posizione pos = new Posizione(posX, posY);
		Casella c = s.getCaselle().get(pos);
		// posiziono il cibo solo in caselle libere e con posizione pari
		if (c.isEmpty()){
			if(posizioneValidaPerCibo(pos)){ // 50% chance is false
				c.setFoodAmount(QTY_STANDARD_FOOD);
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
			c.freeCell();
			if(posizioneValidaPerCibo(c.getPosizione())){
				if(inserisciTestaDiSerpente) {
					c.setFoodAmount(QTY_SPECIAL_FOOD);
					inserisciTestaDiSerpente = false;
				} else {
					c.setFoodAmount(QTY_STANDARD_FOOD);
				}
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
