package gamefield;

/**
 * Rappresenta una posizione, modellata come coppia di interi 
 * ascissa (x) ed ordinata (y), all'interno del {@link Pozzo}.
 * 
 */

public class Position {
	
	private int x;
	private int y;
	
	public Position(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public int getX() {
		return this.x;
	}

	public int getY() {
		return this.y;
	}
	
	public Position getPositionInDirection(Direction d) {
		// X=0 Y=0 : UPPER LEFT CORNER
		int x = getX();
		int y = getY();
		switch(d.getDir()) {
			case UP:
				y -= 1;
				break;
			case RIGHT:
				x += 1;
				break;
			case DOWN:
				y += 1;
				break;
			case LEFT:
				x -= 1;
				break;
		}
		return new Position(x, y);
	}
	
	@Override
	public int hashCode(){
		return this.getX()+this.getY();
	}
	
	@Override
	public boolean equals(Object o){
		Position that = (Position) o;
		return (this.getX()==that.getX() &&
				this.getY()==that.getY());
	}
	@Override
	public String toString(){
		return "X = " + this.getX() + "  Y = " + this.getY();
	}

}
