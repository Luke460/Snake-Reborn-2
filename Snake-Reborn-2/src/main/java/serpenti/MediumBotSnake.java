package serpenti;
import static supporto.Costanti.*;

import game.Partita;
import terrenoDiGioco.Stanza;

public class MediumBotSnake extends CustomBotSnake {

	public MediumBotSnake(String nome, Stanza stanza, int vitaIniziale, Partita partita) {
		super(nome, stanza, vitaIniziale, partita, null);
		Skill skill = new Skill(50, 50, 50, 50);
		super.setSkill(skill);
		super.setStatoCaselleDefault(CARATTERE_CASELLA_BOT_MEDIUM);
	}

}