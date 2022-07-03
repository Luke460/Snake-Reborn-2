package score;

import java.util.Comparator;

import snake.Snake;

public class SnakeEndGameScoreComparator implements Comparator<Snake> {

	@Override
	public int compare(Snake first, Snake second) {
		return Integer.compare(second.getTotalSnakeScoreLifeStatusAdjusted(), first.getTotalSnakeScoreLifeStatusAdjusted());
	}

}



