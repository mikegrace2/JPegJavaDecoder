package model;

import java.util.logging.Level;
import java.util.logging.Logger;

import utils.enums.SubSampling;

/**
 * This is the main translation of the JPeg file into a Java data structure, this model contains the whole picture.
 * @author michael
 */
public class JPegData extends JPegBase  {
	private static final Logger log=Logger.getLogger("JPegData");
	
	/**
	 * Constructor.
	 */
	public JPegData(){
		super(log);
	}	
	
	// 0xc0 SOF0 - Start of Frame (Baseline DCT)
	public StartOfFrame startOfFrameObjects[]=new StartOfFrame[256];
	
	/**
	 * Get the first start of frame.
	 * @return
	 */
	public StartOfFrame getFirstStartOfFrame(){
		for (int i=0;i<256;i++){
			if (this.startOfFrameObjects[i]!=null)
				return this.startOfFrameObjects[i];
		}
		
		return null;
	}
	
	/**
	 * Figure out what sub sampling this current picture is using.
	 * @param inSubSampling
	 * @return
	 */
	public boolean whichSubSamplingItIs(SubSampling inSubSampling){
		if (inSubSampling==SubSampling._4_2_0){		
			if (
					this.getFirstStartOfFrame().sofHorizontalSamplingFrequency1==2 && 
					this.getFirstStartOfFrame().sofVerticalSamplingFrequency1==2 && 
					this.getFirstStartOfFrame().sofHorizontalSamplingFrequency2== 1 &&
					this.getFirstStartOfFrame().sofVerticalSamplingFrequency2 == 1 &&
					this.getFirstStartOfFrame().sofHorizontalSamplingFrequency3 ==1 &&
					this.getFirstStartOfFrame().sofVerticalSamplingFrequency3==1){
				return true;
			} else {
				return false;
			}
		}else if(inSubSampling==SubSampling._4_4_4_no_subsampling){
			if (
					this.getFirstStartOfFrame().sofHorizontalSamplingFrequency1==1 && 
					this.getFirstStartOfFrame().sofVerticalSamplingFrequency1==1 && 
					this.getFirstStartOfFrame().sofHorizontalSamplingFrequency2== 1 &&
					this.getFirstStartOfFrame().sofVerticalSamplingFrequency2 == 1 &&
					this.getFirstStartOfFrame().sofHorizontalSamplingFrequency3 ==1 &&
					this.getFirstStartOfFrame().sofVerticalSamplingFrequency3==1){
				return true;
			} else {
				return false;
			}
		}else if(inSubSampling==SubSampling._4_2_2){
			if (
					this.getFirstStartOfFrame().sofHorizontalSamplingFrequency1==1 && 
					this.getFirstStartOfFrame().sofVerticalSamplingFrequency1==2 && 
					this.getFirstStartOfFrame().sofHorizontalSamplingFrequency2== 1 &&
					this.getFirstStartOfFrame().sofVerticalSamplingFrequency2 == 1 &&
					this.getFirstStartOfFrame().sofHorizontalSamplingFrequency3 ==1 &&
					this.getFirstStartOfFrame().sofVerticalSamplingFrequency3==1){
				return true;
			} else {
				return false;
			}			
		}else{
			log.log(Level.SEVERE, ": ERROR SUBSAMPLING PROBLEM! Implement me!");
			return false;
		}
	}
	
	// 0xc4 DHT - Define Huffman Table(s)
	public HuffmanInputTable huffmantables[]=new HuffmanInputTable[256];
	public int huffmanTablesIntArray[]=null;
	
	// 0xd0 RSTn - Restart 0 to 7
	public char rstNMarker[]={0,0,0,0,0,0,0,0};
	public char rstNSize[]={0,0,0,0,0,0,0,0};
		
	// 0xd8 SOI - Start of Image
	public char soiMarker=0;
	public int soiSize=-1;
	
	// 0xd9 EOI - End of Image
	public char eoiMarker=0;
	public int eoiSize=-1;
	
	// 0xda SOS - Start of Scan
	public char sosMarker=0;
	public int sosSize=-1;
	public int sosImageDataSize=-1;
	public char sosDataArray[]=null; 
	public char sosImageDataArray[]=null;
	public BitByBitReader sosBitByBitReader=null;
	
	public int sosComponentCount=-1;
	
	public int sosComponentOneId=-1;
	public int sosComponentOneDCTable=-1;
	public int sosComponentOneACTable=-1;
	
	public int sosComponentTwoId=-1;
	public int sosComponentTwoDCTable=-1;
	public int sosComponentTwoACTable=-1;
	
	public int sosComponentThreeId=-1;
	public int sosComponentThreeDCTable=-1;
	public int sosComponentThreeACTable=-1;
	
