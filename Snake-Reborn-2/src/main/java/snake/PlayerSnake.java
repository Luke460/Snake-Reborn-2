package snake;

import static support.CostantiConfig.FLAT_CELL;

import java.awt.Color;

import audio.GestoreSuoni;
import game.Partita;
import gamefield.CellRenderOption;
import gamefield.Stanza;
import score.GestorePunteggi;

public class PlayerSnake extends Snake {
	
	public static final CellRenderOption CELL_RENDER_OPTION = new CellRenderOption(FLAT_CELL, Color.blue);

	public PlayerSnake(String nome, Stanza stanza, int vitaIniziale, Partita partita) {
		super(nome, stanza, vitaIniziale, partita);
		this.setCellRenderOption(CELL_RENDER_OPTION);
	}

	@Override
	public void scegliNuovaDirezione() {}
	
	public String getNomeGiocatore() {
		return super.getNome();
	}

	public void setNomeGiocatore(String nomeGiocatore) {
		super.setNome(nomeGiocatore);
	}
	
	@Override
	public void incrementaVitaSerpente(int qta) {
		GestoreSuoni.playTakeSound();
		super.incrementaVitaSerpente(qta);
	}
	
	@Override
	public void muori(){
		GestoreSuoni.playExplodeSound();
		GestorePunteggi.inviaPunteggio();
		super.muori();
	}

}