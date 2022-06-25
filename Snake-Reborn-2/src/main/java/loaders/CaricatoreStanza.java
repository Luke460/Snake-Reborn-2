package loaders;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;

import supporto.FileHandler;
import supporto.Posizione;
import supporto.Utility;
import terrenoDiGioco.Casella;
import terrenoDiGioco.Mappa;
import terrenoDiGioco.Stanza;
import static supporto.CostantiConfig.*;

public class CaricatoreStanza {
	
	public static Stanza caricaFile(String nomeFile, Mappa mappa) throws IOException {
		String testoStanza = FileHandler.readFile(nomeFile);
		String fileWithExt = Paths.get(nomeFile).getFileName().toString();
		String nomeStanza = fileWithExt.split("\\.")[0];
		Stanza stanza = new Stanza(nomeStanza);
		InfoMapFileContent stanzaInfo = LoaderSupporter.getInfoMapFileContent(testoStanza, nomeFile);
		String spawnOption = stanzaInfo.getPrefixMap().get(SPAWN_ENABLED).get(0);
		if(spawnOption!=null) {
			stanza.setSpawnEnabled(spawnOption.equalsIgnoreCase("true") || spawnOption.equalsIgnoreCase("1"));
		}	
		
		int rowIndex=0;
		for(String lineContent:stanzaInfo.getInfoLines()) {
			ArrayList<Character> lineCharList = new ArrayList<>();
			lineCharList.addAll(Utility.stringaToArray(lineContent));
			int characterIndex = 0;
			for(char statusCharacter:lineCharList) {
				Posizione position = new Posizione(characterIndex,rowIndex);
				boolean isSolid = mappa.getSolidCellStatusList().contains(statusCharacter);
				Casella casella = new Casella(stanza, position, statusCharacter, isSolid);
				stanza.getCaselle().put(position,casella);
				characterIndex++;
			}
			rowIndex++;
		}
		return stanza;	
	}


	
}
