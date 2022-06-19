package game;

import static supporto.Costanti.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

import gestoreComandi.GestoreComandi;
import gestorePunteggi.GestorePunteggi;
import loaders.CaricatoreMappa;
import serpenti.EasyBotSnake;
import serpenti.HardBotSnake;
import serpenti.InsaneBotSnake;
import serpenti.MediumBotSnake;
import serpenti.PlayerSnake;
import serpenti.Snake;
import server.client.Client;
import supporto.OSdetector;
import terrenoDiGioco.Mappa;
import terrenoDiGioco.MappaManager;
import terrenoDiGioco.Stanza;

public class Partita {

	private HashMap<String, Snake> serpentiVivi;
	private HashMap<String, Snake> serpentiMorti;
	private Snake serpentePlayer1;
	private String nomePlayer1;
	private Mappa mappa;
	private int sequenzialeSerpentiBot;
	private boolean ilGiocatoreHaFattoLaMossa;
	private int livello;
	private int fattorePopolazione;
	private int vecchioRecord;
	private UserLocal userLocal;
	private boolean ospite;
	private Client client;
	private GestoreComandi gestoreComandi;
	private boolean inGame;
	private boolean modPcVecchio;
	private Stanza stanzaDiSpawn;

	public Partita() throws IOException {
		GestorePunteggi.inizializza(this);
		this.ilGiocatoreHaFattoLaMossa = false;
		this.serpentiVivi = new HashMap<String, Snake>();
		this.serpentiMorti = new HashMap<String, Snake>();
		this.mappa = CaricatoreMappa.caricaFile(PATH_MAPPE + OSdetector.getPathSeparator() + MAP_FILE_NAME, PATH_STANZE);
		this.sequenzialeSerpentiBot = 0;
		this.inGame = true;
		this.stanzaDiSpawn = MappaManager.getStanzaCasualeLiberaPerSpawn(this.mappa, this.serpentiVivi, null);
	}

	public void ImpostaPartita() {
		// un solo giocatore
		if(!ospite)this.vecchioRecord = GestorePunteggi.getRecord();
		this.nomePlayer1 = NOME_PLAYER_1;
		this.serpentePlayer1 = new PlayerSnake(this.nomePlayer1, this.stanzaDiSpawn, VITA_SERPENTE_DEFAULT,this);
		//Just for test
		//Skill skill = new Skill(100,100,100,100);
		//this.serpentePlayer1 = new CustomBotSnake(this.nomePlayer1, this.stanzaDiSpawn, VITA_SERPENTE_DEFAULT,this, skill);
		this.serpentiVivi.put(this.nomePlayer1, this.serpentePlayer1);
	}

	public boolean inserisciBot(String classe){
		Stanza stanza = MappaManager.getStanzaCasualeLiberaPerSpawn(this.mappa, this.serpentiVivi, null);
		if(stanza!=null){
			Snake bot = null;
			if(classe.equals(EasyBotSnake.class.getSimpleName())){
				bot = new EasyBotSnake("bot"+sequenzialeSerpentiBot, stanza,VITA_SERPENTE_DEFAULT,this);
			} else if(classe.equals(MediumBotSnake.class.getSimpleName())){
				bot = new MediumBotSnake("bot"+sequenzialeSerpentiBot, stanza, VITA_SERPENTE_DEFAULT,this);
			} else if(classe.equals(HardBotSnake.class.getSimpleName())){
				bot = new HardBotSnake("bot"+sequenzialeSerpentiBot, stanza, VITA_SERPENTE_DEFAULT,this);
			} else if(classe.equals(InsaneBotSnake.class.getSimpleName())){
				bot = new InsaneBotSnake("bot"+sequenzialeSerpentiBot, stanza, VITA_SERPENTE_DEFAULT,this);
			}
			this.serpentiVivi.put("bot"+sequenzialeSerpentiBot, bot);
			sequenzialeSerpentiBot++;
			return true;
		}
		return false;
	}

	public void eseguiTurni() {
		this.gestoreComandi.eseguiComando();
		Iterator<Snake> iteratore = this.getSerpentiVivi().values().iterator();
		while(iteratore.hasNext()){
			Snake s = iteratore.next();
			if(s.isVivo()){
				s.scegliNuovaDirezione();
				s.sposta();
			} else {
				this.getSerpentiMorti().put(s.getNome(),s);
				iteratore.remove();
			}
		}
	}

	public HashMap<String, Snake> getSerpentiVivi() {
		return serpentiVivi;
	}

	public void setSerpenti(HashMap<String, Snake> serpenti) {
		this.serpentiVivi = serpenti;
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
		if(this.serpentePlayer1.isVivo()) {
			return this.serpentiVivi.size() -1;
		} else {
			return this.serpentiVivi.size();
		}
	}

	public int getLivello() {
		return livello;
	}

	public void setLivello(int livello) {
		this.livello = livello;
	}

	public int getFattorePopolazione() {
		return fattorePopolazione;
	}

	public void setFattorePopolazione(int i) {
		this.fattorePopolazione = i;
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

	public void setModPcLento(boolean selected) {
		this.modPcVecchio = selected;		
	}
	
	public boolean isModPcLento() {
		return this.modPcVecchio;
	}
	
	public HashMap<String, Snake> getSerpentiMorti() {
		return serpentiMorti;
	}
	
	public void setSerpentiMorti(HashMap<String, Snake> serpentiMorti) {
		this.serpentiMorti = serpentiMorti;
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

}
