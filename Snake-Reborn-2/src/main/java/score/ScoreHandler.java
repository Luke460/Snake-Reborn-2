package score;

import game.Game;

public class ScoreHandler {

	public static double getScoreMultiplier(Game game) {
		double aiMultiplier = 0;
		if(game.getGameSpeed()==1) {
			aiMultiplier = 0.2;
		} else if(game.getGameSpeed()==2) {
			aiMultiplier = 0.5;
		} else if(game.getGameSpeed()==3) {
			aiMultiplier = 1;
		}
		return aiMultiplier;
	}

	public static void sendScore(Game game) {
		if(game.getGameSpeed()==3 && !game.isEndlessMode() && !game.isGuest()) {
			InviaPunteggio inviatore = new InviaPunteggio(game);
			inviatore.start();
		}
	}

}
