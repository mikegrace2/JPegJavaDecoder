package control.modules;

import java.util.logging.Level;
import java.util.logging.Logger;

import utils.enums.SubSampling;
import model.JPegData;
import model.MCU.Macroblock;
import model.MCU.TransformBlock;

/**
 * Discrete Cosine Transformation class.
 * @author michael
 */
public class DCT extends ModulesBase{	
	private static final Logger log=Logger.getLogger("DCT");
	
	/** 
	 * Cosine matrix. 
	 * N * N. 
	 */
    private double cosineMatrix8Times8[][]=new double[TransformBlock.DIM_8TIMES8][TransformBlock.DIM_8TIMES8];  
    /** 
     * Transformed cosine matrix
     * N*N. 
     */
    private double transformedCosineMatrix8Times8[][]=new double[TransformBlock.DIM_8TIMES8][TransformBlock.DIM_8TIMES8];    
    
    /**
     * Constructor.
     */
    public DCT(){
		super(log);    	
    	this.initMatrix8Times8();   
    }
	
    /**
     * This method sets up the quantization matrix using the Quality parameter and then sets up the Cosine Transform Matrix and the Transposed CT.
     * These are used by the forward and inverse DCT. The RLE encoding variables are set up to track the number of consecutive zero values
     * that have output or will be input.
     * @param quality The quality scaling factor
     */
    private void initMatrix8Times8(){
        for (int j = 0; j < TransformBlock.DIM_8TIMES8; j++){ // cosine and transformed cosine matrix alpha calculation
            double nn = (double)(TransformBlock.DIM_8TIMES8);
            this.cosineMatrix8Times8[0][j]  = 1.0 / Math.sqrt(nn);
            this.transformedCosineMatrix8Times8[j][0] = this.cosineMatrix8Times8[0][j];
        }
        
        for (int i = 1; i < TransformBlock.DIM_8TIMES8; i++) {// cosine and transformed cosine matrix finishing the matrix
            for (int j = 0; j < TransformBlock.DIM_8TIMES8; j++){
                double jj = (double)j;
                double ii = (double)i;
                this.cosineMatrix8Times8[i][j]  = Math.sqrt(2.0/8.0) * Math.cos(((2.0 * jj + 1.0) * ii * Math.PI) / (2.0 * 8.0));
                this.transformedCosineMatrix8Times8[j][i] = this.cosineMatrix8Times8[i][j];
            }
        }
        
        new Utils().printDoubleArray(this.cosineMatrix8Times8, 8, Level.FINEST);
        new Utils().printDoubleArray(this.transformedCosineMatrix8Times8, 8, Level.FINEST);
    }
        
	/**
	 * Do inverse DCT for all blocks
	 * @param allImageBlocks
	 */
	public void inverseDCTAllImageBlocks(Macroblock allMacroblocks[]){
		for (int i=0;i<allMacroblocks.length;i++){
			if (allMacroblocks[i]!=null) {
				if (allMacroblocks[i].getLuminanceTransformBlock1()!=null){
					allMacroblocks[i].setLuminanceTransformBlock1(this.inverseDCT(allMacroblocks[i].getLuminanceTransformBlock1()));
				}
				
				if (allMacroblocks[i].getLuminanceTransformBlock2()!=null){
					allMacroblocks[i].setLuminanceTransformBlock2(this.inverseDCT(allMacroblocks[i].getLuminanceTransformBlock2()));
				}

				if (allMacroblocks[i].getLuminanceTransformBlock3()!=null){
					allMacroblocks[i].setLuminanceTransformBlock3(this.inverseDCT(allMacroblocks[i].getLuminanceTransformBlock3()));
				}

				if (allMacroblocks[i].getLuminanceTransformBlock4()!=null){
					allMacroblocks[i].setLuminanceTransformBlock4(this.inverseDCT(allMacroblocks[i].getLuminanceTransformBlock4()));
				}
						
				if (allMacroblocks[i].getChrominanceBlueTransformBlock()!=null){
					allMacroblocks[i].setChrominanceBlueTransformBlock(this.inverseDCT(allMacroblocks[i].getChrominanceBlueTransformBlock()));
				}
						
				if (allMacroblocks[i].getChrominanceRedTransformBlock()!=null){
					allMacroblocks[i].setChrominanceRedTransformBlock(this.inverseDCT(allMacroblocks[i].getChrominanceRedTransformBlock()));
				}				
			}else{
				break;
			}
		}
	}	
	
