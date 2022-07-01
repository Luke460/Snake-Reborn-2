package loaders;

import static support.Costanti.CARATTERE_FINE_FILE;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import support.Utility;

public class LoaderSupporter {
	
	final static String IGNORE = "IGNORE";
	final static String PREFIX = "PREFIX";
	final static String READING_ELEMENT = "ELEMENT";
	
	public static InfoMapFileContent getInfoMapFileContent(String fileContent, String sourceForErrorMessage) {
		String prefix = "";
		String actualLineContent = "";
		try {
			ArrayList<Character> charList = new ArrayList<Character>();
			charList.addAll(Utility.stringaToArray(fileContent));
			String status = IGNORE;
			InfoMapFileContent result = new InfoMapFileContent();
			for(char c:charList){
				if(c!=CARATTERE_FINE_FILE){
					if(status == PREFIX && c==']') {
						//prefix section end
						//result.setPrefix(prefix);
						String[] content = prefix.split(":");
						String prefixKey = content[0];
						String prefixValue = content[1];
						List<String> prefixValues = Arrays.asList(prefixValue.split(","));
						result.getPrefixMap().put(prefixKey,prefixValues);
						prefix = "";
						status = IGNORE;
					} 
					else if(status == READING_ELEMENT && c=='>'){
						//end line
						result.getInfoLines().add(actualLineContent);
						status = IGNORE;
						actualLineContent = "";
					} 
					else if(status!=IGNORE){
						//reading
						if(status == READING_ELEMENT){
							actualLineContent += c;
						} else if(status == PREFIX){
							prefix += c;
						}
					} 
					else if(status == IGNORE && c == '[') {
						//prefix section begin
						status = PREFIX;
					}
					else if(status == IGNORE && c=='<'){
						//inizio riga
						status = READING_ELEMENT;
					}
				} else {
					break;
				}
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			throw new IllegalArgumentException("Errore durante l'elaborazione del file '" + sourceForErrorMessage + "'\n" +
			"Dettagli posizione errore: \n" +
			"  - Prefix: ["+prefix+"] \n" +
			"  - Content: <"+actualLineContent+"> ");
		}
	}

}
