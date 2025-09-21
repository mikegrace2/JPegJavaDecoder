package view.modules;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.ImageIcon;

import utils.enums.MacroblockSize;
import control.modules.ModulesBase;
import model.JPegData;
import model.MCU.Macroblock;
import model.MCU.TransformBlock;

/**
 * Handles the painting functions. 
 * @author michael
 */
public class PaintPicture extends ModulesBase{
	private static final Logger log=Logger.getLogger("PaintPicture");
	
	public Macroblock allMacroblocks[] = null;
	public JPegData jpegData=null;
	public String filename=null;
		
	/**
	 * Constructor.
	 * @param allMacroblocks
	 * @param jpegData
	 * @param filename
	 */
	public PaintPicture(Macroblock allMacroblocks[], JPegData jpegData, String filename){
		super(log);
		
        this.allMacroblocks=allMacroblocks;
        this.jpegData=jpegData;
        this.filename=filename;        
	}
	
	/**
	 * Paint 16*16 Macroblocks in 4:2:0 format.
	 * 
	 * 16*16 12345678 12345678
	 *  01:  01000101 10100010
	 *  02:  01101110 01010101
	 *  03:  00011111 11111111
	 *  04:  00110000 00101010
	 *  05:  10010101 01010100
	 *  06:  10001010 01010101
	 *  07:  10001010 10101010
	 *  08:  10101001 10100010
	 *  
	 *  09:  01000101 00100010
	 *  10:  01101110 01010100
	 *  11:  00011111 01010100
	 *  12:  00110000 01010101
	 *  13:  10010101 01010100
	 *  14:  10001010 00111000
	 *  15:  10001010 11111100
	 *  16:  10101001 00010001
	 * 
	 * @param g
	 */
	public void paint16Times16Component_4_2_0(Graphics g){
		int pixelSize=8;
		int macroblockSize=MacroblockSize.SIXTEEN_TIMES_SIXTEEN.getValue();
		int height=this.jpegData.startOfFrameObjects[0].sofImageHeightPixels;
		int width=this.jpegData.startOfFrameObjects[0].sofImageWidthPixels;
		int heightBlocksCount=height/macroblockSize;
		int widthBlockCount=width/macroblockSize;
		
		if (height%macroblockSize!=0){
			heightBlocksCount++;
	    }
	    	
		if (width%macroblockSize!=0){
			widthBlockCount++;
	    }
	    			
		log.log(Level.INFO, "a: allImageBlocks.length="+this.allMacroblocks.length);
		log.log(Level.INFO, "b: Image height="+height+" width="+width);
		log.log(Level.INFO, "c: heightBlocksCount="+heightBlocksCount+" widthBlockCount="+widthBlockCount);
		
		// Paint original picture
		Image image= new ImageIcon(this.filename).getImage();
		log.log(Level.FINEST, "d: image="+image+" width="+width+" height="+height);
		g.drawImage(image,450,macroblockSize*pixelSize,width*pixelSize, height*pixelSize, null);
		
		g.setFont(new Font("default", Font.BOLD, 18));
		g.setColor(new Color(0xff, 0x00, 0x00));
		g.drawString("My Version", width*2, 80);    			
		
		g.setColor(new Color(0xff, 0x00, 0x00));
		g.drawString("Original 4:2:0", 450+(width*3), 80);		    	
		
		int x=0;
		int y=0;			
		
		for (int i=0, transformBlockNum=0;i<this.allMacroblocks.length && this.allMacroblocks[i]!=null;i++){
			int mult=pixelSize*macroblockSize;
			x=x+mult;
			
			if (i%widthBlockCount==0){
				y=y+mult;
				x=0;
			}
			
			this.paintTransformBlock8Times8(g, this.allMacroblocks[i].getLuminanceTransformBlock1(), x+00, y+00, pixelSize, transformBlockNum++, 450);
			this.paintTransformBlock8Times8(g, this.allMacroblocks[i].getLuminanceTransformBlock2(), x+64, y+00, pixelSize, transformBlockNum++, 450);
			this.paintTransformBlock8Times8(g, this.allMacroblocks[i].getLuminanceTransformBlock3(), x+00, y+64, pixelSize, transformBlockNum++, 450);
			this.paintTransformBlock8Times8(g, this.allMacroblocks[i].getLuminanceTransformBlock4(), x+64, y+64, pixelSize, transformBlockNum++, 450);
		}
	}	
	
