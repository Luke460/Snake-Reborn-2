package snake;
import static constants.GeneralConstants.ROOM_SIZE;
import static constants.MapConstants.DARKER_CELL;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map.Entry;

import game.Game;
import gamefield.Cell;
import gamefield.CellManager;
import gamefield.Direction;
import gamefield.Room;
import support.Utility;
import video.CellRenderOption;

public class CustomBotSnake extends Snake {

	private String lastDirectionChange;
	private Direction lastDirection;
	private Skill skill;
	
	private final static String FORWARD = "FORWARD";
	private final static String BACK = "BACK";
	private final static String RIGHT = "RIGHT";
	private final static String LEFT = "LEFT";
	
	private final static String UNDEFINED_LAST_TURN = "NONE";
	
	public static enum BotLevel {
		  INSANE,
		  HARD,
		  MEDIUM,
		  EASY
	}

	public CustomBotSnake(String nome, Room room, int startingHp, Game game, Skill skill) {
		super(nome, room, startingHp, game);
		this.lastDirectionChange = UNDEFINED_LAST_TURN;
		this.skill = skill;
	}
	
	public CustomBotSnake(String nome, Room room, int startingHp, Game game, BotLevel botLevel) {
		super(nome, room, startingHp, game);
		this.lastDirectionChange = UNDEFINED_LAST_TURN;
		// Skill parameters:
		// evadeSkill, farmSkill, exploreSkill, courageSkill
		switch(botLevel) {
			case INSANE:
				this.skill = new Skill(100, 100, 100, 100);
				this.setCellRenderOption(new CellRenderOption(DARKER_CELL, Color.magenta)); 
			break;
			case HARD:
				this.skill = new Skill(80, 80, 80, 80);
				this.setCellRenderOption(new CellRenderOption(DARKER_CELL, Color.red)); 
			break;
			case MEDIUM:
				this.skill = new Skill(50, 50, 50, 50);
				this.setCellRenderOption(new CellRenderOption(DARKER_CELL, new Color(250, 150, 0))); 
			break;
			case EASY:
				this.skill = new Skill(25, 25, 25, 25);
				this.setCellRenderOption(new CellRenderOption(DARKER_CELL, Color.green));
			break;
			default:
				throw new IllegalArgumentException("invalid AI Snake BotLevel type");
		}
	}
	
	public int getSnakeAbilityScore() {
		return this.skill.getSkillScore();
	}

	@Override
	public void move() {
		if(this.getDirection().equals(this.getLastDirection().getRotatedRightDirection())){
			this.lastDirectionChange = RIGHT;
		} else if (this.getDirection().equals(this.getLastDirection().getRotatedLeftDirection())) {
			this.lastDirectionChange = LEFT;
		}
		super.move();
	}
	
	@Override
	public void chooseNewDirection() {
		
		HashMap<String, Direction> directionsMap = new HashMap<>();
		
		directionsMap.put(FORWARD, super.getDirection());
		directionsMap.put(RIGHT, super.getDirection().getRotatedRightDirection());
		directionsMap.put(LEFT, super.getDirection().getRotatedLeftDirection());
		if(this.getHP()==1) {
			directionsMap.put(BACK, super.getDirection().getReverse());
		}
		
		directionsMap = removeWalls(directionsMap);
		
		if(Utility.truePercentage(this.skill.getEvadeSkill())) {
			directionsMap = removeSnakesCells(directionsMap);
		}
		
		if(Utility.truePercentage(this.skill.getEvadeSkill())) {
			directionsMap = rimuoveClosedCells(directionsMap);
		}
		
		if(!Utility.truePercentage(this.skill.getCourageSkill())) {
			directionsMap = avoidDangerousSituation(directionsMap);
		}
		
		directionsMap = avoidEntanglement(directionsMap);
		
		HashMap<String, Direction> directionsWithImmediateFood = new HashMap<>();
		HashMap<String, Direction> directionsWithShortDistanceFood = new HashMap<>();
		HashMap<String, Direction> directionsWithMediumDistanceFood = new HashMap<>();
		HashMap<String, Direction> directionsWithLongDistanceFood = new HashMap<>();
		HashMap<String, Direction> directionsWithPortal = new HashMap<>();
		
		directionsWithImmediateFood = getDirectionsWithFood(directionsMap, 0);
		if(directionsWithImmediateFood.size()==0) {
			directionsWithShortDistanceFood = getDirectionsWithFood(directionsMap, 1);
			if(directionsWithShortDistanceFood.size()==0) {
				directionsWithMediumDistanceFood = getDirectionsWithFood(directionsMap, 4);
				if(directionsWithMediumDistanceFood.size()==0 && Utility.truePercentage(this.skill.getFarmSkill())) {
					directionsWithLongDistanceFood = getDirectionsWithFood(directionsMap, ROOM_SIZE);
				}
			}
		}
		
		if(Utility.truePercentage((int)(this.skill.getExploreSkill()*0.50))) {
			directionsWithPortal = getPortalDirections(directionsMap);
		}

		this.setLastDirection(this.getDirection());
		
		Direction newDirection = getNewDirection(
				directionsMap, 
				directionsWithImmediateFood, 
				directionsWithShortDistanceFood, 
				directionsWithMediumDistanceFood, 
				directionsWithLongDistanceFood, 
				directionsWithPortal);
		
		this.setDirection(newDirection);
		
	}
	
