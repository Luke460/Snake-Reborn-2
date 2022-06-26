package game;

import static support.Costanti.TEMPO_BASE_STANDARD;
import static support.Costanti.TEMPO_BASE_HARDCORE;
import static support.Costanti.TEMPO_RIPOPOLAMENTO_CIBO;
import static support.Costanti.TEMPO_RIPOPOLAMENTO_SERPENTI_BOT;

import java.awt.AWTException;
import java.io.IOException;

import javax.swing.JOptionPane;

import audio.GestoreSuoni;
import client.VisualizzatoreClient;
import commands.GestoreComandi;
import spawn.PopolatoreCibo;
import spawn.PopolatoreSerpenti;
import video.Visualizzatore;

public class Main {

	static Partita partita;
	private static Visualizzatore visualizzatore;

	public static void main(String[] args) throws IOException, InterruptedException {
		try {
			VisualizzatoreClient client = new VisualizzatoreClient();
			visualizzatore = new Visualizzatore();
			while(true) {
				partita = new Partita();
				client.rileggi(partita);
				visualizzatore.setPartita(partita);
				while(!client.isPremuto()){ // viene "sbloccato dal Listener" (busy waiting)
					Thread.sleep(250);
				}
				try {
					client.leggiImpostazioniDaUI();
					partita.ImpostaPartita();
					visualizzatore.getFinestra().setVisible(true);
					avviaIlGioco();
				} catch (AWTException e) {
					e.printStackTrace();
				}
				visualizzatore.getFinestra().setVisible(false);
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Impossibile avviare Snake Reborn 2:\nError details: " + e.getMessage());
			e.printStackTrace();
		}
	}

	public static void avviaIlGioco() throws AWTException, InterruptedException, IOException {
		// lancia un thread che legge i comandi, 
		// SuppressWarnings perchè il compilatore e' stupido
		GestoreComandi gestoreComandi = new GestoreComandi(partita,visualizzatore);
		partita.setGestoreComandi(gestoreComandi);
		GestoreSuoni.playMusicaInLoop();
		cominciaIlGioco(partita);
		GestoreSuoni.silenziaMusica();
	}

	private static void cominciaIlGioco(Partita partita) throws AWTException, InterruptedException {
		PopolatoreCibo.aggiungiCiboNellaMappa(partita.getMappa());
		visualizzatore.repaint();
		Thread.sleep(1000);
		GestoreSuoni.playSpawnSound();
		int contaCicli=0;

		long aspettaPer;

		long oraInizioAlgoritmo = System.currentTimeMillis(); 
		long oraProgrammataDiRipresa = oraInizioAlgoritmo;
		long oraCorrente;	
		long tickTime = partita.isHardcoreMode() ? TEMPO_BASE_HARDCORE : TEMPO_BASE_STANDARD;
		int fps = (int)(1000/tickTime);
		int foodRespawn = (int)(TEMPO_RIPOPOLAMENTO_CIBO*fps);
		int snakeRespawn = (int)(TEMPO_RIPOPOLAMENTO_SERPENTI_BOT*fps);

		while(partita.isInGame()) {
			oraInizioAlgoritmo = oraProgrammataDiRipresa;
			oraProgrammataDiRipresa = oraInizioAlgoritmo + tickTime;
			contaCicli++;

			partita.eseguiTurni();
			
			spawnJob(partita, contaCicli, foodRespawn, snakeRespawn);
			
			visualizzatore.repaint();
			
			if(contaCicli%fps==0) {
				long latency = System.currentTimeMillis()-oraInizioAlgoritmo;
				System.out.println("latency: " + latency +" ms");
			}

			oraCorrente = System.currentTimeMillis();
			aspettaPer = oraProgrammataDiRipresa - oraCorrente;
			if (aspettaPer>0) {
				Thread.sleep(aspettaPer);
			} else {
				System.out.println("lag detected!");
			}
		}
	}

	private static void spawnJob(Partita partita, int contaCicli, int foodRespawn, int snakeRespawn) {
		if((contaCicli%foodRespawn)==0){
			System.out.println("adding food");
			PopolatoreCibo.aggiungiCiboNellaMappa(partita.getMappa());
		}

		if((contaCicli%snakeRespawn==0)){
			System.out.println("ai snake respawn");
			PopolatoreSerpenti.provaAResuscitareUnSerpenteBot(partita);
		}
	}

}