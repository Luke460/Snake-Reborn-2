package score;

import java.util.Comparator;

import snake.Snake;

public class SnakeScoreComparator implements Comparator<Snake> {

	@Override
	public int compare(Snake first, Snake second) {
		if(second.getTotalSnakeScore()!=first.getTotalSnakeScore()) {
			return Integer.compare(second.getTotalSnakeScore(), first.getTotalSnakeScore());
		} else if (second.getKillsNumber()!=first.getKillsNumber()) {
			return Integer.compare(second.getKillsNumber(), first.getKillsNumber());
		} else if (second.getDeathsNumber()!=first.getDeathsNumber()){
			return Integer.compare(first.getDeathsNumber(), second.getDeathsNumber());
		} else {
			return Integer.compare(second.getTotalFoodTaken(), first.getTotalFoodTaken());
		}
	}

}



