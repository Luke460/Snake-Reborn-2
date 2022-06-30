package game;

import static support.Costanti.TICK_TIME_EASY;
import static support.Costanti.TICK_TIME_MEDIUM;
import static support.Costanti.TICK_TIME_HARD;
import static support.Costanti.TEMPO_RIPOPOLAMENTO_CIBO;
import static support.Costanti.TEMPO_RIPOPOLAMENTO_SERPENTI_BOT;

import java.awt.AWTException;
import java.io.IOException;
import java.util.List;

import javax.swing.JOptionPane;

import audio.GestoreSuoni;
import client.VisualizzatoreClient;
import commands.GestoreComandi;
import spawn.PopolatoreCibo;
import spawn.PopolatoreSerpenti;
import video.CellRenderOptionWithPosition;
import video.GraphicAdapter;
import video.LeaderBoardCellRenderOption;
import video.ScoreInfo;
import video.GameVisualizer;

public class Main {

	public static void main(String[] args) throws IOException, InterruptedException {
		Partita game;
		GameVisualizer gameWindow;
		try {
			VisualizzatoreClient client = new VisualizzatoreClient();
			gameWindow = new GameVisualizer();
			while(true) {
				game = new Partita();
				client.rileggi(game);
				while(!client.isPremuto()){ // viene "sbloccato dal Listener" (busy waiting)
					Thread.sleep(250);
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
		GestoreSuoni.playMusicaInLoop();
		cominciaIlGioco(game, gameWindow);
		GestoreSuoni.silenziaMusica();
	}

	private static void cominciaIlGioco(Partita game, GameVisualizer gameWindow) throws AWTException, InterruptedException {
		PopolatoreCibo.aggiungiCiboNellaMappa(game.getMappa());
		gameWindow.repaint();
		Thread.sleep(1000);
		GestoreSuoni.playSpawnSound();
		int contaCicli=0;

		long aspettaPer;

		long oraInizioAlgoritmo = System.currentTimeMillis(); 
		long oraProgrammataDiRipresa = oraInizioAlgoritmo;
		long oraCorrente;	
		long tickTime = getTickTime(game);
		int fps = (int)(1000/tickTime);
		int foodRespawn = (int)(TEMPO_RIPOPOLAMENTO_CIBO*fps);
		int snakeRespawn = (int)(TEMPO_RIPOPOLAMENTO_SERPENTI_BOT*fps);

		while(game.isInGame()) {
			oraInizioAlgoritmo = oraProgrammataDiRipresa;
			oraProgrammataDiRipresa = oraInizioAlgoritmo + tickTime;
			contaCicli++;

			game.eseguiTurni();
			
			spawnJob(game, contaCicli, foodRespawn, snakeRespawn);
			
			setUpGameWindow(game, gameWindow);
			
			gameWindow.repaint();
			
			if(contaCicli%fps==0) {
				long latency = System.currentTimeMillis()-oraInizioAlgoritmo;
				System.out.println("latency: " + latency +" ms");
			}

			oraCorrente = System.currentTimeMillis();
			aspettaPer = oraProgrammataDiRipresa - oraCorrente;
			if (aspettaPer>0) {
				Thread.sleep(aspettaPer);
			} else {
				System.out.println("lag detected!");
			}
		}
	}

	private static void setUpGameWindow(Partita game, GameVisualizer gameWindow) {
		List<CellRenderOptionWithPosition> positionToCellRenderOption = GraphicAdapter.getCellRenderOptionWithPosition(game);
		List<LeaderBoardCellRenderOption> leaderboard = null;
		ScoreInfo scoreInfo = GraphicAdapter.getScoreInfo(game);
		if(game.isShowLeaderboard()) {
			leaderboard = GraphicAdapter.getLeaderBoardMap(game);
		}
		gameWindow.setUpVisualization(positionToCellRenderOption, leaderboard, scoreInfo);
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
			System.out.println("adding food");
			PopolatoreCibo.aggiungiCiboNellaMappa(game.getMappa());
		}

		if((contaCicli%snakeRespawn==0)){
			System.out.println("ai snake respawn");
			PopolatoreSerpenti.provaAResuscitareUnSerpenteBot(game);
		}
	}

}