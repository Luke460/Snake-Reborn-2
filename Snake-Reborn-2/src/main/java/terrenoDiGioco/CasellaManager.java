package terrenoDiGioco;

import static supporto.Costanti.*;

import serpenti.Snake;
import supporto.Direction;
import supporto.Posizione;

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
		Posizione posizioneCasella = casella.getPosizione();
		Posizione posizioneNuovaCasella = posizioneCasella.getPosizioneInDirezione(direzione);
		Stanza stanzaCorrente = casella.getStanza();
		//controllo out of stanza
		if(posizioneNuovaCasella.getX()>DIMENSIONE_STANZA_DEFAULT-1){
			posizioneNuovaCasella = new Posizione(0,posizioneNuovaCasella.getY()); 
			return stanzaCorrente.getCollegamenti().get(EST).getCaselle().get(posizioneNuovaCasella);
		}

		if(posizioneNuovaCasella.getX()<0){
			posizioneNuovaCasella = new Posizione(DIMENSIONE_STANZA_DEFAULT-1,posizioneNuovaCasella.getY()); 
			return stanzaCorrente.getCollegamenti().get(OVEST).getCaselle().get(posizioneNuovaCasella);
		}

		if(posizioneNuovaCasella.getY()>DIMENSIONE_STANZA_DEFAULT-1){
			posizioneNuovaCasella = new Posizione(posizioneNuovaCasella.getX(),0); 
			return stanzaCorrente.getCollegamenti().get(SUD).getCaselle().get(posizioneNuovaCasella);
		}
		if(posizioneNuovaCasella.getY()<0){
			posizioneNuovaCasella = new Posizione(posizioneNuovaCasella.getX(),DIMENSIONE_STANZA_DEFAULT-1); 
			return stanzaCorrente.getCollegamenti().get(NORD).getCaselle().get(posizioneNuovaCasella);
		}

		// stiamo nei confini della stanza
		return  stanzaCorrente.getCaselle().get(posizioneNuovaCasella);
	}

}