	/**
	 * Paint 8*16 Macroblocks in 4:2:2 format.
	 * 
	 * 8*16 12345678 
	 * 01:  01000101
	 * 02:  01101110
	 * 03:  00011111
	 * 04:  00110000
	 * 05:  10010101
	 * 06:  10001010
	 * 07:  10001010
	 * 08:  10101001
	 * 
	 * 09:  01000101
	 * 10:  01101110
	 * 11:  00011111
	 * 12:  00110000
	 * 13:  10010101
	 * 14:  10001010
	 * 15:  10001010
	 * 16:  10101001 
	 * 
	 * @param g
	 */
    public void paint16Times8Component_4_2_2(Graphics g){
    	int pixelSize=8;
    	int macroblockSize=MacroblockSize.SIXTEEN_TIMES_EIGHT.getValue();
    	int height=this.jpegData.startOfFrameObjects[0].sofImageHeightPixels;
    	int width=this.jpegData.startOfFrameObjects[0].sofImageWidthPixels;
    	int heightBlocksCount=height/macroblockSize;
    	int widthBlockCount=width/macroblockSize;
    	
    	if (height%macroblockSize!=0){
    		heightBlocksCount++;
    	}
    	
    	if (width%macroblockSize!=0){
    		widthBlockCount++;
    	}
    			
    	log.log(Level.INFO, "a: allImageBlocks.length="+this.allMacroblocks.length);
    	log.log(Level.INFO, "b: Image height="+height+" width="+width);
    	log.log(Level.INFO, "c: heightBlocksCount="+heightBlocksCount+" widthBlockCount="+widthBlockCount);
    	    	
    	// Paint original picture
    	Image image= new ImageIcon(this.filename).getImage();
    	log.log(Level.FINEST, "d: image="+image+" width="+width+" height="+height);
    	g.drawImage(image,450,macroblockSize*pixelSize,width*pixelSize, height*pixelSize, null);
    			
    	g.setFont(new Font("default", Font.BOLD, 18));
		g.setColor(new Color(0xff, 0x00, 0x00));
		g.drawString("My Version", (width*2)-5, 55);    			
		
		g.setColor(new Color(0xff, 0x00, 0x00));
		g.drawString("Original 4:2:2", (450+(width*3))-35, 55);		    	
    	
    	int x=0;
    	int y=0;			
		
		for (int i=0, transformBlockNum=0;i<this.allMacroblocks.length && this.allMacroblocks[i]!=null;i++){
			int mult=pixelSize*macroblockSize;
			x=x+mult;
		
			if (i%widthBlockCount==0){
				y=y+mult;
				x=0;
			}
		
			this.paintTransformBlock8Times8(g, this.allMacroblocks[i].getLuminanceTransformBlock1(), x+00, y+00, pixelSize, transformBlockNum++, 450);
			this.paintTransformBlock8Times8(g, this.allMacroblocks[i].getLuminanceTransformBlock2(), x+00, y+64, pixelSize, transformBlockNum++, 450);
			}
    }
    
