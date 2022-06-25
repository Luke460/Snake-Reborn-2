package gamefield;

import java.util.Objects;

public class Direction {
	
	public enum Dir {
		  UP,
		  RIGHT,
		  DOWN,
		  LEFT
		}

	private Dir dir;

	public Direction(Dir dir){
		this.dir = dir;
	}

	public Direction(){
		int random = (int) ((Math.random()*4)+1);
		if(random==1){
			this.dir = Dir.UP;
		} else if (random==2){
			this.dir = Dir.RIGHT;
		} else if (random==3){
			this.dir = Dir.DOWN;
		} else if (random==4){
			this.dir = Dir.LEFT;
		}
	}

	public Dir getDir() {
		return dir;
	}

	public void setDir(Dir dir) {
		this.dir = dir;
	}

	public void rotateToRight(){
		switch(this.dir) {
			case UP:
				this.dir = Dir.RIGHT;
				break;
			case RIGHT:
				this.dir = Dir.DOWN;
				break;
			case DOWN:
				this.dir = Dir.LEFT;
				break;
			case LEFT:
				this.dir = Dir.UP;
				break;				
		}
	}

	public void rotateToLeft(){
		switch(this.dir) {
			case UP:
				this.dir = Dir.LEFT;
				break;
			case RIGHT:
				this.dir = Dir.UP;
				break;
			case DOWN:
				this.dir = Dir.RIGHT;
				break;
			case LEFT:
				this.dir = Dir.DOWN;
				break;				
		}
	}
	
	public Direction getRotatedRightDirection(){
		Direction dir = new Direction();
		dir.setDir(this.getDir());
		dir.rotateToRight();
		return dir;
	}
	
	public Direction getRotatedLeftDirection(){
		Direction dir = new Direction();
		dir.setDir(this.getDir());
		dir.rotateToLeft();
		return dir;
	}

	public void Reverse() {
		this.rotateToRight();
		this.rotateToRight();
	}
	
	public Direction getInversa() {
		Direction dir = new Direction(this.getDir());
		dir.rotateToRight();
		dir.rotateToRight();
		return dir;
	}
	

	@Override
	public int hashCode() {
		return Objects.hash(dir);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Direction other = (Direction) obj;
		return dir == other.dir;
	}

	@Override
	public String toString() {
		return "Direction [direction=" + dir + "]";
	}
	
	
	
}
