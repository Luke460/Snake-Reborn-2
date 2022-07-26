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

import gamefield.Mappa;
import gamefield.Stanza;
import support.FileHandler;
import support.OSdetector;
import video.CellRenderOption;

public class CaricatoreMappa {
	
	private static final String CELL_RENDER_BEGIN = "CELL_RENDER(";
	private static final String CELL_RENDER_END = ")";
	private static final String FILE_TYPE = ".txt";

	public static Mappa caricaFile(String selectedMapFolder) throws IOException {		
		HashMap<String, Stanza> stanze = new HashMap<>();	
		String mapFileName = selectedMapFolder + FILE_TYPE;
		String mapPath = MAPS_PATH + OSdetector.getPathSeparator() + selectedMapFolder + OSdetector.getPathSeparator() + mapFileName;
		String roomFolder = MAPS_PATH + OSdetector.getPathSeparator() + selectedMapFolder + OSdetector.getPathSeparator() + ROOMS_FOLDER_NAME;
		ArrayList<String> pathNames = FileHandler.getFileList(roomFolder, FILE_TYPE);
		
		String fileWithExt = Paths.get(mapPath).getFileName().toString();
		String nomeMappa = fileWithExt.split("\\.")[0];
		Mappa mappa = new Mappa(nomeMappa);		
		String strutturaMappa = FileHandler.readFile(mapPath);
		InfoMapFileContent mapInfo = LoaderSupporter.getInfoMapFileContent(strutturaMappa, mapPath);
		
		loadSolidCellStatusList(mappa, mapInfo);
		loadFreeCellStatusList(mappa, mapInfo);	
		loadRenderOptionMap(mappa, mapInfo);
		loadMapBackgroundColor(mappa, mapInfo);
		
		for(String pathStanza:pathNames) {
			Stanza stanza = CaricatoreStanza.caricaFile(roomFolder + OSdetector.getPathSeparator() + pathStanza, mappa);
			stanze.put(stanza.getNome(), stanza);
		}
		
		String prefix = mapInfo.getPrefixMap().get(ROOM_PREFIX).get(0);
		for(String lineContent:mapInfo.getInfoLines()) {
			String[] lineInfo = lineContent.split(":");
			String nomeStanza1 = prefix + lineInfo[0];
			String collegamento = lineInfo[1];
			String nomeStanza2 = prefix + lineInfo[2];
			Stanza stanza1 = stanze.get(nomeStanza1);
			Stanza stanza2 = stanze.get(nomeStanza2);
			if(stanza2!=null) {
				collegamento = getCollegamento(collegamento);
				stanza1.getCollegamenti().put(collegamento, stanza2);
				stanza2.getCollegamenti().put(getInversaCollegamento(collegamento), stanza1);	
			}
		}
		
		mappa.setStanze(new HashSet<Stanza>(stanze.values()));
		for(Stanza stanza:mappa.getStanze()) {
			stanza.setMap(mappa);
		}

		return mappa;	
	}
	
	private static void loadMapBackgroundColor(Mappa mappa, InfoMapFileContent content) {
		List<String> colors = content.getPrefixMap().get(BACKGROUND_COLOR);
		Color background = new Color(
				Integer.parseInt(colors.get(0)),
				Integer.parseInt(colors.get(1)),
				Integer.parseInt(colors.get(2))
				);
		mappa.setBackgroundColor(background);
	}

	private static void loadSolidCellStatusList(Mappa mappa, InfoMapFileContent colorInfoMap) {
		List<String> solidCellList = colorInfoMap.getPrefixMap().get(SOLID_CELL);
		Set<Character> solidCellListReduced = new HashSet<Character>();
		for(String s: solidCellList) {
			solidCellListReduced.add(s.charAt(0));
		}
		mappa.setSolidCellStatusList(solidCellListReduced);
	}	
	
	private static void loadFreeCellStatusList(Mappa mappa, InfoMapFileContent content) {
		List<String> freeCellList = content.getPrefixMap().get(FREE_CELL_FLOOR);
		Set<Character> freeCellListReduced = new HashSet<Character>();
		for(String s: freeCellList) {
			freeCellListReduced.add(s.charAt(0));
		}
		mappa.setFreeCellFloorStatusList(freeCellListReduced);
	}	
	
	private static void loadRenderOptionMap(Mappa mappa, InfoMapFileContent colorInfoMap) {
		Map<Character,CellRenderOption> cellRenderOptionMap = new HashMap<>();
		List<Character> allElements = new ArrayList<Character>();
		allElements.addAll(mappa.getSolidCellStatusList());
		allElements.addAll(mappa.getFreeCellFloorStatusList());
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
		mappa.setCellRenderOptionMap(cellRenderOptionMap);
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

	private static String getCollegamento(String collegamento) {
		if(collegamento.equalsIgnoreCase("N"))return NORTH;
		if(collegamento.equalsIgnoreCase("E"))return EAST;
		if(collegamento.equalsIgnoreCase("S"))return SOUTH;
		if(collegamento.equalsIgnoreCase("W"))return WEST;
		return null;

	}
	
	private static String getInversaCollegamento(String collegamento) {
		if(collegamento.equals(NORTH))return SOUTH;
		if(collegamento.equals(EAST))return WEST;
		if(collegamento.equals(SOUTH))return NORTH;
		if(collegamento.equals(WEST))return EAST;
		return null;

	}
	
}
