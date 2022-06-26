package video;

import static support.Costanti.QTY_SPECIAL_FOOD;
import static support.CostantiConfig.FLAT_CELL;
import static support.CostantiConfig.DARKER_CELL;
import static support.CostantiConfig.RELIEF_CELL;

import java.awt.Color;

import gamefield.Casella;
import gamefield.CellRenderOption;
import gamefield.Stanza;

public class GraphicManager {
	
	private static final Color STANDARD_FOOD_COLOR = Color.yellow;
	private static final Color POISON_FOOD_COLOR = Color.pink;

	public static CellRenderOption getCellRenderOption(Casella casella) {
		
		CellRenderOption cellRenderOption = new CellRenderOption(FLAT_CELL, Color.white);
		if(casella.isSolid() || casella.isEmpty()) {
			Stanza stanza = casella.getStanza();
			cellRenderOption = stanza.getMap().getCellRenderOptionMap().get(casella.getStatoOriginario());
		} else if (casella.isSnake()) {
			cellRenderOption = casella.getSnake().getCellRenderOption();
		} else if (casella.isFood()) {
			if(casella.getFoodAmount() > 0 && casella.getFoodAmount() < QTY_SPECIAL_FOOD) {
				cellRenderOption = new CellRenderOption(FLAT_CELL, STANDARD_FOOD_COLOR);
			} else if (casella.getFoodAmount() >= QTY_SPECIAL_FOOD) {
				cellRenderOption = new CellRenderOption(DARKER_CELL, STANDARD_FOOD_COLOR);
			} else { // poison
				cellRenderOption = new CellRenderOption(RELIEF_CELL, POISON_FOOD_COLOR);
			}
		}
		return cellRenderOption;
	}

}
