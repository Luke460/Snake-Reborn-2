package audio;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.*;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class WAVSound{
	private Clip clip;
	FloatControl gainControl;

	public WAVSound(String filePath, int volume) {
		try {
			File relativePath = new File(filePath);
			File absolutePath = new File(relativePath.getAbsolutePath());
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(absolutePath);
			
			AudioFormat af = audioInputStream.getFormat();
	        DataLine.Info info = new DataLine.Info(Clip.class, af);
	        this.clip = (Clip)AudioSystem.getLine(info);
			if(this.clip==null) throw new Exception("Clip is null");
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
			return;
		}
		this.clip.stop();
		this.clip.setFramePosition(0);
		this.clip.start();
	}
	
	public void loopClip() {
		if (this.clip==null) return;
		this.clip.stop();
		this.clip.setFramePosition(0);
		this.clip.loop(Clip.LOOP_CONTINUOUSLY);
	}
	
	public void stopClip() {
		try {
			this.clip.stop();
		} catch (Exception e) {
			//do nothing
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