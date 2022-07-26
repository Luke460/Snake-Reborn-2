package video;

import static constants.GeneralConstants.HP_BONUS_FOOD;
import static constants.GeneralConstants.HP_STANDARD_FOOD;
import static constants.GeneralConstants.HP_SUPER_FOOD;
import static constants.MapConstants.FLAT_CELL;
import static constants.MapConstants.RELIEF_CELL;

import java.awt.Color;

import gamefield.Cell;
import gamefield.Room;

public class GraphicManager {
	
	private static final Color STANDARD_FOOD_COLOR = Color.yellow;
	private static final Color BONUS_FOOD_COLOR = Color.pink;
	private static final Color SUPER_FOOD_COLOR = Color.cyan;


	public static CellRenderOption getCellRenderOption(Cell cell) {
		
		CellRenderOption cellRenderOption = new CellRenderOption(FLAT_CELL, Color.white);
		if(cell.isSolid() || cell.isEmpty()) {
			Room room = cell.getRoom();
			cellRenderOption = room.getMap().getCellRenderOptionMap().get(cell.getDefaultStatus());
		} else if (cell.isSnake()) {
			cellRenderOption = cell.getSnake().getCellRenderOption();
			if(cell.isSnakeHead()) {
				cellRenderOption = new CellRenderOption(RELIEF_CELL, cellRenderOption.getColor());
			}
		} else if (cell.isFood()) {
			if(cell.getFoodAmount() == HP_STANDARD_FOOD) {
				cellRenderOption = new CellRenderOption(FLAT_CELL, STANDARD_FOOD_COLOR);
			} else if (cell.getFoodAmount() == HP_BONUS_FOOD) {
				cellRenderOption = new CellRenderOption(FLAT_CELL, BONUS_FOOD_COLOR);
			} else if (cell.getFoodAmount() == HP_SUPER_FOOD) {
				cellRenderOption = new CellRenderOption(FLAT_CELL, SUPER_FOOD_COLOR);
			}
		}
		return cellRenderOption;
	}
	
	public static CellRenderOption getCellRenderOptionLowGraphicMode(Cell cell) {
		CellRenderOption cellRenderOption;
		if(!cell.isEmpty()) {
			cellRenderOption = getCellRenderOption(cell);
		} else {
			cellRenderOption = new CellRenderOption(FLAT_CELL, cell.getRoom().getMap().getBackgroundColor());
		}
		return getSemplifiedCellRenderOption(cellRenderOption);
	}
	
	public static CellRenderOption getSemplifiedCellRenderOption(CellRenderOption cellRenderOption) {
		return new CellRenderOption(FLAT_CELL, cellRenderOption.getColor());
	}
	
	public static Color getStandardFoodColor() {
		return STANDARD_FOOD_COLOR;
	}

}
