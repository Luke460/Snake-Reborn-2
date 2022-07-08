package server.model;

import video.CellRenderOption;

public class MatchForGameVisualizer extends Match {

	private int SnakeLength;
	private CellRenderOption playerCellRenderOption;

	public MatchForGameVisualizer() {
		super();
	}

	public int getSnakeLength() {
		return SnakeLength;
	}

	public void setSnakeLength(int snakeLength) {
		SnakeLength = snakeLength;
	}

	public CellRenderOption getPlayerCellRenderOption() {
		return playerCellRenderOption;
	}

	public void setPlayerCellRenderOption(CellRenderOption playerCellRenderOption) {
		this.playerCellRenderOption = playerCellRenderOption;
	}
	
}