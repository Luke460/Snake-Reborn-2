package commands;

import java.util.LinkedList;
import java.util.Queue;

import game.Partita;
import gamefield.Direction;
import snake.Snake;
import spawn.SnakeSpawnManager;
import video.GameVisualizer;

public class GestoreComandi {

	private Queue<String> sequenzaComandi;
	private Partita partita;
	public static final String UP = "UP";
	public static final String RIGHT = "RIGHT";
	public static final String LEFT = "LEFT";
	public static final String DOWN = "DOWN";
	public static final String ROTATE_RIGHT = "ROTATE_RIGHT";
	public static final String ROTATE_LEFT = "ROTATE_LEFT";
	public static final String RESPAWN = "RESPAWN";
	public static final String EXIT = "EXIT";

	public GestoreComandi(Partita partita, GameVisualizer visualizzatore) {
		this.partita = partita;
		this.sequenzaComandi = new LinkedList<>();
		LettoreComandi.initControlliDaTastiera(visualizzatore,this.sequenzaComandi);
	}

	public void goUpP1() {
		Snake serpente = partita.getSerpentePlayer1();
		if(serpente.getHP()==1 || !serpente.getDirezione().getDir().equals(Direction.Dir.DOWN)) {
			serpente.getDirezione().setDir(Direction.Dir.UP);
		}
	}

	public  void goDownP1() {
		Snake serpente = partita.getSerpentePlayer1();
		if(serpente.getHP()==1 || !serpente.getDirezione().getDir().equals(Direction.Dir.UP)) {
			serpente.getDirezione().setDir(Direction.Dir.DOWN);
		}	
	}

	public  void goLeftP1() {
		Snake serpente = partita.getSerpentePlayer1();
		if(serpente.getHP()==1 || !serpente.getDirezione().getDir().equals(Direction.Dir.RIGHT)) {
			serpente.getDirezione().setDir(Direction.Dir.LEFT);
		}		
	}

	public  void goRightP1() {
		Snake serpente =partita.getSerpentePlayer1();
		if(serpente.getHP()==1 || !serpente.getDirezione().getDir().equals(Direction.Dir.LEFT)) {
			serpente.getDirezione().setDir(Direction.Dir.RIGHT);
		}		
	}

	public  void turnLeftP1() {
		partita.getSerpentePlayer1().getDirezione().rotateToLeft();
	}

	public  void turnRightP1() {
		partita.getSerpentePlayer1().getDirezione().rotateToRight();
	}

	public  void resuscitaPlayer1( ) {
		if(!partita.getSerpentePlayer1().isVivo()) {
			SnakeSpawnManager.reviveSpecificSnake(partita, partita.getSerpentePlayer1());
		}
	}

	public  void gameOver() {
		partita.gameOver();
	}

	public void addToQueue(String s) {
		this.sequenzaComandi.add(s);
	}

	public void eseguiComando() {
		if(!this.sequenzaComandi.isEmpty()) {
			String codiceComando = this.sequenzaComandi.poll();
			switch(codiceComando){
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

	public void eseguiComandoEndGame() {
		if(!this.sequenzaComandi.isEmpty()) {
			String codiceComando = this.sequenzaComandi.poll();
			switch(codiceComando){
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
