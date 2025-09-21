package control.modules;

import java.util.logging.Logger;

import model.MCU.Macroblock;
import model.MCU.TransformBlock;

/**
 * Dequantization of the Macroblocks.
 * @author michael
 */
public class Quantizizer extends ModulesBase{	
	private static final Logger log=Logger.getLogger("Quantizizer");
	
    public int zigZag8times8[][]=new int[TransformBlock.DIM_8TIMES8*TransformBlock.DIM_8TIMES8][2]; /** The ZigZag matrix. */	
    
    /**
     * Constructor.
     */
    public Quantizizer(){
		super(log);
    	this.initZigZag8times8();
    }    
	
    /**
     * Initializes the ZigZag matrix.
     */
    private void initZigZag8times8(){
    	zigZag8times8[0][0] = 0; // 0,0
    	zigZag8times8[0][1] = 0;
    	zigZag8times8[1][0] = 0; // 0,1
    	zigZag8times8[1][1] = 1;
    	zigZag8times8[2][0] = 1; // 1,0
    	zigZag8times8[2][1] = 0;
    	zigZag8times8[3][0] = 2; // 2,0
    	zigZag8times8[3][1] = 0;
    	zigZag8times8[4][0] = 1; // 1,1
    	zigZag8times8[4][1] = 1;
    	zigZag8times8[5][0] = 0; // 0,2
    	zigZag8times8[5][1] = 2;
    	zigZag8times8[6][0] = 0; // 0,3
    	zigZag8times8[6][1] = 3;
    	zigZag8times8[7][0] = 1; // 1,2
    	zigZag8times8[7][1] = 2;
    	zigZag8times8[8][0] = 2; // 2,1
    	zigZag8times8[8][1] = 1;
    	zigZag8times8[9][0] = 3; // 3,0
    	zigZag8times8[9][1] = 0;
    	zigZag8times8[10][0] = 4; // 4,0
    	zigZag8times8[10][1] = 0;
    	zigZag8times8[11][0] = 3; // 3,1
    	zigZag8times8[11][1] = 1;
    	zigZag8times8[12][0] = 2; // 2,2
    	zigZag8times8[12][1] = 2;
    	zigZag8times8[13][0] = 1; // 1,3
    	zigZag8times8[13][1] = 3;
    	zigZag8times8[14][0] = 0; // 0,4
    	zigZag8times8[14][1] = 4;
    	zigZag8times8[15][0] = 0; // 0,5
    	zigZag8times8[15][1] = 5;
    	zigZag8times8[16][0] = 1; // 1,4
    	zigZag8times8[16][1] = 4;
    	zigZag8times8[17][0] = 2; // 2,3
    	zigZag8times8[17][1] = 3;
    	zigZag8times8[18][0] = 3; // 3,2
        zigZag8times8[18][1] = 2;
        zigZag8times8[19][0] = 4; // 4,1
        zigZag8times8[19][1] = 1;
        zigZag8times8[20][0] = 5; // 5,0
        zigZag8times8[20][1] = 0;
        zigZag8times8[21][0] = 6; // 6,0
        zigZag8times8[21][1] = 0;
        zigZag8times8[22][0] = 5; // 5,1
        zigZag8times8[22][1] = 1;
        zigZag8times8[23][0] = 4; // 4,2
        zigZag8times8[23][1] = 2;
        zigZag8times8[24][0] = 3; // 3,3
        zigZag8times8[24][1] = 3;
        zigZag8times8[25][0] = 2; // 2,4
        zigZag8times8[25][1] = 4;
        zigZag8times8[26][0] = 1; // 1,5
        zigZag8times8[26][1] = 5;
        zigZag8times8[27][0] = 0; // 0,6
        zigZag8times8[27][1] = 6;
        zigZag8times8[28][0] = 0; // 0,7
        zigZag8times8[28][1] = 7;
        zigZag8times8[29][0] = 1; // 1,6
        zigZag8times8[29][1] = 6;
        zigZag8times8[30][0] = 2; // 2,5
        zigZag8times8[30][1] = 5;
        zigZag8times8[31][0] = 3; // 3,4
        zigZag8times8[31][1] = 4;
        zigZag8times8[32][0] = 4; // 4,3
        zigZag8times8[32][1] = 3;
        zigZag8times8[33][0] = 5; // 5,2
        zigZag8times8[33][1] = 2;
        zigZag8times8[34][0] = 6; // 6,1
        zigZag8times8[34][1] = 1;
        zigZag8times8[35][0] = 7; // 7,0
        zigZag8times8[35][1] = 0;
        zigZag8times8[36][0] = 7; // 7,1
        zigZag8times8[36][1] = 1;
        zigZag8times8[37][0] = 6; // 6,2
        zigZag8times8[37][1] = 2;
        zigZag8times8[38][0] = 5; // 5,3
        zigZag8times8[38][1] = 3;
        zigZag8times8[39][0] = 4; // 4,4
        zigZag8times8[39][1] = 4;
        zigZag8times8[40][0] = 3; // 3,5
        zigZag8times8[40][1] = 5;
        zigZag8times8[41][0] = 2; // 2,6
        zigZag8times8[41][1] = 6;
        zigZag8times8[42][0] = 1; // 1,7
        zigZag8times8[42][1] = 7;
        zigZag8times8[43][0] = 2; // 2,7
        zigZag8times8[43][1] = 7;
        zigZag8times8[44][0] = 3; // 3,6
        zigZag8times8[44][1] = 6;
        zigZag8times8[45][0] = 4; // 4,5
        zigZag8times8[45][1] = 5;
        zigZag8times8[46][0] = 5; // 5,4
        zigZag8times8[46][1] = 4;
        zigZag8times8[47][0] = 6; // 6,3
        zigZag8times8[47][1] = 3;
        zigZag8times8[48][0] = 7; // 7,2
        zigZag8times8[48][1] = 2;
        zigZag8times8[49][0] = 7; // 7,3
        zigZag8times8[49][1] = 3;
        zigZag8times8[50][0] = 6; // 6,4
        zigZag8times8[50][1] = 4;
        zigZag8times8[51][0] = 5; // 5,5
        zigZag8times8[51][1] = 5;
        zigZag8times8[52][0] = 4; // 4,6
        zigZag8times8[52][1] = 6;
        zigZag8times8[53][0] = 3; // 3,7
        zigZag8times8[53][1] = 7;
        zigZag8times8[54][0] = 4; // 4,7
        zigZag8times8[54][1] = 7;
        zigZag8times8[55][0] = 5; // 5,6
        zigZag8times8[55][1] = 6;
        zigZag8times8[56][0] = 6; // 6,5
        zigZag8times8[56][1] = 5;
        zigZag8times8[57][0] = 7; // 7,4
        zigZag8times8[57][1] = 4;
        zigZag8times8[58][0] = 7; // 7,5
        zigZag8times8[58][1] = 5;
        zigZag8times8[59][0] = 6; // 6,6
        zigZag8times8[59][1] = 6;
        zigZag8times8[60][0] = 5; // 5,7
        zigZag8times8[60][1] = 7;
        zigZag8times8[61][0] = 6; // 6,7
        zigZag8times8[61][1] = 7;
        zigZag8times8[62][0] = 7; // 7,6
        zigZag8times8[62][1] = 6;
        zigZag8times8[63][0] = 7; // 7,7
        zigZag8times8[63][1] = 7;
    }
        
