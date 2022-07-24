package score;

import javax.swing.JOptionPane;

import game.Partita;
import server.model.Match;
import server.model.User;

public class InviaPunteggio extends Thread {

	private Partita partita;

	public InviaPunteggio(Partita partita){
		this.partita = partita;
	}

	public void run() {
		try {
			Match match = MatchFactory.buildMatch(partita);
			User outputMessage = partita.getClient().addMatch(match);
			System.out.println(outputMessage.toString());
		} catch (Exception e4){
			JOptionPane.showMessageDialog(null, 
					"Non e' possibile contattare il server, controlla la tua connessione.");
			return;
		}

	}

}