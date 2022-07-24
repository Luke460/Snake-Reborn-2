package server.model;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class Match {

	private String idMatch;
	private String idGame;
	private String version;
	private Date date;
	private int killsNumber;
	private int deathsNumber;
	private int bestKillingSpree;
	private int totalFoodTaken;
	private int finalScore;
	private int finalLeaderboardPosition;
	
	public Match() {}

	public String getIdMatch() {
		return idMatch;
	}

	public void setIdMatch(String idMatch) {
		this.idMatch = idMatch;
	}

	public String getIdGame() {
		return idGame;
	}

	public void setIdGame(String idGame) {
		this.idGame = idGame;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public int getKillsNumber() {
		return killsNumber;
	}

	public void setKillsNumber(int killsNumber) {
		this.killsNumber = killsNumber;
	}

	public int getDeathsNumber() {
		return deathsNumber;
	}

	public void setDeathsNumber(int deathsNumber) {
		this.deathsNumber = deathsNumber;
	}

	public int getBestKillingSpree() {
		return bestKillingSpree;
	}

	public void setBestKillingSpree(int bestKillingSpree) {
		this.bestKillingSpree = bestKillingSpree;
	}

	public int getTotalFoodTaken() {
		return totalFoodTaken;
	}

	public void setTotalFoodTaken(int totalFoodTaken) {
		this.totalFoodTaken = totalFoodTaken;
	}

	public int getFinalScore() {
		return finalScore;
	}

	public void setFinalScore(int finalScore) {
		this.finalScore = finalScore;
	}

	public int getFinalLeaderboardPosition() {
		return finalLeaderboardPosition;
	}

	public void setFinalLeaderboardPosition(int finalLeaderboardPosition) {
		this.finalLeaderboardPosition = finalLeaderboardPosition;
	}
	
}