package video;

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
	
}
