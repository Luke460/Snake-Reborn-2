package commands;

import java.util.LinkedList;
import java.util.Queue;

import game.Partita;
import gamefield.Direction;
import snake.Snake;
import spawn.SnakeSpawnManager;
import video.GameVisualizer;

public class CommandHandler {

	private Queue<String> commandsSequence;
	private Partita game;
	public static final String UP = "UP";
	public static final String RIGHT = "RIGHT";
	public static final String LEFT = "LEFT";
	public static final String DOWN = "DOWN";
	public static final String ROTATE_RIGHT = "ROTATE_RIGHT";
	public static final String ROTATE_LEFT = "ROTATE_LEFT";
	public static final String RESPAWN = "RESPAWN";
	public static final String EXIT = "EXIT";

	public CommandHandler(Partita game, GameVisualizer gameVisualizer) {
		this.game = game;
		this.commandsSequence = new LinkedList<>();
		CommandReader.initControlliDaTastiera(gameVisualizer,this.commandsSequence);
	}

	public void goUpP1() {
		Snake snake = game.getSerpentePlayer1();
		if(snake.getHP()==1 || !snake.getDirezione().getDir().equals(Direction.Dir.DOWN)) {
			snake.getDirezione().setDir(Direction.Dir.UP);
		}
	}

	public  void goDownP1() {
		Snake snake = game.getSerpentePlayer1();
		if(snake.getHP()==1 || !snake.getDirezione().getDir().equals(Direction.Dir.UP)) {
			snake.getDirezione().setDir(Direction.Dir.DOWN);
		}	
	}

	public  void goLeftP1() {
		Snake snake = game.getSerpentePlayer1();
		if(snake.getHP()==1 || !snake.getDirezione().getDir().equals(Direction.Dir.RIGHT)) {
			snake.getDirezione().setDir(Direction.Dir.LEFT);
		}		
	}

	public  void goRightP1() {
		Snake snake =game.getSerpentePlayer1();
		if(snake.getHP()==1 || !snake.getDirezione().getDir().equals(Direction.Dir.LEFT)) {
			snake.getDirezione().setDir(Direction.Dir.RIGHT);
		}		
	}

	public  void turnLeftP1() {
		game.getSerpentePlayer1().getDirezione().rotateToLeft();
	}

	public  void turnRightP1() {
		game.getSerpentePlayer1().getDirezione().rotateToRight();
	}

	public  void resuscitaPlayer1( ) {
		if(!game.getSerpentePlayer1().isVivo()) {
			SnakeSpawnManager.reviveSpecificSnake(game, game.getSerpentePlayer1());
		}
	}

	public void gameOver() {
		game.gameOver();
	}

	public void addToQueue(String s) {
		this.commandsSequence.add(s);
	}

	public void executeCommand() {
		if(!this.commandsSequence.isEmpty()) {
			String command = this.commandsSequence.poll();
			switch(command){
			case UP: 
				goUpP1();
				break;
			case DOWN:
				goDownP1();
				break;
			case RIGHT: 
				goRightP1();
				break;
			case LEFT:
				goLeftP1();
				break;
			case ROTATE_RIGHT: 
				turnRightP1();
				break;
			case ROTATE_LEFT:
				turnLeftP1();
				break;
			case RESPAWN: 
				resuscitaPlayer1();
				break;
			case EXIT:
				gameOver();
				break;
			}
		}
	}

	public void executeCommandInLeaderboardWindow() {
		if(!this.commandsSequence.isEmpty()) {
			String command = this.commandsSequence.poll();
			switch(command){
			case RESPAWN: 
				gameOver();
				break;
			case EXIT:
				gameOver();
				break;
			}
		}
	}
	
}
