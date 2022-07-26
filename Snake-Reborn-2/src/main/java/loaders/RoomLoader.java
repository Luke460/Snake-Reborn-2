package loaders;

import gamefield.RoomManager;

import static constants.MapConstants.SPAWN_ENABLED;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;

import gamefield.Cell;
import gamefield.GameMap;
import gamefield.Position;
import gamefield.Room;
import support.FileHandler;
import support.Utility;

public class RoomLoader {
	
	public static Room loadFile(String fileName, GameMap gameMap) throws IOException {
		String fileContent = FileHandler.readFile(fileName);
		String fileWithExt = Paths.get(fileName).getFileName().toString();
		String roomName = fileWithExt.split("\\.")[0];
		Room room = new Room(roomName);
		InfoMapFileContent roomInfo = LoaderSupporter.getInfoMapFileContent(fileContent, fileName);
		
		byte rowIndex=0;
		for(String lineContent:roomInfo.getInfoLines()) {
			ArrayList<Character> lineCharList = new ArrayList<>();
			lineCharList.addAll(Utility.stringToArray(lineContent));
			byte characterIndex = 0;
			for(char statusCharacter:lineCharList) {
				Position position = new Position(characterIndex,rowIndex);
				boolean isSolid = gameMap.getSolidCellStatusList().contains(statusCharacter);
				Cell cell = new Cell(room, position, statusCharacter, isSolid);
				room.getCellsMap().put(position,cell);
				characterIndex++;
			}
			rowIndex++;
		}
		
		String spawnOption = roomInfo.getPrefixMap().get(SPAWN_ENABLED).get(0);
		if(spawnOption!=null && spawnOption.equalsIgnoreCase("true") || spawnOption.equalsIgnoreCase("1")) {
			if(RoomManager.isActuallyReadyForSpawn(room)) {
				room.setSpawnEnabled(true);
			} else {
				throw new IllegalArgumentException("Invalid spawn setting for room '" + fileWithExt + "': room center is not free");
			}
		}	
		return room;	
	}


	
}
