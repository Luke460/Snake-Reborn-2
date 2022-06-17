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

import supporto.Posizione;

public class StanzaManager {

	public static boolean isLibera(Stanza stanza) {
		for(Casella c:stanza.getCaselle().values()){
			if(c.getStato()!=CARATTERE_CASELLA_VUOTA &&
					c.getStato()!=CARATTERE_CASELLA_CIBO &&
					c.getStato()!=CARATTERE_CASELLA_MURO &&
					c.getStato()!=CARATTERE_CASELLA_PORTALE){
				return false;
			}
			Posizione posizioneCentrale = new Posizione(DIMENSIONE_STANZA_DEFAULT/2,DIMENSIONE_STANZA_DEFAULT/2);
			Casella casellaCentrale = stanza.getCaselle().get(posizioneCentrale);
			if(casellaCentrale.getStato()!=CARATTERE_CASELLA_VUOTA && casellaCentrale.getStato()!=CARATTERE_CASELLA_CIBO) {
				return false;
			}
		}
		return true;
	}
	
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
	
	
	
}
