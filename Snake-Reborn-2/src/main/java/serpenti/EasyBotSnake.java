package serpenti;
import static supporto.Costanti.*;

import game.Partita;
import terrenoDiGioco.Stanza;

public class EasyBotSnake extends CustomBotSnake {

	public EasyBotSnake(String nome, Stanza stanza, int vitaIniziale, Partita partita) {
		super(nome, stanza, vitaIniziale, partita, null);
		Skill skill = new Skill(95, 0, 0, 0, 0);
		super.setSkill(skill);
		super.setStatoCaselleDefault(CARATTERE_CASELLA_BOT_EASY);
	}

}
