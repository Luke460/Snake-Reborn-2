package terrenoDiGioco;

import serpenti.Snake;
import supporto.Posizione;

public class Casella {

	private Posizione posizione;
	private char stato;
	private Stanza stanza;
	private int vita;
	private boolean isTestaDiSerpente;
	private Snake serpente;

	public Casella(Stanza stanza, Posizione posizione, char stato) {
		this.posizione = posizione;
		this.stato = stato;
		this.vita = -1;
		this.stanza = stanza;
	}
	
	public Posizione getPosizione() {
		return posizione;
	}

	public void setPosizione(Posizione posizione) {
		this.posizione = posizione;
	}

	public char getStato() {
		return stato;
	}

	public void setStato(char stato) {
		this.stato = stato;
	}

	public Stanza getStanza() {
		return stanza;
	}

	public void setStanza(Stanza stanza) {
		this.stanza = stanza;
	}

	public boolean isTestaDiSerpente() {
		return isTestaDiSerpente;
	}

	public void setTestaDiSerpente(boolean isTestaDiSerpente) {
		this.isTestaDiSerpente = isTestaDiSerpente;
	}

	@Override
	public int hashCode(){
		return this.getPosizione().hashCode();
	}

	@Override
	public boolean equals(Object o){
		Casella that = (Casella) o;
		return (this.getPosizione().equals(that.getPosizione())
				&& this.getStanza().equals(that.getStanza()));
	}

	public int getVita() {
		return vita;
	}

	public void setVita(int vita) {
		this.vita = vita;
	}
	
	public Snake getSerpente() {
		return serpente;
	}

	public void setSerpente(Snake serpente) {
		this.serpente = serpente;
	}

	@Override
	public String toString(){
		return "Stanza: " + this.getStanza().getNome() + "  Vita: " + this.getVita() + "  " + this.getPosizione().toString();
	}

}
