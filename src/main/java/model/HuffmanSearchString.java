package model;

import java.util.logging.Logger;

/**
 * Representation of the current search string.
 * @author michael
 */
public class HuffmanSearchString extends JPegBase  {
	private static final Logger log=Logger.getLogger("HuffmanSearchString");
	
	private int currentIntArr[]=null;
	private int pos=-1;	
	
	/**
	 * Constructor.
	 */
	public HuffmanSearchString(){
		super(log);
		this.currentIntArr=new int[]{-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1};
		this.pos=0;
	}
	
	/**
	 * Constructor.
	 * @param currentIntArr
	 */
	public HuffmanSearchString(int currentIntArr[]){
		super(log);
		this.currentIntArr=currentIntArr;
		this.pos=0;
	}
	
	/**
	 *Add next bit to the current search string.
	 * @param nextBit
	 * @return
	 */
	public boolean addNextBit(int nextBit){
		this.currentIntArr[this.pos]=nextBit;
		if (this.pos>=16){
			return false;
		}else{
			this.pos++;
			return true;
		}
	}
		
	/**
	 * To string function.
	 */
	@Override
	public String toString(){
		String returnString="";
		
		for (int i=0;i<this.getSize();i++){
			if (this.currentIntArr[i]==-1){
				returnString+=".";
			}else{
				returnString+=""+this.currentIntArr[i];
			}
		}
		
		return returnString;
	}
	
	// Getter/Setter ++++++++++++++++++++
	public int getPos() {
		return pos;
	}

	public int[] getCurrentIntArr() {
		return currentIntArr;
	}

	public int getSize(){
		return 16;
	}	
}