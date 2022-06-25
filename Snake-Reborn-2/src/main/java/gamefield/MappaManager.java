package gamefield;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

import snake.Snake;

public class MappaManager {
	
	public static Stanza getStanzaCasualeLiberaPerSpawn(Mappa mappa, HashMap<String, Snake> snakes, Stanza stanzaPrecedente) {
		HashSet<Stanza> setStanzeDisponibili = new HashSet<Stanza>();
		setStanzeDisponibili.addAll(mappa.getStanze());
		
		for(Snake snake:snakes.values()) {
			if(snake.isVivo()) {
				for(Casella casella:snake.getCaselle()) {
					setStanzeDisponibili.remove(casella.getStanza());
				}
			}
		}
		
		if(setStanzeDisponibili.size()>0) {
			ArrayList<Stanza> listaStanzeDisponibili = new ArrayList<Stanza>(setStanzeDisponibili);
			Collections.shuffle(listaStanzeDisponibili);
			for(Stanza stanza:listaStanzeDisponibili) {
				if(!stanza.equals(stanzaPrecedente) && stanza.isSpawnEnabled() && StanzaManager.isActuallyReadyForSpawn(stanza)) {
					return stanza;
				}
			}
		}
		System.out.println("out of rooms!");
		return null;
	}

}
