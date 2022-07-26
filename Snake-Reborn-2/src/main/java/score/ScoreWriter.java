package score;

import java.io.IOException;

import support.FileHandler;

public class ScoreWriter extends Thread {
	private String fileName;
	private String content;
	public ScoreWriter(String fileName, String content){
		this.fileName = fileName;
		this.content = content;
	}
	
	public void run() {
		try {
			FileHandler.writeFile(fileName, content);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
