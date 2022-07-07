package loaders;

import static loaders.LoadersConstants.END_FILE_MARKER;
import static loaders.LoadersConstants.LINE_END_MARKER;
import static loaders.LoadersConstants.LINE_START_MARKER;
import static loaders.LoadersConstants.PREFIX_END_MARKER;
import static loaders.LoadersConstants.PREFIX_START_MARKER;
import static loaders.LoadersConstants.VALUE_ASSIGN_MARKER;
import static loaders.LoadersConstants.LIST_SEPARATOR_MARKER;

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
			charList.addAll(Utility.stringToArray(fileContent));
			String status = IGNORE;
			InfoMapFileContent result = new InfoMapFileContent();
			for(char c:charList){
				if(c!=END_FILE_MARKER){
					if(status == PREFIX && c==PREFIX_END_MARKER) {
						//prefix section end
						//result.setPrefix(prefix);
						String[] content = prefix.split(String.valueOf(VALUE_ASSIGN_MARKER));
						String prefixKey = content[0];
						String prefixValue = content[1];
						List<String> prefixValues = Arrays.asList(prefixValue.split(String.valueOf(LIST_SEPARATOR_MARKER)));
						result.getPrefixMap().put(prefixKey,prefixValues);
						prefix = "";
						status = IGNORE;
					} 
					else if(status == READING_ELEMENT && c==LINE_END_MARKER){
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
					else if(status == IGNORE && c == PREFIX_START_MARKER) {
						//prefix section begin
						status = PREFIX;
					}
					else if(status == IGNORE && c==LINE_START_MARKER){
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
			throw new IllegalArgumentException("Error while reading file '" + sourceForErrorMessage + "'\n" +
			"Error details: \n" +
			"  - Prefix: " + PREFIX_START_MARKER + prefix + PREFIX_END_MARKER + "\n" +
			"  - Content: " + LINE_START_MARKER + actualLineContent + LINE_END_MARKER + "> ");
		}
	}

}
