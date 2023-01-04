package support;

import java.util.Collection;
import java.util.LinkedList;

public class Utility {

	public static Collection<? extends Character> stringToArray(String string) {
		LinkedList<Character> characters = new LinkedList<>();
		for(int i = 0; i<string.length(); i++){
			characters.add(string.charAt(i));
		}
		return characters;
	}
	
	public static boolean isEven(int number){
		if((number%2)!=0) return false;
		return true;
	}

	public static boolean truePercentage(int percentage){
		if(percentage<0||percentage>100) 
			throw new IllegalArgumentException("Percentage number must be between 0 and 100!");
		int randomNumber = getRandomPercentageValueFrom1To100();
		if(randomNumber<=percentage) return true;
		return false;
	}
	
	public static int getRandomPercentageValueFrom1To100() {
		// from 1 to 100
		return (int) ((Math.random()*100)+1);
	}
	
	public static String extendsStringWithSpaces(String string, int length) {
		String s = new String(string);
	    if(s.length()<length) {
	    	s+= new String(new char[length - s.length()]).replace('\0', ' ');
	    }
	    return s;
	}
	
}
