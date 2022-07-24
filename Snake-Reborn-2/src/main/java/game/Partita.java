package game;

import static constants.GeneralConstants.NOME_PLAYER_1;
import static constants.GeneralConstants.VITA_SERPENTE_DEFAULT;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import commands.CommandHandler;
import gamefield.Mappa;
import gamefield.MappaManager;
import gamefield.Stanza;
import loaders.CaricatoreMappa;
import score.SnakeEndGameScoreComparator;
import server.client.Client;
import snake.PlayerSnake;
import snake.Snake;
import spawn.SnakeSpawnManager;

public class Partita {

	private HashMap<String, Snake> serpenti;
	private Snake serpentePlayer1;
	private String nomePlayer1;
	private Mappa mappa;
	private boolean ilGiocatoreHaFattoLaMossa;
	private int gameSpeed;
	private UserLocal userLocal;
	private boolean ospite;
	private Client client;
	private CommandHandler commandHandler;
	private boolean inGame;
	private Stanza stanzaDiSpawn;
	private String mapFileName;
	private boolean showInterface;
	private boolean lowGraphicMode;
	private long startTimestamp;
	private boolean endlessMode;
	private boolean endGameAlert;

	public Partita() throws IOException {
		this.ilGiocatoreHaFattoLaMossa = false;
		this.serpenti = new HashMap<String, Snake>();
		this.inGame = true;
	}

	public void ImpostaPartita() throws IOException {
		this.setMappa(CaricatoreMappa.caricaFile(mapFileName)); 
		this.stanzaDiSpawn = MappaManager.getStanzaCasualeLiberaPerSpawn(this.mappa, this.serpenti, null);
		this.nomePlayer1 = NOME_PLAYER_1;
		this.endGameAlert = true;
		//Just for test
		//CustomBotSnake testSnake = new CustomBotSnake(this.nomePlayer1, this.stanzaDiSpawn, VITA_SERPENTE_DEFAULT,this, new Skill(100,100,100,100));
		//this.serpentePlayer1 = testSnake;
		this.serpentePlayer1 = new PlayerSnake(this.nomePlayer1, this.stanzaDiSpawn, VITA_SERPENTE_DEFAULT,this);
		this.serpenti.put(this.nomePlayer1, this.serpentePlayer1);
		this.serpenti.putAll(SnakeSpawnManager.createBotSnakes(this));
		for(Snake snake:this.serpenti.values()) {
			snake.setVivo(true);
		}
	}

	public void eseguiTurni() {
		this.commandHandler.executeCommand();
		Iterator<Snake> iteratore = this.getSerpenti().values().iterator();
		while(iteratore.hasNext()){
			Snake s = iteratore.next();
			if(s.isVivo()){
				s.scegliNuovaDirezione();
				s.sposta();
			}
		}
	}

	public HashMap<String, Snake> getSerpenti() {
		return serpenti;
	}

	public void setSerpenti(HashMap<String, Snake> serpenti) {
		this.serpenti = serpenti;
	}

	public void gameOver() {
		this.inGame = false;
	}

	public Mappa getMappa() {
		return mappa;
	}

	public void setMappa(Mappa mappa) {
		this.mappa = mappa;
	}
	
	public int getNumeroAvversari(){
		int count = 0;
		for(Snake snake: this.serpenti.values()) {
			if(snake.isVivo() && !snake.equals(this.getSerpentePlayer1())) {
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

	public boolean isIlGiocatoreHaFattoLaMossa() {
		return ilGiocatoreHaFattoLaMossa;
	}

	public void setIlGiocatoreHaFattoLaMossa(boolean ilGiocatoreHaFattoLaMossa) {
		this.ilGiocatoreHaFattoLaMossa = ilGiocatoreHaFattoLaMossa;
	}

	public UserLocal getUtente() {
		return userLocal;
	}

	public void setUtente(UserLocal userLocal) {
		this.userLocal = userLocal;
	}

	public boolean isOspite() {
		return ospite;
	}

	public void setOspite(boolean ospite) {
		this.ospite = ospite;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}
	
	public void setGestoreComandi(CommandHandler g) {
		this.commandHandler = g;
	}

	public boolean isInGame() {
		return inGame;
	}

	public void setInGame(boolean inGame) {
		this.inGame = inGame;
	}
	
	public String getNomePlayer1() {
		return nomePlayer1;
	}
	
	public void setNomePlayer1(String nomePlayer1) {
		this.nomePlayer1 = nomePlayer1;
	}

	public Snake getSerpentePlayer1() {
		return serpentePlayer1;
	}

	public Stanza getStanzaDiSpawn() {
		return stanzaDiSpawn;
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

	public CommandHandler getGestoreComandi() {
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
		snakes.addAll(this.serpenti.values());
		snakes.sort(new SnakeEndGameScoreComparator());
		int position = 1;
		for(Snake s:snakes) {
			if(s.equals(this.getSerpentePlayer1())) {
				return position;
			}
			position ++;
		}
		return position;
	}

}
