package popolatori;

import static supporto.Costanti.*;

import java.util.Iterator;

import game.Partita;
import serpenti.EasyBotSnake;
import serpenti.HardBotSnake;
import serpenti.InsaneBotSnake;
import serpenti.MediumBotSnake;
import serpenti.Snake;

public class PopolatoreSerpenti {
	Partita partita;
	
	public static void creaPopoloIniziale(Partita partita) {
		
		int numeroMinimoSerpentiIniziali = 0;
		if(partita.getFattorePopolazione()==1) numeroMinimoSerpentiIniziali = NUMERO_MINIMO_SERPENTI_INIZIALI_BASSO;
		if(partita.getFattorePopolazione()==2) numeroMinimoSerpentiIniziali = NUMERO_MINIMO_SERPENTI_INIZIALI_ALTO;

		if(partita.getLivello()==3) {
			while (partita.getNumeroAvversari() < numeroMinimoSerpentiIniziali){
				
				partita.inserisciBot(EasyBotSnake.class.getSimpleName());
				
				partita.inserisciBot(MediumBotSnake.class.getSimpleName());
				
				partita.inserisciBot(HardBotSnake.class.getSimpleName());
				
				partita.inserisciBot(InsaneBotSnake.class.getSimpleName());
				
			}
		} else if(partita.getLivello()==2) {
			while (partita.getNumeroAvversari() < numeroMinimoSerpentiIniziali){
				
				partita.inserisciBot(EasyBotSnake.class.getSimpleName());
				
				partita.inserisciBot(MediumBotSnake.class.getSimpleName());
				
				partita.inserisciBot(MediumBotSnake.class.getSimpleName());
				
				partita.inserisciBot(HardBotSnake.class.getSimpleName());
				
			}
		} else if(partita.getLivello()==1) {
				while (partita.getNumeroAvversari() < numeroMinimoSerpentiIniziali){
					
					partita.inserisciBot(EasyBotSnake.class.getSimpleName());
					
					partita.inserisciBot(EasyBotSnake.class.getSimpleName());
					
					partita.inserisciBot(MediumBotSnake.class.getSimpleName());
					
					partita.inserisciBot(MediumBotSnake.class.getSimpleName());
					
				}
			}
	}

	public static void provaAdInserireUnSerpente(Partita partita) {
		Snake serpenteDaResuscitare;
		Iterator<Snake> iteratore = partita.getSerpentiMorti().values().iterator();
		while(iteratore.hasNext()){
			serpenteDaResuscitare = iteratore.next();
			if(!serpenteDaResuscitare.getNome().equals(partita.getNomePlayer1())) {
				partita.resuscitaSerpente(serpenteDaResuscitare);
				iteratore.remove();
			}
		}

	}
	
}