	private HashMap<String, Direction> avoidDangerousSituation(HashMap<String, Direction> directions) {
		if(directions.size()>1) {		
			Direction direction = this.getDirection();
			Cell headCell = CellManager.getNeighborCell(this.getHeadCell(), direction);
			// to not make the bot immune
			if(!headCell.isMortal()) {
				Cell cell = CellManager.getCellInDirection(this.getHeadCell(), direction, 1);
				if(cell.isSolid() || (cell.isSnake() && cell.getSnake().equals(this))) {
					if(directions.containsKey(FORWARD)) {
						directions.remove(FORWARD);
					}
				}
				direction = this.getDirection().getRotatedRightDirection();
				cell = CellManager.getCellInDirection(this.getHeadCell(), direction, 1);
				if(cell.isSolid() || (cell.isSnake() && cell.getSnake().equals(this))) {
					if(directions.containsKey(RIGHT)) {
						directions.remove(RIGHT);
					}
				}
				direction = this.getDirection().getRotatedLeftDirection();
				cell = CellManager.getCellInDirection(this.getHeadCell(), direction, 1);
				if(cell.isSolid() || (cell.getSnake()!=null && cell.getSnake().equals(this))) {
					if(directions.containsKey(LEFT)) {
						directions.remove(LEFT);
					}
				}
			}
		}
		return directions;
	}

	private HashMap<String, Direction> removeWalls(HashMap<String, Direction> directions) {
		HashMap<String, Direction> availableDirections = new HashMap<String, Direction> ();
		Cell headCell = this.getHeadCell();
		for(Entry<String, Direction> entry: directions.entrySet()) {
			Direction newDirection = entry.getValue();
			Cell targetCell = CellManager.getNeighborCell(headCell, newDirection);
			if(!targetCell.isSolid() && !(targetCell.isSnake() && targetCell.getSnake().equals(this))) {
				availableDirections.put(entry.getKey(), entry.getValue());
			}
		}
		return availableDirections;
	}
	
	private HashMap<String, Direction> removeSnakesCells(HashMap<String, Direction> directions) {
		HashMap<String, Direction> availableDirections = new HashMap<String, Direction> ();
		Cell headCell = this.getHeadCell();
		for(Entry<String, Direction> entry: directions.entrySet()) {
			Direction newDirection = entry.getValue();
			Cell targetCell = CellManager.getNeighborCell(headCell, newDirection);
			if(!targetCell.isSnake()) {
				availableDirections.put(entry.getKey(), entry.getValue());
			}
		}
		return availableDirections;
	}
	
	private HashMap<String, Direction> rimuoveClosedCells(HashMap<String, Direction> directions) {
		HashMap<String, Direction> availableDirections = new HashMap<String, Direction> ();
		Cell headCell = this.getHeadCell();
		for(Entry<String, Direction> entry: directions.entrySet()) {
			Direction newDirection = entry.getValue();
			Cell targetCell = CellManager.getNeighborCell(headCell, newDirection);
			// per i cappi
			if(!(
					CellManager.getNeighborCell(targetCell, new Direction(Direction.Dir.UP)).isMortal() &&
					CellManager.getNeighborCell(targetCell, new Direction(Direction.Dir.RIGHT)).isMortal() &&
					CellManager.getNeighborCell(targetCell, new Direction(Direction.Dir.DOWN)).isMortal() &&
					CellManager.getNeighborCell(targetCell, new Direction(Direction.Dir.LEFT)).isMortal()
					)) {
				availableDirections.put(entry.getKey(), entry.getValue());
			}
		}
		return availableDirections;
	}

	private HashMap<String, Direction> avoidEntanglement(HashMap<String, Direction> direction) {
		HashMap<String, Direction> availableDirections = new HashMap<String, Direction>(direction);
		if(!direction.containsKey(FORWARD) && direction.size()>1) {
			for(String key:direction.keySet()) {
				Cell targetCell = CellManager.getNeighborCell(this.getHeadCell(),direction.get(key));
				if(this.lastDirectionChange.equals(key) && !targetCell.isMortal()) {
					availableDirections.remove(key);
				}
			}
		}
		return availableDirections;
	}

