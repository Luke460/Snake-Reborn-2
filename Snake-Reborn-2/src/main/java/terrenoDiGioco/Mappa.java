package terrenoDiGioco;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class Mappa {

	private String nomeUnivoco;
	private HashSet<Stanza> stanze;
	private Set<Character> solidCellStatusList;
	private Set<Character> freeCellFloorStatusList;
	private Map<Character,CellRenderOption> cellRenderOptionMap;

	public Mappa(String nomeUnivoco) throws IOException {
		this.nomeUnivoco = nomeUnivoco;
		this.stanze = new HashSet<Stanza>();
	}

	public String getNomeUnivoco() {
		return nomeUnivoco;
	}

	public void setNomeUnivoco(String nomeUnivoco) {
		this.nomeUnivoco = nomeUnivoco;
	}

	public HashSet<Stanza> getStanze() {
		return stanze;
	}

	public void setStanze(HashSet<Stanza> stanze) {
		this.stanze = stanze;
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

	public Map<Character, CellRenderOption> getCellRenderOptionMap() {
		return cellRenderOptionMap;
	}

	public void setCellRenderOptionMap(Map<Character, CellRenderOption> cellRenderOptionMap) {
		this.cellRenderOptionMap = cellRenderOptionMap;
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
		Mappa other = (Mappa) obj;
		return Objects.equals(nomeUnivoco, other.nomeUnivoco);
	}

}
