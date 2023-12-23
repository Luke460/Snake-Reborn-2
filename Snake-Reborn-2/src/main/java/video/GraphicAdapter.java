package video;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import game.Game;
import gamefield.Cell;
import gamefield.Room;
import score.SnakeScoreComparator;
import score.SnakeEndGameScoreComparator;
import snake.Snake;

public class GraphicAdapter {
	
	public static List<CellRenderOptionWithPosition> getCellRenderOptionWithPosition(Game game){
		ArrayList<CellRenderOptionWithPosition> frameToVisualize = new ArrayList<>();
		Room currentRoom = game.getSnakePlayer1().getRoomToVisualize();
		for (Cell cell : currentRoom.getCellsMap().values()) {
			CellRenderOption cellRenderOption;
			if(game.isLowGraphicMode()) {
				cellRenderOption = GraphicManager.getCellRenderOptionLowGraphicMode(cell);
			} else {
				cellRenderOption = GraphicManager.getCellRenderOption(cell);
			}
			CellRenderOptionWithPosition cellRenderOptionWithPosition = new CellRenderOptionWithPosition(cellRenderOption, cell.getPosition());
			frameToVisualize.add(cellRenderOptionWithPosition);
		}
		return frameToVisualize;
	}
	
	public static List<LeaderBoardCellRenderOption> getLeaderBoardMap(Game game){
		List<LeaderBoardCellRenderOption> leaderboard = new ArrayList<>();
		ArrayList<Snake> snakes = new ArrayList<>(game.getSnakeMap().values());
		SnakeScoreComparator comparator = new SnakeScoreComparator();
		Collections.sort(snakes, comparator);
		boolean first = true;
		int i = 0;
		int maxValue = -1;
		for(Snake snake:snakes) {
			int score = snake.getTotalSnakeScorePreDeath();
			CellRenderOption cellRenderOption = snake.getCellRenderOption();
			if(game.isLowGraphicMode()) {
				cellRenderOption = GraphicManager.getSemplifiedCellRenderOption(cellRenderOption);
			}
			int kills = snake.getKillsNumber();
			int deaths = snake.getDeathsNumber();
			int foodTaken = snake.getTotalFoodTaken();
			LeaderBoardCellRenderOption scoreElement = new LeaderBoardCellRenderOption(cellRenderOption, score, snake.isAlive(), first, kills, deaths, foodTaken);
			if(score>0 && (first && snake.isAlive() || score>=maxValue && snake.isAlive())) {
				maxValue = score;
				first = false;
			}
			leaderboard.add(scoreElement);
			if(i>=4) break;
			i++;
		}
		return leaderboard;
	}
	
	public static List<LeaderBoardCellRenderOption> getLeaderBoardMapForEndGame(Game game){
		List<LeaderBoardCellRenderOption> leaderboard = new ArrayList<>();
		ArrayList<Snake> snakes = new ArrayList<>(game.getSnakeMap().values());
		SnakeEndGameScoreComparator comparator = new SnakeEndGameScoreComparator();
		Collections.sort(snakes, comparator);
		boolean first = true;
		int i = 0;
		int maxValue = -1;
		for(Snake snake:snakes) {
			int score = snake.getTotalSnakeScoreLifeStatusAdjusted();
			CellRenderOption cellRenderOption = snake.getCellRenderOption();
			if(game.isLowGraphicMode()) {
				cellRenderOption = GraphicManager.getSemplifiedCellRenderOption(cellRenderOption);
			}
			int kills = snake.getKillsNumber();
			int deaths = snake.getDeathsNumber();
			int foodTaken = snake.getTotalFoodTaken();
			LeaderBoardCellRenderOption scoreElement = new LeaderBoardCellRenderOption(cellRenderOption, score, snake.isAlive(), first, kills, deaths, foodTaken);
			if(score>0 && (first || score>=maxValue)) {
				maxValue = score;
				first = false;
			}
			leaderboard.add(scoreElement);
			if(i>=8) break;
			i++;
		}
		return leaderboard;
	}

}
