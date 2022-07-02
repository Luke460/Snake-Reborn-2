package snake;

import static support.Costanti.VITA_SERPENTE_MASSIMA;
import static support.Costanti.NOME_PLAYER_1;
import static support.Costanti.DIMENSIONE_STANZA_DEFAULT;
import static support.Costanti.MOLTIPLICATORE_PUNTEGGIO_CIBO;
import static support.Costanti.MOLTIPLICATORE_PUNTEGGIO_UCCISIONE;
import static support.Costanti.SNAKE_RESPAWN_CD;
import static support.CostantiConfig.FLAT_CELL;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map.Entry;

import audio.GestoreSuoni;
import game.Partita;
import gamefield.Casella;
import gamefield.CasellaManager;
import gamefield.Direction;
import gamefield.Position;
import gamefield.Stanza;
import score.GestorePunteggi;
import spawn.ComparatoreCasellePerVita;
import spawn.PopolatoreCibo;
import support.Utility;
import video.CellRenderOption;

import java.util.Objects;
import java.util.TreeMap;

public abstract class Snake {

	private LinkedList<Casella> caselle;
	private String nome;
	private Direction direzione;
	private int ciboPreso;
	private int numeroUccisioni;
	private long istanteDiNascita;
	private long tempoSopravvivenza;
	private int hpPreMorte;
	private Partita partita;
	private Stanza ultimaStanza;
	private Casella casellaDiTesta;
	private boolean vivo;
	private CellRenderOption cellRenderOption;
	private int previousScore;
	private long deathTimestamp;
	
	public static final CellRenderOption DEFAULT_CELL_RENDER_OPTION = new CellRenderOption(FLAT_CELL, Color.gray);

	public Snake(String nome, Stanza stanza, int vitaIniziale, Partita partita) {
		this.vivo = false;
		this.partita = partita;
		this.nome=nome;
		this.previousScore=0;
		this.deathTimestamp=-1;
		this.cellRenderOption=DEFAULT_CELL_RENDER_OPTION;
		this.resettaSerpente(stanza, vitaIniziale);
	}

	public Stanza getStanzaCorrente() {
		return getCasellaDiTesta().getStanza();
	}

	public Casella getCasellaDiTesta(){
		return this.casellaDiTesta;
	}

	public Casella getCasellaDiCoda(){
		ArrayList<Casella> caselleOrdinatePerVita = new ArrayList<>(this.caselle);
		ComparatoreCasellePerVita comparator = new ComparatoreCasellePerVita();
		Collections.sort(caselleOrdinatePerVita, comparator);
		return caselleOrdinatePerVita.get(caselleOrdinatePerVita.size()-1);
	}

	public int getHP(){
		return this.getCasellaDiTesta().getHp();
	}

	public LinkedList<Casella> getCaselle() {
		return caselle;
	}

	public void setCaselle(LinkedList<Casella> caselle) {
		this.caselle = caselle;
	}

	public Stanza getStanza() {
		if(this.getCasellaDiTesta()!=null) {
			return this.getCasellaDiTesta().getStanza();
		}
		return null;
	}
	
	public void sposta(){
		Casella vecchiaCasella = this.getCasellaDiTesta();
		Casella nuovaCasella = CasellaManager.getCasellaAdiacente(vecchiaCasella, this.getDirezione());

		if(!nuovaCasella.isMortal()){
			if(nuovaCasella.isFood()){
				this.incrementaVitaSerpente(nuovaCasella.getFoodAmount());
			}
			CasellaManager.setCasellaOccupataDalSerpente(nuovaCasella, this,this.getHP());

			// spostamento
			decrementaVitaSerpente();
			this.caselle.add(nuovaCasella);
			this.setCasellaDiTesta(nuovaCasella);
			this.setUltimaStanza(nuovaCasella.getStanza());
		} else { // casella mortale
			if(nuovaCasella.isSnake()){
				Snake altroSerpente = nuovaCasella.getSnake();
				if(altroSerpente.getCasellaDiTesta().getPosizione().equals(nuovaCasella.getPosizione())){
					altroSerpente.muori();
				}
			}
			this.muori();
		}
	}

