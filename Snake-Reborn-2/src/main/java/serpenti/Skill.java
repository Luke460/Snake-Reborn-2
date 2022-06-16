package serpenti;

public class Skill {
	
	private int evadeWallSkill;
	private int evadeSnakeSkill;
	private int farmSkill;
	private int exploreSkill;
	private int courageSkill;
	
	public Skill(int evadeWallSkill, int evadeSnakeSkill, int farmSkill, int exploreSkill, int courageSkill) {
		super();
		this.evadeWallSkill = evadeWallSkill;
		this.evadeSnakeSkill = evadeSnakeSkill;
		this.farmSkill = farmSkill;
		this.exploreSkill = exploreSkill;
		this.courageSkill = courageSkill;
	}

	public int getEvadeWallSkill() {
		return evadeWallSkill;
	}

	public void setEvadeWallSkill(int evadeWallSkill) {
		this.evadeWallSkill = evadeWallSkill;
	}

	public int getEvadeSnakeSkill() {
		return evadeSnakeSkill;
	}

	public void setEvadeSnakeSkill(int evadeSnakeSkill) {
		this.evadeSnakeSkill = evadeSnakeSkill;
	}

	public int getFarmSkill() {
		return farmSkill;
	}

	public void setFarmSkill(int farmSkill) {
		this.farmSkill = farmSkill;
	}

	public int getExploreSkill() {
		return exploreSkill;
	}

	public void setExploreSkill(int exploreSkill) {
		this.exploreSkill = exploreSkill;
	}
	
	public int getCourageSkill() {
		return courageSkill;
	}
	
	public void setCourageSkill(int courageSkill) {
		this.courageSkill = courageSkill;
	}
	
	public int getSkillScore() {
		return (int) (this.evadeWallSkill * 0.50 + this.evadeSnakeSkill * 0.30 + this.farmSkill * 0.10 + this.exploreSkill * 0.05 + this.courageSkill * 0.05);
	}

}
