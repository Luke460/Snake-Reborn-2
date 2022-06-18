package terrenoDiGioco;

import static supporto.Costanti.CARATTERE_CASELLA_PORTALE;
import static supporto.Costanti.EST;
import static supporto.Costanti.NORD;
import static supporto.Costanti.OVEST;
import static supporto.Costanti.SUD;

import supporto.Posizione;

public class StanzaManager {
	
	public static void coloraPorta(Stanza stanza, String orientamentoPorta) {
		if(orientamentoPorta==NORD){
			stanza.getCaselle().get(new Posizione(15,0)).setStato(CARATTERE_CASELLA_PORTALE);
			stanza.getCaselle().get(new Posizione(24,0)).setStato(CARATTERE_CASELLA_PORTALE);
		}
		if(orientamentoPorta==EST){
			stanza.getCaselle().get(new Posizione(39,15)).setStato(CARATTERE_CASELLA_PORTALE);
			stanza.getCaselle().get(new Posizione(39,24)).setStato(CARATTERE_CASELLA_PORTALE);
		}
		if(orientamentoPorta==SUD){
			stanza.getCaselle().get(new Posizione(15,39)).setStato(CARATTERE_CASELLA_PORTALE);
			stanza.getCaselle().get(new Posizione(24,39)).setStato(CARATTERE_CASELLA_PORTALE);
		}
		if(orientamentoPorta==OVEST){
			stanza.getCaselle().get(new Posizione(0,15)).setStato(CARATTERE_CASELLA_PORTALE);
			stanza.getCaselle().get(new Posizione(0,24)).setStato(CARATTERE_CASELLA_PORTALE);
		}
		
	}

	public static int getNumeroStanzeCollegate(Stanza stanza) {
		int n = 0;
		for(Stanza altraStanza:stanza.getCollegamenti().values()) {
			if(!altraStanza.equals(stanza)) {
				n++;
			}
		}
		return n;
	}
	
}