	public int sosSpectralSelectionOne=-1;
	public int sosSpectralSelectionTwo=-1;
	public int sosSuccessiveApproximation=-1;
	
	// 0xdb DQT - Define Quantization Table(s)
	public QuantizationTable qantizationtables[]=new QuantizationTable[256];
	
	// 0xdd DRI - Define Restart Interval
	public char driMarker=0;
	public int driSize=-1;
	public char driDataArray[]=null; 
	
	// 0xe0 APP0 - Application-specific
	public char app0Marker=0;
	public int app0Size=-1;
	public char app0DataArray[]=null; 
	
	public String app0Identifier=null;
	public int app0VersionMajorId=-1;
	public int app0VersionMinorId=-1;
	public int app0Units=-1;
	public int app0XDensity=-1;
	public int app0YDensity=-1;
	public int app0XThumbnailWidth=-1;
	public int app0YThumbnailHeight=-1;
	public char[] app0ThumbnailData=null;
	
	// 0xe1 APP1 - Application-specific
	public char app1Marker=0;
	public int app1Size=-1;
	public char app1DataArray[]=null;
	
	// 0xfe COM - Comment
	public char comMarker=0;
	public int comSize=-1;
	public char comDataArray[]=null;
	
	/**
	 * Create it as decimal int array, makes it sometimes easier in debugging mode.
	 */
	public void recreateAvailableHuffmanTablesAsIntArray(){
		int count=0;

		for (int i=0;i<256;i++){
			if (this.huffmantables[i]!=null){
				log.log(Level.FINEST, "1: Huffman table ="+i);
				count++;
			}
		}
		
		int returnIntArray[]= new int[count];

		for (int i=0, j=0;i<256;i++){
			if (this.huffmantables[i]!=null){
				returnIntArray[j]=i;
				j++;
			}
		}
		
		this.huffmanTablesIntArray=returnIntArray;
	}
	
	public int[] getAvailableHuffmanTablesAsIntArray(){		
		return this.huffmanTablesIntArray;
	}	
	
