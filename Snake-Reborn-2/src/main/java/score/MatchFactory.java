package score;

import game.Partita;
import server.model.Match;

public class MatchFactory {
	
	public Match buildMatch(Partita partita){
		Match m = new Match(); 
		m.setScore(partita.getSerpentePlayer1().getTotalSnakeScore());
		m.setPlayingTime((long)partita.getSerpentePlayer1().getTempoSopravvissutoMillis()/1000);
	    m.setKills(partita.getSerpentePlayer1().getNumeroUccisioni());
	    return m;
	}
	
}