	private void decrementaVitaSerpente() {
		Iterator<Casella> iteratore = this.getCaselle().iterator();
		while(iteratore.hasNext()){
			Casella c = iteratore.next();
			c.setHp(c.getHp()-1);
			if(c.getHp()<=0) {
				c.freeCell();
				iteratore.remove();
			}
		}
	}
	
	public void incrementaVitaSerpente(int qta) {
		this.setCiboPreso(this.getCiboPreso()+qta);
		for(Casella c : this.getCaselle()){
			if(c.getHp()+qta<=VITA_SERPENTE_MASSIMA){
				c.setHp(c.getHp()+qta);
			} else {
				c.setHp(VITA_SERPENTE_MASSIMA);
			}
		}
	}

	public int getHpPreMorte() {
		return hpPreMorte;
	}

	public void setHpPreMorte(int hpPreMorte) {
		this.hpPreMorte = hpPreMorte;
	}
	
	public void muori(){
		this.setVivo(false);
		this.deathTimestamp = System.currentTimeMillis();
		hpPreMorte = this.getCasellaDiTesta().getHp();
		controllaUccisione();
		rilasciaCiboEliberaCaselle();
	}
	
	private void controllaUccisione() {		
		TreeMap<String,Snake> uccisori = new TreeMap<>();
		for(Casella c : this.getCaselle()) {
			HashSet<Casella> caselleAdiacenti = new HashSet<>();
			caselleAdiacenti.add(CasellaManager.getCasellaAdiacente(c, new Direction(Direction.Dir.UP)));
			caselleAdiacenti.add(CasellaManager.getCasellaAdiacente(c, new Direction(Direction.Dir.RIGHT)));
			caselleAdiacenti.add(CasellaManager.getCasellaAdiacente(c, new Direction(Direction.Dir.LEFT)));
			caselleAdiacenti.add(CasellaManager.getCasellaAdiacente(c, new Direction(Direction.Dir.DOWN)));
			for(Casella ca : caselleAdiacenti) {
				if(ca.isSnake()) {
					Snake uccisore = ca.getSnake();
					if(!uccisore.equals(this)) {
						uccisori.put(ca.getSnake().getNome(), ca.getSnake());
					}
				}
			}
		}
		for(Entry<String, Snake> entry:uccisori.entrySet()) {
			entry.getValue().miHaiUcciso();
		}
	}

	protected void rilasciaCiboEliberaCaselle() {
		PopolatoreCibo.rilasciaCiboNelleCaselleDelSerpente(this.caselle);
		this.caselle.clear();
	}

	abstract public void scegliNuovaDirezione();

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Direction getDirezione() {
		return direzione;
	}

	public void setDirezione(Direction direzione) {
		this.direzione = direzione;
	}
	
	public int getCiboPreso() {
		return this.ciboPreso;
	}

	public void setCiboPreso(int ciboPreso) {
		this.ciboPreso = ciboPreso;
	}

	public void miHaiUcciso(){
		this.numeroUccisioni++;
		if(this.getNome().equals(NOME_PLAYER_1)){
			GestoreSuoni.playSlainSound();
		}
	}

	public int getNumeroUccisioni() {
		return numeroUccisioni;
	}

	public void setNumeroUccisioni(int numeroUccisioni) {
		this.numeroUccisioni = numeroUccisioni;
	}

	public double getTempoSopravvissutoMillis() {
		if(this.isVivo()){
			this.tempoSopravvivenza = System.currentTimeMillis()-this.istanteDiNascita;
			return tempoSopravvivenza;
		} else {
			return this.tempoSopravvivenza;
		}
	}

	public boolean isTesta(Casella casella){
		if(this.getCasellaDiTesta().equals(casella)) return true;
		return false;
	}
	
	public Partita getPartita() {
		return partita;
	}
	
	public void setPartita(Partita partita) {
		this.partita = partita;
	}

	public void setUltimaStanza(Stanza ultimaStanza) {
		this.ultimaStanza = ultimaStanza;
	}
	
	public Stanza getUltimaStanza() {
		return ultimaStanza;
	}

