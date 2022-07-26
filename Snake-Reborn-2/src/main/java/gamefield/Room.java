package gamefield;

import static constants.GeneralConstants.EAST;
import static constants.GeneralConstants.NORTH;
import static constants.GeneralConstants.WEST;
import static constants.GeneralConstants.SOUTH;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;



public class Room {

	private Map<Position,Cell> cellsMap;
	private Map<String,Room> linksMap;
	private String uniqueName;
	private boolean spawnEnabled;
	private GameMap map;

	public Room(String uniqueName){
		this.uniqueName = uniqueName;
		this.spawnEnabled = false;
		this.cellsMap = new HashMap<>();
		this.linksMap=new HashMap<>();
		// default links
		this.linksMap.put(NORTH, this);
		this.linksMap.put(EAST, this);
		this.linksMap.put(SOUTH, this);
		this.linksMap.put(WEST, this);
	}

	public Map<Position, Cell> getCellsMap() {
		return cellsMap;
	}

	public void setCellsMap(HashMap<Position, Cell> cellsMap) {
		this.cellsMap = cellsMap;
	}

	public Map<String, Room> getLinksMap() {
		return linksMap;
	}

	public void setLinksMap(HashMap<String, Room> linksMap) {
		this.linksMap = linksMap;
	}

	public String getUniqueName() {
		return uniqueName;
	}

	public void setUniqueName(String uniqueName) {
		this.uniqueName = uniqueName;
	}
	
	public boolean isSpawnEnabled() {
		return spawnEnabled;
	}

	public void setSpawnEnabled(boolean spawnEnabled) {
		this.spawnEnabled = spawnEnabled;
	}

	public GameMap getMap() {
		return map;
	}

	public void setMap(GameMap map) {
		this.map = map;
	}

	@Override
	public int hashCode() {
		return Objects.hash(uniqueName);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Room other = (Room) obj;
		return Objects.equals(uniqueName, other.uniqueName);
	}

	@Override
	public String toString() {
		return "Room [uniqueName=" + uniqueName + ", spawnEnabled=" + spawnEnabled + "]";
	}

}