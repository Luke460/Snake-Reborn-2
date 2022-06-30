package video;

public class LeaderBoardCellRenderOption extends CellRenderOption{
	
	private boolean activeScore;
	private boolean firstScore;
	private int score;

	public LeaderBoardCellRenderOption(CellRenderOption cellRenderOption, int score, boolean activeScore, boolean firstScore) {
		super(cellRenderOption.getRenderType(), cellRenderOption.getColor());
		this.score = score;
		this.activeScore = activeScore;
		this.firstScore = firstScore;
	}

	public boolean isActiveScore() {
		return activeScore;
	}

	public void setActiveScore(boolean activeScore) {
		this.activeScore = activeScore;
	}

	public boolean isFirstScore() {
		return firstScore;
	}

	public void setFirstScore(boolean firstScore) {
		this.firstScore = firstScore;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

}
