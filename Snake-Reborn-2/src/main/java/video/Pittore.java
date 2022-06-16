package video;

import static supporto.Costanti.*;

import java.awt.Color;

public class Pittore {

	public static Color getColore(char stato) {
		switch (stato) {

		case CARATTERE_CASELLA_PLAYER_GENERICO:
			return Color.darkGray;
		
		case CARATTERE_CASELLA_BLUESNAKE:
			return Color.blue;
			
		case CARATTERE_CASELLA_PLAYER2:
			return Color.pink;
			
		case CARATTERE_CASELLA_MURO:
			return Color.gray;
			
		case CARATTERE_CASELLA_BOT_EASY:
			return Color.green;
			
		case CARATTERE_CASELLA_BOT_MEDIUM:
			return new Color(250, 150, 0);
			
		case CARATTERE_CASELLA_BOT_HARD:
			return Color.red;
			
		case CARATTERE_CASELLA_BOT_INSANE:
			return Color.magenta;
			
		case CARATTERE_CASELLA_CIBO:
			return Color.yellow;
			
		case CARATTERE_CASELLA_VUOTA:
			return Color.black;
			
		case CARATTERE_CASELLA_PORTALE:
			return Color.lightGray;
		
		default: // CARATTERE_CASELLA_VUOTA
			return Color.black;
		}	
	}
}
