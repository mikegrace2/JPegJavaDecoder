package model;

import java.util.logging.Logger;

/**
 * Contains the Quantization table from the JPeg file.
 * @author michael
 */
public class QuantizationTable extends JPegBase  {
	private static final Logger log=Logger.getLogger("QuantizationTable");	
	
	/**
	 * Constructor.
	 */
	public QuantizationTable(){
		super(log);
	}			
	
	// 0xdb DQT - Define Quantization Table(s)
	public char dqtMarker=0;
	public int dqtSize=-1;
	public char dqtID=0;
	
	public int dqtTableIdentifier=-1;
	public int dqtQuantValueSize=-1;
	
	public int dqtDataArrayArray[][]=null; 
	
	/**
	 * to String function.
	 */
	@Override
	public String toString(){
		String returnString="";
		returnString+="db Payload: " + this.dqtSize+ " DQT - Define Quantization Table(s)\n";
		returnString+="ID="+Integer.toHexString(this.dqtID)+"\tdqtTableIdentifier="+this.dqtTableIdentifier+"\tdqtQuantValueSize="+this.dqtQuantValueSize+" (0=1 byte 1=two bytes)\n";
		returnString+=this.intArrayToDecString(this.dqtDataArrayArray)+"\n";
		returnString+="----------------------------------------------------------------------\n";
		
		return returnString;
	}	
}