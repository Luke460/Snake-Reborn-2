package audio;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.*;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class SuonoWAV{
	private Clip clip;
	FloatControl gainControl;

	public SuonoWAV(String filePath, int volume) {
		try {
			// Usa URL (invece di File) per leggere dal disco.
			File filePathRelativo = new File(filePath);
			File filePathAssoluto = new File(filePathRelativo.getAbsolutePath());
			//URL url = getClass().getResource(fileSuono.getAbsolutePath());
 			//File fileSuono = new File(filePath);
			// Crea uno stream d'input audio dal file del suono.
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(filePathAssoluto);
			// Ottieni il clip.
			
			AudioFormat af = audioInputStream.getFormat();
	        DataLine.Info info = new DataLine.Info(Clip.class, af);
	        this.clip = (Clip)AudioSystem.getLine(info);
			if(this.clip==null) throw new Exception("Clip is null");
			//clip = AudioSystem.getClip();
			// Apri l'audio del clip.
			this.clip.open(audioInputStream);
			this.gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
			this.setVolume(volume);
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void playClip() {
		if (this.clip==null) {
			//System.out.println("clip is null");
			return;
		}
		//if (this.clip.isRunning()) {
			this.clip.stop();   // Ferma il suono se e' ancora in esecuzione.
			//System.out.println("clip was stopped");
		//}
		this.clip.setFramePosition(0); // Riavvolgi il suono.
		this.clip.start();     // Esegui il suono.
		//System.out.println("clip restarted");
	}
	
	@SuppressWarnings("static-access")
	public void loopClip() {
		if (this.clip==null)return;
		//if (this.clip.isRunning())
		this.clip.stop();   // Ferma il suono se e' ancora in esecuzione.
		this.clip.setFramePosition(0); // Riavvolgi il suono.
		this.clip.loop(clip.LOOP_CONTINUOUSLY);     // Esegui il suono.
	}
	
	public void fermaClip() {
		try {
			this.clip.stop();
		} catch (Exception e) {
			//pazienza
		}
	}
	
	public void setVolume(int newVolumeLevel) {
	    if (newVolumeLevel < 0 || newVolumeLevel > 100) {
	        throw new IllegalArgumentException("Volume not valid: " + newVolumeLevel);
		} else if (this.clip.isOpen()) {
			gainControl.setValue(20f * (float) Math.log10(newVolumeLevel/100f));
	    }
	}
	
	public double getVolume() {
		return (Math.pow(10f, gainControl.getValue() / 20f))*100f;
	}
	
}