package snake;

import static support.CostantiConfig.DARKER_CELL;

import java.awt.Color;

import game.Partita;
import gamefield.Stanza;
import video.CellRenderOption;

public class InsaneBotSnake extends CustomBotSnake {
	
	public static final CellRenderOption CELL_RENDER_OPTION = new CellRenderOption(DARKER_CELL, Color.magenta);

	public InsaneBotSnake(String nome, Stanza stanza, int vitaIniziale, Partita partita) {
		super(nome, stanza, vitaIniziale, partita);
		this.setCellRenderOption(CELL_RENDER_OPTION);
		Skill skill = new Skill(100, 100, 100, 100);
		super.setSkill(skill);
	}

}
