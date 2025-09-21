package control.modules;

import java.util.logging.Level;
import java.util.logging.Logger;

import utils.enums.MacroblockSize;
import model.MCU.Macroblock;
import model.MCU.TransformBlock;

/**
 * Responsible for the Color transformation.
 * @author michael
 */
public class ColorTransform extends ModulesBase{
	private static final Logger log=Logger.getLogger("ColorTransform");
	
	/**
	 * Constructor.
	 */
	public ColorTransform(){
		super(log);
	}	
	
	/**
	 * Checks and transform all Macroblock.
	 * @param allMacroBlocks
	 */
	public void allYCrCbToRGBConvert(Macroblock allMacroBlocks[]){
		for (int i=0;i<allMacroBlocks.length;i++){
			if (allMacroBlocks[i]!=null){
				boolean retStatus=false;
				
				if (allMacroBlocks[i].getBlockSize()==MacroblockSize.EIGHT_TIMES_EIGHT){
					retStatus=this.convertImageBlockFromYCbCrToRGB_4_4_4(allMacroBlocks[i]);
				} else if (allMacroBlocks[i].getBlockSize()==MacroblockSize.SIXTEEN_TIMES_SIXTEEN){
					retStatus=this.convertImageBlockFromYCbCrToRGB_4_2_0(allMacroBlocks[i]);
				} else if (allMacroBlocks[i].getBlockSize()==MacroblockSize.SIXTEEN_TIMES_EIGHT){
					retStatus=this.convertImageBlockFromYCbCrToRGB_4_2_2(allMacroBlocks[i]);					
				}else {
					log.log(Level.SEVERE, "a: ERROR SUB SAMPLING UNKNOWN SO FAR ! exit(-33) allMacroBlocks[i].getBlockSize()="+allMacroBlocks[i].getBlockSize());
					System.exit(-33);					
				}
				
				if (retStatus==false){
					// Delete the last block because he contains no complete color data
					log.log(Level.SEVERE, "b: delete block "+i+" because of unsufficient color information!");
					allMacroBlocks[i]=null;
				}				
			}
		}
	}
	
	/**
	 * Convert YCbCr to RGB 4:2:2
	 * @param macroBlock
	 * @return
	 */
	private boolean convertImageBlockFromYCbCrToRGB_4_2_2(Macroblock macroBlock){			
		try{
			for (int i=0;i<2;i++){
				int red[][]=new int[TransformBlock.DIM_8TIMES8][TransformBlock.DIM_8TIMES8];
				int green[][]=new int[TransformBlock.DIM_8TIMES8][TransformBlock.DIM_8TIMES8];
				int blue[][]=new int[TransformBlock.DIM_8TIMES8][TransformBlock.DIM_8TIMES8];
			
				TransformBlock currentLuminanceTransformBlock=null;
				
				if (i==0){
					currentLuminanceTransformBlock=macroBlock.getLuminanceTransformBlock1();
				}else if (i==1){
					currentLuminanceTransformBlock=macroBlock.getLuminanceTransformBlock2();
				}
				
				for (int j=0;j<TransformBlock.DIM_8TIMES8;j++){
					for (int k=0;k<TransformBlock.DIM_8TIMES8;k++){
						double Y = (double) currentLuminanceTransformBlock.getTransformBlock()[j][k];
						double Cb = (double) macroBlock.getChrominanceBlueTransformBlock().getTransformBlock()[j][k];
						double Cr = (double) macroBlock.getChrominanceRedTransformBlock().getTransformBlock()[j][k];

						int r = (int) (Y + 1.40200 * (Cr - 0x80));
						int g = (int) (Y - 0.34414 * (Cb - 0x80) - 0.71414 * (Cr - 0x80));
						int b = (int) (Y + 1.77200 * (Cb - 0x80));

						r = Math.max(0, Math.min(255, r));
						g = Math.max(0, Math.min(255, g));
						b = Math.max(0, Math.min(255, b));
				  
						red[j][k]=r;
						green[j][k]=g;
						blue[j][k]=b;				  
					}
				}
				
				currentLuminanceTransformBlock.red=red;
				currentLuminanceTransformBlock.green=green;
				currentLuminanceTransformBlock.blue=blue;
			}
			
			return true;
		} catch (NullPointerException ex){
			log.log(Level.SEVERE, ": NullPointerException... this happens only because it is one of the last blocks without any Color information!");
			return false;
		}
	}	
	
