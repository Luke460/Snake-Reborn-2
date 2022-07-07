package snake;

import static constants.MapConstants.DARKER_CELL;

import java.awt.Color;

import audio.SoundManager;
import game.Partita;
import gamefield.Stanza;
import video.CellRenderOption;

public class PlayerSnake extends Snake {
	
	public static final CellRenderOption CELL_RENDER_OPTION = new CellRenderOption(DARKER_CELL, Color.blue);

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
		SoundManager.playTakeSound();
		super.incrementaVitaSerpente(qta);
	}
	
	@Override
	public void muori(){
		SoundManager.playExplodeSound();
		super.muori();
	}

}
