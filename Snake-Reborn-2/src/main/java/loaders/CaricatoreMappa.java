package loaders;

import static supporto.Costanti.EST;
import static supporto.Costanti.NORD;
import static supporto.Costanti.OVEST;
import static supporto.Costanti.SUD;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import supporto.FileHandler;
import supporto.OSdetector;
import terrenoDiGioco.Mappa;
import terrenoDiGioco.Stanza;
import terrenoDiGioco.StanzaManager;

public class CaricatoreMappa {
	
	final static String PATH_ROOMS = "stanze";
	final static String PATH_MAPS = "mappe";
	final static String ROOMS_FILE_TYPE = ".txt";

	public static Mappa caricaFile(String mapFileName) throws IOException {		
		HashMap<String, Stanza> stanze = new HashMap<>();	
		String mapPath = PATH_MAPS + OSdetector.getPathSeparator() + mapFileName;
		String roomFolder = PATH_ROOMS + OSdetector.getPathSeparator() + mapFileName.replaceFirst("[.][^.]+$", "");
		ArrayList<String> pathNames = FileHandler.getFileList(roomFolder, ROOMS_FILE_TYPE);
		
		for(String pathStanza:pathNames) {
			Stanza stanza = CaricatoreStanza.caricaFile(roomFolder + OSdetector.getPathSeparator() + pathStanza);
			stanze.put(stanza.getNome(), stanza);
		}
		
		String fileWithExt = Paths.get(mapPath).getFileName().toString();
		String nomeMappa = fileWithExt.split("\\.")[0];
		Mappa mappa = new Mappa(nomeMappa);		
		String strutturaMappa = FileHandler.readFile(mapPath);
		
		InfoMapFileContent content = LoaderSupporter.getInfoMapFileContent(strutturaMappa);
		String prefix = content.getPrefix();
		for(String lineContent:content.getInfoLines()) {
			String[] lineInfo = lineContent.split(":");
			String nomeStanza1 = prefix + lineInfo[0];
			String collegamento = lineInfo[1];
			String nomeStanza2 = prefix + lineInfo[2];
			Stanza stanza1 = stanze.get(nomeStanza1);
			Stanza stanza2 = stanze.get(nomeStanza2);
			collegamento = getCollegamento(collegamento);
			stanza1.getCollegamenti().put(collegamento, stanza2);
			StanzaManager.coloraPorta(stanza1, collegamento);
			stanza2.getCollegamenti().put(getInversaCollegamento(collegamento), stanza1);
			StanzaManager.coloraPorta(stanza2, getInversaCollegamento(collegamento));
					
		}
		mappa.setStanze(new HashSet<Stanza>(stanze.values()));
		return mappa;	
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
