package model.MCU;

import java.util.logging.Level;
import java.util.logging.Logger;

import utils.enums.MacroblockSize;
import utils.enums.TransformBlockType;
import model.JPegBase;

/**
 * A standard MacroBlock with 8 * 8 dimension
 * 			[row][column]
 * 
 *  (Minimum Coded Unit) MCU blocks of size 
 *  	 8x8 (4:4:4 no subsampling)
 *  	16x8 (4:2:2)
 *     16x16 (4:2:0) 
 * 
 * @author michael
 */
public class Macroblock extends JPegBase {
	private static final Logger log=Logger.getLogger("Macroblock");
	
	private TransformBlock luminanceTransformBlock1=null;
	private TransformBlock luminanceTransformBlock2=null;
	private TransformBlock luminanceTransformBlock3=null;
	private TransformBlock luminanceTransformBlock4=null;
	
	private TransformBlock chrominanceBlueTransformBlock=null;
	
	private TransformBlock chrominanceRedTransformBlock=null;
	
	private MacroblockSize blockSize=MacroblockSize.UNSET;
	
	/**
	 * This determines and configure the minimal coding unit alias Macroblock.
	 * @param blockSize
	 */
	public Macroblock(MacroblockSize blockSize){
		super(log);
		this.blockSize=blockSize;
		
		if (blockSize==MacroblockSize.EIGHT_TIMES_EIGHT){
			this.luminanceTransformBlock1=new TransformBlock(TransformBlockType.LUMINANCE);
			this.luminanceTransformBlock2=null;
			this.luminanceTransformBlock3=null;
			this.luminanceTransformBlock4=null;
			this.chrominanceBlueTransformBlock=new TransformBlock(TransformBlockType.CHROMINANCE_BLUE);
			this.chrominanceRedTransformBlock=new TransformBlock(TransformBlockType.CHROMINANCE_RED);
		}else if (blockSize==MacroblockSize.SIXTEEN_TIMES_EIGHT){
			this.luminanceTransformBlock1=new TransformBlock(TransformBlockType.LUMINANCE);
			this.luminanceTransformBlock2=new TransformBlock(TransformBlockType.LUMINANCE);
			this.luminanceTransformBlock3=new TransformBlock(TransformBlockType.LUMINANCE);
			this.luminanceTransformBlock4=new TransformBlock(TransformBlockType.LUMINANCE);
			this.chrominanceBlueTransformBlock=new TransformBlock(TransformBlockType.CHROMINANCE_BLUE);
			this.chrominanceRedTransformBlock=new TransformBlock(TransformBlockType.CHROMINANCE_RED);
		}else if (blockSize==MacroblockSize.SIXTEEN_TIMES_SIXTEEN){
			this.luminanceTransformBlock1=new TransformBlock(TransformBlockType.LUMINANCE);
			this.luminanceTransformBlock2=new TransformBlock(TransformBlockType.LUMINANCE);
			this.luminanceTransformBlock3=new TransformBlock(TransformBlockType.LUMINANCE);
			this.luminanceTransformBlock4=new TransformBlock(TransformBlockType.LUMINANCE);
			this.chrominanceBlueTransformBlock=new TransformBlock(TransformBlockType.CHROMINANCE_BLUE);
			this.chrominanceRedTransformBlock=new TransformBlock(TransformBlockType.CHROMINANCE_RED);
		}else{
			log.log(Level.SEVERE, ": ERROR Unknown MacroblockSize="+blockSize+" exit with -1209");
			System.exit(-1209);
		}
	}

	// Getter/Setter +++++++++++++++++++++++++++++++++++++++++++++
	public TransformBlock getLuminanceTransformBlock1() {
		return luminanceTransformBlock1;
	}

	public TransformBlock getLuminanceTransformBlock2() {
		return luminanceTransformBlock2;
	}

	public TransformBlock getLuminanceTransformBlock3() {
		return luminanceTransformBlock3;
	}

	public TransformBlock getLuminanceTransformBlock4() {
		return luminanceTransformBlock4;
	}

	public TransformBlock getChrominanceBlueTransformBlock() {
		return chrominanceBlueTransformBlock;
	}

	public TransformBlock getChrominanceRedTransformBlock() {
		return chrominanceRedTransformBlock;
	}

	public MacroblockSize getBlockSize() {
		return blockSize;
	}
	
	public void setLuminanceTransformBlock1(TransformBlock luminanceTransformBlock1) {
		this.luminanceTransformBlock1 = luminanceTransformBlock1;
	}

	public void setLuminanceTransformBlock2(TransformBlock luminanceTransformBlock2) {
		this.luminanceTransformBlock2 = luminanceTransformBlock2;
	}

	public void setLuminanceTransformBlock3(TransformBlock luminanceTransformBlock3) {
		this.luminanceTransformBlock3 = luminanceTransformBlock3;
	}

	public void setLuminanceTransformBlock4(TransformBlock luminanceTransformBlock4) {
		this.luminanceTransformBlock4 = luminanceTransformBlock4;
	}

	public void setChrominanceBlueTransformBlock(
			TransformBlock chrominanceBlueTransformBlock) {
		this.chrominanceBlueTransformBlock = chrominanceBlueTransformBlock;
	}

	public void setChrominanceRedTransformBlock(
			TransformBlock chrominanceRedTransformBlock) {
		this.chrominanceRedTransformBlock = chrominanceRedTransformBlock;
	}
}