	/**
     * This method is performed using the reverse of the operations performed in the DCT. This restores a N * N input block to the corresponding output
     * block with values scaled to 0 to 255 and then stored in the input block of pixels.
     * @param input N * N input block
     * @returns output The pixel array output
     */
    private TransformBlock inverseDCT(TransformBlock input){
    	TransformBlock returnTransformBlock=new TransformBlock(input.getTransformBlockType());
        double temp[][] = new double[TransformBlock.DIM_8TIMES8][TransformBlock.DIM_8TIMES8], temp1;
               
        // Multiply DCT matrix with transform block
        for (int i=0; i<TransformBlock.DIM_8TIMES8; i++){
            for (int j=0; j<TransformBlock.DIM_8TIMES8; j++){
                temp[i][j] = 0.0;
                for (int k=0; k<TransformBlock.DIM_8TIMES8; k++)
                    temp[i][j] += input.getTransformBlock()[i][k] * this.cosineMatrix8Times8[k][j];
            }
        }
        
        for (int i=0; i<TransformBlock.DIM_8TIMES8; i++){
            for (int j=0; j<TransformBlock.DIM_8TIMES8; j++){
                temp1 = 0.0;

                for (int k=0; k<TransformBlock.DIM_8TIMES8; k++){
                    temp1 += this.transformedCosineMatrix8Times8[i][k] * temp[k][j];
                }

                temp1 += 128.0;

                if (temp1 < 0)
                	returnTransformBlock.getTransformBlock()[i][j] = 0;
                else if (temp1 > 255)
                	returnTransformBlock.getTransformBlock()[i][j] = 255;
                else
                	returnTransformBlock.getTransformBlock()[i][j] = (int)Math.round(temp1);
            }
        }
        
        return returnTransformBlock;
    }	
    
