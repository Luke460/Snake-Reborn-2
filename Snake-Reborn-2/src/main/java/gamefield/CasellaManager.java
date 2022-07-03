package gamefield;

import static constants.GeneralConstants.DIMENSIONE_STANZA_DEFAULT;
import static constants.GeneralConstants.EST;
import static constants.GeneralConstants.NORD;
import static constants.GeneralConstants.OVEST;
import static constants.GeneralConstants.SUD;

import snake.Snake;

public class CasellaManager {
	
	public static void setCasellaOccupataDalSerpente(Casella casella, Snake serpente, int vita) {
		casella.setSnake(serpente);
		casella.setHp(vita);
	}

	public static boolean isPozzo(Stanza stanza, Casella casella, Direction direzione) {
		Casella forwardCasella = getCasellaAdiacente(casella, direzione);
		Casella rightCasella = getCasellaAdiacente(casella, direzione.getRotatedRightDirection());
		Casella leftCasella = getCasellaAdiacente(casella, direzione.getRotatedLeftDirection());
		if(	forwardCasella.isMortal() &&
			rightCasella.isMortal() &&
		    leftCasella.isMortal() ){
			return true;
		} else {
			return false;
		}
	}
	
	public static Casella getCasellaInDirezione(Casella casella, Direction direzione, int distanza) {
		if(distanza == 0) {
			return getCasellaAdiacente(casella, direzione);
		} else {
			return getCasellaInDirezione(getCasellaAdiacente(casella, direzione), direzione, distanza-1);
		}
	}
	
	public static Casella getCasellaAdiacente(Casella casella, Direction direzione) {
		Position posizioneCasella = casella.getPosizione();
		Position posizioneNuovaCasella = posizioneCasella.getPositionInDirection(direzione);
		Stanza stanzaCorrente = casella.getStanza();
		//controllo out of stanza
		if(posizioneNuovaCasella.getX()>DIMENSIONE_STANZA_DEFAULT-1){
			posizioneNuovaCasella = new Position(0,posizioneNuovaCasella.getY()); 
			return stanzaCorrente.getCollegamenti().get(EST).getCaselle().get(posizioneNuovaCasella);
		}

		if(posizioneNuovaCasella.getX()<0){
			posizioneNuovaCasella = new Position(DIMENSIONE_STANZA_DEFAULT-1,posizioneNuovaCasella.getY()); 
			return stanzaCorrente.getCollegamenti().get(OVEST).getCaselle().get(posizioneNuovaCasella);
		}

		if(posizioneNuovaCasella.getY()>DIMENSIONE_STANZA_DEFAULT-1){
			posizioneNuovaCasella = new Position(posizioneNuovaCasella.getX(),0); 
			return stanzaCorrente.getCollegamenti().get(SUD).getCaselle().get(posizioneNuovaCasella);
		}
		if(posizioneNuovaCasella.getY()<0){
			posizioneNuovaCasella = new Position(posizioneNuovaCasella.getX(),DIMENSIONE_STANZA_DEFAULT-1); 
			return stanzaCorrente.getCollegamenti().get(NORD).getCaselle().get(posizioneNuovaCasella);
		}

		// stiamo nei confini della stanza
		return  stanzaCorrente.getCaselle().get(posizioneNuovaCasella);
	}

	public static int getNumberOfNonLethalCellsInDirection(Casella firstCell, Direction dir) {
		int distance = 0;
		Casella cell = getCasellaAdiacente(firstCell, dir);
		Stanza startingRoom = cell.getStanza();
		while(!cell.isMortal() && cell.getStanza().equals(startingRoom)) {
			distance++;
			cell = getCasellaAdiacente(cell, dir);
		}
		return distance;
	}

}
