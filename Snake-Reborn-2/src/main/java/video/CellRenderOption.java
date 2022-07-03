package video;

import java.awt.Color;

public class CellRenderOption {
	
	private char renderType;
	private Color color;
	
	public CellRenderOption(char renderType, Color color) {
		this.renderType = renderType;
		this.color = color;
	}

	public char getRenderType() {
		return renderType;
	}

	public void setRenderType(char renderType) {
		this.renderType = renderType;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}
	
}