	private Direction getNewDirection(HashMap<String, Direction> availableDirections,
			HashMap<String, Direction> directionsWithImmediateFood,
			HashMap<String, Direction> directionsWithShortDistanceFood, 
			HashMap<String, Direction> directionsWithMediumDistanceFood, 
			HashMap<String, Direction> directionsWithLongDistanceFood,
			HashMap<String, Direction> directionsWithPortal) {

		Direction dir = getRandomDirection(directionsWithImmediateFood);
		if(dir != null) return dir;
		
		dir = getRandomDirection(directionsWithShortDistanceFood);
		if(dir != null) return dir;
		
		dir = getRandomDirection(directionsWithMediumDistanceFood);
		if(dir != null) return dir;
		
		dir = getRandomDirection(directionsWithLongDistanceFood);
		if(dir != null) return dir;
		
		dir = getRandomDirection(directionsWithPortal);
		if(dir != null) return dir;
		
		dir = getRandomDirection(availableDirections);
		if(dir != null) return dir;
		
		return this.getDirection();
		
	}
	
	private HashMap<String, Direction> getDirectionsWithFood(HashMap<String, Direction> directions, int depth) {
		HashMap<String, Direction> availableDirections = new HashMap<String, Direction> ();
		for(Entry<String, Direction> entry: directions.entrySet()) {
			Cell startingCell = CellManager.getNeighborCell(this.getHeadCell(), entry.getValue());
			if(checkFoodInDirection(startingCell, entry.getValue(), depth)) {
				availableDirections.put(entry.getKey(), entry.getValue());
			}
		}
		return availableDirections;
	}
	
	private HashMap<String, Direction> getPortalDirections(HashMap<String, Direction> directions) {
		HashMap<String, Direction> availableDirections = new HashMap<String, Direction> ();
		for(Entry<String, Direction> entry: directions.entrySet()) {
			if(!entry.getKey().equals(BACK)) {
				Room portalRoom = getRoomWithPortalInDirection(this.getHeadCell(), entry.getValue(), ROOM_SIZE);
				if(portalRoom != null) {
					availableDirections.put(entry.getKey(), entry.getValue());
				}
			}
		}
		return availableDirections;
	}
	
	private boolean checkFoodInDirection(Cell cell, Direction dir, int remainingJumps) {
		if(cell.isFood()) return true; 
		if(cell.isMortal()) return false;
		Cell followingCell = CellManager.getNeighborCell(cell, dir);
		if(!cell.getRoom().equals(followingCell.getRoom())) return false;
		if(remainingJumps>0) {
			return checkFoodInDirection(followingCell, dir, remainingJumps-1);
		} else {
			return false;
		}
	}
	
	private Room getRoomWithPortalInDirection(Cell cell, Direction dir, int range) {
		Cell casellaSuccessiva = CellManager.getNeighborCell(cell, dir);
		if(!cell.getRoom().equals(casellaSuccessiva.getRoom())) return casellaSuccessiva.getRoom();
		if(range <=0 || casellaSuccessiva.isMortal()) return null;
        return getRoomWithPortalInDirection(casellaSuccessiva, dir, range-1);
	}
	
	private Direction getRandomDirection(HashMap<String, Direction> directionsMap) {
		
		if(directionsMap.size()==0) return null;

		if(Utility.truePercentage(95) && directionsMap.containsKey(FORWARD)) {
			return directionsMap.get(FORWARD);
		} else if (Utility.truePercentage(50) && directionsMap.containsKey(RIGHT)) {
			return directionsMap.get(RIGHT);
		} else if (directionsMap.containsKey(LEFT)){
			return directionsMap.get(LEFT);
		}
		
		if(directionsMap.containsKey(FORWARD)) {
			return directionsMap.get(FORWARD);
		} else if (directionsMap.containsKey(RIGHT)) {
			return directionsMap.get(RIGHT);
		} else if (directionsMap.containsKey(LEFT)){
			return directionsMap.get(LEFT);
		} else if (directionsMap.containsKey(BACK)){
			return directionsMap.get(BACK);
		}
		
		return null;
		
	}

	public Skill getSkill() {
		return skill;
	}

	public void setSkill(Skill skill) {
		this.skill = skill;
	}

	public Direction getLastDirection() {
		return lastDirection;
	}

	public void setLastDirection(Direction lastDirection) {
		this.lastDirection = lastDirection;
	}

}
