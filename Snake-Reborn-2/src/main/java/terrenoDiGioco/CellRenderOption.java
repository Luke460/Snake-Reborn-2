package terrenoDiGioco;

import java.awt.Color;

import static supporto.CostantiConfig.RELIEF_CELL;
import static supporto.CostantiConfig.FLAT_CELL;
import static supporto.CostantiConfig.HEAD_CELL;

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
