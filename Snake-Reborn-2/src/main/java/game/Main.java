package game;

import static constants.GeneralConstants.FOOD_SPAWN_JOB_TIMER;
import static constants.GeneralConstants.SNAKE_SPAWN_JOB_TIMER;
import static constants.GeneralConstants.TICK_TIME_EASY;
import static constants.GeneralConstants.TICK_TIME_HARD;
import static constants.GeneralConstants.TICK_TIME_MEDIUM;
import static constants.GeneralConstants.GAME_LENGTH_IN_SECONDS;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Rectangle;
import java.io.IOException;
import java.util.List;

import javax.swing.JOptionPane;

import audio.SoundManager;
import client.ClientWindow;
import commands.CommandHandler;
import score.MatchFactory;
import score.ScoreHandler;
import server.model.MatchForGameVisualizer;
import snake.Snake;
import spawn.FoodSpawnManager;
import spawn.SnakeSpawnManager;
import support.OSdetector;
import video.CellRenderOptionWithPosition;
import video.GraphicAdapter;
import video.LeaderBoardCellRenderOption;
import video.GameVisualizer;

public class Main {

	public static void main(String[] args) throws IOException, InterruptedException {
		Game game = null;
		GameVisualizer gameWindow;
		try {
			String clientSettingsFile = ("config"+OSdetector.getPathSeparator()+"userConfig.ini");
			String serverSettingsFile = ("config"+OSdetector.getPathSeparator()+"serverConfig.json");
			ClientWindow client = new ClientWindow(clientSettingsFile, serverSettingsFile);
			gameWindow = new GameVisualizer();
			while(true) {
				game = new Game();
				client.reload(game);
				while(!client.isActionRequired()){
					Thread.sleep(200);
				}
				try {
					client.readClientSettings();
					game.setUpGame();
					setUpGameWindow(game, gameWindow);
					gameWindow.getFrame().setVisible(true);
					startTheGame(game, gameWindow);
				} catch (AWTException e) {
					e.printStackTrace();
				}
				gameWindow.getFrame().setVisible(false);
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Impossibile avviare Snake Reborn 2:\nError details: " + e.getMessage());
			e.printStackTrace();
		}
	}

	public static void startTheGame(Game game, GameVisualizer gameWindow) throws AWTException, InterruptedException, IOException {
		CommandHandler commandHandler = new CommandHandler(game, gameWindow);
		game.setCommandHandler(commandHandler);
		SoundManager.playMusicLoop();
		startGameLoop(game, gameWindow);
		SoundManager.stopAlert();
		SoundManager.stopMusic();
	}

	private static void startGameLoop(Game game, GameVisualizer gameWindow) throws AWTException, InterruptedException {
		FoodSpawnManager.spawnFoodInTheMap(game.getMap());
		gameWindow.repaint();
		Thread.sleep(1000);
		SoundManager.playSpawnSound();
		int cycleCount=0;
		long waitFor;
		long currentTimestamp;	
		long tickTime = getTickTime(game);
		int fps = (int)(1000/tickTime);
		int foodRespawn = (int)(FOOD_SPAWN_JOB_TIMER*fps);
		int snakeRespawn = (int)(SNAKE_SPAWN_JOB_TIMER*fps);
		Rectangle gameWindowSize = gameWindow.getBounds();
		
		long startTimestamp = System.currentTimeMillis(); 
		long nextCycleTargetTimestamp = startTimestamp;
		game.setStartTimestamp(startTimestamp);
		boolean showEndGameStatistics = false;
		
		long latencyCounter = 0;
		long latencyAcc = 0;
		long latency = 0;
		long maxLatency = 0;

		while(game.isInGame()) {
			startTimestamp = nextCycleTargetTimestamp;
			nextCycleTargetTimestamp = startTimestamp + tickTime;
			cycleCount++;
			
			if(!game.isEndlessMode() && System.currentTimeMillis()>=game.getStartTimestamp()+(GAME_LENGTH_IN_SECONDS*1000)) {
				// game end
				showEndGameStatistics = true;
				break;
			}

			game.executeMoves();
			
			spawnJob(game, cycleCount, foodRespawn, snakeRespawn);
			
			setUpGameWindow(game, gameWindow);
			
			gameWindow.paintImmediately(gameWindowSize);
			
			// latency monitoring begin
			latencyCounter++;
			latency = System.currentTimeMillis()-startTimestamp; 
			latencyAcc += latency;
			if(latency > maxLatency) {
				maxLatency = latency;
			}
			if(cycleCount%fps==0) { // every seconds
				long latencyAvg = latencyAcc/latencyCounter;
				System.out.println("latency: " + maxLatency + "ms (max) - " + latencyAvg +"ms (avg)");
				latencyAcc=0;
				latencyCounter=0;
				maxLatency=0;
			}
			// latency compensation
			currentTimestamp = System.currentTimeMillis();
			waitFor = nextCycleTargetTimestamp - currentTimestamp;
			if (waitFor>0) {
				Thread.sleep(waitFor);
			} else {
				System.out.println("lag detected!");
			}
		}
		if(showEndGameStatistics) {
			SoundManager.playGameEndSound();
			SoundManager.stopAlert();
			SoundManager.stopMusic();
			showEndGameStatistics(game, gameWindow, gameWindowSize);
		}
	}

	private static void showEndGameStatistics(Game game, GameVisualizer gameWindow, Rectangle gameWindowSize)
			throws InterruptedException {
		game.setInGame(true);
		setUpGameWindowForEndGameStatistics(game, gameWindow);
		gameWindow.paintImmediately(gameWindowSize);
		ScoreHandler.sendScore(game);
		while(game.isInGame()) {
			game.getCommandHandler().executeCommandInLeaderboardWindow();
			Thread.sleep(200);
		}
		gameWindow.setShowEndGameStatistics(false);
	}

	private static void setUpGameWindow(Game game, GameVisualizer gameWindow) {
		Snake p1 = game.getSnakePlayer1();
		String message = null;
		if(!p1.isAlive()) {
			if(!p1.canRespawn()) {
				message = "Respawn in: " + p1.getRespawnSecondsLeft() + " seconds";
			} else {
				message = "Press ENTER to respawn";
			}
		}
		MatchForGameVisualizer match = MatchFactory.buildMatchForGameVisualizer(game);
		Color backgroundColor = game.getMap().getBackgroundColor();
		int secondsLeft = -1;
		if(!game.isEndlessMode()) {
			if(game.getStartTimestamp()==0) {
				secondsLeft = GAME_LENGTH_IN_SECONDS;
			} else {
				int currentSecond = (int)(System.currentTimeMillis()/1000);
				int endSecond = (int)(game.getStartTimestamp()/1000 + GAME_LENGTH_IN_SECONDS);
				secondsLeft = endSecond - currentSecond;
				if(secondsLeft<=10 && game.isEndGameAlert()) {
					game.setEndGameAlert(false);
					SoundManager.playAlertSoundLoop();
				}
			}
		}
		List<CellRenderOptionWithPosition> positionToCellRenderOption = GraphicAdapter.getCellRenderOptionWithPosition(game);
		if(game.isShowInterface()) {
			List<LeaderBoardCellRenderOption> leaderboard = GraphicAdapter.getLeaderBoardMap(game);
			gameWindow.setUpVisualizationWithInterface(backgroundColor, positionToCellRenderOption, leaderboard, match, message, secondsLeft);
		} else {
			gameWindow.setUpVisualizationWithoutInterface(backgroundColor, positionToCellRenderOption, match, message, secondsLeft);
		}
	}
	
	private static void setUpGameWindowForEndGameStatistics(Game game, GameVisualizer gameWindow) {
		List<LeaderBoardCellRenderOption> leaderboard = GraphicAdapter.getLeaderBoardMapForEndGame(game);
		Color backgroundColor = Color.black;
		gameWindow.setUpGameWindowForEndGameStatistics(backgroundColor, leaderboard);
	}

	private static long getTickTime(Game game) {
		long tick = 1000;
		if(game.getGameSpeed()==1) {
			tick = TICK_TIME_EASY;
		} else if(game.getGameSpeed()==2) {
			tick = TICK_TIME_MEDIUM;
		} else if(game.getGameSpeed()==3) {
			tick = TICK_TIME_HARD;
		}
		return tick;
	}

	private static void spawnJob(Game game, int cycleCount, int foodRespawn, int snakeRespawn) {
		if((cycleCount%foodRespawn)==0){
			FoodSpawnManager.spawnFoodInTheMap(game.getMap());
		}
		if((cycleCount%snakeRespawn==0)){
			SnakeSpawnManager.reviveOneBotSnake(game);
		}
	}

}