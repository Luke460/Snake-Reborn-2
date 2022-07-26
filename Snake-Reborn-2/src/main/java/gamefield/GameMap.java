package gamefield;

import java.awt.Color;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import video.CellRenderOption;

public class GameMap {

	private String mapUniqueName;
	private HashSet<Room> rooms;
	private Set<Character> solidCellStatusList;
	private Set<Character> freeCellFloorStatusList;
	private Map<Character,CellRenderOption> cellRenderOptionMap;
	private Color backgroundColor;

	public GameMap(String mapUniqueName) throws IOException {
		this.mapUniqueName = mapUniqueName;
		this.rooms = new HashSet<Room>();
	}

	public String getMapUniqueName() {
		return mapUniqueName;
	}

	public void setMapUniqueName(String mapUniqueName) {
		this.mapUniqueName = mapUniqueName;
	}

	public HashSet<Room> getRooms() {
		return rooms;
	}

	public void setRooms(HashSet<Room> stanze) {
		this.rooms = stanze;
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

	public Color getBackgroundColor() {
		return backgroundColor;
	}

	public void setBackgroundColor(Color backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

	@Override
	public int hashCode() {
		return Objects.hash(mapUniqueName);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GameMap other = (GameMap) obj;
		return Objects.equals(mapUniqueName, other.mapUniqueName);
	}

}
