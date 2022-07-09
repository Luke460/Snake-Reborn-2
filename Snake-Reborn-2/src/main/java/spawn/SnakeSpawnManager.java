package spawn;

import static constants.GeneralConstants.VITA_SERPENTE_DEFAULT;

import java.util.HashMap;

import audio.SoundManager;
import constants.GeneralConstants;
import game.Partita;
import gamefield.Mappa;
import gamefield.MappaManager;
import gamefield.Stanza;
import snake.CustomBotSnake;
import snake.Snake;

public class SnakeSpawnManager {
	
	public static HashMap<String,Snake> createBotSnakes(Partita game){
		int snakeNumberLimit = getMaxSnakeNumberForSelectedMap(game.getMappa());
	
		int snakeCounter = 0;
		String botNome = "Bot_";
		
		HashMap<String,Snake> snakeList = new HashMap<String,Snake>();

		while((snakeCounter+4)<snakeNumberLimit) {
			snakeCounter = insertBot(game, snakeCounter, botNome, CustomBotSnake.BotLevel.INSANE, snakeList);
			snakeCounter = insertBot(game, snakeCounter, botNome, CustomBotSnake.BotLevel.HARD, snakeList);
			snakeCounter = insertBot(game, snakeCounter, botNome, CustomBotSnake.BotLevel.MEDIUM, snakeList);
			snakeCounter = insertBot(game, snakeCounter, botNome, CustomBotSnake.BotLevel.EASY, snakeList);
		}

		System.out.println("Initial AI snakes number: " + snakeCounter);
		for(Snake snake:snakeList.values()) {
			System.out.println(snake.toString());
		}
		return snakeList;
	}

	private static int insertBot(Partita game, int snakeCounter, String botName, CustomBotSnake.BotLevel botLevel, HashMap<String, Snake> snakeList) {
		String fullName;
		fullName = botName + snakeCounter;
		Stanza room = MappaManager.getStanzaCasualeLiberaPerSpawn(game.getMappa(), game.getSerpenti(), null);
		if(room!=null) {
			snakeList.put(fullName, new CustomBotSnake(fullName, room, GeneralConstants.VITA_SERPENTE_DEFAULT, game, botLevel));
			snakeCounter++;
		} else {
			System.out.println("Unable to insert Bot");
		}
		return snakeCounter;
	}
	
	public static void reviveAllSnakes(Partita game, HashMap<String, Snake> snakeMap) {			
		for(Snake snake: snakeMap.values()) {
			if(!snake.isVivo() && snake.canRespawn()) {
				reviveSnake(game, snake);
			}
		}
	}
	
	public static void reviveAllBotSnakes(Partita game) {	
		for(Snake snake:game.getSerpenti().values()) {
			if(!snake.isVivo() && snake.canRespawn() && !snake.equals(game.getSerpentePlayer1())){
				reviveSnake(game, snake);
			}
		}
	}
	
	public static void reviveSpecificSnake(Partita game, Snake snake) {	
		if(!snake.isVivo() && snake.canRespawn()){
			reviveSnake(game, snake);
		}
	}
	
	public static void reviveOneBotSnake(Partita game) {
		for(Snake snake:game.getSerpenti().values()) {
			if(!snake.isVivo() && snake.canRespawn() && !snake.equals(game.getSerpentePlayer1())){
				reviveSnake(game, snake);
				return; // just one
			}
		}
	}

	public static int getMaxSnakeNumberForSelectedMap(Mappa map) {
		int spawnableRoomsCounter = 0;
		for(Stanza stanza:map.getStanze()) {
			if(stanza.isSpawnEnabled()) {
				spawnableRoomsCounter ++;
			}
		}
		int maxSuggestedSnakeNumber = (int)(map.getStanze().size()*0.75);
		return Math.min(maxSuggestedSnakeNumber, spawnableRoomsCounter);
	}
	
	private static void reviveSnake(Partita game, Snake snake) {
		if(snake.getNome().equals(game.getNomePlayer1())) SoundManager.playSpawnSound();
		int preDeathHp = snake.getHpPreMorte();
		int respawnHp = Math.max(VITA_SERPENTE_DEFAULT, (int)(preDeathHp/2.0));
		Stanza lastRoom = snake.getUltimaStanza();
		Stanza alternativeSpawnRoom = MappaManager.getStanzaCasualeLiberaPerSpawn(game.getMappa(), game.getSerpenti(), lastRoom);
		if(alternativeSpawnRoom!=null) {
			snake.resettaSerpente(alternativeSpawnRoom, respawnHp);
		}
	}
	
}
