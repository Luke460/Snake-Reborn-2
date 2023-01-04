package constants;

public interface GeneralConstants {

	final static String GAME_VERSION = "alpha-0.1.0";
	
	final static String ROOMS_FOLDER_NAME = "rooms";
	final static String MAPS_PATH = "maps";
	
	final static String SOUNDS_PATH = "sounds";
	
	static final public int TICK_TIME_EASY = 100; // 10 fps
	static final public int TICK_TIME_MEDIUM = 83; // 12 fps
	static final public int TICK_TIME_HARD = 67; // 15 fps

	public static final byte ROOM_SIZE = 40;
	public static final double WINDOW_SCREEN_PERCENTAGE = 0.9; // 1.0: 100%
	
	public static final String EAST = "east";
	public static final String WEST = "west";
	public static final String SOUTH = "south";
	public static final String NORTH = "north";
	
	public static final int GAME_LENGTH_IN_SECONDS = 300;
	
	public static final int MIN_HP = 1;
	static final public int MAX_HP = 40;
	
	static final public int FOOD_SPAWN_JOB_TIMER = 5; // seconds
	static final public int MIN_SNAKE_HP_FOR_BONUS_FOOD = 10;
	static final public int MIN_SNAKE_HP_FOR_SUPER_FOOD = 20;
	static final public int HP_STANDARD_FOOD = 1;
	static final public int HP_POISON_FOOD = -9;
	static final public int HP_BONUS_FOOD = 3;
	static final public int HP_SUPER_FOOD = 6;
	static final public int FOOD_SCORE_MULTIPLIER = 10;
	static final public int KILL_SCORE_MULTIPLIER = 100;
	
	static final public int SNAKE_SPAWN_JOB_TIMER = 1;
	static final public int SNAKE_RESPAWN_CD = 5;
	
	static final public String NAME_PLAYER_1 = "PlayerOne";
	
}
