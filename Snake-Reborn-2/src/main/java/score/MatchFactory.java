package score;

import java.util.Date;

import game.Partita;
import server.model.Match;
import server.model.MatchForGameVisualizer;
import snake.Snake;

public class MatchFactory {
	
	public static Match buildMatch(Partita game){
		Match m = new Match(); 
		Snake p1 = game.getSerpentePlayer1();
		build(game, m, p1);
	    return m;
	}

	public static MatchForGameVisualizer buildMatchForGameVisualizer(Partita game){
		MatchForGameVisualizer m = new MatchForGameVisualizer();
		Snake p1 = game.getSerpentePlayer1();
		build(game, m, p1);
		m.setSnakeLength(p1.getLength());
		m.setPlayerCellRenderOption(p1.getCellRenderOption());
		return m;
	}
	
	private static void build(Partita game, Match m, Snake p1) {
		m.setDate(new Date());
		m.setKillsNumber(p1.getKillsNumber());
		m.setDeathsNumber(p1.getDeathsNumber());
		m.setBestKillingSpree(p1.getBestGameKillingStreak());
		m.setTotalFoodTaken(p1.getTotalFoodTaken());
		m.setFinalScore(p1.getTotalSnakeScoreLifeStatusAdjusted());
		m.setFinalLeaderboardPosition(game.getPlayerPosition());
	}
	
}
