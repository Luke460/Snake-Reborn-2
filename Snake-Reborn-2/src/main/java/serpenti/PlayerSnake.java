package serpenti;

import static supporto.Costanti.*;

import audio.GestoreSuoni;
import game.Partita;
import gestorePunteggi.GestorePunteggi;
import terrenoDiGioco.Stanza;

public class PlayerSnake extends Snake {

	public PlayerSnake(String nome, Stanza stanza, int vitaIniziale, Partita partita) {
		super(nome, stanza, vitaIniziale, partita);
		super.setStatoCaselleDefault(CARATTERE_CASELLA_BLUESNAKE);
	}

	@Override
	public void FaiMossa() {
		if(super.isVivo()){
			super.sposta(this.getDirezione());
		}
	}
	
	public String getNomeGiocatore() {
		return super.getNome();
	}

	public void setNomeGiocatore(String nomeGiocatore) {
		super.setNome(nomeGiocatore);
	}
	
	@Override
	public void incrementaVitaSerpente(int qta) {
		super.incrementaVitaSerpente(qta);
	}
	
	@Override
	public void muori(){
		GestoreSuoni.playExplodeSound();
		GestorePunteggi.inviaPunteggio();
		super.setHpPreMorte(this.getCasellaDiTesta().getVita());
		super.rilasciaCiboEliberaCaselle();
		super.getCaselle().clear();
	}

}
