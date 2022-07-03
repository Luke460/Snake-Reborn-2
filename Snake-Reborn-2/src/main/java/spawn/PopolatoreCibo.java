package spawn;

import static constants.GeneralConstants.DIMENSIONE_STANZA_DEFAULT;
import static constants.GeneralConstants.SNAKE_HP_FOR_SUPER_FOOD;
import static constants.GeneralConstants.SNAKE_HP_FOR_BONUS_FOOD;
import static constants.GeneralConstants.QTY_BONUS_FOOD;
import static constants.GeneralConstants.QTY_STANDARD_FOOD;
import static constants.GeneralConstants.QTY_SUPER_FOOD;

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
		int foodQty;
		if(Utility.veroAl(5)) {
			foodQty = QTY_BONUS_FOOD;
		} else {
			foodQty = QTY_STANDARD_FOOD;
		}
		byte posX = (byte)(Math.random() * DIMENSIONE_STANZA_DEFAULT) ;
		byte posY = (byte)(Math.random() * DIMENSIONE_STANZA_DEFAULT) ;
		Position pos = new Position(posX, posY);
		Casella c = s.getCaselle().get(pos);
		// posiziono il cibo solo in caselle libere e con posizione pari
		if (c.isEmpty()){
			if(posizioneValidaPerCibo(pos)){ // 50% chance is false
				c.setFoodAmount(foodQty);
			}
		}
	}
	public static void rilasciaCiboNelleCaselleDelSerpente(List<Casella> caselle){
		boolean bonus_food = false;
		boolean super_food = false;
		int snakeHp = caselle.size();
		if(snakeHp >= SNAKE_HP_FOR_SUPER_FOOD) {
			super_food = true;
			ComparatoreCasellePerVita comparator = new ComparatoreCasellePerVita();
			Collections.sort(caselle, comparator);
		} else if (snakeHp >= SNAKE_HP_FOR_BONUS_FOOD) {
			bonus_food = true;
			ComparatoreCasellePerVita comparator = new ComparatoreCasellePerVita();
			Collections.sort(caselle, comparator);
		}	
		for(Casella c:caselle){
			c.freeCell();
			if(posizioneValidaPerCibo(c.getPosizione())){
				if(super_food) {
					c.setFoodAmount(QTY_SUPER_FOOD);
					super_food = false;
				} else if (bonus_food){
					c.setFoodAmount(QTY_BONUS_FOOD);
					bonus_food = false;
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
