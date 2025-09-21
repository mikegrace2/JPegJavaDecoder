package model;

import java.util.logging.ConsoleHandler;
import java.util.logging.Logger;

import control.main.LogFormatterSingleton;

/**
 * Basis class for all models. Contains logging facility functions and some utility methods.
 * @author michael
 */
public class JPegBase {
	public JPegBase(Logger log){
		log.setUseParentHandlers(false);
		ConsoleHandler ch = new ConsoleHandler();
		ch.setFormatter(LogFormatterSingleton.getInstance());
		ch.setLevel(LogFormatterSingleton.OVERALL_LEVEL);
		log.addHandler(ch);
		log.setLevel(LogFormatterSingleton.OVERALL_LEVEL);	        
	}		
	
	/**
	 * Translate a char array into a hex char String.s
	 * @param input
	 * @return
	 */
	protected String charArrayToHexString(char[] input) {
		if (input==null)
			return "";
		
		String returnString="";
		for (int i = 0; i < input.length; i++)
			returnString+=Integer.toHexString(input[i]) + "\t";
		
		return returnString;
	}
	
	/**
	 * Translate a 2 dimension char array into a hex String.
	 * @param input
	 * @return
	 */
	protected String charArrayToHexString(char[][] input) {
		String returnString="";
		
		for (int i = 0; i < input[0].length; i++) {
			for (int j=0;j< input[1].length;j++){
				returnString+=Integer.toHexString(input[i][j]) + "\t";
			}
			returnString+="\n";
		}
		
		return returnString;
	}
	
	/**
	 * Translate a 2 dimension int array into a hex String.
	 * @param input
	 * @return
	 */
	protected String intArrayToHexString(int[][] input) {
		String returnString="";
		
		for (int i = 0; i < input[0].length; i++) {
			for (int j=0;j< input[1].length;j++){
				returnString+=Integer.toHexString(input[i][j]) + "\t";
			}
			returnString+="\n";
		}
		
		return returnString;
	}
	
	/**
	 * Translate a 2 dimension int array into a decimal String.
	 * @param input
	 * @return
	 */
	protected String intArrayToDecString(int[][] input) {
		String returnString="";
		
		for (int i = 0; i < input[0].length; i++) {
			for (int j=0;j< input[1].length;j++){
				returnString+=(int)input[i][j] + "\t";
			}
			returnString+="\n";
		}
		
		return returnString;
	}		
}