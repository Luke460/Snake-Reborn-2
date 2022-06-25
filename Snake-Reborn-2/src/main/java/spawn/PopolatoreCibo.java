package spawn;

import static support.Costanti.DIMENSIONE_STANZA_DEFAULT;
import static support.Costanti.LUNGHEZZA_MINIMA_PER_TESTA_SERPENTE;
import static support.Costanti.QTY_SPECIAL_FOOD;
import static support.Costanti.QTY_STANDARD_FOOD;

import java.util.Collections;
import java.util.List;

import gamefield.Casella;
import gamefield.Mappa;
import gamefield.Position;
import gamefield.Stanza;
import support.Utility;

public class PopolatoreCibo {
	
	public static void aggiungiCiboNellaMappa(Mappa m) {
		for(Stanza s:m.getStanze()){
			aggiungiCiboInPosizioneCasuale(s);
		}
	}

	public static void aggiungiCiboInPosizioneCasuale(Stanza s) {
		int posX = (int)(Math.random() * DIMENSIONE_STANZA_DEFAULT) ;
		int posY = (int)(Math.random() * DIMENSIONE_STANZA_DEFAULT) ;
		Position pos = new Position(posX, posY);
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

	public static boolean posizioneValidaPerCibo(Position p){
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