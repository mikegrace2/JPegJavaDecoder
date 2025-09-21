package model;

import java.util.logging.Logger;

/**
 * Data class for the StartOfFrame information of a JPeg file.
 * @author michael
 */
public class StartOfFrame extends JPegBase {
	private static final Logger log=Logger.getLogger("StartOfFrame");	
	
	public StartOfFrame(){
		super(log);
	}		
	
	// 0xc0 SOF - Start of Frame (Baseline DCT)
	public char sofMarker=0;
	public int sofSize=-1;
	public char sofDataArray[]=null; 
	
	public int sofSamplePrecision=-1; // can be 8 or 12 bit
	public int sofImageHeightPixels=-1;
	public int sofImageWidthPixels=-1;
	public int sofNumberOfComponentsInTheImage=-1;
	
	public int sofComponentIdentifier1=-1;
	public int sofHorizontalSamplingFrequency1=-1; // can be 1,2,3 or 4
	public int sofVerticalSamplingFrequency1=-1; // can be 1,2,3 or 4
	public int sofQuantizationIDForCompoent1=-1; // can be 0,1,2 or 3
	
	public int sofComponentIdentifier2=-1;
	public int sofHorizontalSamplingFrequency2=-1; // can be 1,2,3 or 4
	public int sofVerticalSamplingFrequency2=-1; // can be 1,2,3 or 4
	public int sofQuantizationIDForCompoent2=-1; // can be 0,1,2 or 3

	public int sofComponentIdentifier3=-1;
	public int sofHorizontalSamplingFrequency3=-1; // can be 1,2,3 or 4
	public int sofVerticalSamplingFrequency3=-1; // can be 1,2,3 or 4
	public int sofQuantizationIDForCompoent3=-1; // can be 0,1,2 or 3
}