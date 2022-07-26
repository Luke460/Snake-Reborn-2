package gamefield;

import snake.Snake;

public class Cell {

	private Room room;
	private Position position;
	private char defaultStatus;
	private int hp;
	private Snake snake;
	private boolean isSolid;
	private int foodAmount;

	public Cell(Room room, Position position, char defaultStatus, boolean isSolid) {
		this.room = room;
		this.position = position;
		this.defaultStatus = defaultStatus;
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
	
	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}

	public char getDefaultStatus() {
		return defaultStatus;
	}
	
	public void setDefaultStatus(char defaultStatus) {
		this.defaultStatus = defaultStatus;
	}

	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}

	public boolean isSnakeHead() {
		return this.snake!=null && this.equals(this.snake.getHeadCell());
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
