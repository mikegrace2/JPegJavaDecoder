package control.modules;

import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import utils.enums.MacroblockSize;
import model.MCU.Macroblock;
import model.MCU.TransformBlock;

/**
 * Some basic utility functions.
 * @author michael
 */
public class Utils extends ModulesBase{
	private static final Logger log=Logger.getLogger("Utils");
	
	/**
	 * Constructor.
	 */
	public Utils(){
		super(log);
	}	
	
	/**
	 * Print all blocks
	 * @param allImageBlocks
	 */
	public void logAllImageBlocks(Macroblock allMacroblocks[], String message, Level logLeve){		
		log.log(logLeve, "a: "+message+" --------------------------------------------------------------------------");
		for (int i=0;i<allMacroblocks.length;i++){
			if (allMacroblocks[i]!=null) {
				log.log(logLeve, "b: Block["+i+"]");
				
				if (allMacroblocks[i].getLuminanceTransformBlock1()!=null){
					log.log(logLeve, "c: LUMINANCE TransformBlock 1");
					log.log(logLeve, "d: \n"+allMacroblocks[i].getLuminanceTransformBlock1().toStringDec());
				}
				
				if (allMacroblocks[i].getLuminanceTransformBlock2()!=null){
					log.log(logLeve, "e: LUMINANCE TransformBlock 2");
					log.log(logLeve, "f: \n"+allMacroblocks[i].getLuminanceTransformBlock2().toStringDec());
				}
				
				if (allMacroblocks[i].getLuminanceTransformBlock3()!=null){
					log.log(logLeve, "g: LUMINANCE TransformBlock 3");
					log.log(logLeve, "h: \n"+allMacroblocks[i].getLuminanceTransformBlock3().toStringDec());
				}

				if (allMacroblocks[i].getLuminanceTransformBlock4()!=null){
					log.log(logLeve, "i: LUMINANCE TransformBlock 4");
					log.log(logLeve, "j: \n"+allMacroblocks[i].getLuminanceTransformBlock4().toStringDec());
				}

				if (allMacroblocks[i].getChrominanceBlueTransformBlock()!=null){
					log.log(logLeve, "k: CROMINANCE BLUE TransformBlock");
					log.log(logLeve, "l: \n"+allMacroblocks[i].getChrominanceBlueTransformBlock().toStringDec());
				}
								
				if (allMacroblocks[i].getChrominanceRedTransformBlock()!=null){
					log.log(logLeve, "o: CROMINANCE RED TransformBlock");
					log.log(logLeve, "p: \n"+allMacroblocks[i].getChrominanceRedTransformBlock().toStringDec());
				}
												
				log.log(logLeve, "===========================================================================================");
			}else{
				log.log(logLeve, "\n");
				break;
			}
		}
	}
			
	/**
	 * Transform a standard 8*8 quantization matrix which we always get from the source file into a 16*16 table
	 * @param quantMat8times8
	 * @return
	 */
	public int[][] upsample8times8TableInto16times16(int quantMat8times8[][]){
		int quantMat16times16[][] = new int[MacroblockSize.SIXTEEN_TIMES_SIXTEEN.getValue()][MacroblockSize.SIXTEEN_TIMES_SIXTEEN.getValue()];
		
    	for (int i=0; i<TransformBlock.DIM_8TIMES8; i++){
    		for (int j=0; j<TransformBlock.DIM_8TIMES8; j++){
    			log.log(Level.FINEST, "["+i+"]["+j+"]");
    			quantMat16times16[i*2][j*2]=quantMat8times8[i][j];
    			quantMat16times16[i*2+1][j*2]=quantMat8times8[i][j];
    			quantMat16times16[i*2][j*2+1]=quantMat8times8[i][j];
    			quantMat16times16[i*2+1][j*2+1]=quantMat8times8[i][j];
    		}	
    	}
		
		return quantMat16times16;
	}
	
	/**
	 * Transform a standard 8*8 quantization matrix which we always get from the source file into a 16*16 table
	 * @param quantMat8times8
	 * @return
	 */
	public double[][] upsample8times8TableInto16times16(double quantMat8times8[][]){
		double quantMat16times16[][] = new double[MacroblockSize.SIXTEEN_TIMES_SIXTEEN.getValue()][MacroblockSize.SIXTEEN_TIMES_SIXTEEN.getValue()];
		
    	for (int i=0; i<TransformBlock.DIM_8TIMES8; i++){
    		for (int j=0; j<TransformBlock.DIM_8TIMES8; j++){
    			log.log(Level.FINEST, "["+i+"]["+j+"]");
    			quantMat16times16[i*2][j*2]=quantMat8times8[i][j];
    			quantMat16times16[i*2+1][j*2]=quantMat8times8[i][j];
    			quantMat16times16[i*2][j*2+1]=quantMat8times8[i][j];
    			quantMat16times16[i*2+1][j*2+1]=quantMat8times8[i][j];
    		}	
    	}
		
		return quantMat16times16;
	}	
	
	/**
	 * prints only the image data in one line
	 * @param input
	 * @param level
	 */
	public void printCharImageBlock(char input[], Level level){
		for (int i=0;i<input.length;i++){
			if (i%TransformBlock.DIM_8TIMES8==0)
				log.log(level, "");				
			log.log(level, Integer.toHexString(input[i])+"\t");
		}
		
		log.log(level, "");
	}	
	
	/**
	 * Print a block.
	 * @param inputArrArr
	 * @param id
	 * @param level
	 */
	public void printDoubleArray(int[][] inputArrArr, int id, Level level){
		log.log(level, ": Print Quantization table ID="+id);
		for (int i=0;i<inputArrArr.length;i++){
			log.log(level, "="+Arrays.toString(inputArrArr[i]));
		}
		
		log.log(level, "");
	}
	
	/**
	 * Print a block.
	 * @param inputArrArr
	 * @param id
	 * @param level
	 */
	public void printDoubleArray(double[][] inputArrArr, int id, Level level){
		log.log(level, ": Print Quantization table ID="+id);
		for (int i=0;i<inputArrArr.length;i++){
			log.log(level, "="+Arrays.toString(inputArrArr[i]));
		}
		
		log.log(level, "");
	}
}