package score;

import java.util.Date;

import game.Game;
import server.model.Match;
import server.model.MatchForGameVisualizer;
import snake.Snake;

import static constants.GeneralConstants.GAME_VERSION;

public class MatchFactory {
	
	private final static String MATCH_PREFIX = "match-";
	private final static String GAME_PREFIX = "game-";
	
	public static Match buildMatch(Game game){
		Match m = new Match(); 
		Snake p1 = game.getSnakePlayer1();
		build(game, m, p1);
	    return m;
	}

	public static MatchForGameVisualizer buildMatchForGameVisualizer(Game game){
		MatchForGameVisualizer m = new MatchForGameVisualizer();
		Snake p1 = game.getSnakePlayer1();
		build(game, m, p1);
		m.setSnakeLength(p1.getLength());
		m.setPlayerCellRenderOption(p1.getCellRenderOption());
		return m;
	}
	
	private static void build(Game game, Match m, Snake p1) {
		long milliseconds = System.currentTimeMillis();
		m.setIdMatch(MATCH_PREFIX + milliseconds);
		m.setIdGame(GAME_PREFIX + milliseconds);
		m.setVersion(GAME_VERSION);
		m.setDate(new Date());
		m.setKillsNumber(p1.getKillsNumber());
		m.setDeathsNumber(p1.getDeathsNumber());
		m.setBestKillingSpree(p1.getBestGameKillingStreak());
		m.setTotalFoodTaken(p1.getTotalFoodTaken());
		m.setFinalScore(p1.getTotalSnakeScoreLifeStatusAdjusted());
		m.setFinalLeaderboardPosition(game.getPlayerPosition());
	}
	
}
