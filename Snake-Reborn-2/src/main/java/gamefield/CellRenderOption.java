package gamefield;

import static support.CostantiConfig.FLAT_CELL;
import static support.CostantiConfig.HEAD_CELL;
import static support.CostantiConfig.RELIEF_CELL;

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
	
	public boolean isRelief() {
		return this.renderType.equals(RELIEF_CELL);
	}
	
	public boolean isFlat() {
		return this.renderType.equals(FLAT_CELL);
	}

	public boolean isHead() {
		return this.renderType.equals(HEAD_CELL);
	}
	
}