	/**
	 * change the relative DCT values to Absolute DCT values
	 * @param allImageBlocks
	 */
	public void calcDCTAbsolute(Macroblock allMacroblocks[], JPegData jdata){
		if (allMacroblocks.length==0 || allMacroblocks==null){
			log.log(Level.SEVERE, "1: Error no Macroblocks at all! exit -773");
			System.exit(-773);
		}
		
		log.log(Level.FINE, "2: allMacroblocks.length="+allMacroblocks.length);
		
		// Do the compression stuff
		// 2:2 1:1 1:1 --- 4 Luminance Blocks and 2 Chrominance Blocks => 16x16 (4:2:0)
		if (jdata.whichSubSamplingItIs(SubSampling._4_2_0)){
			log.log(Level.FINEST, "3: color compression decompression is 2:2 1:1 1:1 4:2:0");
			
			for (int i=1;i<allMacroblocks.length;i++){
				if (allMacroblocks[i]!=null){					
					if (allMacroblocks[i].getChrominanceBlueTransformBlock()!=null){
						allMacroblocks[i].getChrominanceBlueTransformBlock().getTransformBlock()[0][0]=
								allMacroblocks[i].getChrominanceBlueTransformBlock().getTransformBlock()[0][0]+
								allMacroblocks[i-1].getChrominanceBlueTransformBlock().getTransformBlock()[0][0];
					}
					
					if (allMacroblocks[i].getChrominanceRedTransformBlock()!=null){
						allMacroblocks[i].getChrominanceRedTransformBlock().getTransformBlock()[0][0]=
								allMacroblocks[i].getChrominanceRedTransformBlock().getTransformBlock()[0][0]+
								allMacroblocks[i-1].getChrominanceRedTransformBlock().getTransformBlock()[0][0];
					}
				}
			}			
			
			for (int i=0, lastDCTVal=0;i<allMacroblocks.length;i++){
				allMacroblocks[i].getLuminanceTransformBlock1().getTransformBlock()[0][0]=
						allMacroblocks[i].getLuminanceTransformBlock1().getTransformBlock()[0][0]+
						lastDCTVal;
				
				lastDCTVal=allMacroblocks[i].getLuminanceTransformBlock1().getTransformBlock()[0][0];				
				
				allMacroblocks[i].getLuminanceTransformBlock2().getTransformBlock()[0][0]=
						allMacroblocks[i].getLuminanceTransformBlock2().getTransformBlock()[0][0]+
						lastDCTVal;
				
				lastDCTVal=allMacroblocks[i].getLuminanceTransformBlock2().getTransformBlock()[0][0];
				
				allMacroblocks[i].getLuminanceTransformBlock3().getTransformBlock()[0][0]=
						allMacroblocks[i].getLuminanceTransformBlock3().getTransformBlock()[0][0]+
						lastDCTVal;
				
				lastDCTVal=allMacroblocks[i].getLuminanceTransformBlock3().getTransformBlock()[0][0];
				
				allMacroblocks[i].getLuminanceTransformBlock4().getTransformBlock()[0][0]=
						allMacroblocks[i].getLuminanceTransformBlock4().getTransformBlock()[0][0]+
						lastDCTVal;
				
				lastDCTVal=allMacroblocks[i].getLuminanceTransformBlock4().getTransformBlock()[0][0];				
			}
		// 16�8 (4:2:2 subsampling)
		} else if (jdata.whichSubSamplingItIs(SubSampling._4_2_2)){
			log.log(Level.FINEST, "4: color 4:2:2 subsampling");
			
			for (int i=1;i<allMacroblocks.length;i++){
				if (allMacroblocks[i]!=null){					
					if (allMacroblocks[i].getChrominanceBlueTransformBlock()!=null){
						allMacroblocks[i].getChrominanceBlueTransformBlock().getTransformBlock()[0][0]=
								allMacroblocks[i].getChrominanceBlueTransformBlock().getTransformBlock()[0][0]+
								allMacroblocks[i-1].getChrominanceBlueTransformBlock().getTransformBlock()[0][0];
					}
					
					if (allMacroblocks[i].getChrominanceRedTransformBlock()!=null){
						allMacroblocks[i].getChrominanceRedTransformBlock().getTransformBlock()[0][0]=
								allMacroblocks[i].getChrominanceRedTransformBlock().getTransformBlock()[0][0]+
								allMacroblocks[i-1].getChrominanceRedTransformBlock().getTransformBlock()[0][0];
					}
				}
			}			
			
			for (int i=0, lastDCTVal=0;i<allMacroblocks.length;i++){
				allMacroblocks[i].getLuminanceTransformBlock1().getTransformBlock()[0][0]=
						allMacroblocks[i].getLuminanceTransformBlock1().getTransformBlock()[0][0]+
						lastDCTVal;
				
				lastDCTVal=allMacroblocks[i].getLuminanceTransformBlock1().getTransformBlock()[0][0];				
				
				allMacroblocks[i].getLuminanceTransformBlock2().getTransformBlock()[0][0]=
						allMacroblocks[i].getLuminanceTransformBlock2().getTransformBlock()[0][0]+
						lastDCTVal;
				
				lastDCTVal=allMacroblocks[i].getLuminanceTransformBlock2().getTransformBlock()[0][0];				
			}
		// 8�8 (4:4:4 � no subsampling)
		} else if (jdata.whichSubSamplingItIs(SubSampling._4_4_4_no_subsampling)){
			log.log(Level.FINEST, "4: color 4:4:4 no color subsampling");
			for (int i=1;i<allMacroblocks.length;i++){
				if (allMacroblocks[i]!=null){
					if (allMacroblocks[i].getLuminanceTransformBlock1()!=null){
						allMacroblocks[i].getLuminanceTransformBlock1().getTransformBlock()[0][0]=
								allMacroblocks[i].getLuminanceTransformBlock1().getTransformBlock()[0][0]+
								allMacroblocks[i-1].getLuminanceTransformBlock1().getTransformBlock()[0][0];
					}
					
					if (allMacroblocks[i].getChrominanceBlueTransformBlock()!=null){
						allMacroblocks[i].getChrominanceBlueTransformBlock().getTransformBlock()[0][0]=
								allMacroblocks[i].getChrominanceBlueTransformBlock().getTransformBlock()[0][0]+
								allMacroblocks[i-1].getChrominanceBlueTransformBlock().getTransformBlock()[0][0];
					}
					
					if (allMacroblocks[i].getChrominanceRedTransformBlock()!=null){
						allMacroblocks[i].getChrominanceRedTransformBlock().getTransformBlock()[0][0]=
								allMacroblocks[i].getChrominanceRedTransformBlock().getTransformBlock()[0][0]+
								allMacroblocks[i-1].getChrominanceRedTransformBlock().getTransformBlock()[0][0];
					}
				}
			}			
		}else{
			log.log(Level.SEVERE, "5: ERROR color compression/decrompression ratio is unknown. IMPLEMENT ME!");
			return;
		}		
	}    
}