package spawn;

import static constants.GeneralConstants.ROOM_SIZE;
import static constants.GeneralConstants.MIN_SNAKE_HP_FOR_SUPER_FOOD;
import static constants.GeneralConstants.MIN_SNAKE_HP_FOR_BONUS_FOOD;
import static constants.GeneralConstants.HP_POISON_FOOD;
import static constants.GeneralConstants.HP_BONUS_FOOD;
import static constants.GeneralConstants.HP_STANDARD_FOOD;
import static constants.GeneralConstants.HP_SUPER_FOOD;

import java.util.Collections;
import java.util.List;

import gamefield.Cell;
import gamefield.GameMap;
import gamefield.Position;
import gamefield.Room;
import support.Utility;

public class FoodSpawnManager {
	
	public static void spawnFoodInTheMap(GameMap gameMap) {
		for(Room s:gameMap.getRooms()){
			spawnFoodInTheRoom(s);
		}
	}

	private static void spawnFoodInTheRoom(Room room) {
		// from 1 to 90 -> standard (90%)
		// from 91 to 95 -> poison (5%)
		// from 96 to 99 -> bonus (4%)
		// exactly 100 -> super (1%)
		int randomValue = Utility.getRandomPercentageValueFrom1To100();
		int foodQty = HP_STANDARD_FOOD;
		if(randomValue >= 91 && randomValue <= 95) {
			foodQty = HP_POISON_FOOD;
		} else if (randomValue >= 96 && randomValue <= 99) {
			foodQty = HP_BONUS_FOOD;
		} else if (randomValue == 100) {
			foodQty = HP_SUPER_FOOD;
		}
		byte posX = (byte)(Math.random() * ROOM_SIZE) ;
		byte posY = (byte)(Math.random() * ROOM_SIZE) ;
		Position pos = new Position(posX, posY);
		Cell cell = room.getCellsMap().get(pos);
		if (cell.isEmpty()){
			if(isPositionValidForFoodSpawn(pos)){ // 50% chance is false
				cell.setFoodAmount(foodQty);
			}
		}
	}
	
	public static void spawnFoodAfterSnakeDeath(List<Cell> cellsList){
		int snakeHp = cellsList.size();
		if(snakeHp>1) {
			boolean bonusFood = false;
			boolean superFood = false;
			if(snakeHp >= MIN_SNAKE_HP_FOR_SUPER_FOOD) {
				superFood = true;
				CellHpComparator comparator = new CellHpComparator();
				Collections.sort(cellsList, comparator);
			} else if (snakeHp >= MIN_SNAKE_HP_FOR_BONUS_FOOD) {
				bonusFood = true;
				CellHpComparator comparator = new CellHpComparator();
				Collections.sort(cellsList, comparator);
			}	
			for(Cell c:cellsList){
				c.freeCell();
				if(isPositionValidForFoodSpawn(c.getPosition())){
					if(superFood) {
						c.setFoodAmount(HP_SUPER_FOOD);
						superFood = false;
					} else if (bonusFood){
						c.setFoodAmount(HP_BONUS_FOOD);
						bonusFood = false;
					} else {
						c.setFoodAmount(HP_STANDARD_FOOD);
					}
				}
			}
		} else {
			for(Cell c:cellsList){
				c.freeCell();
			}
		}
	}

	public static boolean isPositionValidForFoodSpawn(Position p){
		// precondition: cell is free
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
