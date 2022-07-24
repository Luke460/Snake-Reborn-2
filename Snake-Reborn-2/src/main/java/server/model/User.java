package server.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class User {

	private String idUser;
	private String username;
	private String email;
	private String rank;
	private Integer elo;
	private Boolean promo;
	
	public String getIdUser() {
		return idUser;
	}
	
	public void setIdUser(String idUser) {
		this.idUser = idUser;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getRank() {
		return rank;
	}
	
	public void setRank(String rank) {
		this.rank = rank;
	}
	
	public Integer getElo() {
		return elo;
	}
	
	public void setElo(Integer elo) {
		this.elo = elo;
	}
	
	public Boolean getPromo() {
		return promo;
	}
	
	public void setPromo(Boolean promo) {
		this.promo = promo;
	}
	
}