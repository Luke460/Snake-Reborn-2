package score;

import game.Partita;

public class ScoreHandler {

	public static double getScoreMultiplier(Partita game) {
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

	public static void sendScore(Partita game) {
		if(game.getGameSpeed()==3 && !game.isEndlessMode() && !game.isOspite()) {
			InviaPunteggio inviatore = new InviaPunteggio(game);
			inviatore.start();
		}
	}

}
