package constants;

public interface GeneralConstants {

	final static String GAME_VERSION = "alpha-0.1.0";
	
	final static String ROOMS_FOLDER_NAME = "rooms";
	final static String MAPS_PATH = "maps";
	final static String CELL_SETTINGS_FILE_NAME = "cell-settings";
	final static String FILE_TYPE = ".txt";
	
	final static String SOUNDS_PATH = "sounds";
	
	static final public int TICK_TIME_EASY = 100; // 10 fps
	static final public int TICK_TIME_MEDIUM = 83; // 12 fps
	static final public int TICK_TIME_HARD = 67; // 15 fps
	//static final public int TEMPO_BASE = 1000; // 1 fps for test
	//static final public int TEMPO_BASE = 28; // 3X speed test
	//static final public int FPS = (int)(1000/TEMPO_BASE);

	public static final byte DIMENSIONE_STANZA_DEFAULT = 40;
	public static final double RAPPORTO_DIMENSIONE_SCHERMO = 0.9; // 90% del lato minimo
	
	public static final String EST = "est";
	public static final String OVEST = "ovest";
	public static final String SUD = "sud";
	public static final String NORD = "nord";
	
	//public static final String NOME_FILE_RECORD = "temp.int";
	public static final String NOME_FILE_INDIRIZZO_SERVER = "config/index.ini";
	public static final String FILE_NOME_SERVER = "config/server.ini";
	public static final String NOME_FILE_USERNAME_TEMPORANEO = "config/username.ini";
	public static final String FILE_PORTA = "config/port.ini";
	public static final String FILE_BRAIN = "ai/brain.json";	
	
	public static final String EMPTY_STATUS = "EM";
	public static final String SNAKE_STATUS = "SN";
	public static final String SOLID_STATUS = "SL";
	public static final String FOOD_STATUS = "FD";
	
	public static final int GAME_LENGTH_IN_SECONDS = 1;
	
	public static final int VITA_SERPENTE_DEFAULT = 1;
	static final public int VITA_SERPENTE_MASSIMA = 40;
	
	static final public int SNAKE_HP_FOR_BONUS_FOOD = 10;
	static final public int SNAKE_HP_FOR_SUPER_FOOD = 20;
	static final public int MOLTIPLICATORE_PUNTEGGIO_CIBO = 10;
	static final public int MOLTIPLICATORE_PUNTEGGIO_UCCISIONE = 100;
	static final public int TEMPO_RIPOPOLAMENTO_CIBO = 5; // 5 sec
	
	static final public int QTY_STANDARD_FOOD = 1;
	static final public int QTY_BONUS_FOOD = 3;
	static final public int QTY_SUPER_FOOD = 6;
	
	static final public int TEMPO_RIPOPOLAMENTO_SERPENTI_BOT = 1;
	static final public int SNAKE_RESPAWN_CD = 5;
	
	static final public int CD_FLASH = 12*60; //fps x time
	
	static final public String NOME_PLAYER_1 = "Giocatore 1";
	
}
