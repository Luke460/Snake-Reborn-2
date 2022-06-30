package video;

public class ScoreInfo {

	private int enemiesNumber;
	private int playerKills;
	private int playerOldRecord;
	private int currentScore;
	private int survivalTime;
	
	public int getEnemiesNumber() {
		return enemiesNumber;
	}
	public void setEnemiesNumber(int enemiesNumber) {
		this.enemiesNumber = enemiesNumber;
	}
	public int getPlayerKills() {
		return playerKills;
	}
	public void setPlayerKills(int playerKills) {
		this.playerKills = playerKills;
	}
	public int getPlayerOldRecord() {
		return playerOldRecord;
	}
	public void setPlayerOldRecord(int playerOldRecord) {
		this.playerOldRecord = playerOldRecord;
	}
	public int getCurrentScore() {
		return currentScore;
	}
	public void setCurrentScore(int currentScore) {
		this.currentScore = currentScore;
	}
	public int getSurvivalTime() {
		return survivalTime;
	}
	public void setSurvivalTime(int survivalTime) {
		this.survivalTime = survivalTime;
	}
	
}
