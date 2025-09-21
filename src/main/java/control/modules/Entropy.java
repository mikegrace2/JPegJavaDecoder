package control.modules;

import java.util.logging.Level;
import java.util.logging.Logger;

import utils.Exceptions.CannotFindHuffmanSearchString;
import utils.Exceptions.EndOfImageDataException;
import utils.Exceptions.MCBIsFullException;
import utils.enums.MacroblockSize;
import utils.enums.SubSampling;
import utils.enums.TransformBlockType;
import model.HuffmanSearchString;
import model.JPegData;
import model.MCU.Macroblock;
import model.MCU.TransformBlock;
import model.huffman.tree.HuffmanTree;

/**
 * Entropy decoding, Huffman and Run Length Coding.
 * @author michael
 */
public class Entropy extends ModulesBase{
	private static final Logger log=Logger.getLogger("Entropy");
	
	/**
	 * Constructor.
	 */
	public Entropy(){
		super(log);
	}
	
	/**
	 * Entropy decode the whole Image Data 
	 */
	public Macroblock[] entropyDecodeAll(JPegData jPegData){
		//new Huffman().testHuffmanTrees(jPegData);
		
		// Check for standard Huffman tables are they there?
		if (jPegData.huffmantables[0]==null ||
			jPegData.huffmantables[16]==null ||
			jPegData.huffmantables[1]==null ||
			jPegData.huffmantables[17]==null){
			log.log(Level.SEVERE, "A: ERROR jpeg is not using standard Huffman tables, so IMPLEMENT ME NOW! EXIT -43");
			System.exit(-43);
		}		

		// Start ********************************************************************
		Macroblock allMacroblock[]=new Macroblock[10000];
		
		int i=0;
		// Do the compression stuff 
		// 2:2 1:1 1:1 --- 4 Luminance Blocks and 2 Chrominance Blocks => 16×16 (4:2:0)
		if (jPegData.whichSubSamplingItIs(SubSampling._4_2_0)){
			log.log(Level.INFO, "B: color compression decompression is 2:2 1:1 1:1 => _4_2_0");
			
			for(i=0;;i++){						
				// Create a new Image Block which contains all three components
				Macroblock currentallMacroblock=new Macroblock(MacroblockSize.SIXTEEN_TIMES_SIXTEEN);
				
				currentallMacroblock.setLuminanceTransformBlock1(this.entropyDecodeNextImageBlock(jPegData, 0, 16, i, TransformBlockType.LUMINANCE));
				if (currentallMacroblock.getLuminanceTransformBlock1()==null){
					log.log(Level.INFO, "C: Image Data end reached! (LUMINANCE1)");
					return this.cutOffBlocksInArray(allMacroblock);
				}
				
				currentallMacroblock.setLuminanceTransformBlock2(this.entropyDecodeNextImageBlock(jPegData, 0, 16, i, TransformBlockType.LUMINANCE));
				if (currentallMacroblock.getLuminanceTransformBlock2()==null){
					log.log(Level.INFO, "D: Image Data end reached! (LUMINANCE2)");
					return this.cutOffBlocksInArray(allMacroblock);
				}

				currentallMacroblock.setLuminanceTransformBlock3(this.entropyDecodeNextImageBlock(jPegData, 0, 16, i, TransformBlockType.LUMINANCE));
				if (currentallMacroblock.getLuminanceTransformBlock3()==null){
					log.log(Level.INFO, "E: Image Data end reached! (LUMINANCE3)");
					return this.cutOffBlocksInArray(allMacroblock);
				}

				currentallMacroblock.setLuminanceTransformBlock4(this.entropyDecodeNextImageBlock(jPegData, 0, 16, i, TransformBlockType.LUMINANCE));
				if (currentallMacroblock.getLuminanceTransformBlock4()==null){
					log.log(Level.INFO, "F: Image Data end reached! (LUMINANCE4)");
					return this.cutOffBlocksInArray(allMacroblock);
				}
				
				currentallMacroblock.setChrominanceBlueTransformBlock(this.entropyDecodeNextImageBlock(jPegData,1, 17, i, TransformBlockType.CHROMINANCE_BLUE));
				if (currentallMacroblock.getChrominanceBlueTransformBlock()==null){
					log.log(Level.INFO, "G: Image Data end reached! (CHROMINANCE_BLUE)");
					return this.cutOffBlocksInArray(allMacroblock);
				}
					
				currentallMacroblock.setChrominanceRedTransformBlock(this.entropyDecodeNextImageBlock(jPegData,1, 17, i, TransformBlockType.CHROMINANCE_RED));
				if (currentallMacroblock.getChrominanceRedTransformBlock()==null){
					log.log(Level.INFO, "H: Image Data end reached! (CHROMINANCE_RED)");
					return this.cutOffBlocksInArray(allMacroblock);
				}
					
				allMacroblock[i]=currentallMacroblock;	
			}							
		// 4:2:2 --- 2 Luminance Blocks and 1 Chrominance Blue and 1 Chrominance Red
		} else if (jPegData.whichSubSamplingItIs(SubSampling._4_2_2)){
			log.log(Level.INFO, "M: color compression decompression is 4:2:2");
			
			for(i=0;;i++){						
				// Create a new Image Block which contains all three components
				Macroblock currentallMacroblock=new Macroblock(MacroblockSize.SIXTEEN_TIMES_EIGHT);
				
				currentallMacroblock.setLuminanceTransformBlock1(this.entropyDecodeNextImageBlock(jPegData, 0, 16, i, TransformBlockType.LUMINANCE));
				if (currentallMacroblock.getLuminanceTransformBlock1()==null){
					log.log(Level.INFO, "C: Image Data end reached! (LUMINANCE1)");
					return this.cutOffBlocksInArray(allMacroblock);
				}
				
				currentallMacroblock.setLuminanceTransformBlock2(this.entropyDecodeNextImageBlock(jPegData, 0, 16, i, TransformBlockType.LUMINANCE));
				if (currentallMacroblock.getLuminanceTransformBlock2()==null){
					log.log(Level.INFO, "D: Image Data end reached! (LUMINANCE2)");
					return this.cutOffBlocksInArray(allMacroblock);
				}
				
				currentallMacroblock.setChrominanceBlueTransformBlock(this.entropyDecodeNextImageBlock(jPegData,1, 17, i, TransformBlockType.CHROMINANCE_BLUE));
				if (currentallMacroblock.getChrominanceBlueTransformBlock()==null){
					log.log(Level.INFO, "G: Image Data end reached! (CHROMINANCE_BLUE)");
					return this.cutOffBlocksInArray(allMacroblock);
				}
					
				currentallMacroblock.setChrominanceRedTransformBlock(this.entropyDecodeNextImageBlock(jPegData,1, 17, i, TransformBlockType.CHROMINANCE_RED));
				if (currentallMacroblock.getChrominanceRedTransformBlock()==null){
					log.log(Level.INFO, "H: Image Data end reached! (CHROMINANCE_RED)");
					return this.cutOffBlocksInArray(allMacroblock);
				}
					
				allMacroblock[i]=currentallMacroblock;	
			}
			// 8×8 (4:4:4 – no subsampling)
		} else if (jPegData.whichSubSamplingItIs(SubSampling._4_4_4_no_subsampling)){
			log.log(Level.INFO, "I: color compression decompression is 1:1 1:1 1:1");
						
			for(i=0;;i++){						
				// Create a new Image Block which contains all three components
				Macroblock currentallMacroblock=new Macroblock(MacroblockSize.EIGHT_TIMES_EIGHT);
							
				currentallMacroblock.setLuminanceTransformBlock1(this.entropyDecodeNextImageBlock(jPegData, 0, 16, i, TransformBlockType.LUMINANCE));
				if (currentallMacroblock.getLuminanceTransformBlock1()==null){
					log.log(Level.INFO, "J: Image Data end reached! (LUMINANCE)");
					return this.cutOffBlocksInArray(allMacroblock);
				}
				
				currentallMacroblock.setChrominanceBlueTransformBlock(this.entropyDecodeNextImageBlock(jPegData,1, 17, i, TransformBlockType.CHROMINANCE_BLUE));
				if (currentallMacroblock.getChrominanceBlueTransformBlock()==null){
					log.log(Level.INFO, "K: Image Data end reached! (CHROMINANCE_BLUE)");
					return this.cutOffBlocksInArray(allMacroblock);
				}
				
				currentallMacroblock.setChrominanceRedTransformBlock(this.entropyDecodeNextImageBlock(jPegData,1, 17, i, TransformBlockType.CHROMINANCE_RED));
				if (currentallMacroblock.getChrominanceRedTransformBlock()==null){
					log.log(Level.INFO, "L: Image Data end reached! (CHROMINANCE_RED)");
					return this.cutOffBlocksInArray(allMacroblock);
				}
				
				allMacroblock[i]=currentallMacroblock;	
			}			
		}else{
			log.log(Level.SEVERE, "M: ERROR color compression/decrompression ratio is unknown. IMPLEMENT ME! exit(-19)");
			System.exit(-19);
		}		
				
		return this.cutOffBlocksInArray(allMacroblock);
	}
	
