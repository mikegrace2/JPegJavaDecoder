package model.MCU;

import java.util.logging.Level;
import java.util.logging.Logger;

import utils.Exceptions.MCBIsFullException;
import utils.enums.TransformBlockType;
import control.modules.Quantizizer;
import model.JPegBase;

/**
 * A tranform block is the lowest element in the jpeg data structure and it is always 8*8 in size.
 * @author michael
 */
public class TransformBlock  extends JPegBase {
	private static final Logger log=Logger.getLogger("TransformBlock");
	
	// It is in jpeg always 8*8
	public static final int DIM_8TIMES8=8;
	
	private TransformBlockType transformBlockType=null;
	private int transformBlock[][] = null;
	private int zigZag8times8[][]=new Quantizizer().zigZag8times8;
	private int pos=0;
	
	// For the transformation
	public int red[][]=null;
	public int green[][]=null;
	public int blue[][]=null;

	/**
	 * Constructor.
	 * @param transformBlockType
	 */
	public TransformBlock(TransformBlockType transformBlockType){
		super(log);
		this.transformBlockType=transformBlockType;
		this.transformBlock= new int[TransformBlock.DIM_8TIMES8][TransformBlock.DIM_8TIMES8];
		this.pos=0;
		
		for (int row=0;row<TransformBlock.DIM_8TIMES8;row++){
			for (int column=0;column<TransformBlock.DIM_8TIMES8;column++){
				this.transformBlock[row][column]=-1;
			}
		}
	}	

	/**
	 * Constructor.
	 * @param transformBlockType
	 * @param transformBlock
	 */
	public TransformBlock(TransformBlockType transformBlockType, int transformBlock[][]){
		super(log);	
		this.transformBlockType=transformBlockType;		
		this.transformBlock=transformBlock;
	}	
		
	/**
	 * Add the next value to the current Transformblock.
	 * @param nextValue
	 * @throws MCBIsFullException
	 */
	public void addNextValue(int nextValue) throws MCBIsFullException {
		int row=-1;
		int column=-1;		
		
		log.log(Level.FINEST, "a: (TransformBlock.DIM8*TransformBlock.DIM8)="+(TransformBlock.DIM_8TIMES8*TransformBlock.DIM_8TIMES8));
		
		if (this.pos>=(TransformBlock.DIM_8TIMES8*TransformBlock.DIM_8TIMES8)){
			log.log(Level.SEVERE, "b: ERROR this.pos is bigger or equal "+this.pos);
			throw new MCBIsFullException();
		}
		
		try{
			row=this.zigZag8times8[this.pos][0];
			column=this.zigZag8times8[this.pos][1];
			
			this.transformBlock[row][column]=nextValue;
			log.log(Level.FINEST, "c: ["+row+"]["+column+"] value="+nextValue);
		
			this.pos++;
		} catch (ArrayIndexOutOfBoundsException ex){
			log.log(Level.SEVERE, "d: ArrayIndexOutOfBoundsException row="+row+" column="+column);
			throw new MCBIsFullException();
		}
	}
			
	/**
	 * Is current Transformblock full?
	 * @return
	 */
	public boolean isFull(){
		int overallSize=TransformBlock.DIM_8TIMES8*TransformBlock.DIM_8TIMES8;
		log.log(Level.FINEST, ": overallSize overallSize overallSize overallSize overallSize overallSize overallSize overallSize="+overallSize);
				
		if (this.pos>=overallSize){
			return true;
		}else{
			return false;
		}
	}	
	
	/**
	 * Print one block
	 * @param input
	 */
	public String toStringHex(){
		String returnString="";
		
		// Print the rest ACT values
		for (int i=0;i<TransformBlock.DIM_8TIMES8;i++){
			String tempLine1="";
			String tempLine2="";
						
			for (int j=0;j<TransformBlock.DIM_8TIMES8;j++){
				if (i==0 && j==0){
					// Print DCT value in decimal not in hex and mark it with parenthesis
					if (this.transformBlock[0][0]<0){
						tempLine2+="("+Integer.toHexString(this.transformBlock[0][0])+")\t";
					}else{
						tempLine2+=" ("+Integer.toHexString(this.transformBlock[0][0])+")\t\t";
					}
					tempLine1+="["+i+":"+j+"]\t\t";
				}else{
					// Rest ACT's print simple
					tempLine2+="  "+Integer.toHexString(this.transformBlock[i][j])+"\t\t";
					tempLine1+="["+i+":"+j+"]\t\t";
				}
			}
			returnString+=tempLine1+"\n";
			returnString+=tempLine2+"\n";
		}
		
		return returnString;
	}
	
	/**
	 * Print one block
	 * @param input
	 */
	public String toStringDec(){
		String returnString="";
		
		// Print the rest ACT values
		for (int i=0;i<TransformBlock.DIM_8TIMES8;i++){
			String tempLine1="";
			String tempLine2="";		

			for (int j=0;j<TransformBlock.DIM_8TIMES8;j++){
				tempLine1+="["+i+":"+j+"]\t";
				tempLine2+="  "+(int)this.transformBlock[i][j]+"\t";
			}
			
			returnString+=tempLine1+"\n";
			returnString+=tempLine2+"\n";
		}
		
		return returnString;
	}	
	
	// Getter/Setter +++++++++++++++++++++++++++++++++++++
	public void setImageBlock(int[][] imageBlock) {
		this.transformBlock = imageBlock;
	}

	public int[][] getTransformBlock() {
		return this.transformBlock;
	}
	
	public int getPosition(){
		return this.pos;
	}
		
	public TransformBlockType getTransformBlockType() {
		return transformBlockType;
	}

	public void setTransformBlockType(TransformBlockType transformBlockType) {
		this.transformBlockType = transformBlockType;
	}
}