package snake;

import static support.CostantiConfig.DARKER_CELL;

import java.awt.Color;

import game.Partita;
import gamefield.CellRenderOption;
import gamefield.Stanza;

public class EasyBotSnake extends CustomBotSnake {
	
	public static final CellRenderOption CELL_RENDER_OPTION = new CellRenderOption(DARKER_CELL, Color.green);

	public EasyBotSnake(String nome, Stanza stanza, int vitaIniziale, Partita partita) {
		super(nome, stanza, vitaIniziale, partita);
		super.setCellRenderOption(CELL_RENDER_OPTION);
		Skill skill = new Skill(25, 25, 25, 25);
		super.setSkill(skill);
	}

}
