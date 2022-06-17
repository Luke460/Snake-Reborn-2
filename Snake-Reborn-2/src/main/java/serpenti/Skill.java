package serpenti;

public class Skill {
	
	private int evadeSkill;
	private int farmSkill;
	private int exploreSkill;
	private int courageSkill;
	
	public Skill(int evadeSkill, int farmSkill, int exploreSkill, int courageSkill) {
		super();
		this.evadeSkill = evadeSkill;
		this.farmSkill = farmSkill;
		this.exploreSkill = exploreSkill;
		this.courageSkill = courageSkill;
	}

	public int getEvadeSkill() {
		return evadeSkill;
	}

	public void setEvadeSkill(int evadeSkill) {
		this.evadeSkill = evadeSkill;
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
		return (int) (this.evadeSkill * 0.60 + this.farmSkill * 0.20 + this.exploreSkill * 0.10 + this.courageSkill * 0.10);
	}

}
