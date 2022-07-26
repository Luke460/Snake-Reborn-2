package snake;

import static constants.GeneralConstants.ROOM_SIZE;
import static constants.GeneralConstants.FOOD_SCORE_MULTIPLIER;
import static constants.GeneralConstants.KILL_SCORE_MULTIPLIER;
import static constants.GeneralConstants.NAME_PLAYER_1;
import static constants.GeneralConstants.SNAKE_RESPAWN_CD;
import static constants.GeneralConstants.MAX_HP;
import static constants.MapConstants.DARKER_CELL;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map.Entry;

import audio.SoundManager;
import game.Game;
import gamefield.Cell;
import gamefield.CellManager;
import gamefield.Direction;
import gamefield.Position;
import gamefield.Room;
import score.ScoreHandler;
import spawn.FoodSpawnManager;
import support.Utility;
import video.CellRenderOption;

import java.util.Objects;
import java.util.TreeMap;

public abstract class Snake {

	private LinkedList<Cell> cells;
	private String name;
	private Direction direction;
	private int killsNumber;
	private int currentKillingSpree;
	private int bestGameKillingSpree;
	private int deathsNumber;
	private int preDeathHp;
	private Game game;
	private Room lastRoom;
	private Cell headCell;
	private boolean alive;
	private CellRenderOption cellRenderOption;
	private int previousScore;
	private long deathTimestamp;
	private int currentFoodTaken;
	private int totalFoodTaken;
	
	public static final CellRenderOption DEFAULT_CELL_RENDER_OPTION = new CellRenderOption(DARKER_CELL, Color.gray);

	public Snake(String name, Room room, int startingHp, Game game) {
		this.alive = false;
		this.game = game;
		this.name=name;
		this.previousScore=0;
		this.deathTimestamp=-1;
		this.deathsNumber=0;
		this.killsNumber=0;
		this.currentKillingSpree=0;
		this.bestGameKillingSpree=0;
		this.currentFoodTaken=0;
		this.totalFoodTaken=0;
		this.cellRenderOption=DEFAULT_CELL_RENDER_OPTION;
		this.resetSnake(room, startingHp);
	}

	public Cell getHeadCell(){
		return this.headCell;
	}

	public int getHP(){
		return this.getHeadCell().getHp();
	}

	public LinkedList<Cell> getCells() {
		return cells;
	}

	public void setCells(LinkedList<Cell> cells) {
		this.cells = cells;
	}
	
	public void move(){
		Cell oldCell = this.getHeadCell();
		Cell newCell = CellManager.getNeighborCell(oldCell, this.getDirection());

		if(!newCell.isMortal()){
			if(newCell.isFood()){
				this.increaseHp(newCell.getFoodAmount());
			}
			CellManager.setSnakeCell(newCell, this,this.getHP());

			// move
			reduceHp();
			this.cells.add(newCell);
			this.setHeadCell(newCell);
			this.setLastRoom(newCell.getRoom());
		} else { // mortal cell
			if(newCell.isSnake()){
				Snake otherSnake = newCell.getSnake();
				if(otherSnake.getHeadCell().getPosition().equals(newCell.getPosition())){
					otherSnake.dieNoKillForSelectedSnake(this);
				}
			}
			this.die();
		}
	}

	private void reduceHp() {
		Iterator<Cell> iterator = this.getCells().iterator();
		while(iterator.hasNext()){
			Cell c = iterator.next();
			c.setHp(c.getHp()-1);
			if(c.getHp()<=0) {
				c.freeCell();
				iterator.remove();
			}
		}
	}
	
	public void increaseHp(int qty) {
		this.currentFoodTaken+=qty;
		this.totalFoodTaken+=qty;
		for(Cell c : this.getCells()){
			if(c.getHp()+qty<=MAX_HP){
				c.setHp(c.getHp()+qty);
			} else {
				c.setHp(MAX_HP);
			}
		}
	}

	public int getPreDeathHp() {
		return preDeathHp;
	}

