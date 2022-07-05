package score;

import java.util.Date;

import game.Partita;
import server.model.Match;
import snake.Snake;

public class MatchFactory {
	
	public static Match buildMatch(Partita partita){
		Match m = new Match(); 
		Snake p1 = partita.getSerpentePlayer1();
		m.setDate(new Date());
		m.setKillsNumber(p1.getKillsNumber());
		m.setDeathsNumber(p1.getDeathsNumber());
		m.setBestKillingStreak(p1.getBestGameKillingStreak());
		m.setTotalFoodTaken(p1.getTotalFoodTaken());
		m.setFinalScore(p1.getTotalSnakeScore());
		m.setFinalLeaderboardPosition(partita.getPlayerPosition());
	    return m;
	}
	
}
