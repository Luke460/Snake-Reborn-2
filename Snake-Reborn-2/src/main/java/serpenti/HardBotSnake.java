package serpenti;

import static supporto.Costanti.CARATTERE_CASELLA_BOT_HARD;

import game.Partita;
import terrenoDiGioco.Stanza;

public class HardBotSnake extends CustomBotSnake {

	public HardBotSnake(String nome, Stanza stanza, int vitaIniziale, Partita partita) {
		super(nome, stanza, vitaIniziale, partita, null);
		Skill skill = new Skill(99, 75, 75, 75, 75);
		super.setSkill(skill);
		super.setStatoCaselleDefault(CARATTERE_CASELLA_BOT_HARD);
	}

}