	public void setPreDeathHp(int preDeathHp) {
		this.preDeathHp = preDeathHp;
	}
	
	public void die(){
		this.setAlive(false);
		this.deathTimestamp = System.currentTimeMillis();
		preDeathHp = this.getHeadCell().getHp();
		this.deathsNumber++;
		checkKill(null);
		releaseFoodAndClearCells();
	}
	
	public void dieNoKillForSelectedSnake(Snake snake){
		this.setAlive(false);
		this.deathTimestamp = System.currentTimeMillis();
		preDeathHp = this.getHeadCell().getHp();
		this.deathsNumber++;
		checkKill(snake);
		releaseFoodAndClearCells();
	}
	
	private void checkKill(Snake excludedSnake) {		
		TreeMap<String,Snake> killers = new TreeMap<>();
		for(Cell c : this.getCells()) {
			HashSet<Cell> nearCells = new HashSet<>();
			nearCells.add(CellManager.getNeighborCell(c, new Direction(Direction.Dir.UP)));
			nearCells.add(CellManager.getNeighborCell(c, new Direction(Direction.Dir.RIGHT)));
			nearCells.add(CellManager.getNeighborCell(c, new Direction(Direction.Dir.LEFT)));
			nearCells.add(CellManager.getNeighborCell(c, new Direction(Direction.Dir.DOWN)));
			for(Cell ca : nearCells) {
				if(ca.isSnake()) {
					Snake killerSnake = ca.getSnake();
					if(!killerSnake.equals(this)) {
						killers.put(ca.getSnake().getName(), ca.getSnake());
					}
				}
			}
		}
		for(Entry<String, Snake> entry:killers.entrySet()) {
			if(excludedSnake==null || !entry.getValue().equals(excludedSnake)) {
				entry.getValue().performKill();
			}
		}
	}

	protected void releaseFoodAndClearCells() {
		FoodSpawnManager.spawnFoodAfterSnakeDeath(this.cells);
		this.cells.clear();
	}

	abstract public void chooseNewDirection();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Direction getDirection() {
		return direction;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}

	public void performKill(){
		this.killsNumber++;
		this.currentKillingSpree++;
		if(this.currentKillingSpree>this.bestGameKillingSpree) {
			this.bestGameKillingSpree = this.currentKillingSpree;
		}
		if(this.getName().equals(NAME_PLAYER_1)){
			SoundManager.playSlainSound();
		}
	}

	public int getKillsNumber() {
		return killsNumber;
	}

	public boolean isHead(Cell cell){
		if(this.getHeadCell().equals(cell)) return true;
		return false;
	}
	
	public Game getGame() {
		return game;
	}
	
	public void setGame(Game game) {
		this.game = game;
	}

	public void setLastRoom(Room lastRoom) {
		this.lastRoom = lastRoom;
	}
	
	public Room getLastRoom() {
		return lastRoom;
	}

	public void setHeadCell(Cell headCell) {
		this.headCell = headCell;
	}
	
	public void resetSnake(Room room, int respawnHp) {
		this.previousScore = (int)(this.getTotalSnakeScorePreDeath()/2);
		this.alive = true;
		this.preDeathHp = 0;
		this.currentKillingSpree=0;
		this.currentFoodTaken=0;

		// random center spawn
		byte deltaXspawn = 0;
		if(Utility.truePercentage(50)) deltaXspawn = -1;
		byte deltaYspawn = 0;
		if(Utility.truePercentage(50)) deltaYspawn = -1;
		byte centerPosition = (byte)(ROOM_SIZE/2);
		Position firstCellPosition = new Position((byte)(centerPosition+deltaXspawn),(byte)(centerPosition+deltaYspawn));
		
		// random direction
		Direction snakeDirection = getBestSpawnDirection(firstCellPosition, room);
		this.setDirection(snakeDirection);
		Direction cellCreationDirection = snakeDirection.getReverse();
		// Snake head
		this.setCells(new LinkedList<Cell>());
		this.setLastRoom(room);
		Cell firstCell = room.getCellsMap().get(firstCellPosition);
		this.headCell = firstCell;
		firstCell.setSnake(this);
		int cellHp = respawnHp;
		firstCell.setHp(cellHp);
		this.getCells().add(firstCell);

		// other snake cell creation
		Cell previousCell = firstCell;
		for(int i=0; i<respawnHp-1; i++){
			Cell cell = CellManager.getNeighborCell(previousCell, cellCreationDirection);
			if(!cell.isMortal()) {
				cell.setSnake(this);
				cellHp--;
				cell.setHp(cellHp);
				this.getCells().add(cell);
				previousCell = cell;
			} else {
				break;
			}
		}
		
	}
	
