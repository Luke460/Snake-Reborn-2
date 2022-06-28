package score;

import java.util.Comparator;

import snake.Snake;

public class SnakeScoreComparator implements Comparator<Snake> {

	@Override
	public int compare(Snake first, Snake second) {
		return Integer.compare(second.getTotalSnakeScore(), first.getTotalSnakeScore());
	}

}



