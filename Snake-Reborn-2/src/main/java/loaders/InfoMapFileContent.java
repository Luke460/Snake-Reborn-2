package loaders;

import java.util.ArrayList;

public class InfoMapFileContent {
	
	private String prefix;
	ArrayList<String> infoLines;
	
	public InfoMapFileContent() {
		this.infoLines = new ArrayList<String>();
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public ArrayList<String> getInfoLines() {
		return infoLines;
	}

	public void setInfoLines(ArrayList<String> infoLines) {
		this.infoLines = infoLines;
	}

}
