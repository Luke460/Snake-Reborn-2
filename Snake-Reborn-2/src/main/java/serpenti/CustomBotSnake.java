package serpenti;
import static supporto.Costanti.*;

import java.util.HashMap;
import java.util.Map.Entry;

import game.Partita;
import supporto.Direction;
import supporto.Utility;
import terrenoDiGioco.Casella;
import terrenoDiGioco.CasellaManager;
import terrenoDiGioco.Stanza;

public class CustomBotSnake extends Snake {

	private String ultimaSterzata;
	private Skill skill;
	
	private final static String FORWARD = "FORWARD";
	private final static String RIGHT = "RIGHT";
	private final static String LEFT = "LEFT";

	public CustomBotSnake(String nome, Stanza stanza, int vitaIniziale, Partita partita, Skill skill) {
		super(nome, stanza, vitaIniziale, partita);
		this.skill = skill;
		ultimaSterzata = "NONE";
		for(Casella c:this.getCaselle()){
			c.setStato(CARATTERE_CASELLA_BOT_HARD);		
		}
		super.setStatoCaselleDefault(CARATTERE_CASELLA_BOT_HARD);
	}
	
	public int getSnakeAbilityScore() {
		return this.skill.getSkillScore();
	}

	@Override
	public void sposta(Direction d) {
		if(d.equals(this.getDirezione().getRotatedRightDirection())){
			this.ultimaSterzata = RIGHT;
		} else if (d.equals(this.getDirezione().getRotatedLeftDirection())) {
			this.ultimaSterzata = LEFT;
		}
		super.sposta(d);
	}
	
	@Override
	public void FaiMossa() {
		
		HashMap<String, Direction> direzioni = new HashMap<>();
		
		direzioni.put(FORWARD, super.getDirezione());
		direzioni.put(RIGHT, super.getDirezione().getRotatedRightDirection());
		direzioni.put(LEFT, super.getDirezione().getRotatedLeftDirection());
		
		if(Utility.veroAl(this.skill.getEvadeWallSkill())) {
			direzioni = rimuoviCelleMuro(direzioni);
		}
		
		if(Utility.veroAl(this.skill.getEvadeSnakeSkill())) {
			direzioni = rimuoviCelleSerpenti(direzioni);
		}
		
		if(Utility.veroAl(this.skill.getEvadeWallSkill())) {
			direzioni = rimuoviCelleCappi(direzioni);
		}
		
		if(!Utility.veroAl(this.skill.getCourageSkill())) {
			direzioni = evitaSituazionePericolosa(direzioni);
		}
		
		direzioni = rimuoviDirezioneAggrovigliamento(direzioni);
		
		HashMap<String, Direction> direzioniConCiboImmediate = new HashMap<>();
		HashMap<String, Direction> direzioniConCiboVicine = new HashMap<>();
		HashMap<String, Direction> direzioniConCiboLontane = new HashMap<>();
		HashMap<String, Direction> direzioniPortali = new HashMap<>();
		
		direzioniConCiboImmediate = getDirezioniConCibo(direzioni, 0);
		
		direzioniConCiboVicine = getDirezioniConCibo(direzioni, DIMENSIONE_STANZA_DEFAULT/10);
				
		if(Utility.veroAl(this.skill.getFarmSkill())) {
			direzioniConCiboLontane = getDirezioniConCibo(direzioni, DIMENSIONE_STANZA_DEFAULT);
		}
		
		if(Utility.veroAl((int)(this.skill.getExploreSkill()*0.50))) {
			direzioniPortali = getDirezioniPortali(direzioni);
		}

		Direction newDirection = getNewDirection(direzioni, direzioniConCiboImmediate, direzioniConCiboVicine, direzioniConCiboLontane, direzioniPortali);
		
		sposta(newDirection);
		
	}
	
