package serpenti;

import static supporto.Costanti.CARATTERE_CASELLA_BOT_INSANE;

import game.Partita;
import terrenoDiGioco.Stanza;

public class InsaneBotSnake extends CustomBotSnake {

	public InsaneBotSnake(String nome, Stanza stanza, int vitaIniziale, Partita partita) {
		super(nome, stanza, vitaIniziale, partita, null);
		Skill skill = new Skill(100, 100, 100, 100);
		super.setSkill(skill);
		super.setStatoCaselleDefault(CARATTERE_CASELLA_BOT_INSANE);
	}

}
