package model;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Bit by bit reader for the image data.
 * @author michael
 */
public class BitByBitReader extends JPegBase {	
	private static final Logger log=Logger.getLogger("BitByBitReader");
	
	private char sosImageDataArray[]=null;
	private int bytePosition=-1;
	private int bitPosition=-1;
	
	/**
	 * public constructor
	 * @param sosImageDataArray
	 */
	public BitByBitReader(char sosImageDataArray[]){
		super(log);
		
		this.sosImageDataArray=sosImageDataArray;
		this.bytePosition=0;
		this.bitPosition=7;
		
		log.log(Level.FINEST, "1: sosImageDataArray.length="+sosImageDataArray.length);
	}
	
	/**
	 * Get data for an debugging preview view.
	 * @param maxBytes
	 * @return
	 */
	public String getDataPreview(int maxBytes){
		String returnString="\n\nCurrent byte.bit Position="+this.bytePosition+"."+this.bitPosition+" length="+this.sosImageDataArray.length+" bytes\n";
		String tempLine1="";
		String tempLine2="";
		
		for (int i=this.bytePosition;i<sosImageDataArray.length && i<=(maxBytes+this.bytePosition);i++){
			tempLine1+="|"+BitByBitReader.checkAndAddOneZero(Integer.toHexString(this.sosImageDataArray[i]))+" ("+BitByBitReader.checkAndAddTwoZeros((new Integer(i).toString()))+")  ";
			tempLine2+="|"+this.transformToBin(this.sosImageDataArray[i])+" ";
		}
		
		returnString+=tempLine1+"\n";
		returnString+=tempLine2+"\n";
		
		return returnString;
	}
	
	/**
	 * Output function which adds a zero if length is one.
	 * @param input
	 * @return
	 */
	public static String checkAndAddOneZero(String input){
		if (input.length()==1){
			return "0"+input;
		}else{
			return input;
		}
	}
	
	/**
	 * Output function which adds two zero if length is one or two.
	 * @param input
	 * @return
	 */
	public static String checkAndAddTwoZeros(String input){
		if (input.length()==1){
			return "00"+input;
		}else if (input.length()==2){
			return "0"+input;
		}else{
			return input;
		}
	}	
	
	/**
	 * Transform a character array into a string array.
	 * @param input
	 * @return
	 */
	private String transformToBin(char input){
		String returnString="";
		
		for (int i = 7; i >= 0; i--){
			int val=(input&(1 << i));
			
			if (i==3)
				returnString+=" ";
			
			if (val==0)
				returnString+="0";
			else
				returnString+="1";
		}
		
		return returnString;
	}	
		
	/**
	 * Start again.
	 */
	public void resetToStart(){
		this.bytePosition=0;
		this.bitPosition=7;		
	}
	
	/**
	 * Get the next bit from the data stream.
	 * @return
	 */
	public int getNextBit(){
		int returnValue=-1;
		
		try{
			char currentByte=this.sosImageDataArray[this.bytePosition];
			int tempVal=currentByte & (1 << this.bitPosition);
			log.log(Level.FINEST, "1: \t\t\tcurrentByte="+Integer.toHexString(currentByte)+" tempVal="+tempVal+" this.bitPosition="+this.bitPosition);
		
			if (tempVal==0)
				returnValue=0;
			else 
				returnValue=1;
	
			if (this.bitPosition==0){
				this.bytePosition=this.bytePosition+1;
				log.log(Level.FINEST, "2: this.bytePosition="+this.bytePosition);
				if (this.sosImageDataArray.length==this.bytePosition){
					log.log(Level.FINEST, "3:  All bits delivered, nothing left! this.sosImageDataArray.length="+this.sosImageDataArray.length+" this.bytePosition="+this.bytePosition);
					return -1;
				}
				this.bitPosition=7;
			}else{
				this.bitPosition=this.bitPosition-1;
			}
		} catch (ArrayIndexOutOfBoundsException e){
			log.log(Level.FINEST, "4: Length has reached end, nothing left yet! this.sosImageDataArray.length="+this.sosImageDataArray.length+" this.bytePosition="+this.bytePosition);
			return -1;
		}
		
		return returnValue;
	}
	
	// Getter +++++++++++++++++++++++++++++++
	public int getSize(){
		return this.sosImageDataArray.length;
	}
	
	public int getSizeInBits(){
		return this.sosImageDataArray.length*8;
	}	
}