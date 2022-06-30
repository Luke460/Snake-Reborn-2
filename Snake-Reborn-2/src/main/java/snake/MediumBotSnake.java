package snake;

import static support.CostantiConfig.DARKER_CELL;

import java.awt.Color;

import game.Partita;
import gamefield.Stanza;
import video.CellRenderOption;

public class MediumBotSnake extends CustomBotSnake {
	
	public static final CellRenderOption CELL_RENDER_OPTION = new CellRenderOption(DARKER_CELL, new Color(250, 150, 0));

	public MediumBotSnake(String nome, Stanza stanza, int vitaIniziale, Partita partita) {
		super(nome, stanza, vitaIniziale, partita);
		this.setCellRenderOption(CELL_RENDER_OPTION);
		Skill skill = new Skill(50, 50, 50, 50);
		super.setSkill(skill);
	}

}