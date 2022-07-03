package audio;

import static constants.GeneralConstants.SOUNDS_PATH;

import support.OSdetector;

public class GestoreSuoni {

	private static boolean effettiAbilitati;
	private static boolean musicaAbilitata;

	private static SuonoWAV suonoSlain;
	private static SuonoWAV suonoSpawn;
	private static SuonoWAV suonoExplode;
	private static SuonoWAV suonoTake;
	private static SuonoWAV suonoMusic;

	public static void inizzializzaSuoni(int effectsVolume, int musicSound){
		suonoSlain = new SuonoWAV(SOUNDS_PATH+OSdetector.getPathSeparator()+"slain.wav", effectsVolume);
		suonoSpawn = new SuonoWAV(SOUNDS_PATH+OSdetector.getPathSeparator()+"spawn.wav", effectsVolume);
		suonoExplode = new SuonoWAV(SOUNDS_PATH+OSdetector.getPathSeparator()+"explode.wav", effectsVolume);
		suonoTake = new SuonoWAV(SOUNDS_PATH+OSdetector.getPathSeparator()+"take.wav", effectsVolume);
		suonoMusic = new SuonoWAV(SOUNDS_PATH+OSdetector.getPathSeparator()+"music.wav", musicSound);
	}

	public static void playMusicaInLoop(){
		if(musicaAbilitata) suonoMusic.loopClip();
	}
	
	public static void playSlainSound(){
		if(effettiAbilitati) suonoSlain.playClip();
	}
	
	public static void playSpawnSound(){
		if(effettiAbilitati) suonoSpawn.playClip();
	}
	
	public static void playExplodeSound(){
		if(effettiAbilitati) suonoExplode.playClip();
	}
	
	public static void playTakeSound(){
		if(effettiAbilitati) suonoTake.playClip();
	}

	public static boolean isEffettiAbilitati() {
		return effettiAbilitati;
	}

	public static void setEffettiAbilitati(boolean b) {
		effettiAbilitati = b;
	}

	public static boolean isMusicaAbilitata() {
		return musicaAbilitata;
	}

	public static void setMusicaAbilitata(boolean b) {
		musicaAbilitata = b;
	}

	public static void silenziaMusica() {
		suonoMusic.fermaClip();	
	}
	
	public static void setVolumeMusica(int volume) {
		suonoMusic.setVolume(volume);
	}
	
	public static void getVolumeMusica(int volume) {
		suonoMusic.getVolume();
	}
	
	public static void setVolumeEffetti(int volume) {
		suonoSlain.setVolume(volume);
		suonoSpawn.setVolume(volume);
		suonoExplode.setVolume(volume);
		suonoTake.setVolume(volume);
	}
	
	public static void getVolumeEffetti(int volume) {
		suonoTake.getVolume();
	}

}
