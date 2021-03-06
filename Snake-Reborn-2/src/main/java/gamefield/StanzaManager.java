package gamefield;

import static constants.GeneralConstants.DIMENSIONE_STANZA_DEFAULT;

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
		
		byte centralDistance = (byte)(DIMENSIONE_STANZA_DEFAULT/2);
		byte centralDistanceMinusOne = (byte)(centralDistance-1);
		
		Position pos1 = new Position(centralDistanceMinusOne, centralDistanceMinusOne);
		Position pos2 = new Position(centralDistance, centralDistanceMinusOne);
		Position pos3 = new Position(centralDistanceMinusOne, centralDistance);
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
