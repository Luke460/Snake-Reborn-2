package video;

public class LeaderBoardCellRenderOption extends CellRenderOption{
	
	private boolean playerAlive;
	private boolean firstScore;
	private int score;
	private int killsNuber;
	private int deathsNumber;

	public LeaderBoardCellRenderOption(CellRenderOption cellRenderOption, int score, boolean activeScore, boolean firstScore, int kills, int deaths) {
		super(cellRenderOption.getRenderType(), cellRenderOption.getColor());
		this.score = score;
		this.playerAlive = activeScore;
		this.firstScore = firstScore;
		this.killsNuber = kills;
		this.deathsNumber = deaths;
	}

	public boolean isPlayerAlive() {
		return playerAlive;
	}

	public void setPlayerAlive(boolean playerAlive) {
		this.playerAlive = playerAlive;
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

	public int getKillsNuber() {
		return killsNuber;
	}

	public void setKillsNuber(int killsNuber) {
		this.killsNuber = killsNuber;
	}

	public int getDeathsNumber() {
		return deathsNumber;
	}

	public void setDeathsNumber(int deathsNumber) {
		this.deathsNumber = deathsNumber;
	}

}
