package score;

import java.util.Comparator;

import snake.Snake;

public class SnakeEndGameScoreComparator implements Comparator<Snake> {

	@Override
	public int compare(Snake first, Snake second) {
		if(second.getTotalSnakeScoreLifeStatusAdjusted() != first.getTotalSnakeScoreLifeStatusAdjusted()) {
		return Integer.compare(second.getTotalSnakeScoreLifeStatusAdjusted(), first.getTotalSnakeScoreLifeStatusAdjusted());
		} else if (second.getKillsNumber()!=first.getKillsNumber()) {
			return Integer.compare(second.getKillsNumber(), first.getKillsNumber());
		} else if (second.getDeathsNumber()!=first.getDeathsNumber()){
			return Integer.compare(first.getDeathsNumber(), second.getDeathsNumber());
		} else {
			return Integer.compare(second.getTotalFoodTaken(), first.getTotalFoodTaken());
		}
	}
}



