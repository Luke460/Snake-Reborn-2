package loaders;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;

import supporto.FileHandler;
import supporto.Posizione;
import supporto.Utility;
import terrenoDiGioco.Casella;
import terrenoDiGioco.Stanza;
import static supporto.CostantiConfig.SPAWN_ENABLED;

public class CaricatoreStanza {
	
	public static Stanza caricaFile(String nomeFile) throws IOException {
		String testoStanza = FileHandler.readFile(nomeFile);
		String fileWithExt = Paths.get(nomeFile).getFileName().toString();
		String nomeStanza = fileWithExt.split("\\.")[0];
		Stanza stanza = new Stanza(nomeStanza);
		InfoMapFileContent content = LoaderSupporter.getInfoMapFileContent(testoStanza, nomeFile);
		String prefix = content.getPrefixMap().get(SPAWN_ENABLED).get(0);
		if(prefix!=null) {
			stanza.setSpawnEnabled(prefix.equalsIgnoreCase("true") || prefix.equalsIgnoreCase("1"));
		}
		int rowIndex=0;
		for(String lineContent:content.getInfoLines()) {
			ArrayList<Character> lineCharList = new ArrayList<>();
			lineCharList.addAll(Utility.stringaToArray(lineContent));
			int characterIndex = 0;
			for(char c:lineCharList) {
				Posizione p = new Posizione(characterIndex,rowIndex);
				stanza.getCaselle().put(p,new Casella(stanza,p, c));
				characterIndex++;
			}
			rowIndex++;
		}
		return stanza;	
	}	
	
}