	private HashMap<String, Direction> evitaSituazionePericolosa(HashMap<String, Direction> direzioni) {
		if(direzioni.size()>1) {		
			Direction direzione = this.getDirezione();
			Casella casella = CasellaManager.getCasellaInDirezione(this.getCasellaDiTesta(), direzione, 1);
			if(CasellaManager.isMuro(casella) || (casella.getSerpente()!=null && casella.getSerpente().equals(this))) {
				if(direzioni.containsKey(FORWARD)) {
					direzioni.remove(FORWARD);
				}
			}
			direzione = this.getDirezione().getRotatedRightDirection();
			casella = CasellaManager.getCasellaInDirezione(this.getCasellaDiTesta(), direzione, 1);
			if(CasellaManager.isMuro(casella) || (casella.getSerpente()!=null && casella.getSerpente().equals(this))) {
				if(direzioni.containsKey(RIGHT)) {
					direzioni.remove(RIGHT);
				}
			}
			direzione = this.getDirezione().getRotatedLeftDirection();
			casella = CasellaManager.getCasellaInDirezione(this.getCasellaDiTesta(), direzione, 1);
			if(CasellaManager.isMuro(casella) || (casella.getSerpente()!=null && casella.getSerpente().equals(this))) {
				if(direzioni.containsKey(LEFT)) {
					direzioni.remove(LEFT);
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
			if(!CasellaManager.isMuro(casellaBersaglio) && !(casellaBersaglio.getSerpente() != null && casellaBersaglio.getSerpente().equals(this))) {
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
			if(!CasellaManager.isOccupataDaSerpente(casellaBersaglio)) {
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
					CasellaManager.isMortale(CasellaManager.getCasellaAdiacente(casellaBersaglio, new Direction(Direction.Dir.UP))) &&
					CasellaManager.isMortale(CasellaManager.getCasellaAdiacente(casellaBersaglio, new Direction(Direction.Dir.RIGHT))) &&
					CasellaManager.isMortale(CasellaManager.getCasellaAdiacente(casellaBersaglio, new Direction(Direction.Dir.DOWN))) &&
					CasellaManager.isMortale(CasellaManager.getCasellaAdiacente(casellaBersaglio, new Direction(Direction.Dir.LEFT)))
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
				if(this.ultimaSterzata.equals(key) && !CasellaManager.isMortale(targetCasella)) {
					newDir.remove(key);
				}
			}
		}
		return newDir;
	}
	
	private Direction getNewDirection(HashMap<String, Direction> direzioniDisponibili,
			HashMap<String, Direction> direzioniConCiboImmediate,
			HashMap<String, Direction> direzioniConCiboVicine, 
			HashMap<String, Direction> direzioniConCiboLontane,
			HashMap<String, Direction> direzioniPortali) {

		Direction dir = getRandomDirection(direzioniConCiboImmediate);
		if(dir != null) return dir;
		
		dir = getRandomDirection(direzioniConCiboVicine);
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
			int actualDepth = depth;
			if(entry.getKey().equals(FORWARD)) {
				actualDepth += 1;
			}
			if(controllaCibo(this.getCasellaDiTesta(), entry.getValue(), actualDepth)) {
				availableDirections.put(entry.getKey(), entry.getValue());
			}
		}
		return availableDirections;
	}
	
	private HashMap<String, Direction> getDirezioniPortali(HashMap<String, Direction> direzioni) {
		HashMap<String, Direction> availableDirections = new HashMap<String, Direction> ();
		for(Entry<String, Direction> entry: direzioni.entrySet()) {
			if(controllaPortale(this.getCasellaDiTesta(), entry.getValue())) {
				availableDirections.put(entry.getKey(), entry.getValue());
			}
		}
		return availableDirections;
	}
	
	private boolean controllaCibo(Casella casella, Direction dir, int remainingJumps) {
		if(CasellaManager.isCibo(casella)) return true; 
		Casella casellaSuccessiva = CasellaManager.getCasellaAdiacente(casella, dir);
		if(!casella.getStanza().equals(casellaSuccessiva.getStanza())) return false;
		if(CasellaManager.isMortale(casellaSuccessiva)) return false;
		if(CasellaManager.isCibo(casellaSuccessiva)) return true;
		if(remainingJumps>0) {
			return controllaCibo(casellaSuccessiva, dir, remainingJumps-1);
		} else {
			return false;
		}
	}
	
	private boolean controllaPortale(Casella casella, Direction dir) {
		Casella casellaSuccessiva = CasellaManager.getCasellaAdiacente(casella, dir);
		if(!casella.getStanza().equals(casellaSuccessiva.getStanza())) return true;
		if(CasellaManager.isMortale(casellaSuccessiva)) return false;
        return controllaPortale(casellaSuccessiva, dir);
	}
	
	private Direction getRandomDirection(HashMap<String, Direction> direzioni) {
		
		if(direzioni.size()==0) return null;

		if(Utility.veroAl(95) && direzioni.containsKey(FORWARD)) {
			return direzioni.get(FORWARD);
		} else if (Utility.veroAl(50) && direzioni.containsKey(RIGHT)) {
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

}
