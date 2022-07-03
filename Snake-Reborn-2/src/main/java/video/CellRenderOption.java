package video;

import java.awt.Color;

public class CellRenderOption {
	
	private byte renderType;
	private Color color;
	
	public CellRenderOption(byte renderType, Color color) {
		this.renderType = renderType;
		this.color = color;
	}

	public byte getRenderType() {
		return renderType;
	}

	public void setRenderType(byte renderType) {
		this.renderType = renderType;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}
	
}
