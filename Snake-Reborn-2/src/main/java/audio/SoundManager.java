package audio;

import static constants.GeneralConstants.SOUNDS_PATH;

import support.OSdetector;

public class SoundManager {

	private static boolean effectsSoundEnabled;
	private static boolean musicSoundEnabled;

	private static WAVSound slainSound;
	private static WAVSound spawnSound;
	private static WAVSound ExplodeSound;
	private static WAVSound takeSound;
	private static WAVSound musicSound;
	private static WAVSound endSound;
	private static WAVSound alertSound;

	public static void inizzializzaSuoni(int effectsVolume, int musicVolume){
		slainSound = new WAVSound(SOUNDS_PATH+OSdetector.getPathSeparator()+"slain.wav", effectsVolume);
		spawnSound = new WAVSound(SOUNDS_PATH+OSdetector.getPathSeparator()+"spawn.wav", effectsVolume);
		ExplodeSound = new WAVSound(SOUNDS_PATH+OSdetector.getPathSeparator()+"explode.wav", effectsVolume);
		takeSound = new WAVSound(SOUNDS_PATH+OSdetector.getPathSeparator()+"take.wav", effectsVolume);
		musicSound = new WAVSound(SOUNDS_PATH+OSdetector.getPathSeparator()+"music.wav", musicVolume);
		endSound = new WAVSound(SOUNDS_PATH+OSdetector.getPathSeparator()+"end.wav", effectsVolume);
		alertSound = new WAVSound(SOUNDS_PATH+OSdetector.getPathSeparator()+"alert.wav", effectsVolume/3);
	}

	public static void playMusicaInLoop(){
		if(musicSoundEnabled) musicSound.loopClip();
	}
	
	public static void playSlainSound(){
		if(effectsSoundEnabled) slainSound.playClip();
	}
	
	public static void playSpawnSound(){
		if(effectsSoundEnabled) spawnSound.playClip();
	}
	
	public static void playExplodeSound(){
		if(effectsSoundEnabled) ExplodeSound.playClip();
	}
	
	public static void playTakeSound(){
		if(effectsSoundEnabled) takeSound.playClip();
	}
	
	public static void playGameEndSound(){
		if(effectsSoundEnabled) endSound.playClip();
	}
	
	public static void playAlertSoundInLoop(){
		if(effectsSoundEnabled) alertSound.loopClip();
	}

	public static boolean isEffettiAbilitati() {
		return effectsSoundEnabled;
	}

	public static void setEffettiAbilitati(boolean b) {
		effectsSoundEnabled = b;
	}

	public static boolean isMusicSoundEnabled() {
		return musicSoundEnabled;
	}

	public static void setMusicSoundEnabled(boolean b) {
		musicSoundEnabled = b;
	}

	public static void stopMusic() {
		musicSound.stopClip();	
	}
	
	public static void stopAlert() {
		alertSound.stopClip();	
	}

}