	public void setCasellaDiTesta(Casella casellaDiTesta) {
		this.casellaDiTesta = casellaDiTesta;
	}
	
	public void resettaSerpente(Stanza stanza, int vitaResurrezione) {
		this.previousScore = (int)(this.getTotalSnakeScore()/2);
		this.vivo = true;
		this.hpPreMorte = 0;
		this.ciboPreso=0;
		this.numeroUccisioni=0;
		this.istanteDiNascita = System.currentTimeMillis();

		// random center spawn
		int deltaXspawn = 0;
		if(Utility.veroAl(50)) deltaXspawn = -1;
		int deltaYspawn = 0;
		if(Utility.veroAl(50)) deltaYspawn = -1;
		int centerPosition = DIMENSIONE_STANZA_DEFAULT/2;
		Position posizionePrimaCasella = new Position(centerPosition+deltaXspawn,centerPosition+deltaYspawn);
		
		// direzione casuale
		Direction direzioneSerpente = getBestSpawnDirection(posizionePrimaCasella, stanza);
		this.setDirezione(direzioneSerpente);
		Direction direzioneCreazioneCaselle = direzioneSerpente.getInversa();
		// creo la testa del serpente
		this.setCaselle(new LinkedList<Casella>());
		this.setUltimaStanza(stanza);
		Casella primaCasella = stanza.getCaselle().get(posizionePrimaCasella);
		this.casellaDiTesta = primaCasella;
		//lo stato verrà sovrascritto dai creatori specializzati
		primaCasella.setSnake(this);
		int vitaCasella = vitaResurrezione;
		primaCasella.setHp(vitaCasella);
		this.getCaselle().add(primaCasella);

		// creo le altre caselle del serpente

		Casella casellaPrecedente = primaCasella;
		for(int i=0; i<vitaResurrezione-1; i++){
			Casella casella = CasellaManager.getCasellaAdiacente(casellaPrecedente, direzioneCreazioneCaselle);
			if(!casella.isMortal()) {
				casella.setSnake(this);
				vitaCasella--;
				casella.setHp(vitaCasella);
				this.getCaselle().add(casella);
				casellaPrecedente = casella;
			} else {
				break;
			}
		}
		
	}
	
	private Direction getBestSpawnDirection(Position firstCellPosition, Stanza room) {
		Casella firstCell = room.getCaselle().get(firstCellPosition);
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
		TreeMap<Integer,Direction> directionToSpace = new TreeMap<>();
		for(Direction dir: dirList) {
			directionToSpace.put(CasellaManager.getNumberOfNonLethalCellsInDirection(firstCell, dir), dir);
		}
		return directionToSpace.get(directionToSpace.lastKey());
	}

	public boolean isVivo() {
		return vivo;
	}

	public void setVivo(boolean vivo) {
		this.vivo = vivo;
	}

	public CellRenderOption getCellRenderOption() {
		return cellRenderOption;
	}

	public void setCellRenderOption(CellRenderOption cellRenderOption) {
		this.cellRenderOption = cellRenderOption;
	}
	
	public double getCurrentGameSnakeScore() {
		double punteggioCibo = this.getCiboPreso()*MOLTIPLICATORE_PUNTEGGIO_CIBO*GestorePunteggi.getMoltiplicatorePunteggio();
		double punteggioUccisioni = this.getNumeroUccisioni()*MOLTIPLICATORE_PUNTEGGIO_UCCISIONE*GestorePunteggi.getMoltiplicatorePunteggio();
		return punteggioCibo+punteggioUccisioni;
	}
	
	public int getTotalSnakeScore() {
		return (int)(this.getCurrentGameSnakeScore() + this.previousScore);
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

	@Override
	public int hashCode() {
		return Objects.hash(nome);
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
		return Objects.equals(nome, other.nome);
	}

	@Override
	public String toString() {
		return "Snake [nome=" + nome + ", ciboPreso=" + ciboPreso + ", numeroUccisioni=" + numeroUccisioni
				+ ", hpPreMorte=" + hpPreMorte + ", vivo=" + vivo + ", getHP()=" + getHP() + ", isVivo()=" + isVivo()
				+ "]";
	}
	
}
