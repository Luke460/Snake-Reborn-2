package serpenti;

import static supporto.CostantiConfig.FLAT_CELL;

import java.awt.Color;

import game.Partita;
import terrenoDiGioco.CellRenderOption;
import terrenoDiGioco.Stanza;

public class HardBotSnake extends CustomBotSnake {
	
	public static final CellRenderOption CELL_RENDER_OPTION = new CellRenderOption(FLAT_CELL, Color.red);

	public HardBotSnake(String nome, Stanza stanza, int vitaIniziale, Partita partita) {
		super(nome, stanza, vitaIniziale, partita);
		this.setCellRenderOption(CELL_RENDER_OPTION);
		Skill skill = new Skill(80, 80, 80, 80);
		super.setSkill(skill);
	}

}
