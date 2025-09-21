package model.huffman.tree;

import java.util.logging.Level;
import java.util.logging.Logger;

import utils.Exceptions.CannotFindHuffmanSearchString;
import model.HuffmanSearchString;

/**
 * Get the Huffmann data and create tree.
 * @author michael
 */
public class HuffmanTree extends TreeBase  {
	private static final Logger log=Logger.getLogger("HuffmanTree");
	
	private char dht16BitArray[]=null;
	private char dhtHuffmanArrayArray[][]=null;
	public HuffmanNode[][] myHuffmanNodeTree= new HuffmanNode[16][];
	public HuffmanNode root=null;
	
	public HuffmanTree(){
		super(log);
	}			
	
	/**
	 * Constructor.
	 * @param dht16BitArray
	 * @param dhtHuffmanArrayArray
	 */
	public HuffmanTree(char dht16BitArray[], char dhtHuffmanArrayArray[][]){
		super(log);		
		
		this.setDht16BitArray(dht16BitArray);
		this.setDhtHuffmanArrayArray(dhtHuffmanArrayArray);
		
		// level[16]=65536 entries
		// level[15]=32768 entries
		// level[14]=16384 entries
		// level[13]=8192 entries
		// level[12]=4096 entries
		// level[11]=2048 entries
		// level[10]=1024 entries
		// level[9]=512 entries
		// level[8]=256 entries
		// level[7]=128 entries
		// level[6]=64 entries
		// level[5]=32 entries
		// level[4]=16 entries
		// level[3]=8 entries
		// level[2]=4 entries
		// level[1]=2 entries
		for (int i=15;i>=0;i--){
			int temp=(1 << (i+1));
			log.log(Level.FINEST, "1: level["+i+"]="+temp+" entries");
			this.myHuffmanNodeTree[i]=new HuffmanNode[temp];
		}
				
		// Go through all levels 0 to 15, but 0 is always empty ...
		for (int i=15;i>=0;i--){
			int tempLength=dhtHuffmanArrayArray[i].length;
			log.log(Level.FINEST, "2: dht16BitArray - level/bits=["+i+"] code words="+tempLength);
		
			if (tempLength!=0){
				for (int j=tempLength-1;j>=0;j--){
					log.log(Level.FINEST, "3: "+Integer.toHexString(dhtHuffmanArrayArray[i][j])+" ");
					HuffmanNode myNewLeafNode=new HuffmanNode(dhtHuffmanArrayArray[i][j], i, -1, null, null);
					log.log(Level.FINEST, "4: "+myNewLeafNode.toString());
					this.insertNewCodeWordAtLevel(myNewLeafNode);
				}
			} 
		}
		log.log(Level.FINEST, "\n5: ------------------------------------------------------------------------------\n");
		//this.printHuffmannTree();
		
		// Create root node
		this.root=new HuffmanNode(-1, -1, -1, this.myHuffmanNodeTree[0][0], this.myHuffmanNodeTree[0][1]);
	}

	/**
	 * Insert a new leaf node into the tree.
	 * @param leafNode
	 */
	private void insertNewCodeWordAtLevel(HuffmanNode leafNode){		
		// Select the specific level and went through all entries from the right... 
		for (int i=this.myHuffmanNodeTree[leafNode.vertical].length-2;i>=0;i--){
			// Is it null?
			if (this.myHuffmanNodeTree[leafNode.vertical][i]==null){
				// Yes then place it there
				this.myHuffmanNodeTree[leafNode.vertical][i]=leafNode;
				leafNode.horizontal=i;
				this.checkAndCreateParentsStructure(leafNode);
				break;
			}
		}
	}
	
	/**
	 * Create the parents.
	 * @param leafNode
	 */
	private void checkAndCreateParentsStructure(HuffmanNode leafNode){
		if (leafNode.vertical==0)
			return;
		
		int parentNodeVerticalPos=leafNode.vertical-1;
		int parentNodeHorizontalPos=leafNode.horizontal/2;
		
		if (myHuffmanNodeTree[parentNodeVerticalPos][parentNodeHorizontalPos]==null){
			myHuffmanNodeTree[parentNodeVerticalPos][parentNodeHorizontalPos]=new HuffmanNode(-1, parentNodeVerticalPos, parentNodeHorizontalPos, null, null);
		}
		
		if (leafNode.horizontal%2==0){
			myHuffmanNodeTree[parentNodeVerticalPos][parentNodeHorizontalPos].zero_left=leafNode;
		}else{
			myHuffmanNodeTree[parentNodeVerticalPos][parentNodeHorizontalPos].one_right=leafNode;
		}
		
		this.checkAndCreateParentsStructure(myHuffmanNodeTree[parentNodeVerticalPos][parentNodeHorizontalPos]);
	}
		
	/**
	 * int bits[] is always 16 bit long and is filled up with -1 for unused bits.
	 * @param bits
	 * @return
	 * @throws CannotFindHuffmanSearchString 
	 */
	public int getCodeWord(HuffmanSearchString bits) throws CannotFindHuffmanSearchString{		
		HuffmanNode currentNode=this.root;
		
		for (int i=0;i<=bits.getPos()-1;i++){
			if (bits.getCurrentIntArr()[i]==-1){
				break;
			}
									
			if (bits.getCurrentIntArr()[i]==1){
				if (currentNode.one_right==null){
					log.log(Level.SEVERE, "1: ERROR currentNode.one_right==null ["+i+"]!");
					throw new CannotFindHuffmanSearchString();
				}
				currentNode=currentNode.one_right;
			}else{
				if (currentNode.zero_left==null){
					log.log(Level.SEVERE, "2: ERROR currentNode.zero_left==null ["+i+"]!");
					throw new CannotFindHuffmanSearchString();
				}				
				currentNode=currentNode.zero_left;
			}			
		}
				
		if (currentNode==null){
			log.log(Level.SEVERE, "3: ERROR currentNode==null (END OF FUNCTION)!");
			return -1;
		}
		return currentNode.codeword;
	}
	
	// ***************************************** Printing the Tree *****************************
	
	/**
	 * Print tree.
	 */
	public String toString(){
		String aReturnString="";
				
		for (int i=0;i<this.myHuffmanNodeTree.length;i++){
			for (int j=0;j<this.myHuffmanNodeTree[i].length;j++){
				if (this.myHuffmanNodeTree[i][j]!=null &&
						this.myHuffmanNodeTree[i][j].isLeaf()==true){
					aReturnString+=this.myHuffmanNodeTree[i][j].getBinSequence(this)+"=";
					aReturnString+=this.myHuffmanNodeTree[i][j].toString()+"\n";
				}
			}
		}
		
		return aReturnString;
	}

	// Getter/Setter +++++++++++++++++++++++++++++++++++
	public char[] getDht16BitArray() {
		return dht16BitArray;
	}

	public void setDht16BitArray(char dht16BitArray[]) {
		this.dht16BitArray = dht16BitArray;
	}

	public char[][] getDhtHuffmanArrayArray() {
		return dhtHuffmanArrayArray;
	}

	public void setDhtHuffmanArrayArray(char dhtHuffmanArrayArray[][]) {
		this.dhtHuffmanArrayArray = dhtHuffmanArrayArray;
	}	
}