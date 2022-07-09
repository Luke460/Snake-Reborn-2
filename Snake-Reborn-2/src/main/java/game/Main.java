package game;

import static constants.GeneralConstants.TEMPO_RIPOPOLAMENTO_CIBO;
import static constants.GeneralConstants.TEMPO_RIPOPOLAMENTO_SERPENTI_BOT;
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
import client.VisualizzatoreClient;
import commands.GestoreComandi;
import score.MatchFactory;
import score.ScoreHandler;
import server.model.MatchForGameVisualizer;
import snake.Snake;
import spawn.FoodSpawnManager;
import spawn.SnakeSpawnManager;
import video.CellRenderOptionWithPosition;
import video.GraphicAdapter;
import video.LeaderBoardCellRenderOption;
import video.GameVisualizer;

public class Main {

	public static void main(String[] args) throws IOException, InterruptedException {
		Partita game = null;
		GameVisualizer gameWindow;
		try {
			VisualizzatoreClient client = new VisualizzatoreClient();
			gameWindow = new GameVisualizer();
			while(true) {
				game = new Partita();
				client.rileggi(game);
				while(!client.isPremuto()){
					Thread.sleep(200);
				}
				try {
					client.leggiImpostazioniDaUI();
					game.ImpostaPartita();
					setUpGameWindow(game, gameWindow);
					gameWindow.getFrame().setVisible(true);
					avviaIlGioco(game, gameWindow);
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

	public static void avviaIlGioco(Partita game, GameVisualizer gameWindow) throws AWTException, InterruptedException, IOException {
		// lancia un thread che legge i comandi, 
		// SuppressWarnings perchè il compilatore e' stupido
		GestoreComandi gestoreComandi = new GestoreComandi(game, gameWindow);
		game.setGestoreComandi(gestoreComandi);
		SoundManager.playMusicaInLoop();
		cominciaIlGioco(game, gameWindow);
		SoundManager.stopAlert();
		SoundManager.stopMusic();
	}

	private static void cominciaIlGioco(Partita game, GameVisualizer gameWindow) throws AWTException, InterruptedException {
		FoodSpawnManager.spawnFoodInTheMap(game.getMappa());
		gameWindow.repaint();
		Thread.sleep(1000);
		SoundManager.playSpawnSound();
		int contaCicli=0;
		long aspettaPer;
		long oraCorrente;	
		long tickTime = getTickTime(game);
		int fps = (int)(1000/tickTime);
		int foodRespawn = (int)(TEMPO_RIPOPOLAMENTO_CIBO*fps);
		int snakeRespawn = (int)(TEMPO_RIPOPOLAMENTO_SERPENTI_BOT*fps);
		Rectangle gameWindowSize = gameWindow.getBounds();
		
		long oraInizioAlgoritmo = System.currentTimeMillis(); 
		long oraProgrammataDiRipresa = oraInizioAlgoritmo;
		game.setStartTimestamp(oraInizioAlgoritmo);
		boolean showEndGameStatistics = false;
		
		long latencyCounter = 0;
		long latencyAcc = 0;
		long latency = 0;
		long maxLatency = 0;

		while(game.isInGame()) {
			oraInizioAlgoritmo = oraProgrammataDiRipresa;
			oraProgrammataDiRipresa = oraInizioAlgoritmo + tickTime;
			contaCicli++;
			
			if(!game.isEndlessMode() && System.currentTimeMillis()>=game.getStartTimestamp()+(GAME_LENGTH_IN_SECONDS*1000)) {
				// game end
				showEndGameStatistics = true;
				break;
			}

			game.eseguiTurni();
			
			spawnJob(game, contaCicli, foodRespawn, snakeRespawn);
			
			setUpGameWindow(game, gameWindow);
			
			gameWindow.paintImmediately(gameWindowSize);
			
			// latency monitoring begin
			latencyCounter++;
			latency = System.currentTimeMillis()-oraInizioAlgoritmo; 
			latencyAcc += latency;
			if(latency > maxLatency) {
				maxLatency = latency;
			}
			if(contaCicli%fps==0) { // every seconds
				long latencyAvg = latencyAcc/latencyCounter;
				System.out.println("latency: " + maxLatency + "ms (max) - " + latencyAvg +"ms (avg)");
				latencyAcc=0;
				latencyCounter=0;
				maxLatency=0;
			}
			// latency compensation
			oraCorrente = System.currentTimeMillis();
			aspettaPer = oraProgrammataDiRipresa - oraCorrente;
			if (aspettaPer>0) {
				Thread.sleep(aspettaPer);
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

	private static void showEndGameStatistics(Partita game, GameVisualizer gameWindow, Rectangle gameWindowSize)
			throws InterruptedException {
		game.setInGame(true);
		setUpGameWindowForEndGameStatistics(game, gameWindow);
		gameWindow.paintImmediately(gameWindowSize);
		ScoreHandler.sendScore(game);
		while(game.isInGame()) {
			game.getGestoreComandi().eseguiComandoEndGame();
			Thread.sleep(200);
		}
		gameWindow.setShowEndGameStatistics(false);
	}

	private static void setUpGameWindow(Partita game, GameVisualizer gameWindow) {
		Snake p1 = game.getSerpentePlayer1();
		String message = null;
		if(!p1.isVivo()) {
			if(!p1.canRespawn()) {
				message = "Resuscita in: " + p1.getRespawnSecondsLeft() + " secondi";
			} else {
				message = "Premi invio per resuscitare";
			}
		}
		List<CellRenderOptionWithPosition> positionToCellRenderOption = GraphicAdapter.getCellRenderOptionWithPosition(game);
		List<LeaderBoardCellRenderOption> leaderboard = null;
		MatchForGameVisualizer match = MatchFactory.buildMatchForGameVisualizer(game);
		if(game.isShowLeaderboard()) {
			leaderboard = GraphicAdapter.getLeaderBoardMap(game);
		}
		Color backgroundColor = game.getMappa().getBackgroundColor();
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
					SoundManager.playAlertSoundInLoop();
				}
			}
		}
		gameWindow.setUpVisualization(backgroundColor, positionToCellRenderOption, leaderboard, match, message, secondsLeft);
	}
	
	private static void setUpGameWindowForEndGameStatistics(Partita game, GameVisualizer gameWindow) {
		List<LeaderBoardCellRenderOption> leaderboard = GraphicAdapter.getLeaderBoardMapForEndGame(game);
		Color backgroundColor = Color.black;
		gameWindow.setUpGameWindowForEndGameStatistics(backgroundColor, leaderboard);
	}

	private static long getTickTime(Partita game) {
		long tick = 1000;
		if(game.getLivello()==1) {
			tick = TICK_TIME_EASY;
		} else if(game.getLivello()==2) {
			tick = TICK_TIME_MEDIUM;
		} else if(game.getLivello()==3) {
			tick = TICK_TIME_HARD;
		}
		return tick;
	}

	private static void spawnJob(Partita game, int contaCicli, int foodRespawn, int snakeRespawn) {
		if((contaCicli%foodRespawn)==0){
			//long before=System.currentTimeMillis();
			FoodSpawnManager.spawnFoodInTheMap(game.getMappa());
			//long after=System.currentTimeMillis();
			//System.out.println("spawn food job (" + (after-before) + "ms)");
		}

		if((contaCicli%snakeRespawn==0)){
			//long before=System.currentTimeMillis();
			SnakeSpawnManager.reviveOneBotSnake(game);
			//long after=System.currentTimeMillis();
			//System.out.println("spawn bot job (" + (after-before) + "ms)");
		}
	}

}