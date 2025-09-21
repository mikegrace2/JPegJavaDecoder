package model;

import java.util.logging.Logger;

import model.huffman.tree.HuffmanTree;

/**
 * Representation of the Huffman table from the JPeg file.
 * @author michael
 */
public class HuffmanInputTable extends JPegBase {
	private static final Logger log=Logger.getLogger("HuffmanInputTable");	
	
	/**
	 * Constructor.
	 */
	public HuffmanInputTable(){
		super(log);
	}		
	
	// 0xc4 DHT - Define Huffman Table(s)
	public char dhtMarker=0;
	public int dhtSize=-1;
	public char dhtID=0;
	public int dhtClass=-1;
	public int dhtTableId=-1;
	public char dht16BitArray[]=null; 
	public char dhtHuffmanArrayArray[][]=null; 
	public HuffmanTree dhtHuffmanTree=null;
	
	/**
	 * is it a DHT table?
	 * @return
	 */
	public boolean isDHTTable(){
		if (this.dhtClass==0)
			return true;
		else
			return false;
	}
	
	/**
	 * Is it a ACT table.
	 * @return
	 */
	public boolean isACTTable(){
		if (this.dhtClass==1)
			return true;
		else
			return false;
	}
	
	/**
	 * Is baseline frame?
	 * @return
	 */
	public boolean isBaselineFrames(){
		if (this.dhtTableId==0 || this.dhtTableId==1)
			return true;
		else
			return false;
	}
	
	/**
	 * Is progressive and extended frame?
	 * @return
	 */
	public boolean isProgressiveAndExtendedFrames(){
		// I am not so sure here, documentation is some kind of unclear...
		if (this.dhtTableId >=2)
			return true;
		else
			return false;
	}
	
	/**
	 * To string function.
	 */
	public String toString(){
		String returnString;
		returnString="DHT-Define Huffman Table(s) --- c4 --- Payload/size: " + this.dhtSize+ " bytes(dec) ---  ID="+(int)this.dhtID+"(dec) class="+(int)this.dhtClass+"(dec) tableId="+(int)this.dhtTableId+"(dec)\n";
		returnString+="                 DHT table="+this.isDHTTable()+"\n                 ACT table="+this.isACTTable()+"\n            BaselineFrames="+this.isBaselineFrames()+"\nProgressive/extendedFrames="+this.isProgressiveAndExtendedFrames()+"\n\n";
		returnString+="BIT distribution\n";
		returnString+="1\t2\t3\t4\t5\t6\t7\t8\t9\t10\t11\t12\t13\t14\t15\t16\n";
		returnString+=this.charArrayToHexString(this.dht16BitArray)+"\n\n";		
		returnString+="BIT codeword assignment\n";
		
		for (int i=0;i<this.dht16BitArray.length;i++)
			returnString+="BITS["+(i+1)+"] length="+this.dhtHuffmanArrayArray[i].length+" data=\t"+charArrayToHexString(this.dhtHuffmanArrayArray[i])+"\n";
		
		returnString+="\n----------------------------------------------------------------------\n";
		
		return returnString;
	}
}