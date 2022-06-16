package serpenti;

import static supporto.Costanti.*;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map.Entry;

import audio.GestoreSuoni;
import game.Partita;
import popolatori.PopolatoreCibo;
import supporto.Direction;
import supporto.Posizione;
import terrenoDiGioco.Casella;
import terrenoDiGioco.Stanza;

import java.util.Objects;
import java.util.TreeMap;

public abstract class Snake {

	private LinkedList<Casella> caselle;
	private String nome;
	private Direction direzione;
	private Casella casellaDiTesta;
	private int ciboPreso;
	private int numeroUccisioni;
	private long istanteDiNascita;
	private long tempoSopravvivenza;
	private int hpPreMorte;
	private Partita partita;
	private char statoCaselleDefault;
	
	public char getStatoCaselleDefault() {
		return statoCaselleDefault;
	}

	public void setStatoCaselleDefault(char statoCaselleDefault) {
		this.statoCaselleDefault = statoCaselleDefault;
		for(Casella c:this.getCaselle()){
			c.setStato(statoCaselleDefault);		
		}
	}

	public Snake(LinkedList<Casella> caselle) {
		this.caselle = caselle;
	}

	public Snake(String nome, Stanza stanza, int vitaIniziale, Partita partita) {
		this.partita = partita;
		this.nome=nome;
		this.resettaSerpente(stanza, vitaIniziale);
	}

	public Stanza getStanzaCorrente() {
		return this.caselle.getFirst().getStanza();
	}

	public Casella getCasellaDiTesta(){
		return this.casellaDiTesta;
	}

	public Casella getCasellaDiCoda(){
		return this.caselle.getLast();
	}

	public int getHP(){
		return this.casellaDiTesta.getVita();
	}

	public LinkedList<Casella> getCaselle() {
		return caselle;
	}

	public void setCaselle(LinkedList<Casella> caselle) {
		this.caselle = caselle;
	}

	public Stanza getStanza() {
		return this.getCasellaDiTesta().getStanza();
	}

	public void sposta(Direction d){
		this.setDirezione(d);
		Casella vecchiaCasella = this.getCasellaDiTesta();
		Casella nuovaCasella = this.getCasellaDiTesta().getStanza().getCasellaAdiacente(d, this.getCasellaDiTesta());

		if(!nuovaCasella.isMortale()){
			if(nuovaCasella.isCibo()){
				if(nuovaCasella.isTestaDiSerpente()){
					this.incrementaVitaSerpente(QTA_CIBO_TESTA_SERPENTE);
				} else {
					this.incrementaVitaSerpente(QTA_CIBO_BASE);
				}
				nuovaCasella.setCasellaOccupataDalSerpente(this,this.getHP()+1,this.getCasellaDiTesta().getStato());
			} else {
				nuovaCasella.setCasellaOccupataDalSerpente(this,this.getHP(),this.getCasellaDiTesta().getStato());
			}
			int vitaSerpente = this.getHP();


			nuovaCasella.setVita(vitaSerpente);
			this.setCasellaDiTesta(nuovaCasella);

			Iterator<Casella> iteratore = this.getCaselle().iterator();
			while(iteratore.hasNext()){
				Casella temp = iteratore.next();
				temp.decrementaVita(); // la testa non si tocca
				if(temp.isMorta()){
					temp.libera();
					iteratore.remove();
				}
			}
			vecchiaCasella.setTestaDiSerpente(false);
			nuovaCasella.setTestaDiSerpente(true);
			// spostamento
			this.caselle.add(nuovaCasella);
		} else { // casella mortale
			if(nuovaCasella.isOccupataDaSerpente()){
				Snake altroSerpente = nuovaCasella.getSerpente();
				if(altroSerpente.getCasellaDiTesta().getPosizione().equals(nuovaCasella.getPosizione())){
					altroSerpente.muori();
				}
			}
			this.muori();
		}
	}

	public void incrementaVitaSerpente(int qta) {
		this.ciboPreso+=qta;
		for(Casella c : this.getCaselle()){
			if(c.getVita()+qta<=VITA_SERPENTE_MASSIMA){
				c.incrementaVita(qta);
			} else {
				c.setVita(VITA_SERPENTE_MASSIMA);
			}
		}
	}
	
