package video;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import game.Partita;
import gamefield.Casella;
import gamefield.Stanza;
import score.SnakeScoreComparator;
import snake.Snake;

public class GraphicAdapter {
	
	public static List<CellRenderOptionWithPosition> getCellRenderOptionWithPosition(Partita game){
		ArrayList<CellRenderOptionWithPosition> frameToVisualize = new ArrayList<>();
		Stanza stanzaCorrente;
		Snake snakePlayer1 = game.getSerpentePlayer1();
		if(snakePlayer1.isVivo()){
			stanzaCorrente = snakePlayer1.getCasellaDiTesta().getStanza(); 
		} else if(snakePlayer1.getUltimaStanza() != null){
			stanzaCorrente = snakePlayer1.getUltimaStanza();
		} else {
			stanzaCorrente = game.getStanzaDiSpawn();
		}
		for (Casella cell : stanzaCorrente.getCaselle().values()) {
			CellRenderOption cellRenderOption = GraphicManager.getCellRenderOption(cell);
			CellRenderOptionWithPosition cellRenderOptionWithPosition = new CellRenderOptionWithPosition(cellRenderOption, cell.getPosizione());
			frameToVisualize.add(cellRenderOptionWithPosition);
		}
		return frameToVisualize;
	}
	
	public static List<LeaderBoardCellRenderOption> getLeaderBoardMap(Partita game){
		List<LeaderBoardCellRenderOption> leaderboard = new ArrayList<>();
		ArrayList<Snake> snakes = new ArrayList<>(game.getSerpenti().values());
		SnakeScoreComparator comparator = new SnakeScoreComparator();
		Collections.sort(snakes, comparator);
		boolean first = true;
		int i = 0;
		int maxValue = -1;
		for(Snake snake:snakes) {
			int score = snake.getTotalSnakeScore();
			CellRenderOption cellRenderOption = snake.getCellRenderOption();
			LeaderBoardCellRenderOption scoreElement = new LeaderBoardCellRenderOption(cellRenderOption, score, snake.isVivo(), first);
			if(score>0 && (first && snake.isVivo() || score>=maxValue && snake.isVivo())) {
				maxValue = score;
				first = false;
			}
			leaderboard.add(scoreElement);
			if(i>=5) break;
		}
		return leaderboard;
	}
	
	public static ScoreInfo getScoreInfo(Partita game) {
		ScoreInfo scoreInfo = new ScoreInfo();
		scoreInfo.setEnemiesNumber(game.getNumeroAvversari());
		scoreInfo.setPlayerKills(game.getSerpentePlayer1().getNumeroUccisioni());
		scoreInfo.setPlayerOldRecord(game.getVecchioRecord());
		scoreInfo.setCurrentScore(game.getSerpentePlayer1().getTotalSnakeScore());
		scoreInfo.setSurvivalTime((int)game.getSerpentePlayer1().getTempoSopravvissutoMillis()/1000);
		return scoreInfo;
	}

}
