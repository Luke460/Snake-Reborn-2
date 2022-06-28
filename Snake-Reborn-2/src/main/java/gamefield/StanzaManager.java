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
		
		int centralDistance = DIMENSIONE_STANZA_DEFAULT/2;
		
		Position pos1 = new Position(centralDistance-1, centralDistance-1);
		Position pos2 = new Position(centralDistance, centralDistance-1);
		Position pos3 = new Position(centralDistance-1, centralDistance);
		Position pos4 = new Position(centralDistance, centralDistance);
		
		Casella cell1 = stanza.getCaselle().get(pos1);
		Casella cell2 = stanza.getCaselle().get(pos2);
		Casella cell3 = stanza.getCaselle().get(pos3);
		Casella cell4 = stanza.getCaselle().get(pos4);
		
		if(cell1.isMortal()) return false;
		if(cell2.isMortal()) return false;
		if(cell3.isMortal()) return false;
		if(cell4.isMortal()) return false;
		
		return true;
	}
	
}
