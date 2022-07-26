package spawn;

import static constants.GeneralConstants.MIN_HP;

import java.util.HashMap;

import audio.SoundManager;
import constants.GeneralConstants;
import game.Game;
import gamefield.GameMap;
import gamefield.GameMapManager;
import gamefield.Room;
import snake.CustomBotSnake;
import snake.Snake;

public class SnakeSpawnManager {
	
	public static HashMap<String,Snake> createBotSnakes(Game game){
		int snakeNumberLimit = getMaxSnakeNumberForSelectedMap(game.getMap());
	
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

	private static int insertBot(Game game, int snakeCounter, String botName, CustomBotSnake.BotLevel botLevel, HashMap<String, Snake> snakeList) {
		String fullName;
		fullName = botName + snakeCounter;
		Room room = GameMapManager.getRandomFreeRoomForSpawn(game.getMap(), game.getSnakeMap(), null);
		if(room!=null) {
			snakeList.put(fullName, new CustomBotSnake(fullName, room, GeneralConstants.MIN_HP, game, botLevel));
			snakeCounter++;
		} else {
			System.out.println("Unable to insert Bot");
		}
		return snakeCounter;
	}
	
	public static void reviveAllSnakes(Game game, HashMap<String, Snake> snakeMap) {			
		for(Snake snake: snakeMap.values()) {
			if(!snake.isAlive() && snake.canRespawn()) {
				reviveSnake(game, snake);
			}
		}
	}
	
	public static void reviveAllBotSnakes(Game game) {	
		for(Snake snake:game.getSnakeMap().values()) {
			if(!snake.isAlive() && snake.canRespawn() && !snake.equals(game.getSnakePlayer1())){
				reviveSnake(game, snake);
			}
		}
	}
	
	public static void reviveSpecificSnake(Game game, Snake snake) {	
		if(!snake.isAlive() && snake.canRespawn()){
			reviveSnake(game, snake);
		}
	}
	
	public static void reviveOneBotSnake(Game game) {
		for(Snake snake:game.getSnakeMap().values()) {
			if(!snake.isAlive() && snake.canRespawn() && !snake.equals(game.getSnakePlayer1())){
				reviveSnake(game, snake);
				return; // just one
			}
		}
	}

	public static int getMaxSnakeNumberForSelectedMap(GameMap map) {
		int spawnableRoomsCounter = 0;
		for(Room room:map.getRooms()) {
			if(room.isSpawnEnabled()) {
				spawnableRoomsCounter ++;
			}
		}
		int maxSuggestedSnakeNumber = (int)(map.getRooms().size()*0.75);
		return Math.min(maxSuggestedSnakeNumber, spawnableRoomsCounter);
	}
	
	private static void reviveSnake(Game game, Snake snake) {
		if(snake.getName().equals(game.getNamePlayer1())) SoundManager.playSpawnSound();
		int preDeathHp = snake.getPreDeathHp();
		int respawnHp = Math.max(MIN_HP, (int)(preDeathHp/2.0));
		Room lastRoom = snake.getLastRoom();
		Room alternativeSpawnRoom = GameMapManager.getRandomFreeRoomForSpawn(game.getMap(), game.getSnakeMap(), lastRoom);
		if(alternativeSpawnRoom!=null) {
			snake.resetSnake(alternativeSpawnRoom, respawnHp);
		}
	}
	
}
