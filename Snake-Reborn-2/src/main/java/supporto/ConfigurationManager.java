package supporto;

import static supporto.Costanti.CARATTERE_FINE_FILE;
import static supporto.Costanti.CARATTERE_FINE_RIGA;
import static supporto.Costanti.CARATTERE_INIZIO_RIGA;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class ConfigurationManager {
	private HashMap<String,String> settings;
	private static final String NOME_FILE = ("config"+OSdetector.getPathSeparator()+"userConfig.ini");
	
	public ConfigurationManager() throws IOException {
		this.readFile();
	}

	public void updateFile() throws IOException {
		LinkedList<String> listaChiavi = new LinkedList<String>();
		listaChiavi.addAll(this.settings.keySet());
		String fileContent = "";
		for(String tempChiave:listaChiavi) {
			fileContent += "<";
			fileContent += tempChiave;
			fileContent += ">";
			fileContent += "\n";
			fileContent += "  <";
			fileContent += this.settings.get(tempChiave);
			fileContent += ">";
			fileContent += "\n";
		}
		fileContent += "$";

		FileHandler.writeFile(NOME_FILE, fileContent);
	}

	//${file.separator}
	
	public void readFile() throws IOException {
		settings = new HashMap<String,String>();
		String text = FileHandler.readFile(NOME_FILE);
		String line = new String();
		String chiave = new String();
		String valore = new String();
		Boolean isChiave = false;
		ArrayList<Character> listaCaratteri = new ArrayList<>();
		listaCaratteri.addAll(Utility.stringaToArray(text));

		boolean rigaValida=false;

		for(char c:listaCaratteri){
			if (c!=CARATTERE_FINE_FILE){ // finche' il file non � finito...
				// controllo
				if(c==CARATTERE_INIZIO_RIGA){
					line = new String();
					rigaValida=true;
					if(isChiave==true) {
						isChiave = false;
					} else {
						isChiave = true;
					}
				}
				if(c==CARATTERE_FINE_RIGA){
					rigaValida=false;
					if(isChiave) {
						chiave = new String(line);
					} else {
						valore = new String(line);
						settings.put(chiave, valore);
					}
				}
				// fine controllo
				if(rigaValida && (c!=CARATTERE_INIZIO_RIGA && c!=CARATTERE_FINE_RIGA)){
					line+=c;
				}
			}
		}
	}

	public String leggiImpostazione(String chiave) throws IOException {
		this.readFile();
		return this.settings.get(chiave);
	}
	
	public void salvaImpostazione(String chiave, String valore) throws IOException {
		this.settings.put(chiave, valore);
		this.updateFile();
	}
	
	public String toStringImpostazioni() throws IOException {
		this.readFile();
		return this.settings.toString();
	}
}
