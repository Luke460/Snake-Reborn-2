package gestorePunteggi;

import java.io.IOException;

import supporto.FileHandler;

public class ScriviPunteggio extends Thread {
	private String nomeFile;
	private String contenuto;
	public ScriviPunteggio(String nomeFile, String contenuto){
		this.nomeFile = nomeFile;
		this.contenuto = contenuto;
	}
	
	public void run() {
		try {
			FileHandler.writeFile(nomeFile, contenuto);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