	/**
	 * Create String ... this is very helpful for debugging.
	 */
	@Override
	public String toString(){
		String returnString="\n";
		
		returnString+="e0 Payload: " + this.app0Size+ " APP0 - Application-specific\n";
		returnString+=this.charArrayToHexString(this.app0DataArray)+"\n\n";
		returnString+="      app0Identifier="+this.app0Identifier+"\n";
		returnString+="  app0VersionMajorId="+this.app0VersionMajorId+"\n";
		returnString+="  app0VersionMinorId="+this.app0VersionMinorId+"\n";
		returnString+="           app0Units="+this.app0Units+"\n";
		returnString+="        app0XDensity="+this.app0XDensity+"\n";
		returnString+="        app0YDensity="+this.app0YDensity+"\n";
		returnString+=" app0XThumbnailWidth="+this.app0XThumbnailWidth+"\n";
		returnString+="app0YThumbnailHeight="+this.app0YThumbnailHeight+"\n";
		returnString+="   app0ThumbnailData="+this.charArrayToHexString(this.app0ThumbnailData)+"\n";
		returnString+="----------------------------------------------------------------------\n\n";
		
		returnString+="e1 Payload: " + this.app1Size+ " APP1 - Application-specific\n";
		returnString+=this.charArrayToHexString(this.app1DataArray)+"\n";
		returnString+="----------------------------------------------------------------------\n";
		
		// Start of Frame -------------------------------------------------------------
		for (int i=0;i<256;i++){
			if (this.startOfFrameObjects[i]!=null){
				returnString+="c"+i+" Payload: " + this.startOfFrameObjects[i].sofSize+ " SOF0 - Start of Frame (Baseline DCT)\n";
				//returnString+=this.charArrayToHexString(this.startOfFrameObjects[i].sofDataArray)+"\n";
				
				returnString+="sofSamplePrecision="+this.startOfFrameObjects[i].sofSamplePrecision+"\n";
				returnString+="sofImageHeightPixels="+this.startOfFrameObjects[i].sofImageHeightPixels+"\n";
				returnString+="sofImageWidthPixels="+this.startOfFrameObjects[i].sofImageWidthPixels+"\n";
				returnString+="sofNumberOfComponentsInTheImage="+this.startOfFrameObjects[i].sofNumberOfComponentsInTheImage+"\n";		
				
				returnString+="\n";

				returnString+="sofComponentIdentifier1="+this.startOfFrameObjects[i].sofComponentIdentifier1+"\n";
				returnString+="sofHorizontalSamplingFrequency1="+this.startOfFrameObjects[i].sofHorizontalSamplingFrequency1+"\n";
				returnString+="sofVerticalSamplingFrequency1="+this.startOfFrameObjects[i].sofVerticalSamplingFrequency1+"\n";
				returnString+="sofQuantizationIDForCompoent1="+this.startOfFrameObjects[i].sofQuantizationIDForCompoent1+"\n";
				
				returnString+="\n";

				returnString+="sofComponentIdentifier2="+this.startOfFrameObjects[i].sofComponentIdentifier2+"\n";
				returnString+="sofHorizontalSamplingFrequency2="+this.startOfFrameObjects[i].sofHorizontalSamplingFrequency2+"\n";
				returnString+="sofVerticalSamplingFrequency2="+this.startOfFrameObjects[i].sofVerticalSamplingFrequency2+"\n";
				returnString+="sofQuantizationIDForCompoent2="+this.startOfFrameObjects[i].sofQuantizationIDForCompoent2+"\n";
				
				returnString+="\n";

				returnString+="sofComponentIdentifier3="+this.startOfFrameObjects[i].sofComponentIdentifier3+"\n";
				returnString+="sofHorizontalSamplingFrequency3="+this.startOfFrameObjects[i].sofHorizontalSamplingFrequency3+"\n";
				returnString+="sofVerticalSamplingFrequency3="+this.startOfFrameObjects[i].sofVerticalSamplingFrequency3+"\n";
				returnString+="sofQuantizationIDForCompoent3="+this.startOfFrameObjects[i].sofQuantizationIDForCompoent3+"\n";	
				
				returnString+="----------------------------------------------------------------------\n";
			}
		}
		
		returnString+="dd Payload: 4 Bytes " + this.driSize+ " DRI - Define Restart Interval\n";
		returnString+=this.charArrayToHexString(this.driDataArray)+"\n";
		returnString+="----------------------------------------------------------------------\n";
				
		returnString+="fe Payload: " + this.comSize+ " COM - Comment\n";
		returnString+=this.charArrayToHexString(this.comDataArray)+"\n";
		returnString+="----------------------------------------------------------------------\n";
		
		for (int i=0;i<=7;i++){
			returnString+="d"+i+" Payload: NONE " + this.rstNSize[i]+ " RST"+i+" - Restart\n";
			returnString+="----------------------------------------------------------------------\n";
		}
		
		returnString+="d8 Payload: NONE " + this.soiSize+ " SOI - Start of Image\n";
		returnString+="----------------------------------------------------------------------\n";

		returnString+="d9 Payload: NONE " + this.eoiSize+ " EOI - End of Image\n";
		returnString+="----------------------------------------------------------------------\n";

		// Quantization tables -----------------------------------------------------------------------
		for (int i=0;i<256;i++){
			if (this.qantizationtables[i]!=null){
				returnString+=this.qantizationtables[i].toString()+"\n";
			}
		}
		
		// Huffman tables ---------------------------------------------------------------
		for (int i=0;i<256;i++){
			if (this.huffmantables[i]!=null){
				returnString+="Huffmantables["+i+"]\n";
				returnString+=this.huffmantables[i].toString()+"\n";
				returnString+=this.huffmantables[i].dhtHuffmanTree.toString()+"\n";
			}
		}
				
		returnString+="da Payload: " + this.sosSize+ " SOS - Start of Scan\n";
		returnString+=""+this.charArrayToHexString(this.sosDataArray)+"\n\n";
		
		returnString+="         sosComponentCount="+this.sosComponentCount+"\n\n";

		returnString+="         sosComponentOneId="+this.sosComponentOneId+"\n";
		returnString+="    sosComponentOneDCTable="+this.sosComponentOneDCTable+"\n";
		returnString+="    sosComponentOneACTable="+this.sosComponentOneACTable+"\n\n";
		
		returnString+="         sosComponentTwoId="+this.sosComponentTwoId+"\n";
		returnString+="    sosComponentTwoDCTable="+this.sosComponentTwoDCTable+"\n";
		returnString+="    sosComponentTwoACTable="+this.sosComponentTwoACTable+"\n\n";
		
		returnString+="       sosComponentThreeId="+this.sosComponentThreeId+"\n";
		returnString+="  sosComponentThreeDCTable="+this.sosComponentThreeDCTable+"\n";
		returnString+="  sosComponentThreeACTable="+this.sosComponentThreeACTable+"\n\n";
		
		returnString+="   sosSpectralSelectionOne="+this.sosSpectralSelectionOne+"\n";
		returnString+="   sosSpectralSelectionTwo="+this.sosSpectralSelectionTwo+"\n";
		returnString+="sosSuccessiveApproximation="+this.sosSuccessiveApproximation+"\n\n";
		
		returnString+="Image Data Size = "+this.sosImageDataSize+"\n";
		returnString+=this.charArrayToHexString(sosImageDataArray)+"\n";
		returnString+="----------------------------------------------------------------------\n";

		return returnString;
	}
}