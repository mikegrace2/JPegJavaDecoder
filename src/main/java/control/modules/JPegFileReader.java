package control.modules;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import model.BitByBitReader;
import model.HuffmanInputTable;
import model.JPegData;
import model.QuantizationTable;
import model.StartOfFrame;
import model.MCU.TransformBlock;
import model.huffman.tree.HuffmanTree;

/**
 * This class is responsible for the reading of a JPeg file. It decodes the format and save a representation into data strucktures.
 * The main output data struckture is JPegData.
 * @author michael
 */
public class JPegFileReader extends ModulesBase{
	private static final Logger log=Logger.getLogger("JpegFileReader");
	
	/**
	 * Constructor.
	 */
	public JPegFileReader(){
		super(log);
	}
	
	/**
	 * Read JPeg file from disk.
	 * @param filename
	 */
	public JPegData readJPeg(String filename) {
		FileInputStream in = null;
		JPegData myJPegData=new JPegData();
		int marker = 0;		
		
		try {
			in = new FileInputStream(filename);

			while ((marker = in.read()) != -1) {
				if (marker == 0xff) {
					marker = (char)in.read();
					switch (marker) {
					case 0x00: // FF with 00 following is an escape sequence
						log.log(Level.FINEST, "1: 00 Payload: NONE"+"\t\tFF with 00 following is an escape sequence");
						break;
					case 0xc0: // SOF0 - Start of Frame (Baseline DCT)
						this.getSof0(in, myJPegData, (char)marker);
						break;
					case 0xc2: // SOF2 - Start of Frame (Progressive DCT)
						this.getSof2(in, myJPegData, (char)marker);
						break;
					case 0xc4: // DHT - Define Huffman Table(s)
						this.getDht(in, myJPegData, (char)marker);
						break;
					case 0xd0: // RST0 - Restart
						this.getRstN(in, myJPegData, (char)marker, 0);
						break;
					case 0xd1: // RST1 - Restart
						this.getRstN(in, myJPegData, (char)marker, 1);
						break;
					case 0xd2: // RST2 - Restart
						this.getRstN(in, myJPegData, (char)marker, 2);						
						break;
					case 0xd3: // RST3 - Restart
						this.getRstN(in, myJPegData, (char)marker, 3);
						break;
					case 0xd4: // RST4 - Restart
						this.getRstN(in, myJPegData, (char)marker, 4);
						break;
					case 0xd5: // RST5 - Restart
						this.getRstN(in, myJPegData, (char)marker, 5);
						break;
					case 0xd6: // RST6 - Restart
						this.getRstN(in, myJPegData, (char)marker, 6);
						break;
					case 0xd7: // RST7 - Restart
						this.getRstN(in, myJPegData, (char)marker, 7);
						break;
					case 0xd8: // SOI - Start of Image
						this.getSoi(in, myJPegData, (char)marker);						
						break;
					case 0xd9: // EOI - End of Image
						this.getEoi(in, myJPegData, (char)marker);
						break;
					case 0xda: // SOS - Start of Scan
						this.getSos(in, myJPegData, (char)marker);						
						break;
					case 0xdb: // DQT - Define Quantization Table(s)
						this.getDqt(in, myJPegData, (char)marker);						
						break;
					case 0xdd: // DRI - Define Restart Interval
						this.getDri(in, myJPegData, (char)marker);						
						break;
					case 0xe0: // APP0 - Application-specific
						this.getApp0(in, myJPegData, (char)marker);						
						break;
					case 0xe1: // APP1 - Application-specific
						this.getApp1(in, myJPegData, (char)marker);						
						break;
					case 0xec: // What is this
						log.log(Level.SEVERE, "2: EC IS UNKNOWN!");
						this.getUnknownAndDiscard(in, myJPegData, (char)marker);
						break;
					case 0xee: // What is this
						log.log(Level.SEVERE, "3: EE IS UNKNOWN!");
						this.getUnknownAndDiscard(in, myJPegData, (char)marker);
						break;												
					case 0xfe: // COM - Comment
						this.getCom(in, myJPegData, (char)marker);						
						break;
					default:
						log.log(Level.SEVERE, "4: ERROR ERROR it is |"+ Integer.toHexString(marker)+"|");
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}	
		
		return myJPegData;
	}
	
	/**
	 * 0xc0 SOF0 - Start of Frame (Baseline DCT)
	 * @param inStream
	 * @param jdata
	 * @param marker
	 */
	private void getSof0(FileInputStream inStream, JPegData jdata, char marker){
		jdata.startOfFrameObjects[0]=new StartOfFrame();
		jdata.startOfFrameObjects[0].sofMarker=marker;
		jdata.startOfFrameObjects[0].sofSize = this.getSize(inStream)-2;
		jdata.startOfFrameObjects[0].sofDataArray = this.getNBytes(inStream, jdata.startOfFrameObjects[0].sofSize);
		
		this.extractSofData(0, jdata);
	}

	/**
	 * 0xc2 SOF2 - Start of Frame (Progressive DCT)
	 * @param inStream
	 * @param jdata
	 * @param marker
	 */
	private void getSof2(FileInputStream inStream, JPegData jdata, char marker){
		jdata.startOfFrameObjects[2]=new StartOfFrame();
		jdata.startOfFrameObjects[2].sofMarker=marker;
		jdata.startOfFrameObjects[2].sofSize= this.getSize(inStream)-2;
		jdata.startOfFrameObjects[2].sofDataArray = this.getNBytes(inStream, jdata.startOfFrameObjects[2].sofSize);
		
		this.extractSofData(2, jdata);
	}
	
	/**
	 * Get SOF data.
	 * @param number
	 * @param jdata
	 */
	private void extractSofData(int number, JPegData jdata){		
		jdata.startOfFrameObjects[number].sofSamplePrecision=new Integer(jdata.startOfFrameObjects[number].sofDataArray[0]);
		int tempByteOne=new Integer(jdata.startOfFrameObjects[number].sofDataArray[1]);
		int tempByteTwo=new Integer(jdata.startOfFrameObjects[number].sofDataArray[2]);
		jdata.startOfFrameObjects[number].sofImageHeightPixels=(tempByteOne*256)+tempByteTwo;
		tempByteOne=new Integer(jdata.startOfFrameObjects[number].sofDataArray[3]);
		tempByteTwo=new Integer(jdata.startOfFrameObjects[number].sofDataArray[4]);
		jdata.startOfFrameObjects[number].sofImageWidthPixels=(tempByteOne*256)+tempByteTwo;
		jdata.startOfFrameObjects[number].sofNumberOfComponentsInTheImage=new Integer(jdata.startOfFrameObjects[number].sofDataArray[5]);
		
		jdata.startOfFrameObjects[number].sofComponentIdentifier1=new Integer(jdata.startOfFrameObjects[number].sofDataArray[6]);
		jdata.startOfFrameObjects[number].sofHorizontalSamplingFrequency1=(new Integer(jdata.startOfFrameObjects[number].sofDataArray[7])&240) >> 4;
		jdata.startOfFrameObjects[number].sofVerticalSamplingFrequency1=new Integer(jdata.startOfFrameObjects[number].sofDataArray[7])&15;
		jdata.startOfFrameObjects[number].sofQuantizationIDForCompoent1=new Integer(jdata.startOfFrameObjects[number].sofDataArray[8]);
		
		jdata.startOfFrameObjects[number].sofComponentIdentifier2=new Integer(jdata.startOfFrameObjects[number].sofDataArray[9]);
		jdata.startOfFrameObjects[number].sofHorizontalSamplingFrequency2=(new Integer(jdata.startOfFrameObjects[number].sofDataArray[10])&240) >> 4;
		jdata.startOfFrameObjects[number].sofVerticalSamplingFrequency2=new Integer(jdata.startOfFrameObjects[number].sofDataArray[10])&15;
		jdata.startOfFrameObjects[number].sofQuantizationIDForCompoent2=new Integer(jdata.startOfFrameObjects[number].sofDataArray[11]);
				
		jdata.startOfFrameObjects[number].sofComponentIdentifier3=new Integer(jdata.startOfFrameObjects[number].sofDataArray[12]);
		jdata.startOfFrameObjects[number].sofHorizontalSamplingFrequency3=(new Integer(jdata.startOfFrameObjects[number].sofDataArray[13])&240) >> 4;
		jdata.startOfFrameObjects[number].sofVerticalSamplingFrequency3=new Integer(jdata.startOfFrameObjects[number].sofDataArray[13])&15;
		jdata.startOfFrameObjects[number].sofQuantizationIDForCompoent3=new Integer(jdata.startOfFrameObjects[number].sofDataArray[14]);		
	}

	/**
	 * 0xc4 DHT - Define Huffman Table(s)
	 * 
	 * It is possible that all four tables are saved in one marker area
	 * @param inStream
	 * @param jdata
	 * @param marker
	 */
	private void getDht(FileInputStream inStream, JPegData jdata, char marker){
		// Bytes to go for the Huffman tables
		int alreadyReadBytes=0;
		
		// How many bytes are there
		int tempSize=this.getSize(inStream)-2;
		alreadyReadBytes=alreadyReadBytes+2;
		log.log(Level.FINEST, "1: tempSize="+tempSize);
		
		do {
			// Get ID
			int tempId=this.getId(inStream);
			alreadyReadBytes=alreadyReadBytes+1;
			tempSize=tempSize-1;
			alreadyReadBytes=alreadyReadBytes+1;
		
			// Get Data
			jdata.huffmantables[tempId]=new HuffmanInputTable();
			jdata.huffmantables[tempId].dhtID=(char)tempId;
			jdata.huffmantables[tempId].dhtClass=(tempId&15);
			jdata.huffmantables[tempId].dhtTableId=(tempId&240)>>4;
			
			log.log(Level.FINEST, "2: class="+jdata.huffmantables[tempId].dhtClass);
			log.log(Level.FINEST, "3: tableID="+jdata.huffmantables[tempId].dhtTableId);
			
			jdata.huffmantables[tempId].dhtMarker=marker;				
			jdata.huffmantables[tempId].dhtSize=tempSize;
			jdata.huffmantables[tempId].dht16BitArray=this.getNBytes(inStream, 16);
			alreadyReadBytes=alreadyReadBytes+16;
		
			alreadyReadBytes=alreadyReadBytes+this.getHuffmanDataAndCreateTree(inStream, jdata, tempId);
			
			jdata.recreateAvailableHuffmanTablesAsIntArray();			
		}while(alreadyReadBytes<tempSize);
	}
	
	/**
	 * Get the Huffmann data and create a tree representation.
	 * @param inStream
	 * @param jdata
	 * @param id
	 */
	private int getHuffmanDataAndCreateTree(FileInputStream inStream, JPegData jdata, int id){
		int byteReadCount=0;
		int lengthOfArray=jdata.huffmantables[id].dht16BitArray.length;
		jdata.huffmantables[id].dhtHuffmanArrayArray = new char[lengthOfArray][];

		log.log(Level.FINEST, ": lengthOfArray="+lengthOfArray);
		for (int i = 0; i < lengthOfArray; i++) {
			int count=jdata.huffmantables[id].dht16BitArray[i];
			jdata.huffmantables[id].dhtHuffmanArrayArray[i]=this.getNBytes(inStream, count);
			byteReadCount=byteReadCount+count;
		}
		
		jdata.huffmantables[id].dhtHuffmanTree=new HuffmanTree(jdata.huffmantables[id].dht16BitArray, jdata.huffmantables[id].dhtHuffmanArrayArray);
		
		return byteReadCount;
	}

	/**
	 * 0xd0 RST0 - Restart
	 * @param inStream
	 * @param jdata
	 * @param marker
	 * @param N
	 */
	private void getRstN(FileInputStream inStream, JPegData jdata, char marker, int N){
		jdata.rstNMarker[N]=marker;
		jdata.rstNSize[N] = 0;
	}

	/**
	 * 0xd8 SOI - Start of Image
	 * @param inStream
	 * @param jdata
	 * @param marker
	 */
	private void getSoi(FileInputStream inStream, JPegData jdata, char marker){
		jdata.soiMarker=marker;
		jdata.soiSize = 0;
	}

	/**
	 * 0xd9 EOI - End of Image
	 * @param inStream
	 * @param jdata
	 * @param marker
	 */
	private void getEoi(FileInputStream inStream, JPegData jdata, char marker){
		jdata.eoiMarker=marker;
		jdata.eoiSize=0;
	}

	/**
	 * 0xda SOS - Start of Scan
	 * @param inStream
	 * @param jdata
	 * @param marker
	 */
	private void getSos(FileInputStream inStream, JPegData jdata, char marker){
		jdata.sosMarker=marker;
		jdata.sosSize=this.getSize(inStream)-2;
		jdata.sosDataArray = this.getNBytes(inStream, jdata.sosSize);
		jdata.sosImageDataArray=this.getImageData(inStream);
		jdata.sosBitByBitReader=new BitByBitReader(jdata.sosImageDataArray);
		jdata.sosImageDataSize=jdata.sosImageDataArray.length;
		
		// get the details out of the data
		jdata.sosComponentCount=jdata.sosDataArray[0];
		
		jdata.sosComponentOneId=jdata.sosDataArray[1];
		jdata.sosComponentOneDCTable=(jdata.sosDataArray[2]&15);
		jdata.sosComponentOneACTable=(jdata.sosDataArray[2]&240)>>4;
		
		jdata.sosComponentTwoId=jdata.sosDataArray[3];
		jdata.sosComponentTwoDCTable=(jdata.sosDataArray[4]&15);
		jdata.sosComponentTwoACTable=(jdata.sosDataArray[4]&240)>>4;
		
		jdata.sosComponentThreeId=jdata.sosDataArray[5];
		jdata.sosComponentThreeDCTable=(jdata.sosDataArray[6]&15);
		jdata.sosComponentThreeACTable=(jdata.sosDataArray[6]&240)>>4;
		
		jdata.sosSpectralSelectionOne=jdata.sosDataArray[7];
		jdata.sosSpectralSelectionTwo=jdata.sosDataArray[8];
		jdata.sosSuccessiveApproximation=jdata.sosDataArray[9];
	}
	
	/**
	 * Get the image data.
	 * @param inStream
	 * @return
	 */
	private char[] getImageData(FileInputStream inStream){
		int size=65535;
		int i=0;
		char[] tempImageArray=new char[size];
		
		char nextByte=0;
		try {
			for (i=0;(nextByte = (char)inStream.read()) != -1;) {
				if (nextByte==0xFF){
					nextByte=(char)inStream.read();
					if (nextByte==0x00){
						tempImageArray[i++]=0xFF;
					}else{
						break;
					}
				}else{
					tempImageArray[i++]=nextByte;
				}
			}
			
			char[] returnImageArray=new char[i];
			
			for (int j=0;j<i;j++){
				returnImageArray[j]=tempImageArray[j];
			}
			
			log.log(Level.FINEST, "\n1: image data size="+returnImageArray.length);
			//this.printCharImageBlock(returnImageArray);
			
			return returnImageArray;
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return new char[0];
	}

	/**
	 * Un Zig Zag the quantization table
	 * @param input
	 * @return
	 */
	private int[][] unZigZag(int input[][]){
		int retArrArr[][]=new int[TransformBlock.DIM_8TIMES8][TransformBlock.DIM_8TIMES8];
		Quantizizer myQuantizizer=new Quantizizer();
				
		int i=0;
		for (int x=0;x<TransformBlock.DIM_8TIMES8;x++){
			for (int y=0;y<TransformBlock.DIM_8TIMES8;y++){
				int nextX=myQuantizizer.zigZag8times8[i][0];
				int nextY=myQuantizizer.zigZag8times8[i][1];
				retArrArr[nextX][nextY]=input[x][y];
				i++;
			}
		}
			
		return retArrArr;
	}
	
	/**
	 * 0xdb DQT - Define Quantization Table(s)
	 * 
	 * It is possible that there are two markers in the file with FFDA which contains each one table,
	 * 
	 * But it is also possible that both tables are encoded in one stream with only one FFDA section in the jpeg file.
	 * Only detectable if the tempSize is 65 which means one section or 130 means two sections.
	 * 
	 * @param inStream
	 * @param jdata
	 * @param marker
	 */
	private void getDqt(FileInputStream inStream, JPegData jdata, char marker){
		int tempSize=this.getSize(inStream)-2;
		log.log(Level.FINEST, "A: tempSize="+tempSize);
		if (tempSize==65){
			int tempId=this.getId(inStream);
			jdata.qantizationtables[tempId]=new QuantizationTable();
			jdata.qantizationtables[tempId].dqtMarker=marker;
			jdata.qantizationtables[tempId].dqtSize=tempSize;
			jdata.qantizationtables[tempId].dqtID=(char)tempId;
			
			jdata.qantizationtables[tempId].dqtQuantValueSize=(tempId&240)>>4;
			jdata.qantizationtables[tempId].dqtTableIdentifier=(tempId&15);
			
			jdata.qantizationtables[tempId].dqtDataArrayArray = this.unZigZag(this.getNNBytes(inStream, tempSize-1));
		} else if (tempSize==130){
			int tempId1=this.getId(inStream);
			jdata.qantizationtables[tempId1]=new QuantizationTable();
			jdata.qantizationtables[tempId1].dqtMarker=marker;
			jdata.qantizationtables[tempId1].dqtSize=tempSize;
			jdata.qantizationtables[tempId1].dqtID=(char)tempId1;
			
			jdata.qantizationtables[tempId1].dqtQuantValueSize=(tempId1&240)>>4;
			jdata.qantizationtables[tempId1].dqtTableIdentifier=(tempId1&15);
			
			jdata.qantizationtables[tempId1].dqtDataArrayArray = this.unZigZag(this.getNNBytes(inStream, tempSize-66));
			//jdata.qantizationtables[tempId1].dqtDataArrayArray = this.getNNBytes(inStream, tempSize-66);

			int tempId2=this.getId(inStream);
			jdata.qantizationtables[tempId2]=new QuantizationTable();
			jdata.qantizationtables[tempId2].dqtMarker=marker;
			jdata.qantizationtables[tempId2].dqtSize=tempSize;
			jdata.qantizationtables[tempId2].dqtID=(char)tempId2;
			
			jdata.qantizationtables[tempId2].dqtQuantValueSize=(tempId2&240)>>4;
			jdata.qantizationtables[tempId2].dqtTableIdentifier=(tempId2&15);
			
			jdata.qantizationtables[tempId2].dqtDataArrayArray = this.unZigZag(this.getNNBytes(inStream, tempSize-66));
		} else{
			log.log(Level.SEVERE, "B: ERROR tempSize is not 65 or 130 it is tempSize="+tempSize);
		}
	}
	
	/**
	 * 0xdd DRI - Define Restart Interval
	 * @param inStream
	 * @param jdata
	 * @param marker
	 */
	private void getDri(FileInputStream inStream, JPegData jdata, char marker){
		jdata.driMarker=marker;
		jdata.driSize=2;
		jdata.driDataArray = this.getNBytes(inStream, jdata.driSize);
	}

	/**
	 * 0xe0 APP0 - Application-specific
	 * @param inStream
	 * @param jdata
	 * @param marker
	 */
	private void getApp0(FileInputStream inStream, JPegData jdata, char marker){
		jdata.app0Marker=marker;
		jdata.app0Size = this.getSize(inStream)-2;
		jdata.app0DataArray = this.getNBytes(inStream, jdata.app0Size);
		
		jdata.app0Identifier=""+jdata.app0DataArray[0]+jdata.app0DataArray[1]+jdata.app0DataArray[2]+jdata.app0DataArray[3]+jdata.app0DataArray[4];
		jdata.app0VersionMajorId=jdata.app0DataArray[5];
		jdata.app0VersionMinorId=jdata.app0DataArray[6];
		jdata.app0Units=jdata.app0DataArray[7];
		jdata.app0XDensity=(((int)jdata.app0DataArray[8])*256)+((int)jdata.app0DataArray[9]);
		jdata.app0YDensity=(((int)jdata.app0DataArray[10])*256)+((int)jdata.app0DataArray[11]);
		jdata.app0XThumbnailWidth=jdata.app0DataArray[12];
		jdata.app0YThumbnailHeight=jdata.app0DataArray[13];
		
		if (jdata.app0XThumbnailWidth>0 && jdata.app0YThumbnailHeight>0 ){
			log.log(Level.SEVERE, ": TODO ERROR THUMBNAIL IS INCLUDED IMPLEMENT ME NOW!");
			jdata.app0ThumbnailData=new char[0];
		} else {
			jdata.app0ThumbnailData=new char[0];
		}
	}

	/**
	 * 0xe1 APP1 - Application-specific
	 * @param inStream
	 * @param jdata
	 * @param marker
	 */
	private void getApp1(FileInputStream inStream, JPegData jdata, char marker){
		jdata.app1Marker=marker;
		jdata.app1Size=this.getSize(inStream)-2;
		jdata.app1DataArray = this.getNBytes(inStream, jdata.app1Size);
	}

	/**
	 * 0xfe COM - Comment
	 * @param inStream
	 * @param jdata
	 * @param marker
	 */
	private void getCom(FileInputStream inStream, JPegData jdata, char marker){
		jdata.comMarker=marker;
		jdata.comSize=this.getSize(inStream)-2;
		jdata.comDataArray = this.getNBytes(inStream, jdata.comSize);
	}
	
	/**
	 * 0xfe COM - Comment
	 * @param inStream
	 * @param jdata
	 * @param marker
	 */
	private void getUnknownAndDiscard(FileInputStream inStream, JPegData jdata, char marker){
		int unknownSize=this.getSize(inStream)-2;
		this.getNBytes(inStream, unknownSize);
	}		
	
	/**
	 * Get size (2 bytes)
	 * @param inStream
	 * @return
	 */
	private int getSize(FileInputStream inStream) {
		int byte1 = -1;
		int byte2 = -1;

		try {
			byte1 = inStream.read();
			byte2 = inStream.read();
			return byte1 * 256 + byte2;
		} catch (IOException e) {
			e.printStackTrace();
			return -1;
		}
	}

	/**
	 * Get the id from the stream
	 * @param input
	 * @return
	 */
	private int getId(FileInputStream input) {
		int intByte = -1;

		try {
			intByte = input.read();
			return intByte;
		} catch (IOException e) {
			e.printStackTrace();
			return intByte;
		}
	}

	/**
	 * get N bytes from the stream.
	 * @param input
	 * @param count
	 * @return
	 */
	private char[] getNBytes(FileInputStream input, int count) {
		char[] returnArray = new char[count];

		try {
			for (int i = 0; i < count; i++)
				returnArray[i] = (char)input.read();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return returnArray;
	}
	
	/**
	 * Useful for the quantization table which is 8 * 8 always
	 * @param input
	 * @param count
	 * @return
	 */
	private int[][] getNNBytes(FileInputStream input, int count) {
		double dimensions=Math.sqrt(count);
		int ceilDimensions=(int)Math.ceil(dimensions);
		int[][] returnArrayArray = new int[ceilDimensions][ceilDimensions];
		char[] tempArray=this.getNBytes(input, count);
		
		int i = 0, j=-1;
		try{
			for (; i < count; i++) {
				if (i%TransformBlock.DIM_8TIMES8==0){
					j++;
				}			
				returnArrayArray[j][i%TransformBlock.DIM_8TIMES8] = tempArray[i];
			}
		} catch (ArrayIndexOutOfBoundsException ex){
			log.log(Level.SEVERE, "1: ArrayIndexOutOfBoundsException dimensions="+dimensions);
			log.log(Level.SEVERE, "2: ArrayIndexOutOfBoundsException ceilDimensions="+ceilDimensions);
			log.log(Level.SEVERE, "3: ArrayIndexOutOfBoundsException count="+count);
			log.log(Level.SEVERE, "4: ArrayIndexOutOfBoundsException i="+i+" j="+j);
			//ex.printStackTrace();
			throw new ArrayIndexOutOfBoundsException();
		}
		
		return returnArrayArray;
	}
}