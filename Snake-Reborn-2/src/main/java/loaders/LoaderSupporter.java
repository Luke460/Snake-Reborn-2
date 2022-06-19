package loaders;

import static supporto.Costanti.CARATTERE_FINE_FILE;
import java.util.ArrayList;
import supporto.Utility;

public class LoaderSupporter {
	
	final static String IGNORE = "IGNORE";
	final static String PREFIX = "PREFIX";
	final static String READING_ELEMENT = "ELEMENT";
	
	public static InfoMapFileContent getInfoMapFileContent(String fileContent) {
		ArrayList<Character> charList = new ArrayList<Character>();
		charList.addAll(Utility.stringaToArray(fileContent));
		String status = IGNORE;
		String prefix = "";
		String actualLineContent = "";
		InfoMapFileContent result = new InfoMapFileContent();
		for(char c:charList){
			if(c!=CARATTERE_FINE_FILE){
				if(status == PREFIX && c==']') {
					//prefix section end
					result.setPrefix(prefix);
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
			}
		}
		return result;
	}

}
