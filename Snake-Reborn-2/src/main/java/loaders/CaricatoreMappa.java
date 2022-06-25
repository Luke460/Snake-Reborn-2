package loaders;

import static supporto.Costanti.*;
import static supporto.CostantiConfig.*;

import java.awt.Color;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import supporto.FileHandler;
import supporto.OSdetector;
import terrenoDiGioco.CellRenderOption;
import terrenoDiGioco.Mappa;
import terrenoDiGioco.Stanza;

public class CaricatoreMappa {
	
	private static final String CELL_RENDER_BEGIN = "CELL_RENDER(";
	private static final String CELL_RENDER_END = ")";

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
			collegamento = getCollegamento(collegamento);
			stanza1.getCollegamenti().put(collegamento, stanza2);
			stanza2.getCollegamenti().put(getInversaCollegamento(collegamento), stanza1);		
		}
		
		mappa.setStanze(new HashSet<Stanza>(stanze.values()));
		for(Stanza stanza:mappa.getStanze()) {
			stanza.setMap(mappa);
		}

		return mappa;	
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
			String renderType = renderParams.get(0);
			int red = Integer.parseInt(renderParams.get(1));
			int green = Integer.parseInt(renderParams.get(2));
			int blue = Integer.parseInt(renderParams.get(3));
			Color color = new Color(red, green, blue);
			CellRenderOption cellRenderOption = new CellRenderOption(renderType, color);
			cellRenderOptionMap.put(c, cellRenderOption);
		}
		mappa.setCellRenderOptionMap(cellRenderOptionMap);
	}
	
	private static String getCollegamento(String collegamento) {
		if(collegamento.equalsIgnoreCase("N"))return NORD;
		if(collegamento.equalsIgnoreCase("E"))return EST;
		if(collegamento.equalsIgnoreCase("S"))return SUD;
		if(collegamento.equalsIgnoreCase("O"))return OVEST;
		return null;

	}
	
	private static String getInversaCollegamento(String collegamento) {
		if(collegamento.equals(NORD))return SUD;
		if(collegamento.equals(EST))return OVEST;
		if(collegamento.equals(SUD))return NORD;
		if(collegamento.equals(OVEST))return EST;
		return null;

	}
	
}
