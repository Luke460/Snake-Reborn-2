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

public class FoodSpawnManager {
	
	public static void spawnFoodInTheMap(Mappa m) {
		for(Stanza s:m.getStanze()){
			spawnFoodInTheRoom(s);
		}
	}

	private static void spawnFoodInTheRoom(Stanza room) {
		int foodQty;
		//95% -> STANDARD
		// 4% -> BONUS
		// 1% -> SUPER
		//5% -> 80% BONUS, 20% SUPER
		if(Utility.truePercentage(95)) {
			foodQty = QTY_STANDARD_FOOD;
			// 5% left
		} else if(Utility.truePercentage(80)) {
			foodQty = QTY_BONUS_FOOD;
		} else {
			foodQty = QTY_SUPER_FOOD;
		}
		byte posX = (byte)(Math.random() * DIMENSIONE_STANZA_DEFAULT) ;
		byte posY = (byte)(Math.random() * DIMENSIONE_STANZA_DEFAULT) ;
		Position pos = new Position(posX, posY);
		Casella cell = room.getCaselle().get(pos);
		if (cell.isEmpty()){
			if(isPositionValidForFoodSpawn(pos)){ // 50% chance is false
				cell.setFoodAmount(foodQty);
			}
		}
	}
	public static void spawnFoodAfterSnakeDeath(List<Casella> cellsList){
		boolean bonusFood = false;
		boolean superFood = false;
		int snakeHp = cellsList.size();
		if(snakeHp >= SNAKE_HP_FOR_SUPER_FOOD) {
			superFood = true;
			CellHpComparator comparator = new CellHpComparator();
			Collections.sort(cellsList, comparator);
		} else if (snakeHp >= SNAKE_HP_FOR_BONUS_FOOD) {
			bonusFood = true;
			CellHpComparator comparator = new CellHpComparator();
			Collections.sort(cellsList, comparator);
		}	
		for(Casella c:cellsList){
			c.freeCell();
			if(isPositionValidForFoodSpawn(c.getPosizione())){
				if(superFood) {
					c.setFoodAmount(QTY_SUPER_FOOD);
					superFood = false;
				} else if (bonusFood){
					c.setFoodAmount(QTY_BONUS_FOOD);
					bonusFood = false;
				} else {
					c.setFoodAmount(QTY_STANDARD_FOOD);
				}
			}
		}
	}

	public static boolean isPositionValidForFoodSpawn(Position p){
		// pre: casella vuota
		int x = p.getX();
		int y = p.getY();
		if(Utility.isEven(x)&&Utility.isEven(y)) {
			return true;
		} else if(!Utility.isEven(x)&&(!Utility.isEven(y))){
			return true;
		} else {
			return false;
		}
	}
}