	/**
	 * Cutt off all null reference blocks and return an array only with filled Macroblocks.
	 * @param inputMacroblocks
	 * @return
	 */
	private Macroblock[] cutOffBlocksInArray(Macroblock[] inputMacroblocks){
		int size=0;
		for (;size<inputMacroblocks.length && inputMacroblocks[size]!=null;size++);
		
		Macroblock returnMacroblocks[]=new Macroblock[size];
		
		for (int j=0;j<size;j++){
			returnMacroblocks[j]=inputMacroblocks[j];
		}
		
		log.log(Level.FINE, ": allMacroblock.length="+inputMacroblocks.length+" returnMacroblocks.length="+returnMacroblocks.length);
		
		return returnMacroblocks;		
	}
	
	// **********************************************************************************************************************************
	// **********************************************************************************************************************************
	
	/**
	 * Decode the next image block.
	 * @param jPegData
	 * @param allImageBlocks
	 * @param dctTable
	 * @param actTable
	 * @param index
	 * @param blockType
	 * @return
	 */
	private TransformBlock entropyDecodeNextImageBlock(JPegData jPegData, int dctTable, int actTable, int index,  TransformBlockType blockType){
		TransformBlock currentTransformBlock = new TransformBlock(blockType);
				
		try {
			this.entropyDecode_DCT(jPegData, dctTable, index, currentTransformBlock);			
			this.entropyDecode_ACT(jPegData, actTable, index, currentTransformBlock);
			
			return currentTransformBlock;
		} catch (EndOfImageDataException e){
			log.log(Level.SEVERE, "a: ERROR can not complete current block because EndOfImageDataException!");
			return null;
		} catch (CannotFindHuffmanSearchString e){
			log.log(Level.SEVERE, "b: ERROR can not complete current block because CannotFindHuffmanSearchString!");
			return null;		
		} catch (MCBIsFullException e){
			log.log(Level.SEVERE, "c: ERROR can not complete current block because MCBIsFullException!");
			return null;			
		}
	}	

