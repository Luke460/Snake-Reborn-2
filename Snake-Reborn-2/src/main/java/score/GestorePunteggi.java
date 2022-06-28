package score;

import javax.swing.JOptionPane;

import game.Partita;
import server.client.Client;

public class GestorePunteggi {

	private static Partita partita;

	public static void inizializza(Partita p){
		partita=p;
	}

	private static boolean punteggioValido() {
		if(partita.getLivello()==3){
			return true;
		} else {
			return false;
		}
	}	

	public static double getMoltiplicatorePunteggio() {
		double aiMultiplier = 0;
		if(partita.getLivello()==1) {
			aiMultiplier = 0.2;
		} else if(partita.getLivello()==2) {
			aiMultiplier = 0.5;
		} else if(partita.getLivello()==3) {
			aiMultiplier = 1;
		}
		return aiMultiplier;
	}


	
	public static void inviaPunteggio() {
		if(!punteggioValido()||partita.isOspite()) return;
		int nuovoRecord = partita.getSerpentePlayer1().getTotalSnakeScore();
		InviaPunteggio inviatore = new InviaPunteggio(partita);
		inviatore.start();
		partita.setVecchioRecord(nuovoRecord);
	}

	public static int getRecord() {
		try{
			Client c = partita.getClient();
			return c.getRecord();
		} catch (Exception e4){
			JOptionPane.showMessageDialog(null, 
					"Non e' possibile contattare il server, controlla la tua connessione.");
			return 0;
		}
	}
}
