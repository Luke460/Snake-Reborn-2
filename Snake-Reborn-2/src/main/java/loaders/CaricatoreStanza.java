package loaders;

import gamefield.StanzaManager;

import static constants.MapConstants.SPAWN_ENABLED;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;

import gamefield.Casella;
import gamefield.Mappa;
import gamefield.Position;
import gamefield.Stanza;
import support.FileHandler;
import support.Utility;

public class CaricatoreStanza {
	
	public static Stanza caricaFile(String nomeFile, Mappa mappa) throws IOException {
		String testoStanza = FileHandler.readFile(nomeFile);
		String fileWithExt = Paths.get(nomeFile).getFileName().toString();
		String nomeStanza = fileWithExt.split("\\.")[0];
		Stanza stanza = new Stanza(nomeStanza);
		InfoMapFileContent stanzaInfo = LoaderSupporter.getInfoMapFileContent(testoStanza, nomeFile);
		
		byte rowIndex=0;
		for(String lineContent:stanzaInfo.getInfoLines()) {
			ArrayList<Character> lineCharList = new ArrayList<>();
			lineCharList.addAll(Utility.stringToArray(lineContent));
			byte characterIndex = 0;
			for(char statusCharacter:lineCharList) {
				Position position = new Position(characterIndex,rowIndex);
				boolean isSolid = mappa.getSolidCellStatusList().contains(statusCharacter);
				Casella casella = new Casella(stanza, position, statusCharacter, isSolid);
				stanza.getCaselle().put(position,casella);
				characterIndex++;
			}
			rowIndex++;
		}
		
		String spawnOption = stanzaInfo.getPrefixMap().get(SPAWN_ENABLED).get(0);
		if(spawnOption!=null && spawnOption.equalsIgnoreCase("true") || spawnOption.equalsIgnoreCase("1")) {
			if(StanzaManager.isActuallyReadyForSpawn(stanza)) {
				stanza.setSpawnEnabled(true);
			} else {
				throw new IllegalArgumentException("Invalid spawn setting for room '" + fileWithExt + "': room center is not free");
			}
		}	
		
		return stanza;	
	}


	
}
