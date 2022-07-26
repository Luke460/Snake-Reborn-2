package loaders;

import static constants.GeneralConstants.EAST;
import static constants.GeneralConstants.MAPS_PATH;
import static constants.GeneralConstants.NORTH;
import static constants.GeneralConstants.WEST;
import static constants.GeneralConstants.ROOMS_FOLDER_NAME;
import static constants.GeneralConstants.SOUTH;
import static constants.MapConstants.BACKGROUND_COLOR;
import static constants.MapConstants.DARKER_CELL;
import static constants.MapConstants.DARKER_CELL_STRING;
import static constants.MapConstants.FLAT_CELL;
import static constants.MapConstants.FLAT_CELL_STRING;
import static constants.MapConstants.FREE_CELL_FLOOR;
import static constants.MapConstants.LIGHT_CELL;
import static constants.MapConstants.LIGHT_CELL_STRING;
import static constants.MapConstants.RELIEF_CELL;
import static constants.MapConstants.RELIEF_CELL_STRING;
import static constants.MapConstants.ROOM_PREFIX;
import static constants.MapConstants.SOLID_CELL;

import java.awt.Color;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import gamefield.GameMap;
import gamefield.Room;
import support.FileHandler;
import support.OSdetector;
import video.CellRenderOption;

public class GameMapLoader {
	
	private static final String CELL_RENDER_BEGIN = "CELL_RENDER(";
	private static final String CELL_RENDER_END = ")";
	private static final String FILE_TYPE = ".txt";

	public static GameMap loadFile(String selectedMapFolder) throws IOException {		
		HashMap<String, Room> rooms = new HashMap<>();	
		String mapFileName = selectedMapFolder + FILE_TYPE;
		String mapPath = MAPS_PATH + OSdetector.getPathSeparator() + selectedMapFolder + OSdetector.getPathSeparator() + mapFileName;
		String roomFolder = MAPS_PATH + OSdetector.getPathSeparator() + selectedMapFolder + OSdetector.getPathSeparator() + ROOMS_FOLDER_NAME;
		ArrayList<String> pathNames = FileHandler.getFileList(roomFolder, FILE_TYPE);
		
		String fileWithExt = Paths.get(mapPath).getFileName().toString();
		String mapName = fileWithExt.split("\\.")[0];
		GameMap gameMap = new GameMap(mapName);		
		String mapStructure = FileHandler.readFile(mapPath);
		InfoMapFileContent mapInfo = LoaderSupporter.getInfoMapFileContent(mapStructure, mapPath);
		
		loadSolidCellStatusList(gameMap, mapInfo);
		loadFreeCellStatusList(gameMap, mapInfo);	
		loadRenderOptionMap(gameMap, mapInfo);
		loadMapBackgroundColor(gameMap, mapInfo);
		
		for(String pathStanza:pathNames) {
			Room room = RoomLoader.loadFile(roomFolder + OSdetector.getPathSeparator() + pathStanza, gameMap);
			rooms.put(room.getUniqueName(), room);
		}
		
		String prefix = mapInfo.getPrefixMap().get(ROOM_PREFIX).get(0);
		for(String lineContent:mapInfo.getInfoLines()) {
			String[] lineInfo = lineContent.split(":");
			String roomName1 = prefix + lineInfo[0];
			String link = lineInfo[1];
			String roomName2 = prefix + lineInfo[2];
			Room room1 = rooms.get(roomName1);
			Room room2 = rooms.get(roomName2);
			if(room2!=null) {
				link = getFullLinkName(link);
				room1.getLinksMap().put(link, room2);
				room2.getLinksMap().put(getOppositeLink(link), room1);	
			}
		}
		
		gameMap.setRooms(new HashSet<Room>(rooms.values()));
		for(Room room:gameMap.getRooms()) {
			room.setMap(gameMap);
		}

		return gameMap;	
	}
	
	private static void loadMapBackgroundColor(GameMap gameMap, InfoMapFileContent content) {
		List<String> colors = content.getPrefixMap().get(BACKGROUND_COLOR);
		Color background = new Color(
				Integer.parseInt(colors.get(0)),
				Integer.parseInt(colors.get(1)),
				Integer.parseInt(colors.get(2))
				);
		gameMap.setBackgroundColor(background);
	}

	private static void loadSolidCellStatusList(GameMap gameMap, InfoMapFileContent colorInfoMap) {
		List<String> solidCellList = colorInfoMap.getPrefixMap().get(SOLID_CELL);
		Set<Character> solidCellListReduced = new HashSet<Character>();
		for(String s: solidCellList) {
			solidCellListReduced.add(s.charAt(0));
		}
		gameMap.setSolidCellStatusList(solidCellListReduced);
	}	
	
	private static void loadFreeCellStatusList(GameMap gameMap, InfoMapFileContent content) {
		List<String> freeCellList = content.getPrefixMap().get(FREE_CELL_FLOOR);
		Set<Character> freeCellListReduced = new HashSet<Character>();
		for(String s: freeCellList) {
			freeCellListReduced.add(s.charAt(0));
		}
		gameMap.setFreeCellFloorStatusList(freeCellListReduced);
	}	
	
	private static void loadRenderOptionMap(GameMap gameMap, InfoMapFileContent colorInfoMap) {
		Map<Character,CellRenderOption> cellRenderOptionMap = new HashMap<>();
		List<Character> allElements = new ArrayList<Character>();
		allElements.addAll(gameMap.getSolidCellStatusList());
		allElements.addAll(gameMap.getFreeCellFloorStatusList());
		for(Character c:allElements) {
			String actualInfoMapKey = CELL_RENDER_BEGIN + c + CELL_RENDER_END;
			List<String> renderParams = colorInfoMap.getPrefixMap().get(actualInfoMapKey);
			String stringRenderType = renderParams.get(0);
			byte renderType = getRenderTypeFromString(stringRenderType);
			int red = Integer.parseInt(renderParams.get(1));
			int green = Integer.parseInt(renderParams.get(2));
			int blue = Integer.parseInt(renderParams.get(3));
			Color color = new Color(red, green, blue);
			CellRenderOption cellRenderOption = new CellRenderOption(renderType, color);
			cellRenderOptionMap.put(c, cellRenderOption);
		}
		gameMap.setCellRenderOptionMap(cellRenderOptionMap);
	}
	
	private static byte getRenderTypeFromString(String s) {
		if(s.equals(FLAT_CELL_STRING)) {
			return FLAT_CELL;
		} else if (s.equals(RELIEF_CELL_STRING)) {
			return RELIEF_CELL;
		} else if (s.equals(DARKER_CELL_STRING)) {
			return DARKER_CELL;
		} else if (s.equals(LIGHT_CELL_STRING)) {
			return LIGHT_CELL;
		} else {
			throw new IllegalArgumentException("Unknown Cell Render Option " + s);
		}
	}

	private static String getFullLinkName(String link) {
		if(link.equalsIgnoreCase("N"))return NORTH;
		if(link.equalsIgnoreCase("E"))return EAST;
		if(link.equalsIgnoreCase("S"))return SOUTH;
		if(link.equalsIgnoreCase("W"))return WEST;
		return null;

	}
	
	private static String getOppositeLink(String link) {
		if(link.equals(NORTH))return SOUTH;
		if(link.equals(EAST))return WEST;
		if(link.equals(SOUTH))return NORTH;
		if(link.equals(WEST))return EAST;
		return null;

	}
	
}
