package loaders;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InfoMapFileContent {
	
	private HashMap<String,List<String>> prefixMap;
	private List<String> infoLines;
	
	public InfoMapFileContent() {
		this.prefixMap = new HashMap<>();
		this.infoLines = new ArrayList<String>();
	}

	public HashMap<String, List<String>> getPrefixMap() {
		return prefixMap;
	}

	public void setPrefixMap(HashMap<String, List<String>> prefixMap) {
		this.prefixMap = prefixMap;
	}

	public List<String> getInfoLines() {
		return infoLines;
	}

	public void setInfoLines(List<String> infoLines) {
		this.infoLines = infoLines;
	}
	
}
