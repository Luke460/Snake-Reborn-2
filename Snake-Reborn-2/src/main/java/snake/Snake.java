package snake;

import static support.Costanti.*;
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
import gamefield.CellRenderOption;
import gamefield.Direction;
import gamefield.Position;
import gamefield.Stanza;
import spawn.ComparatoreCasellePerVita;
import spawn.PopolatoreCibo;

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
	
	public static final CellRenderOption DEFAULT_CELL_RENDER_OPTION = new CellRenderOption(FLAT_CELL, Color.gray);

	public Snake(String nome, Stanza stanza, int vitaIniziale, Partita partita) {
		this.vivo = false;
		this.partita = partita;
		this.nome=nome;
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
				if(nuovaCasella.getFoodAmount()==QTY_SPECIAL_FOOD){
					this.incrementaVitaSerpente(QTY_SPECIAL_FOOD);
				} else {
					this.incrementaVitaSerpente(QTY_STANDARD_FOOD);
				}
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

	@Override
	public String toString(){
		return this.getNome() + " \t " + this.getClass().getSimpleName() + " \t " + (this.getHP()*MOLTIPLICATORE_PUNTEGGIO_CIBO);
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
		this.vivo = true;
		this.hpPreMorte = 0;
		this.ciboPreso=0;
		this.numeroUccisioni=0;
		this.istanteDiNascita = System.currentTimeMillis();
		// sempre al centro della stanza (20,20)
		// pre: prima casella libera
		Position posizionePrimaCasella = new Position(DIMENSIONE_STANZA_DEFAULT/2,DIMENSIONE_STANZA_DEFAULT/2);
		// direzione casuale
		Direction direzioneSerpente = new Direction();
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
	
}