	/**
	 * Get the one and only DCT value.
	 * @param jPegData
	 * @param allImageBlocks
	 * @param dctTable
	 * @param actTable
	 * @param index
	 * @param blockType
	 * @return
	 * @throws MCBIsFullException 
	 * @throws CannotFindHuffmanSearchString 
	 * @throws EndOfImageDataException 
	 */
	private void entropyDecode_DCT(JPegData jPegData, int dctTable, int index,  TransformBlock currentTransformBlock) throws MCBIsFullException, EndOfImageDataException, CannotFindHuffmanSearchString{
		log.log(Level.FINE, "A MCB="+index+": dataPreview "+jPegData.sosBitByBitReader.getDataPreview(50));
		
		int runLengths=this.getNextCodeWord(jPegData, dctTable, index, currentTransformBlock);
							
		// Special case...if the value is zero the overall DCT is zero 
		if (runLengths==0){
			log.log(Level.FINE, "B: MCB="+index+" pos="+currentTransformBlock.getPosition()+" ADD (0)");
			currentTransformBlock.addNextValue(0);
		}else{// get the next bits ...
			int tempIntDCTArr[]=new int[runLengths];
			for (int i=0;i<runLengths;i++){
				tempIntDCTArr[i]=jPegData.sosBitByBitReader.getNextBit();
			}
	
			int dctValue=this.transformDCTBitsIntoADCTValue(tempIntDCTArr);
			log.log(Level.FINE, "C: MCB="+index+" pos="+currentTransformBlock.getPosition()+" ADD ("+dctValue+")");
			currentTransformBlock.addNextValue(dctValue);
		}
	}
	
	/**
	 * Entropy Huffman decoding.
	 * @param i2 
	 * @throws CannotFindHuffmanSearchString 
	 * @throws EndOfImageDataException 
	 * @throws MCBIsFullException 
	 */
	private void entropyDecode_ACT(JPegData jPegData, int actTable, int index, TransformBlock currentTransformBlock) throws EndOfImageDataException, CannotFindHuffmanSearchString, MCBIsFullException{		
		while (currentTransformBlock.isFull()==false){
			int nextCodeWord=this.getNextCodeWord(jPegData, actTable, index, currentTransformBlock);
			
			switch (nextCodeWord) {
			case 0xF0:  // ZRL(Zero run length)
				this.fillZeroRunLengths(currentTransformBlock);
				break;
			case 0:  // EndOfBlock
				this.fillEndOfBlock(jPegData, currentTransformBlock);
				break;
			default: // Standard Case
				addACTValue(jPegData, currentTransformBlock, nextCodeWord, index);
			}
		}
	}	
	
