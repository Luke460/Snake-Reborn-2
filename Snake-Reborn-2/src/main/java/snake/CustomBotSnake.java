package snake;
import static constants.GeneralConstants.DIMENSIONE_STANZA_DEFAULT;
import static constants.MapConstants.DARKER_CELL;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map.Entry;

import game.Partita;
import gamefield.Casella;
import gamefield.CasellaManager;
import gamefield.Direction;
import gamefield.Stanza;
import support.Utility;
import video.CellRenderOption;

public class CustomBotSnake extends Snake {

	private String ultimaSterzata;
	private Direction lastDirection;
	private Skill skill;
	
	private final static String FORWARD = "FORWARD";
	private final static String RIGHT = "RIGHT";
	private final static String LEFT = "LEFT";
	
	private final static String UNDEFINED_LAST_TURN = "NONE";
	
	public static enum BotLevel {
		  INSANE,
		  HARD,
		  MEDIUM,
		  EASY
	}

	public CustomBotSnake(String nome, Stanza stanza, int vitaIniziale, Partita partita, Skill skill) {
		super(nome, stanza, vitaIniziale, partita);
		this.ultimaSterzata = UNDEFINED_LAST_TURN;
		this.skill = skill;
	}
	
	public CustomBotSnake(String nome, Stanza stanza, int vitaIniziale, Partita partita, BotLevel botLevel) {
		super(nome, stanza, vitaIniziale, partita);
		this.ultimaSterzata = UNDEFINED_LAST_TURN;
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
	public void sposta() {
		if(this.getDirezione().equals(this.getLastDirection().getRotatedRightDirection())){
			this.ultimaSterzata = RIGHT;
		} else if (this.getDirezione().equals(this.getLastDirection().getRotatedLeftDirection())) {
			this.ultimaSterzata = LEFT;
		}
		super.sposta();
	}
	
	@Override
	public void scegliNuovaDirezione() {
		
		HashMap<String, Direction> direzioni = new HashMap<>();
		
		direzioni.put(FORWARD, super.getDirezione());
		direzioni.put(RIGHT, super.getDirezione().getRotatedRightDirection());
		direzioni.put(LEFT, super.getDirezione().getRotatedLeftDirection());
		
		direzioni = rimuoviCelleMuro(direzioni);
		
		if(Utility.truePercentage(this.skill.getEvadeSkill())) {
			direzioni = rimuoviCelleSerpenti(direzioni);
		}
		
		if(Utility.truePercentage(this.skill.getEvadeSkill())) {
			direzioni = rimuoviCelleCappi(direzioni);
		}
		
		if(!Utility.truePercentage(this.skill.getCourageSkill())) {
			direzioni = evitaSituazionePericolosa(direzioni);
		}
		
		direzioni = rimuoviDirezioneAggrovigliamento(direzioni);
		
		HashMap<String, Direction> direzioniConCiboImmediate = new HashMap<>();
		HashMap<String, Direction> direzioniConCiboVicine = new HashMap<>();
		HashMap<String, Direction> direzioniConCiboMedie = new HashMap<>();
		HashMap<String, Direction> direzioniConCiboLontane = new HashMap<>();
		HashMap<String, Direction> direzioniPortali = new HashMap<>();
		
		direzioniConCiboImmediate = getDirezioniConCibo(direzioni, 0);
		if(direzioniConCiboImmediate.size()==0) {
			direzioniConCiboVicine = getDirezioniConCibo(direzioni, 1);
			if(direzioniConCiboVicine.size()==0) {
				direzioniConCiboMedie = getDirezioniConCibo(direzioni, 4);
				if(direzioniConCiboMedie.size()==0 && Utility.truePercentage(this.skill.getFarmSkill())) {
					direzioniConCiboLontane = getDirezioniConCibo(direzioni, DIMENSIONE_STANZA_DEFAULT);
				}
			}
		}
		
		if(Utility.truePercentage((int)(this.skill.getExploreSkill()*0.50))) {
			direzioniPortali = getDirezioniPortali(direzioni);
		}

		this.setLastDirection(this.getDirezione());
		Direction nuovaDirezione = getNewDirection(direzioni, direzioniConCiboImmediate, direzioniConCiboVicine, direzioniConCiboMedie, direzioniConCiboLontane, direzioniPortali);
		this.setDirezione(nuovaDirezione);
		
	}
	
	private HashMap<String, Direction> evitaSituazionePericolosa(HashMap<String, Direction> direzioni) {
		if(direzioni.size()>1) {		
			Direction direzione = this.getDirezione();
			Casella casellaInTesta = CasellaManager.getCasellaAdiacente(this.getCasellaDiTesta(), direzione);
			// per non rendere il bot immune alle collisioni
			if(!casellaInTesta.isMortal()) {
				Casella casella = CasellaManager.getCasellaInDirezione(this.getCasellaDiTesta(), direzione, 1);
				if(casella.isSolid() || (casella.isSnake() && casella.getSnake().equals(this))) {
					if(direzioni.containsKey(FORWARD)) {
						direzioni.remove(FORWARD);
					}
				}
				direzione = this.getDirezione().getRotatedRightDirection();
				casella = CasellaManager.getCasellaInDirezione(this.getCasellaDiTesta(), direzione, 1);
				if(casella.isSolid() || (casella.isSnake() && casella.getSnake().equals(this))) {
					if(direzioni.containsKey(RIGHT)) {
						direzioni.remove(RIGHT);
					}
				}
				direzione = this.getDirezione().getRotatedLeftDirection();
				casella = CasellaManager.getCasellaInDirezione(this.getCasellaDiTesta(), direzione, 1);
				if(casella.isSolid() || (casella.getSnake()!=null && casella.getSnake().equals(this))) {
					if(direzioni.containsKey(LEFT)) {
						direzioni.remove(LEFT);
					}
				}
			}
		}
		return direzioni;
	}

	private HashMap<String, Direction> rimuoviCelleMuro(HashMap<String, Direction> direzioni) {
		HashMap<String, Direction> availableDirections = new HashMap<String, Direction> ();
		Casella casellaDiTesta = this.getCasellaDiTesta();
		for(Entry<String, Direction> entry: direzioni.entrySet()) {
			Direction newDirection = entry.getValue();
			Casella casellaBersaglio = CasellaManager.getCasellaAdiacente(casellaDiTesta, newDirection);
			if(!casellaBersaglio.isSolid() && !(casellaBersaglio.isSnake() && casellaBersaglio.getSnake().equals(this))) {
				availableDirections.put(entry.getKey(), entry.getValue());
			}
		}
		return availableDirections;
	}
	
	private HashMap<String, Direction> rimuoviCelleSerpenti(HashMap<String, Direction> direzioni) {
		HashMap<String, Direction> availableDirections = new HashMap<String, Direction> ();
		Casella casellaDiTesta = this.getCasellaDiTesta();
		for(Entry<String, Direction> entry: direzioni.entrySet()) {
			Direction newDirection = entry.getValue();
			Casella casellaBersaglio = CasellaManager.getCasellaAdiacente(casellaDiTesta, newDirection);
			if(!casellaBersaglio.isSnake()) {
				availableDirections.put(entry.getKey(), entry.getValue());
			}
		}
		return availableDirections;
	}
	
	private HashMap<String, Direction> rimuoviCelleCappi(HashMap<String, Direction> direzioni) {
		HashMap<String, Direction> availableDirections = new HashMap<String, Direction> ();
		Casella casellaDiTesta = this.getCasellaDiTesta();
		for(Entry<String, Direction> entry: direzioni.entrySet()) {
			Direction newDirection = entry.getValue();
			Casella casellaBersaglio = CasellaManager.getCasellaAdiacente(casellaDiTesta, newDirection);
			// per i cappi
			if(!(
					CasellaManager.getCasellaAdiacente(casellaBersaglio, new Direction(Direction.Dir.UP)).isMortal() &&
					CasellaManager.getCasellaAdiacente(casellaBersaglio, new Direction(Direction.Dir.RIGHT)).isMortal() &&
					CasellaManager.getCasellaAdiacente(casellaBersaglio, new Direction(Direction.Dir.DOWN)).isMortal() &&
					CasellaManager.getCasellaAdiacente(casellaBersaglio, new Direction(Direction.Dir.LEFT)).isMortal()
					)) {
				availableDirections.put(entry.getKey(), entry.getValue());
			}
		}
		return availableDirections;
	}

	private HashMap<String, Direction> rimuoviDirezioneAggrovigliamento(HashMap<String, Direction> direzioni) {
		HashMap<String, Direction> newDir = new HashMap<String, Direction>(direzioni);
		if(!direzioni.containsKey(FORWARD) && direzioni.size()>1) {
			for(String key:direzioni.keySet()) {
				Casella targetCasella = CasellaManager.getCasellaAdiacente(this.getCasellaDiTesta(),direzioni.get(key));
				if(this.ultimaSterzata.equals(key) && !targetCasella.isMortal()) {
					newDir.remove(key);
				}
			}
		}
		return newDir;
	}
	
	private Direction getNewDirection(HashMap<String, Direction> direzioniDisponibili,
			HashMap<String, Direction> direzioniConCiboImmediate,
			HashMap<String, Direction> direzioniConCiboVicine, 
			HashMap<String, Direction> direzioniConCiboMedie, 
			HashMap<String, Direction> direzioniConCiboLontane,
			HashMap<String, Direction> direzioniPortali) {

		Direction dir = getRandomDirection(direzioniConCiboImmediate);
		if(dir != null) return dir;
		
		dir = getRandomDirection(direzioniConCiboVicine);
		if(dir != null) return dir;
		
		dir = getRandomDirection(direzioniConCiboMedie);
		if(dir != null) return dir;
		
		dir = getRandomDirection(direzioniConCiboLontane);
		if(dir != null) return dir;
		
		dir = getRandomDirection(direzioniPortali);
		if(dir != null) return dir;
		
		dir = getRandomDirection(direzioniDisponibili);
		if(dir != null) return dir;
		
		return this.getDirezione();
		
	}
	
	private HashMap<String, Direction> getDirezioniConCibo(HashMap<String, Direction> direzioni, int depth) {
		HashMap<String, Direction> availableDirections = new HashMap<String, Direction> ();
		for(Entry<String, Direction> entry: direzioni.entrySet()) {
			Casella startingCell = CasellaManager.getCasellaAdiacente(this.getCasellaDiTesta(), entry.getValue());
			if(controllaCibo(startingCell, entry.getValue(), depth)) {
				availableDirections.put(entry.getKey(), entry.getValue());
			}
		}
		return availableDirections;
	}
	
	private HashMap<String, Direction> getDirezioniPortali(HashMap<String, Direction> direzioni) {
		HashMap<String, Direction> availableDirections = new HashMap<String, Direction> ();
		for(Entry<String, Direction> entry: direzioni.entrySet()) {
			Stanza stanzaPortale = getStanzaDelPortaleInDirezione(this.getCasellaDiTesta(), entry.getValue(), DIMENSIONE_STANZA_DEFAULT);
			if(stanzaPortale != null) {
				availableDirections.put(entry.getKey(), entry.getValue());
			}
		}
		return availableDirections;
	}
	
	private boolean controllaCibo(Casella casella, Direction dir, int remainingJumps) {
		if(casella.isFood()) return true; 
		if(casella.isMortal()) return false;
		Casella casellaSuccessiva = CasellaManager.getCasellaAdiacente(casella, dir);
		if(!casella.getStanza().equals(casellaSuccessiva.getStanza())) return false;
		if(remainingJumps>0) {
			return controllaCibo(casellaSuccessiva, dir, remainingJumps-1);
		} else {
			return false;
		}
	}
	
	private Stanza getStanzaDelPortaleInDirezione(Casella casella, Direction dir, int range) {
		Casella casellaSuccessiva = CasellaManager.getCasellaAdiacente(casella, dir);
		if(!casella.getStanza().equals(casellaSuccessiva.getStanza())) return casellaSuccessiva.getStanza();
		if(range <=0 || casellaSuccessiva.isMortal()) return null;
        return getStanzaDelPortaleInDirezione(casellaSuccessiva, dir, range-1);
	}
	
	private Direction getRandomDirection(HashMap<String, Direction> direzioni) {
		
		if(direzioni.size()==0) return null;

		if(Utility.truePercentage(95) && direzioni.containsKey(FORWARD)) {
			return direzioni.get(FORWARD);
		} else if (Utility.truePercentage(50) && direzioni.containsKey(RIGHT)) {
			return direzioni.get(RIGHT);
		} else if (direzioni.containsKey(LEFT)){
			return direzioni.get(LEFT);
		}
		
		if(direzioni.containsKey(FORWARD)) {
			return direzioni.get(FORWARD);
		} else if (direzioni.containsKey(RIGHT)) {
			return direzioni.get(RIGHT);
		} else if (direzioni.containsKey(LEFT)){
			return direzioni.get(LEFT);
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
