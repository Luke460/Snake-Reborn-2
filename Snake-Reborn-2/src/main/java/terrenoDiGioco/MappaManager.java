package terrenoDiGioco;

import java.util.ArrayList;
import java.util.Collections;

public class MappaManager {
	
	public static Stanza getStanzaCasualeLibera_controlloSuTutteLeStanze(Mappa mappa) {
		ArrayList<Stanza> stanzeMischiate = new ArrayList<Stanza>();
		stanzeMischiate.addAll(mappa.getStanze());
		Collections.shuffle(stanzeMischiate);
		for(Stanza tempStanza:stanzeMischiate){
			if(StanzaManager.isLibera(tempStanza)) return tempStanza;
		}
		return null;
	}

}
