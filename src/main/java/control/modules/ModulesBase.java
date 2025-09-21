package control.modules;

import java.util.logging.ConsoleHandler;
import java.util.logging.Logger;

import control.main.LogFormatterSingleton;

/**
 * Base class for all modules.
 * @author michael
 */
public class ModulesBase{
	public ModulesBase(Logger log){		
		log.setUseParentHandlers(false);
		ConsoleHandler ch = new ConsoleHandler();
		ch.setFormatter(LogFormatterSingleton.getInstance());
		ch.setLevel(LogFormatterSingleton.OVERALL_LEVEL);
		log.addHandler(ch);
		log.setLevel(LogFormatterSingleton.OVERALL_LEVEL);	
	}			
}