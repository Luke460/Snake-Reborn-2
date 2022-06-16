package game;

import static supporto.Costanti.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

import audio.GestoreSuoni;
import gestoreComandi.GestoreComandi;
import gestorePunteggi.GestorePunteggi;
import serpenti.EasyBotSnake;
import serpenti.HardBotSnake;
import serpenti.InsaneBotSnake;
import serpenti.MediumBotSnake;
import serpenti.PlayerSnake;
import serpenti.Snake;
import server.client.Client;
import supporto.Utility;
import terrenoDiGioco.Mappa;
import terrenoDiGioco.Stanza;

public class Partita {

	private HashMap<String, Snake> serpentiVivi;
	private HashMap<String, Snake> serpentiMorti;
	private String nomePlayer1;
	private Mappa mappa;
	private int numerettoPerSerpentiBot;
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
	private Stanza ultimaStanza;

	public Partita() throws IOException {
		GestorePunteggi.inizializza(this);
		this.ilGiocatoreHaFattoLaMossa = false;
		this.serpentiVivi = new HashMap<String, Snake>();
		this.serpentiMorti = new HashMap<String, Snake>();
		this.mappa = new Mappa("mappa-1");
		this.numerettoPerSerpentiBot = 0;
		this.inGame = true;
	}

	public void ImpostaPartita() {
		// un solo giocatore
		if(!ospite)this.vecchioRecord = GestorePunteggi.getRecord();
		Stanza stanzaCasuale = this.mappa.getStanzaCasualeLibera_controlloSuTutteLeStanze();
		this.nomePlayer1 = NOME_PLAYER_1;
		Snake serpentePlayer1 = new PlayerSnake(this.nomePlayer1, stanzaCasuale, VITA_SERPENTE_DEFAULT,this);
		//Snake serpentePlayer1 = new InsaneBotSnake(this.nomePlayer1, stanzaCasuale, VITA_SERPENTE_DEFAULT,this);
		this.serpentiVivi.put(this.nomePlayer1, serpentePlayer1);
		

	}
	
	public void resuscitaSerpente(Snake s) {
		//if(!s.isVivo()){
			if(s.getNome().equals(this.nomePlayer1)) GestoreSuoni.playSpawnSound();
			int vecchiaVita = s.getHpPreMorte();
			int vitaResurrezione = Utility.massimoTra(VITA_SERPENTE_DEFAULT,(int)(vecchiaVita/2.0));
			Stanza stanza = s.getStanza();
			Stanza stanzaAlternativa = this.mappa.getStanzaCasualeLibera_controlloSuTutteLeStanze();
			if(stanzaAlternativa!=null){
				stanza = stanzaAlternativa;
			}
			this.serpentiVivi.remove(s.getNome());
			s.resettaSerpente(stanza, vitaResurrezione);
			this.serpentiVivi.put(s.getNome(),s);
		//}
	}

	public void inserisciBotAccurato(String classe){
		Stanza stanza = this.mappa.getStanzaCasualeLibera_controlloSuTutteLeStanze();
		if(stanza!=null){
			Snake bot = null;
			if(classe.equals(EasyBotSnake.class.getSimpleName())){
				bot = new EasyBotSnake("bot"+numerettoPerSerpentiBot, stanza,VITA_SERPENTE_DEFAULT,this);
			} else if(classe.equals(MediumBotSnake.class.getSimpleName())){
				bot = new MediumBotSnake("bot"+numerettoPerSerpentiBot, stanza, VITA_SERPENTE_DEFAULT,this);
			} else if(classe.equals(HardBotSnake.class.getSimpleName())){
				bot = new HardBotSnake("bot"+numerettoPerSerpentiBot, stanza, VITA_SERPENTE_DEFAULT,this);
			} else if(classe.equals(InsaneBotSnake.class.getSimpleName())){
				bot = new InsaneBotSnake("bot"+numerettoPerSerpentiBot, stanza, VITA_SERPENTE_DEFAULT,this);
			}
			this.serpentiVivi.put("bot"+numerettoPerSerpentiBot, bot);
			numerettoPerSerpentiBot++;
		}
	}

	// metodi try: solo se si trova una stanza casuale che � anche libera
	public void inserisciBotVeloce(String classe){
		Stanza stanza = this.mappa.getStanzaCasualeLibera_controlloSuStanzaSingolaCasuale();
		if(stanza!=null){
			Snake bot = null;
			if(classe.equals(EasyBotSnake.class.getSimpleName())){
				bot = new EasyBotSnake("bot"+numerettoPerSerpentiBot, stanza,VITA_SERPENTE_DEFAULT,this);
			} else if(classe.equals(MediumBotSnake.class.getSimpleName())){
				bot = new MediumBotSnake("bot"+numerettoPerSerpentiBot, stanza, VITA_SERPENTE_DEFAULT,this);
			} else if(classe.equals(HardBotSnake.class.getSimpleName())){
				bot = new HardBotSnake("bot"+numerettoPerSerpentiBot, stanza, VITA_SERPENTE_DEFAULT,this);
			} else if(classe.equals(InsaneBotSnake.class.getSimpleName())){
				bot = new InsaneBotSnake("bot"+numerettoPerSerpentiBot, stanza, VITA_SERPENTE_DEFAULT,this);
			}
			this.serpentiVivi.put("bot"+numerettoPerSerpentiBot, bot);
			numerettoPerSerpentiBot++;
		}
	}

	public void eseguiTurni() {
		this.gestoreComandi.eseguiComando();
		Iterator<Snake> iteratore = this.getSerpentiVivi().values().iterator();
		while(iteratore.hasNext()){
			Snake s = iteratore.next();
			if(s.isVivo()){
				s.FaiMossa();
			} else {
				this.getSerpentiMorti().put(s.getNome(),s);
				iteratore.remove();
			}
		}
	}
	
	public Snake getSerpentePlayer1(){
		if(this.serpentiVivi.containsKey(nomePlayer1)){
			return this.serpentiVivi.get(nomePlayer1);
		} else {
			return this.serpentiMorti.get(nomePlayer1);
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
	
	public int getPunteggioPlayer1() {
		int punteggio = 0;
		Snake p1 = this.getSerpentePlayer1();
		punteggio += (int) p1.getCiboPreso()*MOLTIPLICATORE_PUNTEGGIO_CIBO*GestorePunteggi.getMoltiplicatorePunteggio();
		return punteggio;
	}
	
	public int getNumeroDiSerpenti(){
		return this.serpentiVivi.size();
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

	public Stanza getStanzaCorrenteGiocatore() {
		if(this.getSerpentePlayer1()!=null && this.getSerpentePlayer1().isVivo()) {
			this.ultimaStanza = this.getSerpentePlayer1().getStanza();
			return this.ultimaStanza;
		}
		return this.ultimaStanza;
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
	
}
