package supporto;

public interface Costanti {

	final static String MAP_FILE_NAME = "mappa-1.txt";
	final static String PATH_MAPPE = "mappe";
	final static String PATH_STANZE = "stanze";

	static final public int TEMPO_BASE = 83; // 12 fps
	static final public int FPS = (int)(1000/TEMPO_BASE);

	public static final int DIMENSIONE_STANZA_DEFAULT = 40;
	public static final double RAPPORTO_DIMENSIONE_SCHERMO = 0.9; // 90% del lato minimo
	
	public static final String EST = "est";
	public static final String OVEST = "ovest";
	public static final String SUD = "sud";
	public static final String NORD = "nord";
	
	public static final char CARATTERE_FINE_FILE = '$';
	public static final char CARATTERE_INIZIO_RIGA = '<';
	public static final char CARATTERE_FINE_RIGA = '>';
	
	//public static final String NOME_FILE_RECORD = "temp.int";
	public static final String NOME_FILE_INDIRIZZO_SERVER = "config/index.ini";
	public static final String FILE_NOME_SERVER = "config/server.ini";
	public static final String NOME_FILE_USERNAME_TEMPORANEO = "config/username.ini";
	public static final String FILE_PORTA = "config/port.ini";
	public static final String FILE_BRAIN = "ai/brain.json";	
	
	public static final char CARATTERE_CASELLA_VUOTA = ' ';
	public static final char CARATTERE_CASELLA_PLAYER_GENERICO = 'X';
	public static final char CARATTERE_CASELLA_BLUESNAKE = '1';
	public static final char CARATTERE_CASELLA_PLAYER2 = '2';
	public static final char CARATTERE_CASELLA_MURO = 'W';
	public static final char CARATTERE_CASELLA_BOT_EASY = 'E';
	public static final char CARATTERE_CASELLA_BOT_HARD = 'H';
	public static final char CARATTERE_CASELLA_BOT_MEDIUM = 'M';
	public static final char CARATTERE_CASELLA_BOT_INSANE = 'I';
	public static final char CARATTERE_CASELLA_CIBO = 'F';
	public static final char CARATTERE_CASELLA_PORTALE = 'P';
	
	public static final int VITA_SERPENTE_DEFAULT = 2;
	static final public int VITA_SERPENTE_MASSIMA = 40;
	
	static final public int LUNGHEZZA_MINIMA_PER_TESTA_SERPENTE = 10; // 10 hp
	static final public int QTA_CIBO_TESTA_SERPENTE = 5; // 5 unita' cibo
	static final public int QTA_CIBO_BASE = 1; // 1 unita' cibo
	static final public int MOLTIPLICATORE_PUNTEGGIO_CIBO = 10;
	static final public int MOLTIPLICATORE_PUNTEGGIO_UCCISIONE = 100;
	static final public int TEMPO_RIPOPOLAMENTO_CIBO = (int) FPS * 5; // 5 sec
	
	static final public int TEMPO_RIPOPOLAMENTO_SERPENTI_BOT = (int) FPS * 3; // max 3 sec
	static final public int LIMITE_MINIMO_SERPENTI_ALTO = 8;
	static final public int LIMITE_MINIMO_SERPENTI_BASSO = 4;
	
	static final public int CD_FLASH = 12*60; //fps x time
	
	static final public String NOME_PLAYER_1 = "Giocatore 1";
	
}
