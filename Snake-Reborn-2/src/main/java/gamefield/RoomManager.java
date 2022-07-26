package gamefield;

import static constants.GeneralConstants.ROOM_SIZE;

public class RoomManager {

	public static int getLinkedRoomsNumber(Room room) {
		int n = 0;
		for(Room otherRoom:room.getLinksMap().values()) {
			if(!otherRoom.equals(room)) {
				n++;
			}
		}
		return n;
	}
	
	public static boolean isActuallyReadyForSpawn(Room room) {
		
		byte centralDistance = (byte)(ROOM_SIZE/2);
		byte centralDistanceMinusOne = (byte)(centralDistance-1);
		
		Position pos1 = new Position(centralDistanceMinusOne, centralDistanceMinusOne);
		Position pos2 = new Position(centralDistance, centralDistanceMinusOne);
		Position pos3 = new Position(centralDistanceMinusOne, centralDistance);
		Position pos4 = new Position(centralDistance, centralDistance);
		
		Cell cell1 = room.getCellsMap().get(pos1);
		Cell cell2 = room.getCellsMap().get(pos2);
		Cell cell3 = room.getCellsMap().get(pos3);
		Cell cell4 = room.getCellsMap().get(pos4);
		
		if(cell1.isMortal()) return false;
		if(cell2.isMortal()) return false;
		if(cell3.isMortal()) return false;
		if(cell4.isMortal()) return false;
		
		return true;
	}
	
}
