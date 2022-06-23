package serpenti;

import static supporto.Costanti.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map.Entry;

import audio.GestoreSuoni;
import game.Partita;
import popolatori.ComparatoreCasellePerVita;
import popolatori.PopolatoreCibo;
import supporto.Direction;
import supporto.Posizione;
import terrenoDiGioco.Casella;
import terrenoDiGioco.CasellaManager;
import terrenoDiGioco.Stanza;

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
	private char statoCaselleDefault;
	private Stanza ultimaStanza;
	private Casella casellaDiTesta;
	private boolean vivo;
	
	public char getStatoCaselleDefault() {
		return statoCaselleDefault;
	}

	public void setStatoCaselleDefault(char statoCaselleDefault) {
		this.statoCaselleDefault = statoCaselleDefault;
		for(Casella c:this.getCaselle()){
			c.setStato(statoCaselleDefault);		
		}
	}

	public Snake(String nome, Stanza stanza, int vitaIniziale, Partita partita) {
		this.vivo = false;
		this.partita = partita;
		this.nome=nome;
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
		return this.getCasellaDiTesta().getVita();
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

		if(!CasellaManager.isMortale(nuovaCasella)){
			if(CasellaManager.isCibo(nuovaCasella)){
				if(nuovaCasella.isTestaDiSerpente()){
					this.incrementaVitaSerpente(QTA_CIBO_TESTA_SERPENTE);
				} else {
					this.incrementaVitaSerpente(QTA_CIBO_BASE);
				}
				CasellaManager.setCasellaOccupataDalSerpente(nuovaCasella, this,this.getHP(),this.getCasellaDiTesta().getStato());
			} else {
				CasellaManager.setCasellaOccupataDalSerpente(nuovaCasella, this,this.getHP(),this.getCasellaDiTesta().getStato());
			}

			nuovaCasella.setTestaDiSerpente(true);
			vecchiaCasella.setTestaDiSerpente(false);

			// spostamento
			decrementaVitaSerpente();
			this.caselle.add(nuovaCasella);
			this.setCasellaDiTesta(nuovaCasella);
			this.setUltimaStanza(nuovaCasella.getStanza());
		} else { // casella mortale
			if(CasellaManager.isOccupataDaSerpente(nuovaCasella)){
				Snake altroSerpente = nuovaCasella.getSerpente();
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
			c.setVita(c.getVita()-1);
			if(c.getVita()<=0) {
				CasellaManager.libera(c);
				iteratore.remove();
			}
		}
	}
	
	public void incrementaVitaSerpente(int qta) {
		this.setCiboPreso(this.getCiboPreso()+qta);
		for(Casella c : this.getCaselle()){
			if(c.getVita()+qta<=VITA_SERPENTE_MASSIMA){
				c.setVita(c.getVita()+qta);
			} else {
				c.setVita(VITA_SERPENTE_MASSIMA);
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
		hpPreMorte = this.getCasellaDiTesta().getVita();
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
		PopolatoreCibo.rilasciaCiboNelleCaselleDelSerpente(this.caselle);
		rimuoviLinkSerpente(this.caselle);
		this.caselle.clear();
	}

	private void rimuoviLinkSerpente(LinkedList<Casella> caselle) {
		for(Casella c:caselle) {
			c.setSerpente(null);
		}
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
		Posizione posizionePrimaCasella = new Posizione(DIMENSIONE_STANZA_DEFAULT/2,DIMENSIONE_STANZA_DEFAULT/2);
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
		primaCasella.setStato(this.statoCaselleDefault);
		primaCasella.setSerpente(this);
		primaCasella.setTestaDiSerpente(true);
		int vitaCasella = vitaResurrezione;
		primaCasella.setVita(vitaCasella);
		this.getCaselle().add(primaCasella);

		// creo le altre caselle del serpente

		Casella casellaPrecedente = primaCasella;
		for(int i=0; i<vitaResurrezione-1; i++){
			Casella casella = CasellaManager.getCasellaAdiacente(casellaPrecedente, direzioneCreazioneCaselle);
			if(!CasellaManager.isMortale(casella)) {
				casella.setStato(statoCaselleDefault);
				casella.setSerpente(this);
				vitaCasella--;
				casella.setVita(vitaCasella);
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
