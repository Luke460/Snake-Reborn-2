package server.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class Rank {

	String idRank;
	String rankName;
	Integer minElo;
	Integer maxElo;
	Integer firstPositionPoints;
	Integer secondPositionPoints;
	Integer thirdPositionPoints;
	Integer fourthPositionPoints;
	Integer fifthPositionPoints;
	
	public String getIdRank() {
		return idRank;
	}
	
	public void setIdRank(String idRank) {
		this.idRank = idRank;
	}
	
	public String getRankName() {
		return rankName;
	}
	
	public void setRankName(String rankName) {
		this.rankName = rankName;
	}
	
	public Integer getMinElo() {
		return minElo;
	}
	
	public void setMinElo(Integer minElo) {
		this.minElo = minElo;
	}
	
	public Integer getMaxElo() {
		return maxElo;
	}
	
	public void setMaxElo(Integer maxElo) {
		this.maxElo = maxElo;
	}
	
	public Integer getFirstPositionPoints() {
		return firstPositionPoints;
	}
	
	public void setFirstPositionPoints(Integer firstPositionPoints) {
		this.firstPositionPoints = firstPositionPoints;
	}
	
	public Integer getSecondPositionPoints() {
		return secondPositionPoints;
	}
	
	public void setSecondPositionPoints(Integer secondPositionPoints) {
		this.secondPositionPoints = secondPositionPoints;
	}
	
	public Integer getThirdPositionPoints() {
		return thirdPositionPoints;
	}
	
	public void setThirdPositionPoints(Integer thirdPositionPoints) {
		this.thirdPositionPoints = thirdPositionPoints;
	}
	
	public Integer getFourthPositionPoints() {
		return fourthPositionPoints;
	}
	
	public void setFourthPositionPoints(Integer fourthPositionPoints) {
		this.fourthPositionPoints = fourthPositionPoints;
	}
	
	public Integer getFifthPositionPoints() {
		return fifthPositionPoints;
	}
	
	public void setFifthPositionPoints(Integer fifthPositionPoints) {
		this.fifthPositionPoints = fifthPositionPoints;
	}
	
}
