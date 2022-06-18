package game;

import static supporto.Costanti.*;

import java.awt.AWTException;
import java.io.IOException;

import javax.swing.JOptionPane;

import audio.GestoreSuoni;
import gestoreComandi.GestoreComandi;
import popolatori.PopolatoreCibo;
import popolatori.PopolatoreSerpenti;
import supporto.ConfigurationManager;
import video.Visualizzatore;
import visualizzatoreClient.VisualizzatoreClient;

public class Main {

	static Partita partita;
	private static Visualizzatore visualizzatore;

	public static void main(String[] args) throws IOException, InterruptedException {
		try {
			System.out.println(System.getProperty("file.separator"));
			System.out.println(new ConfigurationManager().toStringImpostazioni());
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
					client.leggiImpostazioni();
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

	public static void avviaIlGioco() throws AWTException, InterruptedException {
		partita.ImpostaPartita();
		// lancia un thread che legge i comandi, 
		// SuppressWarnings perchè il compilatore e' stupido
		GestoreComandi gestoreComandi = new GestoreComandi(partita,visualizzatore);
		partita.setGestoreComandi(gestoreComandi);
		//LettoreComandi.initControlliDaTastiera(visualizzatore);
		GestoreSuoni.inizzializzaSuoni();

		GestoreSuoni.playMusicaInLoop();
		cominciaIlGioco(partita);
		GestoreSuoni.silenziaMusica();
	}

	private static void cominciaIlGioco(Partita partita) throws AWTException, InterruptedException {
		PopolatoreSerpenti.creaPopoloIniziale(partita);
		PopolatoreCibo.aggiungiCiboRandom(partita.getMappa());
		visualizzatore.repaint();
		Thread.sleep(1000);
		GestoreSuoni.playSpawnSound();
		int contaCicli=0;

		long aspettaPer;

		long oraInizioAlgoritmo = System.currentTimeMillis(); 
		long oraProgrammataDiRipresa = oraInizioAlgoritmo;
		long oraCorrente;

		while(partita.isInGame()) {
			// sistema anti-lag
			// oraDiRipresa è relativa al ciclo precedente
			oraInizioAlgoritmo = oraProgrammataDiRipresa;
			oraProgrammataDiRipresa = oraInizioAlgoritmo + TEMPO_BASE;

			contaCicli++;

			if((contaCicli%TEMPO_RIPOPOLAMENTO_CIBO)==0){
				PopolatoreCibo.aggiungiCiboRandom(partita.getMappa());
			}

			if(partita.getFattorePopolazione()==1){
				if((contaCicli%(TEMPO_RIPOPOLAMENTO_SERPENTI_BASSO)==0) && partita.getNumeroAvversari()<=LIMITE_SERPENTI_BASSO){
					PopolatoreSerpenti.provaAdInserireUnSerpente(partita);
				}
			} else if(partita.getFattorePopolazione()==2){
				if((contaCicli%(TEMPO_RIPOPOLAMENTO_SERPENTI_ALTO)==0) && partita.getNumeroAvversari()<=LIMITE_SERPENTI_ALTO){
					PopolatoreSerpenti.provaAdInserireUnSerpente(partita);
				}
			}
			partita.eseguiTurni();
			visualizzatore.repaint();

			oraCorrente = System.currentTimeMillis();
			aspettaPer = oraProgrammataDiRipresa - oraCorrente;
			//tempoAlgoritmo = oraCorrente - oraInizioAlgoritmo;
			//oraPreScheduler = oraCorrente;
			if (aspettaPer>0) {
				// doppio repaint per migliorare la fluidità 
				// (altrimenti il kernel mi congela il processo tacci sua)
				if(!partita.isModPcLento()) {
					Thread.sleep(aspettaPer/2);
					visualizzatore.repaint();
					Thread.sleep(oraProgrammataDiRipresa - oraCorrente);
				} else {
					Thread.sleep(aspettaPer);
				}

			} else {
				System.out.println("lag detected!");
			}
		}
	}

}