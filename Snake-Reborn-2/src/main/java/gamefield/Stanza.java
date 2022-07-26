package gamefield;

import static constants.GeneralConstants.EAST;
import static constants.GeneralConstants.NORTH;
import static constants.GeneralConstants.WEST;
import static constants.GeneralConstants.SOUTH;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;



public class Stanza {

	private Map<Position,Casella> caselle;
	private Map<String,Stanza> collegamenti;
	private String nomeUnivoco;
	private boolean spawnEnabled;
	private Mappa map;

	public Stanza(String nomeUnivoco){
		this.nomeUnivoco = nomeUnivoco;
		this.spawnEnabled = false;
		this.caselle = new HashMap<>();
		this.collegamenti=new HashMap<>();
		// collegamenti di default
		this.collegamenti.put(NORTH, this);
		this.collegamenti.put(EAST, this);
		this.collegamenti.put(SOUTH, this);
		this.collegamenti.put(WEST, this);
	}

	public Map<Position, Casella> getCaselle() {
		return caselle;
	}

	public void setCaselle(HashMap<Position, Casella> caselle) {
		this.caselle = caselle;
	}

	public Map<String, Stanza> getCollegamenti() {
		return collegamenti;
	}

	public void setCollegamenti(HashMap<String, Stanza> collegamenti) {
		this.collegamenti = collegamenti;
	}

	public String getNome() {
		return nomeUnivoco;
	}

	public void setNome(String nomeUnivoco) {
		this.nomeUnivoco = nomeUnivoco;
	}
	
	public boolean isSpawnEnabled() {
		return spawnEnabled;
	}

	public void setSpawnEnabled(boolean spawnEnabled) {
		this.spawnEnabled = spawnEnabled;
	}

	public Mappa getMap() {
		return map;
	}

	public void setMap(Mappa map) {
		this.map = map;
	}

	@Override
	public int hashCode() {
		return Objects.hash(nomeUnivoco);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Stanza other = (Stanza) obj;
		return Objects.equals(nomeUnivoco, other.nomeUnivoco);
	}

}