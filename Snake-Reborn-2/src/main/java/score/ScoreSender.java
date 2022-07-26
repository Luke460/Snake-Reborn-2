package score;

import javax.swing.JOptionPane;

import game.Game;
import server.model.Match;
import server.model.User;

public class ScoreSender extends Thread {

	private Game game;

	public ScoreSender(Game game){
		this.game = game;
	}

	public void run() {
		try {
			Match match = MatchFactory.buildMatch(game);
			User outputMessage = game.getClient().addMatch(match);
			System.out.println(outputMessage.toString());
		} catch (Exception e4){
			JOptionPane.showMessageDialog(null, 
					"Unable to connect to the server, please check your internet connection.");
			return;
		}

	}

}