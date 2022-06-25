package terrenoDiGioco;

import serpenti.Snake;
import supporto.Posizione;

public class Casella {

	private Stanza stanza;
	private Posizione position;
	private char originalStatus;
	private int hp;
	private Snake snake;
	private boolean isSolid;
	private int foodAmount;

	public Casella(Stanza stanza, Posizione posizione, char statoOriginario, boolean isSolid) {
		this.stanza = stanza;
		this.position = posizione;
		this.originalStatus = statoOriginario;
		this.hp = -1;
		this.snake = null;
		this.isSolid = isSolid;
		this.foodAmount = 0;
		
	}
	
	public void freeCell() {
		this.hp = -1;
		this.snake = null;
		this.foodAmount = 0;
	}
	
	public Posizione getPosizione() {
		return position;
	}

	public void setPosizione(Posizione posizione) {
		this.position = posizione;
	}

	public char getStatoOriginario() {
		return originalStatus;
	}
	
	public void setStatoOriginario(char statoOriginario) {
		this.originalStatus = statoOriginario;
	}

	public Stanza getStanza() {
		return stanza;
	}

	public void setStanza(Stanza stanza) {
		this.stanza = stanza;
	}

	public boolean isSnakeHead() {
		return this.snake!=null && this.equals(this.snake.getCasellaDiTesta());
	}
	
	public boolean isEmpty() {
		return !this.isSolid && !this.isSnake() && !this.isFood() && !this.isPoison();
	}

	public int getHp() {
		return hp;
	}

	public void setHp(int hp) {
		this.hp = hp;
	}
	
	public Snake getSnake() {
		return snake;
	}

	public void setSnake(Snake snake) {
		this.snake = snake;
	}

	public boolean isSolid() {
		return isSolid;
	}

	public void setSolid(boolean isSolid) {
		this.isSolid = isSolid;
	}
	
	public boolean isSnake() {
		return this.snake!=null;
	}
	
	public boolean isMortal() {
		return this.isSolid() || this.isSnake();
	}

	public boolean isFood() {
		return this.foodAmount>0;
	}
	
	public boolean isPoison() {
		return this.foodAmount<0;
	}
	
	public int getFoodAmount() {
		return foodAmount;
	}

	public void setFoodAmount(int foodAmount) {
		this.foodAmount = foodAmount;	
	}
	
}