    /**
     * Dequantize all Macroblocks of an image.
     * @param allMacroblocks
     * @param luminanceQuant
     * @param chrominanceQuant
     */
	public void allDeQuantitize(Macroblock allMacroblocks[], int luminanceQuant[][], int chrominanceQuant[][]){
		for (int i=0;i<allMacroblocks.length;i++){
			if (allMacroblocks[i]!=null){
				if (allMacroblocks[i].getLuminanceTransformBlock1()!=null){
					allMacroblocks[i].setLuminanceTransformBlock1(this.dequantitize(allMacroblocks[i].getLuminanceTransformBlock1(), luminanceQuant));
				}
				
				if (allMacroblocks[i].getLuminanceTransformBlock2()!=null){
					allMacroblocks[i].setLuminanceTransformBlock2(this.dequantitize(allMacroblocks[i].getLuminanceTransformBlock2(), luminanceQuant));
				}

				if (allMacroblocks[i].getLuminanceTransformBlock3()!=null){
					allMacroblocks[i].setLuminanceTransformBlock3(this.dequantitize(allMacroblocks[i].getLuminanceTransformBlock3(), luminanceQuant));
				}

				if (allMacroblocks[i].getLuminanceTransformBlock4()!=null){
					allMacroblocks[i].setLuminanceTransformBlock4(this.dequantitize(allMacroblocks[i].getLuminanceTransformBlock4(), luminanceQuant));
				}				
				
				if (allMacroblocks[i].getChrominanceBlueTransformBlock()!=null){
					allMacroblocks[i].setChrominanceBlueTransformBlock(this.dequantitize(allMacroblocks[i].getChrominanceBlueTransformBlock(), chrominanceQuant));
				}
												
				if (allMacroblocks[i].getChrominanceRedTransformBlock()!=null){ 
					allMacroblocks[i].setChrominanceRedTransformBlock(this.dequantitize(allMacroblocks[i].getChrominanceRedTransformBlock(), chrominanceQuant));
				}				
			}
		}
	}
		
    /**
     * This method reads in DCT codes  dequanitizes them and places them in the correct location. The codes are stored in the
     * zigzag format so they need to be redirected to a N * N block through simple table lookup. After dequantitization the data needs to be
     * run through an inverse DCT.
     * @param inputData 8x8 Array of quantitized image data
     * @returns outputData A N * N array of de-quantitized data
     */
    public TransformBlock dequantitize(TransformBlock inputData, int quantMat8times8[][]){
    	TransformBlock returnTransformBlock=new TransformBlock(inputData.getTransformBlockType());
    	double result;
        
    	for (int i=0; i<TransformBlock.DIM_8TIMES8; i++){
    		for (int j=0; j<TransformBlock.DIM_8TIMES8; j++){
    			result = inputData.getTransformBlock()[i][j] * quantMat8times8[i][j];
    			returnTransformBlock.getTransformBlock()[i][j] = (int)(Math.round(result));
    		}
    	}
        
    	return returnTransformBlock;
    }            
}