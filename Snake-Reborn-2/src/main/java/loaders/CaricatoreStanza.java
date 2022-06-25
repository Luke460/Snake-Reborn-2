package loaders;

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
import supporto.Posizione;
import supporto.Utility;
import terrenoDiGioco.Casella;
import terrenoDiGioco.CellRenderOption;
import terrenoDiGioco.Stanza;
import static supporto.CostantiConfig.*;

public class CaricatoreStanza {
	
	private static final String CELL_RENDER_BEGIN = "CELL_RENDER(";
	private static final String CELL_RENDER_END = ")";
	
	public static Stanza caricaFile(String nomeFile) throws IOException {
		String testoStanza = FileHandler.readFile(nomeFile);
		String fileWithExt = Paths.get(nomeFile).getFileName().toString();
		String nomeStanza = fileWithExt.split("\\.")[0];
		Stanza stanza = new Stanza(nomeStanza);
		InfoMapFileContent content = LoaderSupporter.getInfoMapFileContent(testoStanza, nomeFile);
		String spawnOption = content.getPrefixMap().get(SPAWN_ENABLED).get(0);
		if(spawnOption!=null) {
			stanza.setSpawnEnabled(spawnOption.equalsIgnoreCase("true") || spawnOption.equalsIgnoreCase("1"));
		}
		loadSolidCellStatusList(stanza, content);
		loadFreeCellStatusList(stanza, content);		
		loadRenderOptionMap(stanza, content);
		int rowIndex=0;
		for(String lineContent:content.getInfoLines()) {
			ArrayList<Character> lineCharList = new ArrayList<>();
			lineCharList.addAll(Utility.stringaToArray(lineContent));
			int characterIndex = 0;
			for(char statusCharacter:lineCharList) {
				Posizione position = new Posizione(characterIndex,rowIndex);
				boolean isSolid = stanza.getSolidCellStatusList().contains(statusCharacter);
				Casella casella = new Casella(stanza, position, statusCharacter, isSolid);
				stanza.getCaselle().put(position,casella);
				characterIndex++;
			}
			rowIndex++;
		}
		return stanza;	
	}

	private static void loadSolidCellStatusList(Stanza stanza, InfoMapFileContent content) {
		List<String> solidCellList = content.getPrefixMap().get(SOLID_CELL);
		Set<Character> solidCellListReduced = new HashSet<Character>();
		for(String s: solidCellList) {
			solidCellListReduced.add(s.charAt(0));
		}
		stanza.setSolidCellStatusList(solidCellListReduced);
	}	
	
	private static void loadFreeCellStatusList(Stanza stanza, InfoMapFileContent content) {
		List<String> freeCellList = content.getPrefixMap().get(FREE_CELL_FLOOR);
		Set<Character> freeCellListReduced = new HashSet<Character>();
		for(String s: freeCellList) {
			freeCellListReduced.add(s.charAt(0));
		}
		stanza.setFreeCellFloorStatusList(freeCellListReduced);
	}	
	
	private static void loadRenderOptionMap(Stanza stanza, InfoMapFileContent content) {
		Map<Character,CellRenderOption> cellRenderOptionMap = new HashMap<>();
		List<Character> allElements = new ArrayList<Character>();
		allElements.addAll(stanza.getSolidCellStatusList());
		allElements.addAll(stanza.getFreeCellFloorStatusList());
		for(Character c:allElements) {
			String actualInfoMapKey = CELL_RENDER_BEGIN + c + CELL_RENDER_END;
			List<String> renderParams = content.getPrefixMap().get(actualInfoMapKey);
			String renderType = renderParams.get(0);
			int red = Integer.parseInt(renderParams.get(1));
			int green = Integer.parseInt(renderParams.get(2));
			int blue = Integer.parseInt(renderParams.get(3));
			Color color = new Color(red, green, blue);
			CellRenderOption cellRenderOption = new CellRenderOption(renderType, color);
			cellRenderOptionMap.put(c, cellRenderOption);
		}
		stanza.setCellRenderOptionMap(cellRenderOptionMap);
	}
	
}
