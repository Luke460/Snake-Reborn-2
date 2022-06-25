package spawn;

import static support.Costanti.VITA_SERPENTE_DEFAULT;

import java.util.HashMap;

import audio.GestoreSuoni;
import game.Partita;
import gamefield.Mappa;
import gamefield.MappaManager;
import gamefield.Stanza;
import snake.EasyBotSnake;
import snake.HardBotSnake;
import snake.InsaneBotSnake;
import snake.MediumBotSnake;
import snake.Snake;
import support.Costanti;
import support.Utility;

public class PopolatoreSerpenti {
	Partita partita;
	
	public static HashMap<String,Snake> creaSerpentiBot(Partita partita){
		int limiteSerpenti = calcolaNumeroMassimoSerpentiMappa(partita.getMappa());
	
		int serpentiInseriti = 0;
		String nomeBot = "Bot_";
		
		HashMap<String,Snake> snakeList = new HashMap<String,Snake>();
		
		if(partita.getLivello()==3) {
			while((serpentiInseriti+4)<limiteSerpenti) {
				serpentiInseriti = insertInsaneBot(partita, serpentiInseriti, nomeBot, snakeList);
				serpentiInseriti = insertHardBot(partita, serpentiInseriti, nomeBot, snakeList);
				serpentiInseriti = insertMediumBot(partita, serpentiInseriti, nomeBot, snakeList);
				serpentiInseriti = insertEasyBot(partita, serpentiInseriti, nomeBot, snakeList);
			}
		} else if (partita.getLivello()==2){
			while((serpentiInseriti+4)<limiteSerpenti) {
				serpentiInseriti = insertHardBot(partita, serpentiInseriti, nomeBot, snakeList);
				serpentiInseriti = insertMediumBot(partita, serpentiInseriti, nomeBot, snakeList);
				serpentiInseriti = insertEasyBot(partita, serpentiInseriti, nomeBot, snakeList);
				serpentiInseriti = insertMediumBot(partita, serpentiInseriti, nomeBot, snakeList);		
			}			
		} else if (partita.getLivello()==1){
			while((serpentiInseriti+4)<limiteSerpenti) {
				serpentiInseriti = insertMediumBot(partita, serpentiInseriti, nomeBot, snakeList);
				serpentiInseriti = insertEasyBot(partita, serpentiInseriti, nomeBot, snakeList);
				serpentiInseriti = insertMediumBot(partita, serpentiInseriti, nomeBot, snakeList);
				serpentiInseriti = insertEasyBot(partita, serpentiInseriti, nomeBot, snakeList);
			}			
		}	
		System.out.println("Initial AI snakes number: " + serpentiInseriti);
		for(Snake snake:snakeList.values()) {
			System.out.println(snake.toString());
		}
		return snakeList;
	}

	private static int insertEasyBot(Partita partita, int serpentiInseriti, String nomeBot,
			HashMap<String, Snake> snakeList) {
		String fullName;
		fullName = nomeBot + serpentiInseriti;
		Stanza stanza = MappaManager.getStanzaCasualeLiberaPerSpawn(partita.getMappa(), partita.getSerpenti(), null);
		if(stanza!=null) {
			snakeList.put(fullName, new EasyBotSnake(fullName, stanza, Costanti.VITA_SERPENTE_DEFAULT, partita));
			serpentiInseriti++;
		} else {
			System.out.println("Unable to insert EasyBot");
		}
		return serpentiInseriti;
	}

	private static int insertMediumBot(Partita partita, int serpentiInseriti, String nomeBot,
			HashMap<String, Snake> snakeList) {
		String fullName;
		fullName = nomeBot + serpentiInseriti;
		Stanza stanza = MappaManager.getStanzaCasualeLiberaPerSpawn(partita.getMappa(), partita.getSerpenti(), null);
		if(stanza!=null) {
			snakeList.put(fullName, new MediumBotSnake(fullName, stanza, Costanti.VITA_SERPENTE_DEFAULT, partita));
			serpentiInseriti++;
		} else {
			System.out.println("Unable to insert MediumBot");
		}
		return serpentiInseriti;
	}

	private static int insertHardBot(Partita partita, int serpentiInseriti, String nomeBot,
			HashMap<String, Snake> snakeList) {
		String fullName;
		fullName = nomeBot + serpentiInseriti;
		Stanza stanza = MappaManager.getStanzaCasualeLiberaPerSpawn(partita.getMappa(), partita.getSerpenti(), null);
		if(stanza!=null) {
			snakeList.put(fullName,new HardBotSnake(fullName, stanza, Costanti.VITA_SERPENTE_DEFAULT, partita));
			serpentiInseriti++;
		} else {
			System.out.println("Unable to insert HardBot");
		}
		return serpentiInseriti;
	}

	private static int insertInsaneBot(Partita partita, int serpentiInseriti, String nomeBot,
			HashMap<String, Snake> snakeList) {
		String fullName;
		fullName = nomeBot + serpentiInseriti;
		Stanza stanza = MappaManager.getStanzaCasualeLiberaPerSpawn(partita.getMappa(), partita.getSerpenti(), null);
		if(stanza!=null) {
			snakeList.put(fullName, new InsaneBotSnake(fullName, stanza, Costanti.VITA_SERPENTE_DEFAULT, partita));
			serpentiInseriti++;
		} else {
			System.out.println("Unable to insert InsaneBot");
		}
		return serpentiInseriti;
	}
	
	public static void resuscitaTuttiSerpenti(Partita partita, HashMap<String, Snake> serpenti) {			
		for(Snake snake: serpenti.values()) {
			if(!snake.isVivo()) {
				Stanza stanza = MappaManager.getStanzaCasualeLiberaPerSpawn(partita.getMappa(), serpenti, null);
				if(stanza!=null) {
					resuscitaSerpente(partita, snake);
				}
			}
		}
	}

	public static int calcolaNumeroMassimoSerpentiMappa(Mappa mappa) {
		int totalRoomsNumber = mappa.getStanze().size();
		int spawnableRoomsCounter = 0;
		for(Stanza stanza:mappa.getStanze()) {
			if(stanza.isSpawnEnabled()) {
				spawnableRoomsCounter ++;
			}
		}
		return Math.min(totalRoomsNumber, spawnableRoomsCounter);
	}
	
	public static void provaAResuscitareUnSerpenteBot(Partita partita) {	
		for(Snake snake:partita.getSerpenti().values()) {
			if(!snake.isVivo() && !snake.getNome().equals(partita.getNomePlayer1())){
				provaAResuscitareUnSerpente(partita, snake);
			}
		}
	}
	
	public static void provaAResuscitareUnSerpente(Partita partita, Snake snake) {	
		if(!snake.isVivo()) {
			resuscitaSerpente(partita, snake);
			return; // just one
		}
	}
	
	private static void resuscitaSerpente(Partita partita, Snake s) {
		if(!s.isVivo()) {
			if(s.getNome().equals(partita.getNomePlayer1())) GestoreSuoni.playSpawnSound();
			int vecchiaVita = s.getHpPreMorte();
			int vitaResurrezione = Utility.massimoTra(VITA_SERPENTE_DEFAULT,(int)(vecchiaVita/2.0));
			Stanza ultimaStanza = s.getUltimaStanza();
			Stanza stanzaAlternativa = MappaManager.getStanzaCasualeLiberaPerSpawn(partita.getMappa(), partita.getSerpenti(), ultimaStanza);
			if(stanzaAlternativa!=null) {
				s.resettaSerpente(stanzaAlternativa, vitaResurrezione);
			}
		}
	}
	
}
