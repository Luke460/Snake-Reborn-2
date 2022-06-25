package terrenoDiGioco;
import static supporto.Costanti.*;

import java.util.*;
import supporto.*;

public class Stanza {

	private Map<Posizione,Casella> caselle;
	private Map<String,Stanza> collegamenti;
	private String nomeUnivoco;
	private boolean spawnEnabled;
	
	private Set<Character> solidCellStatusList;
	private Set<Character> freeCellFloorStatusList;
	private Map<Character,CellRenderOption> cellRenderOptionMap;

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

	public Map<Posizione, Casella> getCaselle() {
		return caselle;
	}

	public void setCaselle(HashMap<Posizione, Casella> caselle) {
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
	
	public Map<Character, CellRenderOption> getCellRenderOptionMap() {
		return cellRenderOptionMap;
	}

	public void setCellRenderOptionMap(Map<Character, CellRenderOption> cellRenderOptionMap) {
		this.cellRenderOptionMap = cellRenderOptionMap;
	}

	public Set<Character> getSolidCellStatusList() {
		return solidCellStatusList;
	}

	public void setSolidCellStatusList(Set<Character> solidCellStatusList) {
		this.solidCellStatusList = solidCellStatusList;
	}

	public Set<Character> getFreeCellFloorStatusList() {
		return freeCellFloorStatusList;
	}

	public void setFreeCellFloorStatusList(Set<Character> freeCellFloorStatusList) {
		this.freeCellFloorStatusList = freeCellFloorStatusList;
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