    /**
     * Paint 8*8 Macroblocks in 4:4:4 format.
	 *
	 * 8*8 12345678 
	 * 01: 01000101
	 * 02: 01101110
	 * 03: 00011111
	 * 04: 00110000
	 * 05: 10010101
	 * 06: 10001010
	 * 07: 10001010
	 * 08: 10101001
     * 
     * @param g
     */
    public void paint8Times8Component_4_4_4(Graphics g){
    	int pixelSize=8;
    	int macroblockSize=MacroblockSize.EIGHT_TIMES_EIGHT.getValue();
    	int height=this.jpegData.startOfFrameObjects[0].sofImageHeightPixels;
    	int width=this.jpegData.startOfFrameObjects[0].sofImageWidthPixels;
    	int heightBlocksCount=height/macroblockSize;
    	int widthBlockCount=width/macroblockSize;
    	
    	if (height%macroblockSize!=0){
    		heightBlocksCount++;
    	}
    	
    	if (width%macroblockSize!=0){
    		widthBlockCount++;
    	}
    			
    	log.log(Level.INFO, "a: allImageBlocks.length="+this.allMacroblocks.length);
    	log.log(Level.INFO, "b: Image height="+height+" width="+width);
    	log.log(Level.INFO, "c: heightBlocksCount="+heightBlocksCount+" widthBlockCount="+widthBlockCount);
    	    	
    	// Paint original picture
    	Image image= new ImageIcon(this.filename).getImage();
    	log.log(Level.FINEST, "d: image="+image+" width="+width+" height="+height);
    	g.drawImage(image,450,macroblockSize*pixelSize,width*pixelSize, height*pixelSize, null);
    			
    	g.setFont(new Font("default", Font.BOLD, 18));
		g.setColor(new Color(0xff, 0x00, 0x00));
		g.drawString("My Version", width*2, 55);    			
		
		g.setColor(new Color(0xff, 0x00, 0x00));
		g.drawString("Original 4:4:4", 450+(width*3), 55);		    	
    	
    	int x=0;
    	int y=0;			
		
		for (int i=0;i<this.allMacroblocks.length && this.allMacroblocks[i]!=null;i++){
			int mult=pixelSize*macroblockSize;
			x=x+mult;
		
			if (i%widthBlockCount==0){
				y=y+mult;
				x=0;
			}
		
			this.paintTransformBlock8Times8(g, this.allMacroblocks[i].getLuminanceTransformBlock1(),x, y, pixelSize, i, 450);
		}
    }    
    
    /** 
     * paint a MCU block.
     * 
     * @param g
     * @param block
     * @param startX
     * @param startY
     * @param pixelSize
     */
    private void paintTransformBlock8Times8(Graphics g, TransformBlock block, int startX, int startY, int pixelSize, int blockNumber, int origImageXOffset){
    	if (block==null){
    		log.log(Level.SEVERE, "ERROR block is null!");
    		return;
    	}
    	
    	int x=startX;
    	int y=startY;

		int redVal=-1;
		int greenVal=-1;
		int blueVal=-1;
				
    	try{    		
    		for(int i=0;i<TransformBlock.DIM_8TIMES8;i++){
    			for (int j=0;j<TransformBlock.DIM_8TIMES8;j++){
    				redVal=block.red[i][j];
    				greenVal=block.green[i][j];
    				blueVal=block.blue[i][j];
    				Color tempColor=new Color(redVal,greenVal,blueVal);
    				
    				g.setColor(tempColor);
    				g.fillRect(x, y, pixelSize, pixelSize);
    				x=x+pixelSize;
    			}
    			x=startX;
    			y=y+pixelSize;
    		}
    		
    		// Draw a box and number the box
    		g.setColor(new Color(0x00, 0x00, 0x00));
        	g.setFont(new Font("default", Font.BOLD, 18));
    		g.drawString(""+blockNumber, x+28, y-26);
    		
    		g.setColor(new Color(0x00, 0xff, 0x00));
    		g.drawRect(startX, startY, pixelSize*8, pixelSize*8);

    		// Do this raster on the original image too --------------
    		g.setColor(new Color(0x00, 0x00, 0x00));
        	g.setFont(new Font("default", Font.BOLD, 18));
    		g.drawString(""+blockNumber, origImageXOffset+x+28, y-26);
    		
    		g.setColor(new Color(0x00, 0xff, 0x00));
    		g.drawRect(origImageXOffset+startX, startY, pixelSize*8, pixelSize*8);    		
    	} catch (NullPointerException ex){
    		log.log(Level.SEVERE, "a: NullPointerExcapeion no color information available, so it is a incomplete block!");
    	} catch (IllegalArgumentException ex){
    		log.log(Level.SEVERE, "b: IllegalArgumentException red="+redVal+" green="+greenVal+" blue="+blueVal);
    		ex.printStackTrace();
    	}
    }
}