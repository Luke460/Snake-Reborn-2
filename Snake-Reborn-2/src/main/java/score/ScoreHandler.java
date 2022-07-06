package score;

import javax.swing.JOptionPane;

import game.Partita;
import server.client.Client;

public class ScoreHandler {

	public static double getScoreMultiplier(Partita game) {
		double aiMultiplier = 0;
		if(game.getLivello()==1) {
			aiMultiplier = 0.2;
		} else if(game.getLivello()==2) {
			aiMultiplier = 0.5;
		} else if(game.getLivello()==3) {
			aiMultiplier = 1;
		}
		return aiMultiplier;
	}

	public static void sendScore(Partita game) {
		if(game.getLivello()==3 && !game.isEndlessMode() && !game.isOspite()) {
			int nuovoRecord = game.getSerpentePlayer1().getTotalSnakeScore();
			InviaPunteggio inviatore = new InviaPunteggio(game);
			inviatore.start();
			game.setVecchioRecord(nuovoRecord);
		}
	}

	public static int getRecord(Partita game) {
		try{
			Client c = game.getClient();
			return c.getRecord();
		} catch (Exception e4){
			JOptionPane.showMessageDialog(null, 
					"Non e' possibile contattare il server, controlla la tua connessione.");
			return 0;
		}
	}
}
