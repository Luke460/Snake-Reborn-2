package video;

import gamefield.Position;

public class CellRenderOptionWithPosition extends CellRenderOption {
	
	private Position position; 

	public CellRenderOptionWithPosition(CellRenderOption cellRenderOption, Position position) {
		super(cellRenderOption.getRenderType(), cellRenderOption.getColor());
		this.position = position;
	}

	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}

}
