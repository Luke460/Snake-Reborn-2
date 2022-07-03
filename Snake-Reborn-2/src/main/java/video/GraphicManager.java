package video;

import static constants.GeneralConstants.QTY_BONUS_FOOD;
import static constants.GeneralConstants.QTY_STANDARD_FOOD;
import static constants.GeneralConstants.QTY_SUPER_FOOD;
import static constants.MapConstants.DARKER_CELL;
import static constants.MapConstants.FLAT_CELL;

import java.awt.Color;

import gamefield.Casella;
import gamefield.Stanza;

public class GraphicManager {
	
	private static final Color STANDARD_FOOD_COLOR = Color.yellow;
	private static final Color BONUS_FOOD_COLOR = Color.pink;
	private static final Color SUPER_FOOD_COLOR = Color.cyan;


	public static CellRenderOption getCellRenderOption(Casella casella) {
		
		CellRenderOption cellRenderOption = new CellRenderOption(FLAT_CELL, Color.white);
		if(casella.isSolid() || casella.isEmpty()) {
			Stanza stanza = casella.getStanza();
			cellRenderOption = stanza.getMap().getCellRenderOptionMap().get(casella.getStatoOriginario());
		} else if (casella.isSnake()) {
			cellRenderOption = casella.getSnake().getCellRenderOption();
		} else if (casella.isFood()) {
			if(casella.getFoodAmount() == QTY_STANDARD_FOOD) {
				cellRenderOption = new CellRenderOption(DARKER_CELL, STANDARD_FOOD_COLOR);
			} else if (casella.getFoodAmount() == QTY_BONUS_FOOD) {
				cellRenderOption = new CellRenderOption(DARKER_CELL, BONUS_FOOD_COLOR);
			} else if (casella.getFoodAmount() == QTY_SUPER_FOOD) {
				cellRenderOption = new CellRenderOption(DARKER_CELL, SUPER_FOOD_COLOR);
			}
		}
		return cellRenderOption;
	}
	
	public static CellRenderOption getCellRenderOptionLowGraphicMode(Casella casella) {
		CellRenderOption cellRenderOption;
		if(!casella.isEmpty()) {
			cellRenderOption = getCellRenderOption(casella);
		} else {
			cellRenderOption = new CellRenderOption(FLAT_CELL, casella.getStanza().getMap().getBackgroundColor());
		}
		return new CellRenderOption(FLAT_CELL, cellRenderOption.getColor());
	}

}
