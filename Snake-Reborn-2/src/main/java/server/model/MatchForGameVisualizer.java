package server.model;

import video.CellRenderOption;

public class MatchForGameVisualizer extends Match {

	private int snakeLength;
	private CellRenderOption playerCellRenderOption;

	public MatchForGameVisualizer() {
		super();
	}

	public int getSnakeLength() {
		return this.snakeLength;
	}

	public void setSnakeLength(int snakeLength) {
		this.snakeLength = snakeLength;
	}

	public CellRenderOption getPlayerCellRenderOption() {
		return playerCellRenderOption;
	}

	public void setPlayerCellRenderOption(CellRenderOption playerCellRenderOption) {
		this.playerCellRenderOption = playerCellRenderOption;
	}
	
}