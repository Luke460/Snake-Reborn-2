package game;

import static support.Costanti.NOME_PLAYER_1;
import static support.Costanti.VITA_SERPENTE_DEFAULT;
import static support.Costanti.MOLTIPLICATORE_PUNTEGGIO_CIBO;
import static support.Costanti.MOLTIPLICATORE_PUNTEGGIO_UCCISIONE;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

import commands.GestoreComandi;
import gamefield.Mappa;
import gamefield.MappaManager;
import gamefield.Stanza;
import loaders.CaricatoreMappa;
import score.GestorePunteggi;
import server.client.Client;
import snake.PlayerSnake;
import snake.Snake;
import spawn.PopolatoreSerpenti;

public class Partita {

	private HashMap<String, Snake> serpenti;
	private Snake serpentePlayer1;
	private String nomePlayer1;
	private Mappa mappa;
	private boolean ilGiocatoreHaFattoLaMossa;
	private int livello;
	private int vecchioRecord;
	private UserLocal userLocal;
	private boolean ospite;
	private Client client;
	private GestoreComandi gestoreComandi;
	private boolean inGame;
	private Stanza stanzaDiSpawn;
	private String mapFileName;
	private boolean hardcoreMode;

	public Partita() throws IOException {
		GestorePunteggi.inizializza(this);
		this.ilGiocatoreHaFattoLaMossa = false;
		this.serpenti = new HashMap<String, Snake>();
		this.inGame = true;
	}

	public void ImpostaPartita() throws IOException {
		this.setMappa(CaricatoreMappa.caricaFile(mapFileName)); 
		this.stanzaDiSpawn = MappaManager.getStanzaCasualeLiberaPerSpawn(this.mappa, this.serpenti, null);
		if(!ospite)this.vecchioRecord = GestorePunteggi.getRecord();
		this.nomePlayer1 = NOME_PLAYER_1;
		this.serpentePlayer1 = new PlayerSnake(this.nomePlayer1, this.stanzaDiSpawn, VITA_SERPENTE_DEFAULT,this);
		//Just for test
		//Skill skill = new Skill(100,100,100,100);
		//CustomBotSnake testSnake = new CustomBotSnake(this.nomePlayer1, this.stanzaDiSpawn, VITA_SERPENTE_DEFAULT,this);
		//testSnake.setSkill(skill);
		//this.serpentePlayer1 = testSnake;
		this.serpenti.put(this.nomePlayer1, this.serpentePlayer1);
		this.serpenti.putAll(PopolatoreSerpenti.creaSerpentiBot(this));
		for(Snake snake:this.serpenti.values()) {
			snake.setVivo(true);
		}
	}

	public void eseguiTurni() {
		this.gestoreComandi.eseguiComando();
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
	
	public int getSnakeScore(Snake s) {
		double punteggioCibo = s.getCiboPreso()*MOLTIPLICATORE_PUNTEGGIO_CIBO*GestorePunteggi.getMoltiplicatorePunteggio();
		double punteggioUccisioni = s.getNumeroUccisioni()*MOLTIPLICATORE_PUNTEGGIO_UCCISIONE*GestorePunteggi.getMoltiplicatorePunteggio();
		return (int) (punteggioCibo+punteggioUccisioni);
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

	public int getLivello() {
		return livello;
	}

	public void setLivello(int livello) {
		this.livello = livello;
	}

	public boolean isHardcoreMode() {
		return hardcoreMode;
	}

	public void setHardcoreMode(boolean hardcoreMode) {
		this.hardcoreMode = hardcoreMode;
	}

	public int getVecchioRecord() {
		return vecchioRecord;
	}

	public void setVecchioRecord(int vecchioRecord) {
		this.vecchioRecord = vecchioRecord;
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
	
	public void setGestoreComandi(GestoreComandi g) {
		this.gestoreComandi = g;
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

}
