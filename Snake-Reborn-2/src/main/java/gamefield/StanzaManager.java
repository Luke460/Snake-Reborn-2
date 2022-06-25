package gamefield;

import static support.Costanti.DIMENSIONE_STANZA_DEFAULT;

public class StanzaManager {

	public static int getNumeroStanzeCollegate(Stanza stanza) {
		int n = 0;
		for(Stanza altraStanza:stanza.getCollegamenti().values()) {
			if(!altraStanza.equals(stanza)) {
				n++;
			}
		}
		return n;
	}
	
	public static boolean isActuallyReadyForSpawn(Stanza stanza) {
		Position posizioneCasellaCentrale = new Position(DIMENSIONE_STANZA_DEFAULT/2, DIMENSIONE_STANZA_DEFAULT/2);
		Casella casellaCentrale = stanza.getCaselle().get(posizioneCasellaCentrale);
		if(!casellaCentrale.isMortal()) {
			return true;
		}
		return false;
	}
	
}
