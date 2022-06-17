package terrenoDiGioco;

import static supporto.Costanti.*;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;

import supporto.FileHandler;
import supporto.Posizione;
import supporto.Utility;

public class CaricatoreStanza {
	
	public static Stanza caricaFile(String nomeFile) throws IOException {
		String testoMappa = FileHandler.readFile(nomeFile);
		ArrayList<Character> listaCaratteri = new ArrayList<>();
		listaCaratteri.addAll(Utility.stringaToArray(testoMappa));
		
		String fileWithExt = Paths.get(nomeFile).getFileName().toString();
		String nomeStanza = fileWithExt.split("\\.")[0];
		
		Stanza stanza = new Stanza(nomeStanza);

		boolean rigaValida=false;
		int riga = 0;
		int colonna = 0;

		for(char c:listaCaratteri){
			if (c!=CARATTERE_FINE_FILE){ // finche' il file non e' finito...
				// controllo
				if(c==CARATTERE_INIZIO_RIGA){
					rigaValida=true;
				}
				if(c==CARATTERE_FINE_RIGA){
					rigaValida=false;
					riga++;
					colonna=0;
				}
				// fine controllo
				if(rigaValida && (c!=CARATTERE_INIZIO_RIGA && c!=CARATTERE_FINE_RIGA)){
					Posizione p = new Posizione(colonna,riga);
					stanza.getCaselle().put(p,new Casella(stanza,p, c));
					colonna++;
				}
			}
		}
		
		return stanza;
		
	}
	
}
