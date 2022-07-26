package gamefield;

import static constants.GeneralConstants.ROOM_SIZE;
import static constants.GeneralConstants.EAST;
import static constants.GeneralConstants.NORTH;
import static constants.GeneralConstants.WEST;
import static constants.GeneralConstants.SOUTH;

import snake.Snake;

public class CellManager {
	
	public static void setSnakeCell(Cell cell, Snake snake, int hp) {
		cell.setSnake(snake);
		cell.setHp(hp);
	}

	public static boolean isClosed(Room room, Cell cell, Direction direction) {
		Cell forwardCell = getNeighborCell(cell, direction);
		Cell rightCell = getNeighborCell(cell, direction.getRotatedRightDirection());
		Cell leftCell = getNeighborCell(cell, direction.getRotatedLeftDirection());
		if(	forwardCell.isMortal() &&
			rightCell.isMortal() &&
		    leftCell.isMortal() ){
			return true;
		} else {
			return false;
		}
	}
	
	public static Cell getCellInDirection(Cell cell, Direction direction, int distance) {
		if(distance == 0) {
			return getNeighborCell(cell, direction);
		} else {
			return getCellInDirection(getNeighborCell(cell, direction), direction, distance-1);
		}
	}
	
	public static Cell getNeighborCell(Cell cell, Direction direction) {
		Position cellPosition = cell.getPosition();
		Position newCellPosition = cellPosition.getPositionInDirection(direction);
		Room currentRoom = cell.getRoom();
		// out of room check
		byte roomLength = (byte)(ROOM_SIZE-1);
		
		if(newCellPosition.getX()>roomLength){
			newCellPosition = new Position((byte)0,newCellPosition.getY()); 
			return currentRoom.getLinksMap().get(EAST).getCellsMap().get(newCellPosition);
		}
		if(newCellPosition.getX()<0){
			newCellPosition = new Position(roomLength,newCellPosition.getY()); 
			return currentRoom.getLinksMap().get(WEST).getCellsMap().get(newCellPosition);
		}
		if(newCellPosition.getY()>roomLength){
			newCellPosition = new Position(newCellPosition.getX(),(byte)0); 
			return currentRoom.getLinksMap().get(SOUTH).getCellsMap().get(newCellPosition);
		}
		if(newCellPosition.getY()<0){
			newCellPosition = new Position(newCellPosition.getX(),roomLength); 
			return currentRoom.getLinksMap().get(NORTH).getCellsMap().get(newCellPosition);
		}
		
		// we are in the room bounds
		return  currentRoom.getCellsMap().get(newCellPosition);
	}

	public static int getNumberOfNonLethalCellsInDirection(Cell firstCell, Direction dir) {
		int distance = 0;
		Cell cell = getNeighborCell(firstCell, dir);
		Room startingRoom = cell.getRoom();
		while(!cell.isMortal() && cell.getRoom().equals(startingRoom)) {
			distance++;
			cell = getNeighborCell(cell, dir);
		}
		return distance;
	}

}
