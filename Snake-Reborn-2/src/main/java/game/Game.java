package game;

import static constants.GeneralConstants.NAME_PLAYER_1;
import static constants.GeneralConstants.MIN_HP;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import commands.CommandHandler;
import gamefield.GameMap;
import gamefield.GameMapManager;
import gamefield.Room;
import loaders.GameMapLoader;
import score.SnakeEndGameScoreComparator;
import server.client.Client;
import snake.PlayerSnake;
import snake.Snake;
import spawn.SnakeSpawnManager;

public class Game {

	private HashMap<String, Snake> snakeMap;
	private Snake snakePlayer1;
	private String namePlayer1;
	private GameMap map;
	private int gameSpeed;
	private UserLocal userLocal;
	private boolean guest;
	private Client client;
	private CommandHandler commandHandler;
	private boolean inGame;
	private Room spawnRoom;
	private String mapFileName;
	private boolean showInterface;
	private boolean lowGraphicMode;
	private long startTimestamp;
	private boolean endlessMode;
	private boolean endGameAlert;

	public Game() throws IOException {
		this.snakeMap = new HashMap<String, Snake>();
		this.inGame = true;
	}

	public void setUpGame() throws IOException {
		this.setMap(GameMapLoader.loadFile(mapFileName)); 
		this.spawnRoom = GameMapManager.getRandomFreeRoomForSpawn(this.map, this.snakeMap, null);
		this.namePlayer1 = NAME_PLAYER_1;
		this.endGameAlert = true;
		//Just for test
		//CustomBotSnake testSnake = new CustomBotSnake(this.nomePlayer1, this.stanzaDiSpawn, MIN_HP,this, new Skill(100,100,100,100));
		//this.serpentePlayer1 = testSnake;
		this.snakePlayer1 = new PlayerSnake(this.namePlayer1, this.spawnRoom, MIN_HP,this);
		this.snakeMap.put(this.namePlayer1, this.snakePlayer1);
		this.snakeMap.putAll(SnakeSpawnManager.createBotSnakes(this));
		for(Snake snake:this.snakeMap.values()) {
			snake.setAlive(true);
		}
	}

	public void executeMoves() {
		this.commandHandler.executeCommand();
		Iterator<Snake> iterator = this.getSnakeMap().values().iterator();
		while(iterator.hasNext()){
			Snake s = iterator.next();
			if(s.isAlive()){
				s.chooseNewDirection();
				s.move();
			}
		}
	}

	public HashMap<String, Snake> getSnakeMap() {
		return snakeMap;
	}

	public void setSnakeMap(HashMap<String, Snake> snakeMap) {
		this.snakeMap = snakeMap;
	}

	public void gameOver() {
		this.inGame = false;
	}

	public GameMap getMap() {
		return map;
	}

	public void setMap(GameMap map) {
		this.map = map;
	}
	
	public int getOpponentsNumber(){
		int count = 0;
		for(Snake snake: this.snakeMap.values()) {
			if(snake.isAlive() && !snake.equals(this.getSnakePlayer1())) {
				count++;
			}
		}
		return count;
	}

	public int getGameSpeed() {
		return gameSpeed;
	}

	public void setGameSpeed(int gameSpeed) {
		this.gameSpeed = gameSpeed;
	}

	public UserLocal getUser() {
		return userLocal;
	}

	public void setUser(UserLocal userLocal) {
		this.userLocal = userLocal;
	}

	public boolean isGuest() {
		return guest;
	}

	public void setGuest(boolean guest) {
		this.guest = guest;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}
	
	public void setCommandHandler(CommandHandler g) {
		this.commandHandler = g;
	}

	public boolean isInGame() {
		return inGame;
	}

	public void setInGame(boolean inGame) {
		this.inGame = inGame;
	}
	
	public String getNamePlayer1() {
		return namePlayer1;
	}
	
	public void setNamePlayer1(String nomePlayer1) {
		this.namePlayer1 = nomePlayer1;
	}

	public Snake getSnakePlayer1() {
		return snakePlayer1;
	}

	public Room getSpawnRoom() {
		return spawnRoom;
	}

	public String getMapFileName() {
		return mapFileName;
	}

	public void setMapFileName(String mapFileName) {
		this.mapFileName = mapFileName;
	}

	public boolean isShowInterface() {
		return showInterface;
	}

	public void setShowInterface(boolean showLeaderboard) {
		this.showInterface = showLeaderboard;
	}

	public boolean isLowGraphicMode() {
		return lowGraphicMode;
	}

	public void setLowGraphicMode(boolean lowGraphicMode) {
		this.lowGraphicMode = lowGraphicMode;
	}

	public long getStartTimestamp() {
		return startTimestamp;
	}

	public void setStartTimestamp(long startTimestamp) {
		this.startTimestamp = startTimestamp;
	}

	public CommandHandler getCommandHandler() {
		return this.commandHandler;
	}

	public boolean isEndlessMode() {
		return endlessMode;
	}

	public void setEndlessMode(boolean endlessMode) {
		this.endlessMode = endlessMode;
	}

	public boolean isEndGameAlert() {
		return endGameAlert;
	}

	public void setEndGameAlert(boolean endGameAlert) {
		this.endGameAlert = endGameAlert;
	}

	public int getPlayerPosition() {
		List<Snake> snakes = new ArrayList<>();
		snakes.addAll(this.snakeMap.values());
		snakes.sort(new SnakeEndGameScoreComparator());
		int position = 1;
		for(Snake s:snakes) {
			if(s.equals(this.getSnakePlayer1())) {
				return position;
			}
			position++;
		}
		return position;
	}

}
