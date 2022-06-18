package terrenoDiGioco;

import static supporto.Costanti.CARATTERE_CASELLA_CIBO;
import static supporto.Costanti.CARATTERE_CASELLA_MURO;
import static supporto.Costanti.CARATTERE_CASELLA_PORTALE;
import static supporto.Costanti.CARATTERE_CASELLA_VUOTA;
import static supporto.Costanti.DIMENSIONE_STANZA_DEFAULT;
import static supporto.Costanti.EST;
import static supporto.Costanti.NORD;
import static supporto.Costanti.OVEST;
import static supporto.Costanti.SUD;

import serpenti.Snake;
import supporto.Direction;
import supporto.Posizione;

public class CasellaManager {
	
	public static void setCasellaOccupataDalSerpente(Casella casella, Snake serpente, int vita, char stato) {
		casella.setSerpente(serpente);
		casella.setVita(vita);
		casella.setStato(stato);
	}

	public static boolean isPozzo(Stanza stanza, Casella casella, Direction direzione) {
		Casella forwardCasella = getCasellaAdiacente(casella, direzione);
		Casella rightCasella = getCasellaAdiacente(casella, direzione.getRotatedRightDirection());
		Casella leftCasella = getCasellaAdiacente(casella, direzione.getRotatedLeftDirection());
		if(	isMortale(forwardCasella) &&
			isMortale(rightCasella) &&
		    isMortale(leftCasella) ){
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
	
	public static boolean isMortale(Casella casella) {
		if (casella.getStato() == CARATTERE_CASELLA_VUOTA || casella.getStato() == CARATTERE_CASELLA_CIBO) return false;
		return true;
	}

	public static void libera(Casella casella) {
		casella.setStato(CARATTERE_CASELLA_VUOTA);
		casella.setSerpente(null);
		casella.setTestaDiSerpente(false);
		casella.setVita(-1);
	}

	public static boolean isCibo(Casella casella) {
		if(casella.getStato() == CARATTERE_CASELLA_CIBO) return true;
		return false;
	}

	public static boolean isVuota(Casella casella) {
		if(casella.getStato() == CARATTERE_CASELLA_VUOTA) return true;
		return false;
	}

	public static boolean isMuro(Casella casella) {
		if (casella.getStato() == CARATTERE_CASELLA_MURO || casella.getStato() == CARATTERE_CASELLA_PORTALE) return true;
		return false;
	}

	public static boolean isOccupataDaSerpente(Casella casella) {
		return casella.getSerpente()!=null;
	}
	
	public static void setCasellaOccupataDalVerme(Casella casella, Snake serpente, char coloreSerpente) {
		casella.setStato(coloreSerpente);
		casella.setVita(serpente.getHP());
	}

}
