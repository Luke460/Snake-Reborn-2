package video;

import terrenoDiGioco.Casella;
import terrenoDiGioco.CellRenderOption;
import terrenoDiGioco.Stanza;

import static supporto.CostantiConfig.FLAT_CELL;
import static supporto.CostantiConfig.HEAD_CELL;
import static supporto.CostantiConfig.RELIEF_CELL;

import java.awt.Color;

import static supporto.Costanti.QTY_SPECIAL_FOOD;

public class GraphicManager {
	
	private static final Color STANDARD_FOOD_COLOR = Color.yellow;
	private static final Color POISON_FOOD_COLOR = Color.pink;

	public static CellRenderOption getCellRenderOption(Casella casella) {
		
		CellRenderOption cellRenderOption = new CellRenderOption(FLAT_CELL, Color.white);
		if(casella.isSolid() || casella.isEmpty()) {
			Stanza stanza = casella.getStanza();
			cellRenderOption = stanza.getCellRenderOptionMap().get(casella.getStatoOriginario());
		} else if (casella.isSnake()) {
			cellRenderOption = casella.getSnake().getCellRenderOption();
			if(casella.isSnakeHead()) {
				cellRenderOption = new CellRenderOption(HEAD_CELL, casella.getSnake().getCellRenderOption().getColor());
			}
		} else if (casella.isFood()) {
			if(casella.getFoodAmount() > 0 && casella.getFoodAmount() < QTY_SPECIAL_FOOD) {
				cellRenderOption = new CellRenderOption(FLAT_CELL, STANDARD_FOOD_COLOR);
			} else if (casella.getFoodAmount() >= QTY_SPECIAL_FOOD) {
				cellRenderOption = new CellRenderOption(HEAD_CELL, STANDARD_FOOD_COLOR);
			} else { // poison
				cellRenderOption = new CellRenderOption(RELIEF_CELL, POISON_FOOD_COLOR);
			}
		}
		return cellRenderOption;
	}

}
