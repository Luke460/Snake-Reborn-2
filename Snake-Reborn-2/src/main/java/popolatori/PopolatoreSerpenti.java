package popolatori;

import static supporto.Costanti.*;

import java.util.ArrayList;
import java.util.Iterator;

import audio.GestoreSuoni;
import game.Partita;
import serpenti.EasyBotSnake;
import serpenti.HardBotSnake;
import serpenti.InsaneBotSnake;
import serpenti.MediumBotSnake;
import serpenti.Snake;
import supporto.Utility;
import terrenoDiGioco.Mappa;
import terrenoDiGioco.MappaManager;
import terrenoDiGioco.Stanza;

public class PopolatoreSerpenti {
	Partita partita;
	
	public static void creaPopoloIniziale(Partita partita) {
		int limiteMinimoSerpenti = calcolaNumeroMinimoSerpenti(partita.getMappa());
		int serpentiInseriti = 0;
		
		ArrayList<String> tipologiaBot = new ArrayList<String>();
		
		if(partita.getLivello()==3) {
			tipologiaBot.add(InsaneBotSnake.class.getSimpleName());
			tipologiaBot.add(HardBotSnake.class.getSimpleName());
			tipologiaBot.add(MediumBotSnake.class.getSimpleName());
			tipologiaBot.add(EasyBotSnake.class.getSimpleName());
			while (serpentiInseriti < limiteMinimoSerpenti){
				for(String classeSerpente:tipologiaBot) {
					boolean esito = partita.inserisciBot(classeSerpente);
					if(!esito) {
						return;
					} else {
						serpentiInseriti ++;
					}
				}
			}
		} else if(partita.getLivello()==2) {
			tipologiaBot.add(HardBotSnake.class.getSimpleName());
			tipologiaBot.add(MediumBotSnake.class.getSimpleName());
			tipologiaBot.add(EasyBotSnake.class.getSimpleName());
			tipologiaBot.add(MediumBotSnake.class.getSimpleName());
			while (serpentiInseriti < limiteMinimoSerpenti){
				for(String classeSerpente:tipologiaBot) {
					boolean esito = partita.inserisciBot(classeSerpente);
					if(!esito) {
						return;
					} else {
						serpentiInseriti ++;
					}
				}
			}
		} else if(partita.getLivello()==1) {
			serpentiInseriti ++;
			tipologiaBot.add(MediumBotSnake.class.getSimpleName());
			tipologiaBot.add(EasyBotSnake.class.getSimpleName());
			tipologiaBot.add(MediumBotSnake.class.getSimpleName());
			tipologiaBot.add(EasyBotSnake.class.getSimpleName());
			while (serpentiInseriti < limiteMinimoSerpenti){
				for(String classeSerpente:tipologiaBot) {
					boolean esito = partita.inserisciBot(classeSerpente);
					if(!esito) {
						return;
					} else {
						serpentiInseriti ++;
					}
				}		
			}
		}
	}

	private static int calcolaNumeroMinimoSerpenti(Mappa mappa) {
		return mappa.getStanze().size()/2;
	}

	public static void provaAdInserireUnSerpente(Partita partita) {
		Snake serpenteDaResuscitare;
		Iterator<Snake> iteratore = partita.getSerpentiMorti().values().iterator();
		while(iteratore.hasNext()){
			serpenteDaResuscitare = iteratore.next();
			if(!serpenteDaResuscitare.getNome().equals(partita.getNomePlayer1())) {
				resuscitaSerpente(partita, serpenteDaResuscitare);
				iteratore.remove();
			}
		}

	}
	
	public static void resuscitaSerpente(Partita partita, Snake s) {
		if(s.getNome().equals(partita.getNomePlayer1())) GestoreSuoni.playSpawnSound();
		int vecchiaVita = s.getHpPreMorte();
		int vitaResurrezione = Utility.massimoTra(VITA_SERPENTE_DEFAULT,(int)(vecchiaVita/2.0));
		Stanza ultimaStanza = s.getUltimaStanza();
		Stanza stanzaAlternativa = MappaManager.getStanzaCasualeLiberaPerSpawn(partita.getMappa(), partita.getSerpentiVivi(), ultimaStanza);
		if(stanzaAlternativa!=null){
			ultimaStanza = stanzaAlternativa;
		}
		partita.getSerpentiVivi().remove(s.getNome());
		s.resettaSerpente(ultimaStanza, vitaResurrezione);
		partita.getSerpentiVivi().put(s.getNome(),s);
	}
	
}
