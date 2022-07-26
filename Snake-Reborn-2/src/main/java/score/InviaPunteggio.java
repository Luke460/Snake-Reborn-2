package score;

import javax.swing.JOptionPane;

import game.Game;
import server.model.Match;
import server.model.User;

public class InviaPunteggio extends Thread {

	private Game game;

	public InviaPunteggio(Game game){
		this.game = game;
	}

	public void run() {
		try {
			Match match = MatchFactory.buildMatch(game);
			User outputMessage = game.getClient().addMatch(match);
			System.out.println(outputMessage.toString());
		} catch (Exception e4){
			JOptionPane.showMessageDialog(null, 
					"Non e' possibile contattare il server, controlla la tua connessione.");
			return;
		}

	}

}