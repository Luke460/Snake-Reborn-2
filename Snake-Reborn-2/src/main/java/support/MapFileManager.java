package support;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class MapFileManager {
	
	public static final char END_FILE_CHAR = '$';
	public static final char START_LINE_CHAR = '<';
	public static final char END_LINE_CHAR = '>';
	private HashMap<String,String> map;
	private String fileName, finalComments;
	
	public MapFileManager(String fileName) {
		this.fileName = fileName;
		this.finalComments = new String();
	}
	
	public void updateFile() throws IOException {
		LinkedList<String> keys = new LinkedList<String>();
		keys.addAll(this.map.keySet());
		String fileContent = "";
		for(String tempChiave:keys) {
			fileContent += "<";
			fileContent+=tempChiave;
			fileContent+= ">";
			fileContent+= "\n";
			fileContent += "  <";
			fileContent+=this.map.get(tempChiave);
			fileContent+= ">";
			fileContent+= "\n";
		}
		fileContent += "$\n" + this.finalComments;
		FileHandler.writeFile(fileName, fileContent);
	}
	
	public void readFile() throws IOException {
		map = new HashMap<String,String>();
		String text = FileHandler.readFile(fileName);
		String line = new String();
		String key = new String();
		String value = new String();
		Boolean isKey = false;
		ArrayList<Character> chars = new ArrayList<>();
		chars.addAll(Utility.stringToArray(text));

		boolean validLine=false;

		for(char c:chars){
			if (c!=END_FILE_CHAR){ // finche' il file non � finito...
				// controllo
				if(c==START_LINE_CHAR){
					line = new String();
					validLine=true;
					if(isKey==true) {
						isKey = false;
					} else {
						isKey = true;
					}
				}
				if(c==END_LINE_CHAR){
					validLine=false;
					if(isKey) {
						key = new String(line);
					} else {
						value = new String(line);
						map.put(key, value);
					}
				}
				// fine controllo
				if(validLine && (c!=START_LINE_CHAR && c!=END_LINE_CHAR)){
					line+=c;
				}
			} else {
				this.finalComments+=c;
			}
		}
	}

	public String get(String key) {
		return this.map.get(key);
	}
	
	public void put(String key, String value) {
		this.map.put(key, value);
	}
	
	public HashMap<String, String> getMap() {
		return map;
	}
	
	public void setMap(HashMap<String, String> map) {
		this.map = map;
	}

}
