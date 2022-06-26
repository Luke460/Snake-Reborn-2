package gamefield;

import static support.CostantiConfig.FLAT_CELL;
import static support.CostantiConfig.RELIEF_CELL;
import static support.CostantiConfig.DARKER_CELL;

import java.awt.Color;

public class CellRenderOption {
	
	private String renderType;
	private Color color;
	
	public CellRenderOption(String renderType, Color color) {
		this.renderType = renderType;
		this.color = color;
	}

	public String getRenderType() {
		return renderType;
	}

	public Color getColor() {
		return color;
	}
	
	public boolean isFlat() {
		return this.renderType.equals(FLAT_CELL);
	}
	
	public boolean isRelief() {
		return this.renderType.equals(RELIEF_CELL);
	}

	public boolean isDarker() {
		return this.renderType.equals(DARKER_CELL);
	}
	
}
