package terrenoDiGioco;
import static supporto.Costanti.*;

import java.util.*;
import supporto.*;

public class Stanza {

	private HashMap<Posizione,Casella> caselle;
	private HashMap<String,Stanza> collegamenti;
	private String nomeUnivoco;
	private boolean spawnEnabled;

	public Stanza(String nomeUnivoco){
		this.nomeUnivoco = nomeUnivoco;
		this.spawnEnabled = false;
		this.caselle = new HashMap<>();
		this.collegamenti=new HashMap<>();
		// collegamenti di default
		this.collegamenti.put(NORD, this);
		this.collegamenti.put(EST, this);
		this.collegamenti.put(SUD, this);
		this.collegamenti.put(OVEST, this);
	}

	public HashMap<Posizione, Casella> getCaselle() {
		return caselle;
	}

	public void setCaselle(HashMap<Posizione, Casella> caselle) {
		this.caselle = caselle;
	}

	public HashMap<String, Stanza> getCollegamenti() {
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