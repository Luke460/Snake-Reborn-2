package snake;

import static constants.MapConstants.DARKER_CELL;

import java.awt.Color;

import audio.SoundManager;
import game.Game;
import gamefield.Room;
import video.CellRenderOption;

public class PlayerSnake extends Snake {
	
	public static final CellRenderOption CELL_RENDER_OPTION = new CellRenderOption(DARKER_CELL, Color.blue);

	public PlayerSnake(String name, Room room, int startingHp, Game game) {
		super(name, room, startingHp, game);
		this.setCellRenderOption(CELL_RENDER_OPTION);
	}

	@Override
	public void chooseNewDirection() {}
	
	@Override
	public void increaseHp(int qta) {
		SoundManager.playTakeSound();
		super.increaseHp(qta);
	}
	
	@Override
	public void die(){
		SoundManager.playExplodeSound();
		super.die();
	}
	
	@Override
	public void dieNoKillForSelectedSnake(Snake s) {
		SoundManager.playExplodeSound();
		super.dieNoKillForSelectedSnake(s);
	}

}
