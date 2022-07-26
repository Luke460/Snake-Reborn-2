package gamefield;

import java.util.Objects;

public class Position {
	
	private byte x;
	private byte y;
	
	public Position(byte x, byte y) {
		this.x = x;
		this.y = y;
	}
	
	public byte getX() {
		return this.x;
	}

	public byte getY() {
		return this.y;
	}
	
	public Position getPositionInDirection(Direction d) {
		// X=0 Y=0 : UPPER LEFT CORNER
		byte x = getX();
		byte y = getY();
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
	public int hashCode() {
		return Objects.hash(x, y);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Position other = (Position) obj;
		return x == other.x && y == other.y;
	}

	@Override
	public String toString(){
		return "X = " + this.getX() + "  Y = " + this.getY();
	}

}