	/**
	 * Get the next code word.
	 * @param jPegData
	 * @param actTable
	 * @param index
	 * @param currentMacroBlock
	 * @return
	 * @throws EndOfImageDataException
	 */
	private int getNextCodeWord(JPegData jPegData, int actTable, int index, TransformBlock currentTransformBlock) throws EndOfImageDataException, CannotFindHuffmanSearchString{
		int codeWord=-1;
		HuffmanTree myHuffi=jPegData.huffmantables[actTable].dhtHuffmanTree;
		
		// Reset my current int array
		HuffmanSearchString myHuffmanSearchString=new HuffmanSearchString();
		
		// 16 times depend on the max depth of the Huffman tree.
		for (int j=0;j<myHuffmanSearchString.getSize();j++){
			int nextBit=jPegData.sosBitByBitReader.getNextBit();
			if (nextBit==-1){
				if (currentTransformBlock==null){
					log.log(Level.SEVERE, "A: MCB="+index+" searchString="+myHuffmanSearchString+" currentTransformBlock==null END OF IMAGE DATA REACHED!");
				}else{
					log.log(Level.SEVERE, "B: MCB="+index+" searchString="+myHuffmanSearchString+" pos="+currentTransformBlock.getPosition()+" END OF IMAGE DATA REACHED!");
				}
				
				throw new EndOfImageDataException();
			}
			myHuffmanSearchString.addNextBit(nextBit);
			
			codeWord=myHuffi.getCodeWord(myHuffmanSearchString);			
			if (codeWord!=-1){
				return codeWord;
			}
		}
		
		// Nothing found is bad, it means something is wrong with the Huffman coding
		throw new CannotFindHuffmanSearchString();
	}
	
	/**
	 * Add ACT value.
	 * @param jPegData
	 * @param currentTransformBlock
	 * @param nextCodeWord
	 * @param index
	 * @throws MCBIsFullException
	 */
	private void addACTValue(JPegData jPegData, TransformBlock currentTransformBlock, int nextCodeWord, int index) throws  MCBIsFullException{
		// Here it starts analyze codeword and split it up
		int zeroRunLength=(nextCodeWord&240) >> 4;
		int size=nextCodeWord&15;								

		// Add zeros...
		for (int x=0;x<zeroRunLength && currentTransformBlock.isFull()==false;x++){
			log.log(Level.FINE, "a: MCB="+index+" pos="+currentTransformBlock.getPosition()+" ADD (0)");
			currentTransformBlock.addNextValue(0);
		}								

		int tempIntDCTArr[]=new int[size];
		for (int x=0;x<size;x++){
			tempIntDCTArr[x]=jPegData.sosBitByBitReader.getNextBit();
		}

		int actValue=this.transformDCTBitsIntoADCTValue(tempIntDCTArr);
		log.log(Level.FINE, "b: MCB="+index+" pos="+currentTransformBlock.getPosition()+" ADD ("+actValue+")");	
		currentTransformBlock.addNextValue(actValue);
	}

	/**
	 * Fill the block with 0s
	 * @param jPegData
	 * @param currentTransformBlock
	 * @throws MCBIsFullException
	 */
	private void fillEndOfBlock(JPegData jPegData, TransformBlock currentTransformBlock) throws MCBIsFullException{
		while (currentTransformBlock.isFull()==false){
			currentTransformBlock.addNextValue(0);
			log.log(Level.FINE, ": pos="+currentTransformBlock.getPosition()+" ADD (0)");
		}		
	}
	
	/**
	 * Insert a run length with zeros.
	 * @param currentTransformBlock
	 * @throws MCBIsFullException
	 */
	private void fillZeroRunLengths(TransformBlock currentTransformBlock) throws MCBIsFullException{
		for (int i=0;i<16 && currentTransformBlock.isFull()==false;i++){
			currentTransformBlock.addNextValue(0);
			log.log(Level.FINE, ": pos="+currentTransformBlock.getPosition()+" ADD (0)");
		}
	}
		
	// **********************************************************************************************************************************
	// **********************************************************************************************************************************
	
	/**
	 * Transform bit sequence into a DCT value.
	 * @param input
	 * @return
	 */
	private int transformDCTBitsIntoADCTValue(int[] input){
		int aReturnValue=-1;		
		boolean isNegative=false;
		
		// MSB Negative case -> reverse bits
		if (input[0]==0){
			// Reverse bits
			for (int i=0;i<input.length;i++){
				if (input[i]==0){
					input[i]=1;
				}else{
					input[i]=0;
				}
			}
			
			isNegative=true;
		}
				
		// Transform it to a decimal number
		int dctVal=0;
		for (int i=0, j=input.length-1;i<input.length;i++, j--){
			int multiplicator=(1 << j);
			dctVal+=multiplicator*input[i];
		}
		
		if (isNegative==true){
			aReturnValue=dctVal*-1;
		}else{
			aReturnValue=dctVal;
		}
		
		return aReturnValue;		
	}	
}