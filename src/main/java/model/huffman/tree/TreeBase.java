package model.huffman.tree;

import java.util.logging.ConsoleHandler;
import java.util.logging.Logger;

import control.main.LogFormatterSingleton;

/**
 * Basis class for the Tree.
 * @author michael
 */
public class TreeBase {
	public TreeBase(Logger log){
		log.setUseParentHandlers(false);
		ConsoleHandler ch = new ConsoleHandler();
		ch.setFormatter(LogFormatterSingleton.getInstance());
		ch.setLevel(LogFormatterSingleton.OVERALL_LEVEL);
		log.addHandler(ch);
		log.setLevel(LogFormatterSingleton.OVERALL_LEVEL);	        
	}	
}