	private Direction getBestSpawnDirection(Position firstCellPosition, Room room) {
		Cell firstCell = room.getCellsMap().get(firstCellPosition);
		Direction up = new Direction(Direction.Dir.UP);
		Direction right = new Direction(Direction.Dir.RIGHT);
		Direction down = new Direction(Direction.Dir.DOWN);
		Direction left = new Direction(Direction.Dir.LEFT);
		ArrayList<Direction> dirList = new ArrayList<>();
		dirList.add(up);
		dirList.add(right);
		dirList.add(down);
		dirList.add(left);
		//not needed due to the random spawn position
		//Collections.shuffle(dirList);
		TreeMap<Integer,Direction> freeSpaceToDirection = new TreeMap<>();
		for(Direction dir: dirList) {
			freeSpaceToDirection.put(CellManager.getNumberOfNonLethalCellsInDirection(firstCell, dir), dir);
		}
		return freeSpaceToDirection.get(freeSpaceToDirection.lastKey());
	}

	public boolean isAlive() {
		return alive;
	}

	public void setAlive(boolean alive) {
		this.alive = alive;
	}

	public CellRenderOption getCellRenderOption() {
		return cellRenderOption;
	}

	public void setCellRenderOption(CellRenderOption cellRenderOption) {
		this.cellRenderOption = cellRenderOption;
	}
	
	public double getCurrentGameSnakeScore() {
		double foodScore = this.currentFoodTaken*FOOD_SCORE_MULTIPLIER*ScoreHandler.getScoreMultiplier(this.game);
		double killsScore = this.getCurrentKillingSpree()*KILL_SCORE_MULTIPLIER*ScoreHandler.getScoreMultiplier(this.game);
		return foodScore+killsScore;
	}
	
	public int getTotalSnakeScorePreDeath() {
		return (int)(this.getCurrentGameSnakeScore() + this.previousScore);
	}
	
	public int getTotalSnakeScoreLifeStatusAdjusted() {
		if(this.isAlive()) {
			return getTotalSnakeScorePreDeath();
		} else {
			return getTotalSnakeScorePreDeath()/2;
		}
	}
	
	public boolean canRespawn() {
		if(System.currentTimeMillis()>this.deathTimestamp+(SNAKE_RESPAWN_CD*1000)) {
			return true;
		}
		return false;
	}
	
	public int getRespawnSecondsLeft() {
		if(this.canRespawn()) {
			return 0;
		} else {
			return (int) ((this.deathTimestamp+(SNAKE_RESPAWN_CD*1000)-System.currentTimeMillis())/1000)+1;
		}
	}

	public int getDeathsNumber() {
		return deathsNumber;
	}
	
	public int getCurrentKillingSpree() {
		return currentKillingSpree;
	}
	
	public int getBestGameKillingSpree() {
		return bestGameKillingSpree;
	}
	
	public int getCurrentFoodTaken() {
		return currentFoodTaken;
	}

	public int getTotalFoodTaken() {
		return totalFoodTaken;
	}
	
	public int getLength() {
		if(this.cells==null) return 0;
		return this.cells.size();
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(name);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Snake other = (Snake) obj;
		return Objects.equals(name, other.name);
	}

	@Override
	public String toString() {
		return "Snake [name=" + name + ", direction=" + direction + ", lastRoom=" + lastRoom + ", alive=" + alive + "]";
	}

}
