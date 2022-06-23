package gestoreComandi;

import java.util.LinkedList;
import java.util.Queue;

import game.Partita;
import popolatori.PopolatoreSerpenti;
import serpenti.Snake;
import supporto.Direction;
import video.Visualizzatore;

public class GestoreComandi {

	private Queue<String> sequenzaComandi;
	private Partita partita;

	public GestoreComandi(Partita partita, Visualizzatore visualizzatore) {
		this.partita = partita;
		this.sequenzaComandi = new LinkedList<>();
		LettoreComandi.initControlliDaTastiera(visualizzatore,this.sequenzaComandi);
	}

	public void goUpP1() {
		Snake serpente = partita.getSerpentePlayer1();
		Direction dir = serpente.getDirezione();
		if(!dir.getDir().equals(Direction.Dir.DOWN)) {
			dir.setDir(Direction.Dir.UP);
		}
	}

	public  void goDownP1() {

		Snake serpente = partita.getSerpentePlayer1();
		Direction dir = serpente.getDirezione();
		if(!dir.getDir().equals(Direction.Dir.UP)) {
			dir.setDir(Direction.Dir.DOWN);
		}	
	}

	public  void goLeftP1() {

		Snake serpente = partita.getSerpentePlayer1();
		Direction dir = serpente.getDirezione();
		if(!dir.getDir().equals(Direction.Dir.RIGHT)) {
			dir.setDir(Direction.Dir.LEFT);
		}		

	}

	public  void goRightP1() {

		Snake serpente =partita.getSerpentePlayer1();
		Direction dir = serpente.getDirezione();
		if(!dir.getDir().equals(Direction.Dir.LEFT)) {
			dir.setDir(Direction.Dir.RIGHT);
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
			PopolatoreSerpenti.provaAResuscitareUnSerpente(partita, partita.getSerpentePlayer1());
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
			case "W": 
				goUpP1();
				break;
			case "S":
				goDownP1();
				break;
			case "D": 
				goRightP1();
				break;
			case "A":
				goLeftP1();
				break;
			case "RIGHT": 
				turnRightP1();
				break;
			case "LEFT":
				turnLeftP1();
				break;
			case "ENTER": 
				resuscitaPlayer1();
				break;
			case "ESCAPE":
				gameOver();
				break;
			}
		}

	}

}