	/**
	 * Convert YCbCr to RGB 4:2:0
	 * @param macroBlock
	 * @return
	 */
	private boolean convertImageBlockFromYCbCrToRGB_4_2_0(Macroblock macroBlock){			
		try{
			for (int i=0;i<4;i++){
				int red[][]=new int[TransformBlock.DIM_8TIMES8][TransformBlock.DIM_8TIMES8];
				int green[][]=new int[TransformBlock.DIM_8TIMES8][TransformBlock.DIM_8TIMES8];
				int blue[][]=new int[TransformBlock.DIM_8TIMES8][TransformBlock.DIM_8TIMES8];
			
				TransformBlock currentLuminanceTransformBlock=null;
				
				if (i==0){
					currentLuminanceTransformBlock=macroBlock.getLuminanceTransformBlock1();
				}else if (i==1){
					currentLuminanceTransformBlock=macroBlock.getLuminanceTransformBlock2();
				} else if (i==2){
					currentLuminanceTransformBlock=macroBlock.getLuminanceTransformBlock3();	
				} else if (i==3){
					currentLuminanceTransformBlock=macroBlock.getLuminanceTransformBlock4();	
				}
				
				for (int j=0;j<TransformBlock.DIM_8TIMES8;j++){
					for (int k=0;k<TransformBlock.DIM_8TIMES8;k++){
						double Y = (double) currentLuminanceTransformBlock.getTransformBlock()[j][k];
						double Cb = (double) macroBlock.getChrominanceBlueTransformBlock().getTransformBlock()[j][k];
						double Cr = (double) macroBlock.getChrominanceRedTransformBlock().getTransformBlock()[j][k];

						int r = (int) (Y + 1.40200 * (Cr - 0x80));
						int g = (int) (Y - 0.34414 * (Cb - 0x80) - 0.71414 * (Cr - 0x80));
						int b = (int) (Y + 1.77200 * (Cb - 0x80));

						r = Math.max(0, Math.min(255, r));
						g = Math.max(0, Math.min(255, g));
						b = Math.max(0, Math.min(255, b));
				  
						red[j][k]=r;
						green[j][k]=g;
						blue[j][k]=b;				  
					}
				}
				
				currentLuminanceTransformBlock.red=red;
				currentLuminanceTransformBlock.green=green;
				currentLuminanceTransformBlock.blue=blue;
			}
			
			return true;
		} catch (NullPointerException ex){
			log.log(Level.SEVERE, ": NullPointerException... this happens only because it is one of the last blocks without any Color information!");
			return false;
		}
	}
	
	/**
	 * Convert YCbCr to RGB 4:4:4
	 * @param macroBlock
	 * @return
	 */
	private boolean convertImageBlockFromYCbCrToRGB_4_4_4(Macroblock macroBlock){
		int red[][]=new int[TransformBlock.DIM_8TIMES8][TransformBlock.DIM_8TIMES8];
		int green[][]=new int[TransformBlock.DIM_8TIMES8][TransformBlock.DIM_8TIMES8];
		int blue[][]=new int[TransformBlock.DIM_8TIMES8][TransformBlock.DIM_8TIMES8];
						
		try{
			for (int i=0;i<TransformBlock.DIM_8TIMES8;i++){
				for (int j=0;j<TransformBlock.DIM_8TIMES8;j++){
					double Y = (double) macroBlock.getLuminanceTransformBlock1().getTransformBlock()[i][j];
					double Cb = (double) macroBlock.getChrominanceBlueTransformBlock().getTransformBlock()[i][j];
					double Cr = (double) macroBlock.getChrominanceRedTransformBlock().getTransformBlock()[i][j];

					int r = (int) (Y + 1.40200 * (Cr - 0x80));
					int g = (int) (Y - 0.34414 * (Cb - 0x80) - 0.71414 * (Cr - 0x80));
					int b = (int) (Y + 1.77200 * (Cb - 0x80));

					r = Math.max(0, Math.min(255, r));
					g = Math.max(0, Math.min(255, g));
					b = Math.max(0, Math.min(255, b));
				  
					red[i][j]=r;
					green[i][j]=g;
					blue[i][j]=b;				  
				}
			}
				
			macroBlock.getLuminanceTransformBlock1().red=red;
			macroBlock.getLuminanceTransformBlock1().green=green;
			macroBlock.getLuminanceTransformBlock1().blue=blue;
			
			return true;
		} catch (NullPointerException ex){
			log.log(Level.SEVERE, ": NullPointerException... this happens only because it is one of the last blocks without any Color information!");
			return false;
		}
	}
}