	public void decrementaVitaSerpente(int qta) {
		Iterator<Casella> iteratore = this.getCaselle().iterator();
		while(iteratore.hasNext()){
			Casella c = iteratore.next();
			c.decrementaVita();
			if(c.getVita()<=0) {
				c.libera();
				iteratore.remove();
			}
		}
	}

	public int getHpPreMorte() {
		return hpPreMorte;
	}

	public void setHpPreMorte(int hpPreMorte) {
		this.hpPreMorte = hpPreMorte;
	}

	public void setCasellaDiTesta(Casella nuovaCasella) {
		this.casellaDiTesta = nuovaCasella;
	}

	public void muori(){
		controllaUccisione();
		rilasciaCiboEliberaCaselle();
		hpPreMorte = this.getCasellaDiTesta().getVita();
	}
	
	private void controllaUccisione() {		
		TreeMap<String,Snake> uccisori = new TreeMap<>();
		for(Casella c : this.getCaselle()) {
			HashSet<Casella> caselleAdiacenti = new HashSet<>();
			caselleAdiacenti.add(c.getCasellaAdiacente(new Direction(Direction.Dir.UP)));
			caselleAdiacenti.add(c.getCasellaAdiacente(new Direction(Direction.Dir.RIGHT)));
			caselleAdiacenti.add(c.getCasellaAdiacente(new Direction(Direction.Dir.LEFT)));
			caselleAdiacenti.add(c.getCasellaAdiacente(new Direction(Direction.Dir.DOWN)));
			for(Casella ca : caselleAdiacenti) {
				if(ca.getSerpente()!=null) {
					Snake uccisore = ca.getSerpente();
					if(!uccisore.equals(this)) {
						uccisori.put(ca.getSerpente().getNome(), ca.getSerpente());
					}
				}
			}
		}
		for(Entry<String, Snake> entry:uccisori.entrySet()) {
			entry.getValue().miHaiUcciso();
		}
	}

	protected void rilasciaCiboEliberaCaselle() {
		PopolatoreCibo.rilasciaCiboNelleCaselle(this.caselle);
		rimuoviLinkSerpente(this.caselle);
		this.caselle.clear();
	}

	private void rimuoviLinkSerpente(LinkedList<Casella> caselle) {
		for(Casella c:caselle) {
			c.setSerpente(null);
		}
	}

	abstract public void FaiMossa();

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

	public boolean isVivo() {
		Casella testa = this.getCasellaDiTesta();
		if(testa==null || testa.getVita()<=0) return false;
		return true;
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

	public double getTempoSopravvissuto() {
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

	public void resettaSerpente(Stanza stanza, int vitaResurrezione) {
		this.hpPreMorte = 0;
		this.ciboPreso=0;
		this.numeroUccisioni=0;
		this.istanteDiNascita = System.currentTimeMillis();
		// sempre al centro della stanza (20,20)
		// pre: prima casella libera
		Posizione posizionePrimaCasella = new Posizione(DIMENSIONE_STANZA_DEFAULT/2,DIMENSIONE_STANZA_DEFAULT/2);
		// direzione casuale
		Direction direzioneSerpente = new Direction();
		this.setDirezione(direzioneSerpente);
		Direction direzioneCreazioneCaselle = direzioneSerpente.getInversa();
		// creo la testa del serpente
		this.setCaselle(new LinkedList<Casella>());
		Casella primaCasella = stanza.getCaselle().get(posizionePrimaCasella);
		this.setCasellaDiTesta(primaCasella);
		//lo stato verrà sovrascritto dai creatori specializzati
		primaCasella.setStato(statoCaselleDefault);
		primaCasella.setSerpente(this);
		primaCasella.setTestaDiSerpente(true);
		int vitaCasella = vitaResurrezione;
		primaCasella.setVita(vitaCasella);
		this.getCaselle().add(primaCasella);

		// creo le altre caselle del serpente

		Casella casellaPrecedente = primaCasella;
		for(int i=0; i<vitaResurrezione-1; i++){
			Casella casella = stanza.getCasellaAdiacente(direzioneCreazioneCaselle, casellaPrecedente);
			if(casella!=null&&!casella.isMortale()) {
				casella.setStato(statoCaselleDefault);
				casella.setSerpente(this);
				vitaCasella--;
				casella.setVita(vitaCasella);
				this.getCaselle().add(casella);
				casellaPrecedente = casella;
			} else {
				i=vitaResurrezione;
			}
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
	
}
