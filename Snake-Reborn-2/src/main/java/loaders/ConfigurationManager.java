package loaders;

import static loaders.LoadersConstants.END_FILE_MARKER;
import static loaders.LoadersConstants.LINE_END_MARKER;
import static loaders.LoadersConstants.LINE_START_MARKER;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import support.OSdetector;

public class ConfigurationManager {
	private HashMap<String,String> settings;
	private static final String FILE_NAME = ("config"+OSdetector.getPathSeparator()+"userConfig.ini");
	
	public ConfigurationManager() throws IOException {
		this.readFile();
	}

	public void updateFile() throws IOException {
		LinkedList<String> keys = new LinkedList<String>();
		keys.addAll(this.settings.keySet());
		String fileContent = "";
		for(String tempKey:keys) {
			fileContent += LINE_START_MARKER;
			fileContent += tempKey;
			fileContent += LINE_END_MARKER;
			fileContent += "\n";
			fileContent += "  " + LINE_START_MARKER;
			fileContent += this.settings.get(tempKey);
			fileContent += LINE_END_MARKER;
			fileContent += "\n";
		}
		fileContent += END_FILE_MARKER;

		support.FileHandler.writeFile(FILE_NAME, fileContent);
	}
	
	public void readFile() throws IOException {
		settings = new HashMap<String,String>();
		String text = support.FileHandler.readFile(FILE_NAME);
		String line = new String();
		String key = new String();
		String value = new String();
		Boolean isKey = false;
		ArrayList<Character> chars = new ArrayList<>();
		chars.addAll(support.Utility.stringToArray(text));

		boolean validLine=false;

		for(char c:chars){
			if (c!=END_FILE_MARKER){
				if(c==LINE_START_MARKER){
					line = new String();
					validLine=true;
					if(isKey==true) {
						isKey = false;
					} else {
						isKey = true;
					}
				}
				if(c==LINE_END_MARKER){
					validLine=false;
					if(isKey) {
						key = new String(line);
					} else {
						value = new String(line);
						settings.put(key, value);
					}
				}
				if(validLine && (c!=LINE_START_MARKER && c!=LINE_END_MARKER)){
					line+=c;
				}
			}
		}
	}

	public String getSetting(String key) throws IOException {
		return this.settings.get(key);
	}
	
	public void updateSetting(String key, String value) throws IOException {
		this.settings.put(key, value);
	}

}
