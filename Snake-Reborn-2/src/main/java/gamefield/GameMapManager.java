package gamefield;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

import snake.Snake;

public class GameMapManager {
	
	public static Room getRandomFreeRoomForSpawn(GameMap gameMap, HashMap<String, Snake> snakes, Room prevRoom) {
		HashSet<Room> availableRoomsSet = new HashSet<Room>();
		availableRoomsSet.addAll(gameMap.getRooms());
		
		for(Snake snake:snakes.values()) {
			if(snake.isAlive()) {
				for(Cell cell:snake.getCells()) {
					availableRoomsSet.remove(cell.getRoom());
				}
			}
		}
		boolean backupRoom = false;
		if(availableRoomsSet.size()>0) {
			ArrayList<Room> availableRoomsList = new ArrayList<Room>(availableRoomsSet);
			Collections.shuffle(availableRoomsList);
			for(Room room:availableRoomsList) {
				if(!room.equals(prevRoom) && room.isSpawnEnabled() && RoomManager.isActuallyReadyForSpawn(room)) {
					return room;
				} else if(prevRoom!=null && room.equals(prevRoom)) {
					backupRoom = true;
				}
			}
		}
		if(backupRoom && prevRoom.isSpawnEnabled() && RoomManager.isActuallyReadyForSpawn(prevRoom)) {
			return prevRoom;
		}
		System.out.println("out of rooms!");
		return null;
	}

}
