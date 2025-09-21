package control.main;

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.io.File;
import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

import control.modules.ColorTransform;
import control.modules.DCT;
import control.modules.Entropy;
import control.modules.JPegFileReader;
import control.modules.Quantizizer;
import control.modules.Utils;
import model.JPegData;
import model.MCU.Macroblock;
import utils.enums.SubSampling;
import view.modules.PaintPicture;

/**
 * JPeg Decoder - decodes a simple jpeg file and displays it later.
 * Lines of code: 4,228
 * @author michael
 */


@SuppressWarnings("serial")
public class JPegDecode extends JFrame {
	//private static final long serialVersionUID = -6692450983455127563L;
	private static final Logger log=Logger.getLogger("JpegDecode");	
	public Macroblock allMacroblocks[] = null;
	public JPegData jpegData=null;
	public String filename=null;
	       
	/**
	 * Standard constructor.
	 */
	public JPegDecode(){		
        log.setUseParentHandlers(false);
        ConsoleHandler ch = new ConsoleHandler();
        ch.setFormatter(new LogFormatterSingleton());
        ch.setLevel(LogFormatterSingleton.OVERALL_LEVEL);
        log.addHandler(ch);
        log.setLevel(LogFormatterSingleton.OVERALL_LEVEL);	           
        
		// get content pane
		Container container = getContentPane();
		container.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));		
		
		//Create a file chooser
		JFileChooser fileChooser = new JFileChooser(new File("C:\\Users\\mschi\\Documents\\JPegDecode\\")); // where to open the file dialog  
		fileChooser.showOpenDialog(this);
		System.out.println("File to open: " + fileChooser.getSelectedFile());
		this.filename=fileChooser.getSelectedFile().toString();
		
		this.decodeJPeg();
		
		pack();
		setSize(1024, 800);
		setLocationRelativeTo(null);
		setVisible(true);
	}	
	
	/**
	 * paint the pictures.
	 */
	public void paint(Graphics g) {
	    super.paint(g);
	    
    	log.log(Level.INFO, "a: Macroblocks count="+this.allMacroblocks.length+"\n");
    	PaintPicture myPaintPicture=new PaintPicture(this.allMacroblocks, this.jpegData, this.filename);
    	    	
    	// 8x8 (4:4:4  no subsampling)		
		if (this.jpegData.whichSubSamplingItIs(SubSampling._4_4_4_no_subsampling)){
			log.log(Level.INFO, "b: PAINT _4_4_4_no_subsampling");
			myPaintPicture.paint8Times8Component_4_4_4(g);
		// 2:2 1:1 1:1 --- 4 Luminance Blocks and 2 Chrominance Blocks => 16x16 (4:2:0)
		} else if (this.jpegData.whichSubSamplingItIs(SubSampling._4_2_0)){
			log.log(Level.INFO, "c: PAINT _4_2_0");
			myPaintPicture.paint16Times16Component_4_2_0(g);
		} else if (this.jpegData.whichSubSamplingItIs(SubSampling._4_2_2)){
			log.log(Level.INFO, "c: PAINT _4_2_2");
			myPaintPicture.paint16Times8Component_4_2_2(g);			
		} else { // ERROR
			log.log(Level.SEVERE, "d: ERROR SUB SAMPLING UNKNOWN! exit(-3)");
			System.exit(-3);
		}	
	}
				
	/**
	 * Main function which starts everything.
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {				
		JPegDecode mySimpleConvertImage=new JPegDecode();	
		mySimpleConvertImage.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		log.log(Level.INFO, "a: Read and create jpeg in memory ---");
	}
	   
    /**
     * Decode a given JPeg file. 
     */
    public void decodeJPeg(){
		// FIRST: Read all the raw data from file
		this.jpegData=new JPegFileReader().readJPeg(this.filename);
		log.log(Level.INFO, this.jpegData.toString());
				
		// SECOND: entropy decode it with help of the Huffman tables
		this.allMacroblocks=new Entropy().entropyDecodeAll(this.jpegData);
		
		// Print print print print print print print print print print print print print print print print
		new Utils().logAllImageBlocks(this.allMacroblocks, "After read from file and entropy decoding...", Level.FINEST);

		// THIRD: Calculate the absolute DCT values
		new DCT().calcDCTAbsolute(this.allMacroblocks, this.jpegData);
			
		// Print print print print print print print print print print print print print print print print
		new Utils().logAllImageBlocks(this.allMacroblocks, "After calculate absolute DCT values...", Level.FINEST);
		
		// FOURTH: Dequantize it
		new Quantizizer().allDeQuantitize(this.allMacroblocks, this.jpegData.qantizationtables[0].dqtDataArrayArray, this.jpegData.qantizationtables[1].dqtDataArrayArray);
	
		// Print print print print print print print print print print print print print print print print
		new Utils().logAllImageBlocks(this.allMacroblocks, "After dequantization...", Level.FINEST);		
		
		// FITH: Do the inverse DCT for all blocks
		new DCT().inverseDCTAllImageBlocks(this.allMacroblocks);
		
		// Print print print print print print print print print print print print print print print print
		new Utils().logAllImageBlocks(this.allMacroblocks, "After inverse DCT...", Level.FINEST);		
		
		// SIX: Convert YCbCr to RGB
		new ColorTransform().allYCrCbToRGBConvert(this.allMacroblocks);
		
		// Print print print print print print print print print print print print print print print print
		new Utils().logAllImageBlocks(this.allMacroblocks, "After color transform...", Level.FINEST);	
    }	
}